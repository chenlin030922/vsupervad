//
// Created by 林晨 on 2018/6/2.
//

#ifndef VSUPERVAD_FRAME_INFO_H
#define VSUPERVAD_FRAME_INFO_H

#include <vector>
#include <cstdint>

struct Frame {
    int64_t start;
    int64_t end;
    bool isActive;
};

class FramesInfo {
private:
    std::vector<Frame> vector;
    uint64_t currentPos;
public:
    FramesInfo()  {
        currentPos=1;
    }

    void clear() {
        currentPos = 1;
        vector.clear();
    }

    void add(uint32_t length, int8_t is_active) {
        Frame frame;
        frame.start = currentPos;
        currentPos += length;
        frame.end = currentPos;
        vector.push_back(frame);
    }

    inline std::vector<Frame> & getInfos() {
        return vector;
    }

};

#endif //VSUPERVAD_FRAME_INFO_H
