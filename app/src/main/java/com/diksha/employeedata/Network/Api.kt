package com.diksha.employeedata.Network

import com.diksha.employeedata.ModelClass.EmployeeModel
import retrofit2.Call
import retrofit2.http.GET

interface Api {

    @get:GET("getAllDetails")
    val allActors: Call<EmployeeModel?>?
}