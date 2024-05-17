package com.project.tathanhson.wallpaperandringtons.view.fragment.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentDetailFavoriteBinding


class DetailFavoriteFragment : Fragment() {
    private lateinit var context: Context
    private lateinit var binding: FragmentDetailFavoriteBinding

    companion object {
        val TAG = DetailFavoriteFragment::class.java.name
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {

    }
}