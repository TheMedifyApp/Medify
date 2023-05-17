package com.geekymusketeers.medify.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.net.HttpURLConnection


open class BaseViewModel(application: Application) :
    AndroidViewModel(application) {
    /**
     * LiveData to show progress in activity/fragment
     */
    val progressLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    /**
     * Common LiveData to handle error in activity/fragment. If required, you can use your own live data to handle error scenarios also.
     */
//    val errorLiveData: SingleLiveEvent<ApiError> by lazy { SingleLiveEvent() }

    /**
     * LiveData to handle exceptions like no internet connection etc
     */
//    val exceptionLiveData: SingleLiveEvent<Throwable> by lazy { SingleLiveEvent() }

    /**
     *  Method to help processing of API response. If further processing of response is not needed you can pass the LiveData to directly post value
     *  @param call: Network call to be executed
     *  @param updateProgress: Boolean value indicating if you want to track the progress using progressLiveData
     *  @return
     */
//    suspend fun <T> processCoroutine(
//        call: suspend () -> NetworkResponse<T>,
//        updateProgress: Boolean = true
//    ): NetworkResponse<T> {
//        if (updateProgress) progressLiveData.postValue(true)
//        val response = call.invoke()
//        response.onError {
//            if (it.responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
//                getApplication<NagarApp>().showUnAuthorizedAPICallForceLogoutScreen(getApplication(), AnalyticsData.EventName.UNAUTHORIZED_ACCESS)
//            }
//        }.onException {
//            exceptionLiveData.postValue(it)
//        }
//        progressLiveData.postValue(false)
//        return response
//    }

    /**
     * Return User preference data(i.e user profile) being set and used throughout the app.
     * @return [UserPreference]
     */
//    protected fun getUserPreference(): UserPreference =
//        (getApplication() as NagarApp).userSharedPreference

    /**
     * Return App preference being set and used throughout the app.
     * @return [AppPreference]
     */
//    protected fun getAppPreference(): AppPreference =
//        (getApplication() as NagarApp).appSharedPreference
}