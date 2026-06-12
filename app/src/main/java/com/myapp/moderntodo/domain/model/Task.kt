package com.myapp.moderntodo.domain.model

data class Task(
    val id: Long,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val createdAt: Long,
)