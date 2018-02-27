package com.promact.chatapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import android.widget.TextView
import android.widget.Toast
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_user.*
import org.json.JSONArray
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class UserActivity : AppCompatActivity() {
    var AppConstant =    "/api/chat"
    var AppConstant1 = "/api/chat/"
    companion object {
        var currentuserid: String = ""
        var username: String = ""
        var name: String = ""
        var token: String=""
    }

    private var items: java.util.ArrayList<Messages> = ArrayList()
    private lateinit var myAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        token = intent.getStringExtra("token")
        var id = intent.getStringExtra("id")
         name = intent.getStringExtra("name")
         currentuserid = intent.getStringExtra("currentuserid")
         username = intent.getStringExtra("username")

        AppConstant1 += id

        Toast.makeText(this,name,Toast.LENGTH_SHORT).show()
        this.setTitle(name);

        items = ArrayList<Messages>()
        myAdapter = MyAdapter(this@UserActivity,items)
        recyclerView1.adapter = myAdapter
        recyclerView1.layoutManager = LinearLayoutManager(this@UserActivity)

        getMsg()

        sendBtn.setOnClickListener(){

            if(sendMessage.text.toString().trim() != "")
            {
                val rp1 = RequestParams()
                rp1.put("message",sendMessage.text.toString().trim())
                rp1.put("toUserId",id)
                rp1.setUseJsonStreamer(true)

                HttpUtils.post(AppConstant, token,2, rp1, object : JsonHttpResponseHandler() {

                    override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONArray?) {
                        try {
                            // val serverResp = JSONObject(response.toString())
                            Log.d("server Response", "success")
                            sendMessage.setText("")
                            getMsg()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(this@UserActivity,""+e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                        // super.onFailure(statusCode, headers, responseString, throwable)
                        sendMessage.setText("")
                        getMsg()
                    }
                })
            }else
            {
                getMsg()
            }


        }





//        val timerObj = Timer()
//        val timerTaskObj = object : TimerTask() {
//            override fun  run() {
//
//            }
//        }
//        timerObj.schedule(timerTaskObj, 0, 15000)

    }

    fun getMsg() {
        val rp = RequestParams()
        rp.setUseJsonStreamer(true)
        items.clear()
        HttpUtils.get(AppConstant1, token, rp, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONArray?) {
                try {
                    // val serverResp = JSONObject(response.toString())
                    //   Log.d("server Response", )

                    for (i in 0 until response!!.length()) {
                        val json_data = response.getJSONObject(i)
                        val msg = json_data.getString("message")
                        val idfrom = json_data.getInt("fromUserId")
                        val idto = json_data.getInt("toUserId")
                        var dateTime = json_data.getString("createdDateTime")
                        items.add(Messages(msg,idfrom,idto,dateTime))

                        myAdapter.notifyDataSetChanged()
                    }


                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this@UserActivity,""+e.toString(), Toast.LENGTH_SHORT).show();

                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                // super.onFailure(statusCode, headers, responseString, throwable)
                Toast.makeText(this@UserActivity,"Network error try again !!" + statusCode , Toast.LENGTH_SHORT).show();
            }
        })

    }

    class MyAdapter(context: Context, myData: ArrayList<Messages>): RecyclerView.Adapter<MyAdapter.MyHolder>() {

        var li: LayoutInflater? = null
        var data : ArrayList<Messages>? = null
        var v: View? = null
        init {
            li = LayoutInflater.from(context)
            data = myData

        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
            v = li!!.inflate(R.layout.message_row,parent,false)
            var myHolder = MyHolder(v)
            return myHolder
        }
        override fun onBindViewHolder(holder: MyHolder?, position: Int) {
            var users = data!!.get(position)

            if(users.fromUserId.toInt() == currentuserid.toInt())
            {
               holder!!.fromname!!.setText(username)
            }
            else
            {
                holder!!.fromname!!.setText(name)
            }
            holder!!.message!!.setText(users.message)
            holder!!.dateTime!!.setText(users.createdDateTime)

        }

        override fun getItemCount(): Int {
            return data!!.size
        }
        class MyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

            var message: TextView? = null
            var fromname: TextView? = null
            var dateTime: TextView? = null

            init {
                message = itemView!!.findViewById(R.id.message)
                fromname = itemView!!.findViewById(R.id.fromname)
                dateTime = itemView!!.findViewById(R.id.dateTime)

            }

        }


    }
}
