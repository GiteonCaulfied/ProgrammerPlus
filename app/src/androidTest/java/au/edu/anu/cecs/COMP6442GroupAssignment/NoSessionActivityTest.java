package au.edu.anu.cecs.COMP6442GroupAssignment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NoSessionActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);
    private String stringToBetyped;

    @Before
    public void initValidString() {
        // Specify a valid string.
        stringToBetyped = "Espresso";

        // Log out if there is a user.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null)
            FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void testIsActivityInView() {
        onView(withId(R.id.no_session)).check(matches(isDisplayed()));
    }

    @Test
    public void testVisibilityOfElements() {
        int[] ids = new int[]{R.id.imageView2, R.id.textView, R.id.main_sign_in, R.id.main_register,
                R.id.textView4};

        for (int id : ids) {
            onView(withId(id)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testContextOfElements() {
        onView(withId(R.id.textView)).check(matches(withText(R.string.welcome_to)));
        onView(withId(R.id.main_sign_in)).check(matches(withText(R.string.sign_in)));
        onView(withId(R.id.main_register)).check(matches(withText(R.string.register)));
        onView(withId(R.id.textView4)).check(matches(withText(R.string.programmer)));
    }

    @Test
    public void testNavSignIn() {
        onView(withId(R.id.main_sign_in)).perform(click());
        onView(withId(R.id.activity_sign_in)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavRegister() {
        onView(withId(R.id.main_register)).perform(click());
        onView(withId(R.id.register)).check(matches(isDisplayed()));
    }
}