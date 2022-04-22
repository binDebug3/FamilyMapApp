package edu.byu.cs240.dallinstewart.familymapclient;

import android.content.Context;
import android.widget.Toast;

public class CreateToast {
    //Global class to make creating toasts easier
    public static void showToast(Context context, String message) {
        Toast t = Toast.makeText(context,
                message,
                Toast.LENGTH_LONG);
        t.show();
    }
}
