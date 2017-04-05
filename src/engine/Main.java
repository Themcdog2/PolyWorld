package engine;

/**
 * Created by williamallen on 4/4/17.
 */
public class Main{
    private static Window window;
    private Engine gameEngine;
    private Renderer renderer;


    public static void main(String[] args){
         //GLFW must be initialized in the main thread for whatever reason
         window = new Window(300, 300, "Title", true);
            new Main();

    }

    public Main(){
        //Create our renderer and engine
        renderer = new Renderer(window);
        gameEngine = new Engine(renderer, window);
    }


}
