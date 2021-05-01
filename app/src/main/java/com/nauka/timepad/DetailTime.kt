package com.nauka.timepad

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.nauka.timepad.databinding.BottomDetailBinding


class DetailTime : Fragment() {

    private lateinit var binding: BottomDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.bottom_detail,
            container,
            false
        )


        val name = arguments?.getString("name")
        val time = arguments?.getString("time")
        val tag = arguments?.getString("tag")

        binding.detailName.text = name
        binding.detileTime.text = time
        binding.detailTag.text = tag


        return binding.root
    }

}