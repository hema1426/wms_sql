package com.winapp.KHDelivery.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.winapp.KHDelivery.CommonMethods
import com.winapp.KHDelivery.R
import com.winapp.KHDelivery.adapter.BatchListPicklistAdapter
import com.winapp.KHDelivery.adapter.PickListDetailNewAdapterNew24
import com.winapp.KHDelivery.model.AppUtils
import com.winapp.KHDelivery.model.HomePageModel
import com.winapp.KHDelivery.model.ProductsModel
import com.winapp.KHDelivery.model.newPickDetail.BatchDetailPickModule
import com.winapp.KHDelivery.model.newPickDetail.NewSalesOrderDetailItem
import com.winapp.KHDelivery.model.newPickDetail.SalesOrderDetail
import com.winapp.KHDelivery.model.newPickDetail.SalesOrderNewModel
import com.winapp.KHDelivery.model.newPickDetail.SoRequests
import com.winapp.KHDelivery.utils.CommonMethodKotl.toast
import com.winapp.KHDelivery.utils.Constants
import com.winapp.KHDelivery.utils.SessionManager
import com.winapp.KHDelivery.utils.SharedPreferenceUtil
import com.winapp.KHDelivery.utils.Utils
import com.winapp.KHDelivery.utils.Utils.hideKeyboard
import org.json.JSONException
import org.json.JSONObject
import retrofit2.http.POST
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects
import java.util.Random
import kotlin.math.roundToInt


class PickListDetailActivity : BaseActivity(), View.OnClickListener,
    PickListDetailNewAdapterNew24.PickListDetailClickListener,
    PickListDetailNewAdapterNew24.PickListDeletelClickListener,
    PickListDetailNewAdapterNew24.PickListCheckboxlClickListener,
    PickListDetailNewAdapterNew24.DynamicKgClickListener,
    PickListDetailNewAdapterNew24.PickEditListener,
    PickListDetailNewAdapterNew24.PickListRemarkClickListener,
    BatchListPicklistAdapter.TotalClickListener,
    BatchListPicklistAdapter.RemoveBatchClickListener {

    private var picklistdetail_rv_standl: RecyclerView? = null
    private var picklistdetail_rv_dynamicl: RecyclerView? = null
    private var barcodePick: ImageView? = null
    private var sharedPreferenceUtil: SharedPreferenceUtil? = null
    private var empty_txt: TextView? = null
    private var scanType: String? = null

    var newSalesPickListDetailAdapter: PickListDetailNewAdapterNew24? = null
    var newpickList_api_set_Item_List: ArrayList<NewSalesOrderDetailItem> = ArrayList<NewSalesOrderDetailItem>()

    var newpickListDetail_Item_get_List: ArrayList<NewSalesOrderDetailItem> = ArrayList()
    var newpickList_Item_stand_remark: ArrayList<NewSalesOrderDetailItem> = ArrayList()
    var newpickList_Item_dynamic_remark: ArrayList<NewSalesOrderDetailItem> = ArrayList()
    var intentIntegrator: IntentIntegrator? = null
    private var cust_namepickd: TextView? = null
    private var order_datepickd: TextView? = null
    private var itemSalesEmp: TextView? = null
    private var itemOwnerName: TextView? = null
    private var invoicepickd: TextView? = null
    private var currentSaveDateTime: String? = ""
    var pickCode: String? = ""
    var pickItem: String? = ""
    var pickSoDate: String? = ""
    var pickSoTimel: String? = ""
    var pickStatus: String? = ""
    var pickOwner: String? = ""
    var pickSalesEmp: String? = ""
    var pickListNo: String? = ""
    var remarkStr: String? = ""
    var palletremarkStr: String? = ""
    var custCodeStr: String? = ""
    var remarkk = ""
    var palletApiremarkk = ""
    var joiningApiQty = ""
    var orderStatus = ""
    var orderStatusDynamic = ""
    var pickqttApi = 0.0
    var no_pcsApi = 0.0
    var no_palletApi = ""
    var dialog_Oqty: Int? = null
    var dialog_pickqty: Double? = null
    var dialog_pdtname: String? = null
    private var save_pickd: Button? = null
    var isscanpdt: Boolean? = false
    var isqtygreat: Boolean? = false
    private var searchLayl: LinearLayout? = null
    private var dynamicLayl: LinearLayout? = null
    private var standardLayl: LinearLayout? = null
    private var dynamicl_viewl: View? = null
    private var standardl_viewl: View? = null

    var RESULT_CODE = 12
    var scannedBarcode = ""
    private var itemSize: TextView? = null

    private var currentRandomNo: Int = -1

    //    var addKg_edLayl: LinearLayout? = null
    var scan_batchImg: ImageView? = null
    var batch_addl: ImageView? = null
    var kg_addBtn: ImageView? = null
    var ctn_qty_pick_ed: TextView? = null
    var ctn_qty_tittlel: TextView? = null
    var kg_txt_pickl: TextView? = null
    var save_batchl: TextView? = null
    var unit_price_pickl: TextView? = null
    var total_pick_kg_txtl: TextView? = null
    var dailogKgLayl: LinearLayout? = null
    val myEditTextList = ArrayList<String>()
    var isDialAdd: Boolean? = false
    var isDialAddEmpty: Boolean? = false
    var batch_rv: RecyclerView? = null
    var batchListAdapter: BatchListPicklistAdapter? = null
    lateinit var itemBatchlist: NewSalesOrderDetailItem
    var addKg_edLayl: LinearLayout? = null
    var pdtname_dialKg: TextView? = null
    var empty_txt_batch: TextView? = null
    var batchsize_item: TextView? = null
    var dialog: Dialog? = null
    var filterBatchNew = ArrayList<BatchDetailPickModule>()
    var session: SessionManager? = null
    var user: java.util.HashMap<String, String>? = null
    var companyId: String? = null
    var locationCode: String? = ""
    lateinit var pDialog: SweetAlertDialog
    var userName: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picklist_detail)
        Log.w("activity_cg", javaClass.simpleName.toString()+" PickListDetailNewAdapterNew24")

        sharedPreferenceUtil = SharedPreferenceUtil(this)

        intentIntegrator = IntentIntegrator(this)
        session = SessionManager(this)

        user = session!!.getUserDetails()
        userName = user!!.get(SessionManager.KEY_USER_NAME)
        companyId = user!!.get(SessionManager.KEY_COMPANY_CODE)
        locationCode = user!![SessionManager.KEY_LOCATION_CODE]
        Log.w("pickdetailloc", "" + locationCode)

        picklistdetail_rv_standl = findViewById(R.id.rv_picklist_detail_stand)
        picklistdetail_rv_dynamicl = findViewById(R.id.rv_picklist_detail_dynamic)
        barcodePick = findViewById(R.id.barcode_piScan)
        cust_namepickd = findViewById(R.id.cust_name_pickd)
        order_datepickd = findViewById(R.id.order_date_pickd)
        itemSalesEmp = findViewById(R.id.item_salesEmp)
        itemOwnerName = findViewById(R.id.owner_pickd)
        save_pickd = findViewById(R.id.save_pick)
        invoicepickd = findViewById(R.id.invoice_no_pickd)
        searchLayl = findViewById(R.id.searchLay)
        itemSize = findViewById(R.id.item_sizeadd)
        dynamicLayl = findViewById(R.id.dyanamicLay)
        standardLayl = findViewById(R.id.standardLay)
        dynamicl_viewl = findViewById(R.id.dynamicl_view)
        standardl_viewl = findViewById(R.id.standardl_view)


        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Pick List"

        save_pickd!!.setOnClickListener(this);

        if (intent.hasExtra("salesCode")) {
            pickCode = intent.getStringExtra("salesCode")
            pickItem = intent.getStringExtra("pick_item")
            pickSoDate = intent.getStringExtra("pick_SoDate")
            pickOwner = intent.getStringExtra("pick_owner")
            pickSalesEmp = intent.getStringExtra("pick_salesEmp")
            if (intent.getStringExtra("pick_status").equals("OC") ||
                intent.getStringExtra("pick_status").equals("OW") ||
                intent.getStringExtra("pick_status").equals("OP")
            ) {
                pickStatus = "O"
            } else {
                pickStatus = intent.getStringExtra("pick_status")
            }
            pickListNo = intent.getStringExtra("pick_listNo")

            Log.w("pistatu_ss", "$pickStatus")
        }

        if (pickCode != null) {
            invoicepickd!!.setText(pickCode)
            itemSalesEmp!!.setText(pickSalesEmp)
            itemOwnerName!!.setText(pickOwner)

//            val picklistdetailmodel = PickListDetailRequest(pickCode!!)
//            presenter?.SalesOrderApiCall(this, picklistdetailmodel)
//            CommonMethods.showProgressDialog(this)
        }

        barcodePick!!.setOnClickListener {
            if (checkPermission())
                scanFromFragment()

//            val intent = Intent(this, BarCodeScanner::class.java)
//            intent.putExtra("from", "add_po")
//            startActivityForResult(intent, RESULT_CODE)
            scanType = "item"
        }

        //setAdapter(this.newpickListDetail_Item_List)

        standardLayl!!.setOnClickListener {
            standardl_viewl!!.setVisibility(View.VISIBLE)
            dynamicl_viewl!!.setVisibility(View.GONE)
            newpickListDetail_Item_get_List = arrayListOf()
//            for (i in 0 until newpickList_api_set_Item_List!!.size) {
//                if (!newpickList_api_set_Item_List.get(i).frozen.equals("Yes")) {
//                    pickDetailItem_filter = newpickList_api_set_Item_List.get(i)
//                    newpickListDetail_Item_get_List.add(pickDetailItem_filter!!)
//                }
            newpickListDetail_Item_get_List = newpickList_api_set_Item_List.filter {
                it.frozen != "Yes"
            } as ArrayList<NewSalesOrderDetailItem>

            setAdapter(newpickListDetail_Item_get_List)
            Log.w("picklistStandard", "" + newpickListDetail_Item_get_List.size)
//            }
        }

        dynamicLayl!!.setOnClickListener {
            dynamicl_viewl!!.setVisibility(View.VISIBLE)
            standardl_viewl!!.setVisibility(View.GONE)
            newpickListDetail_Item_get_List = arrayListOf()
//            for (i in 0 until newpickList_api_set_Item_List!!.size) {
//                if (newpickList_api_set_Item_List.get(i).frozen.equals("Yes")) {
//                    pickDetailItem_filter = newpickList_api_set_Item_List.get(i)
//                    newpickListDetail_Item_get_List.add(pickDetailItem_filter!!)
//                }

            newpickListDetail_Item_get_List = newpickList_api_set_Item_List.filter {
                it.frozen == "Yes"
            } as ArrayList<NewSalesOrderDetailItem>

            setAdapter(newpickListDetail_Item_get_List)
            Log.w("picklistDynamic", "" + newpickListDetail_Item_get_List.size)
//            }
        }
