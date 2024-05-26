package com.joaquito.dogfinder

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface DogApiClient {
    @GET
    suspend fun getDogListByBreeds(@Url url: String):Response<DogModel>
}