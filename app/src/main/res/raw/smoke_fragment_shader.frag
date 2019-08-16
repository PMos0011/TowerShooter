precision mediump float;
uniform float f_Time;
varying vec2 v_TexCoordinate;

vec4 transparentColor = vec4(0.0, 0.0, 0.0, 0.0);
uniform vec4 innerColor;
uniform vec4 outerColor;
uniform float visibility;

float innerThreshold = 0.35;
float outerThreshold = 0.15;
float softEdge = 0.05;

float random(vec2 coord){
    return fract(sin(dot(coord, vec2(2., 7.)))* 4.);
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

float circle(vec2 coord){
    vec2 dist = coord-vec2(0.5);

    return (0.5 - distance(coord, vec2(0.5))) * visibility;
}

void main() {

    vec2 coord = v_TexCoordinate * 8.0;
    vec2 fbmcoord = coord / 6.0;
    float c = circle(v_TexCoordinate);

    float noise1 = noise(coord + vec2(f_Time * 0.25, f_Time * 4.0));
    float noise2 = noise(coord + vec2(f_Time * 0.5, f_Time * 7.0));
    float combinedNoise = (noise1 + noise2) / 2.0;

    float fbmNoise = fbm(fbmcoord + vec2(0.0, f_Time));
    fbmNoise = overlay(fbmNoise, v_TexCoordinate.y);

    float everythingCombined = combinedNoise * c * fbmNoise;

    if (everythingCombined < outerThreshold){
        gl_FragColor = transparentColor;
    } else if (everythingCombined < outerThreshold + softEdge){
        gl_FragColor = mix(transparentColor, outerColor, (everythingCombined - outerThreshold) / softEdge);
    } else if (everythingCombined < innerThreshold){
        gl_FragColor = outerColor;
    } else if (everythingCombined < innerThreshold + softEdge){
        gl_FragColor = mix(outerColor, innerColor, (everythingCombined - innerThreshold) / softEdge);
    } else {
        gl_FragColor = innerColor;
    }

}