//
//        CoroutineScope(Dispatchers.Default).launch {
//            val database = PDDatabase.getInstance(context = this@PickListDetailActivity)
//            val Picklistdata = database.PDpickListDao().all
//
//            Log.w("Picklistdata", "" + Picklistdata);
//        }
    }

    private fun setAdapter(arrayList: ArrayList<NewSalesOrderDetailItem>) {
        newSalesPickListDetailAdapter = PickListDetailNewAdapterNew24(
            this, arrayList,
            this, this, this, this,
            this, this
        )
        itemSize!!.text = arrayList.size.toString() + " Items"

        picklistdetail_rv_standl!!.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            ) as RecyclerView.LayoutManager?
        picklistdetail_rv_standl!!.adapter = newSalesPickListDetailAdapter

        Log.e("PDpickListDao.all", "")
    }

    override fun onClick(view: View?) {
        when (view!!.getId()) {
            R.id.save_pick -> {
//                showdialog_save()
                showdialog_save1()
            }
        }
    }

    private fun showdialog_delete(position: Int?) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Delete")
        alertDialogBuilder.setMessage("Are you sure you want to Delete?")
        alertDialogBuilder.setPositiveButton(
            "Yes"
        ) { dialog, which ->
            if (position != -1) {
                this.newpickList_api_set_Item_List.removeAt(position!!)
                newSalesPickListDetailAdapter!!.notifyDataSetChanged()

                dialog.dismiss()
            }
        }
        alertDialogBuilder.setNegativeButton(
            "No"
        ) { dialog, which -> dialog.dismiss() }
        alertDialogBuilder.show()
    }


//    override fun onActivityResult(
//        requestCode: Int,
//        resultCode: Int,
//        data: Intent?
//    ) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RESULT_CODE) {
//            val barcodeText = data!!.extras!!.getString("Contents")
//            Log.w("BarcodeText:", barcodeText!!)
//
//            Log.e("barcode_result", "..."+data +"....")
//
//            val mp = MediaPlayer.create(this, R.raw.beep) // sound is inside res/raw/mysound
//            mp.start()
//            if (barcodeText == null) {
//                Toast.makeText(this, "no product found", Toast.LENGTH_SHORT).show()
//            }
//            else {
//                scanBarTxt(barcodeText)
//            }
//            }
//        }

