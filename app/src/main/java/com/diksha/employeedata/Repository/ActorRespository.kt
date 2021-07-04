package com.diksha.employeedata.Repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.diksha.employeedata.Dao.ActorDao
import com.diksha.employeedata.Database.ActorDatabase
import com.diksha.employeedata.Modal.Employee

class ActorRespository(application: Application?) {
    private val database: ActorDatabase
    val allActors: LiveData<List<Employee>>
    fun insert(actorList: List<Employee?>?) {
        InsertAsynTask(database).execute(actorList)
    }

    internal class InsertAsynTask(actorDatabase: ActorDatabase) :
        AsyncTask<List<Employee?>?, Void?, Void?>() {
        private val actorDao: ActorDao

        init {
            actorDao = actorDatabase.actorDao()
        }

        override fun doInBackground(vararg params: List<Employee?>?): Void? {
            actorDao.insert(params[0])
            return null
        }


    }

    init {
        database = ActorDatabase.getInstance(application)!!
        allActors = database.actorDao().allActors
    }
}