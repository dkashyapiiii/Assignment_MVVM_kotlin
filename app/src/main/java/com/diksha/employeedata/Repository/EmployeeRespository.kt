package com.diksha.employeedata.Repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.diksha.employeedata.Dao.EmployeeDao
import com.diksha.employeedata.Database.EmployeeDatabase
import com.diksha.employeedata.Modal.Employee

class EmployeeRespository(application: Application?) {
    private val database: EmployeeDatabase
    val allActors: LiveData<List<Employee>>
    fun insert(actorList: List<Employee?>?) {
        InsertAsynTask(database).execute(actorList)
    }

    internal class InsertAsynTask(actorDatabase: EmployeeDatabase) :
        AsyncTask<List<Employee?>?, Void?, Void?>() {
        private val actorDao: EmployeeDao

        init {
            actorDao = actorDatabase.actorDao()
        }

        override fun doInBackground(vararg params: List<Employee?>?): Void? {
            actorDao.insert(params[0])
            return null
        }
    }

    init {
        database = EmployeeDatabase.getInstance(application)!!
        allActors = database.actorDao().allActors
    }
}