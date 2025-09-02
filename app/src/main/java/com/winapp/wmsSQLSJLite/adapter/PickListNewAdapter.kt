package com.winapp.wmsSQLSJLite.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.winapp.pickanddrop.ui.model.PickIistResponseNew
import com.winapp.wmsSQLSJLite.R
import com.winapp.wmsSQLSJLite.activity.NewPickListAddActivity

class PickListNewAdapter(
    private val context: Context,
    private var dataList: MutableList<PickIistResponseNew>
) : RecyclerView.Adapter<PickListNewAdapter.MyViewHolder>() {
    private val pickListInvoiceClickListener: PickListInvoiceClickListener? = null
    private var islongPress: Boolean? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.picklist_invoice_item, parent, false)
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
        var pistatustxt: TextView
        var salesEmpnametxt : TextView
        var usernametxt : TextView
        var picustnametxt: TextView
        var nonPicked_count_item: TextView
        var picked_count_item: TextView
        var picklistinvoicelay: CardView
        var progressBar: ProgressBar? = null
        @SuppressLint("ClickableViewAccessibility")
        fun setData(pickItem: PickIistResponseNew) {
            picustnametxt.text = pickItem.customerName+" - "+pickItem.customerCode
            pidatetxt.text = pickItem.docDate
            piinvoicenotxt.text = pickItem.docNumber
            pinoofitemtxt.text = pickItem.noOfItem
            pistatustxt.text = pickItem.pickListStatus
            usernametxt.text = pickItem.ownerName
            salesEmpnametxt.text = pickItem.salesEmployee
            picked_count_item.text = pickItem.pickedCount
            nonPicked_count_item.text = pickItem.nonPickedCount

            Log.e("cust_nameaa", ".." + pickItem.customerName+" .."+pickItem.noOfItem)
            //   schedule_date.setText(scheduledatel.getDate());
            if (pickItem.pickListStatus.equals(
                    "O",
                    ignoreCase = true
                ) || pickItem.pickListStatus.equals("Open", ignoreCase = true)
            ) {
                pistatustxt.setText(" Pending ")
                islongPress = true
                pistatustxt.setTextColor(ContextCompat.getColor(context, R.color.white))
                pistatustxt.setBackgroundResource(R.drawable.round_accent_rad3_blue)
                picklistinvoicelay.setBackgroundResource(R.color.white)
            } else if (pickItem.pickListStatus.equals(
                    "C",
                    ignoreCase = true
                ) || pickItem.pickListStatus.equals("Close", ignoreCase = true)
            ) {
                pistatustxt.setText(" Completed ")
                islongPress = true
                pistatustxt.setTextColor(ContextCompat.getColor(context, R.color.white))
                pistatustxt.setBackgroundResource(R.drawable.round_accent_rad3_red)
           //     picklistinvoicelay.setBackgroundResource(R.color.colorPrimary)
            } else if (pickItem.pickListStatus.equals("R", ignoreCase = true)) {
                pistatustxt.setText(R.string.release)
                islongPress = true
                pistatustxt.setTextColor(ContextCompat.getColor(context, R.color.black))
                pistatustxt.setBackgroundResource(R.drawable.round_accent_rad3_yellow)
                picklistinvoicelay.setBackgroundResource(R.color.white)
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
            itemView.setOnClickListener {
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
                        Log.w("picknno", "" + pickItem.pickListNo);
                     val intent = Intent(context, NewPickListAddActivity::class.java)
                       // val intent = Intent(context, NewPickListAddActivityCopy::class.java)

                        intent.putExtra("salesCode", pickItem.code)
                        intent.putExtra("custCodePick", pickItem.customerCode)
                        intent.putExtra("pick_item", pickItem.noOfItem)
                        intent.putExtra("pick_SoDate", pickItem.docDate)
                        intent.putExtra("pick_status", pickItem.pickListStatus)
                        intent.putExtra("pick_listNo", pickItem.pickListNo)
                        intent.putExtra("pick_owner", pickItem.ownerName)
                        intent.putExtra("pick_salesEmp", pickItem.salesEmployee)
                        intent.putExtra("pick_DocNum", pickItem.docNumber)
                        intent.putExtra("pick_Datetime", pickItem.dateTime)

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
            val pos = adapterPosition
            pidatetxt = itemView.findViewById<View>(R.id.picki_date_item) as TextView
            piinvoicenotxt = itemView.findViewById<View>(R.id.picki_invoiceno_item) as TextView
            pinoofitemtxt = itemView.findViewById<View>(R.id.picki_noitem_item) as TextView
            pistatustxt = itemView.findViewById<View>(R.id.picki_status_item) as TextView
            picustnametxt = itemView.findViewById<View>(R.id.cust_name_picki_item) as TextView
            picked_count_item = itemView.findViewById<View>(R.id.picked_count_item) as TextView
            nonPicked_count_item = itemView.findViewById<View>(R.id.nonPicked_count_item) as TextView
            usernametxt = itemView.findViewById<View>(R.id.user_picki_item) as TextView
            salesEmpnametxt = itemView.findViewById<View>(R.id.sale_emp_picki_item) as TextView
            picklistinvoicelay = itemView.findViewById<View>(R.id.picklistinvoice_lay) as CardView
        }
    }

    fun updateList(list: ArrayList<PickIistResponseNew>) {
        dataList = list
        notifyDataSetChanged()
    }

    interface PickListInvoiceClickListener {
        fun pickListInvoiceSelected(position: Int?)
    }

    interface PickListInvLongClickListener {
        fun pickListLongClickSelected(position: Int?)
    }
}