package com.stopgroup.stopcar.captain.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Toast;

import com.stopgroup.stopcar.captain.R;


/**
 * <h1>Implement reusable dialogs</h1>
 * Dialogs class for all dialogs and toasts
 * <p>
 *
 * @author kemo94
 * @version 1.0
 * @since 2017-08-9
 */

public abstract class Dialogs {


    public static Dialog progressDialog;

    public static Dialog noInternetDialog;


    public static void showLoadingDialog(Activity activity) {
        dismissLoadingDialog();
        try {
            progressDialog = new Dialog(activity);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setContentView(R.layout.dialog_prog);
            progressDialog.setCancelable(false);
            progressDialog.show();
        } catch (Exception e) {

        }
    }

    public static void dismissLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
            }
        }
    }

    public static void showToast(String message, Activity activity) {
        try {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
        }
        catch (Exception a) {a.printStackTrace();}
    }

    public static void showToast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


}
