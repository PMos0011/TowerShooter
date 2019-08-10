attribute vec4 a_Position;
uniform mat4 u_mModelMatrix;
uniform mat4 u_mProjectionMatrix;
attribute vec2 a_TexCoordinate;
varying vec2 v_TexCoordinate;
void main() {
    v_TexCoordinate = a_TexCoordinate;
    gl_Position = (u_mProjectionMatrix*u_mModelMatrix)*a_Position;
}