//    http://localhost:2009/api/ProductList/GetAllSalesOrderList?Requestdata={"CustomerCode":
//    // "C014-AM-MT-013","CustomerName":"Ang Mo Supermarket",FromDate:"20231101","ToDate":"20240102","SONumber":"24010010"}

    fun scanBarTxt(barcode: String) {
        //update product list
        isscanpdt = false
        isqtygreat = false

        var isSearched = false
        var currentIndex = 0
        for ((index, prod) in this.newpickList_api_set_Item_List.withIndex()) {
            Log.e("picklss", "" + prod.productCode + "  $barcode")

            if (prod.productCode == barcode) {

                isscanpdt = true
                if (prod.quantity > prod.pickedQuantity) {
                    if (prod.stockInHand > prod.pickedQuantity) {
                        isqtygreat = true
                        prod.pickedQuantity = prod.pickedQuantity + 1

                        //  prod.balance = prod.balance - 1
                        //todo
                        prod.balance = 1 - prod.balance

                        newSalesPickListDetailAdapter.let {
                            it!!.notifyDataSetChanged()
                        }
                        if (newSalesPickListDetailAdapter != null) {
                            newSalesPickListDetailAdapter!!.updateQty(prod, true)
                            isSearched = true
                            currentIndex = index
//                        break;
                        }

                        Log.e("pick_compar", "" + prod.productCode + "..." + barcode)
                        Toast.makeText(
                            this,
                            "pick qty added-> " + barcode,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        Toast.makeText(
                            this,
                            "No stock this product",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }
                } else {
                    Toast.makeText(
                        this,
                        "Entered value greater than total order qty ",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                if (isSearched) {
                    picklistdetail_rv_standl!!.smoothScrollToPosition(currentIndex)
                }

            }
//                    if (prod.stockInHand <= prod.quantity) {
//                        Toast.makeText(
//                            this,
//                            "Avaiable Stock : "+prod.stockInHand,
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
//                    }

        }

        if (!isscanpdt!!) {
            Log.e("pickbarco", "..")
            Toast.makeText(this, "no product matched", Toast.LENGTH_SHORT).show()

        }
    }

    fun scanFromFragment() {
        fragmentLauncher.launch(ScanOptions())
    }

    private val fragmentLauncher: ActivityResultLauncher<ScanOptions> = registerForActivityResult(
        ScanContract()
    ) { result ->
        if (result.contents == null) {
            Toast.makeText(this@PickListDetailActivity, "No Product Found1", Toast.LENGTH_LONG)
                .show()
        } else {
            if (scanType == "item") {
                scanBarTxt(result.contents)
            }
            if (scanType == "batch_item") {
                Log.w("itembatchlist11", " $itemBatchlist")
                // if (itemBatchlist.cartonQty != 0.0 && itemBatchlist.cartonQty > filterBatch.size) {

                searchFilter(result.contents, itemBatchlist)
//                    } else {
//                        toast("Check no. of carton! ")
//                    }
            }

            Log.e("scan_barcode.. ", "${result.contents}")
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun showdialog_save1() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Save")
        alertDialogBuilder.setMessage("Are you sure you want to Save?")
        alertDialogBuilder.setPositiveButton(
            "Yes"
        ) { dialog, which ->
            if (this.newpickList_api_set_Item_List.size > 0) {
                val Picklists1: ArrayList<SalesOrderDetail> = ArrayList<SalesOrderDetail>()
                var batchlist: ArrayList<BatchDetailPickModule> = arrayListOf()
                newpickList_Item_stand_remark = arrayListOf()
                newpickList_Item_dynamic_remark = arrayListOf()
//                this.newpickListDetail_Item_List =
//                    newSalesPickListDetailAdapter!!.pickListItem as ArrayList<NewSalesOrderDetailItem>

                Log.w("cg_json ", Gson().toJson(this.newpickList_api_set_Item_List))
                Log.w("list_sizeJson", "" + this.newpickList_api_set_Item_List.size)
                var batchQtySelect = 0.0
                var batchStr = ""

                for (newpickVal in newpickList_api_set_Item_List) {
                    batchlist = newpickVal.selectedBatchDetails

                    if (newpickVal.frozen.equals("Yes")) {
                        batchStr = newpickVal.frozen

                        if (newpickVal.joinBatchQty != null) {
                            joiningApiQty = newpickVal.joinBatchQty!!
                        } else {
                            joiningApiQty = ""
                        }

                        if (newpickVal.palletRemarks != null) {
                            palletApiremarkk = newpickVal.palletRemarks!!
                        } else {
                            palletApiremarkk = ""
                        }

                        if (newpickVal.remarks != null &&
                            !newpickVal.remarks.equals("")
                        ) {
                            remarkk = newpickVal.remarks
                        } else {
                            remarkk = "O"
                        }
                    } else {
                        batchStr = "No"

                        remarkk = newpickVal.remarks
                        palletApiremarkk = ""
                        joiningApiQty = ""
                        batchlist = arrayListOf()
                    }
//                        Log.w("selecbatchharr", "${newpickVal.selectedBatchDetails}")
//                        Log.w("selecbatchh11", "${newpickVal.selectedBatchDetails.size}")
//                        for (batch in newpickVal.selectedBatchDetails) {
//
//                            batchStr = "Yes"
//
//                            if (batch.batchQty != null && !batch.batchQty!!.equals("") &&
//                                batch.batchQty!! > 0
//                            ) {
//                                batchQtySelect = batch.batchQty!!
//                            } else {
//                                batchQtySelect = 0.0
//                            }
////                            batchlist.add(
////                                BatchDetailPickModule(
////                                    batch.batchNo,
////                                    batchQtySelect,
////                                    newpickVal.productCode, 0.0,
////                                    "",
////                                )
////                            )
////                            batchlist.addAll(newpickVal.selectedBatchDetails)
//
////                            batchlist.add(
////                                BatchDetailModule(
////                                    batch.batchNo,
////                                    batchQtySelect,
////                                    batch.itemCode
////                                )
////                            )
//                            Log.w("selectQtyyy.", "" + ".." + batch.batchQty)
//                        }
//
//                        batchlist = newpickVal.selectedBatchDetails
//
//                    } else {
//
//                        batchlist = arrayListOf()
//                        batchStr = "No"
//                    }


                    Log.w("select_qty.", "" + ".." + batchQtySelect + newpickVal.pickedQuantity)

                    var uomCodel = "PCS"
                    if (newpickVal.uomCode != null &&
                        !newpickVal.uomCode.equals("null")
                    ) {
                        uomCodel = newpickVal.uomCode.toString()
                    } else {
                        uomCodel = "PCS"
                    }

                    if (newpickVal.frozen.equals("Yes")) {
//                        pickDetail_filter_remark = newpickVal
                        newpickList_Item_dynamic_remark.add(newpickVal)
                    }else{
                        newpickList_Item_stand_remark.add(newpickVal)
                    }

                    if (newpickVal.pickedQuantity != null &&
                        !newpickVal.pickedQuantity.equals("") &&
                        newpickVal.pickedQuantity > 0
                    ) {
                        pickqttApi = newpickVal.pickedQuantity
                    } else { //faisal given changes
                        pickqttApi = newpickVal.quantity.toDouble()
                    }

                    if (newpickVal.pcsQty != null &&
                        !newpickVal.pcsQty.equals("") &&
                        newpickVal.pcsQty > 0
                    ) {
                        no_pcsApi = newpickVal.pcsQty
                    } else {
                        no_pcsApi = 0.0
                    }
                    if (newpickVal.palletQty != null &&
                        !newpickVal.palletQty.equals("") &&
                        newpickVal.palletQty.toDouble() > 0
                    ) {
                        no_palletApi = newpickVal.palletQty
                    } else {
                        no_palletApi = ""
                    }

                    for (newpickValRemark in newpickList_Item_stand_remark) {
                        if (newpickValRemark.remarks != null &&
                            !newpickValRemark.remarks.equals("")
                        ) {
                            orderStatus = "OC"
                            break
                        } else {
                            orderStatus = "O"
                        }
                    }

//                    for (newpickValRemark in newpickList_Item_dynamic_remark) {
//                        if (newpickValRemark.remarks != null &&
//                            !newpickValRemark.remarks.equals("")&&
//                            newpickValRemark.remarks.equals("O")
//                        ) {
//                            orderStatusDynamic = "O"
//                            break
//                        } else {
//                            orderStatusDynamic = "OW"
//                        }
//                    }

//                    if (!newpickVal.frozen.equals("Yes")) {
//                        finalOrderStatus = orderStatus
//                    }else{
//                        finalOrderStatus = orderStatusDynamic
//                    }
                    val picklistdetail = SalesOrderDetail(
                        joiningApiQty,
                        batchlist,
                        batchStr,
                        newpickVal.cartonQty.toString(),
                        no_pcsApi.toString(),
                        no_palletApi,
                        newpickVal.productCode,
                        pickqttApi,
                        remarkk,
                        palletApiremarkk,
                        newpickVal.price,
                        uomCodel,
                        locationCode!!,
                        newpickVal.lineNum
                        //newpickListDetail_Item_List.get(i).selectedBatchDetails
                    )
                    Picklists1.add(picklistdetail)

                    Log.w("savebatch", "$.. " + newpickVal.selectedBatchDetails)
                    Log.w("picknon", "$.. " + pickCode + "..REma" + newpickVal.remarks)
                }

                if (currentSaveDateTime == null || currentSaveDateTime!!.isEmpty()) {
                    val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    val currentDateandTime = sdf.format(Date())
                    currentSaveDateTime = currentDateandTime
                }

                val postingRequest = SoRequests(
                    userName!!,
                    currentSaveDateTime!!,
                    custCodeStr!!,
                    CommonMethods.dateConvert(pickSoDate!!)!!,
                    pickCode!!,
                    orderStatus!!,
                    pickStatus!!,
                    Picklists1
                )
                val postingPicklistmodel = SalesOrderNewModel(postingRequest)

                  savePicklist1(postingPicklistmodel)
                Log.w("gson_pick_saveee", ".." + Gson().toJson(postingPicklistmodel).toString())
            } else {
                toast("No Product to save")
            }
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton(
            "No"
        ) { dialog, which -> dialog.dismiss() }
        alertDialogBuilder.show()
    }


    fun getRandomNo(): Int {
        currentRandomNo = if (currentRandomNo == -1)
            Random().nextInt(200)
        else {
            currentRandomNo + 1
        }
        return currentRandomNo
    }

//    private fun showdialog_save() {
//        val alertDialogBuilder = AlertDialog.Builder(this)
//        alertDialogBuilder.setTitle("Save")
//        alertDialogBuilder.setMessage("Are you sure you want to Save?")
//        alertDialogBuilder.setPositiveButton(
//            "Yes"
//        ) { dialog, which ->
//            if (this.newpickListDetail_Item_List.size > 0) {
//                val Picklists1: ArrayList<PostingPickDetails> = ArrayList<PostingPickDetails>()
//                var batchlist: ArrayList<BatchDetailModule> = ArrayList()
//
//                this.newpickListDetail_Item_List =
//                    newSalesPickListDetailAdapter!!.pickListItem as ArrayList<NewSalesOrderDetailItem>
//
//                Log.d("cg_json ", Gson().toJson(this.newpickListDetail_Item_List))
//
//                for (i in 0 until this.newpickListDetail_Item_List.size) {
//
//                    if (this.newpickListDetail_Item_List.get(i).selectedBatchDetails.isNullOrEmpty()) {
//                        batchlist = arrayListOf()
//                    } else {
//                        var batchList1 =
//                            this.newpickListDetail_Item_List.get(i).selectedBatchDetails
//
//                        batchlist.add(
//                            BatchDetailModule(
//                                batchList1.get(i).batchNo,
//                                batchList1.get(i).batchQty,
//                                batchList1.get(i).itemCode
//                            )
//                        )
//                    }
//                    Log.e(
//                        "select_qty.",
//                        "" +
//                                this.newpickListDetail_Item_List.get(i).pickedQuantity
//                    )
//                    var uomCodel = "PCS"
//                    if (this.newpickListDetail_Item_List.get(i).uomCode != null &&
//                        !this.newpickListDetail_Item_List.get(i).uomCode.equals("null")
//                    ) {
//                        uomCodel = this.newpickListDetail_Item_List.get(i).uomCode.toString()
//                    } else {
//                        uomCodel = "PCS"
//                    }
//                    val picklistdetail = PostingPickDetails(
//                        this.newpickListDetail_Item_List.get(i).productCode,
//                        this.newpickListDetail_Item_List.get(i).productName,
//                        this.newpickListDetail_Item_List.get(i).quantity.toDouble(),
//                        this.newpickListDetail_Item_List.get(i).pickedQuantity,
//                        this.newpickListDetail_Item_List.get(i).pickedQuantity,
//                        locationCode!!,
//                        this.newpickListDetail_Item_List.get(i).remarks,
//                        this.newpickListDetail_Item_List.get(i).price,
//                        0.0,
//                        uomCodel,
//                        batchlist
//                        //newpickListDetail_Item_List.get(i).selectedBatchDetails
//                    )
//                    Picklists1.add(picklistdetail)
//
//                    Log.e(
//                        "savebatch",
//                        "$.. " + this.newpickListDetail_Item_List.get(i).selectedBatchDetails
//                    )
//                }
//                val postingPicklistmodel = PostingPickListRequest(
//                    CommonMethods.dateConvert(pickSoDate!!)!!,
//                    pickListNo!!,
//                    Picklists1,
//                    pickStatus!!,
//                    pickCode.toString()
//                )
//
//                savePicklist(postingPicklistmodel)
//
//            } else {
//                toast("No Product to save")
//            }
//
//            dialog.dismiss()
//        }
//        alertDialogBuilder.setNegativeButton(
//            "No"
//        ) { dialog, which -> dialog.dismiss() }
//        alertDialogBuilder.show()
//    }

    fun savePicklist1(jsonObj: SalesOrderNewModel) {
        // Initialize a new RequestQueue instance
        val requestQueue = Volley.newRequestQueue(this)
        //oct24 faisal given
        val url = Utils.getBaseUrl(this) + "SalesOrderDetails/SaveSalesOrder"

//        val URL = Constants.BASEURL + "PostingPickList/postingpicklist"
//        http://localhost:2009/api/purchase/InventoryCounting
        Log.w("picka_save_URL:",""+ url)
        Log.w("json_objj", ".." + jsonObj)

        CommonMethods.showProgressDialog(this)

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, null,
            Response.Listener { response: JSONObject ->

                Log.w("picksav_res:", response.toString())

                try {
//                    GlobalScope.launch {
//                        withContext(Dispatchers.Main) {
//
//                            Log.e("GRAsave_res:", response.toString())
                    val statusCode = response.optString("StatusCode")
                    val statusMsg = response.optString("StatusMessage")
//
                    if (statusCode == "1") {

                        val responseData = response.optJSONObject("ResponseData")!!
                        Log.w("savraa", "")
                        if (responseData.length() > 0) {
                            toast(statusMsg)
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                    } else {
                        val responseData = response.optJSONObject("ResponseData")!!
                        if (responseData.equals("null")) {
                            toast("ResponseData :null")
                        }
                        if (responseData.length() > 0) {
                            val errorMsg = responseData.optString("Error")
                            Log.w("errormsg...", "  " + errorMsg)
                           // showError_dialog(errorMsg)
                        }
                    }
                } catch (e: Exception) {
                    //   Log.e("Error_throwing11:", e.localizedMessage!!.toString())
                }
                CommonMethods.cancelProgressDialog()

            },
            Response.ErrorListener { error: VolleyError ->
                // Do something when error occurred
                Log.w("Error_throwing:", error.toString())
                toast("Error Response")

                CommonMethods.cancelProgressDialog()

            }) {
            override fun getBody(): ByteArray {
                return Gson().toJson(jsonObj).toString().toByteArray()
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                val creds =
                    String.format("%s:%s", Constants.API_SECRET_CODE, Constants.API_SECRET_PASSWORD)
                val auth = "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.DEFAULT)
                params["Authorization"] = auth
                return params
            }
        }
        jsonObjectRequest.retryPolicy = object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }

            @Throws(VolleyError::class)
            override fun retry(error: VolleyError) {
            }
        }
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest)
    }

    fun getSalesOrderPick(jsonObject: JSONObject) {
        // Initialize a new RequestQueue instance
        val requestQueue = Volley.newRequestQueue(this)
        val url = Utils.getBaseUrl(this) + "ProductList"
        // Initialize a new JsonArrayRequest instance
        Log.w("Given_SAP_PROUCT_URL:", url + jsonObject.toString())
       // productList = ArrayList()

//          SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//          pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//          pDialog.setTitleText("Loading Products...");
//          pDialog.setCancelable(false);
//          pDialog.show();

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            url,
            jsonObject,
            Response.Listener { response: JSONObject ->
                try {

                    Log.w("Res_picklist_detail:", response.toString())
                    // Loop through the array elements
                    val productArray = response.optJSONArray("responseData")
                    for (i in 0 until Objects.requireNonNull(productArray).length()) {
                        // Get current json object
                        val productObject = productArray.getJSONObject(i)
                        val product = ProductsModel()
                        if (productObject.optString("isActive") == "N") {
                            product.companyCode = "1"
                            // Adding bp name for products
                            if (productObject.optString("bP_Description") != null && !productObject.optString(
                                    "bP_Description"
                                ).isEmpty() && productObject.optString("bP_Description") != "null"
                            ) {
                                product.productName = productObject.optString("bP_Description")
                            } else {
                                product.productName = productObject.optString("productName")
                            }
                            if (productObject.optString("bP_CatalogNo") != null) {
                                product.customerItemCode = productObject.optString("bP_CatalogNo")
                            }
                            product.productCode = productObject.optString("productCode")
                            product.weight = ""
                            product.productImage = productObject.optString("imageURL")
                            product.wholeSalePrice = "0.00"
                            product.retailPrice = productObject.optDouble("retailPrice")
                            product.barcode = productObject.optString("barCode")
                            product.cartonPrice = productObject.optString("cartonPrice")
                            product.pcsPerCarton = productObject.optString("pcsPerCarton")
                            product.unitCost = productObject.optString("price")
                            product.lastPrice = productObject.optString("lastSalesPrice")
                            product.minimumSellingPrice =
                                productObject.optString("minimumSellingPrice")
                            product.defaultUom = productObject.optString("defaultSalesUOM")
                            if (productObject.optString("stockInHand") != "null") {
                                product.stockQty = productObject.optString("stockInHand")
                            } else {
                                product.stockQty = "0"
                            }
                            product.uomCode = productObject.optString("uomCode")
                            product.isItemFOC = productObject.optString("itemAllowFOC")
                            product.isBatch = productObject.optString("manageBatchOrSerial")

                            //  product.setProductBarcode(productObject.optString("BarCode")); Add values In Futue
                            // product.productBarcode = ""

                            /*  ArrayList<UomModel> uomList=new ArrayList<>();
                                JSONArray uomArray=productObject.optJSONArray("uomDetails");
                                if (uomArray!=null && uomArray.length() > 0){
                                    for (int j = 0; j< uomArray.length(); j++){
                                        JSONObject uomObject = uomArray.getJSONObject(j);
                                        UomModel uomModel =new UomModel();
                                        uomModel.setUomCode(uomObject.optString("uomCode"));
                                        uomModel.setUomName(uomObject.optString("uomName"));
                                        uomModel.setUomEntry(uomObject.optString("uomEntry"));
                                        uomModel.setAltQty(uomObject.optString("altQty"));
                                        uomModel.setBaseQty(uomObject.optString("baseQty"));

                                        uomList.add(uomModel);
                                    }
                                }
                                product.setUomText(uomList.toString());
                                product.setProductUOMList(uomList);*/

                            GoodReceiptProductAddActivity.productList!!.add(product)
                        }
                    }
                    HomePageModel.productsList = ArrayList()
//                    productList!!.get(0).barcode = "1234566"
//                    productList!!.get(1).barcode = "888801566968"
                 //   setAdapter(GoodReceiptProductAddActivity.productList)
                    HomePageModel.productsList.addAll(GoodReceiptProductAddActivity.productList!!)
                    // pDialog.dismiss();
                    if (GoodReceiptProductAddActivity.productList!!.size > 0) {
                        runOnUiThread {
                            AppUtils.setProductsList(GoodReceiptProductAddActivity.productList)

                            // setProductsDisplay("All Products")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.w("Errorn:", Objects.requireNonNull(e.message!!))
                }
            },
            Response.ErrorListener { error: VolleyError ->
                // Do something when error occurred
                // pDialog.dismiss();
                Log.w("Error_throwing:", error.toString())
            }) {
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                val creds =
                    String.format("%s:%s", Constants.API_SECRET_CODE, Constants.API_SECRET_PASSWORD)
                val auth = "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.DEFAULT)
                params["Authorization"] = auth
                return params
            }
        }
        jsonObjectRequest.setRetryPolicy(object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }

            @Throws(VolleyError::class)
            override fun retry(error: VolleyError) {
            }
        })
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest)
    }



    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
