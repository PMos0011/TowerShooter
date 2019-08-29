#version 310 es
precision mediump float;

out vec4 outColor;

in vec2 fTextureCoords;
uniform sampler2D textureSampler;

void main() {
    outColor = texture(textureSampler, fTextureCoords);
}
