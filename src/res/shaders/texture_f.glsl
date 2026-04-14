#version 330

in vec3 pv;

in vec3 normalV;

in vec2 uv;

out vec3 FragColor;

uniform sampler2D smplr;



void main() {

    vec3 baseColor = texture(smplr, uv).rgb;

    vec3 Lposition = vec3(-10.0, 20.0, 20.0);

    vec3 L = normalize(Lposition - pv);

    vec3 N = normalize(normalV);

    vec3 R = reflect(-L, N);

    vec3 V = normalize(-pv);

    float diffuse = max(dot(L, N), 0.0) * 0.9;

    float specular = pow(max(dot(R, V), 0.0), 10.0);

    FragColor = baseColor * (diffuse + specular);

}