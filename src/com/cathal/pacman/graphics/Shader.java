package com.cathal.pacman.graphics;

import com.cathal.pacman.maths.Matrix4f;
import com.cathal.pacman.utils.ShaderUtils;

import java.util.Hashtable;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int shaderID;
    private final Map<String, Integer> uniformLocationCache = new Hashtable<>();

    public static Shader WALL, PACMAN, FOOD, GHOST;

    public Shader(String vertexPath, String fragPath) {
        shaderID = ShaderUtils.load(vertexPath, fragPath);
    }

    public static void loadAll() {
        WALL = new Shader("res/shaders/wall.vert", "res/shaders/wall.frag");
        PACMAN = new Shader("res/shaders/pacman.vert", "res/shaders/pacman.frag");
        FOOD = new Shader("res/shaders/food.vert", "res/shaders/food.frag");
        GHOST = new Shader("res/shaders/ghost.vert", "res/shaders/ghost.frag");
    }

    private int getUniformLocation(String name) {
        if (uniformLocationCache.containsKey(name)) {
            return uniformLocationCache.get(name);
        }
        int location = glGetUniformLocation(shaderID, name);
        if (location == -1) {
            System.out.println("Could not find uniform: " + name);
        }
        uniformLocationCache.put(name, location);
        return location;
    }

    public void bind() {
        glUseProgram(shaderID);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void setUniformMat4f(String name, Matrix4f matrix) {
        glUniformMatrix4fv(getUniformLocation(name), false, matrix.toFloatBuffer());
    }
}
