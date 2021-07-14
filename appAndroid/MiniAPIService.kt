package com.proyectoFM.helloqr


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
interface Service {
    data class PostRequest(
        val cod_planta: Int,
        var descripcion: String,
        val fecha_recoleccion: String,
        val peso: Double
    )
    data class PostResponse(
        val data: PlantaResponse,
        val json: PostRequest,
        val headers: Map<String, String>,
        val url: String,
    )
    @POST("/addfrutos")
    suspend fun post(@Body request: PostRequest): Response<PostResponse>
}