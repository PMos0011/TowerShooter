#version 310 es
precision highp float;

out vec4 outColor;

in vec2 fTextureCoords;
in vec4 innerColor;
in vec4 outerColor;
in vec4 options;

uniform sampler2D textureSampler;

vec4 transparentColor = vec4(0.0, 0.0, 0.0, 0.0);

float innerThreshold = 0.35;
float outerThreshold = 0.15;
float softEdge = 0.05;

float random(vec2 coord){
    return fract(sin(dot(coord, vec2(12.9898, 78.233)))* 43758.5);
}

float noise(vec2 coord){
    vec2 i = floor(coord);
    vec2 f = fract(coord);

    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));

    vec2 cubic = f * f * (3.0 - 2.0 * f);

    return mix(a, b, cubic.x) + (c - a) * cubic.y * (1.0 - cubic.x) + (d - b) * cubic.x * cubic.y;
}

float fbm(vec2 coord){
    float value = 0.0;
    float scale = 0.5;

    for (int i = 0; i < 6; i++){
        value += noise(coord) * scale;
        coord *= 2.0;
        scale *= 0.5;
    }
    return value;
}

float overlay(float base, float top) {
    if (base < 0.5) {
        return 2.0 * base * top;
    } else {
        return 1.0 - 2.0 * (1.0 - base) * (1.0 - top);
    }
}

float circle(vec2 coord, float radius){
    float dist;

    dist = (radius - distance(coord, vec2(0.5))) *options[1];
    return clamp(dist, 0.0, 1.0);
}

void main() {

    vec2 coord = fTextureCoords * 8.0;
    vec2 fbmcoord = coord / 6.0;
    float c = circle(fTextureCoords, 0.5f);
    c+= circle(fTextureCoords, 0.3f)/2.;

    float noise1 = noise(coord + vec2(options[0] * 0.25, options[0] * 4.0));
    float noise2 = noise(coord + vec2(options[0] * 0.5, options[0] *7.0));
    float combinedNoise = (noise1 + noise2) / 2.0;

    float fbmNoise = fbm(fbmcoord + vec2(0.0, options[0]));
    fbmNoise = overlay(fbmNoise, fTextureCoords.y);

    float everythingCombined = combinedNoise * c * fbmNoise;

    if (everythingCombined < outerThreshold){
        outColor = transparentColor;
    } else if (everythingCombined < outerThreshold + softEdge){
        outColor = mix(transparentColor, outerColor, (everythingCombined - outerThreshold) / softEdge);
    } else if (everythingCombined < innerThreshold){
        outColor = outerColor;
    } else if (everythingCombined < innerThreshold + softEdge){
        outColor = mix(outerColor, innerColor, (everythingCombined - innerThreshold) / softEdge);
    } else {
        outColor = innerColor;
    }

    // outColor = texture(textureSampler, fTextureCoords);
}