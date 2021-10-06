
#pragma include(camera_integration.glsl)

void main(void) {
    vec2 uv = (2.*gl_FragCoord.xy-iResolution.xy)/iResolution.y;
    vec2 mouseUV = iMouse.xy / iResolution.xy;

    vec3 ro = vec3(0,0,0);
    vec3 rd = normalize(vec3(uv, 1));

    skuggbox_camera(uv, ro, rd);

    fragColor = vec4(abs(rd), 1.0);
}