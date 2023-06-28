package com.example.storyappdicoding.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.test.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.example.storyappdicoding.StoryAppRepository
import com.example.storyappdicoding.api.models.Story
import com.example.storyappdicoding.api.models.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalPagingApi
class MainViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    @Mock
    private lateinit var repository: StoryAppRepository

    @Mock
    private lateinit var userPreference: UserPreference

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(repository, userPreference)
    }


    @Test
    fun `test successful loading of story data`() = runBlockingTest {
        val token = "dummy_token"
        val pagingData = PagingData.from(
            listOf(
                Story(
                    id = "1",
                    name = "Story 1",
                    description = "Description 1",
                    photoUrl = "photo_url_1"
                ),
                Story(
                    id = "2",
                    name = "Story 2",
                    description = "Description 2",
                    photoUrl = "photo_url_2"
                )
            )
        )

        // Mock the response from the repository
        Mockito.`when`(repository.getAllStories(token)).thenReturn(flow {
            emit(pagingData)
        })

        // Call the view model method and collect the result
        val result = mutableListOf<Story>()
        val job = launch {
            viewModel.getStories(token) { pagingData ->
                result.addAll(pagingData.collectData())
            }
        }

        // Delay to allow the collection to complete
        delay(100)

        // Verify the result is not empty
        assertFalse(result.isEmpty())

        // Verify the number of items in the result
        assertEquals(2, result.size)

        // Verify the first item in the result
        assertEquals("1", result[0].id)

        // Cancel the job
        job.cancel()
    }

    @Test
    fun `test no story data`() = runBlockingTest {
        val token = "dummy_token"
        val pagingData = PagingData.empty<Story>()

        // Mock the response from the repository
        Mockito.`when`(repository.getAllStories(token)).thenReturn(flow {
            emit(pagingData)
        })

        // Call the view model method and collect the result
        val result = mutableListOf<Story>()
        val job = launch {
            viewModel.getStories(token) { pagingData ->
                result.addAll(pagingData.collectData())
            }
        }

        // Delay to allow the collection to complete
        delay(100)

        // Verify the result is empty
        assertTrue(result.isEmpty())

        // Cancel the job
        job.cancel()
    }
}

private fun Any.collectData(): Collection<Story> {
    val result = mutableListOf<Story>()

    // Perform a check to see if the current object is a collection containing Story objects
    if (this is Collection<*>) {
        for (item in this) {
            if (item is Story) {
                result.add(item)
            }
        }
    }

    return result
}