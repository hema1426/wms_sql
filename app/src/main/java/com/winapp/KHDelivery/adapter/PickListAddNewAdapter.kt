package com.winapp.KHDelivery.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.winapp.KHDelivery.CommonMethods
import com.winapp.KHDelivery.R
import com.winapp.KHDelivery.model.PicklistAddNewModel
import com.winapp.KHDelivery.utils.SharedPreferenceUtil
import com.winapp.KHDelivery.utils.Utils


class PickListAddNewAdapter(
    private val context: Context, var pickListItem: ArrayList<PicklistAddNewModel>,var statusStr:String
) : RecyclerView.Adapter<PickListAddNewAdapter.MyViewHolder>()
{
    private var sharedPreferenceUtil: SharedPreferenceUtil? = null
    lateinit var selectedModel: PicklistAddNewModel
    var istrue: Boolean = false
    var pickStatus: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        sharedPreferenceUtil = SharedPreferenceUtil(context)

        return MyViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.new_picklist_add_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(pickListItem[position])
    }


    override fun getItemCount(): Int {
        return pickListItem.size
    }
    fun getList(): List<PicklistAddNewModel> {
        return pickListItem
    }
    @SuppressLint("ClickableViewAccessibility")
    inner class MyViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var pcodetxt: TextView
        var pnametxt: TextView
        var oqtytxt: TextView
        var stocktxt: TextView
        var qtyTextPick: EditText
//        var deletepick: ImageView
//        var editpick: ImageView
//        var remarkpick: ImageView
        var picklistlay: CardView
        var picklistlayLine: LinearLayout
        var pickQtytxt: TextView
        var textWatcher: TextWatcher? = null

        private var progressBar: ProgressBar? = null

        fun setData(pickItem: PicklistAddNewModel) {
            pnametxt.text = pickItem.productName
            pcodetxt.text = pickItem.productCode
            oqtytxt.text = pickItem.openQty.toString()
            pickQtytxt.text = Utils.twoDecimalPoint(pickItem.pickQty!!.toDouble())

            if(pickItem.qty != null && !pickItem.qty.equals("0.0",true)
                && !pickItem.qty.equals("")) {
                qtyTextPick.setText( Utils.twoDecimalPoint(pickItem.qty!!.toDouble()).toString())
               picklistlay.setBackgroundResource(R.color.lightble1)

            }else{
                qtyTextPick.setText("")
                picklistlay.setBackgroundResource(R.color.white)
            }

            stocktxt.text = pickItem.stock.toString()

            qtyTextPick.removeTextChangedListener(textWatcher)
            qtyTextPick.setSelection(qtyTextPick.getText().length)
            qtyTextPick.setSelectAllOnFocus(true)
            if(statusStr.equals("C",true)){
                qtyTextPick.setEnabled(false)
            }else{
                qtyTextPick.setEnabled(true)
            }

            qtyTextPick.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun afterTextChanged(s: Editable) {
                    val pos = adapterPosition
                    if (pos != -1) {
                        Log.w("editabl_pick", "" + s.toString())
                        Log.w("pick_pos", "" +pos)
                        if (s.toString().isNotEmpty()) {
                            pickListItem.get(pos).qty = s.toString()

                            //notifyDataSetChanged();
//                            }else {
//                                if (dataList.get(pos).getStockInHand() >= Integer.parseInt(s.toString())) {
//                                    dataList.get(pos).setQty(s.toString());
////                                    notifyDataSetChanged();
//                                } else {
//                                    qtytxt.setText("");
//                                    Toast.makeText(context, "Low stock !", Toast.LENGTH_SHORT).show();
//                                }
//                            }
                        } else {
                            pickListItem.get(pos).qty = ""
                        }
                    }
                }
            })

//            if(pickItem.kgQtyStatus != null && pickItem.kgQtyStatus > 0) {
//                no_catron_pick.setText(Utils.twoDecimalPoint(pickItem.kgQtyStatus))
//            }
//            if (pickItem.pcsQty > 0) {
//                no_catron_pick.setText(Utils.twoDecimalPoint(pickItem.pcsQty))
//            }else{
//                if (pickItem.cartonQty > 0) {
//                    no_catron_pick.setText(Utils.twoDecimalPoint(pickItem.cartonQty))
//                }else{
//                    if (pickItem.palletQty.isNotEmpty() && pickItem.palletQty.toDouble() > 0) {
//                        no_catron_pick.setText(Utils.twoDecimalPoint(pickItem.palletQty.toDouble()))
//                    }else{
//                        no_catron_pick.setText(Utils.twoDecimalPoint(pickItem.cartonQty))
//                    }
//                }
//            }

