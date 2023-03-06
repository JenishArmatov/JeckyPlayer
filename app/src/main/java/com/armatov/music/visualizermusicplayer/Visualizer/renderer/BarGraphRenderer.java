package com.armatov.music.visualizermusicplayer.Visualizer.renderer;


import android.content.Context;


public class BarGraphRenderer extends Thread
{

    public static float REDRAW_TIME  = 17.0f; //частота обновления экрана - 10 мс
    public boolean mRunning; //запущен ли процесс
    private long    mPrevRedrawTime;

    private long elapsedTime;
    public Draw draw;

    public BarGraphRenderer(Context context)
    {
        super();
        draw = new Draw(context);
        mRunning = false;

    }

    public void setRunning(boolean running) { //запускает и останавливает процесс
        mRunning = running;
        mPrevRedrawTime = getTime();

    }

    public long getTime() {
        return System.nanoTime() / 1_000_000;
    }

    @Override
    public void run() {
        while (mRunning ) {
            long curTime = getTime();
            elapsedTime = curTime - mPrevRedrawTime;
            if (elapsedTime < REDRAW_TIME) //проверяет, прошло ли 10 мс
                continue; //если прошло, перерисовываем картинку
          //  draw.draw();
            mPrevRedrawTime = curTime;
        }
    }





}

