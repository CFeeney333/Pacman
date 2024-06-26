package com.cathal.pacman.graphics;

import com.cathal.pacman.Main;
import com.cathal.pacman.maths.Matrix4f;
import com.cathal.pacman.maths.Vector3f;

public class Food implements IGameItem {

    private Mesh mesh;

    private Vector3f position;
    public static final float WIDTH = 0.15f;
    public static final float HEIGHT = 0.25f;

    public Food(Vector3f position) {
        float[] positions = new float[] {
                -WIDTH / 2.0f, -HEIGHT / 2.0f, position.getZ(),
                 WIDTH / 2.0f, -HEIGHT / 2.0f, position.getZ(),
                 WIDTH / 2.0f,  HEIGHT / 2.0f, position.getZ(),
                -WIDTH / 2.0f,  HEIGHT / 2.0f, position.getZ()
        };

        byte[] indices = new byte[] {
                0, 1, 2,
                2, 3, 0
        };

        mesh = new Mesh(positions, indices);
        Shader.FOOD.bind();
        Shader.FOOD.setUniformMat4f("pr_matrix", Main.PR_MATRIX);
        this.position = position;
    }

    @Override
    public void render() {
        bind();
        Shader.FOOD.setUniformMat4f("ml_matrix", Matrix4f.translate(position));
        mesh.render();
    }

    @Override
    public void update() {

    }

    @Override
    public void bind() {
        Shader.FOOD.bind();
        mesh.bind();
    }

    @Override
    public void unbind() {
        Shader.FOOD.unbind();
        mesh.unbind();
    }

    public float getLeftBound() {
        return position.getX() - WIDTH / 2.0f;
    }
    public float getRightBound() {
        return position.getX() + WIDTH / 2.0f;
    }
    public float getTopBound() {
        return position.getY() + HEIGHT / 2.0f;
    }
    public float getBottomBound() {
        return position.getY() - HEIGHT / 2.0f;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
}
