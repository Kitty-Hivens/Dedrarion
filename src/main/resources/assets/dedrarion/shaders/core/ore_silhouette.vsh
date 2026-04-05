#version 150

in vec3 Position;
in vec4 Color;

out vec4 vertexColor;

void main() {
    // Positions are pre-multiplied (proj * modelview * local) by the CPU,
    // so they arrive already in clip space.
    gl_Position = vec4(Position, 1.0);
    vertexColor = Color;
}
