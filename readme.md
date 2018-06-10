# VSuperVad

基于https://github.com/Baidu-AIP/speech-vad-demo修改，主要用于android 音频流切片返回数据。

修改入口file为流输入，每一次返回一个30ms的音频流数据检测，详细的可以查看上述链接的Readme。

入口代码：

```c++
//vad_wrapper.cpp
Frame runWithStream(const StreamBean &bean, simple_vad *vad) {
    int is_active = process_vad(vad, bean.data);//送入到webrtc中进行识别vad,true说明有声音
    Frame frame;
    frame.start=currentPos;
    currentPos+=bean.length*2;
    frame.end=currentPos-1;
    frame.isActive= static_cast<bool>(is_active);
    return frame;
}
```

有需求的可以根据需求自己写JNI入口