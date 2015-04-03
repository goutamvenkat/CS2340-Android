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

/**
 * Created by Suvrat on 4/3/15.
 *
 * @version 1.0
 */
public class LoginActivityFeaturesTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private LoginActivity myActivity;
    private EditText mUsername;
    private EditText mPassword;
    private Button LoginButton;
    private Button RegisterButton;
    private Button FacebookButton;
    private ImageView logo;

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
    }

    /**
     * Tests if all component of activity gets initialized.
     */
    public void testPreconditions() {
        try {
            setUp();
        } catch (Exception e) {

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

        };
        final View decorView = myActivity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, logo);
        assertTrue(View.VISIBLE == logo.getVisibility());
    }

}

