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

public class RequestItemTest extends ActivityInstrumentationTestCase2<RequestItem> {

    private RequestItem myActivity;
    private EditText itemName;
    private EditText itemPrice;
    private Button requestItemButton;

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




}