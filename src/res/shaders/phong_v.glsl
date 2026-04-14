#version 330
layout(location=0) in vec4 eckenAusJava;
layout(location=1) in vec3 aColor;
layout(location=2) in vec3 aNormal;

uniform mat4 modelMatrix;
uniform mat4 persMatrix;

out vec3 VertexColor;
out vec3 pv;
out vec3 normalV;

void main() {
    vec4 worldPos = modelMatrix * eckenAusJava;
    pv = worldPos.xyz;
    mat3 normalMatrix = transpose(inverse(mat3(modelMatrix)));
    normalV = normalMatrix * aNormal;

    VertexColor = aColor;
    gl_Position = persMatrix * worldPos;
}