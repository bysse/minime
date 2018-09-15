uniform vec4 p[2];

const int i_COLOR_ITERATIONS = 6;
const int i_STEPS = 60;
const float i_STEPS7 = .7 / 60.;
const float i_SCALE = 2.2;
const float i_MAXD = 80.0;
const float i_resX = 1280.;
const float i_resY = 720.;
const float i_pattern = 3.84;
const float i_MR2 = .6;

const vec3 axis = vec3(1., 0., 0.);
const vec3 eps = 0.001*axis;

vec4 x=p[0];
vec4 y=p[1];

float tt = 0., apa = 0.;
float C1= i_SCALE-1.0;
float C2= pow(i_SCALE, float(1-15));

vec4 trap;
vec4 TRANS = vec4(5.,-5.,y.y*5., 1.);
vec4 scalevec = vec4(i_SCALE) / i_MR2;
mat4 rot = mat4( cos(y.z), sin(y.z), 0., 0.,
                 -sin(y.z), cos(y.z), 0., 0.,
                 0., 0.,	1., 0.,
			  0., 0.,	0., 1. );

vec2 smin( vec2 a, vec2 b, float k )
{
	float h = clamp( 0.5 + 0.5*(b.x-a.x)/k, 0.0, 1.0 );
	return mix( b, a, h ) - k*h*(1.0-h);
}
vec2 smax( vec2 a, vec2 b, float k )
{
	float h = clamp( 0.5 + 0.5*(b.x-a.x)/k, 0.0, 1.0 );
	return mix( b, a, 1.-h ) + k*h*(h);
}

vec3 inv = vec3(1.6-y.w,-x.y*.1, 0.);
float de2(vec3 p) {
	float dotp=dot(p,p);
	p=sin(10.*p/dotp+inv);
	vec4 z = vec4(3.*p.xyz, 1.);// 2 was the last value
	trap = vec4(1.0);
	//int iter = tt > 3. ? 12 : 15;
  	for (int i=0; i<13; i++) {
    		z*=rot;
    		z.xyz = clamp(z.xyz, -1.0, 1.0) * 2.0 - z.xyz;
    		float r2 = dot(z.xyz, z.xyz);
    		if (i < i_COLOR_ITERATIONS) {
    			trap = min(trap, abs(vec4(z.xyz,r2)));
    		}

    		z = z*clamp(max(i_MR2/r2, i_MR2), 0.0, 1.0)*scalevec + TRANS;
  	}
	return min((length(z.xyz) - C1) / z.w - C2, length(p.xz)-.01)*dotp*.1;
}

vec2 field(in vec3 z) {
	vec2 sphere = vec2(length(z)-.9, 2.);
	if (x.z > 0. && length(z) < 3.) {
		vec2 d = vec2(i_MAXD);
		for (int i=1;i<6;i++) {
			float fi = float(i);
			vec3 s = sin(vec3(.5*x.x*fi, fi+x.x/fi, 4.+.5*x.x*fi));
			s.y +=  x.z - 3.;
			d = smin(d, vec2(length(z-s)-0.5), 0.8);
		}
		d.y = 0.1;
		sphere = smin(sphere, d, .5);
	}

	vec2 i_r = smin(
		// Wavy plane
		// vec2(dot(z,axis.yxy)+1. + sin(x.x+3.*(z.x+z.z))*.002, 0.),
		vec2(z.y+1. + sin(x.x+3.*(z.x+z.z))*.002, 0.),
		// Fractal
		smax(
			vec2(de2(z), 1.),
			vec2(1.-length(vec2(length(z.xz)-2.,z.y-.5)), 1.0),
			20.*p[0].y
			),
		.1);

	return smin(i_r, sphere, .1);
}

vec3 normal(vec3 p) {
	return normalize(vec3(
		field(p+eps.xyz).x - field(p-eps.xyz).x,
		field(p+eps.yxz).x - field(p-eps.yzx).x,
		field(p+eps.yzx).x - field(p-eps.yzx).x
		));
}

vec3 intersect(in vec3 ro, in vec3 rd) {
    	float k = 0.;
    	float e = tt*.1;
    	vec2 r = vec2(0.1);
    	int j = 0;
    	for( int i=0; i<i_STEPS; i++ ) {
        	if(abs(r.x) < eps.x*e || k>i_MAXD) continue;
	    	r = field( ro+rd*k);
	    	k += min(2.,.5*r.x);
	    	tt += r.x;
	    	e += r.x;
		j += 1;
    	}

    	if(k>i_MAXD) r.y=-1.0;
    	return vec3( k, j, r.y );
}

