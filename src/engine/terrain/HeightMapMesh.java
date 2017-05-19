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

import engine.Mesh;
import util.PNGDecoder;
import util.listToArray;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by williamallen on 5/19/17.
 */
public class HeightMapMesh {

    private static final int MAX_COLOUR = 255 * 255 * 255;

    private static final float STARTX = -0.5f;

    private static final float STARTZ = -0.5f;

    float minY;
    float maxY;

    public final Mesh mesh;

    private final float[][] heightArray;


    public HeightMapMesh(float minY, float maxY, String heightMapFile, int width, int height) throws Exception {
        this.minY = minY;
        this.maxY = maxY;

        PNGDecoder decoder = new PNGDecoder(getClass().getClassLoader().getResourceAsStream(heightMapFile));
         int imageWidth = decoder.getWidth();
         int imageHeight = decoder.getHeight();

        ByteBuffer buf = ByteBuffer.allocateDirect(
                4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        buf.flip();

        heightArray = new float[height][width];



        float incx = getXLength() / (width - 1);
        float incz = getZLength() / (height - 1);

        List<Float> positions = new ArrayList();
        List<Integer> indices = new ArrayList();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                // Create vertex for current position
                positions.add(STARTX + col * incx); // x
                float currentHeight = getHeight(col, row, width, buf);
                heightArray[row][col] = currentHeight;
                positions.add(currentHeight); //y
                positions.add(STARTZ + row * incz); //z


                // Create indices
                if (col < width - 1 && row < height - 1) {
                    int leftTop = row * width + col;
                    int leftBottom = (row + 1) * width + col;
                    int rightBottom = (row + 1) * width + col + 1;
                    int rightTop = row * width + col + 1;

                    indices.add(leftTop);
                    indices.add(leftBottom);
                    indices.add(rightTop);

                    indices.add(rightTop);
                    indices.add(leftBottom);
                    indices.add(rightBottom);
                }
            }
        }
        float[] posArr = listToArray.convert(positions);
        int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();
        this.mesh = new Mesh(posArr, indicesArr);
    }


    public float getHeight(int row, int col) {
        float result = 0;
        if ( row >= 0 && row < heightArray.length ) {
            if ( col >= 0 && col < heightArray[row].length ) {
                result = heightArray[row][col];
            }
        }
        return result;
    }

    public static float getXLength() {
        return Math.abs(-STARTX*2);
    }

    public static float getZLength() {
        return Math.abs(-STARTZ*2);
    }


    private float getHeight(int x, int z, int width, ByteBuffer buffer) {
        byte r = buffer.get(x * 4 + 0 + z * 4 * width);
        byte g = buffer.get(x * 4 + 1 + z * 4 * width);
        byte b = buffer.get(x * 4 + 2 + z * 4 * width);
        byte a = buffer.get(x * 4 + 3 + z * 4 * width);
        int argb = ((0xFF & a) << 24) | ((0xFF & r) << 16)
                | ((0xFF & g) << 8) | (0xFF & b);
        return this.minY + Math.abs(this.maxY - this.minY) * ((float) argb / (float) MAX_COLOUR);
    }
}
