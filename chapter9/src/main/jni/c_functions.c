#include "ru_arvalon_chapter9_MyNativeActivity.h"
#include <android/log.h>
//#include <jni.h>

JNIEXPORT jboolean

JNICALL Java_ru_arvalon_chapter9_MyNativeActivity_isPrime (JNIEnv *env, jobject obj, jint number) {

    __android_log_print(ANDROID_LOG_INFO,"vga","NATIVE CALL isPrime");

    int c;

    for (c = 2; c < number ; c++) {
        if (number % c == 0)
            return JNI_FALSE;
    }
    return JNI_TRUE;
}
