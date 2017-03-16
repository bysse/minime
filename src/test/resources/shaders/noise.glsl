
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