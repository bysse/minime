uniform vec3 resolution;
uniform sampler2D image;

const float GA = 2.399;
const mat2 rot = mat2(cos(GA),sin(GA),-sin(GA),cos(GA));

vec3 dof(sampler2D tex,vec2 uv,float rad) {
    vec3 acc=vec3(0);
    vec2 pixel=vec2(.002*resolution.y/resolution.x,.002),angle=vec2(0,rad);;
    for (int j=0;j<80;j++) {
        rad += 1./rad;
        angle*=rot;
        acc+=texture(tex,uv+pixel*(rad-1.)*angle).xyz;
    }
    return acc/80.;
}

void main(void) {
    vec2 uv=gl_FragCoord.xy/resolution.xy;
    gl_FragColor=vec4(dof(image,uv,texture(image,uv).w),1.);
}