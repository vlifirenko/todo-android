package com.myapp.moderntodo.data

import com.myapp.moderntodo.data.local.entity.TaskEntity
import com.myapp.moderntodo.domain.model.Task
import com.myapp.moderntodo.domain.model.TaskStatus

fun TaskEntity.toDomain(): Task {
    return Task(
        id = this.id,
        title = this.title,
        description = this.description,
        status = try {
            TaskStatus.valueOf(this.status)
        } catch (e: IllegalArgumentException) {
            TaskStatus.TODO
        },
        createdAt = this.createdAt
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        status = this.status.name,
        createdAt = this.createdAt
    )
}