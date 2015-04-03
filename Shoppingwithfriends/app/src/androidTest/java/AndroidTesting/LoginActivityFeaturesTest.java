package AndroidTesting;

import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.android.ShoppingWithFriends.LoginActivity;
import com.example.android.ShoppingWithFriends.R;
import com.robotium.solo.Solo;

/**
 * Tests Login Activity Features
 *
 * @author Suvrat Bhooshan
 * @version 2.0
 */
public class LoginActivityFeaturesTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private LoginActivity myActivity;
    private EditText mUsername;
    private EditText mPassword;
    private Button LoginButton;
    private Button RegisterButton;
    private Button FacebookButton;
    private ImageView logo;
    private Solo solo;

    public LoginActivityFeaturesTest() {
        super(LoginActivity.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(true);

        myActivity = getActivity();

        mUsername = (EditText) myActivity.findViewById(R.id.LoginUsername);
        mPassword = (EditText) myActivity.findViewById(R.id.LoginPassword);
        LoginButton = (Button) myActivity.findViewById(R.id.LoginButton);
        RegisterButton = (Button) myActivity.findViewById(R.id.LoginRegister);
        FacebookButton = (Button) myActivity.findViewById(R.id.FacebookRegister);
        logo = (ImageView) myActivity.findViewById(R.id.Logo);

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
        assertNotNull(mUsername);
        assertNotNull(mPassword);
        assertNotNull(LoginButton);
        assertNotNull(RegisterButton);
        assertNotNull(logo);
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

        ViewAsserts.assertOnScreen(decorView, LoginButton);
        ViewAsserts.assertOnScreen(decorView, RegisterButton);
        ViewAsserts.assertOnScreen(decorView, mUsername);
        ViewAsserts.assertOnScreen(decorView, mPassword);
        ViewAsserts.assertOnScreen(decorView, FacebookButton);

        final ViewGroup.LayoutParams layoutParams = LoginButton.getLayoutParams();
        assertNotNull(layoutParams);
        assertEquals(layoutParams.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);

        final ViewGroup.LayoutParams layoutParams1 = RegisterButton.getLayoutParams();
        assertNotNull(layoutParams1);
        assertEquals(layoutParams1.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams1.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    /**
     * Tests presence of Logo
     */
    public void testLogo() {
        try {
            setUp();
        } catch (Exception e) {
            return;
        };
        final View decorView = myActivity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, logo);
        assertTrue(View.VISIBLE == logo.getVisibility());
    }

    /**
     * Tests Login feature when you leave the fields blank
     */
    public void testEmptyLoginCredentials() {
        try {
            setUp();
        } catch (Exception e) {
            return;
        };

        solo.enterText(mUsername, "");
        solo.enterText(mPassword, "");
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                LoginButton.performClick();
            }
        });
        assertTrue("Invalid Credentials", solo.waitForText("Empty Fields"));
        solo.clickOnButton("Ok");
    }

    /**
     * Tests Login feature when you enter invalid credentials
     */
    public void testInvalidUserCredentials() {
        try {
            setUp();
        } catch (Exception e) {
            return;
        };

        solo.enterText(mUsername, "XYZ");
        solo.enterText(mPassword, "XYZ");
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                LoginButton.performClick();
            }
        });
        assertTrue("Invalid Credentials", solo.waitForText("Login Failed!"));
        solo.clickOnButton("Ok");

    }

    /**
     * Correct login test
     */
    public void testValidLoginCredentials() {
        try {
            setUp();
        } catch (Exception e) {
            return;
        };

        solo.enterText(mUsername, "g");
        solo.enterText(mPassword, "x");
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                LoginButton.performClick();
            }
        });
        assertTrue(solo.waitForText("Welcome Back"));
        solo.clickOnButton("Logout");
    }

}

