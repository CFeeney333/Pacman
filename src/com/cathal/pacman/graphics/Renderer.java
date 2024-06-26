package com.cathal.pacman.graphics;

import com.cathal.pacman.maths.Color;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    /**
     * Renderer class
     * Abstracted interface for OpenGL rendering
     * initializes opengl by calling GL.createCapabilities()
     */
    public Renderer() {
        GL.createCapabilities();
    }

    public void setClearColor(Color color) {
        glClearColor(color.getR(), color.getG(), color.getB(), color.getA());
    }

    public void render(IGameItem[] gameItems) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        for (IGameItem gameItem :
                gameItems) {
            gameItem.render();
        }
    }
}