//    override fun apiResult(product_list: Any?) {
//        CommonMethods.cancelProgressDialog()
//
////        if (product_list is PickListDetailResponse) {
////            var picklistdetailResponse = product_list as PickListDetailResponse
////
////            if (picklistdetailResponse.statusCode == 1) {
////                if (picklistdetailResponse.responseData.isNotEmpty()) {
////
////                    CommonMethods.cancelProgressDialog()
//////                    pickListDetail_Item_List.clear()
//////                    pickListDetail_Item_List.addAll(product_list.responseData.get(0).pickListDetails)
//////                    pickListDetailAdapter!!.notifyDataSetChanged()
////                    //          setAdapter(product_list.responseData)
////
////                    order_datepickd!!.setText(picklistdetailResponse.responseData.get(0).pickDate)
////                    for (i in 0 until product_list.responseData.get(0).pickListDetails.size) {
////
////                        product_list.responseData.get(0).pickListDetails.get(i).pickedqQtyOld =
////                            product_list.responseData.get(0).pickListDetails.get(i).pickedQuantity
////                        product_list.responseData.get(0).pickListDetails.get(i).balQtyOld =
////                            product_list.responseData.get(0).pickListDetails.get(i).balance
////
////                        Log.e(
////                            "oqty",
////                            "" + product_list.responseData.get(0).pickListDetails.get(i).orderQty
////                        )
////                    }
////
//////                    doAsync {
//////                        for (i in 0 until product_list.responseData.get(0).pickListDetails.size) {
//////
//////                            pd_pickList = PD_PickList(
//////                                pickListDetail_Item_List.get(i).itemCode,
//////                                pickListDetail_Item_List.get(i).itemName,
//////                                pickListDetail_Item_List.get(i).pickedQuantity,
//////                                pickListDetail_Item_List.get(i).releaseQuantity,
//////                                pickListDetail_Item_List.get(i).uomCode,
//////                                pickListDetail_Item_List.get(i).balance,
//////                                pickListDetail_Item_List.get(i).orderQty,
//////                                pickListDetail_Item_List.get(i).docStatus,
//////                                pickListDetail_Item_List.get(i).stockInHand,
//////                            )
//////
//////                            val pd_pickLists = listOf(pd_pickList)
////////        Log.e("PDpickListDao.all",""+ pdDatabase.PDpickListDao().getAll());
//////
//////                            val database = PDDatabase.getInstance(context = this@PickListDetailActivity)
//////                            database.PDpickListDao().insertAll(pd_pickLists as List<PD_PickList>)
////
////                    //todo local db
//////                    val pdPicklists: ArrayList<PDPickList> = ArrayList<PDPickList>()
//////                    for (i in 0 until product_list.responseData.get(0).pickListDetails.size) {
//////                        val pdPicklistdetail = PDPickList(
//////                            pickListDetail_Item_List.get(i).itemCode,
//////                            pickListDetail_Item_List.get(i).itemName,
//////                            pickListDetail_Item_List.get(i).pickedQuantity,
//////                            pickListDetail_Item_List.get(i).releaseQuantity,
//////                            pickListDetail_Item_List.get(i).uomCode,
//////                            pickListDetail_Item_List.get(i).balance,
//////                            pickListDetail_Item_List.get(i).orderQty,
//////                            pickListDetail_Item_List.get(i).docStatus,
//////                            pickListDetail_Item_List.get(i).stockInHand,
//////                        )
//////                        pdPicklists.add(i, pdPicklistdetail)
//////                    }
////                    //  database!!.PDpickListDao().insertAll(pdPicklists as List<PDPickList>)
////                    // database.PDpickListDao().deleteAll()
////                    // setAdapter()
//////                    }
////
////                } else {
////                    toast(this, "no data found")
////                }
////            } else {
////                toast(this, product_list.statusMessage!!)
////            }
////
////        }
//        if (product_list is NewPickListDetailResponse) {
//            CommonMethods.cancelProgressDialog()
//
//            var newPickListDetailResponse = product_list as NewPickListDetailResponse
//
//            if (newPickListDetailResponse.statusCode == 1) {
//                if (newPickListDetailResponse.responseData.isNotEmpty()) {
//
//                    CommonMethods.cancelProgressDialog()
//                    this.newpickList_api_set_Item_List.clear()
//
//                    this.newpickList_api_set_Item_List.addAll(product_list.responseData.get(0).salesOrderDetails)
//                    Log.w("pickDetail_res_json", "${this.newpickList_api_set_Item_List}")
//                    Log.w("pickDetail_res11", Gson().toJson(newPickListDetailResponse))
//
//                    if (newSalesPickListDetailAdapter != null) {
//                        newSalesPickListDetailAdapter!!.notifyDataSetChanged()
//                        Log.e("pistatu_aaa", "$pickStatus")
//                        newSalesPickListDetailAdapter!!.updateStatus(pickStatus!!)
//                    }
//
//                    cust_namepickd!!.setText(
//                        newPickListDetailResponse.responseData.get(0).customerName
//                                + " - " +
//                                newPickListDetailResponse.responseData.get(0).customerCode
//                    )
//
//                    custCodeStr = newPickListDetailResponse.responseData.get(0).customerCode;
//
//                    pickSoTimel = newPickListDetailResponse.responseData.get(0).soDate + " - " +
//                            newPickListDetailResponse.responseData.get(0).time
//                    order_datepickd!!.setText(pickSoTimel)
//
//                    for (i in 0 until product_list.responseData.get(0).salesOrderDetails.size) {
//                      var  pickDetailItem_filter = newpickList_api_set_Item_List.get(i)
//
//                        if (pickDetailItem_filter.cartonQty != null
//                            && pickDetailItem_filter.cartonQty > 0) {
//                            //todo check carton qty
//                            pickDetailItem_filter.kgQtyStatus = pickDetailItem_filter.cartonQty
//                            pickDetailItem_filter.action = "no.of Carton: "
//                        }else{
//                            if (pickDetailItem_filter.pcsQty != null
//                                && pickDetailItem_filter.pcsQty > 0) {
//                                //todo check pcs qty
//                                pickDetailItem_filter.kgQtyStatus = pickDetailItem_filter.pcsQty
//                                pickDetailItem_filter.action = "no.of PCS: "
//                            }
//                            else{
//                                if (pickDetailItem_filter.palletQty != null && pickDetailItem_filter.palletQty.isNotEmpty()
//                                    && pickDetailItem_filter.palletQty.toDouble() > 0) {
//                                    //todo check pallet qty
//                                    pickDetailItem_filter.kgQtyStatus = 1.0
//                                    pickDetailItem_filter.action = "no.of Pallet: "
//                                }else {
//                                    pickDetailItem_filter.kgQtyStatus = 0.0
//                                    pickDetailItem_filter.action = "no.of Carton: "
//                                }
//                            }
//                        }
//
//                        if (newpickList_api_set_Item_List.get(i).frozen.equals("Yes")) {
////                            pickDetailItem_filter = newpickList_api_set_Item_List.get(i)
////                            cg
////                            pickDetailItem_filter!!.selectedBatchDetails = arrayListOf()
//
//                            if (pickDetailItem_filter.kgQtyStatus != null
//                                && pickDetailItem_filter.kgQtyStatus > 0) {
//                                if (!pickDetailItem_filter.selectedBatchDetails.size.equals(
//                                        pickDetailItem_filter.kgQtyStatus.toInt()
//                                    )
//                                ) {
//                                    val pendingBatch =
//                                        pickDetailItem_filter.kgQtyStatus - pickDetailItem_filter.selectedBatchDetails.size
//                                    Log.w("minuss", "" + pendingBatch)
//                                    var batchList: ArrayList<BatchDetailPickModule> = arrayListOf()
//
//                                    for (i in 0 until pendingBatch.toInt()) {
//                                        val batchPick = BatchDetailPickModule(
//                                            getRandomNo().toString(),
//                                            0.0,
//                                            pickDetailItem_filter.productCode,
//                                            pickDetailItem_filter.stockInHand.toDouble(),
//                                            "", i
//                                        )
//                                        batchList.add(batchPick)
//                                    }
//                                    pickDetailItem_filter.selectedBatchDetails.addAll(batchList)
//                                }
//                            } else {
//
//////                            pickDetailItem_filter = newpickList_api_set_Item_List.get(i)
////                                }
//                            }
//                        }else{
//                            pickDetailItem_filter.selectedBatchDetails = arrayListOf()
//                        }
//
//                        standardl_viewl!!.setVisibility(View.VISIBLE)
//                        dynamicl_viewl!!.setVisibility(View.GONE)
//                        newpickListDetail_Item_get_List = newpickList_api_set_Item_List.filter {
//                            it.frozen != "Yes"
//                        } as ArrayList<NewSalesOrderDetailItem>
//
//                        setAdapter(newpickListDetail_Item_get_List)
//
//
//                        Log.w("picklistStandApi", "" + newpickListDetail_Item_get_List.size)
//
//                        product_list.responseData.get(0).salesOrderDetails.get(i).pickedqQtyOld =
//                            product_list.responseData.get(0).salesOrderDetails.get(i).pickedQuantity.roundToInt()
//                        product_list.responseData.get(0).salesOrderDetails.get(i).balQtyOld =
//                            product_list.responseData.get(0).salesOrderDetails.get(i).balance
//
//                        Log.w(
//                            "oqty",
//                            "" + product_list.responseData.get(0).salesOrderDetails.get(i).quantity.toDouble()
//                        )
//                    }
//
//                } else {
//                    toast(this, product_list.statusMessage!!)
//                }
//            }
//
//        }
//
//        if (product_list is PostingPicklistResponse) {
//            CommonMethods.cancelProgressDialog()
//
//            var postingPicklistResponse = product_list as PostingPicklistResponse
//
//            if (postingPicklistResponse.statusCode == 1) {
////                if (postingPicklistResponse.responseData.isNotEmpty()) {
////
////                }
//                toast(this, product_list.statusMessage!!)
//                finish()
//
//            } else {
//                toast(this, product_list.statusMessage!!)
//            }
//        }
//
//    }

    private fun showRemarkdialog(pos: Int, pickList: ArrayList<NewSalesOrderDetailItem>, action:String) {
        val li = LayoutInflater.from(this)
        val promptsView: View = li.inflate(R.layout.dialog_remark, null)

        val alertDialogBuilder = AlertDialog.Builder(this)
        val remark_qty = promptsView.findViewById<View>(R.id.remark_pick_dial) as EditText
        val save_pickR = promptsView.findViewById<View>(R.id.save_remark_pick) as Button
        val closePickR = promptsView.findViewById<View>(R.id.closebt_dial) as ImageView
        val spinnerRemarkl = promptsView.findViewById<View>(R.id.spinnerRemark) as Spinner
        val spinner_remarklay = promptsView.findViewById<View>(R.id.spinner_remark_lay) as RelativeLayout
        val remarklay = promptsView.findViewById<View>(R.id.remarkLayl) as LinearLayout

        alertDialogBuilder.setView(promptsView)
        val dialog: Dialog = alertDialogBuilder.create()
        dialog.setCancelable(false)
        dialog.show()

        val status = arrayOf("Select Remarks", "No stock", "Expired stock" , "Insufficient stock" , "Damaged", "Other")
        val adapter = ArrayAdapter<CharSequence>(this, R.layout.cust_spinner_item, status)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        spinnerRemarkl.setAdapter(adapter)

        palletremarkStr = spinnerRemarkl.getSelectedItem().toString()

        if(action.equals("Standard")){
            spinner_remarklay.visibility = View.VISIBLE
            remarklay.visibility = View.GONE
//
            if (pickList.get(pos).remarks != null &&
                ! pickList.get(pos).remarks.equals("")) {
                palletremarkStr = pickList.get(pos).remarks
                spinnerRemarkl.setSelection(status.indexOf(palletremarkStr))

//                palletremarkStr.equals(listView1.getAdapter().getItem(i) as String)
//                if(adapter.getFilter().filter(palletremarkStr)) {
//                    spinnerRemarkl.setAdapter(adapter)
//                }
            }else{
                spinnerRemarkl.setAdapter(adapter)
            }
        }
        if(action.equals("Dynamic")){
            spinner_remarklay.visibility = View.GONE
            remarklay.visibility = View.VISIBLE

            if (pickList.get(pos).palletRemarks != null) {
                remarkStr = pickList.get(pos).palletRemarks
                remark_qty.setText(remarkStr.toString())
            }
        }

        if(remark_qty.text.toString().isNotEmpty()){
            remark_qty.setSelection(remark_qty.text.length)
            remark_qty.setSelectAllOnFocus(true)
        }
        else{
            remark_qty.requestFocus()
        }

        closePickR.setOnClickListener() {
            dialog.dismiss()
        }

        save_pickR.setOnClickListener {

            if(action.equals("Standard")){
                if (!spinnerRemarkl.getSelectedItem().toString().equals("Select Remarks")) {
                    //update
                    pickList.get(pos).remarks = spinnerRemarkl.getSelectedItem().toString()
                }
                else {
                    pickList.get(pos).remarks = ""
                }
                pickList.get(pos).palletRemarks = ""
                Log.w("palletremm22..",""+spinnerRemarkl.getSelectedItem().toString());
            }
            if(action.equals("Dynamic")){
                if (remark_qty.text.toString().isNotEmpty()) {
                    //update
                    pickList.get(pos).palletRemarks = remark_qty.text.toString()
                }
                else {
                    pickList.get(pos).palletRemarks = ""
                }
              //  pickList.get(pos).remarks = "O"
                Log.w("palletremm11..","");
            }
            Log.w("palletremm..","action.. "+action+".."+pickList.get(pos).remarks+pickList.get(pos).palletRemarks)
            dialog.dismiss()
            if (newSalesPickListDetailAdapter != null) {
                newSalesPickListDetailAdapter!!.notifyDataSetChanged()
            }

        }

    }

    private fun showProductdialog(
        pos: Int,
        totalQty: Int,
        pickedQty: Double,
        pdtname1: String,
        pickList: ArrayList<NewSalesOrderDetailItem>
    ) {
        val li = LayoutInflater.from(this)
        val promptsView: View = li.inflate(R.layout.dialog_qty, null)

        val alertDialogBuilder = AlertDialog.Builder(this)
        val totalqty = promptsView.findViewById<View>(R.id.dialog_Tot_qty) as TextView
        val pdtname = promptsView.findViewById<View>(R.id.pdtname_dial) as TextView
        val ed_qty = promptsView.findViewById<View>(R.id.qty_ed) as EditText
        val save_pick = promptsView.findViewById<View>(R.id.save_dailog_pick) as Button
        val closePick = promptsView.findViewById<View>(R.id.closebt_dial) as ImageView

        pdtname.setText(pdtname1)

        val position: Int = ed_qty.length()
        val etext: Editable = ed_qty.getText()
        Log.e("dialll", "" + dialog_pickqty)

        if (!dialog_pickqty.toString().equals("0")) {
            //  Selection.setSelection(etext, position)
            ed_qty.setSelection(ed_qty.getText().length);
            ed_qty.setSelectAllOnFocus(true);
            ed_qty.setText(Utils.twoDecimalPoint(dialog_pickqty!!).toString())
        }

        totalqty.setText(dialog_Oqty.toString())
        alertDialogBuilder.setView(promptsView)
        val dialog: Dialog = alertDialogBuilder.create()
        dialog.setCancelable(true)
        dialog.show()

        closePick.setOnClickListener() {
            dialog.dismiss()
        }

        save_pick.setOnClickListener {
            val edtqty: String = ed_qty.getText().toString()
            Log.w("edttqtyact", "" + ed_qty.text.toString())

            if (ed_qty.text.toString().isNotEmpty()) {
                if (pickList.get(pos).quantity >= ed_qty.text.toString()
                        .toDouble()
                ) {
                    if (pickList.get(pos).stockInHand >= ed_qty.text.toString()
                            .toDouble()
                    ) {

                        //update
                        pickList.get(pos).pickedQuantity =
                            ed_qty.text.toString().toDouble()
                        var bal = pickList.get(pos).quantity - ed_qty.text.toString().toDouble()

                        pickList.get(pos).balance = bal.toInt()

                        newSalesPickListDetailAdapter!!.notifyDataSetChanged()
                        toast("pick quantity added")
                        dialog.dismiss()
                    } else {
                        Toast.makeText(
                            this,
                            "Available stock : " + this.newpickList_api_set_Item_List.get(position!!).stockInHand,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        this,
                        "Entered value greater than total order qty ",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }


            } else {
                toast("Enter order qty")
            }


//                if (newpickListDetail_Item_List.get(position!!).stockInHand <= ed_qty.text.toString()
//                        .toInt()) {
//                    Toast.makeText(
//                        this,
//                        "Avaiable Stock : "+newpickListDetail_Item_List.get(position!!).stockInHand,
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                }
            if (newSalesPickListDetailAdapter != null) {
                newSalesPickListDetailAdapter!!.notifyDataSetChanged()
            }

        }
//        save_pick.setOnClickListener {
//            val qtytxt: Int = qtyedit.getText().toString().toInt()
//            val itemprice = 10.0
//            display((qtytxt + 1).toString(), itemprice)
//        }
    }

    override fun pickListDetailRemark(
        pickList: ArrayList<NewSalesOrderDetailItem>,
        position: Int?, action: String
    ) {
        showRemarkdialog(position!!, pickList,action)
    }

    override fun pickListDetailSelected(
        pickList: ArrayList<NewSalesOrderDetailItem>,
        position: Int?
    ) {

        dialog_Oqty = pickList.get(position!!).quantity.roundToInt()
        dialog_pickqty =
            pickList.get(position).pickedQuantity.toDouble()
        dialog_pdtname = pickList.get(position).productName
        itemBatchlist = pickList.get(position);
        Log.w("pos_picll..", " $position")

        showProductdialog(position, dialog_Oqty!!, dialog_pickqty!!, dialog_pdtname!!, pickList)
    }


    override fun pickListDeleteSelected(position: Int?) {
        showdialog_delete(position)
    }

    override fun pickListCheckboxSelected(position: Int?) {

        //update
        if (this.newpickList_api_set_Item_List.get(position!!).isCheckboxSelect) {
            this.newpickList_api_set_Item_List.get(position!!).pickedQuantity =
                this.newpickList_api_set_Item_List.get(position!!).quantity.toDouble()
            this.newpickList_api_set_Item_List.get(position!!).balance = 0
            if (newSalesPickListDetailAdapter != null) {
                newSalesPickListDetailAdapter!!.notifyDataSetChanged()
            }
        } else {
            this.newpickList_api_set_Item_List.get(position!!).pickedQuantity =
                this.newpickList_api_set_Item_List.get(position!!).pickedqQtyOld.toDouble()
            this.newpickList_api_set_Item_List.get(position!!).balance =
                this.newpickList_api_set_Item_List.get(position!!).balQtyOld

            newSalesPickListDetailAdapter!!.notifyDataSetChanged()

            //  pickListDetailAdapter!!.notifyDataSetChanged()
        }
    }

    override fun pickEditSelected(position: Int?, pickqty: Int?) {

        if (position != -1) {
            this.newpickList_api_set_Item_List.get(position!!).pickedQuantity = pickqty!!.toDouble()
            this.newpickList_api_set_Item_List.get(position).balance =
                (this.newpickList_api_set_Item_List.get(position).quantity - pickqty.toDouble()).roundToInt()

            Log.e(
                "pickedit..",
                "" + pickqty!! + " $position"
            )
            if (newSalesPickListDetailAdapter!!.hasStableIds()) {
                newSalesPickListDetailAdapter!!.notifyDataSetChanged()
            }
        }
    }

    //    @SuppressLint("NotifyDataSetChanged")
//    private fun showBatchKG_Dialog(item: ArrayList<NewSalesOrderDetailItem>, position: Int?) {
//        val saveBtn: TextView
//        val stockTxt: TextView
//        val searchTxt: EditText
//        val searchImg: ImageView
//        val closeBtn: ImageView
//        val cqtyTextWatcher: TextWatcher?
//
//        filterBatch = arrayListOf()
//        filterBatchNew = arrayListOf()
//
//        val li = LayoutInflater.from(this)
//        val view: View = li.inflate(R.layout.picklist_kg_batch_dialog, null)
//
//        val alertDialogBuilder = AlertDialog.Builder(this)
//
//        ctn_qty_pick_ed = view.findViewById(R.id.ctn_qty_pick)
//        addKg_edLayl = view.findViewById(R.id.addKg_edLay)
//        kg_txt_pickl = view.findViewById(R.id.kg_txt_pick)
//        save_picklistl = view.findViewById(R.id.add_picklist)
//        unit_price_pickl = view.findViewById(R.id.unit_price_pick)
//        dailogKgLayl = view.findViewById(R.id.dailogKgLay)
////        total_pick_kg_txtl = view.findViewById(R.id.total_pick_kg_txt)
//        pdtname_dialKg = view.findViewById(R.id.pdtName_pickdial)
//        closeBtn = view.findViewById(R.id.cancel_pickdial)
//        kg_addBtn = view.findViewById(R.id.addImg_kg)
//        scan_batchImg = view.findViewById(R.id.barcode_batch_Scan)
//        searchTxt = view.findViewById(R.id.search_pick_batch)
//        searchImg = view.findViewById(R.id.search_batchImg)
//        stockTxt = view.findViewById(R.id.stock_pick_txt)
//        empty_txt_batch = view.findViewById(R.id.empty_txt_batch)
//        batchsize_item = view.findViewById(R.id.batchsize_item)
//        batch_rv = view.findViewById(R.id.rv_batch)
//
//        filterBatch = item.get(position!!).selectedBatchDetails ?: arrayListOf()
//        Log.w("itemp13", ".. " + item.get(position!!).selectedBatchDetails)
//
//        if (filterBatch.size > 0) {
//            setBatchAdapter(filterBatch)
//
//        } else {
//            if (item.get(position!!).cartonQty != null && item.get(position!!).cartonQty > 0) {
//                for (i in 0 until item.get(position!!).cartonQty.toInt()) {
//
//                    filterBatch.add(
//                        BatchDetailPickModule(
//                            "",
//                            item.get(position!!).stockInHand.toDouble(),
//                            "",
//                            0.0,
//                            "", i
//                        )
//                    )
//                }
//                setBatchAdapter(filterBatch)
//            }
//
//        }
//
////        if (filterBatch.size > 0) {
////            setBatchAdapter(filterBatch)
////
////            Log.w("itemp", ".. " + filterBatch.size+".."+item.pickedQuantity)
////        }
//        unit_price_pickl!!.setText(item.get(position!!).price.toString())
//        ctn_qty_pick_ed!!.setText(item.get(position!!).cartonQty.toString())
//        pdtname_dialKg!!.setText(item.get(position!!).productName)
//        stockTxt!!.setText(item.get(position!!).stockInHand.toString())
//
//        kg_txt_pickl!!.setText(Utils.twoDecimalPoint(item.get(position!!).pickedQuantity).toString())
//
//        closeBtn.setOnClickListener {
//            dialog!!.dismiss()
//            filterBatch.clear()
//        }
//        Log.e("scaapi", "$position")
//
//        scan_batchImg!!.setOnClickListener {
//            scanType = "batch_item"
//
//            if (checkPermission())
//                scanFromFragment()
//        }
////        if(ctn_qty_pick_ed!!.text.toString().isNotEmpty()){
////            calcKg(ctn_qty_pick_ed!!.text.toString())
////        }
////        cqtyTextWatcher = object : TextWatcher {
////            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
////                addKg_edLayl!!.removeAllViews()
////                clearValue()
////            }
////            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
////            override fun afterTextChanged(s: Editable) {
////                if (s.toString().isNotEmpty()) {
////                    calcKg(s.toString())
////                }
////                else
////                {
////                    addKg_edLayl!!.removeAllViews()
////                    clearValue()
////                }
////            }
////        }
////        ctn_qty_pick_ed!!.addTextChangedListener(cqtyTextWatcher)
//
//
//        kg_addBtn!!.setOnClickListener() {
//            kg_edittxt(myEditTextList, item.get(position), dialog!!)
//            hideKeyboard(this, dailogKgLayl!!)
//            isDialAdd = false
//        }
//        save_picklistl!!.setOnClickListener {
//            kg_edittxt(myEditTextList, item.get(position), dialog!!)
//
//
//            if (batchListAdapter != null) {
//                item.get(position!!).selectedBatchDetails = batchListAdapter!!.getBatchDataList()
//            }
//            if (newSalesPickListDetailAdapter != null) {
//                newSalesPickListDetailAdapter!!.notifyDataSetChanged()
//            }
//            dialog!!.dismiss()
//
////            if (isDialAddEmpty!!) {
////                dialog!!.dismiss()
////            }
//
//            //   isDialAdd = true
//        }
//
//        alertDialogBuilder.setView(view)
//        dialog = alertDialogBuilder.create()
//        dialog!!.setCancelable(false)
//        dialog!!.show()
//    }

    @POST("SalesOrderDetails/salesorder")


    @SuppressLint("NotifyDataSetChanged")
    private fun showBatchKG_Dialog(item: NewSalesOrderDetailItem, position: Int?) {
        val saveBtn: TextView
        val stockTxt: TextView
        val searchTxt: EditText
        val searchImg: ImageView
        val closeBtn: ImageView
        val cqtyTextWatcher: TextWatcher?

//        filterBatch = arrayListOf()
        filterBatchNew = arrayListOf()

        val li = LayoutInflater.from(this)
        val view: View = li.inflate(R.layout.picklist_kg_batch_dialog, null)

        val alertDialogBuilder = AlertDialog.Builder(this)

        ctn_qty_pick_ed = view.findViewById(R.id.ctn_qty_pick)
        ctn_qty_tittlel = view.findViewById(R.id.ctn_qty_tittle)
        addKg_edLayl = view.findViewById(R.id.addKg_edLay)
        kg_txt_pickl = view.findViewById(R.id.kg_txt_pick)
        save_batchl = view.findViewById(R.id.add_picklist)
        unit_price_pickl = view.findViewById(R.id.unit_price_pick)
        dailogKgLayl = view.findViewById(R.id.dailogKgLay)
//        total_pick_kg_txtl = view.findViewById(R.id.total_pick_kg_txt)
        pdtname_dialKg = view.findViewById(R.id.pdtName_pickdial)
        closeBtn = view.findViewById(R.id.cancel_pickdial)
        kg_addBtn = view.findViewById(R.id.addImg_kg)
        scan_batchImg = view.findViewById(R.id.barcode_batch_Scan)
        searchTxt = view.findViewById(R.id.search_pick_batch)
        searchImg = view.findViewById(R.id.search_batchImg)
        stockTxt = view.findViewById(R.id.stock_pick_txt)
        empty_txt_batch = view.findViewById(R.id.empty_txt_batch)
        batchsize_item = view.findViewById(R.id.batchsize_item)
        batch_rv = view.findViewById(R.id.rv_batch)

        val filterBatch = item.selectedBatchDetails ?: arrayListOf()
        Log.w("itemp13", ".. " + item.selectedBatchDetails)

//        if (item.pcsQty > 0.0) {
//            ctn_qty_tittlel!!.setText("no.of PCS")
//        }else{
//            ctn_qty_tittlel!!.setText("no.of Carton")
//        }
       // ctn_qty_pick_ed!!.setText(item.cartonQty.toString())

        ctn_qty_tittlel!!.setText(item.action)
        if (item.pcsQty > 0) {
            ctn_qty_pick_ed!!.setText(Utils.twoDecimalPoint(item.pcsQty))
        }else {
            if (item.cartonQty > 0) {
                ctn_qty_pick_ed!!.setText(Utils.twoDecimalPoint(item.cartonQty))
            } else {
                if (item.palletQty.isNotEmpty() && item.palletQty.toDouble() > 0) {
                    ctn_qty_pick_ed!!.setText(Utils.twoDecimalPoint(item.palletQty.toDouble()))
                } else {
                    ctn_qty_pick_ed!!.setText(Utils.twoDecimalPoint(item.cartonQty))
                }
            }
        }
//        if (filterBatch.size > 0) {
//            if (item.cartonQty != null && item.cartonQty > 0) {
//                if (filterBatch.size.equals(item.cartonQty.toInt())) {
//                    setBatchAdapter(filterBatch)
//                } else {
//                    var minusCarton = filterBatch.size - item.cartonQty
//                    Log.w("minuss",""+minusCarton)
//                    for (i in 0 until minusCarton.roundToInt()) {
//                        filterBatch.add(
//                            BatchDetailPickModule(
//                                "",
//                                0.0,
//                                "",
//                                item.stockInHand.toDouble(),
//                                "", i
//                            )
//                        )
//                    }
//                    setBatchAdapter(filterBatch)
//                }
//            }
        //        } else {
//            item.cartonQty = 10.0
//            if (item.cartonQty != null && item.cartonQty > 0) {
//                for (i in 0 until item.cartonQty.toInt()) {
//                    filterBatch.add(
//                        BatchDetailPickModule(
//                            "",
//                            0.0,
//                            "",
//                            item.stockInHand.toDouble(),
//                            "", i
//                        )
//                    )
//                }
//                setBatchAdapter(filterBatch)
//            }
//        }
        setBatchAdapter(filterBatch)

//        if (filterBatch.size > 0) {
//            setBatchAdapter(filterBatch)
//
//            Log.w("itemp", ".. " + filterBatch.size+".."+item.pickedQuantity)
//        }
        unit_price_pickl!!.setText(item.price.toString())

        pdtname_dialKg!!.setText(item.productName)
        stockTxt!!.setText(item.stockInHand.toString())

     //   kg_txt_pickl!!.setText(Utils.twoDecimalPoint(item.pickedQuantity).toString())

        closeBtn.setOnClickListener {
            showCloseDialog()
        }
        Log.e("scaapi", "$position")

        scan_batchImg!!.setOnClickListener {
            scanType = "batch_item"

            if (checkPermission())
                scanFromFragment()
        }
//        if(ctn_qty_pick_ed!!.text.toString().isNotEmpty()){
//            calcKg(ctn_qty_pick_ed!!.text.toString())
//        }
//        cqtyTextWatcher = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//                addKg_edLayl!!.removeAllViews()
//                clearValue()
//            }
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//            override fun afterTextChanged(s: Editable) {
//                if (s.toString().isNotEmpty()) { ==//                }
//                else
//                {
//                    addKg_edLayl!!.removeAllViews()
//                    clearValue()
//                }
//            }
//        }
//        ctn_qty_pick_ed!!.addTextChangedListener(cqtyTextWatcher)


        kg_addBtn!!.setOnClickListener() {
            kg_edittxt(myEditTextList, item, dialog!!)
            hideKeyboard(this, dailogKgLayl!!)
            isDialAdd = false
        }
        save_batchl!!.setOnClickListener {
         //   kg_edittxt(myEditTextList, item, dialog!!)

            var remarkStrA = "";
            if (batchListAdapter != null) {
                item.selectedBatchDetails = batchListAdapter!!.getBatchDataList()
                newpickListDetail_Item_get_List[position!!] = item
                var joinQtyVal: String = ""

                var fiterList = item.selectedBatchDetails!!.filter { it.batchQty != 0.0 }
                if(fiterList.size > 0){
                for (i in  fiterList.indices) {
                    joinQtyVal = fiterList.joinToString(separator = "+") { it -> "${it.batchQty}" }
        //            joinQtyVal = stringBuffer.toString()
//                    stringBuffer.append("+")
//                    stringBuffer.append(fiterList.get(i).batchQty)
                }

                    Log.w("joinqtyVal",".."+joinQtyVal)
                    item.joinBatchQty = joinQtyVal
                }
                else{
                    item.pickedQuantity = 1.0
                    item.joinBatchQty = ""
                }
                Log.w("joinqtyValitem",".."+item.joinBatchQty)

                //todo
                if(item.selectedBatchDetails.size.equals(fiterList.size)){
                    item.remarks = "OW"
                    Log.w("itemRemarkkk",""+ item.remarks)

                }else{
                    item.remarks = "O"
                    Log.w("itemRemarkkk22",""+ item.remarks)
                }

            }
            if (newSalesPickListDetailAdapter != null) {
                newSalesPickListDetailAdapter!!.notifyDataSetChanged()
            }
            dialog!!.dismiss()

//            if (isDialAddEmpty!!) {
//                dialog!!.dismiss()
//            }
            //   isDialAdd = true
        }

        alertDialogBuilder.setView(view)
        dialog = alertDialogBuilder.create()
        dialog!!.setCancelable(false)
        dialog!!.show()
    }
    fun showCloseDialog() {
        val builder = android.app.AlertDialog.Builder(this@PickListDetailActivity)
        builder.setCancelable(false)
        builder.setTitle("Warning..!")
        builder.setMessage("All Data Will be Cleared are you sure want to back ?")
        builder.setPositiveButton("YES") { dialogInterface: DialogInterface, i: Int ->
            dialog!!.dismiss()
        }
        builder.setNegativeButton(
            "NO"
        ) { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
        //  getActivity().getWindow().setBackgroundDrawableResource(R.color.primaryDark);
    }

    @SuppressLint("NotifyDataSetChanged")
//    private fun showKG_Dialog1(item: NewSalesOrderDetailItem, position: Int?) {
//
//        val batchLay: LinearLayout
//        val batchAdd: ImageView
//        val saveBtn: TextView
//        val stockTxt: TextView
//        val searchTxt: EditText
//        val searchImg: ImageView
//        val closeBtn: ImageView
//        val cqtyTextWatcher: TextWatcher?
//
//        filterBatch = arrayListOf()
//
//        val li = LayoutInflater.from(this)
//        val view: View = li.inflate(R.layout.picklist_kg_batch_dialog, null)
//
//        val alertDialogBuilder = AlertDialog.Builder(this)
//
//        ctn_qty_pick_ed = view.findViewById(R.id.ctn_qty_pick)
//        addKg_edLayl = view.findViewById(R.id.addKg_edLay)
//        kg_txt_pickl = view.findViewById(R.id.kg_txt_pick)
//        save_picklistl = view.findViewById(R.id.add_picklist)
//        unit_price_pickl = view.findViewById(R.id.unit_price_pick)
//        dailogKgLayl = view.findViewById(R.id.dailogKgLay)
////        total_pick_kg_txtl = view.findViewById(R.id.total_pick_kg_txt)
//        pdtname_dialKg = view.findViewById(R.id.pdtName_pickdial)
//        closeBtn = view.findViewById(R.id.cancel_pickdial)
//        kg_addBtn = view.findViewById(R.id.addImg_kg)
//        scan_batchImg = view.findViewById(R.id.barcode_batch_Scan)
//        searchTxt = view.findViewById(R.id.search_pick_batch)
//        searchImg = view.findViewById(R.id.search_batchImg)
//        stockTxt = view.findViewById(R.id.stock_pick_txt)
//        empty_txt_batch = view.findViewById(R.id.empty_txt_batch)
//        batchsize_item = view.findViewById(R.id.batchsize_item)
//
//        batch_rv = view.findViewById(R.id.rv_batch)
//        filterBatch = item.batchDetails ?: arrayListOf()
//        Log.w("itempqq", ".. " + filterBatch.size + ".." + item.pickedQuantity)
//
//        if (filterBatch.size > 0) {
//            setBatchAdapter(filterBatch)
//        }
//        unit_price_pickl!!.setText(item.price.toString())
//        ctn_qty_pick_ed!!.setText(item.cartonQty.toString())
//        pdtname_dialKg!!.setText(item.productName)
//        stockTxt!!.setText(item.stockInHand.toString())
//
//        kg_txt_pickl!!.setText(Utils.twoDecimalPoint(item.pickedQuantity).toString())
//
//        alertDialogBuilder.setView(view)
//        dialog = alertDialogBuilder.create()
//        dialog!!.setCancelable(false)
//        if (!dialog!!.isShowing)
//            dialog!!.show()
//
//        closeBtn.setOnClickListener {
//            dialog!!.dismiss()
//            // filterBatch.clear()
//        }
//        Log.e("scaapi", "$position")
//
//        scan_batchImg!!.setOnClickListener {
//            if (checkPermission())
//                scanFromFragment()
//
//            scanType = "batch_item"
//        }
//
////        if(ctn_qty_pick_ed!!.text.toString().isNotEmpty()){
////            calcKg(ctn_qty_pick_ed!!.text.toString())
////        }
////        cqtyTextWatcher = object : TextWatcher {
////            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
////                addKg_edLayl!!.removeAllViews()
////                clearValue()
////            }
////            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
////            override fun afterTextChanged(s: Editable) {
////                if (s.toString().isNotEmpty()) {
////                    calcKg(s.toString())
////                }
////                else
////                {
////                    addKg_edLayl!!.removeAllViews()
////                    clearValue()
////                }
////            }
////        }
////        ctn_qty_pick_ed!!.addTextChangedListener(cqtyTextWatcher)
//
//
//        kg_addBtn!!.setOnClickListener() {
//            kg_edittxt(myEditTextList, item, dialog!!)
//            hideKeyboard(this, dailogKgLayl!!)
//            isDialAdd = false
//        }
//        save_picklistl!!.setOnClickListener {
//            kg_edittxt(myEditTextList, item, dialog!!)
//
//            if (newSalesPickListDetailAdapter != null) {
//                newSalesPickListDetailAdapter!!.notifyDataSetChanged()
//            }
//            if (isDialAddEmpty!!) {
//                dialog!!.dismiss()
//            }
//
//            isDialAdd = true
//        }
//
////        fun searchFilter(batchNoStr: String , arrayList: ArrayList<BatchDetailPickModule>) {
////            //new array list that will hold the filtered data
////            var isSearched = false
////            var currentIndex = 0;
////            val filterBatch = ArrayList<BatchDetailPickModule>()
////
////            //looping through existing elements
////            //   for (ProductsModel s : selectProductAdapter.getProductsList()) {
////            for ((index, prod) in arrayList.withIndex()) {
////
//////        for (prod in poScanList) {
////                //if the existing elements contains the search input
////                Log.e("scan_sizePo", "" + prod.itemCode + " size.. " + arrayList.size)
////
////
////                if ((prod.itemCode!!.lowercase() == batchNoStr.lowercase())) {
////                    //adding the element to filtered list
////                    filterBatch.add(prod)
////
////
////                } else {
//////                itemCodeEd!!.clearFocus()
//////                itemCodeEd!!.setText("")
////
////                    batchList!!.clear()
//////                    itemSize!!.visibility = View.GONE\
////                    toast("No Product Found")
////
////                    Log.e("batchll", "  empty")
////                    //listEmptyTxt()
////                }
////
//////                }
//////                else{
//////                    emptyPdt!!.visibility = View.VISIBLE
//////                    itemSize!!.visibility = View.GONE
//////                    poScanAdd_ListView!!.visibility = View.GONE
//////                    toast("Item not found in this PO number !")
//////                }
////            }
////
////            if (isSearched) {
////                poScanAdd_ListView!!.smoothScrollToPosition(currentIndex)
////            }
////
////            Log.e("filterpo", "" + filterBatch.size)
////            if (filterBatch.isEmpty()) {
//////                hideKeyboard(this@PickListDetailActivity, mainlayl!!)
////            }
////        }
//
//
//        val prodQty = item.quantity.toDouble()
//
//        save_picklistl!!.setOnClickListener {
//            if (batchListAdapter != null) {
//                if (batchListAdapter!!.getBatchDataList() != null) {
//                    itemBatchlist.selectedBatchDetails = batchListAdapter!!.getBatchDataList()
//                    dialog!!.dismiss()
//                }
//            }
//        }
//        searchImg!!.setOnClickListener {
//            if (itemBatchlist.cartonQty != 0.0 && itemBatchlist.cartonQty >= filterBatch.size) {
////            if(2  > filterBatch.size) {
//                Log.e("adasize11..  ", ".. " + filterBatch.size + " ..." + itemBatchlist.cartonQty)
//                searchFilter(searchTxt.text.toString(), item)
//            } else {
//                toast("Check no. of carton! ")
//            }
//
//        }
//
//        dialog!!.getWindow()!!
//            .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
//        dialog!!.getWindow()!!
//            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
////        dialog.window!!.setLayout(1000, 1000)
//    }

    @Throws(JSONException::class)
    fun getInvoiceDetails(invoiceNumber: String) {
        // Initialize a new RequestQueue instance
        val jsonObject = JSONObject()
        ///jsonObject.put("CompanyCode",companyId);
        jsonObject.put("InvoiceNo", invoiceNumber)
        jsonObject.put("LocationCode", locationCode)
        val requestQueue = Volley.newRequestQueue(this)

        val url = Utils.getBaseUrl(this) + "SalesOrderDetails/salesorder"
        // Initialize a new JsonArrayRequest instance
        Log.w("picklist_Detail", "" + url + jsonObject)

        pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"))
        pDialog.setTitleText("Getting Invoice Details...")
        pDialog.setCancelable(false)
        pDialog.show()
        newpickListDetail_Item_get_List = arrayListOf()

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, jsonObject,
            Response.Listener { response: JSONObject ->
                try {
                    Log.w("picklist SAP:", response.toString())
//                    if (response.length() > 0) {
//
//                        val statusCode = response.optString("statusCode")
//                        if (statusCode == "1") {
//                            val salesArray = response.optJSONArray("responseData")
//                            val salesObject = salesArray.optJSONObject(0)
//                            val invoice_number = salesObject.optString("invoiceNumber")
//
//                            val products = salesObject.getJSONArray("salesOrderDetails")
//                            for (i in 0 until products.length()) {
//                                val objPdt: JSONObject = products.optJSONObject(i)
//                                val model = NewSalesOrderDetailItem()
//
//                                model.customerName = objPdt.optString("customerName")
//
//                                var lqty: String? = "0.0"
//                                var cqty: String? = "0.0"
//                                if (`object`.optString("unitQty") != "null") {
//                                    lqty = `object`.optString("unitQty")
//                                }
//                                if (`object`.optString("quantity") != "null") {
//                                    cqty = `object`.optString("quantity")
//                                }
//                                val actualPrice =
//                                    `object`.optString("unitPrice").toDouble()
//                            }
//                        }
//                    }
                    pDialog.dismiss()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                // Do something when error occurred
                pDialog.dismiss()
                Log.w("Error_throwing:", error.toString())
            }) {
            override fun getHeaders(): Map<String, String> {
                val params = java.util.HashMap<String, String>()
                val creds =
                    String.format("%s:%s", Constants.API_SECRET_CODE, Constants.API_SECRET_PASSWORD)
                val auth = "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.DEFAULT)
                params["Authorization"] = auth
                return params
            }
        }
        jsonObjectRequest.setRetryPolicy(object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }

            @Throws(VolleyError::class)
            override fun retry(error: VolleyError) {
            }
        })
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest)
    }


    fun searchFilter(batchNo: String, item: NewSalesOrderDetailItem) {

        val prodQty = item.quantity.toDouble()
        Log.w("cg_prodqtypick ", prodQty.toString())
        var isSearched = false
        var currentIndex = 0
        var batchlist1: ArrayList<BatchDetailPickModule> = arrayListOf()
//        val batchlist1 = ArrayList(item.selectedBatchDetails.map {
//            it.copy()
//        })
        if (batchListAdapter != null) {
            batchlist1 = batchListAdapter!!.getBatchDataList()
//                .map {
//                it.copy()
//            })
        }
        Log.w("cg_batclss", "" + batchlist1.size + " $batchNo  ")

        for ((index, model) in batchlist1.withIndex()) {
            isSearched = true
            currentIndex = index
            Log.w("pos_batchqty", "" + model.batchPos)
            Log.w("pos_batchqty1", "" + model.batchQty!!)

//            if (model.batchQty!! == 0.0) {
//                batchListAdapter!!.showSelection(index)
//                break
//            }
//                batchListAdapter!!.updateQty(batchNo, true,)
        }

//        for ((index, model) in batchlist1.withIndex()) {
//            Log.e("batclmodel", "" + model.batchNo)
//
//            if (model.batchNo!! == batchNo) {
//                if (filterBatch.any { it.batchNo == batchNo }) {
//                    isSearched = true
//                    toast("Product already exist!")
//                    Log.e("itemp33", ".. " + filterBatch.size)
//                    batchListAdapter!!.updateQty(batchNo, true)
//                    break
//                } else {
//                    filterBatch.add(model)
//                    Log.e("itemp55", ".. " + filterBatch.size)
//

//                    batch_rv!!.visibility = View.VISIBLE
//                    empty_txt_batch!!.visibility = View.GONE
//                    batchsize_item!!.visibility = View.VISIBLE
//                    setBatchAdapter(filterBatch)
//
//                    if (batchListAdapter != null) {
//                        batchListAdapter!!.updateList(filterBatch)
//                        batchListAdapter!!.updateQty(batchNo, true)
//                        isSearched = true
//                        currentIndex = index
//                    } else {
//                        setBatchAdapter(filterBatch)
//                        batchListAdapter!!.updateQty(batchNo, true)
//                        isSearched = true
//                        currentIndex = index
//                    }
//                    hideKeyboard(this, dailogKgLayl!!)
//                    break
//                }
//            }
//        }

        if (isSearched) {
            batch_rv!!.scrollToPosition(currentIndex)
        } else {
            toast("No product found")
        }
    }

    fun kg_edittxt(
        editArraylist: ArrayList<String>,
        item: NewSalesOrderDetailItem,
        dialog: Dialog
    ) {
        //  editArraylist.clear()
        isDialAddEmpty = false

        for (i in 0 until addKg_edLayl!!.childCount) {
            if (addKg_edLayl!!.getChildAt(i) is EditText) {
                val edittxt = addKg_edLayl!!.getChildAt(i) as EditText

                if (edittxt.text.isNotEmpty()) {
                    editArraylist.add(edittxt.text.toString())
                    Log.w(
                        "edttctcount",
                        "" + editArraylist.size + ".. " + addKg_edLayl!!.childCount
                    )

                    if (editArraylist.size == addKg_edLayl!!.childCount) {
                        isDialAddEmpty = true

                        val totalKg_Val = editArraylist.sumOf {
                            it.toDouble()
                        }
                        val totalVal = (unit_price_pickl!!.text.toString().toDouble() * totalKg_Val)

                        val totalApi = Utils.twoDecimalPoint(totalVal.toString().toDouble())
                        val totalKGApi = Utils.twoDecimalPoint(totalKg_Val.toString().toDouble())
                        //   total_pick_kg_txtl!!.setText(totalApi)
                        kg_txt_pickl!!.setText(Utils.twoDecimalPoint(totalKGApi.toDouble()))
                            .toString()

                        item!!.total = totalApi.toString().toDouble()
                        item!!.pickedQuantity = totalKGApi.toDouble()
                        item.isKgQty = true

                        Log.w("kggg", "... " + totalKg_Val)

                        Log.w("edittxtt11", "" + editArraylist.sumOf {
                            it.toDouble()
                        })
                        if (newSalesPickListDetailAdapter != null) {
                            newSalesPickListDetailAdapter!!.notifyDataSetChanged()
                        }

                    } else {
                        isDialAddEmpty = false
                    }
                }

            }

            Log.e("edittxtt", "" + editArraylist.toString())
        }
//        if(!isDialAddEmpty!!) {
//            Log.e("editaa", "")
//            toast("Enter KG Value")
//        }

    }

