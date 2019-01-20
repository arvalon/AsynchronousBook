#include "ru_arvalon_chapter9_GrayImageLoader.h"
#include <android/bitmap.h>
#include <android/log.h>

#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,"vga",__VA_ARGS__)

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    uint8_t red;
    uint8_t green;
    uint8_t blue;
    uint8_t alpha;
} rgba;

jobject Java_ru_arvalon_chapter9_GrayImageLoader_convertImageToGray
        (JNIEnv *env, jobject obj, jobject bitmap) {

    AndroidBitmapInfo info;
    void *pixels;
    int ret;

    LOGI("NATIVE CALL convertImageToGray");


    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        jclass clazz = env->FindClass("java/lang/RuntimeException");
        env->ThrowNew(clazz, "Failed to get Information from the Bitmap!");
        return 0;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, (void **) &pixels)) < 0) {
        jclass clazz = env->FindClass("java/lang/RuntimeException");
        env->ThrowNew(clazz, "Failed to lock Bitmap pixels !");
        return 0;
    }

    rgba *rgba_pixels = (rgba *) pixels;

    for (int i = 0; i < (info.width * info.height); i++) {
        uint8_t red = rgba_pixels[i].red;
        uint8_t green = rgba_pixels[i].green;
        uint8_t blue = rgba_pixels[i].blue;
        uint8_t gray = red * 0.3 + green * 0.59 + blue * 0.11;
        rgba_pixels[i].red = gray;
        rgba_pixels[i].green = gray;
        rgba_pixels[i].blue = gray;
    }

    AndroidBitmap_unlockPixels(env, bitmap);

    return bitmap;
}
#ifdef __cplusplus
}
#endif