package com.winapp.wmsSQL.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.winapp.wmsSQL.R
import com.winapp.wmsSQL.adapter.PickListAddAdapter
import com.winapp.wmsSQL.model.ItemGroupList
import com.winapp.wmsSQL.newtransfer.LocationModel
import com.winapp.wmsSQL.newtransfer.TransferInModel
import com.winapp.wmsSQL.newtransfer.TransferInModel.TransferInDetails
import com.winapp.wmsSQL.utils.BarCodeScanner
import com.winapp.wmsSQL.utils.CaptureSignatureView
import com.winapp.wmsSQL.utils.Constants
import com.winapp.wmsSQL.utils.ImageUtil
import com.winapp.wmsSQL.utils.SessionManager
import com.winapp.wmsSQL.utils.SharedPreferenceUtil
import com.winapp.wmsSQL.utils.Utils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Objects

class PickListAddActivity : AppCompatActivity() {
    var pDialog: SweetAlertDialog? = null
    private var transferInModels: ArrayList<TransferInModel>? = null
    private var transferInDetailsl: ArrayList<TransferInDetails>? = null
    private var locationDetailsl: ArrayList<LocationModel.LocationDetails>? = null
    var pickListAddAdapter: PickListAddAdapter? = null
    var rv_takeAddView: RecyclerView? = null
    var pdtsizel: TextView? = null
    var count = 0
    private val itemGroup: ArrayList<ItemGroupList>? = null
    private var groupspinner: AppCompatSpinner? = null
    var fromWarehouseCode: String? = ""
    var fromWarehouseName = ""
    var search_ed: EditText? = null
    var emptytxt: TextView? = null
    var date_takel: TextView? = null
    var locationTxt: TextView? = null
    var companyCode: String? = null
    var username: String? = null
    var user: HashMap<String, String>? = null
    var session: SessionManager? = null
    var companyName: String? = null
    var locationCode: String? = null
    private val cancelSheet: ImageView? = null
    private var cancelButton: Button? = null
    private var okButton: Button? = null
    private var alert: AlertDialog? = null
    var invoicePrintCheck: CheckBox? = null
    var saveTitle: TextView? = null
    var signatureCapture: ImageView? = null
    var attachement_layoutInvl: LinearLayout? = null
    private var signatureAlert: AlertDialog? = null
    private var saveMessage: TextView? = null
    private var default_uom_transfl: TextView? = null
    private val settingUOMval = "PCS"
    private var islocationPermission: String? = null
    private var sharedPreferenceUtil: SharedPreferenceUtil? = null
    var RESULT_CODE = 12
    var scannedBarcode: String? = ""
    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picklist_add)
        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Pick List"
        Log.w("activity_cg", javaClass.simpleName.toString())
        session = SessionManager(this)
        user = session!!.userDetails
        progressDialog = ProgressDialog(this)
        sharedPreferenceUtil = SharedPreferenceUtil(this)
        companyCode = user!!.get(SessionManager.KEY_COMPANY_CODE)
        companyName = user!!.get(SessionManager.KEY_COMPANY_NAME)
        username = user!!.get(SessionManager.KEY_USER_NAME)
        locationCode = user!!.get(SessionManager.KEY_LOCATION_CODE)
        islocationPermission = user!!.get(SessionManager.IS_LOCATION_PERMISSION)
        default_uom_transfl = findViewById(R.id.default_uom_transf)
        rv_takeAddView = findViewById(R.id.rv_takeaddlist)
        search_ed = findViewById(R.id.searchBar_transfer)
        emptytxt = findViewById(R.id.empty_txt)
        date_takel = findViewById(R.id.date_take)
        locationTxt = findViewById(R.id.location_takeAdd)
        pdtsizel = findViewById(R.id.pdtsize)
        groupspinner = findViewById(R.id.spinner_status)

        transferInDetailsl = ArrayList()
        val c = Calendar.getInstance().time
        println("Current time => $c")
        val df1 = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        currentDate = df1.format(c)
        val df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = df.format(c)
        date_takel!!.setText(formattedDate)
        if (intent != null) {
            //  locationTxt.setText(locationCode);
            //   fromWarehouseCode = locationCode;
        }
