float testFunction(float value){
    return value+1.;
}
void main(){
    gl_FragColor=vec4(testFunction(0.));
}
