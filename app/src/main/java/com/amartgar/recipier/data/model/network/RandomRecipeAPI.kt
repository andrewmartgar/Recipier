package com.amartgar.recipier.data.model.network

import com.amartgar.recipier.data.model.entities.RandomRecipier
import com.amartgar.recipier.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomRecipeAPI {

    @GET(Constants.API_ENDPOINT)

    fun getRecipes(
        @Query(Constants.API_KEY) apiKey: String,
        @Query(Constants.API_LIMIT_LICENSE) limitLicense: Boolean,
        @Query(Constants.API_TAGS) tags: String,
        @Query(Constants.API_NUMBER) number: Int
    ): Single<RandomRecipier.RandomRecipe>
}