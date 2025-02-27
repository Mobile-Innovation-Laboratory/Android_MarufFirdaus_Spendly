package dev.maruffirdaus.spendly.data.repository

import dev.maruffirdaus.spendly.common.model.Deals
import dev.maruffirdaus.spendly.data.remote.ItadApiService
import dev.maruffirdaus.spendly.data.remote.model.ItadResponse
import dev.maruffirdaus.spendly.data.util.toDeals
import dev.maruffirdaus.spendly.domain.repository.DealRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DealRepositoryImpl @Inject constructor(
    private val itadApiService: ItadApiService
) : DealRepository {
    override suspend fun getDealsFromItad(
        country: String,
        offset: Int,
        shops: IntArray
    ): Result<Deals> = suspendCoroutine { continuation ->
        itadApiService.getDeals(country, offset, shops).enqueue(object : Callback<ItadResponse> {
            override fun onResponse(call: Call<ItadResponse>, response: Response<ItadResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        continuation.resume(Result.success(responseBody.toDeals()))
                        return
                    }
                } else {
                    continuation.resume(Result.failure(Exception(response.message())))
                    return
                }
            }

            override fun onFailure(call: Call<ItadResponse>, t: Throwable) {
                continuation.resume(Result.failure(t))
                return
            }
        })
    }
}