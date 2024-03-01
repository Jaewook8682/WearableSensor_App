package com.microchip.mu_ble1;

import static com.microchip.mu_ble1.MeasureActivity.arr_rcv;
import static com.microchip.mu_ble1.MeasureActivity.d_num;
import static com.microchip.mu_ble1.MeasureActivity.measure_n;
import static com.microchip.mu_ble1.MeasureActivity.saving_point;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import com.google.firebase.database.DatabaseReference;


public class File_saver {
    private Context context;
    private DatabaseReference databaseReference;

    public File_saver(Context context){
        this.context = context;
    }
    public void writeFile(String fileTitle) throws IOException {
        FileOutputStream fos;
        ContentResolver resolver = context.getContentResolver();
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
            int ll = d_num - saving_point;
            String[] saving_arr = new String[ll];
            System.arraycopy(arr_rcv, saving_point, saving_arr, 0, d_num);
            fos.write(Arrays.toString(saving_arr).getBytes());
            saving_point = d_num;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fos.close();
        fos.flush();
        fos.close();
    }

    // upload data on the firebase
    public void writeFile_server(String fileTitle) {
        for(int i =0; i<measure_n;i++){
            String[] d1 = Arrays.copyOfRange(arr_rcv, i*300, (i+1)*300);
            String d0 = Arrays.toString(d1);
            databaseReference.child(fileTitle).child("iv"+(i+1)).setValue(d0);
        }
    }
}

