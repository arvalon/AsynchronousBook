#ifndef ASYNCHRONOUSANDROID_THREAD_H
#define ASYNCHRONOUSANDROID_THREAD_H

#include <thread>
#include <jni.h>
#include <android/log.h>

#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,"MyLib",__VA_ARGS__)

jint JNI_OnLoad(JavaVM *vm, void *reserved);

jclass getCPUStatClass();

class JavaThread{

public:

    JavaThread();

    virtual ~JavaThread(){}

    void entryPoint();

    void start();

    void join(){ thread_.join();}

    void stop();


protected:

    virtual void onAttach(){}

    virtual void onDetach(){}

    virtual void run() = 0;

    inline JNIEnv *jniEnv() {
        return threadEnv;
    }

protected:

    JNIEnv *threadEnv = NULL;
    bool isStarted = false;
    std::condition_variable startCond;
    std::mutex startMutex;

    std::thread thread_;
    bool shouldStop = false;
    std::condition_variable stopCond;
    std::mutex stopMutex;
};

#endif //ASYNCHRONOUSANDROID_THREAD_H
