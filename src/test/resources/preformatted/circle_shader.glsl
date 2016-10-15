void main(){
    vec2 pos=mod(gl_FragCoord.xy,vec2(50.))-vec2(25.);
    float dist_squared=dot(pos,pos);
    gl_FragColor=(dist_squared<400.)?vec4(.9,.9,.9,1.):vec4(.2,.2,.4,1.);
}
