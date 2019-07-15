uniform float iTime;
vec2 iResolution = vec2(1920, 1080);


#define M_GROUND 0
#define M_STRUCTURE 1
#define BPM 93
#define PATTERN_FULL 64
#define PATTERN_HALF 32

float sdSphere( vec3 p, float s )
{
  return length(p)-s;
}

vec2 sdLine( in vec3 pos, in vec3 a, in vec3 b )
{
    vec3 pa = pos - a, ba = b - a;
    float h = clamp( dot(pa,ba)/dot(ba,ba), 0.0, 1.0 );
    return vec2( length(pa-h*ba), h );
}

float udBox( vec3 p, vec3 b )
{
  return length(max(abs(p)-b,0.0));
}

float udRoundBox( vec3 p, vec3 b, float r )
{
  return length(max(abs(p)-b,0.0))-r;
}

float sdBox( vec3 p, vec3 b )
{
  vec3 d = abs(p) - b;
  return min(max(d.x,max(d.y,d.z)),0.0) + length(max(d,0.0));
}

float sdTorus( vec3 p, vec2 t )
{
  vec2 q = vec2(length(p.xz)-t.x,p.y);
  return length(q)-t.y;
}

float sdCylinder( vec3 p, vec3 c )
{
  return length(p.xz-c.xy)-c.z;
}

float sdCone( vec3 p, vec2 c )
{
    // c must be normalized
    float q = length(p.xy);
    return dot(c,vec2(q,p.z));
}

float sdPlane( vec3 p, vec4 n )
 {
   // n must be normalized
   return dot(p,n.xyz) + n.w;
 }

 float sdHexPrism( vec3 p, vec2 h )
 {
     vec3 q = abs(p);
     return max(q.z-h.y,max((q.x*0.866025+q.y*0.5),q.y)-h.x);
 }

 float sdTriPrism( vec3 p, vec2 h )
 {
     vec3 q = abs(p);
     return max(q.z-h.y,max(q.x*0.866025+p.y*0.5,-p.y)-h.x*0.5);
 }

 float sdCapsule( vec3 p, vec3 a, vec3 b, float r )
 {
     vec3 pa = p - a, ba = b - a;
     float h = clamp( dot(pa,ba)/dot(ba,ba), 0.0, 1.0 );
     return length( pa - ba*h ) - r;
 }

 float sdCappedCylinder( vec3 p, vec2 h )
 {
   vec2 d = abs(vec2(length(p.xz),p.y)) - h;
   return min(max(d.x,d.y),0.0) + length(max(d,0.0));
 }

 float sdEllipsoid( in vec3 p, in vec3 r )
 {
     return (length( p/r ) - 1.0) * min(min(r.x,r.y),r.z);
 }
float noise(vec3 p) {
	vec3 ip=floor(p);
    p-=ip;
    vec3 s=vec3(7,157,113);
    vec4 h=vec4(0.,s.yz,s.y+s.z)+dot(ip,s);
    p=p*p*(3.-2.*p);
    h=mix(fract(sin(h)*43758.5),fract(sin(h+s.x)*43758.5),p.x);
    h.xy=mix(h.xz,h.yw,p.y);
    return mix(h.x,h.y,p.z);
}

float MAXD = 20.;
float eps = 0.02;
float fog = 2.4;
int index = 0;
float indexAlpha = 0;
#define steps 10

// (curlyness, funkyness, morph, smoothness)
vec4 x_mushroom = vec4(1, 4.15, 1, .5);
vec4 x_city2 = vec4(1, 2., 1, .5);
vec4 x_flower = vec4(12, 9, 0, .5);
vec4 x_egg = vec4(4, 9, 0, 1);
vec4 x_temple = vec4(4.2, 9, 0.2, 1.9);
vec4 x_the_lab = vec4(10, .42, .36, 2);
vec4 x_tower = vec4(12, 4, .25, .5);
vec4 x_patterns = vec4(1, .42, 1, .5);
vec4 x_blackswan = vec4(21, 2.11, .21, .5); // y -> 2.125

