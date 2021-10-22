package au.edu.anu.cecs.COMP6442GroupAssignment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.allOf;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@LargeTest
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SessionActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void initValidString() {
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
    public void test1_IsActivityInView() {
        onView(withId(R.id.session)).check(matches(isDisplayed()));
    }

    @Test
    public void test2_VisibilityOfElements() {
        int[] ids = new int[]{R.id.viewPager, R.id.tabLayout};

        for (int id : ids) {
            onView(withId(id)).check(matches(isDisplayed()));
        }

        onView(withId(R.id.layout_post)).check(matches(isDisplayed()));
    }

    @Test
    public void test3_NavToFriends() {
        onView(withId(R.id.tabLayout)).perform(selectTabAtPosition(1));
        onView(withId(R.id.layout_friend)).check(matches(isDisplayed()));
    }

    @Test
    public void test4_NavToChats() {
        onView(withId(R.id.tabLayout)).perform(selectTabAtPosition(2));
        try {
            Thread.sleep(100);
        } catch (Exception ignored) {

        }
        onView(withId(R.id.layout_chat)).check(matches(isDisplayed()));
    }

    @Test
    public void test5_NavToProfiles() {
        onView(withId(R.id.tabLayout)).perform(selectTabAtPosition(3));
        try {
            Thread.sleep(100);
        } catch (Exception ignored) {

        }
        onView(withId(R.id.layout_profile)).check(matches(isDisplayed()));
    }

    @NonNull
    private static ViewAction selectTabAtPosition(int position) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }

            @Override
            public String getDescription() {
                return "with tab at index" + String.valueOf(position);
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view instanceof TabLayout) {
                    TabLayout tabLayout = (TabLayout) view;
                    TabLayout.Tab tab = tabLayout.getTabAt(position);

                    if (tab != null) {
                        tab.select();
                    }
                }
            }
        };
    }
}