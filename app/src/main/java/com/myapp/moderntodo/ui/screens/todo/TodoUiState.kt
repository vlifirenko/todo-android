package com.myapp.moderntodo.ui.screens.todo

import com.myapp.moderntodo.domain.model.Task

data class TodoUiState(
    val todoTasks: List<Task> = emptyList(),
    val inProgressTasks: List<Task> = emptyList(),
    val doneTasks: List<Task> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
)