#define SCENES 10
vec4 x_landscape, x_scenes[SCENES] = vec4[](
    x_blackswan, x_egg, x_the_lab, 
    x_mushroom, x_mushroom, x_mushroom, x_mushroom, // city 1-4
    x_city2, // fly-over 1
    x_tower,

    x_mushroom
);
vec4 x_position[SCENES] = vec4[](
    // (x,y,z, camera rotation)
    vec4(0, 1, 0, 0),           // exit the dragon
    vec4(.4, .4, -2, -.2),       // temple 1
    vec4(-5, 0, 0, 0),          // the lab
    vec4(.5, -.4,-1, .1),       // city 1
    vec4(8.2,  0, 0,-.1),       // city 2
    vec4(44, .2, 1, 0),         // city 3
    vec4(5,  0, -6,.2),         // city 4
    vec4(0,  2, 0, 1.4),        // fly over 1

    vec4(-2.5, 0, 1,-.1),       // astronaut

    vec4(3, .6, 0,0)            // last city 1
);
vec4 x_movement[SCENES] = vec4[](
    // (dx,dy,dz, camera rotation speed)
    vec4(.5, 0, 0, 0),       // exit the dragon
    vec4( 1,.3, 0,-.5),       // temple 1
    vec4( 1,.3, 0, 0),       // the lab

    vec4( 1, 0, -.2, -.2),   // city 1
    vec4( 1, .5, 0, 0),      // city 2
    vec4( 1, .2, .5, .2),    // city 3
    vec4( 1,.5, 0, -.2),     // city 4

    vec4( 1, 2, 1,-.6),      // fly over 1

    vec4(.2, 0, .5, 0),      // astronaut

    vec4(2, .5, .5, .2)    // last city 1
);

// scene indices to make the code more semantic
#define SCENE_EXIT_THE_MOUTH    0
#define SCENE_TEMPLE            1
#define SCENE_THE_LAB           2
#define SCENE_CITY_1            3
#define SCENE_CITY_4            6
#define SCENE_FLY_OVER_1        7
#define SCENE_ASTRONAUT         8
#define SCENE_LAST_CITY         9
#define SCENE_COLLISION         10

float x_length[SCENES] = float[](
    // length of each scene in ticks
    PATTERN_FULL * 2,   // exit the dragon  - 0 
    PATTERN_FULL,       // temple 1         - 1
    PATTERN_FULL,       // the lab          - 2
    PATTERN_HALF,       // city 1           - 3
    PATTERN_HALF,       // city 2
    PATTERN_HALF,       // city 3
    PATTERN_HALF,       // city 4           - 6
    PATTERN_FULL*2,     // fly over 1       - 7

    PATTERN_FULL,       // astronaut        - 8

    PATTERN_FULL * 4    // last city 1
);


vec2 smin2( vec2 a, vec2 b, float k ) {
    float h = clamp( 0.5+0.5*(b.x-a.x)/k, 0.0, 1.0 );
    return mix( b, a, h ) - k*h*(1.0-h);
}

mat2 rot(float a) {
  float ca=cos(a);
  float sa=sin(a);
  return mat2(ca,sa,-sa,ca);
}

vec3 FractalSpace(vec3 pos) {
    pos *= 0.5;
    
    float height = length(pos) * 10.0;
    float s=7.;
	for(int i=0;i<steps;i++){
		pos.xz=abs(pos).xz-s; 
        pos.xz *= rot(1.21 + float(i)*x_landscape.y);
		s=s/1.3;
	}
   
    return pos + 0.4;
}

float waves(vec3 p) {
    float d = 5.*noise(p.xxz);
    float e = length(.5 + 0.5*sin(vec2(p.x + 2*d, 2*p.z+d)));
    float height = .2 * e*e - 1.4;

    vec3 q = p;
    q.xz = mod(p.xz + .2*FractalSpace(p).xz, vec2(3))-1.5;
    q.y -= height + 2.5*indexAlpha + noise(floor(p.zxz))*p.z - 1;
    float rock = mix(MAXD, length(q) - .1, step(SCENE_ASTRONAUT, index)*step(-12, -index));

    return min(
        p.y - height,
        rock
    );
}

