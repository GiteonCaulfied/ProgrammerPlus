package au.edu.anu.cecs.COMP6442GroupAssignment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.filters.MediumTest;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule
            = new ActivityScenarioRule<>(LoginActivity.class);

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
        onView(withId(R.id.activity_sign_in)).check(matches(isDisplayed()));
    }

    @Test
    public void testVisibilityOfElements() {
        int[] ids = new int[]{R.id.signin, R.id.password_signin, R.id.email_signin, R.id.imageView4};

        for (int id : ids) {
            onView(withId(id)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testContextOfElements() {
        onView(withId(R.id.email_signin)).check(matches(withHint(R.string.email)));
        onView(withId(R.id.password_signin)).check(matches(withHint(R.string.password)));
        onView(withId(R.id.signin)).check(matches(withText(R.string.sign_in)));
    }

    @Test
    public void testWrongPassword() {
        onView(withId(R.id.email_signin)).perform(typeText("qinyu.zhao@anu.edu.au"));
        onView(withId(R.id.password_signin)).perform(typeText("123555")).perform(closeSoftKeyboard());
        onView(withId(R.id.signin)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.activity_sign_in)).check(matches(isDisplayed()));
    }

    @Test
    public void testCorrectlySignIn() {
        onView(withId(R.id.email_signin)).perform(typeText("qinyu.zhao@anu.edu.au"));
        onView(withId(R.id.password_signin)).perform(typeText("123456")).perform(closeSoftKeyboard());

        onView(withId(R.id.signin)).perform(click());

        // Login
        onView(withText("Login successfully.")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
        onView(withId(R.id.session)).check(matches(isDisplayed()));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Navigate to logout
        onView(withId(R.id.tab_item_me)).perform(click());
        onView(withId(R.id.LogOut)).perform(click());

    }
}