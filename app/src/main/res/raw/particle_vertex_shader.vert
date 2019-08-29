#version 310 es

in vec4 vPosition;
in vec2 vTextureCoords;
in mat4 vModelMatrix;
in vec4 vOuterColor;
in vec4 vInnerColor;
in vec4 vOptions;

layout (std140) uniform projectionMatrix{
    mat4 vProjectionMatrix;
};

out vec2 fTextureCoords;
out vec4 innerColor;
out vec4 outerColor;
out vec4 options;

void main() {
    gl_Position = (vProjectionMatrix*vModelMatrix)*vPosition;

    fTextureCoords=vTextureCoords;
    innerColor = vInnerColor;
    outerColor = vOuterColor;
    options = vOptions;
}
