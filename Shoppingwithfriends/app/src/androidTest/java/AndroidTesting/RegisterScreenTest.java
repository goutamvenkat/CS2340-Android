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


import com.example.android.ShoppingWithFriends.RegisterScreen;
import com.example.android.ShoppingWithFriends.R;

public class RegisterScreenTest extends ActivityInstrumentationTestCase2<RegisterScreen> {

    private RegisterScreen myActivity;
    private EditText mUserName;
    private EditText mEmail;
    private EditText mPassword;
    private Button RegisterButton;

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




}
