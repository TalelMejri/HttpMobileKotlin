package com.example.httpapi

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("/offre")
    suspend fun getOffres(): Response<MutableList<offre>>

    @GET("/offre/{id}")
    suspend fun getOffreById(@Path("id") id:Int): Response<offre>

    @DELETE("/offre/{id}")
    suspend fun deleteById(@Path("id") id:Int)

    @POST("/offre")
    suspend fun AddOffre(@Body offre: offre)

    @PUT("/offre/{id}")
    suspend fun UpdateOffre(@Path("id") id: Int,@Body offre: offre)
}