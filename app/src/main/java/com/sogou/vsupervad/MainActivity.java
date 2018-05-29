package com.sogou.vsupervad;

import com.sogou.translate.vad.JSimpleVad;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("hello-jni");
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

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());
        boolean isSuccess = com.sogou.translate.vad.JSimpleVad.Init();
        short[] data = new short[]{1, 2, 3, 4, 56, 7, 354, 23, 1, 4, 6, 78, 3, 3};
        JSimpleVad.doVad(data, getCache(), "asdasd");
        if (isSuccess) {
            Log.e("dadad", "as");
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
