package com.diksha.employeedata.ModelClass

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Jobholder : Serializable {
    @SerializedName("role")
    var role: String? = null

    @SerializedName("exp")
    var exp: String? = null

    @SerializedName("organization")
    var organization: String? = null
}