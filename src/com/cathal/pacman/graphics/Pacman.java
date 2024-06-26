package com.cathal.pacman.graphics;

import com.cathal.pacman.Main;
import com.cathal.pacman.maths.Matrix4f;
import com.cathal.pacman.maths.Vector3f;

public class Pacman implements IGameItem {

    private Mesh mesh;

    private Vector3f position;
    public static final float WIDTH = 0.75f;
    public static final float HEIGHT = 0.75f;
    public static final float BOUNDS_WIDTH = 1.0f;
    public static final float BOUNDS_HEIGHT = 1.0f;

    public Pacman(Vector3f position) {
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
        Shader.PACMAN.bind();
        Shader.PACMAN.setUniformMat4f("pr_matrix", Main.PR_MATRIX);
        this.position = position;
    }

    @Override
    public void render() {
        bind();
        mesh.render();
    }

    @Override
    public void update() {
        Shader.PACMAN.bind();
        Shader.PACMAN.setUniformMat4f("ml_matrix", Matrix4f.translate(position));
    }

    @Override
    public void bind() {
        Shader.PACMAN.bind();
        mesh.bind();
    }

    @Override
    public void unbind() {
        Shader.PACMAN.unbind();
        mesh.unbind();
    }

    public void translate(Vector3f vector) {
        position.setX(position.getX() + vector.getX());
        position.setY(position.getY() + vector.getY());
        position.setZ(position.getZ() + vector.getZ());
    }

    public float getLeftBound() {
        return position.getX() - BOUNDS_WIDTH / 2.0f;
    }

    public float getRightBound() {
        return position.getX() + BOUNDS_WIDTH / 2.0f;
    }

    public float getTopBound() {
        return position.getY() + BOUNDS_HEIGHT / 2.0f;
    }

    public float getBottomBound() {
        return position.getY() - BOUNDS_HEIGHT / 2.0f;
    }

    public float getLeftMesh() {
        return position.getX() - WIDTH / 2.0f;
    }

    public float getRightMesh() {
        return position.getX() + WIDTH / 2.0f;
    }

    public float getTopMesh() {
        return position.getY() + HEIGHT / 2.0f;
    }

    public float getBottomMesh() {
        return position.getY() - HEIGHT / 2.0f;
    }
}
