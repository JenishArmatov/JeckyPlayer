#include <jni.h>
#include <stdlib.h>
#include <android/bitmap.h>
#include<stddef.h>
#include <math.h>
#include <android/log.h>
#include <cstring>
#include <unistd.h>
#include <android/native_window_jni.h>
#define  LOG_TAG    "DEBUG"
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

class JniBitmap
{
public:
    uint32_t* _storedBitmapPixels;
    AndroidBitmapInfo _bitmapInfo;
    JniBitmap()
    {
        _storedBitmapPixels = NULL;
    }
};
void releaseMatrixArray(JNIEnv *pEnv, jobjectArray pArray);
float *calculateData(float *mFftBytes, float *GroundWidthHeight);
float *toPolar(float  *cartesian, double  cX, double  cY);
/*
extern "C" JNIEXPORT jfloatArray JNICALL
Java_com_maxfour_music_visualizermusicplayer_Visualizer_VisualiserView_anFloatFromJNI(JNIEnv *env,
                                                                                      jobject thiz,
                                                                                      jfloatArray fft) {



    float  lastFFt[1024*4];
    int lenth = env->GetArrayLength(fft);
    float *newFfft = env->GetFloatArrayElements(fft, 0);
    jfloatArray  result = env->NewFloatArray(lenth);
    for(int i = 0; i < lenth/2; i++) {
        float div = (lenth/4) - i*4;
        div = div > 20 ? (int) ( div) : 20;
        float w = newFfft[i*2] * newFfft[i*2];
        float q = ((newFfft[i*2+1]*newFfft[i*2+1]));
        float res = (w+q)/div;
        res = res > 1 ? (int) ( 60* log10(res*res)) : 1;
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

    jfloat* flt1 = env->GetFloatArrayElements( result,0);

    env->SetFloatArrayRegion(result, 0, lenth, lastFFt);
    env->ReleaseFloatArrayElements(result, flt1, 0);

    return result;
}

*/

