package com.project.tathanhson.wallpaperandringtons.view.fragment.ringtones

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wallpagerandringtons.view.adapter.wallpapers.TitleWallpaperAdapter
import com.example.wallpagerandringtons.viewmodel.WallpaperVM
import com.example.wallpagerandringtons.viewmodel.utils.CommonObject
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentRingtonesBinding
import com.project.tathanhson.wallpaperandringtons.model.ringtones.Ringtone
import com.project.tathanhson.wallpaperandringtons.view.adapter.ringtones.ListRingtonesAdapter
import com.project.tathanhson.wallpaperandringtons.view.adapter.ringtones.TitleRingtonesAdapter
import com.project.tathanhson.wallpaperandringtons.viewmodel.RingtonesVM
import java.io.IOException


class RingtonesFragment : Fragment() {
    private lateinit var context: Context
    private lateinit var binding: FragmentRingtonesBinding
    lateinit var viewModel: RingtonesVM
    private lateinit var adapterTitle : TitleRingtonesAdapter
    private lateinit var adapterListRingtones: ListRingtonesAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRingtonesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[RingtonesVM::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createData()
        initView()
    }

    private fun createData() {
        viewModel.getFoldersFromAssets(context)
        viewModel.getRingtonesFromFolder(context,"mix" )
    }

    private fun initView() {
        viewModel.ldListFolder.observe(viewLifecycleOwner , Observer { titleFolder ->
            titleFolder?.let {
                adapterTitle = TitleRingtonesAdapter(context, viewModel, viewLifecycleOwner, titleFolder)
                binding.rcvTitle.adapter = adapterTitle
            }
        })

        CommonObject.itemTitleRingtone.observe(viewLifecycleOwner , Observer{ titleSelect->
            run {
                titleSelect?.let {
                    viewModel.getRingtonesFromFolder(context, titleSelect)
                }
            }
        })

        viewModel.ldItemFolder.observe(viewLifecycleOwner, Observer{ listRingtones ->
            binding.rcvRingtones.layoutManager = LinearLayoutManager(context)
            adapterListRingtones = ListRingtonesAdapter(context, viewModel, viewLifecycleOwner, listRingtones)
            binding.rcvRingtones.adapter = adapterListRingtones
        })


    }


}