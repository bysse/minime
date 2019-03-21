#define XRES 1280
#define YRES  720
void main(void) {
    float a = 1.0;
    float d = XRES / YRES;
    gl_FragColor = vec4(a / d);
}