/*
public class BarGraphRenderer extends Thread
{
    public static int stepForGalaxy = 1;
    public static float stepForSubopod;
    private int red = 250;
    private int green = 0;
    private int blue = 0;
    public  int i = 0;

    public float[][] fftHistory = new float[100][1024*4];
    public float[][] dataHistory = new float[100][1024*4];

    public Canvas mCanvas;
    public static float REDRAW_TIME  = 17.0f; //частота обновления экрана - 10 мс
    private SurfaceHolder mSurfaceHolder; //нужен, для получения canvas
    public boolean mRunning; //запущен ли процесс
    private long    mPrevRedrawTime;
    public Paint paint;
    public static float[] data = new float[1024*4];

    private CircleWater circleWater;
    private Planet planet;
    private ClassicRenderer classicRenderer;
    private ClassicRendererHead classicRendererHead;
    private LineCircleRenderer lineCircleRenderer;
    private LineRenderer lineRenderer;
    private LineRenderer2 lineRenderer2;
    private Cubics cubics;


    private CirleRenderer cirleRenderer;
    private Subopod subopod;
    private Subopod2 subopod2;
    private SimpleLineRenderer simpleLineRenderer;
    private Tesla tesla;
    private final WeaveRenderer weaveRenderer = new WeaveRenderer();
    private Classic classic;
    private long elapsedTime;
    public static float stepForLineRenderer = 1;
    public static boolean flag = false;
    public static int column = 100;
    public int stepForBluetoothLatency = 1;
    private final Context context;
    private int latencyForBluetooth;
    private float[] fft = new float[1024*4];
    public static boolean drawing = true;
    private Draw draw;


    public BarGraphRenderer(Context context)
    {
        super();
        draw = new Draw(context);
      //  mSurfaceHolder = holder;
        this.context = context;
        mRunning = false;
   /*     setPlayerPos();
        classicRenderer = new ClassicRenderer();
        classicRendererHead = new ClassicRendererHead();
        lineCircleRenderer = new LineCircleRenderer();
        lineRenderer = new LineRenderer();
        lineRenderer2 = new LineRenderer2();
        cubics = new Cubics();

        cirleRenderer = new CirleRenderer();
        subopod = new Subopod();
        subopod2 = new Subopod2();
        simpleLineRenderer = new SimpleLineRenderer();
        tesla = new Tesla();
        planet = new Planet();
        circleWater = new CircleWater();
        classic = new Classic();
*//*
    }
    private void setPlayerPos(){
        StorageUtil storageUtil = new StorageUtil(context.getApplicationContext());

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HHmmss", Locale.getDefault());
        String timeText = dateText + timeFormat.format(currentDate);
        Player.checkedItem = storageUtil.getCheckedItem();
        long oldTime = new StorageUtil(context.getApplicationContext()).getTimeOfWatchVideo(storageUtil.getCheckedItem()) + 1000000;
        long realTime = Long.parseLong(timeText);
        if (realTime > oldTime){
            if(storageUtil.getCheckedItem() > 1){
                Player.checkedItem = 0;

            }else {
                Player.checkedItem = storageUtil.getCheckedItem();

            }

        }else {
            Player.checkedItem = storageUtil.getCheckedItem();

        }
    }

    public void setRunning(boolean running) { //запускает и останавливает процесс
        mRunning = running;
        mPrevRedrawTime = getTime();

    }

    public long getTime() {
        return System.nanoTime() / 1_000_000;
    }

    @Override
    public void run() {
        while (mRunning ) {
            long curTime = getTime();
            elapsedTime = curTime - mPrevRedrawTime;
            if (elapsedTime < REDRAW_TIME) //проверяет, прошло ли 10 мс
                continue; //если прошло, перерисовываем картинку
            mCanvas = null;
            draw.draw();
            /*
            try {
                    mCanvas = mSurfaceHolder.lockCanvas(); //получаем canvas
                    synchronized (mSurfaceHolder) {
                        if(drawing){
                            draw(mCanvas); //функция рисования

                        }else {
                            mCanvas.drawColor(Color.argb(255,0,0,0));
                        }
                    }

            }
            catch (NullPointerException e) {

                /*если canvas не доступен*/
        /*}
            finally {
                if (mCanvas != null)mSurfaceHolder.unlockCanvasAndPost(mCanvas); //освобождаем canvas
            }
*//*
            mPrevRedrawTime = curTime;
        }


    }
    private boolean chekBluetooth(){
        REDRAW_TIME = 20;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean result = false;
        if(bluetoothAdapter != null){
            result = BluetoothProfile.STATE_CONNECTED == bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);

        }
        return result;
    }
    private void draw(@NonNull Canvas canvas) {

        if(chekBluetooth()){
            latencyForBluetooth = 11;

        } else {
            latencyForBluetooth = 0;

        }


        System.arraycopy(VisualiserView.data,0,data, 0, VisualiserView.data.length/2);
        calculateFft();
        fftHistory[stepForBluetoothLatency] = fft.clone();

        dataHistory[stepForBluetoothLatency] = data.clone();


        Rect rect = new Rect(0,0,canvas.getWidth(),canvas.getHeight());


        switch (Player.checkedItem){
            case 0 :
                if(stepForBluetoothLatency != latencyForBluetooth){
                    simpleLineRenderer.draw(fftHistory[stepForBluetoothLatency + 1],rect);

                }else {
                    simpleLineRenderer.draw(fftHistory[0], rect);
                }

                break;

            case 1 :
                if(stepForBluetoothLatency != latencyForBluetooth){
                    cubics.draw(canvas,fftHistory[stepForBluetoothLatency + 1],rect);
                }else {
                    cubics.draw(canvas,fftHistory[0],rect);
                }


                break;

            case 2 :
                if(stepForBluetoothLatency != latencyForBluetooth){
                    classicRendererHead.draw(canvas, fftHistory[stepForBluetoothLatency + 1], rect, paint);

                }else {
                    classicRendererHead.draw(canvas, fftHistory[0], rect, paint);

                }


                break;

            case 3 :
                if(stepForBluetoothLatency != latencyForBluetooth){
                    weaveRenderer.draw(canvas, fftHistory[stepForBluetoothLatency + 1], rect);

                }else {
                    weaveRenderer.draw(canvas, fftHistory[0], rect);
                }

                break;


            case 4 :
                if(stepForBluetoothLatency != latencyForBluetooth){
                    classicRenderer.draw(canvas, fftHistory[stepForBluetoothLatency + 1], rect);
                }else {
                    classicRenderer.draw(canvas, fftHistory[0],  rect);
                }

                break;
            case 5 :
                if(stepForBluetoothLatency != latencyForBluetooth){

                    if(rect.height() >= rect.width()){
                        planet.draw(canvas, fftHistory[stepForBluetoothLatency + 1],  rect, paint);
                    }
                    if(rect.height() < rect.width()){
                        planet.draw(canvas, fftHistory[stepForBluetoothLatency + 1],
                                new Rect(0,0,rect.height(),rect.width()),paint);
                    }

                }else {
                    if(rect.height() >= rect.width()){
                        planet.draw(canvas, fftHistory[0], rect, paint);
                    }
                    if(rect.height() < rect.width()){
                        planet.draw(canvas, fftHistory[0],
                                new Rect(0,0,rect.height(),rect.width()),paint);
                    }

                }


                break;
            case 6 :

                if(stepForBluetoothLatency != latencyForBluetooth){
                    if(rect.height() >= rect.width()){
                        lineCircleRenderer.draw(canvas, fftHistory[stepForBluetoothLatency + 1], rect);
                    }
                    if(rect.height() < rect.width()){
                        lineCircleRenderer.draw(canvas, fftHistory[stepForBluetoothLatency + 1],
                                new Rect(0,0,rect.height(),rect.width()));
                    }

                }else {
                    if(rect.height() >= rect.width()){
                        lineCircleRenderer.draw(canvas, fftHistory[0], rect);
                    }
                    if(rect.height() < rect.width()){
                        lineCircleRenderer.draw(canvas, fftHistory[0],
                                new Rect(0,0,rect.height(),rect.width()));
                    }

                }

                break;
            case 7 :

                if(stepForBluetoothLatency != latencyForBluetooth){

                    if(rect.height() >= rect.width()){
                        subopod2.draw(fftHistory[stepForBluetoothLatency + 1], rect, canvas, paint);
                    }
                    if(rect.height() < rect.width()){
                        subopod2.draw(fftHistory[stepForBluetoothLatency + 1],
                                new Rect(0,0,rect.width(),rect.height()), canvas,paint);
                    }


                }else {
                    if(rect.height() >= rect.width()){
                        subopod2.draw(fftHistory[0],rect, canvas, paint);
                    }
                    if(rect.height() < rect.width()){
                        subopod2.draw(fftHistory[0], new Rect(0,0,rect.width(),rect.height()), canvas, paint);

                    }

                }

                break;
            case 8 :
                REDRAW_TIME = 40;

                if(stepForBluetoothLatency != latencyForBluetooth){
                    lineRenderer.draw(canvas, fftHistory[stepForBluetoothLatency + 1], rect,column);

                }else {
                    lineRenderer.draw(canvas, fftHistory[0], rect,column);

                }

                break;
            case 9 :
                if(stepForBluetoothLatency != latencyForBluetooth){
                    cirleRenderer.draw(dataHistory[stepForBluetoothLatency + 1], rect, paint, canvas);

                }else {
                    cirleRenderer.draw(dataHistory[0], rect, paint, canvas);

                }

                break;
            case 10 :
                REDRAW_TIME = 40;

                if(stepForBluetoothLatency != latencyForBluetooth){
                    subopod.draw(canvas, fftHistory[stepForBluetoothLatency + 1],rect,column);
                }else {
                    subopod.draw(canvas, fftHistory[0], rect,column);
                }

                break;
            case 11 :
                if(stepForBluetoothLatency != latencyForBluetooth){
                    classic.draw(canvas,fftHistory[stepForBluetoothLatency + 1], rect);
                }else {
                    classic.draw(canvas,fftHistory[0], rect);
                }

                break;
            case 12 :

                if(stepForBluetoothLatency != latencyForBluetooth){
                    if(rect.height() >= rect.width()){
                        circleWater.draw(canvas, fftHistory[stepForBluetoothLatency + 1], rect);
                    }
                    if(rect.height() < rect.width()){
                        circleWater.draw(canvas, fftHistory[stepForBluetoothLatency + 1],
                                new Rect(0,0,rect.height(),rect.width()));
                    }

                }else {
                    if(rect.height() >= rect.width()){
                        circleWater.draw(canvas, fftHistory[0],  rect);
                    }
                    if(rect.height() < rect.width()){
                        circleWater.draw(canvas, fftHistory[0],
                                new Rect(0,0,rect.height(),rect.width()));
                    }

                }

                break;

            case 13 :
                REDRAW_TIME = 40;
                if(stepForBluetoothLatency != latencyForBluetooth){
                    lineRenderer2.draw(canvas, fftHistory[stepForBluetoothLatency + 1],  rect,column);

                }else {
                    lineRenderer2.draw(canvas, fftHistory[0], rect,column);

                }

                break;
            case 14 :
                if(stepForBluetoothLatency != latencyForBluetooth){
                    tesla.draw(canvas, fftHistory[stepForBluetoothLatency + 1],   rect);

                }else {
                    tesla.draw(canvas, fftHistory[0],   rect);

                }

                break;

        }


        stepForGalaxy++;
        stepForSubopod++;
        stepForBluetoothLatency++;

        if(stepForGalaxy > 1300){
            stepForGalaxy = 0;
        }
        if(stepForSubopod > 100){
            stepForSubopod = 1;
        }
        if(stepForBluetoothLatency > latencyForBluetooth){
            stepForBluetoothLatency = 0;
        }
   //     canvas.drawText("" + elapsedTime, canvas.getWidth()/2,canvas.getHeight()/2, paint);

    }
    private void initColorChange() {
        paint = new Paint();
        paint.setARGB(255, red,green,blue);
        if(i < 26 && i > 0){
            red = 250;
            green = i * 10;
            blue = 0;
        }
        if(i > 25 && i < 51){
            red = 250 - ((i - 25)*10);
            blue = 0;
            green = 250;

        }
        if(i > 50 && i < 76){
            blue = (i - 50) * 10;
            green = 250 - ((i - 50)*10);
            red = 0;

        }
        if(i > 75 && i < 99){
            green = 0;
            red = (i - 75) * 10;
            blue = 250;
        }
        i = (int) (i + 1.5f);
        if(i > 100){i = 0;}

        paint.setARGB(255, red,green,blue);
    }
    private void calculateFft(){
        initColorChange();

        float[] lastFFt = new float[1024*4];
        System.arraycopy(VisualiserView.lastFFt,0,lastFFt, 0, VisualiserView.lastFFt.length/2);

        int lenth = lastFFt.length/2;

        for(int i = 0; i < lenth-5; i++) {
            float div = (lenth/2) - i*2;
            div = div > 20 ? (int) ( div) : 20;
            float w = lastFFt[i*2] * lastFFt[i*2];
            float q = ((lastFFt[i*2+1]*lastFFt[i*2+1]));
            float res = (w+q)/div;
            res = res > 0 ? (int) ( 60* Math.log10(res*res)) : 0;
            for (int f = 0; f <= 5; f++){
                if (i <= 5) {
                    fft[f] = 1;
                }
            }
            for (int f = 0; f < 5; f++){
                if ((i > 4) && (fft[i-f] > fft[(i-f) - 1])) {
                    fft[(i-f)-1] = fft[(i-f)]- fft[(i-f)]/4;
                }
            }


            fft[i+5] = res;


        }
        for (int f = 0; f < 1000; f++){
            for (int i = 0; i < 5; i++){
                if (fft[f] > fft[f + 1]) {
                    fft[f+1] = fft[f]- fft[(f)]/4;
                }
            }
        }
        //fft[0] = fft[1] - fft[1]/5;

    }

}
*/
