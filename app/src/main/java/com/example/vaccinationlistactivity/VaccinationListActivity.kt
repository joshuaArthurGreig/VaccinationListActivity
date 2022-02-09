package com.example.vaccinationlistactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vaccinationlistactivity.databinding.ActivityVaccinationListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VaccinationListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityVaccinationListBinding
    lateinit var adapter : VaccineAdapter
    lateinit var vaccineInformation : VaccinationInfo

    companion object {
        val TAG = "VaccinationListActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVaccinationListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val inputStream = resources.openRawResource(R.raw.data)
        val jsonString = inputStream.bufferedReader().use {
            it.readText()
        }
        val gson = Gson()
        val type = object : TypeToken<List<VaccinationInfo>>() { }.type
        val vaccineList = gson.fromJson<List<VaccinationInfo>>(jsonString, type)
        Log.d(TAG, "onCreate: \n$vaccineList")


        val vaccineAPI = RetrofitHelper.getInstance().create(Covid19Service::class.java)
        val vaccineCall = vaccineAPI.getVaccinations(10)

        vaccineCall.enqueue(object: Callback<List<VaccinationInfo>>() {
            override fun onResponse(
                call: Call<List<VaccinationInfo>>,
                response: Response<List<VaccinationInfo>>
            ) {
                Log.d(TAG, "onResponse: ${response.body()}")
            }

            override fun onFailure(call: Call<List<VaccinationInfo>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })

        val adapter = VaccineAdapter(vaccineList)
        binding.recyclerViewVaccinationList.adapter = adapter
        binding.recyclerViewVaccinationList.layoutManager = LinearLayoutManager(this)
    }
}