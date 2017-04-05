package engine;

import org.lwjgl.opengl.GL;

import javax.swing.plaf.synth.SynthEditorPaneUI;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by williamallen on 4/5/17.
 */
public class Engine implements Runnable{

    private Renderer renderer;
    private Window window;

    private Thread gameThread;





    public Engine(Renderer renderer, Window window){

        this.renderer = renderer;
        this.window = window;

        init();

    }


    private void init(){
        //Create our game thread and name it
        gameThread = new Thread(this, "GAME_THREAD");


        //Weird quirk with GLFW
        //Multithreading on mac behaves strangely with GLFW
        //So if we run on a mac we have to start the thread differently
        String osName = System.getProperty("os.name");
        if(osName.contains("Mac")) {
            gameThread.run();
        }else{
            gameThread.start();
        }
    }




    @Override
    public void run() {
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and opens the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        // ALL GL stuff must be done after this call


        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);


        // Run the rendering loop until the user presses esc
        while ( !glfwWindowShouldClose(window.windowHandle) ) {

            // clear the framebuffer
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // swap the color buffers
            glfwSwapBuffers(window.windowHandle);


            // Poll for window events.
            // Key callback will be polled here
            glfwPollEvents();
        }
        System.exit(0);



    }


    //GL and GLFW don't clean up after themselves
    //So we have to do it
    public void cleanup(){

    }
}
