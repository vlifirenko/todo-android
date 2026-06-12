package com.myapp.moderntodo.ui.screens.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.moderntodo.data.repository.TaskRepository
import com.myapp.moderntodo.domain.model.Task
import com.myapp.moderntodo.domain.model.TaskStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<TodoUiState> = repository.getAllTasks()
        .combine(_searchQuery) { tasks, query ->
            val filteredTasks = if (query.isBlank()) {
                tasks
            } else {
                tasks.filter {
                    it.title.contains(query, ignoreCase = true)
                            || it.description.contains(query, ignoreCase = true)
                }
            }

            TodoUiState(
                todoTasks = filteredTasks.filter { it.status == TaskStatus.TODO },
                inProgressTasks = filteredTasks.filter { it.status == TaskStatus.IN_PROGRESS },
                doneTasks = filteredTasks.filter { it.status == TaskStatus.DONE },
                searchQuery = query,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TodoUiState()
        )

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun addTask(title: String, description: String) {
        viewModelScope.launch {
            val newTask = Task(
                id = 0,
                title = title,
                description = description,
                status = TaskStatus.TODO,
                createdAt = System.currentTimeMillis()
            )
            repository.insertTask(newTask)
        }
    }

    fun updateTaskStatus(task: Task, newStatus: TaskStatus) {
        viewModelScope.launch {
            repository.updateTask(task.copy(status = newStatus))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}