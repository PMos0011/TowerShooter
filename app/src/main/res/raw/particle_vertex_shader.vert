#version 310 es

in vec4 vPosition;
in vec2 vTextureCoords;
in mat4 vModelMatrix;
in vec4 outerColor;
in vec4 innerColor;
in vec4 options;

layout (std140) uniform projectionMatrix{
    mat4 vProjectionMatrix;
};

out vec2 fTextureCoords;

void main() {
    gl_Position = (vProjectionMatrix*vModelMatrix)*vPosition;

    fTextureCoords=vTextureCoords;
}