//            if(pickItem.remarks != null && pickItem.remarks.isNotEmpty()) {
//                if (pickItem.remarks.equals("OW", ignoreCase = true)) {
//                    status_txt.setText("Open Weight")
//                    picklistlay.setBackgroundResource(R.color.lightble1)
//                }else if(pickItem.remarks.equals("OC", ignoreCase = true)){
//                    status_txt.setText("Open Check")
//                    picklistlay.setBackgroundResource(R.color.white)
//                }
//                else if(pickItem.remarks.equals("O", ignoreCase = true)){
//                    status_txt.setText("Open")
//                    picklistlay.setBackgroundResource(R.color.white)
//                }
//                else if(pickItem.remarks.equals("C", ignoreCase = true)){
//                    status_txt.setText("Close")
//                    picklistlay.setBackgroundResource(R.color.white)
//                }
//            }
//            else{
//                status_txt.setText("Open")
//            }

//            if(pickListItem.size > 0){
//                pickListItem.get(0).isManageBatch = "Yes"
//            }
            

            Log.w("pickkklqtyyy  ",""+pickItem.pickQty.toString())

            
            if (pickItem.stock!!.toDouble() < 0.0) {
                stocktxt.setTextColor(ContextCompat.getColor(context, R.color.red_btn_bg_color))
//                checkboxpick.visibility = View.GONE
                pickQtytxt.isEnabled = false
            }
            if (::selectedModel.isInitialized
                && selectedModel.productCode == pickItem.productCode
//                && selectedModel.location == pickItem.location
                && istrue) {
                Log.e("pickadd_entry","")
                val pos = adapterPosition

                qtyTextPick.requestFocus()

             //   qtyTextPick.setSelection(pos)

                object : CountDownTimer(1500, 500) {
                    override fun onTick(millisUntilFinished: Long) {
                        CommonMethods.setBlinkingLay(picklistlayLine)
//                        CommonMethods.setBlinkingText(pickQtytxt)
//                        CommonMethods.setBlinkingText(qtyTextPick)
                    }

                    override fun onFinish() {
                        picklistlayLine.clearAnimation()
//                        pickQtytxt.clearAnimation()
//                        qtyTextPick.clearAnimation()
                    }
                }.start()
            } else {
                Log.e("graadd_enss","")
                pickQtytxt.clearAnimation()
            }
             }

        init {
            pcodetxt = itemView.findViewById<View>(R.id.pdtcode_picklis_item) as TextView
            pnametxt = itemView.findViewById<View>(R.id.pdtname_picklis_item) as TextView
            oqtytxt = itemView.findViewById<View>(R.id.open_picklis_item) as TextView
            pickQtytxt = itemView.findViewById<View>(R.id.pick_picklis_item) as TextView
           
            stocktxt = itemView.findViewById<View>(R.id.stock_picklis_item) as TextView
            qtyTextPick = itemView.findViewById<View>(R.id.qty_picklis_item) as EditText
         
//            checkboxpick = itemView.findViewById<View>(R.id.checkbox_pick) as CheckBox
            picklistlay = itemView.findViewById<View>(R.id.picklist_add_lay) as CardView
            picklistlayLine = itemView.findViewById<View>(R.id.picklist_add_lay1) as LinearLayout
//            editpick = itemView.findViewById<View>(R.id.edit_pick) as ImageView
//            remarkpick = itemView.findViewById<View>(R.id.remark_pick) as ImageView
//            baltxt = itemView.findViewById<View>(R.id.pbal_item) as TextView
//            deletepick = itemView.findViewById<View>(R.id.delete_pick) as ImageView
            this.progressBar = itemView.findViewById(R.id.progress)
            

//            editpick.setOnClickListener {
//                if (pickListDetailClickListener != null) {
//                    val pos = adapterPosition
//                    if (pos != -1) {
////                        Log.w("pickedtuom", "" + pickListItem[pos].uoMCode)
//
//                        //card_bg.setBackgroundResource(R.color.colorPrimaryLight);
//                        // packageCategoryClickListener.packageCategorySelected(dataList.get(pos));
//
////                            for (int i = 0; i < dataList.size(); i++) {
////                                ResponseDataPickListDetailItem respItem = dataList.get(i);
////                                respItem.setItemSelected(i == pos);
////                                dataList.set(i, respItem);
////                                notifyItemChanged(i);
////                            }
//                        if (pickListItem[pos].stockInHand > pickListItem[pos].pickedQuantity) {
//                            pickListDetailClickListener.pickListDetailSelected(pickListItem,pos)
//                        } else {
//                            Toast.makeText(
//                                context,
//                                "No stock this product",
//                                Toast.LENGTH_SHORT
//                            )
//                                .show()
//                        }
//                    }
//                }
//            }

            //todo hide 18.11
//            pickQtytxt.setOnTouchListener(OnTouchListener { v, event ->
//                Log.e("edtouch","")
//             //   picktxt.setSelection(picktxt.getText().length);
//                pickQtytxt.setSelectAllOnFocus(true);
//
//                pickQtytxt.addTextChangedListener(object:TextWatcher{
//                    @SuppressLint("NotifyDataSetChanged")
//                    override fun afterTextChanged(s: Editable?) {
//                        if (pickEditListener != null) {
//                            val pos = adapterPosition
//                            if (pickListItem[pos].quantity.toString() >= s.toString()) {
//
//                            if (pos != -1) {
//                                if (!TextUtils.isEmpty(s)) {
//                                    Log.e("pickedt2", "" + s.toString())
//                                        pickEditListener.pickEditSelected(pos, (s.toString().toInt()))
//
//                                        baltxt.text = pickListItem[pos].balance.toString()
//                                        Log.e(
//                                            "balqtypi..",
//                                            "" + pickListItem[pos].pickedQuantity + "..." + pickListItem[pos].balance
//                                        )
//                                    }
//
//                                }
//                            }
//                            else
//                            {
//                                if (!TextUtils.isEmpty(s)) {
//                                    toast(context, "Check order qty value")
//                                }
//                            }
//                        }
//
//                    }
//
//                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//                    }
//
//                    @SuppressLint("NotifyDataSetChanged")
//                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    }
//                })
//
//                false
//            })

//            checkboxpick.setOnClickListener(View.OnClickListener { v ->
//                val cb = v as CheckBox
//                val pos = adapterPosition
//                if (cb.isChecked) {
//                    if (pos != -1) {
//                        pickListItem[pos].isCheckboxSelect = true
//                    }
//                } else {
//                    pickListItem[pos].isCheckboxSelect = false
//                }
//                pickListCheckboxlClickListener.pickListCheckboxSelected(pos)
//            }
//
//            )
           
        }

//     

    }
    
    interface PickListDetailClickListener {
        fun pickListDetailSelected(pickList:ArrayList<PicklistAddNewModel>  , position: Int?)
    }
    interface PickListRemarkClickListener {
        fun pickListDetailRemark(pickList:ArrayList<PicklistAddNewModel>  , position: Int? ,action: String)
    }


    interface PickListDeletelClickListener {
        fun pickListDeleteSelected(position: Int?)
    }

    interface PickListCheckboxlClickListener {
        fun pickListCheckboxSelected(position: Int?)
    }

    interface PickEditListener {
        fun pickEditSelected(position: Int?, pickqty: Int?)
    }

    interface PickBinLocListener {
        fun pickBinSelected(position: Int?)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateQty(pdtName: PicklistAddNewModel, istrueVal: Boolean) {
        selectedModel = pdtName
        istrue = istrueVal
        notifyDataSetChanged()
    }
    fun updateStatus(status : String) {
        pickStatus = status
        notifyDataSetChanged()
    }
    fun updateList(list: ArrayList<PicklistAddNewModel>) {
        pickListItem = list
        notifyDataSetChanged()
    }
    interface DynamicKgClickListener {
        fun batchKgSelected(item: PicklistAddNewModel, position: Int?)
    }

}