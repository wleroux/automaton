#version 450 core

layout (location = 0) in vec2 posCoord;
layout (location = 2) uniform mat4 projection;

out vec2 PosCoord;

void main()
{
    gl_Position = projection * vec4(posCoord, 0.0f, 1.0f);
    PosCoord = posCoord;
}
