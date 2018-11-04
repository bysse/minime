const float i_MAXD = 15000.0;
vec3 eps = vec3(.02, 0., 0.);

vec3 waterColor_2 = vec3(0.01, 0.1, 0.15);
vec3 sunColor = vec3(.99, .94, .9);
vec3 attenuation = vec3(.3, .03, .01);
vec3 sun = vec3(0.38, 0.531, -0.758);
vec2 cloud = vec2(2501., 3400.);

float globalDistance = 0.;
vec3 sunAmount = vec3(0.);
bool above;

float sdTriPrism( vec3 p, vec2 h ) {
	vec3 q = abs(p);
	return max(q.y-h.y,max(0.866025*q.x + 0.5*p.z,-p.z)-.5*h.x);
}

float hash( float n ) {
	return fract(sin(n)*43758.5453);
}

float noise(vec3 x ) {
	vec3 p = floor(x);
	vec3 f = fract(x);

	f = f*f*(3.0-2.0*f);
	float n = p.x + 57.0*p.y + 113.0*p.z;
	return mix(mix(mix( hash(n+  0.0), hash(n+  1.0),f.x),
					mix( hash(n+ 57.0), hash(n+ 58.0),f.x),f.y),
				mix(mix( hash(n+113.0), hash(n+114.0),f.x),
					mix( hash(n+170.0), hash(n+171.0),f.x),f.y),f.z);
}

mat3 m = mat3( 0.0,  0.8,  0.6,
              -0.8,  0.3, -0.4,
              -0.6, -0.4,  0.6 );

float fbm(vec3 p, float d) {
	float v  = 0.500*noise( p ); p = m*p*2.1 + d;
          v += 0.210*noise( p ); p = m*p*2.2 + d;
    	  v += 0.120*noise( p ); p = m*p*2.3 + d;
    	  v += 0.063*noise( p );
	return v;
}

vec4 cloudField(vec3 p) {
	return vec4(1, 1, 1, smoothstep(0., 1., pow(fbm(.001 * p + vec3(2, 4.2, 0), 0.),2.) - .3));
}

float waterHeight(vec3 q) {
	float d = .2*iTime;
	vec3 p = .15*q + d;
	p.y = 0.;
	return .5*fbm(p, d) + 0.0025*noise( 32.*p )
		+ .5*sin(.1*q.z + 5.*d + 0.01*q.x) + .05*sin(.44*q.z + d);
}

vec4 traceClouds(vec3 rp, vec3 rd) {
	vec4 color = vec4(0.);
	vec2 interval = (cloud - rp.y) / rd.y;
	float inc = (interval.y - interval.x) / float(60);
	float aa = clamp(20000./(interval.x + globalDistance), 0., 1.);

	for(int i=0;i<60;i++) {
		if (color.w < 1.) {
			vec3 p = rp + interval.x*rd;
			vec4 c = cloudField(p);

			vec4 shade = cloudField(p + 400.*sun);
			c.xyz *= 1. - .25*smoothstep(0., 1., 10. * shade.w);
			c.xyz *= 1. - .0001*(cloud.y-p.y);

			c.w *= aa;
			c.xyz *= c.w;
			color += c * (1.-color.w);

			interval.x += inc;
		}
	}
	vec3 sky = mix(vec3(0.8, 0.9, 1.2), vec3(0.08, 0.25, 0.5), .45+rd.y);
	color.xyz = mix(sky, color.xyz/(0.01+color.w), color.a*aa);
	return color;
}

vec2 field(vec3 q, float d, int nowater) {
	vec2 h = vec2(i_MAXD * float(nowater));

	if (nowater < 1) {
		float k = smoothstep(0., 1., 200. / d);
		h.x = q.y
			+ noise( 0.01*vec3(q.x, 0., q.z) + iTime )
			+ (1. - k)*.1*noise( 0.1*vec3(q.x, 0., q.z))		// FLICKERY
			;

		h.x = abs(h.x + (k>0.01?k*waterHeight(q):0.));
		h.y = .5;
	}


	float y = 8. + .5*sin(iTime);
	if (dot(q,q) < 4e6) {
		float a = noise(20.*floor((vec3(q.x, 0., q.z))/128.));
		y += 200. * smoothstep(0., 1., a) - 3.;
		q.xz = mod(q.xz, vec2(128.)) - 64.;
	}

	float e = min(
		max(sdTriPrism(q + vec3(0, y, 0), vec2(20, 11)), -sdTriPrism(q + vec3(5, y, -5), vec2(10, 12))),
		sdTriPrism(q + vec3(5.5, y+.5*sin(4.+iTime), -5.5), vec2(5, 11))
		);

	return e<h.x?vec2(e, 1.5):h;
}

