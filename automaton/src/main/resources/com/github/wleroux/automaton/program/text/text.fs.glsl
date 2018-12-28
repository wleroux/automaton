#version 450 core

in vec2 TexCoord;
out vec4 color;
layout (location = 3) uniform sampler2D BaseColor;
layout (location = 4) uniform vec4 Color;

const float width = 0.5;
const float edge = 0.2;

void main()
{
    float distance = 1.0 - texture2D(BaseColor, TexCoord).a;
    float alpha = 1.0 - smoothstep(width, width + edge, distance);

    color = vec4(Color.rgb, alpha);
}
