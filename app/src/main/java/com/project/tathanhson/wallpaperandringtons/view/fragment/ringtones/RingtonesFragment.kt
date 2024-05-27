package com.project.tathanhson.wallpaperandringtons.view.fragment.ringtones

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.tathanhson.mediaplayer.model.Ringtones
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.OnMainCallback
import com.project.tathanhson.wallpaperandringtons.R
import com.project.tathanhson.wallpaperandringtons.databinding.FragmentRingtonesBinding
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseFragment
import com.project.tathanhson.wallpaperandringtons.view.adapter.ringtones.CategoryRingtonesAdapter
import com.project.tathanhson.wallpaperandringtons.view.adapter.ringtones.ListRingtonesAdapter
import com.project.tathanhson.wallpaperandringtons.viewmodel.RingtonesVM


class RingtonesFragment :
    BaseFragment<FragmentRingtonesBinding>(FragmentRingtonesBinding::inflate), OnMainCallback {
    lateinit var viewModel: RingtonesVM
    private lateinit var mediaPlayerList: ArrayList<Ringtones>
    private lateinit var adapterTitle : CategoryRingtonesAdapter
    private lateinit var adapterListRingtones: ListRingtonesAdapter
    private var title = ""
    override fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[RingtonesVM::class.java]
        CommonObject.categoryRingtone.observe(viewLifecycleOwner) { title ->
            viewModel.getDataListForCategory(title)
            this.title = title
        }


    }

    override fun initData() {
        mediaPlayerList = viewModel.readJSONToMediaPlayerList(resources, R.raw.ringtone)
    }

    override fun initView() {
        //RecyclerView Category
        CommonObject.listCategorysRingtones.observe(viewLifecycleOwner) { categories ->
            categories?.let {
                viewModel.getDataListForCategory(categories[0])
                binding.rcvTitle.layoutManager =
                    LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                adapterTitle =
                    CategoryRingtonesAdapter(mContext, viewModel, viewLifecycleOwner, categories)
                binding.rcvTitle.adapter = adapterTitle
            }
        }

        //RecyclerView List ringtones
        CommonObject.listDataRingtone.observe(this) { listRingtoneData ->
            listRingtoneData?.let {
                binding.rcvRingtones.layoutManager = LinearLayoutManager(mContext)
                adapterListRingtones = ListRingtonesAdapter(
                    mContext,
                    viewModel,
                    viewLifecycleOwner,
                    listRingtoneData,
                    title
                )
                binding.rcvRingtones.adapter = adapterListRingtones
            }
        }
    }

    override fun onPause() {
        super.onPause()
        adapterListRingtones.stopMediaRingtone()
    }


    override fun showActivity(tag: String?, data: Any?) {

    }
}