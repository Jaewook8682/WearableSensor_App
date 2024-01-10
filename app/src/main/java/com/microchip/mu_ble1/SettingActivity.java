package com.microchip.mu_ble1;

import static com.microchip.mu_ble1.BleMainActivity.bleService;
import static com.microchip.mu_ble1.MeasureActivity.duty;
import static com.microchip.mu_ble1.MeasureActivity.duty_tv_2;
import static com.microchip.mu_ble1.MeasureActivity.gain;
import static com.microchip.mu_ble1.MeasureActivity.gain_tv_2;
import static com.microchip.mu_ble1.MeasureActivity.stm;
import static com.microchip.mu_ble1.MeasureActivity.stm_tv_2;
import static com.microchip.mu_ble1.MeasureActivity.test;
import static com.microchip.mu_ble1.MeasureActivity.test_tv_2;
import static com.microchip.mu_ble1.MeasureActivity.vds;
import static com.microchip.mu_ble1.MeasureActivity.vds_tv_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Measure;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {
    private TextView vds_tv_, gain_tv_, duty_tv_, stm_tv_, test_tv_;
    static int set_main = 0;
    public static String set_xx = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button bt1_ = findViewById(R.id.bt1);
        Button bt2_ = findViewById(R.id.bt2);
        Button bt3_ = findViewById(R.id.bt3);
        Button bt4_ = findViewById(R.id.bt4);
        Button bt5_ = findViewById(R.id.bt5);
        Button bt6_ = findViewById(R.id.bt6);
        Button btp_ = findViewById(R.id.btp);
        Button btq_ = findViewById(R.id.btq);
        Button btr_ = findViewById(R.id.btr);
        Button btl_ = findViewById(R.id.btl);
        Button btd_ = findViewById(R.id.btd);

        vds_tv_  = findViewById(R.id.vds_tv);
        gain_tv_ = findViewById(R.id.gain_tv);
        duty_tv_ = findViewById(R.id.duty_tv);
        stm_tv_  = findViewById(R.id.stm_tv);
        test_tv_ = findViewById(R.id.test_tv);
        set_xx = "1";
        initialize_display();

        // @@@ button functions @@@
        bt1_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vds = "1";
                vds_tv_.setText("Vds : 30mV");
                bleService.writeToTransparentUART(vds.getBytes());
            }
        });

        bt2_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vds = "2";
                vds_tv_.setText("Vds : 60mV");
                bleService.writeToTransparentUART(vds.getBytes());
            }
        });

        bt3_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vds = "3";
                vds_tv_.setText("Vds : 120mV");
                bleService.writeToTransparentUART(vds.getBytes());
            }
        });

        bt4_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gain = "4";
                gain_tv_.setText("Gain : 35kOhm");
                bleService.writeToTransparentUART(gain.getBytes());
            }
        });

        bt5_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gain = "5";
                gain_tv_.setText("Gain : 120kOhm");
                bleService.writeToTransparentUART(gain.getBytes());
            }
        });

        bt6_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gain = "6";
                gain_tv_.setText("Gain : 350kOhm");
                bleService.writeToTransparentUART(gain.getBytes());
            }
        });
        btp_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                duty = "p";
                duty_tv_.setText("Duty Cycle : 10%");
                bleService.writeToTransparentUART(duty.getBytes());
            }
        });
        btq_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                duty = "q";
                duty_tv_.setText("Duty Cycle : 5%");
                bleService.writeToTransparentUART(duty.getBytes());
            }
        });
        btr_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stm = "r";
                stm_tv_.setText("Stimulate : Start");
                bleService.writeToTransparentUART(stm.getBytes());
            }
        });
        btl_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stm = "l";
                stm_tv_.setText("Stimulate : Stop");
                bleService.writeToTransparentUART(stm.getBytes());
            }
        });
        btd_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test = "d";
                test_tv_.setText("Test Gate Voltage : On");
                bleService.writeToTransparentUART(test.getBytes());
            }
        });

        Button bt_measure = findViewById(R.id.msr_btn);
        bt_measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msr_activity();
            }
        });

        Button bt_preset = findViewById(R.id.btpreset);
        bt_preset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendData = "s";
                bleService.writeToTransparentUART(sendData.getBytes());
            }
        });
    }
    private void msr_activity(){
        Intent bleIntent = new Intent(this, MeasureActivity.class);
        Log.d("vds", vds);
        Log.d("gain", gain);
        Log.d("duty", duty);
        Log.d("stm", stm);
        Log.d("test", test);
        bleIntent.putExtra("set_vds", vds);
        bleIntent.putExtra("set_gain", gain);
        bleIntent.putExtra("set_duty", duty);
        bleIntent.putExtra("set_stm", stm);
        bleIntent.putExtra("set_test", test);
        set_main = 1;
        startActivity(bleIntent);
    }

    private void initialize_display(){
        vds_tv_.setText("Vds : ");
        gain_tv_.setText("Gain : ");
        duty_tv_.setText("Duty Cycle : ");
        stm_tv_.setText("Stimulate : ");
        test_tv_.setText("Test Gate Voltage : ");
    }
}