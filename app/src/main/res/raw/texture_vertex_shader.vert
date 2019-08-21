attribute vec4 a_Position;
uniform mat4 u_mModelMatrix;
uniform mat4 u_mProjectionMatrix;
attribute vec2 a_TexCoordinate;
varying vec2 v_TexCoordinate;
uniform vec2 transition;
uniform vec2 movement;
uniform bool isFont;
void main() {

    if (isFont){
        // v_TexCoordinate = a_TexCoordinate*vec2(0.083984375,0.11914062)+vec2(0.173828125,0.35);
        v_TexCoordinate = a_TexCoordinate*transition+movement;
    } else {
        v_TexCoordinate = a_TexCoordinate;
    }
    gl_Position = (u_mProjectionMatrix*u_mModelMatrix)*a_Position;
}