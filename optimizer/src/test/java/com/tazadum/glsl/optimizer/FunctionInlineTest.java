package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.cli.builder.PreprocessorExecutor;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.language.function.FunctionRegistry;
import com.tazadum.glsl.language.model.StorageQualifier;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.TypeQualifierList;
import com.tazadum.glsl.language.variable.VariableRegistry;
import com.tazadum.glsl.language.variable.VariableRegistryContext;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.parser.functions.ConstructorsFunctionSet;
import com.tazadum.glsl.preprocessor.Preprocessor;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;
import com.tazadum.slf4j.TLogConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.event.Level;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static com.tazadum.glsl.language.type.PredefinedType.*;
import static com.tazadum.glsl.optimizer.RefCheck.*;
import static com.tazadum.glsl.util.SourcePosition.TOP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Created by Erik on 2016-10-20.
 */
public class FunctionInlineTest extends BaseOptimizerTest {
    @Override
    protected OptimizerType[] getOptimizerTypes() {
        return new OptimizerType[]{OptimizerType.DeadCodeEliminationType, OptimizerType.FunctionInline};
    }

    @BeforeEach
    void setup() {
        testInit(5, true);
    }

    @ParameterizedTest(name = "case: {1}")
    @DisplayName("Optimizations that should work")
    @MethodSource("getPositiveCases")
    void testOptimizerPositive(String expected, String source, List<RefCheck> checks) {
        GLSLContext context = parserContext.currentContext();
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(FLOAT), "fv", null, null, null));
        registry.declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(INT), "iv", null, null, null));

        Node node = optimize(source);
        sourceEquals(expected, toString(node));

        RefCheck.runChecks(checks, parserContext);
    }

    private static Arguments[] getPositiveCases() {
        return new Arguments[]{
                Arguments.of( // single line void function
                        "void main(){iv+=1;}",
                        "void f(int b){ iv+=b; } void main(){ f(1); }",
                        list(
                                function("f", 0),
                                variable("b", 0),
                                variable("iv", 1)
                        )),
                Arguments.of( // single line void function with parameter mutation
                        "void main(){int _gen0=1;iv+=++_gen0;}",
                        "void f(int b){ iv+=++b; } void main(){ f(1); }",
                        list(
                                function("f", 0),
                                variable("b", 0),
                                variable("_gen0", 1),
                                variable("iv", 1)
                        )),
                Arguments.of( // single line function
                        "void main(){iv=2*3;}",
                        "int f(int b){ return 2*b; } void main(){ iv=f(3); }",
                        list(
                                function("f", 0),
                                variable("b", 0),
                                variable("iv", 1)
                        )),
                Arguments.of( // single line function with parameter mutation
                        "void main(){int _gen0=3;iv=2*_gen0++;}",
                        "int f(int b){ return 2*b++; } void main(){ iv=f(3); }",
                        list(
                                function("f", 0),
                                variable("b", 0),
                                variable("_gen0", 1),
                                variable("iv", 1)
                        )),
                Arguments.of( // single line function with global mutation
                        "int a=0;void main(){int b=a,c=(3+a++);iv=b+c;}",
                        "int a=0; int f(int b){ return b+a++; } void main(){ int b=a,c=f(3); iv=b+c; }",
                        list(
                                function("f", 0),
                                variable("a", 2),
                                variable("b", 1),
                                variable("c", 1),
                                variable("iv", 1)
                        )),
                Arguments.of( // multi line void function
                        "void main(){iv+=1;fv++;}",
                        "void f(int b){ iv+=b;fv++; } void main(){ f(1); }",
                        list(
                                function("f", 0),
                                variable("iv", 1),
                                variable("fv", 1)
                        )),
                Arguments.of( // multi line function
                        "void main(){int _gen0=1;for(int _gen1=0;_gen1<2;_gen1++)_gen0*=1;iv=_gen0;}",
                        "int f(int a,int b){ int c=1;for(int i=0;i<b;i++){c*=a;} return c; } void main(){ iv=f(1,2); }",
                        list(
                                function("f", 0),
                                variable("iv", 1),
                                variable("_gen0", 2),
                                variable("_gen1", 2)
                        )),
                Arguments.of( // multi line function 2
                        "void main(){int _gen0=1;for(int _gen1=0;_gen1<2;_gen1++)if(_gen1>2)_gen0*=1;iv=_gen0;}",
                        "int f(int a,int b){ int c=1;for(int i=0;i<b;i++){ if(i>2) c*=a;} return c; } void main(){ iv=f(1,2); }",
                        list(
                                function("f", 0),
                                variable("iv", 1),
                                variable("_gen0", 2),
                                variable("_gen1", 3),
                                variable("b", 0)
                        )),
                Arguments.of( // multi line void function with parameter mutation
                        "void main(){int _gen0=1;iv+=++_gen0;fv++;}",
                        "void f(int b){ iv+=++b;fv++; } void main(){ f(1); }",
                        list(
                                function("f", 0),
                                variable("iv", 1),
                                variable("fv", 1),
                                variable("b", 0)
                        )),
                Arguments.of( // multi line function that mutates it's parameter
                        "void main(){if(iv.x>0){int _gen0=3;_gen0+=1;iv=_gen0;}}",
                        "int f(int a){ a+=1; return a; } void main(){ if(iv.x>0) iv=f(3); }",
                        list(
                                function("f", 0),
                                variable("iv", 2),
                                variable("_gen0", 2)
                        )),
                Arguments.of( // multi line function that mutates it's parameter
                        "void main(){int _gen0=1+1;_gen0+=1;iv=_gen0;}",
                        "int f(int a){ a+=1; return a; } void main(){ iv=f(1+1); }",
                        list(
                                function("f", 0),
                                variable("iv", 1),
                                variable("_gen0", 2)
                        )),
                Arguments.of(
                        "void main(){float b=1;float _gen0=(2+b)+1;fv=_gen0;}",
                        "float f(float a){ float c=a+1; return c; } void main(){ float b=1;fv=f(2+b); }",
                        list(
                                function("f", 0),
                                variable("b", 1),
                                variable("fv", 1),
                                variable("_gen0", 1)
                        )),
                Arguments.of( // declaration of dependent variable 'b' hinders inline
                        "void main(){float b=1;float _gen0=b;float d=_gen0;fv=d;}",
                        "float F(float a){ float c=a; return c; } void main(){ float b=1,d=F(b);fv=d; }",
                        list(
                                function("F", 0),
                                variable("a", 0),
                                variable("b", 1),
                                variable("c", 0),
                                variable("d", 1),
                                variable("fv", 1),
                                variable("_gen0", 1)
                        )),
                Arguments.of(
                        "void main(){fv=2;}",
                        "float func(float a){ return a; } void main(){ fv=func(2); }",
                        list(
                                function("func", 0),
                                variable("a", 0),
                                variable("fv", 1)
                        )),
                Arguments.of(
                        "void main(){fv=2*2;}",
                        "float func(float a){ return 2*a; } void main(){ fv=func(2); }",
                        list(
                                function("func", 0),
                                variable("a", 0),
                                variable("fv", 1)
                        )),
                Arguments.of(
                        "void main(){float b=1;fv=2*(2+b);}",
                        "float func(float a){ return 2*a; } void main(){ float b=1;fv=func(2+b); }",
                        list(
                                function("func", 0),
                                variable("a", 0),
                                variable("b", 1),
                                variable("fv", 1)
                        )),
                Arguments.of(
                        "mat4 M;void main(){fv=M[1].x;}",
                        "mat4 M;float col(int i){ return M[i]; } void main(){ fv=col(1).x; }",
                        list(
                                function("col", 0),
                                variable("M", 1),
                                variable("fv", 1),
                                variable("i", 0)
                        ))
        };
    }

    @Test
    void testOptimizerReferences() {
        TLogConfiguration.get().useGlobalConfiguration();
        TLogConfiguration.get().getConfig().setLogLevel(Level.TRACE);

        GLSLContext context = parserContext.currentContext();
        outputConfig = outputConfig.edit().renderNewLines(true).indentation(3).build();

        // add constructors
        BuiltInFunctionRegistry builtInRegistry = parserContext.getFunctionRegistry().getBuiltInFunctionRegistry();
        new ConstructorsFunctionSet().generate(builtInRegistry, GLSLProfile.COMPATIBILITY);

        parserContext.getVariableRegistry()
                .declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(VEC3), "gl_FragColor", null, null, null));

        String source = "uniform float time;\n" +
                "float tiny(vec3 T) { return T.x; }\n" +
                "float small(vec3 S, float Sa) { vec3 Sq=Sa*S; return Sq; }\n" +
                "vec3 medium(in vec3 M) {\n" +
                "    float Mc = time * tiny(M);\n" +
                "    Mc += tiny(M);\n" +
                "    Mc += tiny(M);\n" +
                "    Mc += small(M, Mc);\n" +
                "    return Mc*M;\n" +
                "}\n" +
                "void main() {\n" +
                "    gl_FragColor = medium(vec3(1,0,0));\n" +
                "}";

        Branch result = optimizeBranch(source);
        VariableRegistry variableRegistry = result.getContext().getVariableRegistry();
        FunctionRegistry functionRegistry = result.getContext().getFunctionRegistry();

        Map<GLSLContext, VariableRegistryContext> variableDeclarations = variableRegistry.getDeclarationMap();
        List<Usage<FunctionPrototypeNode>> usedFunctions = functionRegistry.getUsedFunctions();

        String expected = "uniform float time;\n" +
                "vec3 medium(in vec3 M){\n" +
                "   float Mc=time*M.x;\n" +
                "   Mc+=M.x;\n" +
                "   Mc+=M.x;\n" +
                "   vec3 _gen0=Mc*M;\n" +
                "   Mc+=_gen0;\n" +
                "   return Mc*M;\n" +
                "}\n" +
                "void main(){\n" +
                "   gl_FragColor=medium(vec3(1,0,0));\n" +
                "}";

        assertEquals(expected, toString(result.getNode()));
        assertEquals(2, variableDeclarations.size(), "only 2 contexts, global and medium");
        assertEquals(2, usedFunctions.size(), "only 2 functions, vec3 and medium");
    }

    @Test
    void testOptimizerReferences2() {
        TLogConfiguration.get().useGlobalConfiguration();
        TLogConfiguration.get().getConfig().setLogLevel(Level.TRACE);

        GLSLContext context = parserContext.currentContext();
        outputConfig = outputConfig.edit().renderNewLines(true).indentation(3).build();

        // add constructors
        BuiltInFunctionRegistry builtInRegistry = parserContext.getFunctionRegistry().getBuiltInFunctionRegistry();
        new ConstructorsFunctionSet().generate(builtInRegistry, GLSLProfile.COMPATIBILITY);

        TypeQualifierList uniform = new TypeQualifierList();
        uniform.add(StorageQualifier.UNIFORM);

        parserContext.getVariableRegistry()
                .declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(VEC3), "gl_FragColor", null, null, null));
        parserContext.getVariableRegistry()
                .declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(uniform, FLOAT), "time", null, null, null));

        String source =
                "float small(vec3 S, float Sa) { S+=vec3(S); return S.x; }\n" +
                        "vec3 medium(in vec3 M) {\n" +
                        "    float Mc = time;\n" +
                        "    Mc += small(M+vec3(1,0,0), Mc);\n" +
                        "    Mc += small(M+vec3(0,1,0), Mc+time);\n" +
                        "    return Mc*M;\n" +
                        "}\n" +
                        "void main() {\n" +
                        "    gl_FragColor = medium(vec3(1,0,0));\n" +
                        "}";

        Branch result = optimizeBranch(source);
        VariableRegistry variableRegistry = result.getContext().getVariableRegistry();
        FunctionRegistry functionRegistry = result.getContext().getFunctionRegistry();

        Map<GLSLContext, VariableRegistryContext> variableDeclarations = variableRegistry.getDeclarationMap();
        List<Usage<FunctionPrototypeNode>> usedFunctions = functionRegistry.getUsedFunctions();

        System.out.println(toString(result.getNode()));
        assertEquals(2, variableDeclarations.size(), "only 2 contexts and main");
        assertEquals(2, usedFunctions.size(), "only 2 functions, two flavors of vec3");
    }

    @Test
    void testNestedContexts() {
        TLogConfiguration.get().useGlobalConfiguration();
        TLogConfiguration.get().getConfig().setLogLevel(Level.TRACE);

        GLSLContext context = parserContext.currentContext();
        outputConfig = outputConfig.edit().renderNewLines(true).indentation(3).build();

        // add constructors
        BuiltInFunctionRegistry builtInRegistry = parserContext.getFunctionRegistry().getBuiltInFunctionRegistry();
        new ConstructorsFunctionSet().generate(builtInRegistry, GLSLProfile.COMPATIBILITY);

        TypeQualifierList uniform = new TypeQualifierList();
        uniform.add(StorageQualifier.UNIFORM);

        parserContext.getVariableRegistry()
                .declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(uniform, FLOAT), "time", null, null, null));

        String source = "vec4 small(vec3 S) {\n" +
                "  float a=time*S.x;\n" +
                "    for(int i=0;i<5;i++) {\n" +
                "      a -= 0.1;\n" +
                "      if(a<0)\n" +
                "         a*=13;\n" +
                "    }\n" +
                "    return vec4(a);\n" +
                "  }\n" +
                "void main() {\n" +
                "    gl_FragColor = small(vec3(1,2,3));\n" +
                "}";

        Branch result = optimizeBranch(source);
        VariableRegistry variableRegistry = result.getContext().getVariableRegistry();
        FunctionRegistry functionRegistry = result.getContext().getFunctionRegistry();

        Map<GLSLContext, VariableRegistryContext> variableDeclarations = variableRegistry.getDeclarationMap();
        List<Usage<FunctionPrototypeNode>> usedFunctions = functionRegistry.getUsedFunctions();

        System.out.println(toString(result.getNode()));
        assertEquals(3, variableDeclarations.size(), "only 3 contexts and main");
        assertEquals(2, usedFunctions.size(), "only 1 functions, two flavors of vec3");
    }

    @Test
    void testNestedContexts2() {
        TLogConfiguration.get().useGlobalConfiguration();
        TLogConfiguration.get().getConfig().setLogLevel(Level.TRACE);

        GLSLContext context = parserContext.currentContext();
        outputConfig = outputConfig.edit().renderNewLines(true).indentation(3).build();

        // add constructors
        BuiltInFunctionRegistry builtInRegistry = parserContext.getFunctionRegistry().getBuiltInFunctionRegistry();
        new ConstructorsFunctionSet().generate(builtInRegistry, GLSLProfile.COMPATIBILITY);

        TypeQualifierList uniform = new TypeQualifierList();
        uniform.add(StorageQualifier.UNIFORM);

        parserContext.getVariableRegistry()
                .declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(VEC3), "gl_FragColor", null, null, null));
        parserContext.getVariableRegistry()
                .declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(uniform, FLOAT), "time", null, null, null));

        String source = "int g(int i) { int j=2*i; return j; }\n" +
                "vec3 small(vec3 S) {\n" +
                "  float a=time*S.x;\n" +
                "    for(int i=0;i<5;i++) {\n" +
                "      a -= 0.1;\n" +
                "      if(a<0)\n" +
                "         a*=g(7);\n" +
                "    }\n" +
                "    return vec3(a);\n" +
                "  }\n" +
                "void main() {\n" +
                "    gl_FragColor = small(vec3(g(1),2,3));\n" +
                "}";

        Branch result = optimizeBranch(source);
        VariableRegistry variableRegistry = result.getContext().getVariableRegistry();
        FunctionRegistry functionRegistry = result.getContext().getFunctionRegistry();

        Map<GLSLContext, VariableRegistryContext> variableDeclarations = variableRegistry.getDeclarationMap();
        List<Usage<FunctionPrototypeNode>> usedFunctions = functionRegistry.getUsedFunctions();

        System.out.println(toString(result.getNode()));
        assertEquals(4, variableDeclarations.size(), "only 3 contexts and main");
        assertEquals(2, usedFunctions.size(), "only 1 functions, two flavors of vec3");
    }

    @Test
    @DisplayName("starstruck.glsl")
    void testShader() {
        TLogConfiguration.get().useGlobalConfiguration();
        TLogConfiguration.get().getConfig().setLogLevel(Level.TRACE);

        GLSLContext context = parserContext.currentContext();
        outputConfig = outputConfig.edit().renderNewLines(true).indentation(3).build();

        // add constructors
        BuiltInFunctionRegistry builtInRegistry = parserContext.getFunctionRegistry().getBuiltInFunctionRegistry();
        new ConstructorsFunctionSet().generate(builtInRegistry, GLSLProfile.COMPATIBILITY);

        TypeQualifierList uniform = new TypeQualifierList();
        uniform.add(StorageQualifier.UNIFORM);

        Preprocessor.Result preprocessResult = PreprocessorExecutor.create()
                .source(Paths.get("src/test/resources/shaders/simple.glsl"))
                .process();

        String source = preprocessResult.getSource();

        Branch result = optimizeBranch(source);
        VariableRegistry variableRegistry = result.getContext().getVariableRegistry();
        FunctionRegistry functionRegistry = result.getContext().getFunctionRegistry();

        Map<GLSLContext, VariableRegistryContext> variableDeclarations = variableRegistry.getDeclarationMap();
        List<Usage<FunctionPrototypeNode>> usedFunctions = functionRegistry.getUsedFunctions();

        System.out.println(toString(result.getNode()));
        assertEquals(2, variableDeclarations.size(), "only 2 contexts and main");
        assertEquals(2, usedFunctions.size(), "only 2 functions, two flavors of vec3");
    }

    private void sourceEquals(String expected, String actual) {
        if (expected.contains("?")) {
            if (expected.length() != actual.length()) {
                assertEquals(expected, actual);
            }

            for (int i = 0; i < expected.length(); i++) {
                char ch = expected.charAt(i);
                if (ch == '?') {
                    continue;
                }
                if (ch != actual.charAt(i)) {
                    fail("Expected " + expected + " but was " + actual);
                }
            }
        }
        assertEquals(expected, actual);
    }
}


