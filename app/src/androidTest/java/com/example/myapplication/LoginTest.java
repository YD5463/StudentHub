package com.example.myapplication;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;


import androidx.annotation.StringRes;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.myapplication.auth.Login;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class LoginTest {

        @Rule
        public ActivityTestRule<Login> mActivityTestRule = new ActivityTestRule<com.example.myapplication.auth.Login>(com.example.myapplication.auth.Login.class);


        private String getString(@StringRes int resourceId) {
            return mActivityTestRule.getActivity().getString(resourceId);
        }
    @Test
    public void emailIsEmpty() {
        onView(withId(R.id.email_input)).perform(clearText());
        onView(withId(R.id.login_btn)).perform(click());
//        onView(withId(R.id.email_input)).check(matches(withError(getString(R.string.error_field_required))));
    }

    @Test
    public void passwordIsEmpty() {
        onView(withId(R.id.email_input)).perform(typeText("email@email.com"), closeSoftKeyboard());
        onView(withId(R.id.password_input)).perform(clearText());
        onView(withId(R.id.login_btn)).perform(click());
//        onView(withId(R.id.password_input)).check(matches(withError(getString(R.string.error_field_required))));
    }

    @Test
    public void emailIsInvalid() {
        onView(withId(R.id.email_input)).perform(typeText("invalid"), closeSoftKeyboard());
        onView(withId(R.id.login_btn)).perform(click());
//        onView(withId(R.id.email_input)).check(matches(withError(getString(R.string.error_invalid_email))));
    }

    @Test
    public void passwordIsTooShort() {
        onView(withId(R.id.email_input)).perform(typeText("email@email.com"), closeSoftKeyboard());
        onView(withId(R.id.password_input)).perform(typeText("1234"), closeSoftKeyboard());
        onView(withId(R.id.login_btn)).perform(click());
//        onView(withId(R.id.password_input)).check(matches(withError(getString(R.string.error_invalid_password))));
    }
        @Test
        public void test_not_logged_in_user(){
//            getInstrumentation().runOnMainSync(() -> {
//                FirebaseAuth.getInstance().signOut();
//                EditText email = loginActivity.findViewById(R.id.email_input);
//                EditText pass = loginActivity.findViewById(R.id.password_input);
//                email.setText("somemail@gmail.com");
//                pass.setText("longpass123");
//                Button loginBtn = loginActivity.findViewById(R.id.login_btn);
//                loginBtn.performClick();
//                assertNull(FirebaseAuth.getInstance().getCurrentUser());
//            });
        }

        @Test
        public void test_logged_in_user() throws InterruptedException {
            FirebaseAuth.getInstance().signOut();
            onView(withId(R.id.email_input)).perform(typeText("yosefdanan555@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.password_input)).perform(typeText("yosef5463"), closeSoftKeyboard());
            onView(withId(R.id.login_btn)).perform(click());

            onView(withText(getString(R.string.please_wait)))
                    .check(matches(not(isDisplayed())));
        }
        @Test
        public void test_invalid_data(){
            //TODO
        }

        @Test
        public void test_forgot_password(){
            //TODO
        }

    }

