package com.example.android.ShoppingWithFriends;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Goutam Venkat on 2/26/15.
 */
public class Utility {
    protected static String GOOGLE_API_KEY = "AIzaSyCZzrnj8f5c-6QZRZsFKy37XmcwfBEM7W0";
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
    protected static JSONArray remove(JSONArray array, int index) {
        JSONArray newArray = new JSONArray();
        try{
            for (int i = 0; i < array.length(); i++) {
                if (i != index) {
                    newArray.put(array.get(i));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return newArray;
    }
}
