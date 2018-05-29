cmake_minimum_required(VERSION 3.4.1)

file(GLOB SRC_FILES speech_vad/webrtc/common_audio/signal_processing/*.cpp
     speech_vad/webrtc/common_audio/vad/*.cpp
     speech_vad/webrtc/rtc_base/*.cpp
     speech_vad/src/file_cut.cpp
     speech_vad/src/period_format.cpp
     speech_vad/src/simple_vad.cpp
     speech_vad/src/vad_wrapper.cpp)

add_library(speech_vad SHARED
            ${SRC_FILES})

add_library(hello-jni SHARED
            hello-jni.cpp)

# Include libraries needed for hello-jni lib
target_link_libraries(hello-jni
                      android
                      speech_vad
                      log)
