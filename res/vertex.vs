#version 330

layout (location =0) in vec3 position;

out vec3 color;

void main()
{

	gl_Position = vec4(position, 0.1);
	color = position;

}