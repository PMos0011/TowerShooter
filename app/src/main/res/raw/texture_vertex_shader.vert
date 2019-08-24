#version 310 es

in vec4 vPosition;
in vec2 vTextureCoords;

out vec2 fTextureCoords;

void main() {
    gl_Position = vPosition;

    fTextureCoords=vTextureCoords;
}
