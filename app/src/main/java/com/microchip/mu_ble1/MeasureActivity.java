package com.microchip.mu_ble1;

import static android.content.ContentValues.TAG;
import static com.microchip.mu_ble1.BleMainActivity.bleService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.common.util.Hex;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MeasureActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private static LineChart chart_iv;
    private static LineChart chart_res;
    public static TextView vds_tv_2, gain_tv_2, duty_tv_2, stm_tv_2, test_tv_2;
    public static int interval_n, d_num = 0, measure_n;
    public static String[] arr_rcv, arr_rsp;
    private int vds_n = 0, gain_n = 0, duty_n = 0, stm_n = 0, test_n = 0;
    public static String vds="0", gain="0", duty="0", stm="0", test="0";
    public static String G_BLE_Connection = "0";
    static TextView tv_ble;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");

        vds_tv_2   = findViewById(R.id.vds_tv2);
        gain_tv_2  = findViewById(R.id.gain_tv2);
        duty_tv_2  = findViewById(R.id.duty_tv2);
        stm_tv_2   = findViewById(R.id.stm_tv2);
        test_tv_2  = findViewById(R.id.test_tv2);

        // Graph View
        chart_iv  = findViewById(R.id.graph_iv);
        chart_iv.getLegend().setEnabled(true);
        chart_iv.setTouchEnabled(true);
        chart_iv.setDoubleTapToZoomEnabled(true);
        chart_iv.invalidate();
        LineData data = new LineData();
        chart_iv.setData(data);

        chart_res = findViewById(R.id.graph_response);
        chart_res.getLegend().setEnabled(true);
        chart_res.setTouchEnabled(true);
        chart_res.setDoubleTapToZoomEnabled(true);
        chart_res.invalidate();
        LineData data2 = new LineData();
        chart_res.setData(data2);

        tv_ble = findViewById(R.id.ble_status);

        Button bt_setting = findViewById(R.id.set_btn);
        bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_activity();
            }
        });

        Button bt_presetting = findViewById(R.id.set_btn);
        bt_presetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_activity();
            }
        });

        Button btg_ = findViewById(R.id.btg);
        Button btt_ = findViewById(R.id.btt);
        btg_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sendData = "g";
                arr_rcv = new String[300];
                arr_rsp = new String[1];
                measure_n = 1;

                chart_iv.invalidate();
                LineData data = new LineData();
                chart_iv.setData(data);

                chart_res.invalidate();
                LineData data2 = new LineData();
                chart_res.setData(data2);

                bleService.writeToTransparentUART(sendData.getBytes());
            }
        });

        btt_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sendData = "t";
                bleService.writeToTransparentUART(sendData.getBytes());
            }
        });
        // @@@ button functions @@@

        EditText n_measure_ = findViewById(R.id.n_measure);
        Button bt_start = findViewById(R.id.start);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("START", "CLICKED");
                chart_iv.invalidate();
                LineData data = new LineData();
                chart_iv.setData(data);

                chart_res.invalidate();
                LineData data2 = new LineData();
                chart_res.setData(data2);

                d_num = 0;
                measure_n = Integer.valueOf(String.valueOf(n_measure_.getText()));
                arr_rcv = new String[measure_n * 300];
                arr_rsp = new String[measure_n];
                String sendData = "g";
                for (int i = 0; i < measure_n; i++) {
                    Log.d("REQ" + interval_n, "req");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    bleService.writeToTransparentUART(sendData.getBytes());
                }
            }
        });

        Button clear_ = findViewById(R.id.clear);
        clear_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Clear", "CLICKED");
                Arrays.fill(arr_rcv, "0");
                Arrays.fill(arr_rsp, "0");
                chart_iv.invalidate();
                chart_iv.clear();
                chart_res.invalidate();
                chart_res.clear();
            }
        });

        EditText et_save_ = findViewById(R.id.et_save);
        Button bt_save_   = findViewById(R.id.bt_save);
        bt_save_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    writeFile(String.valueOf(et_save_.getText()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Toast mToast = Toast.makeText(getApplicationContext(), "Successfully saved '"+et_save_.getText()+"'", Toast.LENGTH_SHORT);
                et_save_.setText(null);
                mToast.show();
            }
        });
        update_setting();
        Button disconn_ = findViewById(R.id.disconn_btn);
        disconn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleService.disconnectBle();
                Log.d("BLE", "DISCONNECTED!!..");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.menu_scan:
                case R.id.menu_disconnect: {                                                              //Menu option Scan chosen
                    //scan_int();
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "22Oops, exception caught in " + e.getStackTrace()[0].getMethodName() + ": " + e.getMessage());
        }
        return super.onOptionsItemSelected(item);
    }

    // data upload on the firebase server
    public void writeFile_server(String fileTitle) {
        for(int i =0; i<measure_n;i++){
            String[] d1 = Arrays.copyOfRange(arr_rcv, i*300, (i+1)*300);
            String d0 = Arrays.toString(d1);
            databaseReference.child(fileTitle).child("iv"+(i+1)).setValue(d0);
        }

        String d2 = Arrays.toString(arr_rsp);
        databaseReference.child(fileTitle).child("Lowest points").setValue(d2);
    }

    public void writeFile(String fileTitle) throws IOException {
        FileOutputStream fos;
        ContentResolver resolver = getContentResolver();
        ContentValues values = new ContentValues();

        values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileTitle+".txt");
        values.put(MediaStore.MediaColumns.MIME_TYPE, "application/*");

        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS+ File.separator+"Database/");
        Uri fileUri = resolver.insert(MediaStore.Files.getContentUri("external"), values);
        try{
            fos = (FileOutputStream) resolver.openOutputStream(fileUri);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try{
            int ll = arr_rcv.length + arr_rsp.length;
            String[] saving_arr = new String[ll];
            System.arraycopy(arr_rcv, 0, saving_arr, 0, arr_rcv.length);
            System.arraycopy(arr_rsp, 0, saving_arr, arr_rcv.length, arr_rsp.length);
            fos.write(Arrays.toString(saving_arr).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fos.close();
        fos.flush();
        fos.close();
    }

    public static void processIncomingData(byte[] newBytes) {
        try {
            int d_len = Hex.bytesToStringUppercase(newBytes).length();
            Log.d("Requesting..", "("+String.valueOf(interval_n+1)+"/"+ String.valueOf(measure_n)+")");
            Log.d("Received Length", String.valueOf(d_len));

            if(d_len > 0){
                int d_conv = d_len / 400;    // data block 개수
                for(int i =0; i < 100*d_conv; i++){
                    String d11 = String.valueOf(Hex.bytesToStringUppercase(newBytes).charAt(i * 4 + 2));
                    String d12 = String.valueOf(Hex.bytesToStringUppercase(newBytes).charAt(i * 4 + 3));
                    String d13 = String.valueOf(Hex.bytesToStringUppercase(newBytes).charAt(i * 4 + 0));
                    String d14 = String.valueOf(Hex.bytesToStringUppercase(newBytes).charAt(i * 4 + 1));
                    arr_rcv[i+d_num*100] = String.valueOf(Integer.valueOf(d11 + d12 + d13 + d14, 16));
                }
                d_num += d_conv;            // global data index(3block마다 1 데이터)
                Log.d("D num", String.valueOf(d_num));
                Log.d("d conv", String.valueOf(d_conv));
                if((d_num/3) == measure_n){
                    addEntry();
                    d_num = 0;
                }
            }
            if(d_num%3==0){
                Log.d("Received", "1 Data Block");
                interval_n = d_num/3;
            }
        } catch (Exception e) {
            Log.e(TAG, "55Oops, exception caught in " + e.getStackTrace()[0].getMethodName() + ": " + e.getMessage());
        }
    }

    private static void addEntry() {
        int[] cc = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.GRAY, Color.CYAN, Color.BLACK, Color.MAGENTA, Color.DKGRAY, Color.LTGRAY};

        LineData data_iv = chart_iv.getData();
        LineData data_rs = chart_res.getData();

        if(data_iv != null) {
            Log.d("Draw", "IT!");
            ILineDataSet set_iv = data_iv.getDataSetByIndex(0);
            ILineDataSet set_rs = data_rs.getDataSetByIndex(0);
            if (set_iv == null) {
                set_iv = createSet();
                data_iv.addDataSet(set_iv);
            }
            if (set_rs == null) {
                set_rs = createSet();
                data_rs.addDataSet(set_rs);
            }
            for(int i = 0; i < measure_n; i++){
                ArrayList<Entry> val_iv  = new ArrayList<>();
                ArrayList<Entry> val_rs  = new ArrayList<>();
                int min_d = 0;
                for(int j = 0; j < 300; j++){
                    int rxd = Integer.parseInt(arr_rcv[(i*300)+j]);
                    val_iv.add(new Entry(j, rxd));
                    if(j==0){
                        min_d = rxd;
                    } else if (min_d > rxd) {
                        min_d = rxd;
                    }
                }
                val_rs.add(new Entry(i, min_d));
                arr_rsp[i] = String.valueOf(min_d);
                //iv start
                LineDataSet lineDataSet_iv = new LineDataSet(val_iv, "iv"+(i+1));
                lineDataSet_iv.setColor(cc[i]);
                lineDataSet_iv.setDrawCircles(false);
                data_iv.addDataSet(lineDataSet_iv);

                chart_iv.notifyDataSetChanged();
                chart_iv.setVisibleXRangeMaximum(300);
                chart_iv.moveViewToX(data_iv.getEntryCount());
                chart_iv.setData(data_iv);

                chart_iv.invalidate();

                // rs start
                LineDataSet lineDataSet_rs = new LineDataSet(val_rs, "iv"+i);
                lineDataSet_rs.setColor(cc[i]);
                lineDataSet_rs.setCircleColor(cc[i]);
                data_rs.addDataSet(lineDataSet_rs);

                chart_res.notifyDataSetChanged();
                chart_res.setVisibleXRangeMaximum(measure_n);
                chart_res.moveViewToX(data_rs.getEntryCount());
                chart_res.setData(data_rs);
                chart_res.invalidate();
            }
        }
    }

    private static LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setFillAlpha(110);
        set.setFillColor(Color.parseColor("#d7e7fa"));
        set.setColor(Color.parseColor("#800080"));
        set.setValueTextColor(Color.WHITE);
        set.setDrawValues(false);
        set.setLineWidth(2);
        set.setValueTextSize(9f);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setHighLightColor(Color.rgb(244, 117, 117));

        return set;
    }
    private void set_activity(){
        Intent set_int = new Intent(this, SettingActivity.class);
        startActivity(set_int);
    }

    private void update_setting(){
        Intent set_int = getIntent();
        String vv = set_int.getStringExtra("set_vds");
        String gg = set_int.getStringExtra("set_gain");
        String dd = set_int.getStringExtra("set_duty");
        String ss = set_int.getStringExtra("set_stm");
        String tt = set_int.getStringExtra("set_test");
        if(SettingActivity.set_xx == "1"){
            if (Objects.equals(vv, "1")){vds_tv_2.setText("Vds : 30mV");} else if (Objects.equals(vv, "2")) {vds_tv_2.setText("Vds : 60mV");} else if (Objects.equals(vv, "3")) {vds_tv_2.setText("Vds : 120mV");}
            if(Objects.equals(gg, "4")){gain_tv_2.setText("Gain : 35kOhm");} else if (Objects.equals(gg, "5")) {gain_tv_2.setText("Gain : 120kOhm");} else if (Objects.equals(gg, "6")) {gain_tv_2.setText("Gain : 350kOhm");}
            if(Objects.equals(dd, "p")){duty_tv_2.setText("Duty Cycle : 10%");} else if (Objects.equals(dd, "q")) {duty_tv_2.setText("Duty Cycle : 5%");}
            if(Objects.equals(ss, "r")){stm_tv_2.setText("Stimulate : Start");} else if (Objects.equals(ss, "l")) {stm_tv_2.setText("Stimulate : Stop");}
            if(Objects.equals(tt, "d")){test_tv_2.setText("Test the gate Voltage : On");}
        }
    }

    public static void get_data(){
        final byte[] newBytes = bleService.readFromTransparentUART();
        processIncomingData(newBytes);
    }

    public static void GConnAddress(String address) {
        Log.d("BLE CONN", "Reconnecting...");
        G_BLE_Connection = "0";
        while(G_BLE_Connection.equals("0")){
            bleService.connectBle(address);
            Log.d("@@", G_BLE_Connection);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Log.d("##", G_BLE_Connection);
    }
}