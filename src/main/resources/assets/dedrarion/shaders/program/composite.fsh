#version 150

uniform sampler2D GlowSampler;
uniform float GlowIntensity;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main() {
    fragColor = texture(GlowSampler, texCoord) * GlowIntensity;
}