vec3 normal(vec3 p, float d, float f) {
	return normalize(vec3(
		field(p+f*eps.xyz, d, 0).x - field(p-f*eps.xyz, d, 0).x,
		field(p+f*eps.yxz, d, 0).x - field(p-f*eps.yzx, d, 0).x,
		field(p+f*eps.yzx, d, 0).x - field(p-f*eps.yzx, d, 0).x
		));
}


vec3 intersect(vec3 ro, vec3 rd, int nowater) {
	vec3 hit = vec3(0., .1, 0.);
	// hit.x = position
	// hit.y = step size
	// hit.z = material
	for( int i=0; i<60; i++ ) { // Lower for speed
		if (abs(hit.y) > eps.x && hit.x < i_MAXD) {
			hit.x += hit.y;
			hit.yz = field(ro + rd * hit.x, hit.x, nowater);
		}
	}
	if (nowater < 1 && (hit.x > i_MAXD || hit.y > 1.)) {
		hit.x = -ro.y / rd.y;
		hit.y = 10.;
		hit.z = .5;
	}
    return hit;
}

vec3 colorOfObject(float d) {
	return vec3(2., 2., .8)*(.1 + d); // INLINE?
}

vec3 colorOfWater(vec3 h, vec3 ro, vec3 rd) {
	vec4 traceResults = vec4(0);

	vec3 hit = ro + (h.x - 0.05) * rd;
	vec3 n = normal(hit, h.x, 1.);

	float side = above ? 1. : -1.;
	float D = max(0.,dot(sun, n)*side);
	globalDistance += h.x;

	// Material in h.z
	if (h.z > 1.) {
		return mix(waterColor_2, colorOfObject(D), above?1.:exp(-0.0002*pow(h.x,1.6)));
	}

	vec3 R = reflect(rd, n);
	vec3 H = normalize(sun - rd);

	// reflective indices
	float index = above ? .75 : 1.33;
	float d = abs(dot(rd, n));
	float det = 1. - index * index * (1. - d*d); // if this is < 0, then it's total reflection

	vec3 reflection = waterColor_2 * (1. - max(0.,-R.y)), // Add 'diffuse' lightning under water if schlick > 0.01
		transmission = waterColor_2; // total reflection

	if (det > 0.) {
		// calculate transmission
		vec3 T = normalize(index*rd + (index*d - sqrt(det))*side*n);

		vec3 tHit = intersect(hit, T, 1);
		if (tHit.z > 1. && tHit.x < i_MAXD) {
			vec3 uwhit = hit + T*tHit.x;
			transmission = colorOfObject(max(0., dot(sun, normal(uwhit, tHit.x, 1.)))) * exp(0.5*attenuation*uwhit.y); // reduce attenuation because it looks good
		} else {
			transmission = vec3(0.0, 0.01, 0.05);
			if (!above) {
				traceResults = traceClouds(hit, T);
				transmission = traceResults.xyz + (1. - traceResults.a) * sunAmount;
			}
		}
	}

	// Calculate Schlick's approximation to the Fresnel factor
	// this is the same no matter on which side of the surface you are
	float schlick = 0.02 + (1. - 0.02) * pow(1. - d, 2.);

	if (schlick > 0.01) {
		// calculatee reflection
		vec3 rHit = intersect(hit, R, 1); // raymarch without the water function
		if (rHit.z > .1 && rHit.x < i_MAXD && dot(R, vec3(0,1,0)) > 0.05) {
			reflection = mix(
						waterColor_2,
						colorOfObject(max(0., dot(sun, normal(hit + R*rHit.x, rHit.x, 1.)))),
						above ? 1. : exp(-0.0002*pow(h.x + rHit.x,1.6))
						);
		} else if (above) {
			traceResults = traceClouds(hit, R);
			reflection = .75 * traceResults.xyz
				+ (1.-traceResults.w)
					* (sunColor + (1.0 - sunColor)
						* pow(1.0 - max(0., dot(sun, H)), 5.))
					* pow(max(0., dot(n, H)), 64.)
					* D
					* (12. - 0.008*ro.y);
		}
	}

	return schlick * reflection + (1. - schlick) * transmission;
}

