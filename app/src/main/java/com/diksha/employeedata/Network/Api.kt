package com.diksha.employeedata.Network

import com.diksha.employeedata.ModelClass.EmployeeModel
import retrofit2.Call
import retrofit2.http.GET

interface Api {
    //    @GET("data.php")
    //    Call<List<Actor>> getAllActors();
    @get:GET("getAllDetails")
    val allActors: Call<EmployeeModel?>?
}