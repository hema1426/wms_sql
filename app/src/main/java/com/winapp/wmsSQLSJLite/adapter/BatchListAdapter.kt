package com.winapp.wmsSQLSJLite.adapter

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
import androidx.recyclerview.widget.RecyclerView
import com.winapp.wmsSQLSJLite.R
import com.winapp.wmsSQLSJLite.model.BatchDetailModule
import com.winapp.wmsSQLSJLite.utils.Utils.hideKeyboard

class BatchListAdapter(
    private val context: Context,
    private var dataList: ArrayList<BatchDetailModule>,
    var removeBatchClickListener: RemoveBatchClickListener,
    var batchQtyClickListener: BatchQtyClickListener,

    ) : RecyclerView.Adapter<BatchListAdapter.ViewHolder>() {
    var isRemove: Boolean = false
    private var batchqtyTextWatcher: TextWatcher? = null
    private var batchNoTextWatcher: TextWatcher? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.batch_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") i: Int) {

        val model = dataList[i]
        viewHolder.batchNotxt.clearFocus()
        viewHolder.batchQtytxt.clearFocus()

        viewHolder.batchNotxt.setText(model.batchNo)
        viewHolder.batchQtytxt.setText(model.batchQty.toString())

        Log.e("remov", ",," + model.isRemove + model.batchQty)

        if (model.isRemove) {
            viewHolder.removeBatchl.visibility = View.GONE
            viewHolder.batchQtytxt.isEnabled = false
            viewHolder.batchNotxt.isEnabled = false
        } else {
            viewHolder.removeBatchl.visibility = View.VISIBLE
            viewHolder.batchQtytxt.isEnabled = true
            viewHolder.batchNotxt.isEnabled = true

            hideKeyboard(context, viewHolder.batchlay!!)
            viewHolder.batchQtytxt.clearFocus()
            viewHolder.batchNotxt.clearFocus()
        }



        batchqtyTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                if (s.toString().isNotEmpty()) {
                    if (viewHolder.batchQtytxt.hasFocus()) {

                        Log.e("pos_batccc", ",," + s.toString())
                        dataList.get(i).batchQty = s.toString()
                        batchQtyClickListener.batchQtySelected(dataList)

                        Log.e("1pos_bat", ",,$i  " + dataList.get(i).batchQty)

                    }
                }
            }
        }

        batchNoTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //  viewHolder.batchNotxt.removeTextChangedListener(batchNoTextWatcher)
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().isNotEmpty()) {
                    if (viewHolder.batchNotxt.hasFocus()) {
                        dataList.get(i).batchNo = s.toString()
                    }
                }

            }
        }


        viewHolder.batchQtytxt.addTextChangedListener(batchqtyTextWatcher)
        viewHolder.batchNotxt.addTextChangedListener(batchNoTextWatcher)


        viewHolder.removeBatchl.setOnClickListener {
//            removeBatchClickListener.removeBatchSelected(i)
            dataList.removeAt(i)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    override fun getItemViewType(position: Int): Int {
         return position;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    public fun getBatchDataList(): ArrayList<BatchDetailModule> {
        return dataList
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val batchNotxt: EditText
        val batchQtytxt: EditText
        val removeBatchl: ImageView
        val batchlay: LinearLayout

        init {
            batchNotxt = view.findViewById(R.id.batchNo_item)
            batchQtytxt = view.findViewById(R.id.batchQty_item)
            removeBatchl = view.findViewById(R.id.removeBatch)
            batchlay = view.findViewById(R.id.batchLaylist)
        }
    }

    fun updateList(arrayList: ArrayList<BatchDetailModule>) {
        dataList = arrayList
        notifyDataSetChanged()
    }

    fun listAdd(isRemovel: Boolean, arrayList: ArrayList<BatchDetailModule>) {
        isRemove = isRemovel
        dataList = arrayList
        notifyDataSetChanged()
    }

    fun listRemove(isRemovel: Boolean) {
        isRemove = isRemovel
        notifyDataSetChanged()
    }

    interface RemoveBatchClickListener {
        fun removeBatchSelected(position: Int?)
    }

    interface BatchQtyClickListener {
        fun batchQtySelected(arrayList: ArrayList<BatchDetailModule>)
    }

    interface EditClickListener {
        fun onEditClick(model: BatchDetailModule)
    }
}