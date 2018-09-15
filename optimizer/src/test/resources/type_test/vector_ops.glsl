uniform vec3 colorIn;
out vec4 colorOut;
void main(){
    vec3 x=cross(colorIn,vec3(1.));
    colorOut=vec4(x,x.x);
}
