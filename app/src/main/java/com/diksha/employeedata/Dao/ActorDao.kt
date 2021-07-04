package com.diksha.employeedata.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.diksha.employeedata.Modal.Employee

@Dao
interface ActorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(actorList: List<Employee?>?)

    @get:Query("SELECT * FROM employee")
    val allActors: LiveData<List<Employee>>

    @Query("DELETE FROM employee")
    fun deleteAll()
}