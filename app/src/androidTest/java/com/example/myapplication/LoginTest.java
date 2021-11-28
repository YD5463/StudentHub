package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.hamcrest.core.IsInstanceOf.any;
import static org.junit.Assert.*;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.IdRes;
import androidx.test.espresso.AmbiguousViewMatcherException;
import androidx.test.espresso.NoMatchingRootException;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class LoginTest {

        @Rule
        public ActivityTestRule<com.example.myapplication.auth.Login> mActivityTestRule = new ActivityTestRule<com.example.myapplication.auth.Login>(com.example.myapplication.auth.Login.class);

        public com.example.myapplication.auth.Login loginActivity;

        @Before
        public void setUp() throws Exception {
            loginActivity = mActivityTestRule.getActivity();
        }

        static void waitFor(int ms) {
            final CountDownLatch signal = new CountDownLatch(1);

            try {
                signal.await(ms, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Assert.fail(e.getMessage());
            }
        }
        @Test
        public void test_not_logged_in_user(){
            getInstrumentation().runOnMainSync(() -> {
                FirebaseAuth.getInstance().signOut();
                EditText email = loginActivity.findViewById(R.id.email_input);
                EditText pass = loginActivity.findViewById(R.id.password_input);
                email.setText("somemail@gmail.com");
                pass.setText("longpass123");
                Button loginBtn = loginActivity.findViewById(R.id.login_btn);
                loginBtn.performClick();
                assertNull(FirebaseAuth.getInstance().getCurrentUser());
            });
        }

        @Test
        public void test_logged_in_user(){
            getInstrumentation().runOnMainSync(() -> {
                FirebaseAuth.getInstance().signOut();
                EditText email = loginActivity.findViewById(R.id.email_input);
                EditText pass = loginActivity.findViewById(R.id.password_input);
                email.setText("yosefdanan555@gmail.com");
                pass.setText("yosef5463");
                Button loginBtn = loginActivity.findViewById(R.id.login_btn);
                loginBtn.performClick();
                waitFor(1000); //TODO: change to smart wait
                assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
            });
        }
        @Test
        public void test_invalid_data(){
            //TODO
        }

        @Test
        public void test_forgot_password(){
            //TODO
        }


        @After
        public void tearDown() throws Exception {
            loginActivity = null;
        }
    }

