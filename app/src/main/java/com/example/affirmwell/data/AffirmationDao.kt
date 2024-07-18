package com.example.affirmwell.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AffirmationDao {
    @Query("SELECT * FROM affirmations WHERE category = :category")
    fun getAffirmationsByCategory(category: String): Flow<List<Affirmation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAffirmations(affirmations: List<Affirmation>)

    @Insert
    suspend fun insertAffirmation(affirmation: Affirmation)

    @Update
    suspend fun updateAffirmation(affirmation: Affirmation)

    @Delete
    suspend fun deleteAffirmation(affirmation: Affirmation)

    @Query("SELECT * FROM affirmations WHERE isFavorite = 1")
    suspend fun getFavoriteAffirmations(): List<Affirmation>

    @Query("SELECT COUNT(*) FROM affirmations")
    suspend fun getAffirmationsCount(): Int
}
