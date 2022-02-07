package com.example.vaccinationlistactivity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*


@Parcelize
data class VaccinationInfo(
    var country : String,
    var timeline : SortedMap<String, Long>
) : Parcelable
