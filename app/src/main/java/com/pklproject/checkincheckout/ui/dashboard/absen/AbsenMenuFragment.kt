package com.pklproject.checkincheckout.ui.dashboard.absen

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.`interface`.ApiInterface
import com.pklproject.checkincheckout.api.models.LoginModel
import com.pklproject.checkincheckout.databinding.FragmentMenuAbsenBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity
import com.pklproject.checkincheckout.ui.settings.TinyDB
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AbsenMenuFragment : Fragment(R.layout.fragment_menu_absen) {

    private val binding: FragmentMenuAbsenBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tinyDB = TinyDB(requireContext())

        initialisation(tinyDB)

        binding.absenpagi.setOnClickListener {
            goToAbsensi("pagi")
        }

        binding.absensiang.setOnClickListener {
            goToAbsensi("siang")
        }

        binding.absenpulang.setOnClickListener {
            goToAbsensi("pulang")
        }

    }

    private fun initialisation(tinyDB: TinyDB) {
        binding.pilihanAbsen.check(R.id.absen)
        binding.absensi.isVisible = true
        binding.izindialog.isVisible = false

        val api = ApiInterface.createApi()
        val username = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).username
        val password = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).password
        val hariIni = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        cekAbsenHariIni(api, username.toString(), password.toString(), hariIni)

        val keterangan = binding.keterangan.text
        var tipeAbsen:String

        binding.pilihanAbsen.setOnCheckedChangeListener{ _, isChecked ->
            when(isChecked){
                R.id.absen -> {
                    binding.absensi.isVisible = true
                    binding.izindialog.isVisible = false
                    cekAbsenHariIni(api, username.toString(), password.toString(), hariIni)
                }
                R.id.izin -> {
                    binding.izindialog.isVisible = true
                    binding.absensi.isVisible = false
                    tipeAbsen = "izin"
                    binding.kirim.setOnClickListener {
                        kirimAbsen(tipeAbsen, tinyDB, keterangan.toString())
                    }
                }
                R.id.cuti -> {
                    binding.izindialog.isVisible = true
                    binding.absensi.isVisible = false
                    tipeAbsen = "cuti"
                    binding.kirim.setOnClickListener {
                        kirimAbsen(tipeAbsen, tinyDB, keterangan.toString())
                    }
                }
            }
        }
    }

    private fun kirimAbsen (tipeAbsen: String, tinyDB: TinyDB, keterangan:String) {

        var absentype = ""
        if (tipeAbsen == "izin") {
            absentype = "4"
        } else if (tipeAbsen == "cuti") {
            absentype = "5"
        }
        val api = ApiInterface.createApi()
        val username = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).username
        val password = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).password
        val longitude = 1.2093213912
        val latitude = 1923190238129.0

        lifecycleScope.launch {
            val response = api.kirimAbsen(username.toString(), password.toString(), absentype, longitude, latitude, null, keterangan)

            try {
                if (response.isSuccessful){
                    Snackbar.make(binding.rootLayout, "Data berhasil dikirim", Snackbar.LENGTH_SHORT)
                        .setAction("Ok") {}
                        .show()
                } else {
                    Snackbar.make(binding.rootLayout, "Gagal mengirim data, terjadi kesalahan", Snackbar.LENGTH_SHORT)
                        .setAction("Ok") {}
                        .show()
                }
            } catch (e: Exception) {
                Snackbar.make(binding.rootLayout, "Gagal mengambil data, aktifkan internet anda", Snackbar.LENGTH_SHORT)
                    .setAction("Ok") {}
                    .show()
            }
        }
    }

    private fun cekAbsenHariIni(api: ApiInterface, username:String, password:String, hariIni:String) {
        lifecycleScope.launch {
            val response = api.cekAbsenHariIni(username, password, hariIni)
            try {
                if (response.isSuccessful) {
                    val statusAbsen = response.body()!!.absenHariIni
                    if (statusAbsen!!.isEmpty()) {
                        binding.absenpagi.isClickable= true
                        binding.absensiang.isClickable = false
                        binding.absenpulang.isClickable = false
                    }
                    when (statusAbsen[0].tipeAbsen) {
                        "pagi" -> {
                            binding.absenpagi.isClickable = false
                            binding.absensiang.isClickable = true
                            binding.absenpulang.isClickable = false
                        }
                        "siang" -> {
                            binding.absenpagi.isClickable = false
                            binding.absensiang.isClickable = false
                            binding.absenpulang.isClickable = true
                        }
                        "pulang" -> {
                            binding.absenpagi.isClickable = false
                            binding.absensiang.isClickable = false
                            binding.absenpulang.isClickable = false
                        }
                        else -> {
                            binding.absenpagi.isClickable = false
                            binding.absensiang.isClickable = false
                            binding.absenpulang.isClickable = false
                        }
                    }

                } else {
                    Snackbar.make(
                        requireView(),
                        "Gagal mengambil data absen",
                        Snackbar.LENGTH_SHORT).setAction("Ok") {}
                        .show()
                }
            } catch (e: Exception) {
                Snackbar.make(binding.rootLayout, "Gagal mengambil data, aktifkan internet anda", Snackbar.LENGTH_SHORT)
                    .setAction("Ok") {}
                    .show()
            }
        }
    }

    private fun goToAbsensi(tipeAbsen:String) {
        val bundle = bundleOf(AbsenFragment.ABSEN_TYPE to tipeAbsen)
        findNavController().navigate(R.id.action_navigation_absen_to_absenFragment, bundle)
    }
}