package com.cathal.pacman.maths;

public class Vector1f {

    private float value;

    public Vector1f(float value) {
        this.value = value;
    }

    public Vector1f() {
        this(0.0f);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
