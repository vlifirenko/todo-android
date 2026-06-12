package com.myapp.moderntodo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myapp.moderntodo.domain.model.TaskStatus

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val status: String,
    val createdAt: Long
)