package com.cathal.pacman.maths;

import com.cathal.pacman.utils.BufferUtils;

import java.nio.FloatBuffer;

@SuppressWarnings("PointlessArithmeticExpression")
public class Matrix4f {

    public static final int SIZE = 4 * 4;

    private float[] elements = new float[SIZE];

    private Matrix4f() {

    }

    public static Matrix4f identity() {
        Matrix4f result = new Matrix4f();

        result.elements[0 + 0 * 4] = 1.0f;
        result.elements[1 + 1 * 4] = 1.0f;
        result.elements[2 + 2 * 4] = 1.0f;
        result.elements[3 + 3 * 4] = 1.0f;

        return result;
    }

    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f result = Matrix4f.identity();

        result.elements[0 + 0 * 4] = 2 / (right - left);
        result.elements[1 + 1 * 4] = 2 / (top - bottom);
        result.elements[2 + 2 * 4] = 2 / (near - far);

        result.elements[0 + 3 * 4] = (left + right) / (left - right);
        result.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
        result.elements[2 + 3 * 4] = (far + near) / (far - near);

        return result;
    }

    public static Matrix4f translate(Vector3f vector) {
        Matrix4f result = Matrix4f.identity();

        result.elements[0 + 3 * 4] = vector.getX();
        result.elements[1 + 3 * 4] = vector.getY();
        result.elements[2 + 3 * 4] = vector.getZ();

        return result;
    }

    public FloatBuffer toFloatBuffer() {
        return BufferUtils.createFloatBuffer(elements);
    }
}
