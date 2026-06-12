package com.myapp.moderntodo.data.repository

import com.myapp.moderntodo.data.local.dao.TaskDao
import com.myapp.moderntodo.data.toDomain
import com.myapp.moderntodo.data.toEntity
import com.myapp.moderntodo.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }
}