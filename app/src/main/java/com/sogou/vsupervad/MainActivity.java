package com.sogou.vsupervad;

import com.sogou.translate.vad.JSimpleVad;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("hello-jni");
    }

    /**
     * Hello World!
     */
    private TextView mSampleText;
    private Button mTestbtn;
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
        PATH = getSaveCacheDir(this) + "/" + "16k_1.pcm";
        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());
        boolean isSuccess = JSimpleVad.Init();
        if (isSuccess) {
            Log.e("dadad", "as");
//            JSimpleVad.testStream(data);
        }
//        convertToWav();
    }

    private void convertToWav() {
        try {
            InputStream stream = getAssets().open("16k_1.pcm");
            String path = getSaveCacheDir(this) + "/" + "16k_1.pcm";
            File file = new File(path);
            if (file.exists()) {
                return;
            }
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            while (stream.available() > 0) {
                byte[] data = new byte[2048];
                int size = stream.read(data);
                byte[] tmp = new byte[size];
                for (int i = 0; i < size; i++) {
                    tmp[i] = data[i];
                }
                fileOutputStream.write(tmp);
            }
            File wavFile = new File(getSaveCacheDir(this) + "/bb.wav");
            if (!wavFile.exists()) {
                wavFile.createNewFile();
            }
            makePCMToWAV(file.getAbsolutePath(), wavFile.getAbsolutePath());
            Log.e("aaa", "wav success");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isExternalStorageAvailable(Context context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) &&
                !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            return true;
        }
        return false;
    }

    public static String getSaveCacheDir(Context context) {
        String cachePath = "";
        if (isExternalStorageAvailable(context)) {
            cachePath = context.getExternalCacheDir().getAbsolutePath();
        } else {
            cachePath = context.getCacheDir().getAbsolutePath();
        }
        return cachePath;
    }

    /**
     * 将单个pcm文件转化为wav文件
     */
    private void makePCMToWAV(final String pcmPath, final String wavPath) {
        WavHeader wavHeader = new WavHeader();
        try {
            wavHeader.rawToWave(new File(pcmPath), new File(wavPath), 16000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static final long BYTE_ONE_SECOND=16000*2;
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    private short[] convertToShort(byte[] bytes) {
        short[] shorts = new short[bytes.length / 2];
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
        return shorts;
    }

    private void initView() {
        mSampleText = (TextView) findViewById(R.id.sample_text);
        mStartrecord = (Button) findViewById(R.id.startrecord);
        mStartrecord.setOnClickListener(this);
        mStartrecord = (Button) findViewById(R.id.startrecord);
        mStartrecord.setOnClickListener(this);
        mTestbtn = findViewById(R.id.testbtn);
        mTestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTest();
//                testNativeCode();
            }
        });
        Log.e("asdasd", FRAME_PATH);
    }

    int checknUM = 0;

    private void testNativeCode() {
        JSimpleVad.DoVad(PATH, getSaveCacheDir(this), "a");
    }

    //native层支持三种模式，10ms,20ms,30ms当做解析包，10ms为320个字节数据，以此类推
    private static final int FRAME_SIZE = 320*3;//160个shrt数据，即320 byte数据
    private static String PATH;
    private static final  String FRAME_PATH=(Frame.class.getPackage().getName()
            +"/"+Frame.class.getSimpleName()).replaceAll("\\.","/");
    private void startTest() {
        try {
            checknUM = 0;
            InputStream stream = new FileInputStream(new File(PATH));
            List<Frame> list = new ArrayList<>();
            while (true) {
                int byteNum = stream.available();
                boolean ret = byteNum > 0;
                if (!ret) {
                    break;
                }
                if (byteNum > FRAME_SIZE) {
                    byteNum = FRAME_SIZE;
                }
                byte[] data = new byte[byteNum];
                while (stream.available() > 0) {
                    Frame frame;
                    int size = stream.read(data);
                    if (size == data.length) {
                        short[] shorts = convertToShort(data);
                       frame= JSimpleVad.testStream(shorts,FRAME_PATH);
                    } else {
                        byte[] temp = new byte[size];
                        System.arraycopy(data, 0, temp, 0, size);
                       frame= JSimpleVad.testStream(convertToShort(temp),FRAME_PATH);
                    }
                    list.add(frame);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
