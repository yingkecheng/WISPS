package com.example.wisps.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WarningInfoDao {

    @Query("Select * FROM warning_info_table")
    fun getAll(): List<WarningInfo>

    @Query("SELECT * FROM warning_info_table WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<WarningInfo>

    @Query("SELECT * FROM warning_info_table WHERE time LIKE :time LIMIT 1")
    fun findByTime(time: String): WarningInfo

    @Insert
    fun insert(info: WarningInfo)

    @Delete
    fun delete(info: WarningInfo)

}