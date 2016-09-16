package com.example.iosuser11.postonwall.GL;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.iosuser11.postonwall.GL.util.TextureHelper;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;


public class Table {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final VertexArray vertexArray;

    private Bitmap bitmap;
    private TextureShaderProgram textureProgram;
    private int texture;


    private float[] matCache = new float[16];
    private float d = 10;


    private float[] finalTransformMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] modelMatrix = new float[16];

    public void setModelMatrix(float[] modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    public float[] getModelMatrix() {
        return modelMatrix;
    }

    public float[] getViewMatrix() {
        return viewMatrix;
    }

    public float[] getFinalTransformMatrix() {
        return finalTransformMatrix;
    }

    public void setFinalTransformMatrix(float[] finalTransformMatrix) {
        this.finalTransformMatrix = finalTransformMatrix;
    }

    public void setViewMatrix(float[] viewMatrix) {
        this.viewMatrix = viewMatrix;
    }


    public void setD(int d) {
        this.d = d;
    }

    public void setMatCache(float[] matCache) {
        this.matCache = matCache;
    }

    public float getD() {
        return d;
    }

    public float[] getMatCache() {
        return matCache;
    }


    private Context context;

    public Table(Context c, Bitmap b, float updatedD, float[] matCache)
    {
        context = c;

        bitmap = b;

        d=updatedD;

        this.matCache = matCache;


        float width = b.getWidth();
        float height = b.getHeight();

        final float[] VERTEX_DATA = {
                // Order of coordinates:
                // X,            Y,                       S,     T
                0f,              0f,                      0.5f,  0.5f,
                -width / 900f,   -height / 900f,           0f,    1f,
                width / 900f,   -height / 900f,           1f,    1f,
                width / 900f,    height / 900f,           1f,    0f,
                -width / 900f,    height / 900f,           0f,    0f,
                -width / 900f,   -height / 900f,           0f,    1f
        };

        vertexArray = new VertexArray(VERTEX_DATA);

        textureProgram = new TextureShaderProgram(context);
        texture = TextureHelper.loadTexture(context, bitmap);
        textureProgram.useProgram();


    }

    public void bindData(TextureShaderProgram textureProgram) {
        vertexArray.setVertexAttribPointer(0, textureProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, textureProgram.getTextureCoordinatesAttributeLocation(), TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
    }

    public void draw(float[] finalTransformMatrix) {
        textureProgram.useProgram();
        textureProgram.setUniforms(finalTransformMatrix, texture);
        bindData(textureProgram);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }
    public void draw(){
        textureProgram.useProgram();
        textureProgram.setUniforms(finalTransformMatrix, texture);
        bindData(textureProgram);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }

}