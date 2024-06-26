package com.cathal.pacman.graphics;

import com.cathal.pacman.Main;
import com.cathal.pacman.maths.Matrix4f;
import com.cathal.pacman.maths.Vector3f;

public class Wall implements IGameItem {

    private Mesh mesh;

    private Vector3f position;
    public static final float WIDTH = 1.0f;
    public static final float HEIGHT = 1.0f;

    public Wall(Vector3f position) {
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
        Shader.WALL.bind();
        Shader.WALL.setUniformMat4f("pr_matrix", Main.PR_MATRIX);
        this.position = position;
    }

    @Override
    public void render() {
        bind();
        Shader.WALL.setUniformMat4f("ml_matrix", Matrix4f.translate(position));
        mesh.render();
    }

    @Override
    public void update() {

    }

    @Override
    public void bind() {
        Shader.WALL.bind();
        mesh.bind();
    }

    @Override
    public void unbind() {
        Shader.WALL.unbind();
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

    public void translate(Vector3f vector) {
        position.setX(position.getX() + vector.getX());
        position.setY(position.getY() + vector.getY());
        position.setZ(position.getZ() + vector.getZ());
    }

    public void setPosition(Vector3f vector) {
        position.setX(vector.getX());
        position.setY(vector.getY());
        position.setZ(vector.getZ());
    }
}
