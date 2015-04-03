package AndroidTesting;

/**
 * Created by Bhavani
 */

import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


import com.example.android.ShoppingWithFriends.RequestItem;
import com.example.android.ShoppingWithFriends.R;
import com.robotium.solo.Solo;

import java.util.Random;


public class RequestItemTest extends ActivityInstrumentationTestCase2<RequestItem> {

    private RequestItem myActivity;
    private EditText itemName;
    private EditText itemPrice;
    private Button requestItemButton;
    private Solo solo;


    public RequestItemTest() {
        super(RequestItem.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(true);

        myActivity = getActivity();


        itemName = (EditText) myActivity.findViewById(R.id.ItemName);
        itemPrice = (EditText) myActivity.findViewById(R.id.MaxPrice);
        requestItemButton = (Button) myActivity.findViewById(R.id.RequestItemButton);
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
        assertNotNull(itemName);
        assertNotNull(itemPrice);
        assertNotNull(requestItemButton);

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

        ViewAsserts.assertOnScreen(decorView, requestItemButton);
        ViewAsserts.assertOnScreen(decorView, itemName);
        ViewAsserts.assertOnScreen(decorView, itemPrice);



        final ViewGroup.LayoutParams layoutParams1 = requestItemButton.getLayoutParams();
        assertNotNull(layoutParams1);
        assertEquals(layoutParams1.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams1.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    /**
     * Tests Item request feature when you leave the fields blank
     */
    public void testEmptyItem() {
        try {
            setUp();
        } catch (Exception e) {
            return;
        };

        solo.enterText(itemName, "");
        solo.enterText(itemPrice, "");
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                requestItemButton.performClick();
            }
        });
        assertTrue("Invalid Credentials", solo.waitForText("Item Request Failed"));
        solo.clickOnButton("Ok");
    }

    /**
     * Tests RequestItem feature when item already exists
     */
    /*public void testExistingItem() {
        try {
            setUp();
        } catch (Exception e) {
            return;
        };

        solo.enterText(itemName, "3");
        solo.enterText(itemPrice, "3");
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                requestItemButton.performClick();
            }
        });
        assertTrue("Duplicate Credentials", solo.waitForText("Duplicate Item"));
        solo.clickOnButton("Ok");
    }
*/
    /**
     * Correct item test makes random item
     */
   /* public void testValidItem() {
        try {
            setUp();
        } catch (Exception e) {
            return;
        };
        Random r = new Random();        //To create random item



        int n[] = new int[] {97 + r.nextInt(26),97 + r.nextInt(26)};
        String us = ((char)n[0] + "a" + (char)n[1] + "om");

        solo.enterText(itemName, us);
        solo.enterText(itemPrice, Integer.toString(r.nextInt()));
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                requestItemButton.performClick();
            }
        });
        assertTrue(solo.waitForText("Success"));
    }*/





}