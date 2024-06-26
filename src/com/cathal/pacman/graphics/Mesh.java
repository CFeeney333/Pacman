package com.cathal.pacman.graphics;

import com.cathal.pacman.utils.BufferUtils;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh {

    public static final int POS_INDEX = 0;

    private int vaoID, iboID;
    private int count;

//    private float[] positions;
//    private byte[] indices;

    public Mesh(float[] positions, byte[] indices) {
//        this.positions = positions;
//        this.indices = indices;
        this.count = indices.length;

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(positions), GL_STATIC_DRAW);
        glEnableVertexAttribArray(POS_INDEX);
        glVertexAttribPointer(POS_INDEX, 3, GL_FLOAT, false, 0, 0);

        iboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createByteBuffer(indices), GL_STATIC_DRAW);
    }

    public void bind() {
        glBindVertexArray(vaoID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
    }

    public void unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
        glBindVertexArray(0);
    }

    public void render() {
        bind();
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0);
    }
//
//    public float[] getPositions() {
//        return this.positions.clone();
//    }
//
//    public byte[] getIndices() {
//        return this.indices.clone();
//    }
}
