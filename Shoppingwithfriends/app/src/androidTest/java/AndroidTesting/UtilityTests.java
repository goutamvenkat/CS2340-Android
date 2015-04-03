package AndroidTesting;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

import com.example.android.ShoppingWithFriends.LoginActivity;
import com.example.android.ShoppingWithFriends.Utility;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Abhishek with help from Suvrat on 4/3/15.
 */
@SuppressWarnings("DefaultFileTemplate")
public class UtilityTests extends ActivityUnitTestCase<LoginActivity> {

    private Intent launchIntent;

    public UtilityTests() {
        super(LoginActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        launchIntent = new Intent(getInstrumentation().getTargetContext(), LoginActivity.class);
    }

//    public void testActivityLaunch() {
//        startActivity(launchIntent, null, null);
//
//
//        final EditText mUsername = (EditText) getActivity().findViewById(R.id.LoginUsername);
//        final EditText mPassword = (EditText) getActivity().findViewById(R.id.LoginPassword);
//        final Button LoginButton = (Button) getActivity().findViewById(R.id.LoginButton);
//
//        getInstrumentation().runOnMainSync(new Runnable() {
//            @Override
//            public void run() {
//                mUsername.requestFocus();
//                mUsername.setText("G");
//            }
//        });
//        getInstrumentation().waitForIdleSync();
//
//        getInstrumentation().runOnMainSync(new Runnable() {
//            @Override
//            public void run() {
//                mPassword.requestFocus();
//                mPassword.setText("x");
//            }
//        });
//        getInstrumentation().waitForIdleSync();
//
//        assertEquals("G", mUsername.getEditableText().toString());
//        assertEquals("x", mPassword.getEditableText().toString());
//
//
//        LoginButton.performClick();
//
//
//        final Intent newIntent = getStartedActivityIntent();
//        assertNotNull("Intent was null", newIntent);
//        assertTrue(isFinishCalled());
//
//    }

    private JSONArray getArray() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(0, 1);
        list.add(1, 2);
        list.add(2,3);
        JSONArray array = new JSONArray(list);
        return array;
    }

    public void testUtilityNull() {
        JSONArray array = null;
        //noinspection ConstantConditions
        assertEquals(new JSONArray(), Utility.remove(array, -1));
    }

    public void testIndexOutOfBound() {
        assertEquals(getArray(), Utility.remove(getArray(), -1));
    }

    public void testSuccessCase() {
        JSONArray arr = getArray();
        JSONArray modified = Utility.remove(arr, 0);
        ArrayList<Integer> list = new ArrayList<>();
        list.add(0, 2);
        list.add(1,3);
        JSONArray array = new JSONArray(list);
        assertEquals(array, modified);
    }

}
