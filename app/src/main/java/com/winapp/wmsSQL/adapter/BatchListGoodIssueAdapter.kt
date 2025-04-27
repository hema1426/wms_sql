package com.winapp.wmsSQL.adapter

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
import com.winapp.wmsSQL.R
import com.winapp.wmsSQL.model.BatchDetailModule
import com.winapp.wmsSQL.utils.Utils
import com.winapp.wmsSQL.utils.Utils.hideKeyboard

class BatchListGoodIssueAdapter(
    private val context: Context,
    private var dataList: ArrayList<BatchDetailModule>,
    var removeBatchClickListener: RemoveBatchClickListener,
    var batchQtyClickListener: BatchQtyClickListener,

    ) : RecyclerView.Adapter<BatchListGoodIssueAdapter.ViewHolder>() {
    var isRemove: Boolean = false
    private var batchqtyTextWatcher: TextWatcher? = null
    private var batchNoTextWatcher: TextWatcher? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.batch_good_issue_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") i: Int) {

        val model = dataList[i]
        viewHolder.batchNotxt.clearFocus()
        viewHolder.batchQtytxt.clearFocus()

        if(model.batchNo!!.isNotEmpty()) {
            viewHolder.batchNotxt.setText(model.batchNo)
        }
     viewHolder.batchQtytxt.setText(model.batchQty.toString())

        if(model.avlQty!!.isNotEmpty()) {
            viewHolder.avaiableQty.setText(
                model.avlQty!!.toString())
        }
    //   Log.w("avalbb", ",," + model.avlQty + model.batchQty)

//        if (model.isRemove) {
//            viewHolder.removeBatchl.visibility = View.GONE
//            viewHolder.batchQtytxt.isEnabled = false
//            viewHolder.batchNotxt.isEnabled = false
//        } else {
//            viewHolder.removeBatchl.visibility = View.VISIBLE
//            viewHolder.batchQtytxt.isEnabled = true
//            viewHolder.batchNotxt.isEnabled = true
//
//            hideKeyboard(context, viewHolder.batchlay!!)
//            viewHolder.batchQtytxt.clearFocus()
//            viewHolder.batchNotxt.clearFocus()
//        }

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

                    if (s.toString() != "" && s.toString() != "." && s.toString().toInt() > 0) {
                        if (model.avlQty!!.isNotEmpty() && model.avlQty!!.toInt() >= s.toString().toInt()) {
                            model.batchQty = s.toString()

                            Log.w("cg_11", ",," + model.batchQty)
                            Log.w("cg_22", ",," + s.toString())
                        } else {
                            viewHolder.batchQtytxt.setText("")
                            model.batchQty ="0"
//                            viewHolder.batchKGQtytxt.setText("")
                        }
                    } else {
                        model.batchQty ="0"
                    }
                    batchQtyClickListener.batchQtySelected(dataList)
                }else {
                    model.batchQty = "0"

                    batchQtyClickListener.batchQtySelected(dataList)
                    Log.w("cg_11a", ",," + model.batchQty)

                    //                        removeBatchClickListener.updateTotalQty("")
                }
//                } catch (e: Exception) {
//
//                }
            }
        }

//        batchqtyTextWatcher = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//
//                if (s.toString().isNotEmpty()) {
//                    if (viewHolder.batchQtytxt.hasFocus()) {
//
//                        Log.e("pos_batccc", ",," + s.toString())
//                        dataList.get(i).batchQty = s.toString()
//                        batchQtyClickListener.batchQtySelected(dataList)
//
//                        Log.e("1pos_bat", ",,$i  " + dataList.get(i).batchQty)
//
//                    }
//                }
//            }
//        }

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
        val batchNotxt: TextView
        val batchQtytxt: EditText
        val removeBatchl: ImageView
        val batchlay: LinearLayout
        val avaiableQty: TextView

        init {
            batchNotxt = view.findViewById(R.id.batchNo_item)
            batchQtytxt = view.findViewById(R.id.batchQty_item)
            avaiableQty = view.findViewById(R.id.avaiableQty_item)
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