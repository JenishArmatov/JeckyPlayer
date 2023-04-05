package com.armatov.music.visualizermusicplayer.Visualizer;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
public class VisualizerRenderer  implements GLSurfaceView.Renderer {
    private Context context;
    private int programId;
    private int positionHandle;
    private int colorHandle;
    private int mvpMatrixHandle;
    private float[] projectionMatrix = new float[16];
    private float[] modelMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] mvpMatrix = new float[16];
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private int vertexCount;
    private float[] fftData;
    private float[] vertices;
    private float[] colors;
    private int[] colorRanges = {0xFF3300, 0xFF6600, 0xFF9900, 0xFFCC00, 0xFFFF00, 0xCCFF00, 0x99FF00, 0x66FF00, 0x33FF00, 0x00FF00};

    public VisualizerRenderer(Context context, float[] fftData) {
        this.context = context;
        this.fftData = fftData;
        vertices = new float[fftData.length * 2];
        colors = new float[fftData.length * 4];
        vertexCount = fftData.length / 4;
        ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        vertexByteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = vertexByteBuffer.asFloatBuffer();
        ByteBuffer colorByteBuffer = ByteBuffer.allocateDirect(colors.length * 4);
        colorByteBuffer.order(ByteOrder.nativeOrder());
        colorBuffer = colorByteBuffer.asFloatBuffer();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        String vertexShaderSource = "attribute vec4 a_Position;\n" +
                "uniform mat4 u_MvpMatrix;\n" +
                "void main() {\n" +
                "    gl_Position = u_MvpMatrix * a_Position;\n" +
                "}";
        String fragmentShaderSource = "precision mediump float;\n" +
                "uniform vec4 u_Color;\n" +
                "void main() {\n" +
                "    gl_FragColor = u_Color;\n" +
                "}";
        int vertexShaderId = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource);
        int fragmentShaderId = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource);
        programId = GLES20.glCreateProgram();
        GLES20.glAttachShader(programId, vertexShaderId);
        GLES20.glAttachShader(programId, fragmentShaderId);
        GLES20.glLinkProgram(programId);
        positionHandle = GLES20.glGetAttribLocation(programId, "a_Position");
        colorHandle = GLES20.glGetUniformLocation(programId, "u_Color");
        mvpMatrixHandle = GLES20.glGetUniformLocation(programId, "u_MvpMatrix");
        GLES20.glUseProgram(programId);
    }
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 100);
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3, 0, 0, 0, 0, 1, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        float scale = 1.0f / 256.0f;
        for (int i = 0; i < vertexCount; i++) {
            float x = ((float) i / (float) vertexCount) * 2.0f - 1.0f;
            float y = fftData[i] * scale * 10.0f;
            vertices[i * 2] = x;
            vertices[i * 2 + 1] = y;
            int colorIndex = (int) (y * 10.0f);
            if (colorIndex < 0) colorIndex = 0;
            if (colorIndex >= colorRanges.length) colorIndex = colorRanges.length - 1;
            int color = colorRanges[colorIndex];
            colors[i * 4] = ((color >> 16) & 0xFF) / 255.0f;
            colors[i * 4 + 1] = ((color >> 8) & 0xFF) / 255.0f;
            colors[i * 4 + 2] = (color & 0xFF) / 255.0f;
            colors[i * 4 + 3] = 0.8f;
        }
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        colorBuffer.put(colors);
        colorBuffer.position(0);
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);
        for (int i = 0; i < vertexCount - 1; i++) {
            GLES20.glUniform4fv(colorHandle, 1, colorBuffer);
            GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, i, 2);
        }
    }

    private int loadShader(int type, String source) {
        int shaderId = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shaderId, source);
        GLES20.glCompileShader(shaderId);
        return shaderId;
    }
}
