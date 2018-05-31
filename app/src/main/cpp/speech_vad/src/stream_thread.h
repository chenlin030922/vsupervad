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
#include "../../jni_log.h"

using namespace std;
static const size_t MAX_SIZE = 30;

//使用单生产者单消费者模型进行写入
class StreamDispatcher {
private:
    queue<StreamBean> streamQueue;
    bool isLoop;
    mutex mtx;
    condition_variable queue_not_full;//缓冲区不为满.
    condition_variable queue_not_empty;//缓冲区不为满.

    void clear() {
        std::queue<StreamBean> empty;
        swap(empty, streamQueue);
    }



public:
    StreamDispatcher() {
        isLoop = 0;
    }

    void startLoop() {
        if (isLoop) {
            return;
        }
        isLoop = 1;
    }
    //取数据
    void loop() {
        StreamBean &bean = streamQueue.front();
        //todo 传入vad中进行静音检测
        streamQueue.pop();
        while (1) {
            std::unique_lock<std::mutex> lock(mtx);
            if (!isLoop) {
                break;
            }
            while (streamQueue.size() == 0 && isLoop) {
                ALOGE("queue is empty,wait to product");
                queue_not_empty.wait(lock);
            }
            StreamBean &bean = streamQueue.front();
            //todo 传入vad中进行静音检测
            streamQueue.pop();
            queue_not_full.notify_all();
            lock.unlock();
        }
    }
    //读数据
    void addQueue(StreamBean &bean) {
        std::unique_lock<std::mutex> lock(mtx);
//        while (streamQueue.size() == MAX_SIZE && isLoop) {
//            ALOGE("queue is full,wait to consume");
//            queue_not_full.wait(lock);// 生产者等待"产品库缓冲区不为满"这一条件发生.
//        }
        streamQueue.push(bean);
        queue_not_empty.notify_all();// 通知消费者产品库不为空.
        lock.unlock();
    }


    void stopLoop() {
        isLoop = 0;
        std::thread t1(&StreamDispatcher::loop, this);
        t1.join();
    }


    ~StreamDispatcher() {
        clear();
        stopLoop();
    }
};

#endif //VSUPERVAD_STREAM_THREAD_H
