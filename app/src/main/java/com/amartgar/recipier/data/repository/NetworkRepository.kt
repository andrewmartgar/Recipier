package com.amartgar.recipier.data.repository

import androidx.lifecycle.MutableLiveData
import com.amartgar.recipier.data.model.entities.RandomRecipier
import com.amartgar.recipier.data.model.network.RandomRecipeAPIService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class NetworkRepository {

    private val randomRecipeApiService = RandomRecipeAPIService()
    private val compositeDisposable = CompositeDisposable()

    val loadRandomRecipe = MutableLiveData<Boolean>()
    val randomRecipeResponse = MutableLiveData<RandomRecipier.RandomRecipe>()
    val randomRecipeLoadingError = MutableLiveData<Boolean>()

    fun getRandomRecipeFromAPI() {
        loadRandomRecipe.value = true

        compositeDisposable.add(
            randomRecipeApiService.getRandomRecipe()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RandomRecipier.RandomRecipe>() {
                    override fun onSuccess(value: RandomRecipier.RandomRecipe) {
                        loadRandomRecipe.value = false
                        randomRecipeResponse.value = value
                        randomRecipeLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadRandomRecipe.value = false
                        randomRecipeLoadingError.value = true
                        e!!.printStackTrace()
                    }

                })
        )
    }
}