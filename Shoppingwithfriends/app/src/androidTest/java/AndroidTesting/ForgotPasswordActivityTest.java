package AndroidTesting;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.ShoppingWithFriends.ForgotPasswordActivity;
import com.example.android.ShoppingWithFriends.R;
import com.robotium.solo.Solo;

/**
 * Created by Goutam Venkat on 3/31/15.
 */
public class ForgotPasswordActivityTest extends ActivityInstrumentationTestCase2<ForgotPasswordActivity> {
    private Activity myActivity;
    private EditText emailEditText;
    private Button button;
    private Solo solo;
    public ForgotPasswordActivityTest() {
        super(ForgotPasswordActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        myActivity = getActivity();
        solo = new Solo(getInstrumentation(), myActivity);
        setActivityInitialTouchMode(true);
        emailEditText = (EditText) myActivity.findViewById(R.id.forgotPasswordEmail);
        button = (Button) myActivity.findViewById(R.id.sendEmailButton);
    }

    /**
     * Tests empty string
     */
    public void testInvalidEmptyString() {
        solo.enterText(emailEditText, "");
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });
        assertTrue("Invalid Email", solo.waitForText("Not of email format"));
        solo.clickOnButton("Ok");

    }

    /**
     * Tests with no @ symbol
     */
    public void testInvalidNoAt() {
        solo.enterText(emailEditText, "gg.com");

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });
        assertEquals(true, solo.waitForText("Not of email format"));
        solo.clickOnButton("Ok");
    }

    /**
     * Tests with email with no '.'
     */
    public void testInvalidNoDot() {
        solo.enterText(emailEditText, "g@gcom");

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });
        assertEquals(true, solo.waitForText("Not of email format"));
        solo.clickOnButton("Ok");
    }

    /**
     * Tests crazy string
     */
    public void testInvalidCrazy() {
        solo.enterText(emailEditText, "@g.g.com");

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });
        assertEquals(true, solo.waitForText("Not of email format"));
        solo.clickOnButton("Ok");
    }

    /**
     * Tests with @ at beginning of string
     */
    public void testInvalidAtBeginning() {
        solo.enterText(emailEditText, "@@.com");

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });
        assertEquals(true, solo.waitForText("Not of email format"));
        solo.clickOnButton("Ok");
    }

    /**
     * Email begins with '.'
     */
    public void testInvalidDotBeginning() {
        solo.enterText(emailEditText, "..@");

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });
        assertEquals(true, solo.waitForText("Not of email format"));
        solo.clickOnButton("Ok");
    }
    public void testValidEmail() {
        solo.enterText(emailEditText, "g@g.com");
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });
        assertEquals(true, solo.waitForText("Not a registered email!"));
        solo.clickOnButton("Ok");
    }
    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }
}
