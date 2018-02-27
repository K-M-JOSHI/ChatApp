package com.promact.chatapplication

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.client.protocol.RequestDefaultHeaders
import cz.msebera.android.httpclient.client.utils.HttpClientUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    val AppConstant = "api/user"
    companion object {
        var token: String = ""
        var currentuserid: String = ""
        var username: String = ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        token = intent.getStringExtra("token")
        currentuserid = intent.getStringExtra("id")
        username = intent.getStringExtra("name")

        val items = ArrayList<Users>()
        val rp = RequestParams()

        HttpUtils.get(AppConstant, token, rp, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONArray?) {
                try {
                   // val serverResp = JSONObject(response.toString())
                 //   Log.d("server Response", )

                    for (i in 0 until response!!.length()) {
                        val json_data = response!!.getJSONObject(i)
                        val id = json_data.getInt("id")
                        val name = json_data.getString("name")
                        items.add(Users(id,name.toString()))
                        Log.d("hello", items[i].name)
                    }
                    var myAdapter: MyAdapter = MyAdapter(this@MainActivity,items)
                    recyclerView.adapter = myAdapter
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity,""+e.toString(), Toast.LENGTH_SHORT).show();

                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                // super.onFailure(statusCode, headers, responseString, throwable)
                Toast.makeText(this@MainActivity,"Network error try again !!" + statusCode , Toast.LENGTH_SHORT).show();
            }
        })




    }

    class MyAdapter(context: Context,myData: ArrayList<Users>): RecyclerView.Adapter<MyAdapter.MyHolder>() {

        var li: LayoutInflater? = null
        var data : ArrayList<Users>? = null
        var v: View? = null
        init {
            li = LayoutInflater.from(context)
            data = myData

        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
             v = li!!.inflate(R.layout.single_row,parent,false)

             var myHolder = MyHolder(v)
             return myHolder
        }
        override fun onBindViewHolder(holder: MyHolder?, position: Int) {
            var users = data!!.get(position)
            Log.d("name", users.name)
            holder!!.id!!.setText(users.id.toString())
            holder.name!!.setText(users.name)

        }

        override fun getItemCount(): Int {
            return data!!.size
        }
        class MyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

            var id: TextView? = null
            var name: TextView? = null

            init {
                id = itemView!!.findViewById(R.id.id)
                name = itemView!!.findViewById(R.id.name)

                name!!.setOnClickListener()
                {
                    startActivity(itemView.context,Intent(itemView.context,UserActivity::class.java).putExtra("currentuserid", currentuserid).putExtra("token",token).putExtra("username",username!!).putExtra("name",name!!.text.toString()).putExtra("id",id!!.text.toString()), Bundle())
                    Log.d("NameFrom: ",name!!.text.toString())
                }
            }

        }


    }
}
