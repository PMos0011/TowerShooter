#version 310 es

in vec4 vPosition;

out vec3 color;

void main() {

    color = vec3(1.0,1.0,1.0);
    gl_Position = vPosition;
}
