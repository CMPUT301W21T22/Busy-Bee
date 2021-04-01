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

public class ExperimentFragmentTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }


    // Checking to see if the adding button works or not
    @Test
    public void checkAddExperiment(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.add_button));

        solo.enterText((EditText) solo.getView(R.id.description), "Coin Flip Test IntentTesting321321312");
        solo.enterText((EditText) solo.getView(R.id.region), "Vancouver");
        solo.enterText((EditText) solo.getView(R.id.count), " 2132131");

        assertTrue(solo.waitForText("Vancouver", 1, 2000));


        solo.clickOnView(solo.getView(R.id.publish_button));
    }

    // Making sure that the cancel button works and doesn't post the experiment
    @Test
    public void checkCancelExperiment(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.add_button));

        solo.enterText((EditText) solo.getView(R.id.description), "Bottle Flip Test");
        solo.enterText((EditText) solo.getView(R.id.region), "Red Deer");
        solo.enterText((EditText) solo.getView(R.id.count), "2132131");


        assertTrue(solo.waitForText("Bottle Flip Test", 1, 2000));
        assertTrue(solo.waitForText("Red Deer", 1, 2000));
        assertTrue(solo.waitForText("2132131", 1, 2000));

        solo.clickOnView(solo.getView(R.id.cancel));
    }

    // Checking to see if the asking question works
    @Test
    public void checkAskQuestion(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.add_button));
        solo.enterText((EditText) solo.getView(R.id.description), "Coin Flip Test IntentTesting1");
        solo.enterText((EditText) solo.getView(R.id.region), "Vancouver");
        solo.enterText((EditText) solo.getView(R.id.count), "1");

        assertTrue(solo.waitForText("Vancouver", 1, 2000));

        solo.clickOnView(solo.getView(R.id.publish_button));

        solo.clickInList(0);

        solo.enterText((EditText) solo.getView(R.id.post_question), "How long was the experiment taken?");
        solo.clickOnView(solo.getView(R.id.post_question_button));

        assertTrue(solo.waitForText("How long was the experiment taken?", 1, 2000));

    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
