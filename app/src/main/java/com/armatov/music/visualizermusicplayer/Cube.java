package com.armatov.music.visualizermusicplayer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Cube {

    private float[][] colors = {  // Colors of the 6 faces
            {1.0f, 0.5f, 0.0f, 1.0f},  // 0. orange
            {1.0f, 0.0f, 1.0f, 1.0f},  // 1. violet
            {0.0f, 1.0f, 0.0f, 1.0f},  // 2. green
            {0.0f, 0.0f, 1.0f, 1.0f},  // 3. blue
            {1.0f, 0.0f, 0.0f, 1.0f},  // 4. red
            {1.0f, 1.0f, 0.0f, 1.0f}   // 5. yellow
    };



    // Constructor - Set up the buffers
    public Cube() {
        // Setup vertex-array buffer. Vertices in float. An float has 4 bytes
        // Rewind
    }

    // Draw the color cube
    public void draw(GL10 gl, float fft[], int column) {
        float columnX = 10;
        int  angle = 90;

        float x = 5;
        float y = 5;
        float z = 5;
        gl.glTranslatef(-100.0f, 0.0f, 0.0f);

        for(int i = 0; i < 50; i++){

            float newFft = fft[i]/5;

            float[] vertices = {  // Vertices for the front face
                    -x, -y,     z,  // 0. left-bottom-front
                    x, -y,     z,  // 1. right-bottom-front
                    -x,  y + newFft, z,  // 2. left-top-front
                    x,  y + newFft, z   // 3. right-top-front
            };

            ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()); // Use native byte order
            // Buffer for vertex-array
            FloatBuffer vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
            vertexBuffer.put(vertices);
            // Copy data into buffer
            vertexBuffer.position(0);

            gl.glFrontFace(GL10.GL_CCW);    // Front face in counter-clockwise orientation
            gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
            gl.glCullFace(GL10.GL_BACK);    // Cull the back face (don't display)

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

            // Front
            //  gl.glColor4f(colors[0][0], colors[0][1], colors[0][2], colors[0][3]);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

            // Right - Rotate 90 degree about y-axis
            gl.glRotatef(90.0f, 0.0f, 1, 0.0f);
            gl.glColor4f(colors[1][0], colors[1][1], colors[1][2], colors[1][3]);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

            // Back - Rotate another 90 degree about y-axis
            gl.glRotatef(90.0f, 0.0f, 1, 0.0f);
            gl.glColor4f(colors[2][0], colors[2][1], colors[2][2], colors[2][3]);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

            // Left - Rotate another 90 degree about y-axis
            gl.glRotatef(90.0f, 0.0f, x, 0.0f);
            gl.glColor4f(colors[3][0], colors[3][1], colors[3][2], colors[3][3]);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
            vertices = new float[]{  // Vertices for the front face
                    -x, -y, z,  // 0. left-bottom-front
                    x, -y, z,  // 1. right-bottom-front
                    -x,  y, z,  // 2. left-top-front
                    x,  y, z   // 3. right-top-front
            };


            vertexBuffer.clear();
            vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
            vertexBuffer.put(vertices);         // Copy data into buffer
            vertexBuffer.position(0);

            gl.glFrontFace(GL10.GL_CCW);    // Front face in counter-clockwise orientation
            gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
            gl.glCullFace(GL10.GL_BACK);    // Cull the back face (don't display)

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
            // Bottom - Rotate 90 degree about x-axis
            gl.glRotatef(90.0f, x, 0.0f, 0.0f);
            gl.glColor4f(colors[4][0], colors[4][1], colors[4][2], colors[4][3]);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
            vertices = new float[]{  // Vertices for the front face
                    -x, -y, z + newFft,  // 0. left-bottom-front
                    x, -y, z + newFft,  // 1. right-bottom-front
                    -x,  y, z + newFft,  // 2. left-top-front
                    x,  y, z + newFft   // 3. right-top-front
            };


            vertexBuffer.clear();
            vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
            vertexBuffer.put(vertices);         // Copy data into buffer
            vertexBuffer.position(0);

            gl.glFrontFace(GL10.GL_CCW);    // Front face in counter-clockwise orientation
            gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
            gl.glCullFace(GL10.GL_BACK);    // Cull the back face (don't display)

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
            // Top - Rotate another 180 degree about x-axis
            gl.glRotatef(180.0f, x, 0.0f, 0.0f);
            gl.glColor4f(colors[5][0], colors[5][1], colors[5][2], colors[5][3]);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisable(GL10.GL_CULL_FACE);
            gl.glTranslatef(0.0f, 12, 0.0f);
            gl.glRotatef(90, 1,0,0);
            gl.glRotatef(90, 0,1,0);

        }

    }
    /*
package com.maxfour.music.visualizermusicplayer.Visualizer;

import static android.opengl.GLES20.GL_VERTEX_SHADER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.opengl.GLSurfaceView;
import android.view.View;
import android.widget.TextView;

import com.h6ah4i.android.media.IMediaPlayerFactory;
import com.h6ah4i.android.media.audiofx.IHQVisualizer;
import com.h6ah4i.android.media.opensl.audiofx.OpenSLHQVisualizer;
import com.maxfour.music.visualizermusicplayer.Cube;
import com.maxfour.music.visualizermusicplayer.Square;
import com.maxfour.music.visualizermusicplayer.Triangle;
import com.maxfour.music.visualizermusicplayer.Visualizer.renderer.BarGraphRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class VisualiserView extends GLSurfaceView implements GLSurfaceView.Renderer, View.OnTouchListener {
    private static final String TAG = "VisualiserView";
    private Context context;
    public static float[] lastFFt = new float[1024*4];
    private OpenSLHQVisualizer v;
    private Visualizer visualiserView;
    public BarGraphRenderer barGraphRenderer;
    public boolean retry = true;

    static {
        System.loadLibrary("myapplication");
    }




    private static float mAngCtr = 0; //for animation
    long mLastTime = SystemClock.elapsedRealtime();

    //for touch event - dragging
    float mDragStartX = -1;
    float mDragStartY = -1;
    float mDownX = -1;
    float mDownY = -1;
    //we add the .0001 to avoid divide by 0 errors
    //starting camera angles
    static float mCamXang = 0.0001f;
    static float mCamYang = 180.0001f;
    //starting camera position
    static float mCamXpos = 0.0001f;
    static float mCamYpos = 60.0001f;
    static float mCamZpos = 180.0001f;
    //distance from camera to view target
    float mViewRad = 100;
    //target values will get set in constructor
    static float mTargetY = 0;
    static float mTargetX = 0;
    static float mTargetZ = 0;
    //scene angles will get set in constructor
    static float mSceneXAng = 0.0001f;
    static float mSceneYAng = 0.0001f;

    float mScrHeight = 0; //screen height
    float mScrWidth  = 0; //screen width
    float mScrRatio  = 0; //width/height
    float mClipStart = 1; //start of clip region

    final double mDeg2Rad = Math.PI / 180.0; //Degrees To Radians
    final double mRad2Deg = 180.0 / Math.PI; //Radians To Degrees
    boolean mResetMatrix = false; //set to true when camera moves
    public float AccelZ = 0;
    public float AccelY = 0;
    int mOrientation = 0; //portrait\landscape


    public boolean RotateScene = true;
    public boolean UseTiltAngle = false;

    public boolean Paused = false;


    boolean lightingEnabled = true;   // Is lighting on? (NEW)
    private float[] lightAmbient = {0.5f, 0.5f, 0.5f, 1.0f};
    private float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
    private float[] lightPosition = {0.0f, 0.0f, 2.0f, 1.0f};


    private float[] mModelMatrix = new float[16];


    private float[] mViewMatrix = new float[16];

    private float[] mProjectionMatrix = new float[16];

    private float[] mMVPMatrix = new float[16];


    private float[] mLightModelMatrix = new float[16];

    private FloatBuffer mCubePositions;
    private FloatBuffer mCubeColors;
    private FloatBuffer mCubeNormals;

    private int mMVPMatrixHandle;

    private int mMVMatrixHandle;

    private int mLightPosHandle;

    private int mPositionHandle;

    private int mColorHandle;

    private int mNormalHandle;

    private final int mBytesPerFloat = 4;

    private final int mPositionDataSize = 3;

    private final int mColorDataSize = 4;

    private final int mNormalDataSize = 3;

    private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};

    private final float[] mLightPosInWorldSpace = new float[4];

    private final float[] mLightPosInEyeSpace = new float[4];

    private int mPerVertexProgramHandle;

    private int mPointProgramHandle;

    private float[] [] []cubsZ = new float[21][100][3];
    private float[] cubsY;


    public VisualiserView(Context context, AttributeSet attrs, int defStyle)
    {

        super(context, attrs);
        this.context = context;
        super.setEGLContextClientVersion(2);
        super.setRenderer(this);


        //    getHolder().addCallback(this);
        init();

    }


    public VisualiserView(Context context, AttributeSet attrs)
    {

        this(context, attrs, 0);
        this.context = context;


    }

    public VisualiserView(Context context)
    {
        this(context, null, 0);
        this.context = context;

        init();

    }



    public void init() {
        lastFFt = new float[1024*4];

//        getHolder().addCallback(this);
        LessonOneRenderer();
        super.setOnTouchListener(this);
    }

    public void link(MediaPlayer factory)
    {
        visualiserView = new Visualizer(factory.getAudioSessionId());
        Visualizer.OnDataCaptureListener listener = new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                for(int i = 1; i < waveform.length; i++) {
                    BarGraphRenderer.data[i] = waveform[i];
                }
            }
            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                lastFFt = new float[1024*4];
                for(int i = 21; i < fft.length/2; i++) {
                    float w = ((fft[(i - 20)*2]*fft[(i - 20)*2]));
                    float q = ((fft[(i - 20)*2+1]*fft[(i - 20)*2+1]));
                    float res = (w+q)/2;
                    res = res > 1 ? (int) ( 30* Math.log10(res*res)) : 1;
                    lastFFt[i] = res;
                }
            }
        };

        visualiserView.setDataCaptureListener(listener,
                Visualizer.getMaxCaptureRate(), true, true);
        visualiserView.setEnabled(true);

    }
    public void link(IMediaPlayerFactory factory)
    {
        v = (OpenSLHQVisualizer) factory.createHQVisualizer();;
        v.setCaptureSize(1024*4);
        Log.d(TAG, Visualizer.getMaxCaptureRate() + "");


        IHQVisualizer.OnDataCaptureListener listener = new IHQVisualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(IHQVisualizer visualizer, float[] waveform, int numChannels, int samplingRate) {
                //  BarGraphRenderer.data = new float[1024*4];
                //   BarGraphRenderer.data = waveform.clone();
                //  Log.d("ddddddddd", "wa: " + waveform[1]);
            }

            @Override
            public void onFftDataCapture(IHQVisualizer visualizer, float[] fft, int numChannels, int samplingRate) {

                for(int i = 0; i < fft.length/2; i++) {

                    float div = (fft.length/4) - i*4;
                    div = div > 20 ? (int) ( div) : 20;
                    float w = ((fft[i*2]*fft[i*2]));
                    float q = ((fft[i*2+1]*fft[i*2+1]));
                    float res = (w+q)/div;
                    res = res > 1 ? (int) ( 60* Math.log10(res*res)) : 1;
                    for (int f = 0; f < 5; f++){
                        if ((i > 4) && (lastFFt[i-f] > lastFFt[(i-f) - 1])) {
                            lastFFt[(i-f)-1] = lastFFt[(i-f)]- lastFFt[(i-f)]/4;
                        }
                    }
                    if(i > 500){
                        res = res/3;
                        lastFFt[i] = res;


                    }else {


                        lastFFt[i] = res;

                    }




                }

                for (int f = 0; f < 1000; f++){
                    for (int i = 0; i < 5; i++){
                        if (lastFFt[f] > lastFFt[f + 1]) {
                            lastFFt[f+1] = lastFFt[f]- lastFFt[(f)]/4;
                        }
                    }


                }

            }
        };

        v.setDataCaptureListener(listener,
                Visualizer.getMaxCaptureRate(), true, true);
        v.setEnabled(true);

    }
    public void release()
    {
        Log.d("///Visualizer", "release");

        ////
        if (v != null){
            v.release();
        }
        if (v != null){
            v.release();
        }
    }




    public void LessonOneRenderer()
    {
        // Define points for a cube.

        // X, Y, Z
        final float[] cubePositionData =
                {
                        // In OpenGL counter-clockwise winding is default. This means that when we look at a triangle,
                        // if the points are counter-clockwise we are looking at the "front". If not we are looking at
                        // the back. OpenGL has an optimization where all back-facing triangles are culled, since they
                        // usually represent the backside of an object and aren't visible anyways.

                        // Front face
                        -1.0f, 1.0f, 1.0f,
                        -1.0f, -1.0f, 1.0f,
                        1.0f, 1.0f, 1.0f,
                        -1.0f, -1.0f, 1.0f,
                        1.0f, -1.0f, 1.0f,
                        1.0f, 1.0f, 1.0f,

                        // Right face
                        1.0f, 1.0f, 1.0f,
                        1.0f, -1.0f, 1.0f,
                        1.0f, 1.0f, -1.0f,
                        1.0f, -1.0f, 1.0f,
                        1.0f, -1.0f, -1.0f,
                        1.0f, 1.0f, -1.0f,

                        // Back face
                        1.0f, 1.0f, -1.0f,
                        1.0f, -1.0f, -1.0f,
                        -1.0f, 1.0f, -1.0f,
                        1.0f, -1.0f, -1.0f,
                        -1.0f, -1.0f, -1.0f,
                        -1.0f, 1.0f, -1.0f,

                        // Left face
                        -1.0f, 1.0f, -1.0f,
                        -1.0f, -1.0f, -1.0f,
                        -1.0f, 1.0f, 1.0f,
                        -1.0f, -1.0f, -1.0f,
                        -1.0f, -1.0f, 1.0f,
                        -1.0f, 1.0f, 1.0f,

                        // Top face
                        -1.0f, 1.0f, -1.0f,
                        -1.0f, 1.0f, 1.0f,
                        1.0f, 1.0f, -1.0f,
                        -1.0f, 1.0f, 1.0f,
                        1.0f, 1.0f, 1.0f,
                        1.0f, 1.0f, -1.0f,

                        // Bottom face
                        1.0f, -1.0f, -1.0f,
                        1.0f, -1.0f, 1.0f,
                        -1.0f, -1.0f, -1.0f,
                        1.0f, -1.0f, 1.0f,
                        -1.0f, -1.0f, 1.0f,
                        -1.0f, -1.0f, -1.0f,
                };

        // R, G, B, A
        final float[] cubeColorData =
                {
                        // Front face (red)
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,

                        // Right face (green)
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,

                        // Back face (blue)
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,

                        // Left face (yellow)
                        1.0f, 1.0f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 1.0f,

                        // Top face (cyan)
                        0.0f, 1.0f, 1.0f, 1.0f,
                        0.0f, 1.0f, 1.0f, 1.0f,
                        0.0f, 1.0f, 1.0f, 1.0f,
                        0.0f, 1.0f, 1.0f, 1.0f,
                        0.0f, 1.0f, 1.0f, 1.0f,
                        0.0f, 1.0f, 1.0f, 1.0f,

                        // Bottom face (magenta)
                        1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 0.0f, 1.0f, 1.0f
                };

        // X, Y, Z
        // The normal is used in light calculations and is a vector which points
        // orthogonal to the plane of the surface. For a cube model, the normals
        // should be orthogonal to the points of each face.
        final float[] cubeNormalData =
                {
                        // Front face
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f,

                        // Right face
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        1.0f, 0.0f, 0.0f,

                        // Back face
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,
                        0.0f, 0.0f, -1.0f,

                        // Left face
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,
                        -1.0f, 0.0f, 0.0f,

                        // Top face
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,

                        // Bottom face
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f
                };

        // Initialize the buffers.
        mCubePositions = ByteBuffer.allocateDirect(cubePositionData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubePositions.put(cubePositionData).position(0);

        mCubeColors = ByteBuffer.allocateDirect(cubeColorData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubeColors.put(cubeColorData).position(0);

        mCubeNormals = ByteBuffer.allocateDirect(cubeNormalData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubeNormals.put(cubeNormalData).position(0);
    }

    protected String getVertexShader()
    {
        // TODO: Explain why we normalize the vectors, explain some of the vector math behind it all. Explain what is eye space.
        final String vertexShader =
                "uniform mat4 u_MVPMatrix;      \n"		// A constant representing the combined model/view/projection matrix.
                        + "uniform mat4 u_MVMatrix;       \n"		// A constant representing the combined model/view matrix.

                        + "attribute vec4 a_Position;     \n"		// Per-vertex position information we will pass in.
                        + "attribute vec4 a_Color;        \n"		// Per-vertex color information we will pass in.
                        + "attribute vec3 a_Normal;       \n"		// Per-vertex normal information we will pass in.

                        + "varying vec3 v_Position;       \n"		// This will be passed into the fragment shader.
                        + "varying vec4 v_Color;          \n"		// This will be passed into the fragment shader.
                        + "varying vec3 v_Normal;         \n"		// This will be passed into the fragment shader.

                        // The entry point for our vertex shader.
                        + "void main()                                                \n"
                        + "{                                                          \n"
                        // Transform the vertex into eye space.
                        + "   v_Position = vec3(u_MVMatrix * a_Position);             \n"
                        // Pass through the color.
                        + "   v_Color = a_Color;                                      \n"
                        // Transform the normal's orientation into eye space.
                        + "   v_Normal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));      \n"
                        // gl_Position is a special variable used to store the final position.
                        // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
                        + "   gl_Position = u_MVPMatrix * a_Position;                 \n"
                        + "}                                                          \n"
                ;

        return vertexShader;
    }

    protected String getFragmentShader()
    {
        final String fragmentShader =
                "precision mediump float;       \n"		// Set the default precision to medium. We don't need as high of a
                        // precision in the fragment shader.
                        + "uniform vec3 u_LightPos;       \n"	    // The position of the light in eye space.

                        + "varying vec3 v_Position;		\n"		// Interpolated position for this fragment.
                        + "varying vec4 v_Color;          \n"		// This is the color from the vertex shader interpolated across the
                        // triangle per fragment.
                        + "varying vec3 v_Normal;         \n"		// Interpolated normal for this fragment.

                        // The entry point for our fragment shader.
                        + "void main()                    \n"
                        + "{                              \n"
                        // Will be used for attenuation.
                        + "   float distance = length(u_LightPos - v_Position);                  \n"
                        // Get a lighting direction vector from the light to the vertex.
                        + "   vec3 lightVector = normalize(u_LightPos - v_Position);             \n"
                        // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
                        // pointing in the same direction then it will get max illumination.
                        + "   float diffuse = max(dot(v_Normal, lightVector), 5999.0);              \n"
                        // Add attenuation.
                        + "   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));  \n"
                        // Multiply the color by the diffuse illumination level to get final output color.
                        + "   gl_FragColor = v_Color * diffuse;                                  \n"
                        + "}                                                                     \n";

        return fragmentShader;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
    {
        // Set the background clear color to black.
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Position the eye in front of the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = -0.5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, mCamXpos, mCamYpos, mCamZpos, mTargetX, mTargetY, mTargetZ, upX, upY, upZ);

        final String vertexShader = getVertexShader();
        final String fragmentShader = getFragmentShader();

        final int vertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        mPerVertexProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[] {"a_Position",  "a_Color", "a_Normal"});

        // Define a simple shader program for our point.
        final String pointVertexShader =
                "uniform mat4 u_MVPMatrix;      \n"
                        +	"attribute vec4 a_Position;     \n"
                        + "void main()                    \n"
                        + "{                              \n"
                        + "   gl_Position = u_MVPMatrix   \n"
                        + "               * a_Position;   \n"
                        + "   gl_PointSize = 5.0;         \n"
                        + "}                              \n";

        final String pointFragmentShader =
                "precision mediump float;       \n"
                        + "void main()                    \n"
                        + "{                              \n"
                        + "   gl_FragColor = vec4(1.0,    \n"
                        + "   1.0, 1.0, 1.0);             \n"
                        + "}                              \n";

        final int pointVertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, pointVertexShader);
        final int pointFragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, pointFragmentShader);
        mPointProgramHandle = createAndLinkProgram(pointVertexShaderHandle, pointFragmentShaderHandle,
                new String[] {"a_Position"});
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height)
    {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        // make adjustments for screen ratio, default would be stretched square
        mScrHeight = height;
        mScrWidth = width;
        mScrRatio = mScrWidth/mScrHeight;

        // reset the matrix to its default state
        //calculate the clip region to minimize the depth buffer range (more precise)
        float camDist = (float)Math.sqrt(mCamXpos*mCamXpos+mCamYpos*mCamYpos+mCamZpos*mCamZpos);
        //set up the perspective pyramid and clip points
        mClipStart = Math.max(2, camDist-365); //max scene radius is 185 points at corners


        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, mCamXpos, mCamYpos, mCamZpos, mTargetX, mTargetY, mTargetZ, upX, upY, upZ);

        Matrix.frustumM(mProjectionMatrix, 0,
                -mScrRatio*.5f*mClipStart,
                mScrRatio*.5f*mClipStart,
                -1f*.5f*mClipStart,
                1f*.5f*mClipStart,
                mClipStart,
                mClipStart+185+Math.min(365, camDist));

    }
    private int frameCicle = 0;
    private boolean candraw = false;
    @Override
    public void onDrawFrame(GL10 glUnused)
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        // Set our per-vertex lighting program.
        GLES20.glUseProgram(mPerVertexProgramHandle);

        // Set program handles for cube drawing.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_MVMatrix");
        mLightPosHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_LightPos");
        mPositionHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Color");
        mNormalHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Normal");

        // Calculate position of the light. Rotate and then push into the distance.
        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -100.0f);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 2.0f);

        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);

        if(frameCicle == 20){candraw = true;}

        // Draw some cubes.
        for(int z = 0; z < frameCicle; z++){
            for(int t = 0; t < 20; t++){
                for(int i = 0; i < lastFFt[t]/10; i++){
                    cubsZ[z][t] = new float[]{-10 + t * 3, i * 3, -7 - z * 3};
                }
            }
        }
        if(candraw){
            for(int z = 0; z < 20; z++){
                for(int t = 0; t < 20; t++){
                    Matrix.setIdentityM(mModelMatrix, 0);
                    Matrix.translateM(mModelMatrix, 0, cubsZ[z][t][0], cubsZ[z][t][1], cubsZ[z][t][2]);
                    Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
                    drawCube();
                }
            }
        }

        frameCicle++;
        if(frameCicle > 20){ frameCicle = 0;}


        // Draw a point to indicate the light.
        GLES20.glUseProgram(mPointProgramHandle);
        drawLight();
        GL11 gl = (GL11)glUnused; //we need 1.1 functionality
        if (mResetMatrix) //camera distance changed
        {
            //recalc projection matrix and clip region
            onSurfaceChanged(gl, (int)mScrWidth, (int)mScrHeight);
            mResetMatrix = false;
        }


        if (UseTiltAngle) //use phone tilt to determine X axis angle
        {
            //float hyp = (float)Math.sqrt(AccelY*AccelY+AccelZ*AccelZ);
            if (RotateScene) //rotate camera around 0,0,0
            {
                //calculate new X angle
                float HypLen = (float)Math.sqrt(mCamXpos*mCamXpos+mCamZpos*mCamZpos); //across floor
                mSceneXAng = 90-(float)Math.atan2(AccelY,AccelZ)*(float)mRad2Deg;
                // stop at 90 degrees or scene will go upside down
                if (mSceneXAng > 89.9) mSceneXAng = 89.9f;
                if (mSceneXAng < -89.9) mSceneXAng = -89.9f;

                float HypZLen = (float)Math.sqrt(mCamXpos*mCamXpos+mCamYpos*mCamYpos+mCamZpos*mCamZpos); //across floor
                //HypZLen stays same with new angle
                //move camera to match angle
                mCamYpos = HypZLen*(float)Math.sin(mSceneXAng*mDeg2Rad);
                float HypLenNew = HypZLen*(float)Math.cos(mSceneXAng*mDeg2Rad); //across floor
                mCamZpos *= HypLenNew/HypLen;
                mCamXpos *= HypLenNew/HypLen;
            }
            else //rotate camera
            {
                mCamXang = (float)Math.atan2(AccelY,AccelZ)*(float)mRad2Deg - 90;
                //don't let scene go upside down
                if (mCamXang > 89.9) mCamXang = 89.9f;
                if (mCamXang < -89.9) mCamXang = -89.9f;
                ChangeCameraAngle(0, 0); //set target position
            }
        }

        //gluLookAt tells openGL the camera position and view direction (target)
        //target is 0,0,0 for scene rotate
        //Y is up vector, so we set it to 100 (can be any positive number)
        GLU.gluLookAt(gl, mCamXpos, mCamYpos, mCamZpos, mTargetX, mTargetY, mTargetZ, 0f, 100.0f, 0.0f);


        //use clock to adjust animation angle for smoother motion
        //if frame takes longer, angle is greater and we catch up
        long now = SystemClock.elapsedRealtime();
        long diff = now - mLastTime;
        mLastTime = now;

        //if paused, animation angle does not change
        if (!Paused)
        {
            mAngCtr += diff/100.0;
            if (mAngCtr > 360) mAngCtr -= 360;
        }
    }


    private void drawCube()
    {
        // Pass in the position information
        mCubePositions.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                0, mCubePositions);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        mCubeColors.position(0);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
                0, mCubeColors);

        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Pass in the normal information
        mCubeNormals.position(0);
        GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES20.GL_FLOAT, false,
                0, mCubeNormals);

        GLES20.glEnableVertexAttribArray(mNormalHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Pass in the light position in eye space.
        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);

        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
    }


    private void drawLight()
    {
        final int pointMVPMatrixHandle = GLES20.glGetUniformLocation(mPointProgramHandle, "u_MVPMatrix");
        final int pointPositionHandle = GLES20.glGetAttribLocation(mPointProgramHandle, "a_Position");

        // Pass in the position.
        GLES20.glVertexAttrib3f(pointPositionHandle, mLightPosInModelSpace[0], mLightPosInModelSpace[1], mLightPosInModelSpace[2]);

        // Since we are not using a buffer object, disable vertex arrays for this attribute.
        GLES20.glDisableVertexAttribArray(pointPositionHandle);

        // Pass in the transformation matrix.
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mLightModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(pointMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Draw the point.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
    }


    private int compileShader(final int shaderType, final String shaderSource)
    {
        int shaderHandle = GLES20.glCreateShader(shaderType);

        if (shaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(shaderHandle, shaderSource);

            // Compile the shader.
            GLES20.glCompileShader(shaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0)
            {
                Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }

        if (shaderHandle == 0)
        {
            throw new RuntimeException("Error creating shader.");
        }

        return shaderHandle;
    }


    private int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes)
    {
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0)
        {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes
            if (attributes != null)
            {
                final int size = attributes.length;
                for (int i = 0; i < size; i++)
                {
                    GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
                }
            }

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0)
            {
                Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0)
        {
            throw new RuntimeException("Error creating program.");
        }

        return programHandle;
    }
    void ChangeSceneAngle(float pChgXang, float pChgYang)
    {
        //hypotenuse using 2 dimensions
        float hypLen = (float)Math.sqrt(mCamXpos*mCamXpos+mCamZpos*mCamZpos); //across floor
        //process X and Y angles separately
        if (pChgYang != 0)
        {
            mSceneYAng += pChgYang;
            if (mSceneYAng < 0) mSceneYAng += 360;
            if (mSceneYAng > 360) mSceneYAng -= 360;
            //move camera according to new Y angle
            mCamXpos = hypLen*(float)Math.sin(mSceneYAng*mDeg2Rad);
            mCamZpos = hypLen*(float)Math.cos(mSceneYAng*mDeg2Rad);
        }

        if (pChgXang != 0)
        {
            //hypotenuse using all 3 dimensions
            float hypZLen = (float)Math.sqrt(hypLen*hypLen+mCamYpos*mCamYpos); // 0,0,0 to camera
            mSceneXAng += pChgXang;
            if (mSceneXAng > 89.9) mSceneXAng = 89.9f;
            if (mSceneXAng < -89.9) mSceneXAng = -89.9f;
            //hypZLen stays same with new angle
            //move camera according to new X angle
            mCamYpos = hypZLen*(float)Math.sin(mSceneXAng*mDeg2Rad);
            float HypLenNew = hypZLen*(float)Math.cos(mSceneXAng*mDeg2Rad); //across floor
            mCamZpos *= HypLenNew/hypLen;
            mCamXpos *= HypLenNew/hypLen;
        }
        //float camDist = (float)Math.sqrt(mCamXpos*mCamXpos+mCamYpos*mCamYpos+mCamZpos*mCamZpos);
        ///SetStatusMsg(""+camDist+" : "+mSceneXAng+" : "+mSceneYAng);
    }

    //change camera view direction
    void ChangeCameraAngle(float pChgXang, float pChgYang)
    {
        mCamXang += pChgXang;
        mCamYang += pChgYang;
        //keep angle within 360 degrees
        if (mCamYang > 360) mCamYang -= 360;
        if (mCamYang < 0) mCamYang += 360;
        //don't let view go upside down
        if (mCamXang > 89.9) mCamXang = 89.9f;
        if (mCamXang < -89.9) mCamXang = -89.9f;
        // move view target according to new angles
        mTargetY = mCamYpos+mViewRad*(float)Math.sin(mCamXang * mDeg2Rad);
        mTargetX = mCamXpos+mViewRad*(float)Math.cos(mCamXang * mDeg2Rad)*(float)Math.sin(mCamYang * mDeg2Rad);
        mTargetZ = mCamZpos+mViewRad*(float)Math.cos(mCamXang * mDeg2Rad)*(float)Math.cos(mCamYang * mDeg2Rad);
    }

    void MoveCamera(float pDist)
    {
        //move camera along line of sight toward target vertex
        if (RotateScene) //move towards\away from 0,0,0
        {
            //distance from 0,0,0
            float curdist = (float)Math.sqrt(
                    mCamXpos*mCamXpos +
                            mCamYpos*mCamYpos +
                            mCamZpos*mCamZpos);
            //if camera will pass center than reduce distance
            if (pDist<0 && curdist + pDist < 0.01) //can't go to exact center
                pDist = 0.01f-curdist;//0.01 closest distance
            float ratio = pDist/curdist;
            float chgCamX = (mCamXpos)*ratio;
            float chgCamY = (mCamYpos)*ratio;
            float chgCamZ = (mCamZpos)*ratio;
            mCamXpos += chgCamX;
            mCamYpos += chgCamY;
            mCamZpos += chgCamZ;
        }
        else //move towards\away from target
        {
            //mViewRad is 100, so do percentage
            float ratio = pDist/mViewRad;
            float chgCamX = (mCamXpos-mTargetX)*ratio;
            float chgCamY = (mCamYpos-mTargetY)*ratio;
            float chgCamZ = (mCamZpos-mTargetZ)*ratio;
            mCamXpos += chgCamX;
            mCamYpos += chgCamY;
            mCamZpos += chgCamZ;
            mTargetX += chgCamX;
            mTargetY += chgCamY;
            mTargetZ += chgCamZ;
        }
        ///float camDist = (float)Math.sqrt(mCamXpos*mCamXpos+mCamYpos*mCamYpos+mCamZpos*mCamZpos);
        ///SetStatusMsg(""+camDist+" : "+mSceneXAng+" : "+mSceneYAng);
        mResetMatrix = true; //recalc depth buffer range
    }



    @Override
    public boolean onTouch(View view, MotionEvent pEvent) {
        if (pEvent.getAction() == MotionEvent.ACTION_DOWN) //start drag
        {
            //store start position
            mDragStartX = pEvent.getX();
            mDragStartY = pEvent.getY();
            mDownX = pEvent.getX();
            mDownY = pEvent.getY();
            return true; //must have this
        }
        else if (pEvent.getAction() == MotionEvent.ACTION_UP) //drag stop
        {
            //if user did not move more than 5 pixels, assume screen tap
            if ((Math.abs(mDownX - pEvent.getX()) <= 5) && (Math.abs(mDownY - pEvent.getY()) <= 5))
            {
                if (pEvent.getY() < mScrHeight/2.0) //top half of screen
                    MoveCamera(-5); //move camera forward
                else if (pEvent.getY() > mScrHeight/2.0) //bottom half of screen
                    MoveCamera(5); //move camera back
            }
            return true; //must have this
        }
        else if (pEvent.getAction() == MotionEvent.ACTION_MOVE) //dragging
        {
            Log.d("//dd", "Move");
            //to prevent constant recalcs, only process after 5 pixels
            //if user moves less than 5 pixels, we assume screen tap, not drag
            //we divide by 3 to slow down scene rotate
            if (Math.abs(pEvent.getX() - mDragStartX) > 5) //process Y axis rotation
            {
                if (RotateScene) //rotate around fountain
                    ChangeSceneAngle(0, (mDragStartX - pEvent.getX())/3f); //Y axis
                else //rotate camera
                    ChangeCameraAngle(0, (mDragStartX - pEvent.getX())/3f); //Y axis
                mDragStartX = pEvent.getX();
            }
            if (Math.abs(pEvent.getY() - mDragStartY) > 5) //process X axis rotation
            {
                if (RotateScene) //rotate around fountain
                    ChangeSceneAngle((pEvent.getY() - mDragStartY)/3f, 0); //X axis
                else //rotate camera
                    ChangeCameraAngle((mDragStartY - pEvent.getY())/3f, 0); //X axis
                mDragStartY = pEvent.getY();
            }
            mResetMatrix = true;
            return true; //must have this
        }
        return true; //must have this

    }
    public native int anIntFromJNI(int a, int b);


/*
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        barGraphRenderer = new BarGraphRenderer(getHolder(), context);
        barGraphRenderer.setRunning(true);
        barGraphRenderer.start();
        Log.d("///Visualizer", "surfaceCreated");


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        holder.setFormat(format);
        holder.setFixedSize(width,height);

        //когда view меняет свой размер
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { //когда view исчезает из поля зрения
      //  release();

        barGraphRenderer.setRunning(false); //останавливает процесс
        Log.d("///Visualizer", "surfaceDestroyed");

        while(retry) {
            try {
                barGraphRenderer.join();
                retry = false;
            }
            catch (InterruptedException e) {
                //не более чем формальность
            }
        }
        barGraphRenderer = null;

    }

*/

}