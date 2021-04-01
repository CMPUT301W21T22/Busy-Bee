package com.example.spearmint;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ProfileFragmentTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void checkCreateProfile(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.profile_fragment));

        solo.enterText((EditText) solo.getView(R.id.edit_text_username), "gavriellowie");
        solo.enterText((EditText) solo.getView(R.id.edit_text_email), "gavriellowie@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.edit_text_phone), "7804496315");

        assertTrue(solo.waitForText("gavriellowie", 1, 2000));
        assertTrue(solo.waitForText("gavriellowie@ualberta.ca", 1, 2000));
        assertTrue(solo.waitForText("7804496315", 1, 2000));

        solo.clickOnView(solo.getView(R.id.button_save_profile));


    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
