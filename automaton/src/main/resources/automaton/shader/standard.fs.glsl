#version 450 core

in vec2 TexCoord;
out vec4 color;

layout (location = 3) uniform sampler2D baseColorTexture;

const float aoStrength = 0.2;

void main() {
    vec3 baseColor = texture(baseColorTexture, TexCoord).rgb;
    color = vec4(baseColor.rgb, 1.0);
}
