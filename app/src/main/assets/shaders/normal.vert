attribute vec2 position;


uniform mat4 ModelMatrix;

varying vec2 TextureCoord;

void main(){
    gl_Position = ModelMatrix * vec4(position, 0, 1.0);
    TextureCoord = position;
}