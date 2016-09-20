package com.example.iosuser11.postonwall.GL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.ViewGroup;

import com.example.iosuser11.postonwall.GL.util.MatrixHelper;
import com.example.iosuser11.postonwall.MotionSensors;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;

/**
 * Created by iosuser11 on 8/19/16.
 */
public class PictureView extends GLSurfaceView{

    private int parentWidth;
    private int parentHeight;

    public PictureView(Context context, int width, int height) {
        super(context);
        parentHeight = width;
        parentWidth = height;
        setLayoutParams(new ViewGroup.LayoutParams(parentWidth, parentHeight));
    }

    }