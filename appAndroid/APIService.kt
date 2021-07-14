package com.proyectoFM.helloqr

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface APIService {
        @GET("/plantas/{Id}")
        suspend fun getPlantas(@Path(value = "Id", ) plantaId: Int): Response<List<MiniPlantaResponse>>
    }
