package engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by williamallen on 4/4/17.
 */
public class Window {
    long windowHandle;
    private GLFWWindowSizeCallback windowSizeCallback;
    private GLFWWindowCloseCallback windowCloseCallback;
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;

    private int width;
    private int height;
    private String title;

    private boolean vSync;
    private boolean resized;
    private boolean windowShouldClose;


    public Window(int width, int height, String title, boolean vSync){

        this.width = width;
    this.height = height;
    this.title = title;

    init();
    }

    public void init(){

        //Fancy lambda expression
        //This just forwards all GLFW errors to our console
       // errorCallback = glfwSetErrorCallback((error, description) -> System.err.println(error + " " + description));



        //Init GLFW.. most methods won't work without this
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        //Set all our GLFW errors to kickback to the console
        //This expression might be able to be done with a lambda expression
        errorCallback = GLFWErrorCallback.createPrint(System.err);


        //Set the window to default hints
        glfwDefaultWindowHints();





        // GLFW_TRUE is a static constant for 1

        //This is an OSX thing
        //GLFW Will NOT work without these certain parameters
        //Ofc there's next to no documentation on why
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);


        // the window should be resizable
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);



        //Window is focused by default
        glfwWindowHint(GLFW_FOCUSED, GLFW_TRUE);

        //Window is visible by default (Duh)
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);







        //Create the window
       windowHandle = glfwCreateWindow(width, height, "Test", NULL, NULL);
       //Even the documentation says to put the last arg as NULL, wtf why is it even there?

        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }




        //Set our window callback here
        //Fancy anonymous inner type oooh
        windowCloseCallback = glfwSetWindowCloseCallback(windowHandle, windowCloseCallback = new GLFWWindowCloseCallback(){
            @Override
            public void invoke(long window) {
                //Do something with closing the window
            }
        });

        //Set our key callback here
        //Fancy anonymous inner type oooh
        keyCallback = glfwSetKeyCallback(windowHandle, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                //Do something with key interactions..

                //If the user presses escape AND releases then close the window
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, true);
            }
        });

        // Setup resize callback
        //Fancy anonymous inner type oooh
        glfwSetWindowSizeCallback(windowHandle, windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {



                Window.this.width = width;
                Window.this.height = height;
                Window.this.setResized(true);



            }
        });



        //Make this context current
        //If we have multiple things running openGL, the graphics card needs to know which to draw to
        glfwMakeContextCurrent(windowHandle);

        //Swap in according to vSync
        glfwSwapInterval(1);

        //Show the window.. duh
        glfwShowWindow(windowHandle);




    }



    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public boolean isResized(){
        return resized;
    }

    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }







}
