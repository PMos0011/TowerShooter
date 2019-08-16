precision mediump float;
uniform float time;
varying vec2 v_TexCoordinate;

vec4 transparent = vec4(0.0, 0.0, 0.0, 0.0);
vec4 inner = vec4(0.85,0.61,0.11,0.8);
vec4 outer = vec4(0.85, 0.19,0.11,0.8);

float inner_threshold = 0.4;
float outer_threshold = 0.15;
float soft_edge = 0.04;

float rand(vec2 coord){
    return fract(sin(dot(coord, vec2(2., 7.)))* 4.);
}

float noise(vec2 coord){
    vec2 i = floor(coord);
    vec2 f = fract(coord);

    float a = rand(i);
    float b = rand(i + vec2(1.0, 0.0));
    float c = rand(i + vec2(0.0, 1.0));
    float d = rand(i + vec2(1.0, 1.0));

    vec2 cubic = f * f * (3.0 - 2.0 * f);

    return mix(a, b, cubic.x) + (c - a) * cubic.y * (1.0 - cubic.x) + (d - b) * cubic.x * cubic.y;
}

float fbm(vec2 coord){
    float value = 0.0;
    float scale = 0.5;

    for(int i = 0; i < 6; i++){
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

float circleShape(vec2 coord, float radius){
    vec2 diff = abs(coord - vec2(0.5, 0.5));

    float dist = sqrt(diff.x * diff.x + diff.y * diff.y) / radius;
    float value = sqrt(1.0 - dist*dist);
    return clamp(value, 0.0, 1.0);
 //return value;
    //return sqrt(1.0-dist*dist);
}

float circle(in vec2 _st, in float _radius){
    vec2 dist = _st-vec2(0.5);

    return (0.5 - distance(_st,vec2(0.5)))*4.;
}

void main() {

    vec2 coord = v_TexCoordinate * 8.0;
    vec2 fbmcoord = coord / 6.0;

    float circleS=circleShape(v_TexCoordinate, 0.45);
    circleS+=circleShape(v_TexCoordinate, 0.15)/2.0;

    float c = circle(v_TexCoordinate, 0.45);

    float noise1 = noise(coord + vec2(time * 0.25, time * 4.0));
    float noise2 = noise(coord + vec2(time * 0.5, time * 7.0));
    float combined_noise = (noise1 + noise2) / 2.0;

    float fbm_noise = fbm(fbmcoord + vec2(0.0, time));
    fbm_noise = overlay(fbm_noise, v_TexCoordinate.y);

    //float everything_combined = combined_noise * fbm_noise * circleS;
    float everything_combined = combined_noise * c* fbm_noise;

    if (everything_combined < outer_threshold){
        gl_FragColor = transparent;
    } else if (everything_combined < outer_threshold + soft_edge){
        gl_FragColor = mix(transparent, outer, (everything_combined - outer_threshold) / soft_edge);
    } else if (everything_combined < inner_threshold){
        gl_FragColor = outer;
    } else if (everything_combined < inner_threshold + soft_edge){
        gl_FragColor = mix(outer, inner, (everything_combined - inner_threshold) / soft_edge);
   } else {
        gl_FragColor = inner;
    }


   // gl_FragColor = vec4(vec3(everything_combined), 1.0);
}
