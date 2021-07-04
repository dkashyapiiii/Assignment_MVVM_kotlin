package com.diksha.employeedata.ModelClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class EmployeeModel : Serializable {
    @SerializedName("data")
    @Expose
    var banner1: List<Maindata>? = null
    override fun toString(): String {
        return "EmployeeModel{" +
                ", data=" + banner1 +
                '}'
    }
}