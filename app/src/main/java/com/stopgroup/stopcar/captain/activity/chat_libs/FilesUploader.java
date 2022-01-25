package com.stopgroup.stopcar.captain.activity.chat_libs;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import id.zelory.compressor.Compressor;

import static com.stopgroup.stopcar.captain.activity.chat_libs.Libs.getFile;
public class FilesUploader {
    public interface OnImageUploadListener {
        public void OnImageUploaded(String file);
        public void OnError();
    }
    public FilesUploader(Context context, Uri file, String folder, String extention, final OnImageUploadListener onImageUploadListener) {
        final ProgressDialog mProgress = new ProgressDialog(context);
        mProgress.setTitle("Please wait...");
        mProgress.setMessage("uploading selected files ....");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        try {

            StorageReference path = FirebaseStorage.getInstance().getReference(folder).child("chat" ).child(System.currentTimeMillis() + extention);
            UploadTask uploadTask;
            if (extention.equals("jpg") || extention.equals("jpeg") || extention.equals("png")) {
                final File thumb_filepath = getFile(context, file, System.currentTimeMillis() + extention);
                Bitmap thumb_bitmap = new Compressor(context).setMaxHeight(512).setMaxWidth(512).setQuality(75).compressToBitmap(thumb_filepath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();
                uploadTask = path.putBytes(thumb_byte);
            } else {
                uploadTask = path.putFile(file);
            }
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                    if (thumb_task.isSuccessful()) {
                        path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri image) {
                                mProgress.dismiss();
                                onImageUploadListener.OnImageUploaded(image.toString());
                            }
                        });
                    } else {
                        mProgress.dismiss();
                        onImageUploadListener.OnError();
                    }
                }
            });
        } catch (Exception e) {
            mProgress.dismiss();
            onImageUploadListener.OnError();
        }
    }
}
