@file:OptIn(ExperimentalCoroutinesApi::class)

package com.iucoding.chatgptclient.viewmodel

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.iucoding.chatgptclient.MainCoroutineExtension
import com.iucoding.chatgptclient.R
import com.iucoding.chatgptclient.actions.UIEvent
import com.iucoding.chatgptclient.composable.UiText
import com.iucoding.chatgptclient.composable.toUiText
import com.iucoding.chatgptclient.networking.Result
import com.iucoding.chatgptclient.repository.ChatGptFacade
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MainViewModelTest {

    // region instances
    private lateinit var viewModel: MainViewModel
    private lateinit var dispatcher: TestDispatcher
    private lateinit var facade: ChatGptFacade
    // endregion

    // region shared
    @BeforeEach
    fun setUp() {
        dispatcher = mainCoroutineExtension.dispatcher
        facade = mockk(relaxed = true, relaxUnitFun = true)
        viewModel = MainViewModel(
            facade = facade,
            dispatcher = dispatcher
        )
    }

    @AfterEach
    fun cleanUp() {
        clearAllMocks()
    }
    // endregion

    // region tests
    @Test
    fun `sendEvent, when Search with empty prompt is received, displays toast`() = runTest {
        viewModel.toast.test {
            val event = UIEvent.Search(prompt = "")
            viewModel.sendEvent(event)

            val emission1 = awaitItem()
            assertThat(emission1).isInstanceOf(UiText.StringResource::class.java)
            assertThat((emission1 as UiText.StringResource).id).isEqualTo(R.string.prompt_is_empty)
        }
    }

    @Test
    fun `sendEvent, when Search with prompt is received, fetches response from facade`() =
        runTest {
            // setup mocks
            coEvery { facade.prompt(any()) } coAnswers {
                delay(DELAY)
                Result.Success(SAMPLE_RESPONSE.toUiText())
            }

            // initial state check
            assertThat(viewModel.uiState.value.isLoading).isFalse()
            assertThat(viewModel.uiState.value.question).isNull()
            assertThat(viewModel.uiState.value.response).isNull()

            // action
            val event = UIEvent.Search(prompt = SAMPLE_QUESTION)
            viewModel.sendEvent(event)

            // initial state check
            assertThat(viewModel.uiState.value.isLoading).isTrue()
            assertThat((viewModel.uiState.value.question as UiText.DynamicString).value)
                .isEqualTo(SAMPLE_QUESTION)
            assertThat(viewModel.uiState.value.response).isNull()
            advanceTimeBy(DELAY * 2)

            // Response received check
            assertThat(viewModel.uiState.value.isLoading).isFalse()
            assertThat((viewModel.uiState.value.question as UiText.DynamicString).value)
                .isEqualTo(SAMPLE_QUESTION)
            assertThat((viewModel.uiState.value.response as UiText.DynamicString).value)
                .isEqualTo(SAMPLE_RESPONSE)
        }
    // endregion

    companion object {

        private const val DELAY = 10L
        private const val SAMPLE_QUESTION = "Question here"
        private const val SAMPLE_RESPONSE = "Response here"

        @JvmField
        @RegisterExtension
        val mainCoroutineExtension = MainCoroutineExtension()
    }
}
