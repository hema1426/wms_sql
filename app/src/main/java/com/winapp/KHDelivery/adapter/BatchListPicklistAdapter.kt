package com.winapp.KHDelivery.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.winapp.KHDelivery.R
import com.winapp.KHDelivery.model.newPickDetail.BatchDetailPickModule
import com.winapp.KHDelivery.model.newPickDetail.NewSalesOrderDetailItem
import com.winapp.KHDelivery.utils.Utils

class BatchListPicklistAdapter(
    private val context: Context,
    private val dataList: ArrayList<BatchDetailPickModule>,
    private var salesOrderDetailItem: NewSalesOrderDetailItem,
    var removeBatchClickListener: RemoveBatchClickListener,
    var totalClickListener: TotalClickListener,

    ) : RecyclerView.Adapter<BatchListPicklistAdapter.ViewHolder>() {
    var isRemove: Boolean = false
    private var batchqtyTextWatcher: TextWatcher? = null
    private var batchNoTextWatcher: TextWatcher? = null
    var selectedModel: String = ""
    var istrue: Boolean = false
    var selectedPos: Int = -1

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.batch_pick_item, viewGroup, false)
        return ViewHolder(view)
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") i: Int) {

        var model = dataList[i]

        viewHolder.batchNotxt.setText(model.batchNo)
        if (model.batchQty != null && model.batchQty!! > 0.0) {
            viewHolder.batchKGQtytxt.setText(Utils.twoDecimalPoint(model.batchQty!!).toString())
        } else {
            viewHolder.batchKGQtytxt.setText("")
        }
        viewHolder.avaiableQty.setText(
            Utils.twoDecimalPoint(salesOrderDetailItem!!.stockInHand.toDouble()).toString()
        )

        Log.w("selemodll", "" + " .. $selectedModel.. ")

        //todo 8.11.24
//        if (selectedPos == i) {
//            // if (selectedModel == model.batchNo
//            //  if(model.selectKgQty!!.equals("")) {
//            // viewHolder.SelectKGQtytxt.setSelection(i);
//            viewHolder.batchKGQtytxt.setFocusable(true)
//            viewHolder.batchKGQtytxt.requestFocus()
//            Log.w("selemodll11", ",," + model.selectKgQty!! + "  " + i)
//
//            object : CountDownTimer(1000, 500) {
//                override fun onTick(millisUntilFinished: Long) {
//                    CommonMethods.setBlinkingText(viewHolder.batchKGQtytxt)
//                    CommonMethods.setBlinkingText(viewHolder.avaiableQty)
//                    viewHolder.batchKGQtytxt.setBackgroundResource(R.color.colorPrimaryLight)
//                    viewHolder.avaiableQty.setBackgroundResource(R.color.colorPrimaryLight)
//
//                }
//
//                override fun onFinish() {
//                    viewHolder.batchKGQtytxt.clearAnimation()
//                    viewHolder.avaiableQty.clearAnimation()
//                    viewHolder.batchKGQtytxt.setFocusable(true)
//                    viewHolder.batchKGQtytxt.requestFocus()
//
//                    viewHolder.batchKGQtytxt.setBackgroundResource(R.color.white)
//                    viewHolder.avaiableQty.setBackgroundResource(R.color.white)
//                }
//            }.start()
        //  }
//        } else {
//            viewHolder.batchNotxt.clearAnimation()
//            viewHolder.avaiableQty.clearAnimation()
//            viewHolder.batchKGQtytxt.clearFocus()
//            viewHolder.batchNotxt.setBackgroundResource(R.color.white)
//            viewHolder.avaiableQty.setBackgroundResource(R.color.white)
//        }


        viewHolder.batchKGQtytxt.removeTextChangedListener(batchqtyTextWatcher)

        batchqtyTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                    if (s.toString().isNotEmpty()) {

                        if (s.toString() != "" && s.toString() != "." && s.toString().toDouble() > 0) {
                            if (salesOrderDetailItem!!.stockInHand >= s.toString().toDouble()) {
                                model.batchQty = s.toString().toDouble()
                                Log.e("cg_11", ",," + model.batchQty)
                                Log.e("cg_22", ",," + s.toString())
                            } else {
                                model.batchQty = 0.0
                                viewHolder.batchKGQtytxt.setText("")
                            }
                        } else {
                            model.batchQty = 0.0
                        }
                        //                        removeBatchClickListener.updateTotalQty("")
                        totalClickListener.totalSelected(dataList)

                    }else {
                        model.batchQty = 0.0
                        totalClickListener.totalSelected(dataList)
//                        removeBatchClickListener.updateTotalQty("")
                    }
//                } catch (e: Exception) {
//
//                }
            }
        }
//        if (viewHolder.SelectKGQtytxt.hasFocus()) {
        viewHolder.batchKGQtytxt.addTextChangedListener(batchqtyTextWatcher)
//        }

        viewHolder.removeBatchl.setOnClickListener {
//            removeBatchClickListener.removeBatchSelected(i)
            istrue = false
            dataList.remove(model)
            notifyDataSetChanged()
            removeBatchClickListener.updateTotalQty("")
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    public fun getBatchDataList(): ArrayList<BatchDetailPickModule> {
        return dataList
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val batchNotxt: TextView
        val batchKGQtytxt: EditText
        val batchlay: LinearLayout
        val avaiableQty: TextView
        val removeBatchl: ImageView

        init {
            batchNotxt = view.findViewById(R.id.batchNo_item)
            batchKGQtytxt = view.findViewById(R.id.batchQty_item)
            batchlay = view.findViewById(R.id.batchLaylist)
            avaiableQty = view.findViewById(R.id.avaiableQty_item)
            removeBatchl = view.findViewById(R.id.removeBatch)

        }
    }

    fun updateList(arrayList: ArrayList<BatchDetailPickModule>) {
//        dataList = arrayList
        notifyItemInserted(dataList.size)
    }

    fun listAdd(isRemovel: Boolean, arrayList: ArrayList<BatchDetailPickModule>) {
        isRemove = isRemovel
//        dataList = arrayList
        notifyDataSetChanged()
    }

    fun listRemove(isRemovel: Boolean) {
        isRemove = isRemovel
        notifyDataSetChanged()
    }

    fun showSelection(index: Int) {
        selectedPos = index
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateQty(pdtName: String, istrueVal: Boolean) {
        selectedModel = pdtName
        istrue = istrueVal
//        salesOrderDetailItem = item
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList() {
        notifyDataSetChanged()
    }

    interface RemoveBatchClickListener {
        fun removeBatchSelected(position: Int?)
        fun updateTotalQty(total: String)
    }

    interface batchNoClickListener {
        fun batchNoSelected(position: Int?)
    }
    interface TotalClickListener {
        fun totalSelected(dataList: ArrayList<BatchDetailPickModule>)
    }

    interface EditClickListener {
        fun onEditClick(model: BatchDetailPickModule)
    }
}