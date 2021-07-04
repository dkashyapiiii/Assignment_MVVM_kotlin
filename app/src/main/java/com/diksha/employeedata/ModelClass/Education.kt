package com.diksha.employeedata.ModelClass

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Education : Serializable {
    @SerializedName("degree")
    var degree: String? = null

    @SerializedName("institution")
    var institution: String? = null
}