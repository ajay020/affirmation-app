package com.example.affirmwell.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "affirmations")
data class Affirmation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val text: String,
    val isCustom: Boolean = false
)