void mainImage( out vec4 fragColor, in vec2 fragCoord ) {
	vec2 uv = -1. + 2. * fragCoord.xy / iResolution.xy;
	uv.x *= iResolution.x / iResolution.y;

	float s0  = 2. * smoothstep(0., 1., (iTime -  0.)*.04);
	float s25 = smoothstep(0., 1., (iTime - 25.)*.08);
	float s40 = smoothstep(0., 1., (iTime - 35.)*.06);
	float s45 = smoothstep(0., 1., (iTime - 45.)*.12);

	float h1 = clamp(iTime - 25., 0., 10.);

	vec2 look = vec2(
		// left-right
		1.20 + s0
		- .35 * sin(3.14*s40)
		,
		// up-down
		0.75 - .30*s0
		-.30*(smoothstep(0., 1., (iTime - 20.)*.10) - s25)
		+ .25 * sin(3.14*smoothstep(0., 1., (iTime - 35.)*.20))
		- .3 * s40 + .3 * s45*s45
		+ .5 * smoothstep(0., 1., (iTime - 55.) * .1)
	);

	vec3 cp = vec3(
		// x
		440.*s40 -128.
		 ,
		// y
		111.
		- 125.*s25
		+ 5. * clamp(iTime-35., 0., 5.)
		+   80.*s40
		-   80.*s45
		- 100. * clamp(iTime - 54.8, 0., .3) - 10. * clamp(iTime - 55.1, 0., 15.)
		,
		// z
		5178.
		- 500.*h1 + 10.*h1*h1
		- 1038.*s40
		);

	vec3 cd = normalize(vec3(sin(look.x), look.y, cos(look.x)));
	vec3 side = cross(cd, vec3(0, -1., 0.));
	vec3 rd = normalize(cd + .5*(uv.x*side + uv.y*cross(cd, side)));

	float wh = waterHeight(cp);
	vec3 color = waterColor_2;
	vec3 result = intersect(cp, rd, 0);
	vec4 tr = vec4(0);

	above = cp.y >= wh;

	float sunSpecular = max(0.,dot(sun, rd));
	sunAmount = sunColor * min(4.0 * pow(sunSpecular, 2048.0) + pow(sunSpecular, 32.0), 1.0);

	if (result.x > 0.) {
		color = colorOfWater(result, cp, rd);
	} else if (above) {
		tr = traceClouds(cp, rd);
		color = tr.xyz;
	}

	if (above && sunSpecular > 0.0) {
		vec2 sunPos = vec2(dot(sun, side), sun.y);
		vec2 pos = uv - sunPos;
		pos = pos * length(pos);
		sunColor *= .1 * pow(sunSpecular, 6.0);
		color += sunColor * 25.0 * pow(max(0., 1.0-2.0*length(sunPos*2.0 + pos)), 10.0) * vec3(1.0, .4, .2);
		color += sunColor * 10.0 * pow(max(0., 1.0-length(sunPos*5.0 + mix(pos, uv, -2.1))), 4.0);
	}

	color = above ? color + sunAmount * (1.-tr.a) : color * exp(-attenuation*(wh - cp.y)) * min(1.+ rd.y, 1.);

	color +=    smoothstep(0., 1., 1.-.1*iTime);	// FADE IN
	color *= 1.-smoothstep(0., 1., .2*iTime-12.3);	// FADE OUT

	// gamma + contrast
	color = pow( min(color, 1.), vec3(0.44) );
	fragColor = vec4(color*color*(3.0-2.0*color), 0.);
}
