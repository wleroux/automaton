#version 450 core

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 texCoord;

layout (location = 2) uniform mat4 projection;

out vec2 TexCoord;

void main()
{
    gl_Position = projection * vec4(position, 0.0f, 1.0f);
    TexCoord = texCoord;
}