vec2 map(vec3 p) {
    float a = 10*x_landscape.x/sin(dot(p,p));
    vec3 q = mix(FractalSpace(p*a)/a, FractalSpace(p), x_landscape.z), size = vec3(0.1, 1., 0.1);
    return smin2(
        vec2(waves(p), M_GROUND), 
        vec2(min(sdBox(q, size), sdBox(q, size.yxz)), M_STRUCTURE), 
        x_landscape.w);
}

vec3 normal(vec3 pos) {
    float d=map(pos).x;
	return normalize(vec3(map(pos+vec3(eps,0,0)).x-d,map(pos+vec3(0,eps,0)).x-d,map(pos+vec3(0,0,eps)).x-d));
}

vec3 intersect(vec3 ro, vec3 rd) {
    vec2 h=map(ro);
    float t=2*eps;
	for(int i=0;i<125;i++) {
        if(h.x>eps||t<MAXD) {
            h = map(ro+rd*t);
            t += h.x*.9;
        }
	}
    return vec3(t, h.y,0);
}

vec4 moon_trace(vec3 o, vec3 rd){
    float radius = 3e6, scale = 1.4 - 1.1*iTime/120;
    vec3 center=vec3(1e7,5e6,0)*scale, oc = o - center;
    float a = dot(rd, rd), 
        b = 2.0 * dot(oc, rd), 
        c = dot(oc,oc) - radius*radius,
        discriminant = b*b - 4*a*c,
        t = discriminant<0?0:(-b - sqrt(discriminant)) / (2.0*a);
    return vec4(normalize(o+t*rd - center), t);
}

const mat3 m = mat3( 0.0,  0.8,  0.6,
                    -0.8,  0.3, -0.4,
                    -0.6, -0.4,  0.6 );

float fbm(vec3 x) {
	return 0.5*noise(.3*x) + 0.4*noise(m*x*.66);
}

vec3 structure(vec3 p, vec3 n) {
     return vec3(.2 + .1*noise(20*p)) * n.y; // color based on slope
}

vec3 background(vec3 rd) {
    return vec3(.1, .1, .3)
        *smoothstep(0,1,1-rd.y*1.2)
        *(.5 + .2*cos(5+rd+vec3(0.,2.,4.)));
}

vec3 render(vec3 ro, vec3 rd, float snareMod) {
    float moonPhase = pow(iTime/120, 2);
    vec3 sundir = normalize(vec3(1, .5, 0));
    vec3 moonlight = mix(vec3(.9, .9, 1.1), vec3(.8,.3,.2), moonPhase);
    vec3 color = background(rd);

    vec3 hit = intersect(ro, rd);
    if (hit.x < MAXD) {
        vec3 pos = hit.x * rd + ro;
        //vec3 n = normal(pos);
        float d=map(pos).x;
	    vec3 n = normalize(vec3(map(pos+vec3(eps,0,0)).x-d,map(pos+vec3(0,eps,0)).x-d,map(pos+vec3(0,0,eps)).x-d));

        // diffuse lighting
        float light = max(0.2, dot(n, sundir));
        float brus = .9+.1*noise(80.*pos.xxz);

        // trace shadow
        vec3 shade = intersect(pos + 0.002*n, sundir);
        if (shade.x < MAXD) {
            light *= 0.3;
        }

        // color based on material
        color = mix(
            vec3(.4, .4, .2)*brus, //background(reflect(rd, n)),
            structure(pos, n),        
            sqrt(max(0,hit.y))
        )*light;

        // fade into the background
        float fogIntensity = 0.4;
        vec3 fogColor = mix(background(rd), fogIntensity*vec3(1,max(0,pos.y+.8),0), snareMod*.5);

        color = mix(color, fogColor, 1-exp(-5*max(0, 1-(pos.y+fog-.8*fbm(pos.xzx + .7*iTime)))));
        //color = mix(color,vec3(1,1.2*pos.y+2,0), 1-exp(-5*max(0, 1-(pos.y+scene.w-.8*noise(pos.xzx)))));
        color = mix(color, background(rd), 1-exp(-0.2*hit.x));
        color *= moonlight;
    } else {
        vec4 moon = moon_trace(ro, rd);
        if (moon.w > 0) {
            float light = max(0, dot(moon.xyz, -vec3(.4,.4,.8))); // normalize(vec3(-1,-1,-2))
            float eh = noise(sin(moon.xyz)*15);
            // color of the moon
            color = mix(color, vec3(.6) - .1*eh*eh, light*light);
            color = mix(color, vec3(.5,.2,.2)*(.6+.4*cos(eh+6. * moon.y)), light*light);
        }
        // moon glow
        vec3 md = normalize(vec3(1e7,5e6,-8e5)-ro);
        float mdot = dot(rd, md);
        color += .3*moonlight * mix(.8, .7 + .3*fbm(15*rd+mdot*iTime), moonPhase) *
                smoothstep(0, 1, pow(mdot, 15-13*iTime/120) * (.8+.2*noise(10*rd))); // glow scaling
        color*=moonlight;
    }

    return color;
}

