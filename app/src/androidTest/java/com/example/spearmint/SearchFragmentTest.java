package com.example.spearmint;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SearchFragmentTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<MainActivity>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void checkSearchFragment(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.search_fragment));

        solo.clickOnView(solo.getView(R.id.search_action));
        solo.clickOnView(solo.getView(R.id.search_action));

        solo.enterText((EditText) solo.getView(R.id.search_action), "Edmonton");
    }



    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
