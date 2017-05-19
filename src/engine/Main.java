package engine;

import engine.terrain.Noise;

/**
 * Created by williamallen on 4/4/17.
 */
public class Main{
    private static Window window;
    private Engine gameEngine;
    private Renderer renderer;


    public static void main(String[] args){
         //GLFW must be initialized in the main thread for whatever reason


        Noise noise = new Noise();
        noise.generateNoise();

         window = new Window(300, 300, "Title", true);
        new Main();

    }

    public Main(){
        //Create our renderer and engine


        gameEngine = new Engine(renderer, window);
    }




}