extern "C" JNIEXPORT void JNICALL
Java_com_armatov_music_visualizermusicplayer_Visualizer_VisualiserView_CalculateFFT(
        JNIEnv *env, jobject thiz, jfloatArray fft) {

    float  lastFFt[1024*4];
    int lenth = env->GetArrayLength(fft)/2;
    float *newFfft = env->GetFloatArrayElements(fft, 0);
    jfloatArray  result = env->NewFloatArray(lenth);
    for(int i = 0; i < lenth/2; i++) {
        float div = (lenth/4) - i*4;
        div = div > 20 ? (int) ( div) : 20;
        float w = newFfft[i*2] * newFfft[i*2];
        float q = ((newFfft[i*2+1]*newFfft[i*2+1]));
        float res = (w+q)/div;
        res = res > 1 ? (int) ( 60* log10(res*res)) : 1;
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
    env->SetFloatArrayRegion(fft, 0, lenth/2, lastFFt);
    //  env->ReleaseFloatArrayElements(fft, reinterpret_cast<jfloat *>(lenth), 0);
}



extern "C"
JNIEXPORT jobjectArray  JNICALL
Java_com_armatov_music_visualizermusicplayer_Visualizer_renderer_Subopod2_Calculate(JNIEnv *env,
                                                                                    jobject thiz,
                                                                                    jobjectArray myArray) {
    int len1 = env -> GetArrayLength(myArray);
    jfloatArray dim=  (jfloatArray)env->GetObjectArrayElement(myArray, 0);
    int len2 = env -> GetArrayLength(dim);
    float **localArray;
    // allocate localArray using len1
    localArray = new float*[len1];
    for(int i=0; i<len1; ++i){
        jfloatArray oneDim= (jfloatArray)env->GetObjectArrayElement(myArray, i);
        jfloat *element=env->GetFloatArrayElements(oneDim, 0);
        //allocate localArray[i] using len2
        localArray[i] = new float[len2];
        for(int j=0; j<len2; ++j) {
            localArray[i][j]= element[j];
        }
    }
    releaseMatrixArray(env, myArray);
    return reinterpret_cast<jobjectArray>(localArray);
}

void releaseMatrixArray(JNIEnv *env, jobjectArray pArray) {
    int size = (env)->GetArrayLength(pArray);
    for (int i = 0; i < size; i++) {
        jfloatArray oneDim = (jfloatArray) (env)->GetObjectArrayElement(pArray, i);
        jfloat *elements = (env)->GetFloatArrayElements( oneDim, 0);

        (env)->ReleaseFloatArrayElements( oneDim, elements, 0);
        (env)->DeleteLocalRef(oneDim);
    }
    float *pDouble;
    double a;
    double b;
    toPolar(pDouble, a,b);
}


float *toPolar(float  *cartesian, double  cX, double  cY) {

    double angle = (cartesian[0]) * 2 * M_PI;
    double radius = (cartesian[1] / 2);
    float  *out = new float [2];

    float x = cX - radius * sin(angle);
    float y =  cY - radius * cos(angle);

    out[0] = x;
    out[1] = y;

    return out;
}
float  *x = new float[1024];
float *newbytes = new float[1024*4];
float *lastFft = new float[1024*4];
float *calculateData(float *mFftBytes, float *GroundWidthHeight, float *data) {
    float graund = GroundWidthHeight[0];
    float width = GroundWidthHeight[1];
    float height = GroundWidthHeight[2];
    int k = 1;
    int lastIndex = 0;
    int index = 1;


    for (int i = 0; i < 100; i++) {
        float highSample = 0;
        for (int j = lastIndex; j <= index; j++) {
            if (mFftBytes[j] > highSample) {
                highSample = mFftBytes[j];
            }
        }

        lastIndex = index;
        if (i > 87) {
            if (i < 98) {
                highSample = highSample - highSample / 3;
            }
            index = index + i;
        } else {

            index = index + k + i / 30;
        }
        if (i > 87) {
            for (int f = i - 5; f < 100; f++) {
                if ((lastFft[f] > lastFft[f - 1])) {
                    lastFft[f - 1] = lastFft[f] - lastFft[f] / 5;
                }
            }
        }
        lastFft[i] = highSample;
    }
    for (int f = 82; f < 95; f++) {
        for (int i = 0; i < 5; i++) {
            if (lastFft[f + i] > lastFft[f + i + 1]) {
                lastFft[f + i + 1] = lastFft[f + i] - lastFft[(f + i)] / 5;
            }
        }
    }
    for (int i = 0; i < 100; i++) {


        newbytes[i] = graund - (lastFft[i] - lastFft[i] / 5);

        if (newbytes[i] > graund - 6) {
            newbytes[i] = graund - 6;
        }
        x[i] = (x[i] + x[i] + newbytes[i]) / 3;

        if (i > 0 && x[i] < x[i - 1]) {
            x[i - 1] = x[i];
        }
        if (x[i] < x[i + 1]) {
            x[i + 1] = x[i];
        }
        if (x[i] < 0) {
            x[i] = 0;
        }

        if (x[i] > graund - 6) {
            x[i] = graund - 6;
        }

    }


        for (int i = 0; i < 100; i++) {
            float *cartPoint = new float [2];
            cartPoint[0] = (float) (i-50) / (100);
            cartPoint[1] = height / 5 + (graund - x[i-50]) * (height / 2) / 128;

            float  *polarPoint = toPolar(cartPoint, width,height);

            float  *cartPoint1 = new float [2];
            cartPoint1[0] = (float) (i) / (-100);
            cartPoint1[1] = height / 5 + ((graund - x[i])) * (height / 2) / 128;

            float *polarPoint1 = toPolar(cartPoint1, width, height);
            if(i <= 50){
                data[i * 4] = width;
                data[i * 4 + 1] = height;
                data[i * 4 + 2] = polarPoint1[0];
                data[i * 4 + 3] = polarPoint1[1];
            }
            if(i > 50){
                data[i * 4] = width;
                data[i * 4 + 1] = height;
                data[i * 4 + 2] = polarPoint[0];
                data[i * 4 + 3] = polarPoint[1];
            }

        }


    return data;
}


extern "C"
JNIEXPORT void JNICALL
Java_com_armatov_music_visualizermusicplayer_Visualizer_renderer_Subopod2_calculateData(JNIEnv *env,
                                                                                        jobject thiz,
                                                                                        jfloatArray m_fft_bytes,
                                                                                        jfloatArray data,

                                                                                        jfloatArray ground_width_height) {
    int lenth = env->GetArrayLength(m_fft_bytes);
    float *d = env->GetFloatArrayElements(data, 0);
    float *newFfft = env->GetFloatArrayElements(m_fft_bytes, 0);
    float *GroundWidthHeight = env->GetFloatArrayElements(ground_width_height, 0);
    d = calculateData(newFfft, GroundWidthHeight, d);

    env->SetFloatArrayRegion(data, 0, 400, d);

}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_armatov_music_visualizermusicplayer_JniBitmapHolder_jniStoreBitmapData(JNIEnv *env,
                                                                                jobject thiz,
                                                                                jobject bitmap) {
    AndroidBitmapInfo bitmapInfo;
    uint32_t* storedBitmapPixels = NULL;
    //LOGD("reading bitmap info...");
    int ret;
    if ((ret = AndroidBitmap_getInfo(env, bitmap, &bitmapInfo)) < 0)
    {
    //    LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return NULL;
    }
 //   LOGD("width:%d height:%d stride:%d", bitmapInfo.width, bitmapInfo.height, bitmapInfo.stride);
    if (bitmapInfo.format != ANDROID_BITMAP_FORMAT_RGBA_8888)
    {
      //  LOGE("Bitmap format is not RGBA_8888!");
        return NULL;
    }
    //
    //read pixels of bitmap into native memory :
    //
    //LOGD("reading bitmap pixels...");
    void* bitmapPixels;
    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &bitmapPixels)) < 0)
    {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
        return NULL;
    }
    uint32_t* src = (uint32_t*) bitmapPixels;
    storedBitmapPixels = new uint32_t[bitmapInfo.height * bitmapInfo.width];
    int pixelsCount = bitmapInfo.height * bitmapInfo.width;
    memcpy(storedBitmapPixels, src, sizeof(uint32_t) * pixelsCount);
    AndroidBitmap_unlockPixels(env, bitmap);
    JniBitmap *jniBitmap = new JniBitmap();
    jniBitmap->_bitmapInfo = bitmapInfo;
    jniBitmap->_storedBitmapPixels = storedBitmapPixels;
    return env->NewDirectByteBuffer(jniBitmap, 0);}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_armatov_music_visualizermusicplayer_JniBitmapHolder_jniGetBitmapFromStoredBitmapData(
        JNIEnv *env, jobject thiz, jobject handler) {
    JniBitmap* jniBitmap = (JniBitmap*) env->GetDirectBufferAddress(handler);
    if (jniBitmap->_storedBitmapPixels == NULL)
    {
    ///    LOGD("no bitmap data was stored. returning null...");
        return NULL;
    }
    //
    //creating a new bitmap to put the pixels into it - using Bitmap Bitmap.createBitmap (int width, int height, Bitmap.Config config) :
    //
    //LOGD("creating new bitmap...");
    jclass bitmapCls = env->FindClass("android/graphics/Bitmap");
    jmethodID createBitmapFunction = env->GetStaticMethodID(bitmapCls, "createBitmap", "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    jstring configName = env->NewStringUTF("ARGB_8888");
    jclass bitmapConfigClass = env->FindClass("android/graphics/Bitmap$Config");
    jmethodID valueOfBitmapConfigFunction = env->GetStaticMethodID(bitmapConfigClass, "valueOf", "(Ljava/lang/String;)Landroid/graphics/Bitmap$Config;");
    jobject bitmapConfig = env->CallStaticObjectMethod(bitmapConfigClass, valueOfBitmapConfigFunction, configName);
    jobject newBitmap = env->CallStaticObjectMethod(bitmapCls, createBitmapFunction,
                                                    jniBitmap->_bitmapInfo.width, jniBitmap->_bitmapInfo.height, bitmapConfig);
    //
    // putting the pixels into the new bitmap:
    //
    int ret;
    void* bitmapPixels;
    if ((ret = AndroidBitmap_lockPixels(env, newBitmap, &bitmapPixels)) < 0)
    {
    //    LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
        return NULL;
    }
    uint32_t* newBitmapPixels = (uint32_t*) bitmapPixels;
    int pixelsCount = jniBitmap->_bitmapInfo.height * jniBitmap->_bitmapInfo.width;
    memcpy(newBitmapPixels, jniBitmap->_storedBitmapPixels, sizeof(uint32_t) * pixelsCount);
    AndroidBitmap_unlockPixels(env, newBitmap);
    //LOGD("returning the new bitmap");
    return newBitmap;
}
extern "C"
JNIEXPORT void JNICALL
Java_com_armatov_music_visualizermusicplayer_JniBitmapHolder_jniFreeBitmapData(JNIEnv *env,
                                                                               jobject thiz,
                                                                               jobject handler) {
    JniBitmap* jniBitmap = (JniBitmap*) env->GetDirectBufferAddress(handler);
    if (jniBitmap->_storedBitmapPixels == NULL)
        return;
    delete[] jniBitmap->_storedBitmapPixels;
    jniBitmap->_storedBitmapPixels = NULL;
    delete jniBitmap;
}
extern "C"
JNIEXPORT void JNICALL
Java_com_armatov_music_visualizermusicplayer_JniBitmapHolder_jniRotateBitmapCcw90(JNIEnv *env,
                                                                                  jobject thiz,
                                                                                  jobject handler)    {
    JniBitmap* jniBitmap = (JniBitmap*) env->GetDirectBufferAddress(handler);
    if (jniBitmap == NULL || jniBitmap->_storedBitmapPixels == NULL)
        return;
    uint32_t* previousData = jniBitmap->_storedBitmapPixels;
    uint32_t newWidth = jniBitmap->_bitmapInfo.height;
    uint32_t newHeight = jniBitmap->_bitmapInfo.width;
    jniBitmap->_bitmapInfo.width = newWidth;
    jniBitmap->_bitmapInfo.height = newHeight;
    uint32_t* newBitmapPixels = new uint32_t[newWidth * newHeight];
    int whereToGet = 0;
    // XY. ... ... ..X
    // ...>Y..>...>..Y
    // ... X.. .YX ...
    for (int x = 0; x < newWidth; ++x)
        for (int y = newHeight - 1; y >= 0; --y)
        {
            //take from each row (up to bottom), from left to right
            uint32_t pixel = previousData[whereToGet++];
            newBitmapPixels[newWidth * y + x] = pixel;
        }
    delete[] previousData;
    jniBitmap->_storedBitmapPixels = newBitmapPixels;
}
extern "C"
JNIEXPORT void JNICALL
Java_com_armatov_music_visualizermusicplayer_JniBitmapHolder_jniCropBitmap(JNIEnv *env,
                                                                           jobject thiz,
                                                                           jobject handler,
                                                                           jint left, jint top,
                                                                           jint right,
                                                                           jint bottom) {
    JniBitmap* jniBitmap = (JniBitmap*) env->GetDirectBufferAddress(handler);
    if (jniBitmap->_storedBitmapPixels == NULL)
        return;
    uint32_t* previousData = jniBitmap->_storedBitmapPixels;
    uint32_t oldWidth = jniBitmap->_bitmapInfo.width;
    uint32_t newWidth = right - left, newHeight = bottom - top;
    uint32_t* newBitmapPixels = new uint32_t[newWidth * newHeight];
    uint32_t* whereToGet = previousData + left + top * oldWidth;
    uint32_t* whereToPut = newBitmapPixels;
    for (int y = top; y < bottom; ++y)
    {
        memcpy(whereToPut, whereToGet, sizeof(uint32_t) * newWidth);
        whereToGet += oldWidth;
        whereToPut += newWidth;
    }
    //done copying , so replace old data with new one
    delete[] previousData;
    jniBitmap->_storedBitmapPixels = newBitmapPixels;
    jniBitmap->_bitmapInfo.width = newWidth;
    jniBitmap->_bitmapInfo.height = newHeight;
}

extern "C"
JNIEXPORT void JNICALL Java_com_armatov_music_visualizermusicplayer_JniBitmapHolder_jniFlipBitmapVertical(
        JNIEnv * env, jobject thiz, jobject handle)
{
    JniBitmap* jniBitmap = (JniBitmap*) env->GetDirectBufferAddress(handle);
    if (jniBitmap->_storedBitmapPixels == NULL)
        return;

    uint32_t* pixels = jniBitmap->_storedBitmapPixels;
    uint32_t width = jniBitmap->_bitmapInfo.width;
    uint32_t height = jniBitmap->_bitmapInfo.height;
    uint32_t* rowBuffer = new uint32_t[width];

    // Iterate over each row, swapping it with the corresponding row from the other end of the bitmap
    for (int y = 0; y < height / 2; y++) {
        int otherY = height - y - 1;
        memcpy(rowBuffer, &pixels[y * width], width * sizeof(uint32_t));
        memcpy(&pixels[y * width], &pixels[otherY * width], width * sizeof(uint32_t));
        memcpy(&pixels[otherY * width], rowBuffer, width * sizeof(uint32_t));
    }

    // Clean up temporary buffer
    delete[] rowBuffer;
}

extern "C"

JNIEXPORT void JNICALL Java_com_armatov_music_visualizermusicplayer_JniBitmapHolder_jniScaleNNBitmap(
        JNIEnv * env, jobject thiz, jobject handle, jint newWidth,
        jint newHeight)
{
    JniBitmap* jniBitmap = reinterpret_cast<JniBitmap*>(env->GetDirectBufferAddress(handle));
    if (jniBitmap == nullptr || jniBitmap->_storedBitmapPixels == nullptr)
        return;

    uint32_t* previousData = jniBitmap->_storedBitmapPixels;
    uint32_t oldWidth = jniBitmap->_bitmapInfo.width;
    uint32_t oldHeight = jniBitmap->_bitmapInfo.height;
    uint32_t* newBitmapPixels = new uint32_t[newWidth * newHeight];

    for (int y = 0; y < newHeight; ++y) {
        float yRatio = (float)(y * oldHeight) / newHeight;
        int yIndex = (int) yRatio;

        for (int x = 0; x < newWidth; ++x) {
            float xRatio = (float)(x * oldWidth) / newWidth;
            int xIndex = (int) xRatio;

            int pixelIndex = (yIndex * oldWidth) + xIndex;
            uint32_t pixel = previousData[pixelIndex];
            newBitmapPixels[(y * newWidth) + x] = pixel;
        }
    }

    delete[] previousData;
    jniBitmap->_storedBitmapPixels = newBitmapPixels;
    jniBitmap->_bitmapInfo.width = newWidth;
    jniBitmap->_bitmapInfo.height = newHeight;
}

extern "C"

JNIEXPORT void JNICALL
Java_com_armatov_music_visualizermusicplayer_JniBitmapHolder_cropBitmap(JNIEnv *env, jobject thiz,
                                                            jobject input_bitmap,
                                                            jobject output_bitmap) {
    AndroidBitmapInfo input_bitmap_info;
    AndroidBitmapInfo output_bitmap_info;
    void *input_pixels;
    void *output_pixels;

    // Get input bitmap info
    if (AndroidBitmap_getInfo(env, input_bitmap, &input_bitmap_info) < 0) {
        return;
    }

    // Get output bitmap info
    if (AndroidBitmap_getInfo(env, output_bitmap, &output_bitmap_info) < 0) {
        return;
    }

    // Check if input and output bitmaps have the same dimensions and format
    if (input_bitmap_info.width != output_bitmap_info.width ||
        input_bitmap_info.height != output_bitmap_info.height ||
        input_bitmap_info.format != output_bitmap_info.format) {
        return;
    }

    // Lock input bitmap pixels for reading
    if (AndroidBitmap_lockPixels(env, input_bitmap, &input_pixels) < 0) {
        return;
    }

    // Lock output bitmap pixels for writing
    if (AndroidBitmap_lockPixels(env, output_bitmap, &output_pixels) < 0) {
        AndroidBitmap_unlockPixels(env, input_bitmap);
        return;
    }

    // Crop the bitmap from all sides by 1/100
    int crop_left = input_bitmap_info.width / 50;
    int crop_top = input_bitmap_info.height / 50;
    int crop_right = input_bitmap_info.width - crop_left;
    int crop_bottom = input_bitmap_info.height - crop_top;

    // Apply the tunnel effect by scaling the cropped bitmap back to its original dimensions
    for (int y = 0; y < output_bitmap_info.height; y++) {
        for (int x = 0; x < output_bitmap_info.width; x++) {
            int input_x = crop_left + x * (crop_right - crop_left) / output_bitmap_info.width;
            int input_y = crop_top + y * (crop_bottom - crop_top) / output_bitmap_info.height;
            int output_index = y * output_bitmap_info.stride + x * 4;
            int input_index = input_y * input_bitmap_info.stride + input_x * 4;
            ((uint32_t *) output_pixels)[output_index >> 2] = ((uint32_t *) input_pixels)[input_index >> 2];
        }
    }

    // Unlock input and output bitmaps
    AndroidBitmap_unlockPixels(env, input_bitmap);
    AndroidBitmap_unlockPixels(env, output_bitmap);
}





