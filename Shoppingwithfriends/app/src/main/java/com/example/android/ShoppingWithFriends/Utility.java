package com.example.android.ShoppingWithFriends;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Goutam Venkat on 2/26/15.
 */
public class Utility {
    /**
     * method showMessage
     * @param message String
     * @param title String
     * @param c Context
     * @return void : shows dialog box
     */
    protected static void showMessage(String message, String title, Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
