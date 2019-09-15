#version 310 es

in vec4 vPosition;
in vec2 vTextureCoords;
in mat4 vModelMatrix;
in vec2 vTransition;
in vec2 vMovement;
in vec4 vColor;


layout (std140) uniform projectionMatrix{
    mat4 vProjectionMatrix;
};

out vec2 fTextureCoords;
out vec4 color;


void main() {
    gl_Position = (vProjectionMatrix*vModelMatrix)*vPosition;

    fTextureCoords=vTextureCoords*vTransition+vMovement;
    color=vColor;

}