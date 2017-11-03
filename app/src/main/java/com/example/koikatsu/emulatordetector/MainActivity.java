package com.example.koikatsu.emulatordetector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import io.github.dmitrikudrenko.emulatordetector.Callback;
import io.github.dmitrikudrenko.emulatordetector.EmulatorDetector;
import io.github.dmitrikudrenko.emulatordetector.accelerometer.AccelerometerDetector;
import io.github.dmitrikudrenko.emulatordetector.gyroscope.GyroscopeDetector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean isEmulatro = false;
    private TextView tv_detect_su, tv_detect_file, tv_detect_cmd, tv_detect_emulator;
    private Button btn_detect_su, btn_detect_file, btn_detect_cmd, btn_detect_emulator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tv_detect_su = findViewById(R.id.tv_detect_su);
        tv_detect_file = findViewById(R.id.tv_detect_file);
        tv_detect_cmd = findViewById(R.id.tv_detect_cmd);
        tv_detect_emulator = findViewById(R.id.tv_detect_emulator);

        btn_detect_su = findViewById(R.id.btn_detect_su);
        btn_detect_file = findViewById(R.id.btn_detect_file);
        btn_detect_cmd = findViewById(R.id.btn_detect_cmd);
        btn_detect_emulator = findViewById(R.id.btn_detect_emulator);

        btn_detect_su.setOnClickListener(this);
        btn_detect_file.setOnClickListener(this);
        btn_detect_cmd.setOnClickListener(this);
        btn_detect_emulator.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_detect_su:
                if (rootCheck()) {
                    tv_detect_su.setText("루팅 디바이스");
                } else {
                    tv_detect_su.setText("디바이스");
                }
                break;
            case R.id.btn_detect_file:
                if (checkFile()) {
                    tv_detect_file.setText("루팅 디바이스");
                } else {
                    tv_detect_file.setText("디바이스");
                }

                break;
            case R.id.btn_detect_cmd:
                if (execCmd()) {
                    tv_detect_cmd.setText("루팅 디바이스");
                } else {
                    tv_detect_cmd.setText("디바이스");
                }
                break;
            case R.id.btn_detect_emulator:
                AccelerometerDetector accelerometerDetector = AccelerometerDetector.builder()
                        .setDelay(500)
                        .setEventCount(5)
                        .build();

                GyroscopeDetector gyroscopeDetector = GyroscopeDetector.builder()
                        .setDelay(500)
                        .setEventCount(5)
                        .build();

                EmulatorDetector emulatorDetector = new EmulatorDetector(accelerometerDetector, gyroscopeDetector);
                emulatorDetector.detect(this, new Callback() {
                    @Override
                    public void onDetect(boolean b) {
                        isEmulatro = b;
                        if (b){
                            tv_detect_emulator.setText("에뮬레이터");
                        }else {
                            tv_detect_emulator.setText("디바이스");
                        }
                    }

                    @Override
                    public void onError(Exception exception) {
                        tv_detect_emulator.setText("ㅇㅔ러....");
                    }
                });
                break;
        }
    }


    public boolean rootCheck() {
        String[] arrayOfString = {"/sbin/su", "/system/su", "/system/sbin/su", "/system/xbin/su", "/data/data/com.noshufou.android.su", "/system/app/Superuser.apk"};
        int i = 0;
        while (true) {
            if (i >= arrayOfString.length)
                return false;
            if (new File(arrayOfString[i]).exists())
                return true;

            i++;
        }
    }

    private boolean checkFile() {
        String[] arrayOfString = {"/system/bin/.ext", "/system/xbin/.ext"};
        int i = 0;
        while (true) {
            if (i >= arrayOfString.length)
                return false;
            if (new File(arrayOfString[i]).exists())
                return true;
            i++;
        }
    }

    public boolean execCmd() {
        boolean flag = false;
        try {
            Runtime.getRuntime().exec("su");
            flag = true;
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }
}
