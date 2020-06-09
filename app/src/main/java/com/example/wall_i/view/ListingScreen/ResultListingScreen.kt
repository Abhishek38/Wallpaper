package com.example.wall_i.view.ListingScreen

import ImageListResponse
import com.example.wall_i.model.ImageResponse
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.wall_i.data.DataSource
import com.example.wall_i.R
import com.example.wall_i.data.api.ServiceBuilder
import com.example.wall_i.model.ListDataModel
import com.example.wall_i.utils.Constants
import com.example.wall_i.view.PreviewScreen.Preview
import kotlinx.android.synthetic.main.activity_result_listing_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultListingScreen : AppCompatActivity(),onItemClick {
    private lateinit var Ladapter: ListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_listing_screen)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)

        val imageSearched = intent.getStringExtra("image")!!

        val request = ServiceBuilder.invoke()
        val call = request.getAllImages(Constants.apiKey, imageSearched)


        call.enqueue(object : Callback<ImageListResponse> {
            override fun onResponse(
                call: Call<ImageListResponse>,
                response: Response<ImageListResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@ResultListingScreen,
                        " LINK " + response.body()!!.imageList[0].id.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    ListRecyclerView.apply {
                        layoutManager = StaggeredGridLayoutManager(3, 1)
                        var data = DataSource.createDataSet(initListView(response.body()!!.imageList))
                        Ladapter = ListAdapter(data, this@ResultListingScreen)
                        adapter = Ladapter

                    }
                }
            }

            override fun onFailure(call: Call<ImageListResponse>, t: Throwable) {
                Toast.makeText(this@ResultListingScreen, "${t.message}", Toast.LENGTH_LONG).show()
            }

        })


        }
    override fun onclick(data: ListDataModel, I: Int) {
         var intent= Intent(this,Preview::class.java)
         intent.putExtra("image",data.img)
         startActivity(intent)



    }

    fun initListView(imgLst: List<ImageResponse>):ArrayList<ListDataModel>{
        var list=ArrayList<ListDataModel>()
        for (i in imgLst){
            list.add(ListDataModel(i.webFormatURL))
        }
        return list



    }

    }






