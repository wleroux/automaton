#version 450 core

in vec2 TexCoord;
out vec4 color;

uniform sampler2D BaseColor;

void main()
{
    color = texture2D(BaseColor, TexCoord);
}
