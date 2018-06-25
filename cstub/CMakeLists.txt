cmake_minimum_required(VERSION 3.10)
project(cstub C)

set(CMAKE_C_STANDARD 11)

set(CMAKE_C_FLAGS "-Os -save-temps -fverbose-asm")
add_executable(cstub main.c glsl_builder.h glsl_builder.c)