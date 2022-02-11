package com.example.vaccinationlistactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
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
        var vaccineList = gson.fromJson<List<VaccinationInfo>>(jsonString, type)
        Log.d(TAG, "onCreate: \n$vaccineList")


        val vaccineAPI = RetrofitHelper.getInstance().create(Covid19Service::class.java)
        val vaccineCall = vaccineAPI.getVaccinations(10)

        vaccineCall.enqueue(object: Callback<List<VaccinationInfo>> {
            override fun onResponse(
                call: Call<List<VaccinationInfo>>,
                response: Response<List<VaccinationInfo>>
            ) {
                Log.d(TAG, "onResponse: ${response.body()}")
                vaccineList = response.body()
                adapter = VaccineAdapter(vaccineList)
                binding.recyclerViewVaccinationList.adapter = adapter
                binding.recyclerViewVaccinationList.layoutManager = LinearLayoutManager(this@VaccinationListActivity)
                val country1 = vaccineList[0]
                val firstDay = country1.timeline.firstKey()
                val lastDay = country1.timeline.lastKey()
                country1.timeline.get(firstDay)

                country1.timeline.toList().sortedBy {
                    it.second
                }[0]
            }

            override fun onFailure(call: Call<List<VaccinationInfo>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })

        val adapter = VaccineAdapter(vaccineList)
        binding.recyclerViewVaccinationList.adapter = adapter
        binding.recyclerViewVaccinationList.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.sortingmenu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.country -> {
                Toast.makeText(this, "country", Toast.LENGTH_SHORT).show()
                adapter.dataset = adapter.dataset.sortedBy { it.country }
                adapter.notifyDataSetChanged()
                true
            }

            R.id.increase -> {
                Toast.makeText(this, "increase", Toast.LENGTH_SHORT).show()
                adapter.dataset = adapter.dataset.sortedBy {
                    (it.timeline.get(it.timeline.lastKey() ?: 0L))?.minus(
                        (it.timeline.get(it.timeline.firstKey()) ?: 0L)
                    )
                }
                adapter.notifyDataSetChanged()
                true
            }

            R.id.vaccineNumber -> {
                Toast.makeText(this, "VaccineNumber", Toast.LENGTH_SHORT).show()
                adapter.dataset = adapter.dataset.sortedByDescending { it.timeline.get(it.timeline.lastKey()) }
                adapter.notifyDataSetChanged()
                true
            }

            else -> return super.onOptionsItemSelected(item)
        }

    }
}