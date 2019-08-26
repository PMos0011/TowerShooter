#version 310 es

in vec4 vPosition;
in vec2 vTextureCoords;
uniform mat4 vModelMatrix;

layout (std140) uniform projectionMatrix{
    mat4 vProjectionMatrix;
};

out vec2 fTextureCoords;

void main() {
    gl_Position = (vProjectionMatrix*vModelMatrix)*vPosition;

    fTextureCoords=vTextureCoords;
}
