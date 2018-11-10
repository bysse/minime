#pragma include(noise.glsl)

void main() {
    vec3 seed = gl_FragCoord.xyx;
    gl_FragColor = vec4(noise(seed));
}
