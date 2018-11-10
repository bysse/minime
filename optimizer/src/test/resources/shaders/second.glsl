#pragma include(noise.glsl)

void main() {
    vec3 seed = gl_FragCoord.xxy;
    gl_FragColor = vec4(noise(10.*seed));
}
