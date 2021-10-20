package au.edu.anu.cecs.COMP6442GroupAssignment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {
    private String stringToBetyped;

    @Rule
    public ActivityScenarioRule<RegisterActivity> activityRule
            = new ActivityScenarioRule<>(RegisterActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        stringToBetyped = "Espresso";
    }

    @Test
    public void testIsActivityInView() {
        onView(withId(R.id.register)).check(matches(isDisplayed()));
    }

    @Test
    public void testVisibilityOfElements() {
        int[] ids = new int[]{R.id.password_signup, R.id.confirm_signup, R.id.email_signup, R.id.name_signup,
                R.id.portrait_signup, R.id.signUp, R.id.intro_signup, R.id.selectImage_signup, R.id.friend_switch};

        for (int id : ids) {
            onView(withId(id)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testContextOfElements() {
        onView(withId(R.id.password_signup)).check(matches(withHint(R.string.hintPassword)));
        onView(withId(R.id.confirm_signup)).check(matches(withHint(R.string.confirm_password)));
        onView(withId(R.id.email_signup)).check(matches(withHint(R.string.email)));
        onView(withId(R.id.name_signup)).check(matches(withHint(R.string.name)));
        onView(withId(R.id.intro_signup)).check(matches(withHint(R.string.my_introduction)));

        onView(withId(R.id.selectImage_signup)).check(matches(withText(R.string.select_image)));
        onView(withId(R.id.signUp)).check(matches(withText(R.string.Sign_up)));
        onView(withId(R.id.friend_switch)).check(matches(withText(R.string.friendSend)));
    }

    @Test
    public void testIntent() {

    }
}