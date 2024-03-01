package com.microchip.mu_ble1;

import static com.microchip.mu_ble1.BleMainActivity.bleService;
import static com.microchip.mu_ble1.MeasureActivity.et_r_time;
import static com.microchip.mu_ble1.MeasureActivity.loop_time;

import android.icu.util.Measure;

public class Infinite_request extends Thread{
    @Override
    public void run(){
        MeasureActivity.arr_rcv = new String[2];
        String sendData = "g";
        int r_time = Integer.valueOf(String.valueOf(et_r_time.getText())) * 50 / 2;
        int time = 0;
        et_r_time = et_r_time;
        while(true){
            if (time == 0) {
                MeasureActivity.t1 = System.nanoTime();
                MeasureActivity.loop_time = (long) 10.0;
                MeasureActivity.start_time = System.nanoTime();
                MeasureActivity.saving_point = 0;
            } else{
                MeasureActivity.t5 = System.nanoTime();
                //System.out.println("T5 : "+ MeasureActivity.t5);
                Float loop_time2 = (float) ((MeasureActivity.t5 - MeasureActivity.t1)/ Math.pow(10, 9));
                //System.out.println("One-loop : "+ loop_time2);
                if (loop_time > loop_time2){
                    loop_time = loop_time2;
                }
                //System.out.println("Min One-loop : "+ loop_time);
                MeasureActivity.t1 = MeasureActivity.t5;
                bleService.writeToTransparentUART(sendData.getBytes());
            }
            try {
                MeasureActivity.t2 = System.nanoTime();
                //System.out.println("T2 : "+ MeasureActivity.t2);
                Thread.sleep(100);
                MeasureActivity.t3 = System.nanoTime();
                //System.out.println("T3 : "+ MeasureActivity.t3);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
