#version 330 core

layout (location = 0) in vec4 vertex;


uniform vec2 screen_size;
uniform vec2 sprite_position;
uniform vec2 sprite_size;

out vec2 uv;

void main(){
    vec2 scaled_vert = vertex.xy * sprite_size;

    vec2 pos_RatioTo1 = (scaled_vert+sprite_position) / screen_size;
    vec2 clip_space = (pos_RatioTo1 * 2.0) - 1;
    clip_space.y *= -1;

    gl_Position = vec4(clip_space, 0.0, 1.0);
    uv = vertex.zw;
}