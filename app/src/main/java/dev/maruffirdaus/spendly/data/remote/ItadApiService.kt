package dev.maruffirdaus.spendly.data.remote

import dev.maruffirdaus.spendly.data.remote.model.ItadResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItadApiService {
    @GET("/deals/v2")
    fun getDeals(
        @Query("country") country: String,
        @Query("offset") offset: Int,
        @Query("shops") shops: IntArray,
        @Query("key") key: String = API_KEY,
        @Query("limit") limit: Int = 10,
        @Query("sort") sort: String = "-trending"
    ): Call<ItadResponse>

    companion object {
        private const val API_KEY = "fb93673ff9bff5da52edcab0c05fa8b2bdc506fc"
    }
}