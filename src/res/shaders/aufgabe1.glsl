#version 330

out vec3 background;

void rectangle(in float x,in float y,in float width,in float height, vec3 color){
    if(gl_FragCoord.x > x && gl_FragCoord.x < x + width && gl_FragCoord.y > y && gl_FragCoord.y < y + height)
    background = color;
}

void circle(in float x,in float y, in float radius,vec3 color){
    float d = distance(gl_FragCoord.xy, vec2(x,y));
    if(d < radius){
        background = color;
    }
}

mat2 rotate2d(float angle) {
    return mat2(
    cos(angle), -sin(angle),
    sin(angle),  cos(angle)
    );
}

// !!!
void rectangle_rotated(in float x, in float y, in float width, in float height, float angle, vec3 color){
    // Mittelpunkt des Rechtecks
    vec2 center = vec2(x + width*0.5, y + height*0.5);

    // Pixel relativ zum Mittelpunkt
    vec2 pos = gl_FragCoord.xy - center;

    // Rotation anwenden
    pos = rotate2d(angle) * pos;

    // zurückverschieben (optional, da die Prüfung um Mittelpunkt erfolgt)
    vec2 halfSize = vec2(width*0.5, height*0.5);

    if(pos.x > -halfSize.x && pos.x < halfSize.x && pos.y > -halfSize.y && pos.y < halfSize.y)
    background = color;
}
//

/*
void line(in float x, in float y, in float height, float angle, vec3 color){
    // Pixel relativ zum Mittelpunkt
    vec2 pos = gl_FragCoord.xy - x;

    // Rotation anwenden
    pos = rotate2d(angle) * pos;

    // zurückverschieben (optional, da die Prüfung um Mittelpunkt erfolgt)
    vec2 halfSize = vec2(height*0.5);

    if(pos.x > -halfSize.x && pos.x < halfSize.x)
    background = color;
}*/


void main(){
    background = vec3(0.0,0.0,0.0);

    rectangle(350,350,700,700, vec3(1.0,0.0,0.0));

    rectangle_rotated(800,800,400,400, radians(60.0), vec3(1.0,1.0,1.0));

    //line(1000,800,1,radians(120.0), vec3(0.0,1.0,0.0));

    circle(700,700,100, vec3(1.0,0.5,0.5));

    circle(700,700,50, vec3(0.0,0.0,0.0));

}