vec3 shade(in vec3 hit, in vec3 p, in vec3 n, in vec3 r) {
	vec3 struc = vec3(1.) - vec3(0., 1.-trap.x, 1.) * (trap.w * trap.w);
	//float D_ = .5*pow(1.0 - hit.y*i_STEPS7, 2.0);
	return mix(struc*hit.z, vec3(2.), max(0., hit.z-1.)) * .5*pow(1.0 - hit.y*i_STEPS7, 2.0);
}

float seg(float from, float to) {
	return step(i_pattern*from, x.x)*(1.-step(i_pattern*to, x.x));
}

void main(void) {
	vec2 b = (gl_FragCoord.xy /vec2(i_resX, i_resY));
	vec2 uv = -1. + 2. * b;
	uv.x *= i_resX / i_resY;

	float t=x.x, d;
    	//-----------------------------------------------------

	// 00 - 04
	vec3 cp = seg(0., 4.)*(vec3(4., .5, 4.) + vec3(.3, .1, 0.)*t);
	vec3 cd = seg(0., 4.)*(vec3(1.,-2., 1.));

	// 04 - 06
	cp+= seg(4., 6.)*(vec3(3., .0, -6.5) + vec3(0., .2, 0.)*(t-4.*i_pattern));
	cd+= seg(4., 6.)*(axis);

	// 06 - 08
	cp+= seg(6., 8.)*(vec3(-9., 1.0, 1.) + vec3(.5,.1,0.)*(t-6.*i_pattern));
	cd+= seg(6., 8.)*(vec3(1., -.2, .0));

	// inlineSin(PAT_1_0*3.1415f/12.0f*(time-PAT_16_0))*p0d05;
	y.w = seg(16., 22.)*sin(i_pattern*3.141/12.*(t-16.*i_pattern))*0.05;

	// 08 - 29
	float t8_14 = clamp((t-8.*i_pattern) / (5.*i_pattern), .0, 1.);
	float t26_32 = clamp((t-26.*i_pattern) / (6.*i_pattern), .0, .1);
	float t24_26 = smoothstep(.0, .1, (t-24.*i_pattern) / (4.*i_pattern));
	float t14_d = 5. - 2.*t8_14 + 2.*t24_26*t24_26 - 4.*t26_32*t26_32;
	cp+= seg(8.,32.)*(vec3(sin(t*-.1), 2. - 1.5*t8_14 - .4*t24_26 +  .5*t26_32, cos(t*.2)) * t14_d);
	cd+= seg(8.,32.)*(vec3(.0, 1.- t8_14, .0) - cp);

    	//-----------------------------------------------------
    	//cp = 10.*uCamera;
    	//cd= 2.*uCameraDir - 1.;
    	//-----------------------------------------------------

	//cp = vec3(sin(cp.x), cp.y, cos(cp.x)) * cp.z;
	cd = normalize(cd);

	vec3 side = cross(axis.yxy, cd);
	vec3 up = cross(cd, side);
	vec3 rd = normalize(cd + 1.1*(uv.x*side + uv.y*up)); // FOV

	//-----------------------------------------------------
    	// marching
    	//-----------------------------------------------------
	vec3 color;
	vec3 h1 = intersect(cp, rd);
	vec3 p = cp + h1.x*rd;
	if (h1.z >= 0.) {
		vec3 pn = p-rd*.01;
		vec3 n = normalize(normal(pn));
		vec3 r = reflect(rd, n);
		color = shade(h1, p, n, r);
		d = .8;
		for (int i=0;i<3;i++) {
			if (h1.z < .8) {
			 	d *= .8 - h1.z;
				h1 = intersect(pn, r);
				if (h1.z >= 0.) {
					pn = pn + (h1.x*.98)*r ;
					n = normal(pn);
					r = reflect(r, n);
					color = color + d*shade(h1, pn, n, r);
				}
			}
		}
	}
	color = pow( color*2., vec3(1.5)) * sqrt( 32.*b.x*b.y*(1.-b.x)*(1.-b.y) )*x.w*x.w;
	gl_FragColor = vec4(color, 1.);
}