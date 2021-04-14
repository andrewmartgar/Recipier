package com.amartgar.recipier.data.model.network

import com.amartgar.recipier.data.model.entities.RandomRecipier
import com.amartgar.recipier.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RandomRecipeAPIService {

    private val api = Retrofit.Builder().baseUrl(Constants.API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(RandomRecipeAPI::class.java)

    fun getRandomRecipe(): Single<RandomRecipier.RandomRecipe> {
        return api.getRecipes(
            Constants.API_KEY_VALUE, Constants.API_LIMIT_LICENSE_VALUE,
            Constants.API_TAGS_VALUE, Constants.API_NUMBER_VALUE
        )
    }
}