package com.conestoga.lifetracker.domain.model

/**
 * Domain model for a Note.
 * Independent from database and API representations.
 */
data class Note(
    val id: Int = 0,
    val title: String,
    val createdAt: Long = System.currentTimeMillis()
)
