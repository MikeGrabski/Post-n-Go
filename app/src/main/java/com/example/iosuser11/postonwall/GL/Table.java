package com.example.iosuser11.postonwall.GL;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.example.iosuser11.postonwall.GL.util.TextureHelper;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;

    import android.content.Context;
    import android.graphics.Bitmap;
    import android.util.DisplayMetrics;

    import com.example.iosuser11.postonwall.GL.util.TextureHelper;

    import static android.opengl.GLES20.GL_TRIANGLE_FAN;
    import static android.opengl.GLES20.glDrawArrays;


    public class Table {
        private static final int POSITION_COMPONENT_COUNT = 2;
        private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
        private static final int BYTES_PER_FLOAT = 4;
        private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

        private Context context;

        private final VertexArray vertexArray;
        private Bitmap bitmap;
        private TextureShaderProgram textureProgram;


        private int texture;
        private float[] matCache = new float[16];


        private float d = 10;
        private float[] finalTransformMatrix = new float[16];

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

        public Table(Context c, Bitmap b, float updatedD, float[] matCache) {
            context = c;

            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

            float xdpi = displayMetrics.xdpi;
            float ydpi = displayMetrics.ydpi;
            bitmap = b;

            d = updatedD;

            this.matCache = matCache;


            float width = b.getWidth();
            float height = b.getHeight();

            final float[] VERTEX_DATA = {
                    // Order of coordinates:
                    // X,                   Y,                              S,     T
                    0f, 0f, 0.5f, 0.5f,
                    (-width / 2f) / xdpi, (-height / 2f) / ydpi, 0f, 1f,
                    (width / 2f) / xdpi, (-height / 2f) / ydpi, 1f, 1f,
                    (width / 2f) / xdpi, (height / 2f) / ydpi, 1f, 0f,
                    (-width / 2f) / xdpi, (height / 2f) / ydpi, 0f, 0f,
                    (-width / 2f) / xdpi, (-height / 2f) / ydpi, 0f, 1f
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

        public void draw() {
            textureProgram.useProgram();
            textureProgram.setUniforms(finalTransformMatrix, texture);
            bindData(textureProgram);
            glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
        }

    }