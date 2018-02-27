package com.promact.chatapplication
import com.loopj.android.http.*;
/**
 * Created by kandarp on 2/26/2018.
 */
object HttpUtils {
    private val BASE_URL = "https://chat.promactinfo.com/"

    private val client = AsyncHttpClient()


    operator fun get(url: String,token: String,params: RequestParams, responseHandler: AsyncHttpResponseHandler) {
        client.addHeader("Authorization",token)
       // client.addHeader("Content-Type","application/json")
        client.get(getAbsoluteUrl(url), params, responseHandler)
    }

    fun post(url: String, token: String,header1: Int,params: RequestParams, responseHandler: AsyncHttpResponseHandler) {

        if(header1 == 1)
           // client.addHeader("Content-Type","application/json")
        if(header1 == 2)
        {
            client.addHeader("Authorization",token)
          //  client.addHeader("Content-Type","application/json")
        }

        client.post(getAbsoluteUrl(url), params, responseHandler)
    }

    fun getByUrl(url: String, params: RequestParams, responseHandler: AsyncHttpResponseHandler) {
        client.get(url, params, responseHandler)
    }

    fun postByUrl(url: String, params: RequestParams, responseHandler: AsyncHttpResponseHandler) {
        client.post(url, params, responseHandler)
    }

    private fun getAbsoluteUrl(relativeUrl: String): String {
        return BASE_URL + relativeUrl
    }
}