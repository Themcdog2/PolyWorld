/*
 * MIT License
 *
 * Copyright (c) 2017 William Allen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package engine;


import static org.lwjgl.opengl.GL20.*;

public class Shader {


    //GL handles for shaders
    public int programID;
    private int vertexID;
    private int fragmentID;



    public Shader() {

        createShaderProgram();

    }

    //Create shader management program
    public void createShaderProgram(){
        programID = glCreateProgram();
        if(programID == 0){
            System.out.println("Could not create shader program");
        }
    }

    //Create vertex shader
    public void createVertexShader(String shaderCode) throws Exception{
        createShader(GL_VERTEX_SHADER, shaderCode);

    }

    //Create fragment shader
    public void createFragmentShader(String shaderCode) throws Exception{
        createShader(GL_FRAGMENT_SHADER, shaderCode);
    }



    public int createShader(int shaderType, String shaderCode) throws Exception{
        int shaderID = glCreateShader(shaderType);
        if(shaderID == 0){
            System.out.println("Error in creating shader");
        }


        //Attach the shader source, then attempt to compile it
        glShaderSource(shaderID, shaderCode);
        glCompileShader(shaderID);

        //Displays and encountered errors while compiling
        if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling shader: " + glGetShaderInfoLog(shaderID, 2048));
        }

        //Attach the shader to the management program
        glAttachShader(programID, shaderID);

        return shaderID;

    }
    public void link() throws Exception {

        //Like our management program to our GL instance
        glLinkProgram(programID);
        System.out.println(programID);
        if (glGetProgrami(programID, GL_LINK_STATUS) == 0) {
            String s = glGetProgramInfoLog(programID, Integer.MAX_VALUE);
            System.out.println(s.length());
            System.out.println(s);
            throw new Exception("Error linking shader: " + glGetProgramInfoLog(programID, 2048));

        }

        if (vertexID != 0) {
            glDetachShader(programID, vertexID);
        }
        if (fragmentID != 0) {
            glDetachShader(programID, fragmentID);
        }


        //Debugging purposes only, remove in production
        glValidateProgram(programID);


        if (glGetProgrami(programID, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating shader: " + glGetProgramInfoLog(programID, 2048));
        }

    }

    public void bind() {
        glUseProgram(programID);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        //Messy GL is messy. Gotta clean up
        unbind();
        if (programID != 0) {
            glDeleteProgram(programID);
        }
    }

}
