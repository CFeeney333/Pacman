package com.cathal.pacman.graphics;

import com.cathal.pacman.Main;
import com.cathal.pacman.maths.Matrix4f;
import com.cathal.pacman.maths.Vector3f;

public class Ghost implements IGameItem {

    private Mesh mesh;

    private Vector3f position;
    public static final float WIDTH = 0.75f;
    public static final float HEIGHT = 0.75f;
    public static final float BOUNDS_WIDTH = 1.0f;
    public static final float BOUNDS_HEIGHT = 1.0f;
    private Direction facing;
    private float speed;

    public Direction getFacing() {
        return facing;
    }

    public void setFacing(Direction direction) {
        this.facing = direction;
    }

    public Ghost(Vector3f position, Direction direction, float speed) {
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
        Shader.GHOST.bind();
        Shader.GHOST.setUniformMat4f("pr_matrix", Main.PR_MATRIX);
        this.position = position;
        this.facing = direction;
        this.speed = speed;
    }

    @Override
    public void render() {
        bind();
        Shader.GHOST.setUniformMat4f("ml_matrix", Matrix4f.translate(position));
        mesh.render();
    }

    @Override
    public void update() {

    }

    @Override
    public void bind() {
        Shader.GHOST.bind();
        mesh.bind();
    }

    @Override
    public void unbind() {
        Shader.GHOST.unbind();
        mesh.unbind();
    }

    public void move() {
        switch (this.facing) {
            case EAST:
                position.setX(position.getX() + speed);
                break;
            case WEST:
                position.setX(position.getX() - speed);
                break;
            case NORTH:
                position.setY(position.getY() + speed);
                break;
            case SOUTH:
                position.setY(position.getY() - speed);
                break;
        }
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
