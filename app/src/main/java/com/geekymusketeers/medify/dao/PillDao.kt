package com.geekymusketeers.medify.dao

import androidx.room.*
import com.geekymusketeers.medify.model.Pill
import com.geekymusketeers.medify.model.PillEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PillDao {
    @Transaction
    @Query("SELECT * FROM pill WHERE deleted = 0 ORDER BY pillId ASC")
    fun getEverythingFlow(): Flow<List<Pill>>

    @Transaction
    @Query("SELECT * FROM pill WHERE deleted = 0 ORDER BY pillId ASC")
    suspend fun getEverything(): List<Pill>

    @Transaction
    @Query("SELECT * FROM pill ORDER BY pillId ASC")
    fun getEverythingIncludingDeletedFlow(): Flow<List<Pill>>

    @Transaction
    @Query("SELECT * FROM pill ORDER BY pillId ASC")
    suspend fun getEverythingIncludingDeleted(): List<Pill>

    @Transaction
    @Query("SELECT * FROM pill WHERE pillId = (:pillId)")
    fun getWithIdFlow(pillId: Long): Flow<Pill>

    @Transaction
    @Query("SELECT * FROM pill WHERE pillId = (:pillId)")
    suspend fun getWitId(pillId: Long): Pill

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPillEntity(pillEntity: PillEntity): Long

    @Update
    suspend fun updatePillEntity(pillEntity: PillEntity)

    @Delete
    suspend fun deletePillEntity(pillEntity: PillEntity)
}