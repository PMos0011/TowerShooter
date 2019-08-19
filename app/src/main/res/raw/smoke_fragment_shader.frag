precision mediump float;
uniform float f_Time;
varying vec2 v_TexCoordinate;

vec4 transparentColor = vec4(0.0, 0.0, 0.0, 0.0);
uniform vec4 innerColor;
uniform vec4 outerColor;
uniform float visibility;
uniform bool isFire;

float innerThreshold = 0.35;
float outerThreshold = 0.15;
float softEdge = 0.05;

float random(vec2 coord){
    return fract(sin(dot(coord, vec2(12.9898, 78.233)))* 43.7585);
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
    if (isFire){
        vec2 diff = abs(coord - vec2(0.5, 0.8));

        if (coord.y < 0.8){
            diff.y /= 1.6;
        } else {
            diff.y *= 2.0;
        }
        dist = sqrt(diff.x * diff.x + diff.y * diff.y) / radius;
        dist=(1.-dist)*visibility;
    } else {
        dist = (radius - distance(coord, vec2(0.5))) * visibility;
    }
    return clamp(dist, 0.0, 1.0);

}

void main() {

    vec2 coord = v_TexCoordinate * 8.0;
    vec2 fbmcoord = coord / 6.0;
    float c = circle(v_TexCoordinate, 0.5f);
    c+= circle(v_TexCoordinate, 0.3f)/2.;
    
    float noise1 = noise(coord + vec2(f_Time * 0.25, f_Time * 4.0));
    float noise2 = noise(coord + vec2(f_Time * 0.5, f_Time *7.0));
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

    //gl_FragColor = vec4(vec3(random(v_TexCoordinate)), 1.0);

}
