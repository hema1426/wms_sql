package com.winapp.KHDelivery.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.winapp.KHDelivery.model.ItemBinLocation.ResponseDataBinLocItem
import com.winapp.KHDelivery.CommonMethods
import com.winapp.KHDelivery.R
import com.winapp.KHDelivery.model.newPickDetail.NewSalesOrderDetailItem
import com.winapp.KHDelivery.utils.SharedPreferenceUtil
import com.winapp.KHDelivery.utils.Utils


class PickListDetailNewAdapterNew24(
    private val context: Context, var pickListItem: ArrayList<NewSalesOrderDetailItem>,
    private val pickListDetailClickListener: PickListDetailClickListener?,
    private val pickListRemarkClickListener: PickListRemarkClickListener?,
    private val pickListDeletelClickListener: PickListDeletelClickListener,
    private val pickListCheckboxlClickListener: PickListCheckboxlClickListener,
    var dynamicKgClickListener: DynamicKgClickListener,
    private val pickEditListener: PickEditListener?
) : RecyclerView.Adapter<PickListDetailNewAdapterNew24.MyViewHolder>()
{
    private var sharedPreferenceUtil: SharedPreferenceUtil? = null
    private var warehouseCode: String? = null
    var pickListBinAdapter: PickListBinAdapter? = null
    lateinit var selectedModel: NewSalesOrderDetailItem
    lateinit var selectedModelKg: NewSalesOrderDetailItem
    var istrue: Boolean = false
    var isKgQty: Boolean = false
    var pickStatus: String = ""
    private var remarkTextWatcher: TextWatcher? = null

    var pickListBin_Item_List: java.util.ArrayList<ResponseDataBinLocItem> =
        java.util.ArrayList<ResponseDataBinLocItem>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        sharedPreferenceUtil = SharedPreferenceUtil(context)

        return MyViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.picklist_detail_new_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(pickListItem[position])
    }


    override fun getItemCount(): Int {
        return pickListItem.size
    }
    fun getList(): List<NewSalesOrderDetailItem> {
        return pickListItem
    }
    @SuppressLint("ClickableViewAccessibility")
    inner class MyViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var pcodetxt: TextView
        var pnametxt: TextView
        var oqtytxt: TextView
        var baltxt: TextView
        var stocktxt: TextView
        var releas_qty_txtl: TextView
        var qtyTextviewl: TextView
        var deletepick: ImageView
        var editpick: ImageView
        var remarkpick: ImageView
        var picklistlay: CardView
        var imgpick: ImageView
        var imgCardl: CardView
        var pickQtytxt: TextView
        var unit_price_item_pickl: TextView
        var no_catron_pick: TextView
        var no_carton_title_iteml: TextView
        var remarktxt_iteml: TextView
        var total_kg_pick_iteml: TextView
        var picklist_lay: LinearLayout
        var status_txt_lay: LinearLayout
        var remark_itemLayl: LinearLayout
        lateinit var status_txt: TextView
        var bin_image_down: ImageView? = null
        var bin_image_up: ImageView? = null
        var pickListBinAdapter: PickListBinAdapter? = null
        var pick_bin_List: ArrayList<ResponseDataBinLocItem> = ArrayList<ResponseDataBinLocItem>()
        private var pickBin_rv: RecyclerView? = null
        var bin_layl: LinearLayout? = null
        private var progressBar: ProgressBar? = null

        fun setData(pickItem: NewSalesOrderDetailItem) {
            pnametxt.text = pickItem.productName + " - " +pickItem.foreignName
            pcodetxt.text = pickItem.productCode
            oqtytxt.text = pickItem.quantity.toString()
            baltxt.text = pickItem.balance.toString()
            stocktxt.text = pickItem.stockInHand.toString()

            total_kg_pick_iteml.setText(Utils.twoDecimalPoint(pickItem.total))
            unit_price_item_pickl.setText(Utils.twoDecimalPoint(pickItem.price))
            releas_qty_txtl.setText(Utils.twoDecimalPoint(pickItem.releasedQty))

//            if(pickItem.kgQtyStatus != null && pickItem.kgQtyStatus > 0) {
//                no_catron_pick.setText(Utils.twoDecimalPoint(pickItem.kgQtyStatus))
//            }
            no_carton_title_iteml.setText(pickItem.action)
            remarktxt_iteml.setText(pickItem.remarks)

            if (pickItem.pcsQty > 0) {
                no_catron_pick.setText(Utils.twoDecimalPoint(pickItem.pcsQty))
            }else{
                if (pickItem.cartonQty > 0) {
                    no_catron_pick.setText(Utils.twoDecimalPoint(pickItem.cartonQty))
                }else{
                    if (pickItem.palletQty.isNotEmpty() && pickItem.palletQty.toDouble() > 0) {
                        no_catron_pick.setText(Utils.twoDecimalPoint(pickItem.palletQty.toDouble()))
                    }else{
                        no_catron_pick.setText(Utils.twoDecimalPoint(pickItem.cartonQty))
                    }
                }
            }

            if(pickItem.remarks != null && pickItem.remarks.isNotEmpty()) {
                if (pickItem.remarks.equals("OW", ignoreCase = true)) {
                    status_txt.setText("Open Weight")
                    picklistlay.setBackgroundResource(R.color.lightble1)
                }else if(pickItem.remarks.equals("OC", ignoreCase = true)){
                    status_txt.setText("Open Check")
                    picklistlay.setBackgroundResource(R.color.white)
                }
                else if(pickItem.remarks.equals("O", ignoreCase = true)){
                    status_txt.setText("Open")
                    picklistlay.setBackgroundResource(R.color.white)
                }
                else if(pickItem.remarks.equals("C", ignoreCase = true)){
                    status_txt.setText("Close")
                    picklistlay.setBackgroundResource(R.color.white)
                }
            }
            else{
                status_txt.setText("Open")
            }
            Log.w("pickstatt","$pickStatus")
            Log.w("remark_item",""+pickItem.remarks)

//            if(pickListItem.size > 0){
//                pickListItem.get(0).isManageBatch = "Yes"
//            }

//            if((!pickStatus.equals("O"))){
                if(pickItem.frozen.equals("Yes")) {
                    editpick.visibility = View.GONE
                    status_txt_lay.visibility = View.VISIBLE
//                    picklistlay.setBackgroundResource(R.color.lightble1)
                    picklistlay.isEnabled = true
                }
                else{
                    editpick.visibility = View.VISIBLE
                    status_txt_lay.visibility = View.GONE
                  //  picklistlay.setBackgroundResource(R.color.white)
                    picklistlay.isEnabled = false
                }
            //           }
//            else{
//                picklistlay.setBackgroundResource(R.color.white)
//                picklistlay.isEnabled = false
//                Log.e("pickstatt33","")
//
//            }


//            if(pickItem.isManageBatch.equals("Yes")){
//                picklistlay.setBackgroundResource(R.color.lightble)
//                picklistlay.isEnabled = true
//            }
//            else
//            {
//                picklistlay.setBackgroundResource(R.color.white)
//                picklistlay.isEnabled = false
//            }

            Log.w("pickkklqtyyy  ",""+pickItem.pickedQuantity.toString())
            if(pickItem.isKgQty){
                pickQtytxt.setText(Utils.twoDecimalPoint(pickItem.pickedQuantity).toString() + " KG")
            }
           else{
                pickQtytxt.setText(Utils.twoDecimalPoint(pickItem.pickedQuantity).toString())
            }

            picklistlay.setOnClickListener {
                 var pos = adapterPosition
                dynamicKgClickListener.batchKgSelected(pickItem,pos)
            }

            if(pickItem.imageString != null && pickItem.imageString.isNotEmpty()){
                imgCardl.visibility = View.VISIBLE

                val imageByteArray: ByteArray = Base64.decode(pickItem.imageString, Base64.DEFAULT)

                Glide.with(context)
                    .asBitmap()
                    .load(imageByteArray)
                    .placeholder(R.drawable.no_image_found)
                    .into(imgpick)
            }
            else{
                imgCardl.visibility = View.GONE
            }

          // pickItem.imageUrl = Constants.imgUrl
            if(pickItem.frozen.equals("Yes")) {
                remarkpick.visibility = GONE
                remark_itemLayl.visibility = GONE

//                if (pickItem.palletQty.isNotEmpty() && pickItem.palletQty.toDouble() > 0) {
//                    remarkpick.visibility = VISIBLE
//                } else {
//                    remarkpick.visibility = GONE
//                }
            }else{
                remarkpick.visibility = VISIBLE
                remark_itemLayl.visibility = VISIBLE
            }

            remarkpick.setOnClickListener {
                if (pickListRemarkClickListener != null) {
                    val pos = adapterPosition
                    if (pos != -1) {
                        Log.w("remarkitem22", "")
                        if(pickItem.frozen.equals("Yes")) {
                            pickListRemarkClickListener!!.pickListDetailRemark(pickListItem,pos,"Dynamic")
                        }
                        else{
                            pickListRemarkClickListener!!.pickListDetailRemark(pickListItem,pos,"Standard")
                        }
                    }
                }

                Log.w("remarkitemqq", "")
            }


//            pickItem.pickedQuantity = 0
//            pickItem.balance = 0
         //   picktxt.setSelection(picktxt.length())

          //  setBinAdapter()

//            pickListBinAdapter = PickListBinAdapter(context, pick_bin_List)
//
//            pickBin_rv!!.layoutManager =
//                androidx.recyclerview.widget.LinearLayoutManager(
//                    context,
//                    RecyclerView.VERTICAL,
//                    false
//                ) as RecyclerView.LayoutManager?
//            pickBin_rv!!.adapter = pickListBinAdapter

//            if(pickItem.pickedQuantity.toString().isNotEmpty()){
//               picktxt.setText(pickItem.pickedQuantity.toString())
//            }
            //  for(int i =0 ;i < dataList.size(); i++){



//            if (pickItem.quantity == pickItem.pickedQuantity) {
////                checkboxpick.visibility = View.GONE
//                picklistlay.setBackgroundResource(R.color.colorPrimaryLight)
//                pickQtytxt.isEnabled = false
//            } else {
////                checkboxpick.visibility = View.VISIBLE
//                picklistlay.setBackgroundResource(R.color.white)
//                pickQtytxt.isEnabled = true
//            }
            //   }
            if (pickItem.stockInHand < 0.0) {
                stocktxt.setTextColor(ContextCompat.getColor(context, R.color.red_btn_bg_color))
//                checkboxpick.visibility = View.GONE
                pickQtytxt.isEnabled = false
            }

            if (::selectedModel.isInitialized
                && selectedModel.productCode == pickItem.productCode
//                && selectedModel.location == pickItem.location
                && istrue) {
                Log.e("graadd_entry","")

                object : CountDownTimer(1000, 500) {
                    override fun onTick(millisUntilFinished: Long) {
                        CommonMethods.setBlinkingText(pickQtytxt)
                        CommonMethods.setBlinkingText(qtyTextviewl)
                    }

                    override fun onFinish() {
                        pickQtytxt.clearAnimation()
                        qtyTextviewl.clearAnimation()
                    }
                }.start()
            } else {
                Log.e("graadd_enss","")
                pickQtytxt.clearAnimation()

            }
//            if (::selectedModelKg.isInitialized && isKgQty){
//               pickQtytxt.setText(selectedModelKg.kgQty.toString() + " KG")
//                Log.e("piqtyyy11",".. "+pickQtytxt.text.toString()+isKgQty)
//            }
//            else{
//                pickQtytxt.setText(pickItem.kgQty.toString())
//                Log.e("piqtyyy22",".. "+pickQtytxt.text.toString()+isKgQty)
//
//            }


//            if(pickItem.isCheckboxSelect()){
//                baltxt.setText ("0.0");
//                picktxt.setText((String.valueOf(pickItem.getOrderQty())));
//            }
//            else{
//                baltxt.setText ((String.valueOf(pickItem.getBalance())));
//                picktxt.setText((String.valueOf(pickItem.getPickedQuantity())));
//            }

//            if (pickItem.getStockInHand() == pickItem.getPickedQuantity()) {
//                picklistlay.setBackgroundResource(R.color.colorPrimaryLight);
//            } else {
//                picklistlay.setBackgroundResource(R.color.white);
//            }

            //  Glide.with(context).load(categoryFood.getImage_url()).apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(16)).placeholder(R.drawable.placeholder_food)).into(image);
        }

        init {
            pcodetxt = itemView.findViewById<View>(R.id.pcode_item) as TextView
            pnametxt = itemView.findViewById<View>(R.id.pname_item) as TextView
            oqtytxt = itemView.findViewById<View>(R.id.pqty_item) as TextView
            pickQtytxt = itemView.findViewById<View>(R.id.ppick_item) as TextView
            baltxt = itemView.findViewById<View>(R.id.pbal_item) as TextView
            deletepick = itemView.findViewById<View>(R.id.delete_pick) as ImageView
            stocktxt = itemView.findViewById<View>(R.id.stock_hand) as TextView
            qtyTextviewl = itemView.findViewById<View>(R.id.qtytextviewa) as TextView
            total_kg_pick_iteml = itemView.findViewById<View>(R.id.total_kg_pick_item) as TextView
            unit_price_item_pickl = itemView.findViewById<View>(R.id.unit_price_item_pick) as TextView
            no_catron_pick = itemView.findViewById<View>(R.id.no_carton_pick) as TextView
            no_carton_title_iteml = itemView.findViewById<View>(R.id.no_carton_title_item) as TextView
            remarktxt_iteml = itemView.findViewById<View>(R.id.remarktxt_item) as TextView
//            checkboxpick = itemView.findViewById<View>(R.id.checkbox_pick) as CheckBox
            picklistlay = itemView.findViewById<View>(R.id.picklist_lay) as CardView
            editpick = itemView.findViewById<View>(R.id.edit_pick) as ImageView
            remarkpick = itemView.findViewById<View>(R.id.remark_pick) as ImageView
            picklist_lay = itemView.findViewById(R.id.picklist_lay1)
            this.bin_image_up = itemView.findViewById<View>(R.id.bin_pick_img_up) as ImageView
            this.bin_image_down = itemView.findViewById<View>(R.id.bin_pick_img_down) as ImageView
            this.pickBin_rv = itemView.findViewById(R.id.rv_bin)
            this.progressBar = itemView.findViewById(R.id.progress)
            imgpick = itemView.findViewById<View>(R.id.imgPickl) as ImageView
            imgCardl = itemView.findViewById<View>(R.id.imgcard) as CardView
            releas_qty_txtl = itemView.findViewById<View>(R.id.releas_qty_txt) as TextView
            status_txt = itemView.findViewById<View>(R.id.status_txt_detail) as TextView
            status_txt_lay = itemView.findViewById<View>(R.id.status_lay) as LinearLayout
            remark_itemLayl = itemView.findViewById<View>(R.id.remark_itemLay) as LinearLayout
          //  pickqty_txtl = itemView.findViewById<View>(R.id.pickqty_txt) as TextView

            this.bin_layl = itemView.findViewById<View>(R.id.bin_lay) as LinearLayout


//            bin_image_down!!.setOnClickListener(View.OnClickListener {
//                pos = adapterPosition
//                bin_image_down!!.visibility = View.GONE
//                bin_image_up!!.visibility = View.VISIBLE
//                bin_layl!!.setVisibility(View.VISIBLE)
//
////                if (pos != -1){
////
////                val itemBinLocRequest =
////                    ItemBinLocRequest(pickListItem.get(pos).productCode, warehouseCode!!)
////                presenter?.itemBinLocationApiCall(context, itemBinLocRequest)
////                progressBar!!.visibility = View.VISIBLE
////                Log.e("progress", ""+pos)
////            }
////                bin_layl!!.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_accent_24dp)
//
//            })

            bin_image_down!!.setOnClickListener(View.OnClickListener {
                bin_layl!!.setVisibility(View.VISIBLE)
                bin_image_down!!.visibility = View.GONE
                bin_image_up!!.visibility = View.VISIBLE
//                bin_layl!!.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_accent_24dp)

            })
            bin_image_up!!.setOnClickListener(View.OnClickListener {
                bin_layl!!.setVisibility(View.GONE)
                bin_image_up!!.visibility = View.GONE
                bin_image_down!!.visibility = View.VISIBLE

            })

//            picklistlay.setOnClickListener {
//                val pos = adapterPosition
//                if (pos != -1) {
//                    if (pickListItem[pos].stockInHand > pickListItem[pos].pickedQuantity) {
//                        Log.e("layclick","")
//                    } else {
//                        Toast.makeText(
//                            context,
//                            "No stock this product",
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
//                    }
//                }
//            }

            editpick.setOnClickListener {
                if (pickListDetailClickListener != null) {
                    val pos = adapterPosition
                    if (pos != -1) {
//                        Log.w("pickedtuom", "" + pickListItem[pos].uoMCode)

                        //card_bg.setBackgroundResource(R.color.colorPrimaryLight);
                        // packageCategoryClickListener.packageCategorySelected(dataList.get(pos));

//                            for (int i = 0; i < dataList.size(); i++) {
//                                ResponseDataPickListDetailItem respItem = dataList.get(i);
//                                respItem.setItemSelected(i == pos);
//                                dataList.set(i, respItem);
//                                notifyItemChanged(i);
//                            }
                        if (pickListItem[pos].stockInHand > pickListItem[pos].pickedQuantity) {
                            pickListDetailClickListener.pickListDetailSelected(pickListItem,pos)
                        } else {
                            Toast.makeText(
                                context,
                                "No stock this product",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
            }

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
            deletepick.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    istrue = false
                    if (pickListDetailClickListener != null) {
                        val pos = adapterPosition
                        if (pos != -1) {
                            pickListDeletelClickListener.pickListDeleteSelected(pos)
                        }
                    }
                }
            })
        }

//        private fun setBinAdapter() {
//
//            val pickListBinmodels: Array<ResponseDataBinLocItem> = arrayOf<ResponseDataBinLocItem>(
//
//                ResponseDataBinLocItem(
//                    "10", "30",1,"qewww"),
//                ResponseDataBinLocItem(
//                    "20", "10",1,"qewww"),
//                ResponseDataBinLocItem(
//                    "30", "20",1,"qewww"),
//                ResponseDataBinLocItem(
//                    "10", "30",1,"qewww"),
//            )
//
//            pickListBinAdapter = PickListBinAdapter(context, pickListBinmodels.toMutableList())
//            pickBin_rv!!.layoutManager =
//                androidx.recyclerview.widget.LinearLayoutManager(
//                    context,
//                    RecyclerView.VERTICAL,
//                    false
//                ) as RecyclerView.LayoutManager?
//            pickBin_rv!!.adapter = pickListBinAdapter
//
//        }

    }

