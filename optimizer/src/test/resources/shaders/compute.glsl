#version 430
layout(local_size_x = 1, local_size_y = 1) in;
uniform vec2 u_simulation;
uniform sampler3D u_velocity;
layout(binding=1) buffer P { vec4 pos[]; };
layout(binding=2) buffer V { vec4 vel[]; };
layout(binding=3) buffer F { vec4 force[]; };

void main() {
    uint index = int(gl_GlobalInvocationID);

    vec4 p = pos[index] + u_simulation.x * vel[index];

    vec4 b = vec4(200.0);
    p = mod(p+ 0.5*b, b)-0.5*b;

    vec3 v = texture(u_velocity, (32.00 + p.xyz)/64.0).xyz;

    pos[index] = p;

    vel[index] += u_simulation.x * (force[index] + vec4(v, 0));
    vel[index] *= u_simulation.y; // add friction
}
