package com.stopgroup.stopcar.captain.activity.chat_libs;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
public class Libs {


    public static File getTemp(String name) {
        File tempDir = new File(Environment.getExternalStorageDirectory() + File.separator + "temp" + File.separator);
        tempDir.mkdirs();
        File file = new File(tempDir, name + ".png");
        if (file.exists() && !file.delete()) {
            file = new File(tempDir, name + "_" + System.currentTimeMillis() + ".png");
        }
        return file;
    }
    public static File getFile(Context context, Uri sourceuri, String name) {
        File file = getTemp(name);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(sourceuri);
            assert inputStream != null;
            bis = new BufferedInputStream(inputStream);
            bos = new BufferedOutputStream(new FileOutputStream(file, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }



    private Context context;
    SharedPreferences data;
    public Libs(Context context) {
        this.context = context;
        data = context.getSharedPreferences("data", 0);
    }

    public static String getCurrentTime() {
        String today = new Date().toString();
        String[] data = today.split("\\s+");
        return data[3];
    }

}