//    fun setModifierAdapter() {
//        val str = "all,project,file"
//        val arrayList = str.split(",").toTypedArray()
//        for (i in arrayList.indices) {
//            choiceList.add(arrayList[i])
//            Log.e("split", ".." + arrayList[i])
//        }
//
//        rvModifierlist.setHasFixedSize(true);
//        rvModifierlist.setLayoutManager(new LinearLayoutManager(SplitViewActivity.this, LinearLayoutManager.HORIZONTAL, false));
//        modifierAdapter = new ModifierSplitAdapter(SplitViewActivity.this, choiceList, this);
//        rvModifierlist.setAdapter(modifierAdapter);
//    }

    interface PickListDetailClickListener {
        fun pickListDetailSelected(pickList:ArrayList<NewSalesOrderDetailItem>  , position: Int?)
    }
    interface PickListRemarkClickListener {
        fun pickListDetailRemark(pickList:ArrayList<NewSalesOrderDetailItem>  , position: Int? ,action: String)
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
    fun updateQty(pdtName: NewSalesOrderDetailItem, istrueVal: Boolean) {
        selectedModel = pdtName
        istrue = istrueVal
        notifyDataSetChanged()
    }
    fun updateStatus(status : String) {
        pickStatus = status
        notifyDataSetChanged()
    }
    fun updateList(list: ArrayList<NewSalesOrderDetailItem>) {
        pickListItem = list
        notifyDataSetChanged()
    }
    interface DynamicKgClickListener {
        fun batchKgSelected(item: NewSalesOrderDetailItem, position: Int?)
    }

}