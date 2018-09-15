uniform vec3 iResolution;
uniform float iGlobalTime;
const vec4 sphere=vec4(0,0,0,1);
const int steps=40;
float sdBox(vec3 p,vec3 b){
    vec3 d=abs(p)-b;
    return min(max(d.x,max(d.y,d.z)),0)+length(max(d,0));
}
float field(vec3 p){
    vec3 c=vec3(5);
    return max(-(length(p-sphere.xyz)-sphere.w),sdBox(p,vec3(.8)));
}
vec3 normal(vec3 p){
    const vec3 e=vec3(.0001,0,0);
    float x=field(p+e.xyz)-field(p-e.xyz);
    float y=field(p+e.yxz)-field(p-e.yzx);
    float z=field(p+e.yzx)-field(p-e.yzx);
    return normalize(vec3(x,y,z));
}
vec3 march(vec3 pos,vec3 dir){
    float t=0;
    float d=1;
    vec3 p=pos;
    for(int i=0;i<steps;i++){
        p=pos+t*dir;
        d=field(p);
        if(abs(d)<=.0001)
            return normal(p);
        t+=d;
    }
    return vec3(0);
}
void mainImage(out vec4 fragColor,in vec2 fragCoord){
    vec2 uv=-1+2*(fragCoord.xy/iResolution.xy);
    uv.x*=iResolution.x/iResolution.y;
    vec3 cp=vec3(cos(iGlobalTime)*3,sin(iGlobalTime)*3,-3);
    vec3 ct=vec3(0,0,0);
    vec3 cd=normalize(ct-cp);
    vec3 side=cross(vec3(0,1,0),cd);
    vec3 up=cross(cd,side);
    vec3 rp=cp+cd+.5*(uv.x*side+uv.y*up);
    vec3 rd=normalize(rp-cp);
    vec3 color=abs(march(rp,rd));
    fragColor=vec4(color,1);
}