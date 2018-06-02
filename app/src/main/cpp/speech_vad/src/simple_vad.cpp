//
// Created by fu on 3/6/18.
//

#include "simple_vad.h"
#include "../jni_log.h"
#include <stdio.h>
#include <stddef.h>
#include <string.h>
#include <string>
#include <sstream>

static int check_end_file(FILE *fp) {
    if (feof(fp)) {
        return 0;
    }
    return ferror(fp);
}
bool  aaa=1;
template <typename T>
std::string to_string(T value)
{
    std::ostringstream os ;
    os << value ;
    return os.str() ;
}
int read_int16_bytes(FILE *fp, int16_t *output) {
    if (feof(fp)) {
        return 1;
    }

    size_t readed = fread(output, sizeof(int16_t), FRAME_SIZE, fp);
    if (aaa) {
        aaa=0;
        std::string a = "";
        int16_t * temp=output;
//        while (temp != nullptr) {
//            a.append(",");
//            a.append(to_string(*temp));
//            temp++;
//        }
//        ALOGE("value=%s",a.c_str());
    }
    if (readed == 0) {
        int res = check_end_file(fp);
        return (res == 0) ? 1 : 1000 + res;
    }
    if (readed < FRAME_SIZE) {
        memset(output, 0, (FRAME_SIZE - readed) * sizeof(int16_t));
        // printf("only %ld bits, will refill to %ld\n", readed, length);
    }
    return 0;

}

simple_vad *simple_vad_create() {
    VadInst *inst = WebRtcVad_Create();
    int res = WebRtcVad_Init(inst);
    if (res != 0) {
        fprintf(stderr, "WebRtcVad_Init failed %d", res);
        return NULL;
    }
    res = WebRtcVad_set_mode(inst, WEBRTC_VAD_MODE);
    if (res != 0) {
        fprintf(stderr, "WebRtcVad_set_mode failed %d", res);
        WebRtcVad_Free(inst);
        return NULL;
    }
    return inst;
}

int process_vad(VadInst *inst, int16_t *data) {
    int res = WebRtcVad_Process(inst, SAMPLE_RATE, data, FRAME_SIZE);
    return res;
}

void simple_vad_free(simple_vad *inst) {
    WebRtcVad_Free(inst);
}