//    fun calcKg(s: String) {
//        GlobalScope.launch(Dispatchers.Main) {
//            val count = s.toInt()
//            for (i in 0 until count) {
//                val inflater =
//                    applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//                val view: View = inflater.inflate(R.layout.child_edit_text, addKg_edLayl!!, false)
////                val view: View = inflater.inflate(R.layout.batch_pick_item,addKg_edLayl!!,false)
//
////                            val lparams = LinearLayout.LayoutParams(
////                                LinearLayout.LayoutParams.WRAP_CONTENT,
////                                LinearLayout.LayoutParams.WRAP_CONTENT
////                            )
////                            val tv = EditText(this@PickListKGAddActivity)
////
////                            lparams.gravity = Gravity.RIGHT;
////                            lparams.setMargins(5, 0, 5, 2); // (left, top, right, bottom)
////
////                            tv.layoutParams = lparams
//                addKg_edLayl!!.addView(view)
//
//            }
//            Log.e("cqtyy", "")
//        }
//
//    }

    fun setBatchAdapter(BatchDetailPickModule: ArrayList<BatchDetailPickModule>) {

        batchsize_item!!.text = BatchDetailPickModule.size.toString() + " Batch"
        totalSelected(BatchDetailPickModule)

        batchListAdapter =
            BatchListPicklistAdapter(this, BatchDetailPickModule, itemBatchlist,
                this,this)
        batch_rv!!.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )
        batch_rv!!.adapter = batchListAdapter

        batch_rv!!.visibility = View.VISIBLE
        empty_txt_batch!!.visibility = View.GONE
        batchsize_item!!.visibility = View.VISIBLE
    }


    //    fun calcKg(s:String){
