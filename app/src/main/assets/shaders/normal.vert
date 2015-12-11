attribute vec2 position;


uniform mat4 ModelMatrix;

void main(){
    gl_Position = ModelMatrix * vec4(position, 0, 1.0);
}