package AndroidTesting;

/**
 * Created by KaranPrime
 */

import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.ShoppingWithFriends.R;
import com.example.android.ShoppingWithFriends.RegisterScreen;
import com.robotium.solo.Solo;

import java.util.Random;

public class RegisterScreenTest extends ActivityInstrumentationTestCase2<RegisterScreen> {

    private RegisterScreen myActivity;
    private EditText mUserName;
    private EditText mEmail;
    private EditText mPassword;
    private Button RegisterButton;
    private Solo solo;

    public RegisterScreenTest() {
        super(RegisterScreen.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(true);

        myActivity = getActivity();

        mUserName = (EditText) myActivity.findViewById(R.id.RegisterUserNameEditText);
        mEmail = (EditText) myActivity.findViewById(R.id.RegisterEmailEditText);
        mPassword = (EditText) myActivity.findViewById(R.id.RegisterPasswordEditText);
        RegisterButton = (Button) myActivity.findViewById(R.id.RegisterButton);

        solo = new Solo(getInstrumentation(), myActivity);
    }

    /**
     * Tests if all component of activity gets initialized.
     */
    public void testPreconditions() {
        try {
            setUp();
        } catch (Exception e) {
            return;
        };
        assertNotNull(myActivity);
        assertNotNull(mUserName);
        assertNotNull(mEmail);
        assertNotNull(mPassword);
        assertNotNull(RegisterButton);

    }

    /**
     * Tests presence of Buttons
     */
    public void testButtonsLayout() {
        try {
            setUp();
        } catch (Exception e) {
            return;
        };
        final View decorView = myActivity.getWindow().getDecorView();

        ViewAsserts.assertOnScreen(decorView, RegisterButton);
        ViewAsserts.assertOnScreen(decorView, mUserName);
        ViewAsserts.assertOnScreen(decorView, mEmail);
        ViewAsserts.assertOnScreen(decorView, mPassword);


        final ViewGroup.LayoutParams layoutParams1 = RegisterButton.getLayoutParams();
        assertNotNull(layoutParams1);
        assertEquals(layoutParams1.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams1.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    /**
     * Tests Registration feature when you leave the fields blank
     */
    public void testEmptyLoginCredentials() {
        try {
            setUp();
        } catch (Exception e) {
            return;
        };

        solo.enterText(mUserName, "");
        solo.enterText(mEmail, "");
        solo.enterText(mPassword, "");
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                RegisterButton.performClick();
            }
        });
        assertTrue("Invalid Credentials", solo.waitForText("Registration Failed"));
        solo.clickOnButton("Ok");
    }

    /**
     * Tests Register feature when you enter invalid Email
     */
    public void testInvalidEmail() {
        try {
            setUp();
        } catch (Exception e) {
            return;
        };

        solo.enterText(mUserName, "XYZ");
        solo.enterText(mEmail, "k12");
        solo.enterText(mPassword, "XYZ");
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                RegisterButton.performClick();
            }
        });
        assertTrue("Invalid Credentials", solo.waitForText("Registration Failed"));
        solo.clickOnButton("Ok");

    }

    /**
     * Tests Register feature when you enter used Email
     */
    public void testUsedEmail() {
        try {
            setUp();
        } catch (Exception e) {
            return;
        };

        solo.enterText(mUserName, "XYZ");
        solo.enterText(mEmail, "goutamvenkat@gmail.com");
        solo.enterText(mPassword, "XYZ");
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                RegisterButton.performClick();
            }
        });
        assertTrue("Invalid Credentials", solo.waitForText("Registration Failed"));
        solo.clickOnButton("Ok");

    }

    /**
     * Tests Register feature when you enter used Username
     */
    public void testUsedUser() {
        try {
            setUp();
        } catch (Exception e) {
            return;
        };

        solo.enterText(mUserName, "g"); //Used username
        solo.enterText(mEmail, "goutamvenkat@gmail.com");
        solo.enterText(mPassword, "XYZ");
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                RegisterButton.performClick();
            }
        });
        assertTrue("Invalid Credentials", solo.waitForText("Registration Failed"));
        solo.clickOnButton("Ok");

    }

    /**
     * Correct login test makes random user
     */
    public void testValidLoginCredentials() {
        try {
            setUp();
        } catch (Exception e) {
            return;
        };
        Random r = new Random();        //To create random users

        int n[] = new int[] {97 + r.nextInt(26),97 + r.nextInt(26)};
        String em = ((char)n[0] + "@" + (char)n[1] + ".com");

        n = new int[] {97 + r.nextInt(26),97 + r.nextInt(26)};
        String us = ((char)n[0] + "a" + (char)n[1] + "om");

        solo.enterText(mUserName, us);
        solo.enterText(mEmail, em);
        solo.enterText(mPassword, "s");
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                RegisterButton.performClick();
            }
        });
        assertTrue(solo.waitForText("Success"));
        solo.clickOnButton("Logout");
    }





}
