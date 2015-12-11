package cz.nitramek.linewarriors.game.shaders;


public class ShaderException extends Exception {
    public ShaderException() {
        super();
    }

    public ShaderException(String detailMessage) {
        super(detailMessage);
    }

    public ShaderException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ShaderException(Throwable throwable) {
        super(throwable);
    }
}
