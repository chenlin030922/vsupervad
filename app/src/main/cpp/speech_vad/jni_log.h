//
// Created by linchen on 2018/4/11.
//

#ifndef ALOHA_JNI_LOG_H

#define ALOHA_JNI_LOG_H
#include "jni.h"
#include "android/log.h"
#include <cstdlib>


#ifndef JNI_LOG
#define JNI_LOG "jni_log"
#endif
//build log
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR, JNI_LOG, __VA_ARGS__)
#define ALOGD(...) __android_log_print(ANDROID_LOG_DEBUG, JNI_LOG, __VA_ARGS__)
#define ALOGI(...) __android_log_print(ANDROID_LOG_INFO, JNI_LOG, __VA_ARGS__)
#define ALOGW(...) __android_log_print(ANDROID_LOG_WARN, JNI_LOG, __VA_ARGS__)
#endif //ALOHA_JNI_LOG_H
