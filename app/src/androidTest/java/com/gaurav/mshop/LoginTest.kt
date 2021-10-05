package com.gaurav.mshop

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import com.gaurav.mshop.ui.login.LoginFragment
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class LoginTest {

//    @get:Rule
//    val testRule = launchFragmentInContainer<LoginFragment>()

//    private lateinit var loginViewModel: LoginViewModel

//    @Before fun testLoginUIt(){
//        loginViewModel = mock {
//
//        }
//    }


    @Test
    fun testLoginUI(){
        launchFragmentInContainer<LoginFragment>()
        onView(withId(R.id.etLoginEmail))
            .perform(typeText("daya@gmail.com"))

        onView(withId(R.id.etLoginPassword))
            .perform(typeText("daya"))

        closeSoftKeyboard()

        onView(withId(R.id.btnLogin))
            .perform(click())

        onView(withId(R.id.tvFullName))
            .check(matches(withText("Dayag Mahato")))
    }
}