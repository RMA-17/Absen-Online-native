package com.pklproject.checkincheckout.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.models.LoginModel
import com.pklproject.checkincheckout.databinding.FragmentProfileBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity
import com.pklproject.checkincheckout.ui.settings.TinyDB

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tinyDB = TinyDB(requireContext())

        binding.name.text = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).namaKaryawan
        binding.jabatan.text = tinyDB.getObject(LoginActivity.KEYSIGNIN,LoginModel::class.java).jabatan
        binding.departemen.text = tinyDB.getObject(LoginActivity.KEYSIGNIN,LoginModel::class.java).departement
        binding.unit.text = tinyDB.getObject(LoginActivity.KEYSIGNIN,LoginModel::class.java).businessUnit
    }
}