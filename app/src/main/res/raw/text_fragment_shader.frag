#version 310 es
precision mediump float;

out vec4 outColor;

in vec2 fTextureCoords;
in vec4 color;

uniform sampler2D textureSampler;

void main() {
    outColor = texture(textureSampler, fTextureCoords)*color;
}
