package com.diksha.employeedata.Modal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "employee", indices = [Index(value = ["first_name"], unique = true)])
class Employee
//    public String toString() {
    (
    @field:ColumnInfo(name = "first_name") var firstname: String?,
    @field:ColumnInfo(name = "last_name") var lastname: String?,
    @field:ColumnInfo(name = "age") var age: String,
    @field:ColumnInfo(name = "gender") var gender: String,
    @field:ColumnInfo(name = "image") var picture: String?,
    @field:ColumnInfo(name = "exp") var exp: String?, //    @Override
    @field:ColumnInfo(name = "org") var org: String?,
    @field:ColumnInfo(name = "role") var role: String?,
    @field:ColumnInfo(name = "degree") var degree: String?,
    @field:ColumnInfo(name = "institute") var institution: String?
) {
    @PrimaryKey(autoGenerate = true)
    var actorId = 0

    //    public Employee(String firstname, String lastname, String image, String age,String exp) {
    //        this.firstname = firstname;
    //        this.lastname = lastname;
    //        this.image = image;
    //        this.age = age;
    //        this.exp = exp;
    //    }
    var isExpanded = false

    override fun toString(): String {
        return "Employee{" +
                "actorId=" + actorId +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", image='" + picture + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", exp='" + exp + '\'' +
                ", expanded=" + isExpanded +
                ", org='" + org + '\'' +
                ", role='" + role + '\'' +
                ", degree='" + degree + '\'' +
                ", institute='" + institution + '\'' +
                '}'
    }
}