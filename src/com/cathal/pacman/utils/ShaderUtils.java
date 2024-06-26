package com.cathal.pacman.utils;

import static org.lwjgl.opengl.GL20.*;

public class ShaderUtils {

    private ShaderUtils() {

    }

    public static int load(String vertexPath, String fragmentPath) {
        String vertexShader = FileUtils.loadAsString(vertexPath);
        String fragmentShader = FileUtils.loadAsString(fragmentPath);
        return create(vertexShader, fragmentShader);
    }

    private static int create(String vertexShader, String fragmentShader) {
        int program = glCreateProgram();
        int vertID = glCreateShader(GL_VERTEX_SHADER);
        int fragID = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(vertID, vertexShader);
        glShaderSource(fragID, fragmentShader);

        glCompileShader(vertID);
        if (glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Could not compile vertex shader!");
            System.err.println(glGetShaderInfoLog(vertID, 1024));
            return -1;
        }

        glCompileShader(fragID);
        if (glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Could not compile fragment shader!");
            System.err.println(glGetShaderInfoLog(fragID, 1024));
            return -1;
        }

        glAttachShader(program, vertID);
        glAttachShader(program, fragID);
        glLinkProgram(program);
        glValidateProgram(program);

        glDeleteShader(vertID);
        glDeleteShader(fragID);

        return program;
    }
}
