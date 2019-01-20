#include "ru_arvalon_chapter9_ExceptionActivity.h"

#ifdef __cplusplus
extern "C" {
#endif

void Java_ru_arvalon_chapter9_ExceptionActivity_genException
        (JNIEnv *jniEnv, jobject activityObj, jobject byteBuffer) {

    jclass byteBufferClass = jniEnv->GetObjectClass(byteBuffer);

    jmethodID getMid = jniEnv->GetMethodID(byteBufferClass, "get", "(I)B");

    // Trying to access a buffer position out of the buffer capacity
    jbyte byte = jniEnv->CallByteMethod(byteBuffer, getMid, 32);

    //
    if (jniEnv->ExceptionCheck()) {
        // Prints the exception  on the standard Error
        jniEnv->ExceptionDescribe();
        // Clears the exception
        jniEnv->ExceptionClear();

        jclass exceptionClass = jniEnv->FindClass("java/lang/RuntimeException");
        jniEnv->ThrowNew(exceptionClass, "Failed to get byte from buffer");

        jniEnv->DeleteLocalRef(exceptionClass);
        jniEnv->DeleteLocalRef(byteBufferClass);
        return;
    }
    // Do Stuff

    jniEnv->DeleteLocalRef(byteBufferClass);

}

#ifdef __cplusplus
}
#endif
