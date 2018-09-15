out vec4 colorOut;
vec3 a(){
    return vec3(1);
}
void main(){
    colorOut=mix(a().xxyz,vec4(1,0,0,1),1);
}
