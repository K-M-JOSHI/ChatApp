package com.promact.chatapplication

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header


class LoginActivity : AppCompatActivity() {

    val AppConstant = "api/user/login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        title = "Login"

        loginBtn.setOnClickListener()
        {
            if(name.text.isBlank())
                Toast.makeText(this,"Name field can't be empty !!",Toast.LENGTH_SHORT).show();
            else if(name.text.length != name.text.trim().length)
                Toast.makeText(this,"Name shold not be containing spaces !!",Toast.LENGTH_SHORT).show();
            else{

                val rp = RequestParams()
                rp.put("name", name.text.toString())
                rp.setUseJsonStreamer(true)

                HttpUtils.post(AppConstant, "",1,rp, object : JsonHttpResponseHandler() {
                    override fun onSuccess(statusCode: Int, headers: Array<Header>, response: JSONObject) {

                        try {
                            val serverResp = JSONObject(response.toString())
                           // Toast.makeText(this@LoginActivity,serverResp["token"].toString(),Toast.LENGTH_SHORT).show();

                            startActivity( Intent(this@LoginActivity,MainActivity::class.java).putExtra("id",serverResp["id"].toString()).putExtra("name",serverResp["name"].toString()).putExtra("token",serverResp["token"].toString()));

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }

                    override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                       // super.onFailure(statusCode, headers, responseString, throwable)
                        Toast.makeText(this@LoginActivity,"Network error try again !!" + statusCode,Toast.LENGTH_SHORT).show();
                    }
                })
            }

        }

    }
}