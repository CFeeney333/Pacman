package com.cathal.pacman;

import com.cathal.pacman.graphics.Renderer;
import com.cathal.pacman.graphics.Shader;
import com.cathal.pacman.graphics.Window;
import com.cathal.pacman.level.ILevel;
import com.cathal.pacman.level.LevelOne;
import com.cathal.pacman.maths.Color;
import com.cathal.pacman.maths.Matrix4f;
import com.cathal.pacman.utils.Timer;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class Main implements Runnable {

    public static final int TARGET_UPS = 30;
    public static final int TARGET_FPS = 75;

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 704;

    public static final float UNIT_SIZE = 64.0f;  // 64 pixels is one unit
    public static final float UNIT_WIDTH = WIDTH / UNIT_SIZE;
    public static final float UNIT_HEIGHT = HEIGHT / UNIT_SIZE;

    public enum Bounds {
        LEFT, RIGHT, TOP, BOTTOM, NEAR, FAR
    }

    public static final float LEFT = 0.0f;
    public static final float RIGHT = UNIT_WIDTH;
    public static final float BOTTOM = 0.0f;
    public static final float TOP = UNIT_HEIGHT;
    public static final float NEAR = -1.0f;
    public static final float FAR = 1.0f;
    public static final Matrix4f PR_MATRIX = Matrix4f.orthographic(LEFT, RIGHT, BOTTOM, TOP, NEAR, FAR);

    private Window window;
    private Renderer renderer;
    private Timer timer;

    private ILevel level;

    private Thread thread = null;

    private boolean running = false;
    private boolean gameNotOver = false;

    public static void main(String[] args) {
        new Main().start();
    }

    private void start() {
        running = true;
        gameNotOver = true;
        thread = new Thread(this, "game");
        thread.start();
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        do {
            init();
            gameNotOver = true;
        } while (gameLoop() == 1);
        window.terminate();
    }

    private int gameLoop() {
        float elapsedTime;
        float accumulator = 0.0f;
        float interval = 1.0f / TARGET_UPS;

        while (running) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            while (accumulator >= interval) {
                update();
                accumulator -= interval;
            }

            render();

            float loopSlot = 1.0f / TARGET_FPS;
            double endTime = timer.getLastLoopTime() + loopSlot;
            while (timer.getTime() < endTime) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.err.println("Thread " + thread.getName() + " interrupted!");
                }

            }
            if (window.windowShouldClose()) {
                running = false;
            }

            if (!gameNotOver) {
                return 1;
            }
        }
        return 0;
    }

    private void init() {
        window = new Window(WIDTH, HEIGHT, "Pacman");
        renderer = new Renderer();
        timer = new Timer();
        timer.init();
        Shader.loadAll();
        level = new LevelOne(renderer, window);

        renderer.setClearColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));
    }

    private void update() {
        window.update();
        if (window.isKeyPressed(GLFW_KEY_ESCAPE)) {
            running = false;
        }
        if (level.update() == 0) {
            setGameOver();
        }
    }

    private void render() {
        level.render();
        window.swapBuffers();
    }

    private void setGameOver() {
        gameNotOver = false;
    }
}
