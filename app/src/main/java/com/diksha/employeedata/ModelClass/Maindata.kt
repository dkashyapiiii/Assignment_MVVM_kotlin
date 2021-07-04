package com.diksha.employeedata.ModelClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

//@Entity(tableName="user")
class Maindata : Serializable {
    //    @PrimaryKey
    //    @NonNull
    @SerializedName("firstname")
    var firstname: String? = null
    var isExpanded = false

    @SerializedName("lastname")
    var lastname: String? = null

    @Expose
    @SerializedName("age")
    var age: String = null.toString()

    @Expose
    @SerializedName("gender")
    var gender: String = null.toString()

    @SerializedName("picture")
    var picture: String? = null

    @SerializedName("job")
    var jobholder: Jobholder? = null

    @SerializedName("education")
    var education: Education? = null
}