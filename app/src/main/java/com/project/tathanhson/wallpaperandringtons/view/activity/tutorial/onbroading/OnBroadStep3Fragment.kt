package com.project.tathanhson.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentOnBroadStep3Binding

class OnBroadStep3Fragment : Fragment() {
    private lateinit var binding: FragmentOnBroadStep3Binding
    private lateinit var context: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBroadStep3Binding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun getInstance(): OnBroadStep3Fragment {
            return OnBroadStep3Fragment()
        }
    }
}