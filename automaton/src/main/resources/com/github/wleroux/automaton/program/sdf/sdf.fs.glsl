#version 450 core

in vec2 TexCoord;
out vec4 color;

uniform sampler2D BaseColor;

uniform vec4 BorderColor;
uniform float BorderWidth;
uniform float BorderEdge;
uniform vec2 BorderOffset;

uniform vec4 Color;
uniform float Width;
uniform float Edge;

void main()
{
    float mainDistance = 1.0 - texture2D(BaseColor, TexCoord).a;
    float mainAlpha = 1.0 - smoothstep(Width, Width + Edge, mainDistance);

    float borderDistance = 1.0 - texture2D(BaseColor, TexCoord + BorderOffset).a;
    float borderAlpha = 1.0 - smoothstep(BorderWidth, BorderWidth + BorderEdge, borderDistance);

    float overallAlpha = mainAlpha + (1.0 - mainAlpha) * borderAlpha;
    vec4 overallColor = mix(BorderColor, Color, mainAlpha / overallAlpha);

    color = vec4(overallColor.rgb, overallAlpha);
}
