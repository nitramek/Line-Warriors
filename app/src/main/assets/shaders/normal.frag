varying vec2 TextureCoord;
uniform sampler2D TextureUnit;
void main(){
    gl_FragColor = texture2D(TextureUnit, TextureCoord);
}