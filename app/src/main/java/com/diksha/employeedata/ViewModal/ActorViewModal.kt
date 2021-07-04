package com.diksha.employeedata.ViewModal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.diksha.employeedata.Modal.Employee
import com.diksha.employeedata.Repository.ActorRespository

class ActorViewModal(application: Application) : AndroidViewModel(application) {
    private val actorRespository: ActorRespository
    val allActor: LiveData<List<Employee>>
    fun insert(list: List<Employee?>?) {
        actorRespository.insert(list)
    }

    init {
        actorRespository = ActorRespository(application)
        allActor = actorRespository.allActors
    }
}