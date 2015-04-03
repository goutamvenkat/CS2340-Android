package com.example.android.ShoppingWithFriends;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.util.List;


public class ForgotPasswordActivity extends Activity {
    private EditText emailEditText;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Button sendEmailButton = (Button) findViewById(R.id.sendEmailButton);
        emailEditText = (EditText) findViewById(R.id.forgotPasswordEmail);
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEditText.getText().toString();
                validateEmail();
            }
        });
    }

    private void validateEmail() {
        if (!isOfValidFormat()) {
            Utility.showMessage("Not of email format", "Invalid Email", this);
            emailEditText.setText("");
        } else {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("email", email);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> parseUsers, ParseException e) {
                    if (e == null && parseUsers.size() > 0) {
                        sendEmail();
                    } else {
                        Utility.showMessage("Not a registered email!", "Invalid Email", ForgotPasswordActivity.this);
                        emailEditText.setText("");
                    }
                }
            });
        }
    }
    private boolean isOfValidFormat() {
        if (TextUtils.isEmpty(email)) return false;
        else if (!email.contains("@") || !email.contains(".")) return false;
        StringBuilder str = new StringBuilder();
        for (int i = email.length() - 1; i >= 0; i--) {
            str.append(email.charAt(i));
        }
        int index_of_first_dot = 0;
        int index_of_first_at = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '.') {
                index_of_first_dot = i;
                break;
            }
        }
        if (index_of_first_dot == 0) return false;
        for (int i = index_of_first_dot + 1; i < str.length(); i++) {
            if (str.charAt(i) == '@') {
                index_of_first_at = i;
                break;
            }
        }
        if ((index_of_first_at - index_of_first_dot <= 1) || (index_of_first_at == 0) || (index_of_first_at == str.length() - 1)) return false;
        return true;
    }
    private void sendEmail() {
        ParseUser.requestPasswordResetInBackground(email, new ForgotPasswordCallBack());
    }

    private class ForgotPasswordCallBack extends RequestPasswordResetCallback {
        public ForgotPasswordCallBack() {
            super();
        }
        @Override
        public void done(ParseException e) {
            if (e == null) {
                Utility.showMessage("Successful!", "Sent Email", ForgotPasswordActivity.this);
                emailEditText.setText("");
            } else {
                Utility.showMessage("Oops! Something is wrong!", "", ForgotPasswordActivity.this);
                emailEditText.setText("");
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
