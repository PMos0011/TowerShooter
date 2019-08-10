attribute vec4 a_Position;
attribute vec4 a_Color;
uniform mat4 u_mModelMatrix;
uniform mat4 u_mProjectionMatrix;
varying vec4 v_Color;
void main() {
    v_Color=a_Color;
    gl_Position = (u_mProjectionMatrix*u_mModelMatrix)*a_Position;
}