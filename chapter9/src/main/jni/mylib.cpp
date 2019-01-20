#include "ru_arvalon_chapter9_MyNativeActivity.h"
#include <iostream>
#include <cctype>
#include <sstream>
#include <algorithm>
#include <string>
#include <android/log.h>

class Math {
public:
    static int isPrime(int number) {
        int c;

        for (c = 2; c <= number - 1; c++) {
            if (number % c == 0)
                return 0;
        }
        if (c == number)
            return 1;
    }
};

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_packpublishing_asynchronousandroid_chapter9_MyNativeActivity
 * Method:    helloJNI
 * Signature: ()Ljava/lang/String;
 */
jstring Java_ru_arvalon_chapter9_MyNativeActivity_isPrimeCPlusPlus
        (JNIEnv *env, jobject obj, jint number) {

    __android_log_print(ANDROID_LOG_INFO,"vga","NATIVE CALL isPrimeCPlusPlus");

    return (env)->NewStringUTF(Math::isPrime(number) ? "True" : "False");
}


class Util {
public:
    static int countWords(const std::string &strString) {

        __android_log_print(ANDROID_LOG_INFO,"vga","NATIVE CALL countWords");

        std::string x = strString;
        std::replace_if(x.begin(), x.end(), std::ptr_fun<int, int>(std::isspace), ' ');
        x.erase(0, x.find_first_not_of(" "));
        if (x.empty()) return 0;
        return std::count(x.begin(), std::unique(x.begin(), x.end()), ' ') +
               !std::isspace(*strString.rbegin());
    }
};

void Java_ru_arvalon_chapter9_MyNativeActivity_updateWordCounter
        (JNIEnv *env, jobject obj, jstring text) {

    __android_log_print(ANDROID_LOG_INFO,"vga","NATIVE CALL updateWordCounter");

    std::string content(env->GetStringUTFChars(text, 0));

    size_t word_cnt = Util::countWords(content);

    jclass activityClass = env->GetObjectClass(obj);

    jfieldID charCountFId = env->GetFieldID(activityClass, "charCountTv", "Landroid/widget/TextView;");

    jobject tvObj = env->GetObjectField(obj, charCountFId);

    jclass textViewClass = env->GetObjectClass(tvObj);

    jmethodID setTextMId = env->GetMethodID(textViewClass, "setText", "(Ljava/lang/CharSequence;)V");

    std::ostringstream temp;
    temp << word_cnt;
    std::string result = temp.str();
    const char *wordCountStr = result.c_str();
    env->CallVoidMethod(tvObj, setTextMId, env->NewStringUTF(wordCountStr));

    env->DeleteLocalRef(activityClass);
    env->DeleteLocalRef(tvObj);
    env->DeleteLocalRef(textViewClass);
}

#ifdef __cplusplus
}
#endif
