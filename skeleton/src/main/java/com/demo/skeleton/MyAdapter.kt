package com.demo.skeleton

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.demo.demolib.utils.ApiUtil
import com.demo.demolib.utils.GlideLoder

class MyAdapter : BaseQuickAdapter<NavObject, BaseViewHolder>(R.layout.item) {
    override fun convert(helper: BaseViewHolder?, item: NavObject) {
//        GlideUtil.displayImage(mContext,ApiUtil.baseUrl+item.ImgUrl,helper!!.getView(R.id.image_item))
        GlideLoder.display(mContext,ApiUtil.BASEURL+item.ImgUrl,helper!!.getView(R.id.image_item))
    }
}