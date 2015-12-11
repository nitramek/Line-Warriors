package cz.nitramek.linewarriors.game.shaders;


import java.util.List;

public class ShaderInitiator {

    private final String vertexShader;
    private final String fragmentShader;
    private final List<String> attributes;
    private final List<String> uniforms;

    public ShaderInitiator(String vertexShader, String fragmentShader, List<String> attributes, List<String> uniforms) {
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
        this.attributes = attributes;
        this.uniforms = uniforms;

    }

    public Shader compileLinkAndGetShader() throws ShaderException {
        return Shader.createShader(vertexShader, fragmentShader, attributes, uniforms);
    }
}
