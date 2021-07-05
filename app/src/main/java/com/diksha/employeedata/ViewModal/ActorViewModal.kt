package com.diksha.employeedata.ViewModal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.diksha.employeedata.Modal.Employee
import com.diksha.employeedata.Repository.EmployeeRespository

class ActorViewModal(application: Application) : AndroidViewModel(application) {
    private val actorRespository: EmployeeRespository
    val allActor: LiveData<List<Employee>>
    fun insert(list: List<Employee?>?) {
        actorRespository.insert(list)
    }

    init {
        actorRespository = EmployeeRespository(application)
        allActor = actorRespository.allActors
    }
}