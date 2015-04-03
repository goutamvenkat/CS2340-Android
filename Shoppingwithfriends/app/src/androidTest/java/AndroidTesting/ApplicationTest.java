package AndroidTesting;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.example.android.ShoppingWithFriends.LoginActivity;
import com.example.android.ShoppingWithFriends.R;

/**
 * Created by Suvrat on 4/2/15.
 */
public class ApplicationTest
        extends ActivityInstrumentationTestCase2<LoginActivity> {

    private LoginActivity mFirstTestActivity;
    private TextView mFirstTestText;

    public ApplicationTest() {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mFirstTestActivity = getActivity();
        mFirstTestText =
                (TextView) mFirstTestActivity
                        .findViewById(R.id.editText);
    }

    public void testPreconditions() {
        assertNotNull(mFirstTestActivity);
    }
}