#version 330
in vec3 VertexColor;
in vec3 pv;
in vec3 normalV;

out vec3 FragColor;

void main() {

    vec3 Lposition = vec3(-10.0, 20.0, 20.0);
    vec3 L = normalize(Lposition - pv);
    vec3 N = normalize(normalV);
    vec3 R = reflect(-L, N);
    vec3 V = normalize(-pv);

    /*float diffuse = max(dot(L, N), 0.0) * 0.9;
    float specular = pow(max(dot(R, V), 0.0), 10.0);

    FragColor = VertexColor * (diffuse + specular);*/

    vec3 I = VertexColor * (max(dot(L,N),0.0) * 0.9 + pow(max(dot(R,V),0.0),10.0));
    FragColor = I;
}