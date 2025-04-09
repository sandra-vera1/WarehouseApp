package com.example.warehouseapp.screen

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.warehouseapp.MainActivity
import com.example.warehouseapp.data.models.User
import com.example.warehouseapp.ui.screens.LoginScreen
import com.example.warehouseapp.util.repository.FakeUserRepository
import com.example.warehouseapp.util.viewmodel.FakeUserViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeUserViewModel: FakeUserViewModel
    private lateinit var fakeUserRepository: FakeUserRepository

    private var loginSuccessCalled = false
    private var errorMessage: String? = null

    @Before
    fun setup() {
        fakeUserRepository = FakeUserRepository()
        fakeUserViewModel = FakeUserViewModel(fakeUserRepository)

        // Seed user
        val testUser = User(id = 1, userName = "test", password = "1234", role = 1)
        fakeUserRepository.addUser(testUser)

        loginSuccessCalled = false
        errorMessage = null

        composeTestRule.setContent {
            LoginScreen(
                viewModel = fakeUserViewModel,
                onLoginSuccess = { loginSuccessCalled = true },
                showErrorMessage = { msg -> errorMessage = msg }
            )
        }
    }

    @Test
    fun login_withValidCredentials_triggersLoginSuccess() {
        composeTestRule.onNodeWithText("Username").performTextInput("test")
        composeTestRule.onNodeWithText("Password").performTextInput("1234")
        composeTestRule.onNodeWithText("Login").performClick()

        composeTestRule.waitUntil(3_000) { loginSuccessCalled }
        assert(loginSuccessCalled)
    }

    @Test
    fun login_withInvalidCredentials_showsError() {
        composeTestRule.onNodeWithText("Username").performTextInput("wrong")
        composeTestRule.onNodeWithText("Password").performTextInput("wrong")
        composeTestRule.onNodeWithText("Login").performClick()

        composeTestRule.waitUntil(3_000) { errorMessage != null }
        assert(errorMessage == "The username or password is incorrect. Try again")
    }
}