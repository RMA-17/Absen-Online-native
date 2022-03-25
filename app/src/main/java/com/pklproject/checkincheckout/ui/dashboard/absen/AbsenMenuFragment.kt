package com.pklproject.checkincheckout.ui.dashboard.absen

import android.os.Bundle
import android.util.Log
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
            goToAbsensi("1")
        }

        binding.absensiang.setOnClickListener {
            goToAbsensi("2")
        }

        binding.absenpulang.setOnClickListener {
            goToAbsensi("3")
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

        cekAbsenToday(api, username.toString(), password.toString(), hariIni)

        val keterangan = binding.keterangan.text

        binding.pilihanAbsen.setOnCheckedChangeListener{ _, isChecked ->
            when(isChecked){
                R.id.absen -> {
                    binding.absensi.isVisible = true
                    binding.izindialog.isVisible = false
                    cekAbsenToday(api, username.toString(), password.toString(), hariIni)
                }
                R.id.izin -> {
                    binding.izindialog.isVisible = true
                    binding.absensi.isVisible = false
                    binding.kirim.setOnClickListener {
                        kirimAbsen("5", tinyDB, keterangan.toString())
                    }
                }
                R.id.cuti -> {
                    binding.izindialog.isVisible = true
                    binding.absensi.isVisible = false
                    binding.kirim.setOnClickListener {
                        kirimAbsen("4", tinyDB, keterangan.toString())
                    }
                }
            }
        }
    }

    private fun kirimAbsen (tipeAbsen: String, tinyDB: TinyDB, keterangan:String) {

        val api = ApiInterface.createApi()
        val username = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).username
        val password = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).password
        val longitude = 1.2093213912
        val latitude = 1923190238129.0

        lifecycleScope.launch {
            try {
                val response = api.kirimAbsen(username.toString(), password.toString(), tipeAbsen, longitude, latitude, null, keterangan)
                if (response.code == 200) {
                    Snackbar.make(binding.rootLayout, "Sukses mengirim absen ${response.tipeAbsen}", Snackbar.LENGTH_SHORT)
                        .setAction("Ok") {}
                        .show()
                } else {
                    Snackbar.make(binding.rootLayout, "Data gagal dikirim", Snackbar.LENGTH_SHORT)
                        .setAction("Ok") {}
                        .show()
                }
            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
                Snackbar.make(binding.rootLayout, "Gagal mengambil data, aktifkan internet anda", Snackbar.LENGTH_SHORT)
                    .setAction("Ok") {}
                    .show()
            }
        }
    }

    private fun cekAbsenToday(api: ApiInterface, username:String, password:String, hariIni:String) {
        lifecycleScope.launch {
            try {
                val response = api.cekAbsenHariIni(username, password, hariIni)
                val statusAbsen = response.absenHariIni
                when (statusAbsen?.get(0)?.tipeAbsen) {
                    "Data Kosong" -> {
                        binding.absenpagi.isClickable = true
                        binding.absensiang.isClickable = false
                        binding.absenpulang.isClickable = false
                        binding.kirim.isEnabled = true
                    }
                    "pagi" -> {
                        binding.absenpagi.isClickable = false
                        binding.absensiang.isClickable = true
                        binding.absenpulang.isClickable = false
                        binding.kirim.isEnabled = true
                    }
                    "siang" -> {
                        binding.absenpagi.isClickable = false
                        binding.absensiang.isClickable = false
                        binding.absenpulang.isClickable = true
                        binding.kirim.isEnabled = true
                    }
                    "pulang" -> {
                        binding.absenpagi.isClickable = false
                        binding.absensiang.isClickable = false
                        binding.absenpulang.isClickable = false
                        binding.kirim.isEnabled = true
                    }
                    "izin" -> {
                        binding.absenpagi.isClickable = false
                        binding.absensiang.isClickable = false
                        binding.absenpulang.isClickable = false
                        binding.kirim.isEnabled = false
                    }
                    "cuti" -> {
                        binding.absenpagi.isClickable = false
                        binding.absensiang.isClickable = false
                        binding.absenpulang.isClickable = false
                        binding.kirim.isEnabled = false
                    }
                    else -> {
                        binding.absenpagi.isClickable = false
                        binding.absensiang.isClickable = false
                        binding.absenpulang.isClickable = false
                        binding.kirim.isEnabled = false
                    }
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
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