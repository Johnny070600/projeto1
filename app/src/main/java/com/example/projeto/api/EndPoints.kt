package com.example.projeto.api

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

  @GET("/myslim/api/problemas")
  fun getProblemas(): Call<List<problemas>>

  @GET("myslim/api/problema/{id}")
  fun deleteProblema(
          @Path("id") id: Int): Call<OutputPost>

  @FormUrlEncoded
  @POST("/myslim/api/User")
  fun postLogin(
          @Field("name") name: String?,
          @Field("password") password: String?): Call<OutputPost>

  @FormUrlEncoded
  @POST("/myslim/api/problemas")
  fun postAddPoint(
          @Field("descr") descr: String?,
          @Field("latitude") latitude: String?,
          @Field("longitude") longitude: String?,
          @Field("user_id") user_id: Int) : Call<OutputPost>
}