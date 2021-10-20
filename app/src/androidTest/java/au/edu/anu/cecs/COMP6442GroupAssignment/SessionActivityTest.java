package au.edu.anu.cecs.COMP6442GroupAssignment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
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
public class SessionActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);
    private String stringToBetyped;

    @Before
    public void initValidString() {
        // Specify a valid string.
        stringToBetyped = "Espresso";

        // Sign in if there is not a user.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                            "qinyu.zhao@anu.edu.au",
                            "123456");
        }
    }

    @Test
    public void testIsActivityInView() {
        onView(withId(R.id.session)).check(matches(isDisplayed()));
    }

    @Test
    public void testVisibilityOfElements() {
        int[] ids = new int[]{R.id.viewPager, R.id.tabLayout};

        for (int id : ids) {
            onView(withId(id)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testContextOfElements() {
    }

    @Test
    public void testLogOut() {
        onView(withId(R.id.tab_item_me)).perform(click());
        onView(withId(R.id.LogOut)).perform(click());
    }
}