#version 330 core

layout (location=0) in vec4 position;

uniform mat4 ml_matrix;
uniform mat4 pr_matrix;

void main()
{
    gl_Position = pr_matrix * ml_matrix * position;
}