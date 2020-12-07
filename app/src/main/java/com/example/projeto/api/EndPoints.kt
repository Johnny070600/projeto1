package com.example.projeto.api

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

  @GET("/myslim/api/problemas")
  fun getProblemas(): Call<List<problemas>>

  @FormUrlEncoded
  @POST("/myslim/api/User")
  fun postLogin(
          @Field("name") name: String?,
          @Field("password") password: String?): Call<OutputPost>
}