# minime
GLSL transpiler which minifies the input shader in a way that makes it easy to compress.

## Usage

Simplest usage is just to optimize a shader and write the output to disk:
  
    minimi-optimize shader.glsl
  
Which will produce an output file called `shader.min.glsl`

## Features and limitations

#### Macro expansion
Macros are always expanded during preprocessing. There's no options avoid that so size optimizations with macros doesn't work.
You can pass in macro definitions from the command line, which can be useful when changing shader resolution for instance.

#### Include
The preprocessor supports a custom pragma directive for including other files. The concept should be familiar to anyone using this tool.

    #pragma include(some_shader.glsl)

#### Optimizations
* Variable declaration squeeze is of course done to reduce the size of the output.
* Dead code is removed from the output shader so no need to comment out things you don't need.
* Identifiers are renamed after optimization so you can use sane variable names.
* Function inlining, simple functions can be inlined when it makes sense.
* Constant propagation is performed where is saves bytes.
* Some expressions are automatically simplified and rewritted for size. But constants are almost always better to optimize by hand. It tries to be smart, but it's probably not smarter than you.


## Build
To execute the Gradle build, type:

    ./gradlew dist

The resulting binaries will be in `optimizer/build/` and `optimizer/build/installer`.

## Install on Linux
To install, execute the bash installer:

    minime-install.sh <destination dir>

Then make sure to add the <destination dir> to the PATH and source the completion script.
Either by adding the commands that the installer shows to ~/.bashrc or executing these lines:
  
    DEST_DIR=<destination dir> # <-- change me !!
    echo "export PATH=\$PATH:$DEST_DIR" >> ~/.bashrc
    echo "source $DEST_DIR/minime_completion" >> ~/.bashrc
  
