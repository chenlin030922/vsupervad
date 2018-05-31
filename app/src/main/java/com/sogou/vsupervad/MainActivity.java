package com.sogou.vsupervad;

import com.sogou.translate.vad.JSimpleVad;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("hello-jni");
    }

    /**
     * Hello World!
     */
    private TextView mSampleText;
    /**
     * 点击录音
     */
    private Button mStartrecord;
    private boolean isRecord;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.startrecord:
                if (isRecord) {
                    Toast.makeText(this, "正在录制，不要重复点点点点！", Toast.LENGTH_SHORT).show();
                }
                isRecord = true;
                Toast.makeText(this, "开始录制", Toast.LENGTH_SHORT).show();
                break;
            case R.id.stoprecord:
                Toast.makeText(this, "停止录制", Toast.LENGTH_SHORT).show();
                isRecord = false;
                break;
        }
    }

    private String getCache() {
        String cachePath = "";
        String state = Environment.getExternalStorageState();
        File cacheDir = getExternalCacheDir();
        if (cacheDir != null && (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))) {
            cachePath = cacheDir.getAbsolutePath();
        } else {
            cachePath = getCacheDir().getAbsolutePath();
        }
        return cachePath;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());
        boolean isSuccess = JSimpleVad.Init();
        short[] data = new short[]{1, 2, 3, 4, 56, 7, 354, 23, 1, 4, 6, 78, 3, 3};
        JSimpleVad.doVad(data);
        if (isSuccess) {
            Log.e("dadad", "as");
            JSimpleVad.testStream(data);
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    private void initView() {
        mSampleText = (TextView) findViewById(R.id.sample_text);
        mStartrecord = (Button) findViewById(R.id.startrecord);
        mStartrecord.setOnClickListener(this);
        mStartrecord = (Button) findViewById(R.id.startrecord);
        mStartrecord.setOnClickListener(this);
    }
}
