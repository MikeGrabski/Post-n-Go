package com.example.iosuser11.postonwall.GL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.iosuser11.postonwall.MotionSensors;
import com.example.iosuser11.postonwall.GL.util.MatrixHelper;
import com.example.iosuser11.postonwall.GL.util.TextureHelper;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.*;

public class PictureRenderer implements GLSurfaceView.Renderer
{
    private final Context context;

    private ArrayList<Table> tableList;
    private Table previewTable;

    private float[] identMatrix = new float[16];
    private float[] projectionMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] modelMatrix = new float[16];
    private float[] finalTransformMatrix = new float[16];

    private float[] oneTimeFTM = new float[16];
    private float[] oneTimeVM = new float[16];
    private float[] oneTimeMM = new float[16];


    private MotionSensors motionSensors;
    float[] rotationMatrix = new float[16];
    float[] matCache = new float[16];
    float[] matCacheTranspose = new float[16];

    float[] result = new float[16];

    private boolean pttvel = false;
    private boolean afterelseif = false;

    private float d = 10;


    private Bitmap updatedImage;
    private Context mContext;
    private Boolean updateImage = false;
//    double translateX;
//    double translateY;

    public PictureRenderer(Activity activity) {
        this.context = activity.getApplicationContext();
        motionSensors = new MotionSensors(activity);

        tableList = new ArrayList<>();

        setIdentityM(identMatrix, 0);
        modelMatrix = identMatrix.clone();
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        glViewport(0, 0, width, height);
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 1000f);
        setLookAtM(viewMatrix, 0,
                0f, 0f, 0.1f,
                0f, 0f, 0f,
                0f, 1f, 0f);
    }


    @Override
    public void onDrawFrame(GL10 glUnused) {
        glClear(GL_COLOR_BUFFER_BIT);
        GLES20.glEnable(GL10.GL_CULL_FACE);     // enable face culling feature
        GLES20.glCullFace(GL10.GL_BACK);        // specify which faces to not draw

        rotationMatrix = motionSensors.getRotationMatrix();
//        float[] translateXYZ = motionSensors.getTranslationXYZ();



        if(afterelseif && pttvel && rotationMatrix != null) {
            //translation due to rotation and seekbar
            for(Table a : tableList) {
                oneTimeFTM = a.getFinalTransformMatrix();
                oneTimeMM = a.getModelMatrix();
                oneTimeVM = a.getViewMatrix();

                oneTimeMM = modelMatrix;
                oneTimeVM = viewMatrix;
                oneTimeFTM = finalTransformMatrix;

                multiplyMM(oneTimeFTM, 0, projectionMatrix, 0, identMatrix, 0);
                multiplyMM(oneTimeFTM, 0, oneTimeFTM, 0, oneTimeVM, 0);
                multiplyMM(oneTimeFTM, 0, oneTimeFTM, 0, modelMatrix, 0);

                transposeM(matCacheTranspose, 0, a.getMatCache(), 0);
                multiplyMM(result, 0, rotationMatrix, 0, matCacheTranspose, 0);
                translateM(oneTimeFTM, 0, -(a.getD())*result[8],  -(a.getD())*result[9],  -(a.getD())*result[10]);

                //rotation
                multiplyMM(oneTimeFTM, 0, oneTimeFTM, 0, rotationMatrix, 0);
                multiplyMM(oneTimeFTM, 0, oneTimeFTM, 0, matCacheTranspose, 0);

                a.setFinalTransformMatrix(oneTimeFTM);
                a.setModelMatrix(oneTimeMM);
                a.setViewMatrix(oneTimeVM);

                finalTransformMatrix = oneTimeFTM;
                viewMatrix = oneTimeVM;
                modelMatrix = oneTimeMM;


                a.draw();

                a.draw(oneTimeFTM);
            }
        } else if (!pttvel || !afterelseif) {


            matCache = motionSensors.getRotationMatrix();
            translateM(finalTransformMatrix, 0, 0, 0, -(d));
            afterelseif = true;
//            for(int i = 0; i < tableList.size(); i++) {
//                tableList.get(i).draw(finalTransformMatrix);
//            }
        }

        if(updateImage) {
            multiplyMM(finalTransformMatrix, 0, projectionMatrix, 0, identMatrix, 0);
            multiplyMM(finalTransformMatrix, 0, finalTransformMatrix, 0, viewMatrix, 0);
            multiplyMM(finalTransformMatrix, 0, finalTransformMatrix, 0, modelMatrix, 0);
            matCache = motionSensors.getRotationMatrix();

            tableList.add(new Table(mContext, updatedImage,  d,matCache));
            updateImage =false;
        }


    }

    public void attachToWall() {
        pttvel = true;
    }

    public void updateDistance(int d) {
        this.d = (float) d;
    }

    public void addTable(Context c, Bitmap b, int d) {
        updatedImage = b;
        mContext = c;
        updateDistance(d);

        updateImage = true;
        previewTable = new Table(c,b,d,matCache);

    }
}