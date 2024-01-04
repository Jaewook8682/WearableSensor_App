package com.microchip.mu_ble1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {
    private String vds = "0";
    private String gain = "0";
    private String duty = "0";
    private String stm = "0";
    private String test = "0";
    private String done = "1";
    private TextView vds_tv_, gain_tv_, duty_tv_, stm_tv_, test_tv_;
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

        initialize_display();

        // @@@ button functions @@@
        bt1_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vds = "1";
                vds_tv_.setText("Vds : 30mV");
            }
        });

        bt2_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vds = "2";
                vds_tv_.setText("Vds : 60mV");
            }
        });

        bt3_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vds = "3";
                vds_tv_.setText("Vds : 120mV");
            }
        });

        bt4_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gain = "4";
                gain_tv_.setText("Gain : 35kOhm");
            }
        });

        bt5_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gain = "5";
                gain_tv_.setText("Gain : 120kOhm");
            }
        });

        bt6_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gain = "6";
                gain_tv_.setText("Gain : 350kOhm");
            }
        });
        btp_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                duty = "p";
                duty_tv_.setText("Duty Cycle : 5%");
            }
        });
        btq_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                duty = "q";
                duty_tv_.setText("Duty Cycle : 10%");
            }
        });
        btr_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stm = "r";
                stm_tv_.setText("Stimulate : Start");
            }
        });
        btl_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stm = "l";
                stm_tv_.setText("Stimulate : Stop");
            }
        });
        btd_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test = "d";
                test_tv_.setText("Test Gate Voltage : On");
            }
        });

        Button bt_measure = findViewById(R.id.msr_btn);
        bt_measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msr_activity();
            }
        });
    }

    private void msr_activity(){
        Intent bleIntent = new Intent(this, BleMainActivity.class);
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
        bleIntent.putExtra("done", done);
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