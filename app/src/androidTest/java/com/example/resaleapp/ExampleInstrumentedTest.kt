package com.example.resaleapp

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import java.util.regex.Pattern.matches

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.resaleapp", appContext.packageName)

        //onView(withId(R.menu.menu_main)).perform(click())
        //onView(withId(R.id.action_signout)).perform(click())
        onView(withId(R.id.button_signin)).perform(click())

        //onView(ViewMatchers.withText("Login Fragment"))
        //    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        //onView(withId(R.id.emailInputField))
        //    .perform(clearText())
        //    .perform(typeText("z@z.com"))
        //onView(withId(R.id.passwordInputField))
        //    .perform(clearText())
        //    .perform(typeText("123456"))
            //.perform(closeSoftKeyboard())
        //onView(withId(R.id.sign_in)).perform(click())
    }
}