//        GlobalScope.launch(Dispatchers.Main) {
//            val count = s.toInt()
//            for(i in 0 until count) {
//                val inflater =
//                    applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//                val view: View = inflater.inflate(R.layout.child_edit_text,addKg_edLayl!!,false)
//
////                            val lparams = LinearLayout.LayoutParams(
////                                LinearLayout.LayoutParams.WRAP_CONTENT,
////                                LinearLayout.LayoutParams.WRAP_CONTENT
////                            )
////                            val tv = EditText(this@PickListKGAddActivity)
////
////                            lparams.gravity = Gravity.RIGHT;
////                            lparams.setMargins(5, 0, 5, 2); // (left, top, right, bottom)
////
////                            tv.layoutParams = lparams
//                addKg_edLayl!!.addView(view)
//
//            }
//            Log.e("cqtyy", "")
//        }
//
//    }
//
//
//    fun kg_edittxt(editArraylist : ArrayList<String>, item: NewSalesOrderDetailItem, dialog: Dialog){
//        editArraylist.clear()
//        isDialAddEmpty = false
//
//        for (i in 0 until addKg_edLayl!!.childCount) {
//            if (addKg_edLayl!!.getChildAt(i) is EditText) {
//                val edittxt = addKg_edLayl!!.getChildAt(i) as EditText
//                if (edittxt.text.isNotEmpty()) {
//                    editArraylist.add(edittxt.text.toString())
//                    Log.e(
//                        "edttctcount",
//                        "" + editArraylist.size + ".. " + addKg_edLayl!!.childCount
//                    )
//
//                    if (editArraylist.size == addKg_edLayl!!.childCount) {
//                        isDialAddEmpty = true
//
//                        val totalKg_Val = editArraylist.sumOf {
//                            it.toDouble()
//                        }
//                        val totalVal = (unit_price_pickl!!.text.toString().toDouble() * totalKg_Val)
//
//                        val totalApi = Utils.twoDecimalPoint(totalVal.toString().toDouble())
//                        val totalKGApi = Utils.twoDecimalPoint(totalKg_Val.toString().toDouble())
//                        total_pick_kg_txtl!!.setText(totalApi)
//                        kg_txt_pickl!!.setText(totalKGApi)
//
//                        item!!.total = totalApi.toString().toDouble()
//                        item!!.pickedQuantity = totalKGApi.toDouble()
//                        item.isKgQty = true
//
//                        Log.e("kggg", "... " + totalKg_Val)
//
//                        Log.e("edittxtt11", "" + editArraylist.sumOf {
//                            it.toDouble()
//                        })
//                    }
//                    else {
//                    isDialAddEmpty = false
//                }
//            }
//
//            }
//
//            Log.e("edittxtt", "" + editArraylist.toString())
//        }
//        if(!isDialAddEmpty!!) {
//            Log.e("editaa", "")
//            toast("Enter KG Value")
//        }
//
//    }
    fun showAlertDialog() {
        val builder1 = AlertDialog.Builder(this)
        builder1.setTitle("Warning...!")
        builder1.setMessage("Your Data will be lost, Are you sure want to back?.")
        builder1.setCancelable(false)
        builder1.setPositiveButton(
            "Yes"
        ) { dialog, id ->
            finish()
            dialog.cancel()
        }
        builder1.setNegativeButton(
            "No"
        ) { dialog, id -> dialog.cancel() }
        val alert11 = builder1.create()
        alert11.show()
    }

    override fun onBackPressed() {
        if (newSalesPickListDetailAdapter != null) {
            if (this.newpickList_api_set_Item_List!!.size > 0) {
                showAlertDialog()
            } else {
                finish()
            }
        } else {
            finish()
        }
    }

    fun clearValue() {
        kg_txt_pickl!!.setText("0.00")
        //  total_pick_kg_txtl!!.setText("0.00")
    }

    override fun batchKgSelected(item: NewSalesOrderDetailItem, position: Int?) {
        itemBatchlist = item
        //Log.w("pos_batch22..", " $position" + itemBatchlist)

        if ((dialog != null && !dialog!!.isShowing)
            || dialog == null
        )
            showBatchKG_Dialog(item, position)
    }

    override fun removeBatchSelected(position: Int?) {

    }

    override fun updateTotalQty(total: String) {
        val totalKG = batchListAdapter!!.getBatchDataList().sumOf {
            if (it.batchQty != null) {
                it.batchQty!!.toDouble()
            } else 0.0
//            it.selectKgQty!!.toDouble()
        }

        batchListAdapter!!.getBatchDataList().forEach {
            Log.d("cg_getbat ", it.batchNo.toString() + " " + it.batchQty.toString())
        }
        kg_txt_pickl!!.setText(Utils.twoDecimalPoint(totalKG)).toString()
        itemBatchlist.pickedQuantity = totalKG
        //itemBatchlist.isKgQty = true

     //   batchListAdapter!!.updateList()

        if (newSalesPickListDetailAdapter != null) {
            newSalesPickListDetailAdapter!!.notifyDataSetChanged()
        }
        //   toast(totalKG.toString())

    }

    override fun totalSelected(dataList: ArrayList<BatchDetailPickModule>) {
       var totalKG = 0.0
        totalKG = dataList.sumOf {
            if (it.batchQty != null) {
                it.batchQty!!.toDouble()
            } else {
                0.0
            }
//            it.selectKgQty!!.toDouble()
        }

        for(k in 0 until dataList.size) {
//            if (dataList.get(k).batchQty != null &&
//                dataList.get(k).batchQty!! > 0.0
//            ) {
//                totalKG += dataList.get(k).batchQty!!
//            } else {
//                totalKG = 0.0
//            }
            Log.w("cg_total_kg11 ", ""+dataList.get(k).batchQty!!.toDouble())

        }
            kg_txt_pickl!!.setText(Utils.twoDecimalPoint(totalKG)).toString()
            itemBatchlist.pickedQuantity = totalKG
            Log.w("cg_total_kg ", ""+totalKG)

            //itemBatchlist.isKgQty = true

            //   batchListAdapter!!.updateList()

            if (newSalesPickListDetailAdapter != null) {
                newSalesPickListDetailAdapter!!.notifyDataSetChanged()
            }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            if (newSalesPickListDetailAdapter != null) {
                if (this.newpickList_api_set_Item_List!!.size > 0) {
                    showAlertDialog()
                } else {
                    finish()
                }
            } else {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}