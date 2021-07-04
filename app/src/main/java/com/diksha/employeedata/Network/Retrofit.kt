package com.diksha.employeedata.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retrofit {
    var retrofit = Retrofit.Builder()
        .baseUrl(Url.URL_DATA)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var api = retrofit.create(Api::class.java)
}