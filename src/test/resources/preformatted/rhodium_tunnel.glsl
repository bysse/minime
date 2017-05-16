uniform vec3 iResolution;
uniform float iGlobalTime;
float time=iGlobalTime;
vec3 res=iResolution;
float bounce=abs(fract(.05*time)-.5)*20;
void pR(inout vec2 p,float a){
    p=cos(a)*p+sin(a)*vec2(p.y,-p.x);
}
float noise(vec3 p){
    vec3 ip=floor(p);
    p-=ip;
    vec3 s=vec3(7,157,113);
    vec4 h=vec4(0,s.yz,s.y+s.z)+dot(ip,s);
    p=p*p*(3-2*p);
    h=mix(fract(sin(h)*43758.5),fract(sin(h+s.x)*43758.5),p.x);
    h.xy=mix(h.xz,h.yw,p.y);
    return mix(h.x,h.y,p.z);
}
float map(vec3 p){
    p.z+=(3-sin(.314*time+1.1));
    pR(p.zy,1.57);
    return mix(length(p.xz)-.2,length(vec3(p.x,abs(p.y)-1.3,p.z))-.2,step(1.3,abs(p.y)))-.1*noise(8*p+.4*bounce);
}
vec3 calcNormal(vec3 pos){
    float eps=.0001;
    float d=map(pos);
    return normalize(vec3(map(pos+vec3(eps,0,0))-d,map(pos+vec3(0,eps,0))-d,map(pos+vec3(0,0,eps))-d));
}
float castRayx(vec3 ro,vec3 rd){
    float function_sign=(map(ro)<0)?-1:1;
    float precis=.0001;
    float h=precis*2;
    float t=0;
    for(int i=0;i<120;i++){
        if(abs(h)<precis||t>12)
            break;
        h=function_sign*map(ro+rd*t);
        t+=h;
    }
    return t;
}
float refr(vec3 pos,vec3 lig,vec3 dir,vec3 nor,float angle,out float t2,out vec3 nor2){
    float h=0;
    t2=2;
    vec3 dir2=refract(dir,nor,angle);
    for(int i=0;i<50;i++){
        if(abs(h)>3)
            break;
        h=map(pos+dir2*t2);
        t2-=h;
    }
    nor2=calcNormal(pos+dir2*t2);
    return (.5*clamp(dot(-lig,nor2),0,1)+pow(max(dot(reflect(dir2,nor2),lig),0),8));
}
float softshadow(vec3 ro,vec3 rd){
    float sh=1;
    float t=.02;
    float h=0;
    for(int i=0;i<22;i++){
        if(t>20)
            continue;
        h=map(ro+rd*t);
        sh=min(sh,4*h/t);
        t+=h;
    }
    return sh;
}
void mainImage(out vec4 fragColor,in vec2 fragCoord){
    vec2 uv=gl_FragCoord.xy/res.xy;
    vec2 p=uv*2-1;
    float wobble=(fract(.1*(time-1))>=.9)?fract(-time)*.1*sin(30*time):0;
    wobble*=.3;
    vec3 dir=normalize(vec3(2*gl_FragCoord.xy-res.xy,res.y));
    vec3 org=vec3(0,2*wobble,-3);
    dir=normalize(vec3(dir.xy,sqrt(max(dir.z*dir.z-dot(dir.xy,dir.xy)*.2,0))));
    vec2 m=sin(vec2(0,1.57)+time/8);
    dir.xy=mat2(m.y,-m.x,m)*dir.xy;
    dir.xz=mat2(m.y,-m.x,m)*dir.xz;
    vec3 color=vec3(0);
    vec3 color2=vec3(0);
    float t=castRayx(org,dir);
    vec3 pos=org+dir*t;
    vec3 nor=calcNormal(pos);
    vec3 lig=normalize(-pos);
    float depth=clamp((1-.09*t),0,1);
    vec3 pos2,nor2=vec3(0);
    if(t<12){
        color2=vec3(max(dot(lig,nor),0)+pow(max(dot(reflect(dir,nor),lig),0),16));
        color2*=clamp(softshadow(pos,lig),0,1);
        float t2;
        color2.r+=refr(pos,lig,dir,nor,.91,t2,nor2)*depth;
        color2.g+=refr(pos,lig,dir,nor,.9,t2,nor2)*depth;
        color2.b+=refr(pos,lig,dir,nor,.89,t2,nor2)*depth;
        color2-=clamp(.1*t2,0,1);
    }
    float tmp=0;
    float T=1;
    float intensity=.1*-sin(.209*time+1)+.1;
    for(int i=0;i<128;i++){
        float density=0;
        float nebula=noise(org+bounce);
        density=intensity-map(org+.5*nor2)*nebula;
        if(density>0){
            tmp=density/128;
            T*=1-tmp*100;
            if(T<=0)
                break;
        }
        org+=dir*.078;
    }
    vec3 basecol=vec3(1/1,1/4,1/16);
    T=clamp(T,0,1.5);
    color+=basecol*exp(4*(.5-T)-.8);
    color2*=depth*depth;
    color2+=(1-depth)*noise(6*dir+.3*time)*.1;
    fragColor=vec4(vec3(1*color+.8*color2)*1.3,0);
}