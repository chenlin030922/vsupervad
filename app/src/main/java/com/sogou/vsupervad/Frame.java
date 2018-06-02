package com.sogou.vsupervad;

/**
 * Created by linchen on 2018/6/2.
 * mail: linchen@sogou-inc.com
 */
public class Frame {
    private long start;
    private boolean isActive;//有声音为true
    private long end;

    private Frame(long start, boolean isActive, long end) {
        this.start = start;
        this.isActive = isActive;
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public boolean isActive() {
        return isActive;
    }

    public long getEnd() {
        return end;
    }
}
