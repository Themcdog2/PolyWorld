package engine;

import engine.terrain.HeightMapMesh;
import org.lwjgl.opengl.GL20;
import util.resourceLoader;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 * Created by williamallen on 4/5/17.
 */
public class Renderer {

    Window window;
    Shader shaderProgram;
    Mesh mesh;
    resourceLoader loader = new resourceLoader();
    HeightMapMesh mapMesh;

    float time;

    public Renderer(Window window){
    this.window = window;
    init();



    }

    public void init(){


        float[] positions = new float[]{
                -0.5f,  0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f,  0.5f, 0.0f,
        };
        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,
        };
        mesh = new Mesh(positions, indices);
        try {
             mapMesh = new HeightMapMesh(0, 10, "noise.png", 100, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
       //mesh = mapMesh.mesh;
        System.out.println(mesh.getVertexCount());
        shaderProgram = new Shader();
        try {
            shaderProgram.createVertexShader(loader.loadFile("vertex.vs"));
            shaderProgram.createFragmentShader(loader.loadFile("fragment.fs"));
            shaderProgram.link();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render(){

        time+=0.03;
       // System.out.println(time);


        shaderProgram.bind();
        setUniformVariables(shaderProgram.programID);
        // Draw the mesh
        glBindVertexArray(mesh.getVaoId());
        glEnableVertexAttribArray(0);
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shaderProgram.unbind();

        // swap the color buffers
        glfwSwapBuffers(window.windowHandle);
    }
    public void setUniformVariables(int programID) {

        int loc1 = GL20.glGetUniformLocation(programID, "u_time");
        GL20.glUniform1f(loc1, time * 2);

        int loc2 = GL20.glGetUniformLocation(programID, "u_resolution");
        GL20.glUniform2f(loc2, 500, 500);

    }


    public void clear(){

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}
