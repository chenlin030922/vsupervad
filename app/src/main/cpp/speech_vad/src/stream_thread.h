//
// Created by linchen on 2018/5/29.
//

#ifndef VSUPERVAD_STREAM_THREAD_H

#include "stream_bean.h"
#include "queue"
#include "mutex"

#define NUM_THREADS 1
#define VSUPERVAD_STREAM_THREAD_H

#include <iostream>
#include "thread"
#include <condition_variable>

using namespace std;

class StreamThread {
private:
    queue<StreamBean> streamQueue;
    bool isLoop;
    mutex mutex1;
    condition_variable condvar;



    void clear() {
        std::queue<StreamBean> empty;
        swap(empty, streamQueue);
    }

public:
    StreamThread() = default {
        isLoop = 0;
    }

    void addQueue(StreamBean &bean) {
        //自动加解锁
        lock_guard<mutex> guard(mutex1);
        streamQueue.push(bean);
    }

    void *loop(void *threadid) {


    }

    void stopLoop() {
        pthread_exit(NULL);
    }

    void startLoop() {
        if (isLoop) {
            return;
        }
        isLoop = 1;
    }

    ~StreamThread() {
        clear();
        stopLoop();
    }
};

#endif //VSUPERVAD_STREAM_THREAD_H
