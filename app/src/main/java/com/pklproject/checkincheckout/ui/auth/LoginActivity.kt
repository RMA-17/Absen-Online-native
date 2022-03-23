package com.pklproject.checkincheckout.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.pklproject.checkincheckout.MainActivity
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.`interface`.ApiInterface
import com.pklproject.checkincheckout.api.models.LoginModel
import com.pklproject.checkincheckout.databinding.ActivityLoginBinding
import com.pklproject.checkincheckout.ui.settings.Preferences
import com.pklproject.checkincheckout.ui.settings.TinyDB
import kotlinx.coroutines.launch
import okio.Timeout
import java.util.concurrent.TimeoutException

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val api = ApiInterface.createApi()

        binding.masuk.setOnClickListener {
            val username = binding.username.editText?.text
            val password = binding.password.editText?.text
            print("$username, $password")
            if (binding.username.editText?.text.toString() == "") {
                binding.username.error = "Username tidak boleh kosong"
            } else if (binding.password.editText?.text.toString() == ""){
                binding.password.error = "Password tidak boleh kosong"
            } else {
                binding.password.error = null
                binding.username.error = null
                lifecycleScope.launch {
                    val response = api.login(username.toString(), password.toString())
                    try {
                        if (response.isSuccessful) {
                            val code = response.body()!!.code
                            if (code == 200) {
                                loginKeDashBoard(response.body()!!)
                            } else if (code == 404) {
                                Snackbar.make(
                                    binding.rootLayout,
                                    "Password atau Username salah, coba lagi",
                                    Snackbar.LENGTH_SHORT
                                )
                                    .setAction("Ok") {}
                                    .show()
                            }
                        } else {
                            Snackbar.make(
                                binding.rootLayout,
                                "Gagal mengambil data",
                                Snackbar.LENGTH_SHORT
                            )
                                .setAction("Ok") {}
                                .show()
                        }
                    } catch (e: TimeoutException) {
                        Snackbar.make(
                            binding.rootLayout,
                            "Server sedang tidak aktif atau internet anda bermasalah",
                            Snackbar.LENGTH_SHORT
                        )
                            .setAction("Ok") {}
                            .show()
                    }
                }
            }
        }
    }

    private fun loginKeDashBoard(response: LoginModel) {
        Snackbar.make(binding.rootLayout, "Login Berhasil!", Snackbar.LENGTH_SHORT)
            .setAction("Ok") {}
            .show()

        TinyDB(this@LoginActivity).putObject(
            KEYSIGNIN,
            LoginModel(
                response.businessUnit,
                response.code,
                response.departement,
                response.idKaryawan,
                response.jabatan,
                response.message,
                response.namaKaryawan,
                response.status,
                response.statusAdmin,
                response.statusKaryawan,
            )
        )
        Preferences(this@LoginActivity).isLoggedIn = true
        Preferences(this@LoginActivity).employeeName = response.namaKaryawan
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    companion object {
        const val KEYSIGNIN = "SIGNINKEY"
    }
}

