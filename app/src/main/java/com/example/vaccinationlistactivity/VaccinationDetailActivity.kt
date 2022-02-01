package com.example.vaccinationlistactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vaccinationlistactivity.databinding.ActivityVaccinationDetailBinding

class VaccinationDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVaccinationDetailBinding

    companion object {
        val EXTRA_VACCINE = "The vaccine"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccination_detail)
    }
}