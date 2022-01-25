package com.stopgroup.stopcar.captain.helper;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.actions.NoteIntents;
import com.stopgroup.stopcar.captain.R;

import static android.app.Activity.RESULT_OK;


/**
 * <h1>Implement reusable methods of all intent actions</h1>
 * IntentClass class for all actions of intent
 * <p>
 *
 * @author kemo94
 * @version 1.0
 * @since 2017-08-9
 */
public abstract class IntentClass {


    // go to another activity

    public static void goToActivity(Activity currentActivity, Class targetClass, Bundle value) {

        Intent intent = new Intent(currentActivity, targetClass);
        intent.putExtra("data", value);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        currentActivity.startActivity(intent);
    }
    public static void goSms(Activity activity, String smsNumber) {


        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", smsNumber, null)));

    }


    public static void sendResultToActivity(Activity currentActivity, Bundle value) {
        Intent intent = new Intent();
        intent.putExtra("data", value);
        currentActivity.setResult(RESULT_OK,intent);
    }
//    public static void goToResultActivity(Activity currentActivity, Class targetClass, Bundle value) {
//
//        Intent intent = new Intent(currentActivity, targetClass);
//        intent.putExtra("data", value);
//        currentActivity.startActivityForResult(intent, Constants.UPDATE_ITEMS);
//    }


    // show marker on map
    public static void goMap(Activity activity, Double lat, Double lng) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<lat>,<long>?q=" + lat + "," + lng));
        activity.startActivity(intent);
    }


    public static void rateApp(Activity currentActivity) {
        Uri uri = Uri.parse("market://details?id=" + currentActivity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            currentActivity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            currentActivity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + currentActivity.getPackageName())));
        }
    }

    public static void goToActivityAndClear(Activity currentActivity, Class targetClass, Bundle value) {

        Intent intent = new Intent(currentActivity, targetClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("data", value);
        currentActivity.startActivity(intent);
    }
    // to open dial phone number

    public static void goTodialPhoneNumber(Activity currentActivity, String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length() > 0) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            if (intent.resolveActivity(currentActivity.getPackageManager()) != null) {
                currentActivity.startActivity(intent);
            }
        }
    }

    public static void goToFacebook(Activity activity, String id) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + id));
            activity.startActivity(intent);
        } catch (Exception e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + id)));
        }

    }

    //go to fb ,twitter ,google plus ....etc

    public static void goToLink(Activity activity, String url) {
        if (url != null && url.length() != 0) {
            try {
                if (!url.contains("https://") || !url.contains("http://"))
                    url = "https://" + url;
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
            } catch (NullPointerException e) {

            } catch (Exception e) {

            }
        }

    }


    // go to other app with data
    public static void goSharedata(Activity activity, String text, String sendTo) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        activity.startActivity(Intent.createChooser(sendIntent, sendTo));
    }


    // go to whatsapp
    public static void goTowhatsApp(Activity activity, String smsNumber, String smsText) {
try {
    Uri uri = Uri.parse("smsto:" + smsNumber);
    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
    i.putExtra("sms_body", smsText);
    i.setPackage("com.whatsapp");
    activity.startActivity(i);
}
catch (ActivityNotFoundException a) {a.printStackTrace();
    Toast.makeText(activity, activity.getString(R.string.whatsnotfound), Toast.LENGTH_SHORT).show();
}
    }

    // to open wifi settings and can change any action setting
    public static void goToOpenWifiSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    // to navigate
    public static void goToNavigate(Activity activity, Double lat, Double lng) {

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://maps.google.com/maps?saddr=20.344,34.34&daddr=" + lat + "," + lng));
        activity.startActivity(intent);
    }


    //to open bluetooth
    public static void goToBluetooth(Activity activity) {

        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.bluetooth.BluetoothSettings");
        intent.setComponent(cn);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);

    }


    // to create event on mob
    public static void goToAddEvent(Activity activity, String title, String location, long begin, long end) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }


    //add new contact

    public static void goToInsertContact(Activity activity, String name, String email) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }


    // send email to more than one with attachment

    public static void goTocomposeEmail(Activity activity, String[] addresses, String subject, Uri attachment) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }
    //send email to one

    public static void goToEmail(Activity activity, String address, String subject) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    // create note
    public static void goToCreateNote(Activity activity, String subject, String text) {
        Intent intent = new Intent(NoteIntents.ACTION_CREATE_NOTE)
                .putExtra(NoteIntents.EXTRA_NAME, subject)
                .putExtra(NoteIntents.EXTRA_TEXT, text);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }


    //search any thing in any app on your mob

    public static void goToSearchWeb(Activity activity, String query) {
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

}