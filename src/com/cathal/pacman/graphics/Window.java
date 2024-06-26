package com.cathal.pacman.graphics;

import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width, height;
    private long windowID;
    private String title;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;

        // try to initialize glfw
        if (!glfwInit()) {
            System.err.println("Could not initialize GLFW!");
        }

        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        windowID = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowID == NULL) {
            System.err.println("Could not create GLFW window!");
        }

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(windowID, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        showWindow();

        glfwMakeContextCurrent(windowID);
        glfwSwapInterval(1);
    }

    public void showWindow() {
        glfwShowWindow(windowID);
    }

    public void hideWindow() {
        glfwHideWindow(windowID);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowID);
    }

    public void swapBuffers() {
        glfwSwapBuffers(windowID);
    }

    public void update() {
        glfwPollEvents();
    }

    public void terminate() {
        glfwTerminate();
    }

    public boolean isKeyPressed(int key) {
        return (glfwGetKey(windowID, key) == GLFW_PRESS);
    }

    public boolean isKeyReleased(int key) {
        return (glfwGetKey(windowID, key) == GLFW_RELEASE);
    }
}
