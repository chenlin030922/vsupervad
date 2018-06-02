package com.sogou.vsupervad;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linchen on 2018/6/2.
 * mail: linchen@sogou-inc.com
 */
public class FrameInfo {
    private List<Frame> list;
    public FrameInfo(){
        list = new ArrayList<>();
    }
    public void clear(){
        list.clear();
    }

}
