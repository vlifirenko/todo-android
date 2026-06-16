package com.myapp.moderntodo.ui.screen.todo

import app.cash.turbine.test
import com.myapp.moderntodo.data.repository.TaskRepository
import com.myapp.moderntodo.domain.model.Task
import com.myapp.moderntodo.domain.model.TaskStatus
import com.myapp.moderntodo.ui.screens.todo.TodoViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class KanbanViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: TaskRepository = mockk()

    private val sampleTasks = listOf(
        Task(id = 1, title = "Buy milk", description = "Milk for me", status = TaskStatus.TODO, createdAt = 1000L),
        Task(id = 2, title = "Buy food", description = "Food for dog", status = TaskStatus.DONE, createdAt = 2000L),
    )

    @Test
    fun `when repository returns tasks, viewModel groups them correctly by status`() = runTest {
        coEvery { repository.getAllTasks() } returns flowOf(sampleTasks)

        val viewModel = TodoViewModel(repository)

        viewModel.uiState.test {
            val initialState = awaitItem()

            val finalState = if (initialState.isLoading) awaitItem() else initialState

            assertFalse(finalState.isLoading)
            assertEquals(1, finalState.todoTasks.size)
            assertEquals("Buy milk", finalState.todoTasks.first().title)

            assertEquals(0, finalState.inProgressTasks.size)

            assertEquals(1, finalState.doneTasks.size)
            assertEquals("Buy food", finalState.doneTasks.first().title)
        }
    }

    @Test
    fun `when search query changes, viewModel filters tasks correctly`() = runTest {
        coEvery { repository.getAllTasks() } returns flowOf(sampleTasks)
        val viewModel = TodoViewModel(repository)

        viewModel.uiState.test {
            val stateBeforeSearch = awaitItem()
            val actualState = if (stateBeforeSearch.isLoading) awaitItem() else stateBeforeSearch
            assertEquals(1, actualState.todoTasks.size)

            viewModel.onSearchQueryChanged("food")

            val stateAfterSearch = awaitItem()

            assertEquals("food", stateAfterSearch.searchQuery)
            assertEquals(0, stateAfterSearch.todoTasks.size)
            assertEquals(1, stateAfterSearch.doneTasks.size)
            assertEquals("Buy food", stateAfterSearch.doneTasks.first().title)
        }
    }
}