package com.myapp.moderntodo.ui.screens.todo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myapp.moderntodo.domain.model.TaskStatus

@Composable
fun TodoScreen(
    viewModel: TodoViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var newTitle by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            label = { Text("Search tasks...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "New task", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = newDescription,
                    onValueChange = { newDescription = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (newTitle.isNotBlank()) {
                            viewModel.addTask(newTitle, newDescription)
                            newTitle = ""
                            newDescription = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TodoColumn(
                    title = "TODO",
                    tasks = uiState.todoTasks,
                    onMoveForward = { viewModel.updateTaskStatus(it, TaskStatus.IN_PROGRESS) },
                    onMoveBackward = {},
                    onDelete = { viewModel.deleteTask(it) },
                    modifier = Modifier.weight(1f)
                )
                TodoColumn(
                    title = "In progress",
                    tasks = uiState.inProgressTasks,
                    onMoveForward = { viewModel.updateTaskStatus(it, TaskStatus.DONE) },
                    onMoveBackward = { viewModel.updateTaskStatus(it, TaskStatus.TODO) },
                    onDelete = { viewModel.deleteTask(it) },
                    modifier = Modifier.weight(1f)
                )
                TodoColumn(
                    title = "Done",
                    tasks = uiState.doneTasks,
                    onMoveForward = {},
                    onMoveBackward = { viewModel.updateTaskStatus(it, TaskStatus.IN_PROGRESS) },
                    onDelete = { viewModel.deleteTask(it) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}