uniform vec3 iResolution;
uniform float iGlobalTime;
vec3 checker(vec2 uv,vec2 size){
    vec2 m=sign(fract(uv*size)-.5);
    return vec3(m.x*m.y);
}
void mainImage(out vec4 fragColor,in vec2 fragCoord){
    vec2 uv=2*fragCoord.xy/iResolution.xy-1;
    vec2 size=2+vec2(sin(iGlobalTime),cos(iGlobalTime));
    vec3 color=checker(uv,size);
    fragColor=vec4(color,1);
}