uniform vec3 iResolution2;
void main(void) {
    vec2 uv = gl_FragCoord.xy / iResolution2.xy;
    gl_FragColor = vec4(uv,0.5+0.5*sin(iResolution2.z),1.0);
}