#version 330

in vec3 color;

out vec4 fragColor;

void main()
{
	fragColor = vec4(color.x, 0.5 , color.y, 1.0);

}