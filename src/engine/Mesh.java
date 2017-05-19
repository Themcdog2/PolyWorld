package engine;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

/**
 * Created by williamallen on 4/6/17.
 */
public class Mesh {

    /*
    1. Create a OpenGL Program
    2. Load the vertex and shader code files.
    3. For each shader, create a new shader program and specify its type (vertex, fragment).
    4. Compile the shader.
    5. Attach the shader to the program.
    6. Link the program.
     */


    private final int vaoId;

    private final int positionsVboId;
    private final int indecesVboId;

    private final int vertexCount;



    public Mesh(float[] positions, int[] indices){

        //GL and java's floats don't really understand each other
        //So we have to put it in a verticesBuffer to transfer the vertex data
        FloatBuffer verticesBuffer = null;
        IntBuffer indicesBuffer = null;
        //3 values per point, divide by 3 to get number of points
        vertexCount = indices.length;

        try{

            //Allocate memory to our vertices and indices buffer
            verticesBuffer = memAllocFloat(positions.length);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);

            //Essentially switches the buffer from write to read.
            verticesBuffer.put(positions).flip();
            indicesBuffer.put(indices).flip();

            //Generate a VAO (Vertex Array Object)
            vaoId = glGenVertexArrays();

            //Bind our VAO to GL
            glBindVertexArray(vaoId);


            //Generate a VBO (Vertex Buffer Object) for positions and indices
            positionsVboId = glGenBuffers();
            indecesVboId = glGenBuffers();

            //Bind our VBOs to our already bound VAO
            glBindBuffer(GL_ARRAY_BUFFER, positionsVboId);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indecesVboId);

            //Tell GL our data is static, meaning it won't ever change after it's been inputted
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            //Index - index of VBO to modify | Size - number of components per vertex
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            //This set's the VAO's index 0 to take in 3 floats per vertex

            //Bind our array to our VAO
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            glBindVertexArray(0);









        } finally {
            if (verticesBuffer  != null) {
                memFree(verticesBuffer);
            }
            if(indicesBuffer != null){
                memFree(indicesBuffer);
            }
        }


    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

}
