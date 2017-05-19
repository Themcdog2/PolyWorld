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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by williamallen on 5/19/17.
 */
public class Noise {

    public static BufferedImage im;

    public Noise(){
        im = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB);
    }

    public static void generateNoise(){
        DiamondSquareFractal fractal = new DiamondSquareFractal((long)Math.random() * Long.MAX_VALUE, 9);
        for(int x = 0; x < fractal.texture.length; x++){
            for(int y = 0; y < fractal.texture[x].length; y++){
                // System.out.print(fractal.data[x][y]*255 + " ");

                float value = fractal.texture[x][y]*255;
                if(value > 255){
                    value = 255;
                }
                if(value < 0){
                    value = 0;
                }


                //System.out.print(value + "  ");
                int castedValue = (int)value;
                im.setRGB(x, y, new Color(castedValue, castedValue ,castedValue).getRGB());
            }
            System.out.println();
        }

        try {
            ImageIO.write(im, "png", new File("res/noise.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
