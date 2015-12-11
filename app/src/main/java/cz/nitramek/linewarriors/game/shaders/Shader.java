package cz.nitramek.linewarriors.game.shaders;

import android.opengl.GLES20;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shader {
    private int programHandle;

    private Map<String, Integer> attributesHandles;

    private Map<String, Integer> uniformHandles;

    protected Shader(int programHandle, List<String> attributes, List<String> uniforms) {
        this.programHandle = programHandle;
        attributesHandles = new HashMap<>(attributes.size());
        uniformHandles = new HashMap<>(uniforms.size());
        for (String attributeName : attributes) {
            this.attributesHandles.put(attributeName, GLES20.glGetAttribLocation(programHandle, attributeName));
        }
        for (String uniformName : uniforms) {
            this.uniformHandles.put(uniformName, GLES20.glGetUniformLocation(programHandle, uniformName));
        }
    }


    static Shader createShader(String vertexShader, String fragmentShader, List<String> attributes, List<String> uniforms) throws ShaderException {
        int programHandle = GLES20.glCreateProgram();
        GLES20.glAttachShader(programHandle, compileShader(vertexShader, GLES20.GL_VERTEX_SHADER));
        GLES20.glAttachShader(programHandle, compileShader(fragmentShader, GLES20.GL_FRAGMENT_SHADER));
        int i = 0;
        for (String attribute : attributes) {
            GLES20.glBindAttribLocation(programHandle, i, attribute);
            i++;
        }
        GLES20.glLinkProgram(programHandle);
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            Log.e(Shader.class.getName(), GLES20.glGetShaderInfoLog(programHandle));
            GLES20.glDeleteShader(programHandle);
            throw new ShaderException("Couldn't link shaders");
        }
        //for now only one shader is needed
        GLES20.glUseProgram(programHandle);
        return new Shader(programHandle, attributes, uniforms);
    }

    static int compileShader(String shaderText, int shaderType) throws ShaderException {
        int shaderHandle = GLES20.glCreateShader(shaderType);
        if (shaderHandle != 0) {
            GLES20.glShaderSource(shaderHandle, shaderText);

            GLES20.glCompileShader(shaderHandle);
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
            if (compileStatus[0] == 0) {
                Log.e(Shader.class.getName(), GLES20.glGetShaderInfoLog(shaderHandle));
                GLES20.glDeleteShader(shaderHandle);
                throw new ShaderException("Shader wasn't  compiled successfully");
            }
            return shaderHandle;
        } else {
            throw new ShaderException("Couldn't get shader handle");
        }

    }

    public void useShader() {
        GLES20.glUseProgram(this.programHandle);
    }

    public int getUniformHandle(String uniformName) {
        return this.uniformHandles.get(uniformName);
    }

    public int getAttributeHandle(String attributeName) {
        return this.attributesHandles.get(attributeName);
    }

}
