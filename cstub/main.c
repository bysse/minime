#include <stdio.h>


//#define GLSL_SINGLE_SHADER 1
#include "glsl_builder.h"

int main() {
    printf("SHADER 0: %s\n", glslShader(0));
    return 0;
}