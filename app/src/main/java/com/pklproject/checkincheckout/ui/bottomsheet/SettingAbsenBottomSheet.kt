package com.pklproject.checkincheckout.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import com.pklproject.checkincheckout.MainActivity
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.`interface`.ApiInterface
import com.pklproject.checkincheckout.api.models.Setting
import com.pklproject.checkincheckout.databinding.BottomsheetJadwalAbsenBinding
import com.pklproject.checkincheckout.ui.settings.SettingsFragment.Companion.KEYKIRIMWAKTU
import com.pklproject.checkincheckout.ui.settings.TinyDB
import kotlinx.coroutines.launch

class SettingAbsenBottomSheet : BottomSheetDialogFragment(){

    private val binding :BottomsheetJadwalAbsenBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_jadwal_absen, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val waktu = arguments?.getString(KEYKIRIMWAKTU) ?:""
        val tinyDb = TinyDB(requireContext())

        val listAbsenSettings = tinyDb.getObject(MainActivity.PENGATURANABSENKEY, Setting::class.java)

        binding.pagi.text = "Menampilkan waktu absen untuk: $waktu"

        when (waktu) {
            "Pagi" -> {
                binding.jamMulai.text = listAbsenSettings?.absenPagiAwal
                binding.jamSampai.text = listAbsenSettings?.absenPagiAkhir
                binding.jamMulai.setOnClickListener {
                    val timePicker = MaterialTimePicker.Builder()
                        .setTimeFormat(CLOCK_24H)
                        .setInputMode(INPUT_MODE_CLOCK)
                        .setHour(listAbsenSettings?.absenPagiAwal?.substring(0,2)?.toInt()?:0)
                        .setMinute(listAbsenSettings?.absenPagiAwal?.substring(3,5)?.toInt()?:0)
                        .setTitleText("Atur jam awal absen pagi")
                        .build()
                    timePicker.addOnCancelListener { dismiss() }
                    timePicker.addOnPositiveButtonClickListener {
                        val jam = if (timePicker.hour < 10) "0${timePicker.hour}" else "${timePicker.hour}"
                        val menit = if (timePicker.minute < 10) "0${timePicker.minute}" else "${timePicker.minute}"
                        binding.jamMulai.text = "$jam:$menit:00"
                    }
                    timePicker.show(childFragmentManager, "jamPagiMulai")
                }
                binding.jamSampai.setOnClickListener {
                    val timePicker = MaterialTimePicker.Builder()
                        .setTimeFormat(CLOCK_24H)
                        .setInputMode(INPUT_MODE_CLOCK)
                        .setHour(listAbsenSettings?.absenPagiAkhir?.substring(0,2)?.toInt()?:0)
                        .setMinute(listAbsenSettings?.absenPagiAkhir?.substring(3,5)?.toInt()?:0)
                        .setTitleText("Atur jam akhir absen pagi")
                        .build()
                    timePicker.addOnCancelListener { dismiss() }
                    timePicker.addOnPositiveButtonClickListener {
                        val jam = if (timePicker.hour < 10) "0${timePicker.hour}" else "${timePicker.hour}"
                        val menit = if (timePicker.minute < 10) "0${timePicker.minute}" else "${timePicker.minute}"
                        binding.jamSampai.text = "$jam:$menit:00"
                    }
                    timePicker.show(childFragmentManager, "jamPagiSampai")
                }
                binding.simpan.setOnClickListener {
                    val jamMulai = binding.jamMulai.text
                    val jamSampai = binding.jamSampai.text
                    ubahPengaturanAbsen("1", jamMulai.toString(), jamSampai.toString(), tinyDb)
                    dismiss()
                }
            }
            "Siang" -> {
                binding.jamMulai.text = listAbsenSettings?.absenSiangAwal
                binding.jamSampai.text = listAbsenSettings?.absenSiangAkhir
                binding.jamMulai.setOnClickListener {
                    val timePicker = MaterialTimePicker.Builder()
                        .setTimeFormat(CLOCK_24H)
                        .setInputMode(INPUT_MODE_CLOCK)
                        .setHour(listAbsenSettings?.absenSiangAwal?.substring(0,2)?.toInt()?:0)
                        .setMinute(listAbsenSettings?.absenSiangAwal?.substring(3,5)?.toInt()?:0)
                        .setTitleText("Atur jam awal absen siang")
                        .build()
                    timePicker.addOnCancelListener { dismiss() }
                    timePicker.addOnPositiveButtonClickListener {
                        val jam = if (timePicker.hour < 10) "0${timePicker.hour}" else "${timePicker.hour}"
                        val menit = if (timePicker.minute < 10) "0${timePicker.minute}" else "${timePicker.minute}"
                        binding.jamMulai.text = "$jam:$menit:00"
                    }
                    timePicker.show(childFragmentManager, "jamSiangMulai")
                }
                binding.jamSampai.setOnClickListener {
                    val timePicker = MaterialTimePicker.Builder()
                        .setTimeFormat(CLOCK_24H)
                        .setInputMode(INPUT_MODE_CLOCK)
                        .setHour(listAbsenSettings?.absenSiangAkhir?.substring(0,2)?.toInt()?:0)
                        .setMinute(listAbsenSettings?.absenSiangAkhir?.substring(3,5)?.toInt()?:0)
                        .setTitleText("Atur jam akhir absen siang")
                        .build()
                    timePicker.addOnCancelListener { dismiss() }
                    timePicker.addOnPositiveButtonClickListener {
                        val jam = if (timePicker.hour < 10) "0${timePicker.hour}" else "${timePicker.hour}"
                        val menit = if (timePicker.minute < 10) "0${timePicker.minute}" else "${timePicker.minute}"
                        binding.jamSampai.text = "$jam:$menit:00"
                    }
                    timePicker.show(childFragmentManager, "jamSiangSampai")
                }
                binding.simpan.setOnClickListener {
                    val jamMulai = binding.jamMulai.text
                    val jamSampai = binding.jamSampai.text
                    ubahPengaturanAbsen("2", jamMulai.toString(), jamSampai.toString(), tinyDb)
                    dismiss()
                }
            }
            "Pulang" -> {
                binding.jamMulai.text = listAbsenSettings?.absenPulangAwal
                binding.jamSampai.text = listAbsenSettings?.absenPulangAkhir
                binding.jamMulai.setOnClickListener {
                    val timePicker = MaterialTimePicker.Builder()
                        .setTimeFormat(CLOCK_24H)
                        .setInputMode(INPUT_MODE_CLOCK)
                        .setHour(listAbsenSettings?.absenPulangAwal?.substring(0,2)?.toInt()?:0)
                        .setMinute(listAbsenSettings?.absenPulangAwal?.substring(3,5)?.toInt()?:0)
                        .setTitleText("Atur jam awal absen pulang")
                        .build()
                    timePicker.addOnCancelListener { dismiss() }
                    timePicker.addOnPositiveButtonClickListener {
                        val jam = if (timePicker.hour < 10) "0${timePicker.hour}" else "${timePicker.hour}"
                        val menit = if (timePicker.minute < 10) "0${timePicker.minute}" else "${timePicker.minute}"
                        binding.jamMulai.text = "$jam:$menit:00"
                    }
                    timePicker.show(childFragmentManager, "jamPulangMulai")
                }
                binding.jamSampai.setOnClickListener {
                    val timePicker = MaterialTimePicker.Builder()
                        .setTimeFormat(CLOCK_24H)
                        .setInputMode(INPUT_MODE_CLOCK)
                        .setHour(listAbsenSettings?.absenPulangAkhir?.substring(0,2)?.toInt()?:0)
                        .setMinute(listAbsenSettings?.absenPulangAkhir?.substring(3,5)?.toInt()?:0)
                        .setTitleText("Atur jam akhir absen pulang")
                        .build()
                    timePicker.addOnCancelListener { dismiss() }
                    timePicker.addOnPositiveButtonClickListener {
                        val jam = if (timePicker.hour < 10) "0${timePicker.hour}" else "${timePicker.hour}"
                        val menit = if (timePicker.minute < 10) "0${timePicker.minute}" else "${timePicker.minute}"
                        binding.jamSampai.text = "$jam:$menit:00"
                    }
                    timePicker.show(childFragmentManager, "jamPulangSampai")
                }
                binding.simpan.setOnClickListener {
                    val jamMulai = binding.jamMulai.text
                    val jamSampai = binding.jamSampai.text
                    ubahPengaturanAbsen("3", jamMulai.toString(), jamSampai.toString(), tinyDb)
                    dismiss()
                }
            }
        }
    }

    private fun ubahPengaturanAbsen(tipeAbsen:String, absenAwal:String, absenAkhir:String, tinyDB: TinyDB) {
        val api = ApiInterface.createApi()
        lifecycleScope.launch {
            try {
                val response = api.updateSettingsAbsen(tipeAbsen, absenAwal, absenAkhir)
                if (response.code == 200) {
                    Snackbar.make(
                        requireActivity().findViewById(R.id.container),
                        "Berhasil mengubah pengaturan absen!",
                        Snackbar.LENGTH_SHORT
                    ).setAction("Ok") {
                        MainActivity().retrieveSettingsAbsen(tinyDB)
                        dismiss()
                    }.show()
                } else {
                    Snackbar.make(
                        requireActivity().findViewById(R.id.container),
                        "Gagal mengubah pengaturan absen",
                        Snackbar.LENGTH_SHORT
                    ).setAction("Ok") {
                        MainActivity().retrieveSettingsAbsen(tinyDB)
                        dismiss()
                    }.show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Snackbar.make(requireActivity().findViewById(R.id.container), "Gagal mengubah pengaturan absen, periksa kembali internet anda", Snackbar.LENGTH_SHORT)
                    .setAction("Ok") {
                        MainActivity().retrieveSettingsAbsen(tinyDB)
                        dismiss()
                    }.show()
            }
        }

    }

}