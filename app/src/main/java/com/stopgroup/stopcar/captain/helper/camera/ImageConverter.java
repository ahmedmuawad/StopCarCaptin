package com.stopgroup.stopcar.captain.helper.camera;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
/**
 * Created by mac on 9/12/17.
 */
public class ImageConverter {
    public static String convert(Bitmap bitmap) {
        int maxHeight = 500;
        int maxWidth = 500;
        float scale = Math.min(((float) maxHeight / bitmap.getWidth()), ((float) maxWidth / bitmap.getHeight()));
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
    }
}

