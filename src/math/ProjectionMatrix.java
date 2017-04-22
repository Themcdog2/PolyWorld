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

package math;

public class ProjectionMatrix {

    /*
        NOTE
        The camera itself never moves. The world around the camera moves to give the illusion
     */


    /*
    If w == 1, then the vector (x,y,z,1) is a position in space.
    If w == 0, then the vector (x,y,z,0) is a direction.
     */


    Vector4f camera;



    public ProjectionMatrix(Vector4f camera){

        this.camera = camera;
    }


    // translation*rotation*scale
    public float[][] transform(Vector3f translationVector, float rotationAngleX, float rotationAngleY, float rotationAngleZ, Vector3f scalingVector){

        float[][] combinedRotation = culminateTransforms(rotateX(rotationAngleX), rotateY(rotationAngleY), rotateZ(rotationAngleZ));
        float[][] combinedTransformations = culminateTransforms(translate(translationVector), combinedRotation, scale(scalingVector));
           return combinedTransformations;
        // return calculateCameraTransform(combinedTransformations, camera);


    }




    //Create a transformation function for translation
    private float[][] translate(Vector3f translationVector){

        float[][] translationMatrix = new float[3][3];


        translationMatrix[0][0] = 1;
        translationMatrix[1][1] = 1;
        translationMatrix[2][2] = 1;
        translationMatrix[3][3] = 1; //w is a direction

        translationMatrix[3][0] = translationVector.x;
        translationMatrix[3][1] = translationVector.y;
        translationMatrix[3][2] = translationVector.z;

        return translationMatrix;


    }


    //Create a transformation function for scaling
    private float[][] scale(Vector3f scalingVector){

        float[][] scaleMatrix = new float[3][3];
        scaleMatrix[0][0] = scalingVector.x;
        scaleMatrix[1][1] = scalingVector.y;
        scaleMatrix[2][2] = scalingVector.z;
        scaleMatrix[3][3] = 1; //w is a direction

        return scaleMatrix;

    }




    private float[][] rotateX(float angle){

        float[][] rotationMatrix = new float[3][3];

        rotationMatrix[0][0] = 1;
        rotationMatrix[1][1] = (float)Math.cos(angle);
        rotationMatrix[1][2] = (float)-Math.sin(angle);
        rotationMatrix[2][1] = (float) Math.sin(angle);
        rotationMatrix[2][2] = (float)-Math.cos(angle);
        rotationMatrix[3][3] = 1;

        return rotationMatrix;
    }


    private float[][] rotateY(float angle){

        float[][] rotationMatrix = new float[3][3];

        rotationMatrix[0][0] = (float)Math.cos(angle);
        rotationMatrix[0][2] = (float)Math.sin(angle);
        rotationMatrix[1][1] = 1;
        rotationMatrix[0][2] = (float)-Math.sin(angle);
        rotationMatrix[2][2] = (float)-Math.cos(angle);
        rotationMatrix[3][3] = 1;

        return rotationMatrix;

    }

    private float[][] rotateZ(float angle){

        float[][] rotationMatrix = new float[3][3];

        rotationMatrix[0][0] = (float)Math.cos(angle);
        rotationMatrix[0][1] = (float)-Math.sin(angle);
        rotationMatrix[1][0] = (float)Math.sin(angle);
        rotationMatrix[1][1] = (float)Math.cos(angle);
        rotationMatrix[2][2] = 1;
        rotationMatrix[3][3] = 1;

        return rotationMatrix;
    }




    public float[][] culminateTransforms(float[][] transform1, float[][] transform2){
        return matrixProduct(transform1, transform2);
    }


    public float[][] culminateTransforms(float[][] transform1, float[][] transform2, float[][] transform3){
        return matrixProduct(matrixProduct(transform1, transform2), transform3);
    }


    public float[][] matrixProduct(float[][] matrix1, float[][] matrix2){

        int m1 = matrix1.length;
        int n1 = matrix1[0].length;

        int m2 = matrix2.length;
        int n2 = matrix2[0].length;

        float[][] resultantMatrix = new float[m1][n2];

        for (int i = 0; i < m1; i++) {
            for (int j = 0; j < n2; j++) {
                for (int k = 0; k < n1; k++) {
                    resultantMatrix[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return resultantMatrix;
    }


    //TODO
    //Not elegant, consider revising to an algorithm
    //Multiply a matrix by vector4f
    public Vector4f calculateCameraTransform(float[][] matrix, Vector4f vector){



        float resultX;
        float resultY;
        float resultZ;
        float resultW;

        resultX = ((matrix[0][0] * vector.x) + (matrix[0][1] * vector.y) + (matrix[0][2] * vector.z) + (matrix[0][3] * vector.w));
        resultY = ((matrix[1][0] * vector.x) + (matrix[1][1] * vector.y) + (matrix[1][2] * vector.z) + (matrix[1][3] * vector.w));
        resultZ = ((matrix[2][0] * vector.x) + (matrix[2][1] * vector.y) + (matrix[2][2] * vector.z) + (matrix[2][3] * vector.w));
        resultW = ((matrix[3][0] * vector.x) + (matrix[3][1] * vector.y) + (matrix[3][2] * vector.z) + (matrix[3][3] * vector.w));

        return new Vector4f(resultX, resultY, resultZ, resultW);
    }








    public Vector4f getCamera(){
        return camera;
    }

    public void setCamera(Vector4f camera){
        this.camera = camera;
    }



}