//        locationTxt.setOnClickListener(View.OnClickListener { getfromlocationDialog(locationDetailsl) })
        if (transferInDetailsl == null) {
            emptytxt!!.setVisibility(View.VISIBLE)
            pdtsizel!!.setVisibility(View.GONE)
            search_ed!!.setEnabled(false)
            rv_takeAddView!!.setVisibility(View.GONE)
        }
        try {
            // getGrouplist();
            locationlist
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        search_ed!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (!s.toString().isEmpty()) {
                    val searchtxt = s.toString()
                    // if (!searchtxt.isEmpty()) {
                    filter(searchtxt)
                    //}
//                    else{
//                        setTransferInAdapter(transferInDetailsl);
//                    }
                    //  Log.w("transFiltSize",""+transferInDetailsl.size());
                } else {
                    Log.w("transFiltSizeaa", "" + transferInDetailsl!!.size)
                    setpickListAddAdapter(transferInDetailsl!!)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(query: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            var count = 0
            for (i in transferInDetailsl!!.indices) {
                if (!transferInDetailsl!![i].qty.isEmpty()) {
                    count += transferInDetailsl!![i].qty.toInt()
                }
            }
            if (count > 0) {
                showDeleteAlert()
            } else {
                finish()
            }
            return true
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun showDeleteAlert() {
        val builder1 = AlertDialog.Builder(this@PickListAddActivity)
        builder1.setMessage("Data Will be Cleared are you sure want to back?")
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

    @SuppressLint("NotifyDataSetChanged")
    fun setpickListAddAdapter(transferInList: ArrayList<TransferInDetails>) {
        try {
            rv_takeAddView!!.visibility = View.VISIBLE
            pdtsizel!!.visibility = View.VISIBLE
            search_ed!!.isEnabled = true
            emptytxt!!.visibility = View.GONE
            pdtsizel!!.text = transferInList.size.toString() + " Products"
            pickListAddAdapter = PickListAddAdapter(applicationContext, transferInList)
            rv_takeAddView!!.layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            rv_takeAddView!!.itemAnimator = DefaultItemAnimator()
            rv_takeAddView!!.adapter = pickListAddAdapter
            pickListAddAdapter!!.notifyDataSetChanged()
            //categoriesView.setVisibility(View.VISIBLE);
            //emptyLayout.setVisibility(View.GONE);
        } catch (ex: Exception) {
            Log.e("TAG", "Error in Populating the data:" + ex.message)
        }
    }

    fun showSaveAlert() {
        try {
            // create an alert builder
            val builder = AlertDialog.Builder(this)
            // set the custom layout
            builder.setCancelable(false)
            val customLayout = layoutInflater.inflate(R.layout.invoice_save_option, null)
            builder.setView(customLayout)
            // add a button
            okButton = customLayout.findViewById(R.id.btn_ok)
            cancelButton = customLayout.findViewById(R.id.btn_cancel)
            invoicePrintCheck = customLayout.findViewById(R.id.invoice_print_check)
            saveMessage = customLayout.findViewById(R.id.save_message)
            saveTitle = customLayout.findViewById(R.id.save_title)
            signatureCapture = customLayout.findViewById(R.id.signature_capture)
            attachement_layoutInvl = customLayout.findViewById(R.id.attachement_layoutInv)
            attachement_layoutInvl!!.setVisibility(View.GONE)

            val noOfCopy = customLayout.findViewById<TextView>(R.id.no_of_copy)
            val copyPlus = customLayout.findViewById<Button>(R.id.increase)
            val copyMinus = customLayout.findViewById<Button>(R.id.decrease)
            val signatureButton = customLayout.findViewById<Button>(R.id.btn_signature)
            val copyLayout = customLayout.findViewById<LinearLayout>(R.id.print_layout)

            invoicePrintCheck!!.setVisibility(View.GONE)
            //invoicePrintCheck.setVisibility(View.GONE);
            saveTitle!!.setText("Save Stock Take")
            saveMessage!!.setText("Are you sure want to save Stock Take?")
            //                invoicePrintCheck.setText("Stock Request Print");
            invoicePrintCheck!!.setOnClickListener(View.OnClickListener {
                if (invoicePrintCheck!!.isChecked()) {
                    isPrintEnable = true
                } else {
                    isPrintEnable = false
                }
            })
            okButton!!.setOnClickListener(View.OnClickListener { view1: View? ->
                try {
                    alert!!.dismiss()
                    createJsonObject()
                } catch (exception: Exception) {
                }
            })
            copyPlus.setOnClickListener {
                val copyvalue = noOfCopy.text.toString()
                var copy = copyvalue.toInt()
                copy++
                noOfCopy.text = copy.toString() + ""
            }
            copyMinus.setOnClickListener {
                if (noOfCopy.text.toString() != "1") {
                    val copyvalue = noOfCopy.text.toString()
                    var copy = copyvalue.toInt()
                    copy--
                    noOfCopy.text = copy.toString() + ""
                }
            }
            signatureButton.setOnClickListener { showSignatureAlert() }
            cancelButton!!.setOnClickListener(View.OnClickListener { alert!!.dismiss() })
            // create and show the alert dialog
            alert = builder.create()
            alert!!.show()
        } catch (exception: Exception) {
        }
    }

    fun showSignatureAlert() {
        val alertDialog = AlertDialog.Builder(this)
        val customLayout = layoutInflater.inflate(R.layout.signature_layout, null)
        alertDialog.setView(customLayout)
        val acceptButton = customLayout.findViewById<Button>(R.id.buttonYes)
        val cancelButton = customLayout.findViewById<Button>(R.id.buttonNo)
        val clearButton = customLayout.findViewById<Button>(R.id.buttonClear)
        val mContent = customLayout.findViewById<LinearLayout>(R.id.signature_layout)
        acceptButton.isEnabled = false
        acceptButton.alpha = 0.4f
        val mSig = CaptureSignatureView(this@PickListAddActivity, null) {
            acceptButton.isEnabled = true
            acceptButton.alpha = 1f
        }
        mContent.addView(
            mSig,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        acceptButton.setOnClickListener { // byte[] signature = captureSignatureView.getBytes();
            val signature = mSig.bitmap
            signatureCapture!!.setImageBitmap(signature)
            signatureString = ImageUtil.convertBimaptoBase64(signature)
            Utils.setSignature(signatureString)
            signatureAlert!!.dismiss()
            Log.w("SignatureString:", signatureString)
        }
        cancelButton.setOnClickListener { signatureAlert!!.dismiss() }
        clearButton.setOnClickListener {
            signatureString = ""
            Utils.setSignature("")
            mSig.ClearCanvas()
        }
        signatureAlert = alertDialog.create()
        signatureAlert!!.setCanceledOnTouchOutside(false)
        signatureAlert!!.show()
    }

    @Throws(JSONException::class)
    private fun createJsonObject() {
        // transferInDetailsl = transferInAdapter.getTransferInlist();
        val rootJson = JSONObject()
        var itemsObject = JSONObject()
        val itemsArray = JSONArray()
        rootJson.put("DocEntry", "")
        rootJson.put("Status", "")
        rootJson.put("DocDate", currentDate)
        rootJson.put("Remarks", "")
        rootJson.put("Image", "")
        rootJson.put("Signature", "")

        // Sales Details Add to the Objects
        var index = 1
        for (model in transferInDetailsl!!) {
            if (model.qty != null && !model.qty.isEmpty() && model.qty.toInt() > 0) {
                Log.w("takeQtyaa", "" + model.qty)
                itemsObject = JSONObject()
                itemsObject.put("itemCode", model.productCode)
                itemsObject.put("itemName", model.productName)
                itemsObject.put("qty", model.qty.toString())
                itemsObject.put("price", "")
                itemsObject.put("WarehouseCode", fromWarehouseCode)
                itemsObject.put("UomCode", model.inventoryUOM)
                itemsArray.put(itemsObject)
                index++
            }
        }
        rootJson.put("InventorycountingDetails", itemsArray)
        Log.w("GivenStockRequest:", rootJson.toString())
        saveTransferOrRequest(rootJson, 1)
    }

    fun saveTransferOrRequest(jsonBody: JSONObject, copy: Int) {
        try {
            pDialog = SweetAlertDialog(this@PickListAddActivity, SweetAlertDialog.PROGRESS_TYPE)
            pDialog!!.progressHelper.barColor = Color.parseColor("#A5DC86")
            pDialog!!.setCancelable(false)
            val requestQueue = Volley.newRequestQueue(this)
            Log.w("GiventakeRequest:", jsonBody.toString())
            var URL = ""
            URL = Utils.getBaseUrl(this) + "PostingStockTake"
            Log.w("Given_takeApi:", URL)
            pDialog!!.setTitleText("Saving Take...")
            pDialog!!.show()
            val salesOrderRequest: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST,
                URL,
                jsonBody,
                Response.Listener { response: JSONObject ->
                    Log.w("Take_ResSap:", response.toString())
                    pDialog!!.dismiss()
                    val statusCode = response.optString("statusCode")
                    val message = response.optString("statusMessage")
                    var responseData: JSONObject? = null
                    responseData = response.optJSONObject("responseData")
                    if (statusCode == "1") {
                        val docNum = responseData.optString("docNum")
                        Toast.makeText(
                            applicationContext,
                            "Transfer Saved Success...!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(applicationContext, StockTakeListActivity::class.java)
                        if (isPrintEnable) {
                            intent.putExtra("docNum", docNum)
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        /* Intent intent=new Intent(getApplicationContext(),TransferListProductActivity.class);
                    if (isPrintEnable) {
                        intent.putExtra("docNum","22010004");
                        intent.putExtra("transferType",transferType);
                    }
                    startActivity(intent);
                    finish();*/
                        if (responseData != null) {
                            Toast.makeText(
                                applicationContext,
                                responseData.optString("error"),
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Error in Saving Data...",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                Response.ErrorListener { error: VolleyError ->
                    Log.w("SalesOrder_Response:", error.toString())
                    pDialog!!.dismiss()
                }) {
                /* @Override
                 public byte[] getBody() {
                     return jsonBody.toString().getBytes();
                 }*/
                override fun getBodyContentType(): String {
                    return "application/json"
                }

                override fun getHeaders(): Map<String, String> {
                    val params = HashMap<String, String>()
                    val creds = String.format(
                        "%s:%s",
                        Constants.API_SECRET_CODE,
                        Constants.API_SECRET_PASSWORD
                    )
                    val auth = "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.DEFAULT)
                    params["Authorization"] = auth
                    return params
                }
            }
            salesOrderRequest.setRetryPolicy(object : RetryPolicy {
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
            requestQueue.add(salesOrderRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun filter(text: String) {
        try {
            //new array list that will hold the filtered data
            val filterProducts = ArrayList<TransferInDetails>()
            //looping through existing elements
            //   for (ProductsModel s : selectProductAdapter.getProductsList()) {
            emptytxt!!.visibility = View.GONE
            for (s in transferInDetailsl!!) {
                //if the existing elements contains the search input
                if (s.productName.lowercase(Locale.getDefault())
                        .contains(text.lowercase(Locale.getDefault())) ||
                    s.productCode.lowercase(Locale.getDefault())
                        .contains(text.lowercase(Locale.getDefault()))
                ) {
                    //adding the element to filtered list
                    filterProducts.add(s)
                    emptytxt!!.visibility = View.GONE
                }
            }
            //calling a method of the adapter class and passing the filtered list
            pickListAddAdapter!!.updateList(filterProducts)
            Log.e("filter", "" + filterProducts)
            if (filterProducts.isEmpty()) {
                emptytxt!!.visibility = View.VISIBLE
            }

            //setAdapter(filterProducts);
            pdtsizel!!.text = filterProducts.size.toString() + " Products"
        } catch (ex: Exception) {
            Log.e("Error_in_filter", Objects.requireNonNull(ex.message)!!)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_CODE) {
                val barcodeText = data!!.extras!!.getString("Contents")
                Log.w("BarcodeTextInv:", barcodeText!!)
                val mp = MediaPlayer.create(this, R.raw.beep) // sound is inside res/raw/mysound
                mp.start()
                scannedBarcode = barcodeText
             //   searchAndSendActivity(barcodeText)
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun getTakeAdd(warehouseCode: String?, itemGroupCode: String) {
        val url: String
        try {
            val jsonObj = JSONObject()
            jsonObj.put("WarehouseCode", warehouseCode)
            jsonObj.put("ItemGroupCode", itemGroupCode)
            val requestQueue = Volley.newRequestQueue(this)
            url = Utils.getBaseUrl(this) + "ProductList"
            Log.w("pdtlist_urlTransAdd:", url + jsonObj)
            pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            pDialog!!.progressHelper.barColor = Color.parseColor("#A5DC86")
            pDialog!!.setTitleText("Loading...")
            pDialog!!.setCancelable(false)
            pDialog!!.show()
            transferInModels = ArrayList()
            transferInDetailsl = ArrayList()
            val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST,
                url,
                jsonObj,
                Response.Listener { response: JSONObject ->
                    try {
                        pDialog!!.dismiss()
                        Log.w("pdtlistTransAdd:", response.toString())

                        //pDialog.dismiss();
                        val statusCode = response.optString("statusCode")
                        val statusMessage = response.optString("statusMessage")
                        if (statusCode == "1") {
                            val transferInModel = TransferInModel()
                            val pdtArray = response.optJSONArray("responseData")
                            for (i in 0 until pdtArray.length()) {
                                val jsonObject = pdtArray.getJSONObject(i)

//                                    if (transferType.equals("Transfer In")) {
//                                        if (jsonObject.optInt("stockInHand") > 0){
//                                            TransferInModel.TransferInDetails transferInDetails = new TransferInModel.TransferInDetails();
//                                            transferInDetails.setProductName(jsonObject.optString("productName"));
//                                            transferInDetails.setProductCode(jsonObject.optString("productCode"));
//                                            transferInDetails.setStockInHand((jsonObject.optInt("stockInHand")));
//                                            transferInDetails.setQty("");
//                                            transferInDetails.setInventoryUOM(jsonObject.optString("defaultInventoryUOM"));
//                                            transferInDetailsl.add(transferInDetails);
//                                        }
//                                    } else {
                                val transferInDetails = TransferInDetails()
                                transferInDetails.productName = jsonObject.optString("productName")
                                transferInDetails.productCode = jsonObject.optString("productCode")
                                transferInDetails.stockInHand = jsonObject.optInt("stockInHand")
                                transferInDetails.qty = ""
                                transferInDetails.inventoryUOM =
                                    jsonObject.optString("defaultInventoryUOM")
                                transferInDetailsl!!.add(transferInDetails)
                                //  }
                            }
                            Log.w("entrTake_ddd", "" + transferInDetailsl!!.size)
                            if (transferInDetailsl!!.size > 0) {
                                transferInModel.transferInDetails = transferInDetailsl
                                setpickListAddAdapter(transferInDetailsl!!)
                                Log.w("entrTake", "" + transferInDetailsl!!.size)
                            }
                        } else {
                            pickListAddAdapter!!.notifyDataSetChanged()
                            transferInDetailsl!!.clear()
                            rv_takeAddView!!.adapter = null
                            pdtsizel!!.text = "0 Products"
                            Toast.makeText(applicationContext, statusMessage, Toast.LENGTH_SHORT)
                                .show()
                            Log.w("entrTake", "")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error: VolleyError ->
                    // Do something when error occurred
                    // pDialog.dismiss();
                    Log.w("Error_throwing:", error.toString())
                }) {
                override fun getHeaders(): Map<String, String> {
                    val params = HashMap<String, String>()
                    val creds = String.format(
                        "%s:%s",
                        Constants.API_SECRET_CODE,
                        Constants.API_SECRET_PASSWORD
                    )
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
        } catch (e: Exception) {
        }
    }

    @get:Throws(JSONException::class)
    private val locationlist: Unit
        private get() {
            val requestQueue = Volley.newRequestQueue(this)
            val url = Utils.getBaseUrl(this) + "WarehouseList"
            // Initialize a new JsonArrayRequest instance
            Log.w("Given_url_location:", url)
            pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            pDialog!!.progressHelper.barColor = Color.parseColor("#A5DC86")
            pDialog!!.setTitleText("Loading Warehouses...")
            pDialog!!.setCancelable(false)
            pDialog!!.show()
            locationDetailsl = ArrayList()
            val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
                Method.GET,
                url,
                null,
                Response.Listener { response: JSONObject ->
                    try {
                        Log.w("locationlist:", response.toString())
                        pDialog!!.dismiss()
                        val statusCode = response.optString("statusCode")
                        val statusMessage = response.optString("statusMessage")
                        if (statusCode == "1") {
                            val locationModel = LocationModel()
                            val locationArray = response.optJSONArray("responseData")
                            for (i in 0 until locationArray.length()) {
                                val jsonObject = locationArray.getJSONObject(i)
                                val locationDetails = LocationModel.LocationDetails()
                                locationDetails.locationName = jsonObject.optString("whsName")
                                locationDetails.locationCode = jsonObject.optString("whsCode")
                                locationDetailsl!!.add(locationDetails)
                            }
                            if (locationDetailsl!!.size > 0) {
                                locationModel.setLocationDetailsArrayList(locationDetailsl)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error: VolleyError ->
                    // Do something when error occurred
                    pDialog!!.dismiss()
                    Log.w("Error_throwing:", error.toString())
                }) {
                override fun getHeaders(): Map<String, String> {
                    val params = HashMap<String, String>()
                    val creds = String.format(
                        "%s:%s",
                        Constants.API_SECRET_CODE,
                        Constants.API_SECRET_PASSWORD
                    )
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getfromlocationDialog(locationDetailsArrayList: ArrayList<LocationModel.LocationDetails>?) {
        val builderSingle = AlertDialog.Builder(this)
        builderSingle.setTitle("Select From Warehouse")
        val arrayAdapter = ArrayAdapter<String>(this, R.layout.selection_single_dialog)
        for (i in locationDetailsArrayList!!.indices) {
            arrayAdapter.add(locationDetailsArrayList[i].locationName)
        }
        val checkedItem = -1
        builderSingle.setSingleChoiceItems(
            arrayAdapter,
            checkedItem
        ) { dialog, which -> // user checked an item
            val strName = arrayAdapter.getItem(which)
            locationTxt!!.text = strName
            for (i in locationDetailsArrayList.indices) {
                if (strName == locationDetailsArrayList[i].locationName) {
                    Log.e("fromlocatcode", "" + locationDetailsArrayList[i].locationCode)
                    fromWarehouseCode = locationDetailsArrayList[i].locationCode
                    fromWarehouseName = locationDetailsArrayList[i].locationName
                }
            }
            getTakeAdd(fromWarehouseCode, "All")
            dialog.dismiss()
        }
        builderSingle.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
        builderSingle.setCancelable(false)
        builderSingle.show()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.save_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                var count = 0
                for (i in transferInDetailsl!!.indices) {
                    if (!transferInDetailsl!![i].qty.isEmpty()) {
                        count += transferInDetailsl!![i].qty.toInt()
                    }
                }
                if (count > 0) {
                    showDeleteAlert()
                } else {
                    finish()
                }
                true
            }

            R.id.action_save -> {

                for (i in transferInDetailsl!!.indices) {
                    if (!transferInDetailsl!![i].qty.isEmpty()) {
                        count += transferInDetailsl!![i].qty.toInt()
                    }
                }
                Log.e("qqty", "" + count)
                if (fromWarehouseCode != null && !fromWarehouseCode!!.isEmpty()) {
                    if (count > 0) {
                        try {
                            showSaveAlert()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        Toast.makeText(applicationContext, "Add product first...!", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Select Locations...!", Toast.LENGTH_SHORT)
                        .show()
                }

                true
            }

            R.id.action_scan_menu -> {
                scannedBarcode = ""
                val intent = Intent(this@PickListAddActivity, BarCodeScanner::class.java)
                startActivityForResult(intent, RESULT_CODE)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
        //  return super.onOptionsItemSelected(item);
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        var currentDate: String? = null
        var progressDialog: ProgressDialog? = null
        var customerCode: String? = null
        var isPrintEnable = false
        var selectedBank: TextView? = null
        var amountText: EditText? = null
        var signatureString = ""
        var imageString: String? = null
    }
}