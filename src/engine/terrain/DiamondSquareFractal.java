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

package engine.terrain;

import java.util.Random;

/**
 * Created by williamallen on 5/18/17.
 */

//https://sdm.scad.edu/faculty/mkesson/vsfx419/wip/best/winter12/jonathan_mann/noise.html

public class DiamondSquareFractal {

    public float[][] texture;
    public int width;
    public int height;


    public DiamondSquareFractal(long mseed, int n) {
        //value 2^n+1
        int DATA_SIZE = (int)Math.pow(2, n) + 1;
        width = DATA_SIZE;
        height = DATA_SIZE;
        //an initial seed value for the corners of the texture
        final float seed = 1000.0f;
        texture = new float[DATA_SIZE][DATA_SIZE];
        //seed the texture
        texture[0][0] = texture[0][DATA_SIZE-1] = texture[DATA_SIZE-1][0] =
                texture[DATA_SIZE-1][DATA_SIZE-1] = seed;

        float valmin = Float.MAX_VALUE;
        float valmax = Float.MIN_VALUE;

        float h = 500.0f;// average offset
        Random r = new Random(mseed);
        //side length is distance of a single square side
        //or distance of diagonal in diamond
        for(int sideLength = DATA_SIZE-1; sideLength >= 2; sideLength /=2, h/= 2.0){
            int halfSide = sideLength/2;


            for(int x=0;x<DATA_SIZE-1;x+=sideLength){
                for(int y=0;y<DATA_SIZE-1;y+=sideLength){
                    float avg = texture[x][y] + //top left
                            texture[x+sideLength][y] +//top right
                            texture[x][y+sideLength] + //lower left
                            texture[x+sideLength][y+sideLength];//lower right
                    avg /= 4.0;

                    texture[x+halfSide][y+halfSide] =
                            avg + (r.nextFloat()*2*h) - h;

                    valmax = Math.max(valmax, texture[x+halfSide][y+halfSide]);
                    valmin = Math.min(valmin, texture[x+halfSide][y+halfSide]);
                }
            }


            for(int x=0;x<DATA_SIZE-1;x+=halfSide){
                for(int y=(x+halfSide)%sideLength;y<DATA_SIZE-1;y+=sideLength){
                    float avg =
                            texture[(x-halfSide+DATA_SIZE-1)%(DATA_SIZE-1)][y] + //left of center
                                    texture[(x+halfSide)%(DATA_SIZE-1)][y] + //right of center
                                    texture[x][(y+halfSide)%(DATA_SIZE-1)] + //below center
                                    texture[x][(y-halfSide+DATA_SIZE-1)%(DATA_SIZE-1)]; //above center
                    avg /= 4.0;
                    avg = avg + (r.nextFloat()*2*h) - h;
                    //update value for center of diamond
                    texture[x][y] = avg;

                    valmax = Math.max(valmax, avg);
                    valmin = Math.min(valmin, avg);
                    if(x == 0)  texture[DATA_SIZE-1][y] = avg;
                    if(y == 0)  texture[x][DATA_SIZE-1] = avg;
                }
            }
        }


        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                texture[i][j] = (texture[i][j] - valmin) / (valmax - valmin);
            }
        }

    }

}
