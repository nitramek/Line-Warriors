precision mediump float;
attribute vec2 position;
attribute vec2 textureUv;

uniform mat4 ModelMatrix;

varying vec2 TextureCoord;

uniform float xSpriteOffset;
uniform float ySpriteOffset;

void main(){
    gl_Position = ModelMatrix * vec4(position, 0, 1.0);
    TextureCoord = textureUv + vec2(xSpriteOffset, ySpriteOffset);
}