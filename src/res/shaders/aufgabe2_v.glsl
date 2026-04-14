#version 330

//für die Rotation
layout(location = 0) in vec2 eckenAusJava;

//für die Farbe
layout(location = 1) in vec3 ourColor;

out vec3 vertexColor;

mat2 rotate2d(float angle) {
    return mat2(
    cos(angle), -sin(angle),
    sin(angle),  cos(angle)
    );
}

void main() {
    float angle = 0.9;
    mat2 rotation = rotate2d(angle);

    gl_Position = vec4(rotation * eckenAusJava, 0.0, 1.0);
    vertexColor = ourColor;
}



