package com.pklproject.checkincheckout.api.models


import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("business_unit")
    val businessUnit: String,
    @SerializedName("departement")
    val departement: String,
    @SerializedName("id_karyawan")
    val idKaryawan: String,
    @SerializedName("jabatan")
    val jabatan: String,
    @SerializedName("nama_karyawan")
    val namaKaryawan: String,
    @SerializedName("status_admin")
    val statusAdmin: String,
    @SerializedName("status_karyawan")
    val statusKaryawan: String
)