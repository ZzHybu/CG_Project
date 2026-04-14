#version 330
in vec3 VertexColor;
in vec3 pv;
in vec3 normalV;
in vec2 uv;

out vec3 FragColor;

uniform sampler2D smplr;
uniform int useTexture;

void main(){
    vec3 baseColor;

    if (useTexture == 1){
        baseColor = texture(smplr, uv).rgb;
    } else {
        baseColor = VertexColor;
    }

    vec3 texel = vec3(texture(smplr, uv)).rgb;

    vec3 Lposition = vec3(-10.0,20.0,20.0);

    vec3 L = normalize(Lposition - pv);

    vec3 N = normalize(normalV);

    vec3 R = reflect(-L, N);

    vec3 V = normalize(-pv);

    //vec3 I = texel * (max(dot(L,N),0.0) * 0.9 + pow(max(dot(R,V),0.0),20.0));

    //vec3 I = VertexColor * (max(dot(L,N),0.0) * 0.9 + pow(max(dot(R,V),0.0),20.0));

    float diffuse = max(dot(L, N), 0.0) * 0.9;
    float specular = pow(max(dot(R, V), 0.0), 10.0);

    FragColor = baseColor * (diffuse + specular);

    //FragColor = I;
}