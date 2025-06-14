package com.winapp.wmsSQL.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.winapp.pickanddrop.ui.model.PickIistDeliveryListingModel
import com.winapp.wmsSQL.R
import com.winapp.wmsSQL.activity.NewDeliveryPickListActivity
import com.winapp.wmsSQL.activity.NewPickListAddActivity
import com.winapp.wmsSQL.printpreview.SalesOrderPrintPreview

class DeliveryPickListNewAdapter(
    private val context: Context,
    private var dataList: MutableList<PickIistDeliveryListingModel>,
    private val pickListUploadClickListener: PickListUploadClickListener
) : RecyclerView.Adapter<DeliveryPickListNewAdapter.MyViewHolder>() {
    private val pickListInvoiceClickListener: PickListInvoiceClickListener? = null
    private var islongPress: Boolean? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.picklist_deliverylist_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var pidatetxt: TextView
        var piinvoicenotxt: TextView
        var pinoofitemtxt: TextView
        var custaddrtxt: TextView
        var pistatustxt: TextView
        var usernametxt : TextView
        var picustnametxt: TextView
        var picklistinvoicelay: CardView
        var three_dot_pickl: ImageView
        var picklist_deli_Lay : LinearLayout
        var three_dot_picklay: LinearLayout

        var progressBar: ProgressBar? = null
        @SuppressLint("ClickableViewAccessibility", "SuspiciousIndentation")
        fun setData(pickItem: PickIistDeliveryListingModel) {
            picustnametxt.text = pickItem.customerName+" - "+pickItem.customerCode
            pidatetxt.text = pickItem.docDate
            piinvoicenotxt.text = pickItem.docNumber
            pinoofitemtxt.text = pickItem.noOfItem
            pistatustxt.text = pickItem.pickListStatus
            pistatustxt.text = pickItem.customerAddress


            Log.e("cust_nameaa", ".." + pickItem.customerName+" .."+pickItem.noOfItem)
            //   schedule_date.setText(scheduledatel.getDate());
            if (pickItem.pickListStatus.equals(
                    "O",
                    ignoreCase = true
                ) || pickItem.pickListStatus.equals("Open", ignoreCase = true)
            ) {
                pistatustxt.setText(" Pending ")
                islongPress = true
                pistatustxt.setBackgroundResource(R.drawable.corner_picklist_blue_text)

                //  pistatustxt.setTextColor(ContextCompat.getColor(context, R.color.white))
//                pistatustxt.setBackgroundResource(R.drawable.round_accent_rad3_blue)
              //  picklistinvoicelay.setBackgroundResource(R.color.white)
            } else if (pickItem.pickListStatus.equals(
                    "C", ignoreCase = true) || pickItem.pickListStatus.equals("Close", ignoreCase = true)) {
                pistatustxt.setText(" Completed ")
                islongPress = true
                pistatustxt.setBackgroundResource(R.drawable.corner_picklist_red_text)

                //  pistatustxt.setTextColor(ContextCompat.getColor(context, R.color.white))
           //     picklistinvoicelay.setBackgroundResource(R.color.colorPrimary)
            } else if (pickItem.pickListStatus.equals("R", ignoreCase = true)) {
                pistatustxt.setText(R.string.release)
                islongPress = true
                pistatustxt.setBackgroundResource(R.drawable.corner_picklist_orange_text)

                //pistatustxt.setTextColor(ContextCompat.getColor(context, R.color.black))
          //      picklistinvoicelay.setBackgroundResource(R.color.white)
            } else if (pickItem.pickListStatus.equals("OW", ignoreCase = true)) {
                islongPress = false
                pistatustxt.setText("Open Weight")
                pistatustxt.setTextColor(ContextCompat.getColor(context, R.color.white))
                pistatustxt.setBackgroundResource(R.drawable.round_accent_rad3_green)
                picklistinvoicelay.setBackgroundResource(R.color.white)
            }
            else if (pickItem.pickListStatus.equals("OC", ignoreCase = true)) {
                pistatustxt.setText(" Partial ")
                islongPress = false
                pistatustxt.setTextColor(ContextCompat.getColor(context, R.color.black))
                pistatustxt.setBackgroundResource(R.drawable.round_accent_rad3_yellow)
                picklistinvoicelay.setBackgroundResource(R.color.white)
            }
            else if (pickItem.pickListStatus.equals("P", ignoreCase = true)) {
                islongPress = false
                pistatustxt.setText("Open Printed")
                pistatustxt.setTextColor(ContextCompat.getColor(context, R.color.white))
                pistatustxt.setBackgroundResource(R.drawable.round_accent_rad3_green)
                picklistinvoicelay.setBackgroundResource(R.color.white)
            }

            three_dot_picklay.setOnClickListener{ view ->
                pickListUploadClickListener!!.pickListUploadSelected(pickItem,view)
            }

            picklist_deli_Lay.setOnClickListener {
                    val pos = adapterPosition
              //  if(!pickItem.pickListStatus.equals("C")) {

                    if (pos != -1) {
                        //card_bg.setBackgroundResource(R.color.colorPrimaryLight);
                        // packageCategoryClickListener.packageCategorySelected(dataList.get(pos));

//                        for (i in dataList.indices) {
//                            val respItem = dataList[i]
//                            respItem.isItemSelected = i == pos
//                            dataList[i] = respItem
//                            notifyItemChanged(i)
//                        }
                     val intent = Intent(context, NewDeliveryPickListActivity::class.java)
                       // val intent = Intent(context, NewPickListAddActivityCopy::class.java)

                        intent.putExtra("salesCodeDel", pickItem.code)
                        intent.putExtra("custCodePickDel", pickItem.customerCode)
                        intent.putExtra("pick_itemDel", pickItem.noOfItem)
                        intent.putExtra("pick_InvDateDel", pickItem.docDate)
                        intent.putExtra("pick_statusDel", pickItem.pickListStatus)
                        intent.putExtra("pick_DocNumDel", pickItem.docNumber)
                        intent.putExtra("pick_DatetimeDel", pickItem.dateTime)
                     context.startActivity(intent)

                        //  pickListInvoiceClickListener.pickListInvoiceSelected(pos)
                    }
//                }else{
//
//                    // Device does not support Bluetooth
//                    Toast.makeText(
//                        context,
//                        "Already Completed",
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//                }
            }

//            if (pickItem.isItemSelected()) {
//                picklistinvoicelay.setBackgroundResource(R.color.colorPrimaryLight);
//            } else {
//                picklistinvoicelay.setBackgroundResource(R.color.white);
//            }

            //  Glide.with(context).load(packageCategory.getImage_url()).apply(new RequestOptions().placeholder(R.drawable.placeholder_food)).into(image);
            //  Glide.with(context).load(categoryFood.getImage_url()).apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(16)).placeholder(R.drawable.placeholder_food)).into(image);
        }
        init {
            pidatetxt = itemView.findViewById<View>(R.id.picki_del_date_item) as TextView
            picustnametxt = itemView.findViewById<View>(R.id.cust_name_picki_del_item) as TextView
            piinvoicenotxt = itemView.findViewById<View>(R.id.picki_del_invoiceno_item) as TextView
            pinoofitemtxt = itemView.findViewById<View>(R.id.picki_del_noitem_item) as TextView
            custaddrtxt = itemView.findViewById<View>(R.id.cust_addr_pick_deli_item) as TextView
            pistatustxt = itemView.findViewById<View>(R.id.picki_del_status_item) as TextView
            usernametxt = itemView.findViewById<View>(R.id.user_picki_item) as TextView
            picklistinvoicelay = itemView.findViewById<View>(R.id.picklist_deli_card) as CardView
            three_dot_pickl = itemView.findViewById<View>(R.id.three_dot_pickl) as ImageView
            three_dot_picklay = itemView.findViewById<View>(R.id.three_dot_pickl_lay) as LinearLayout
            picklist_deli_Lay = itemView.findViewById<View>(R.id.picklist_deli_lay) as LinearLayout

        }
    }

    fun updateList(list: ArrayList<PickIistDeliveryListingModel>) {
        dataList = list
        notifyDataSetChanged()
    }

    interface PickListInvoiceClickListener {
        fun pickListInvoiceSelected(position: Int?)
    }
    interface PickListUploadClickListener {
        fun pickListUploadSelected(pickModel: PickIistDeliveryListingModel,view: View)
    }

    interface PickListInvLongClickListener {
        fun pickListLongClickSelected(position: Int?)
    }
}