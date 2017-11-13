package com.example.uporabnik.sestevalnik;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        Intent intent = new Intent(appContext, MainActivity.class);
        appContext.startActivity(intent);


        ViewInteraction prva = onView(withId(R.id.prvaSt));
        ViewInteraction druga = onView(withId(R.id.drugaSt));
        ViewInteraction vsota = onView(withId(R.id.vsota));
        ViewInteraction btn = onView(withId(R.id.button));

        //druga.perform(typeText("15156"));
       Random rnd = new Random();
        for (int i = 0; i < 1; i++) {
            int st1 = rnd.nextInt(50000);
            int st2 = rnd.nextInt(50000);
            int s   = st1 + st2;

            // Vnos naključnih številk v polje
            prva.perform(typeText(Integer.toString(st1)));
            druga.perform(typeText(Integer.toString(st2)));

            btn.perform(click());
            vsota.check(matches(withText(Integer.toString(s))));
            Thread.sleep(1000);
        }

    }
}