float sdBox2( vec2 p, vec2 b ) {
  return length(max(abs(p) - b,0.0));
}

void main(void) {
    // manual syncing stuff
    float ticks = iTime * BPM * 4 / 60;
    float snare = clamp((ticks-132)/8, 0, 32);
    float snareOne = fract(snare); 
    float snareMod = sin(12*snareOne)*smoothstep(0,1,1-snareOne*4);

    // find the active scene from the ticks
    float segmentStart = 0;
    for (int i=0;i<SCENES;i++) {
        if (ticks >= segmentStart) {
            indexAlpha = (ticks-segmentStart)/x_length[i];
            index = i;
        }
        segmentStart += x_length[i];
    }
    
    if (index == SCENE_THE_LAB) { 
        // move the camera closer on the snare drum
        fog -= 1-indexAlpha*.3;
        indexAlpha += .3*(floor(snare)-8);
    }

    x_landscape = x_scenes[index];
    vec4 camera = x_position[index] + indexAlpha*x_movement[index];

    // custom logic
    if (index == SCENE_EXIT_THE_MOUTH) { 
        // animate the landscape so the mouth is opening
        x_landscape.y += 0.015*smoothstep(0, 1, ticks/80);
    } 
    if (SCENE_CITY_1 < index && index < SCENE_ASTRONAUT) {
        // do small variations of the landscape
        x_landscape.y += 3.25 + index*0.01; // city shape
    }

    float shake = 0, endOfWorldFactor = 1;
    if (index >= 9) {
        float amount = max(0, ticks - 660)*0.0003, 
            a = 6.3*noise(vec3(800*camera+9*iTime)); 
        camera.x += amount * cos(a);
        camera.y += amount * sin(a);
    }

    if (index >= SCENE_ASTRONAUT) {
        endOfWorldFactor = max(1, pow(.65+(ticks-512)/260, 10));
    }

    // setup the camera
    vec3 rd = normalize(vec3(2.*gl_FragCoord.xy -iResolution.xy, iResolution.y));

    rd.xz *= rot(1.57 + camera.w); 

    vec3 color = render(camera.xyz, rd, snareMod) *
        smoothstep(0, 1, ticks/16); // fade in

    // contrast
    color = mix(
        smoothstep(0,1, color*endOfWorldFactor),
        vec3(1), 
        smoothstep(0,1,.2*iTime - 24) // make sure we fade to white
        );
    // gamma correction
    color = pow(color, vec3(1.0/2.4));    


    // tazadum logo
    /*
    vec2 uv = gl_FragCoord.xy / iResolution.xy;
    uv.x *= iResolution.x / iResolution.y;
    
    vec2 a = 32.0*(2.*uv - 1.) + 30,b=a,c=a+vec2(-5,.1),v=vec2(0,1),w=vec2(1, .2);
    a.x -= 2.5;
    float T = min(sdBox2(b-v,w),sdBox2(b,vec2(.2,1.2))), 
          Z = min(min(sdBox2(a-v,w),sdBox2(a+v,w)),sdBox2(a-v*a.x, w)),
          M = min(min(sdBox2(c-vec2(.8,-.1), w.yx),sdBox2(c+vec2(.8,.1),w.yx)),sdBox2(c-v*(.1+abs(c.x)),w));
    float df = min(min(T,Z),M);

    color += 1 - smoothstep(0., .05, df);
    */
    gl_FragColor = vec4(color,1.0);
}
