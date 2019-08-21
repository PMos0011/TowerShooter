uniform vec2 position;
uniform vec2 textureCoords;
varying vec2 pass_textureCoords;
uniform vec2 translation;

void main(void){
    pass_textureCoords = textureCoords;
    gl_Position = vec4(position + translation * vec2(2.0, -2.0), 0.0, 1.0);
}
