package com.pklproject.checkincheckout.ui.dashboard.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.databinding.FragmentHistoryBinding
import com.pklproject.checkincheckout.ui.dashboard.history.item.HistoryItem

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private val binding: FragmentHistoryBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HistoryItem()
        //TODO: Set adapter dari recyclerView di layout fragment_history dengan HistoryItem()

    }

}