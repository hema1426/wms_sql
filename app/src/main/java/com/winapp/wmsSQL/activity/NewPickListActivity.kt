package com.winapp.wmsSQL.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.winapp.pickanddrop.ui.model.PickIistResponseNew
import com.winapp.wmsSQL.CommonMethods
import com.winapp.wmsSQL.R
import com.winapp.wmsSQL.adapter.PickListNewAdapter
import com.winapp.wmsSQL.model.AddressZoneModel
import com.winapp.wmsSQL.model.SupplierModel1
import com.winapp.wmsSQL.multiselectspinner.MultiSelectSpinnerView
import com.winapp.wmsSQL.multiselectspinner.MultipleSelectSpinnerPojo
import com.winapp.wmsSQL.utils.CommonMethodKotl.toast
import com.winapp.wmsSQL.utils.Constants
import com.winapp.wmsSQL.utils.SessionManager
import com.winapp.wmsSQL.utils.SharedPreferenceUtil
import com.winapp.wmsSQL.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NewPickListActivity : AppCompatActivity(), OnClickListener {

    private var picklistinvoice_rv: RecyclerView? = null
    private var sharedPreferenceUtil: SharedPreferenceUtil? = null

    // var intentIntegrator: IntentIntegrator? = null
    var pickListNewAdapter: PickListNewAdapter? = null
    private var barCodelay: LinearLayout? = null
    var session: SessionManager? = null
    var companyId: String? = null
    var locationCode: String? = null
    var select_FromDateStr: String? = ""
    var select_FromDateShowStr: String? = ""
    var select_ToDateStr: String? = ""
    var select_ToDateShowStr: String? = ""
    var zoneMutipleCode: String? = ""
    var zoneStrName: String? = ""
    var zoneStrCode: String? = ""
    var username: String? = null
    var user: HashMap<String, String>? = null
    var todateShared: String? = ""
    var fromdateShared: String? = ""

    var DAY_FORMAT = "dd"
    var MONTH_FORMAT = "MM"
    var YEAR_FORMAT = "yyyy"
    var DATE_FORMAT_API1 = "$YEAR_FORMAT$MONTH_FORMAT$DAY_FORMAT"
    private var current_FromDateApi =
        SimpleDateFormat(DATE_FORMAT_API1, Locale.US).format(Calendar.getInstance().time)
    private var current_ToDateApi =
        SimpleDateFormat(DATE_FORMAT_API1, Locale.US).format(Calendar.getInstance().time)
    private var currentDate =
        SimpleDateFormat(Constants.DAY_FORMAT, Locale.US).format(Calendar.getInstance().time)
    private val DATE_FORMAT1: String = DAY_FORMAT + "/" + MONTH_FORMAT + "/" + YEAR_FORMAT

    var isscanpdt: Boolean? = false
    private var spinner_statusl: Spinner? = null

    //    private var SO_sharf: Boolean? = null
//    private var INV_sharf: Boolean? = null
    private var pickifromdatelay: LinearLayout? = null
    private var pickitodatelay: LinearLayout? = null
    private var pickifromdate_txt: TextView? = null
    private var pickitodate_txt: TextView? = null
    private var pickdate_search: Button? = null
    private var btn_cancelm: Button? = null
    private var spinner_layl: RelativeLayout? = null
    private var pick_type_api: String? = null
    private var isspinner: Boolean? = null
    private lateinit var callback: OnBackPressedCallback
    private var filterImg: ImageView? = null
    private var addImg: ImageView? = null
    private var emptytxt: TextView? = null
    var picklistNew: ArrayList<PickIistResponseNew> = ArrayList()
    var spinnertxt: String? = "";
    var customerStr: String? = "";
    var soNum: String? = "";
    var totalSize: TextView? = null
    var so_number_filter_pickl: TextView? = null
    var filter_iconl: FloatingActionButton? = null
    var searchFilterView: View? = null
    var custFilterAutol: AutoCompleteTextView? = null
    private var supplierlist: ArrayList<SupplierModel1>? = ArrayList()
    private var customerlist: ArrayList<SupplierModel1>? = ArrayList()
    private var searchableCustomerList: ArrayList<String>? = null
    var autoCompleteAdapter: ArrayAdapter<String>? = null
    var addressZoneList: ArrayList<AddressZoneModel> = ArrayList()
    var zoneSpinner: MultiSelectSpinnerView? = null
    val zoneSpinnerStr: String? = ""
    private var pDialog: SweetAlertDialog? = null
    var date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
    private var search_ed: EditText? = null
    var searchTextWatcher: TextWatcher? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_picklist_invoice)
        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Pick List"

        picklistinvoice_rv = findViewById(R.id.rv_picklist_invoice)
        barCodelay = findViewById(R.id.barcode_lay)
        pickitodatelay = findViewById(R.id.picki_todatelay)
        pickifromdatelay = findViewById(R.id.picki_fromdatelay)
        pickifromdate_txt = findViewById(R.id.picki_fromdate_txt)
        pickitodate_txt = findViewById(R.id.picki_todate_txt)
        pickdate_search = findViewById(R.id.search_dat_picki)
        btn_cancelm = findViewById(R.id.btn_cancel_pickl)
//        search_lay =  findViewById(R.id.picklist_search_lay)
        search_ed = findViewById(R.id.searchBar_pick)
        spinner_statusl = findViewById<View>(R.id.spinner_status_pick) as Spinner
        spinner_layl = findViewById(R.id.spinner_lay)
        emptytxt = findViewById(R.id.empty_txt)
        totalSize = findViewById(R.id.item_size_list)
        so_number_filter_pickl = findViewById(R.id.so_number_filter_pick)
        filter_iconl = findViewById(R.id.filter_icon)
        searchFilterView = findViewById(R.id.search_filter)
        custFilterAutol = findViewById(R.id.custFilterAuto)
        zoneSpinner = findViewById(R.id.zoneSpinnerl)

        val status = arrayOf("All", "Pending", "Partial", "Completed")

        val langAdapter =
            ArrayAdapter<CharSequence>(this, R.layout.cust_spinner_item, status)
        langAdapter.setDropDownViewResource(R.layout.item_grouplist_spinner)
        spinner_statusl!!.setAdapter(langAdapter)

        pickitodatelay!!.setOnClickListener(this)
        pickifromdatelay!!.setOnClickListener(this)
        pickdate_search!!.setOnClickListener(this)
        btn_cancelm!!.setOnClickListener(this)
        // search_ed!!.setOnClickListener(this)

        spinnertxt = ""

        if (Utils.getAddressZonelist().size > 0) {
            setZoneAdapter(Utils.getAddressZonelist()!!)
        } else {
            setAddressZone()
        }

        if (Utils.getSupplierList().size > 0) {
            var custlist = Utils.getSupplierList()
            // for (i in 0 until  Utils.getSupplierList()!!.size) {
            //   searchableCustomerList!!.add(custlist!!.get(i).supplierName + "~" + custlist!!.get(i).supplierCode)
            // setDataToAdapter(searchableCustomerList)
            // }
            //Log.w("autoArrayliaaa",""+searchableCustomerList);
        } else {
            getVendorList()
        }
        getCustomerList()
        Log.w("selectdateSO", "" + select_FromDateShowStr + ".." + select_FromDateStr)
        Log.w("selectdateSOTo", "" + select_ToDateShowStr + ".." + select_ToDateStr)


        if (select_FromDateShowStr!!.isNotEmpty()) {
            pickifromdate_txt!!.setText(select_FromDateShowStr)
            fromdateShared = select_FromDateStr
        } else {
            select_FromDateStr = CommonMethods.getCurrentDateApiNOSpace()
            fromdateShared = select_FromDateStr
            pickifromdate_txt!!.setText(CommonMethods.getCurrentTime1())
        }

        if (select_ToDateShowStr!!.isNotEmpty()) {
            pickitodate_txt!!.setText(select_ToDateShowStr)
            todateShared = select_ToDateStr

        } else {
            select_ToDateStr = CommonMethods.getCurrentDateApiNOSpace()
            todateShared = select_ToDateStr
            pickitodate_txt!!.setText(CommonMethods.getCurrentTime1())
        }

        getpicklist_Detail(
            "", "",
            fromdateShared!!, todateShared!!, soNum!!, spinnertxt!!, zoneStrCode!!
        )

        Log.w("zoneStrCode", "" + zoneStrCode)

        searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (!editable.toString().isEmpty()) {
                    filterProducts(editable.toString())
                } else {
                    setAdapter(picklistNew!!)
                }
            }
        }
        custFilterAutol!!.clearFocus()

        search_ed!!.addTextChangedListener(searchTextWatcher)

        spinner_statusl!!.setOnTouchListener(View.OnTouchListener { v, event ->

            Log.e("edtouch", "")
            spinner_statusl!!.setOnItemSelectedListener(@SuppressLint("ClickableViewAccessibility")
            object :
                AdapterView.OnItemSelectedListener {
                @SuppressLint("ClickableViewAccessibility")
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    //first,  we have to retrieve the item position as a string
                    // then, we can change string value into integer
                    val item_position = position.toString()
                    val positonInt = Integer.valueOf(item_position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            })
            false
        })

        filter_iconl!!.setOnClickListener {
            searchFilterView!!.visibility = View.GONE
            if (searchFilterView!!.visibility == View.VISIBLE) {
                searchFilterView!!.visibility = View.GONE
                //slideUp(searchFilterView);
            } else {
                //    customerNameText.setText("")

                searchFilterView!!.visibility = View.VISIBLE
            }
            Log.w("entyyy", "..")
        }
//        barCodelay!!.setOnClickListener {
//            // val intentIntegrator = IntentIntegrator(activity)
//            intentIntegrator!!.setBeepEnabled(false)
//            intentIntegrator!!.setCameraId(0)
//            intentIntegrator!!.setPrompt("SCAN")
//            intentIntegrator!!.setOrientationLocked(true)
//            intentIntegrator!!.setCaptureActivity(CaptureActivityPortrait::class.java)
//            intentIntegrator!!.setBarcodeImageEnabled(false)
//            intentIntegrator!!.initiateScan()
//        }

        sharedPreferenceUtil = SharedPreferenceUtil(this)
        //  intentIntegrator = IntentIntegrator.forSupportFragment(this) // use this instead
        session = SessionManager(this)

        user = session!!.getUserDetails()
        companyId = user!!.get(SessionManager.KEY_COMPANY_CODE)
        locationCode = user!!.get(SessionManager.KEY_LOCATION_CODE)
        username = user!!.get(SessionManager.KEY_USER_NAME)


//        user = Helper.getLoggedInUser(sharedPreferenceUtil!!)
//        if(user!=null && user!!.size>0){
//            userName = user!!.get(0).userName
//        }
        zoneStrCode = sharedPreferenceUtil!!.getStringPreference(
            Constants.KEY_ADDRESS_ZONE_CODE,
            Constants.DEFAULT_STRING
        )
        zoneStrName = sharedPreferenceUtil!!.getStringPreference(
            Constants.KEY_ADDRESS_ZONE_NAME,
            Constants.DEFAULT_STRING
        )
        select_FromDateShowStr = sharedPreferenceUtil!!.getStringPreference(
            Constants.KEY_SELECT_FROMDATE_DISPLAY,
            Constants.DEFAULT_STRING
        )
        select_FromDateStr = sharedPreferenceUtil!!.getStringPreference(
            Constants.KEY_SELECT_FROMDATE,
            Constants.DEFAULT_STRING
        )
        select_ToDateShowStr = sharedPreferenceUtil!!.getStringPreference(
            Constants.KEY_SELECTDATE_TODISPLAY,
            Constants.DEFAULT_STRING
        )
        select_ToDateStr = sharedPreferenceUtil!!.getStringPreference(
            Constants.KEY_SELECT_TODATE,
            Constants.DEFAULT_STRING
        )
    }

    private fun setAdapter(arrayList: ArrayList<PickIistResponseNew>) {
        visibletxt()
        totalSize!!.text = "(" + arrayList.size + ")" + "Items"

        pickListNewAdapter = PickListNewAdapter(this, arrayList)
        picklistinvoice_rv!!.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            ) as RecyclerView.LayoutManager?
        picklistinvoice_rv!!.adapter = pickListNewAdapter

//        search_ed!!.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//            override fun afterTextChanged(s: Editable) {
//                if (s != null && s.length > 3) {
//                    Log.e("searchgg",""+s.toString())
//                  //  getPicklistapi(fromdateApi, todateApi, s.toString(), pick_type_api)
//                }
//            }
//        })
//        search_ed!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                Log.e("searchff",""+search_ed!!.getText().toString())
//               // getPicklistapi(fromdateApi, todateApi, search_ed!!.getText().toString(), pick_type_api)
//
//            }
//            false
//        })
    }

    fun filterProducts(name: String) {
        try {
            //new array list that will hold the filtered data
            val filterdNames = java.util.ArrayList<PickIistResponseNew>()
            //looping through existing elements
            for (s in picklistNew!!) {
                //if the existing elements contains the search input
                if (s.customerName!!.lowercase(Locale.getDefault())
                        .contains(name.lowercase(Locale.getDefault())) ||
                    s.customerCode!!.lowercase(Locale.getDefault())
                        .contains(name.lowercase(Locale.getDefault()))
                ) {
                    //adding the element to filtered list
                    filterdNames.add(s)
                }
            }
            //calling a method of the adapter class and passing the filtered list
            if (filterdNames.size > 0) {
                visibletxt()
                pickListNewAdapter!!.updateList(filterdNames)
                totalSize!!.text = "(" + filterdNames.size + ")" + "Items"
            } else {
                emptytxt()
                Utils.hideKeyBoard(this, search_ed)
            }
        } catch (ex: Exception) {
            Log.e("Error_in_filter", ex.message!!)
        }
    }

    @Throws(JSONException::class)
    private fun getpicklist_Detail(
        customerCode: String,
        user: String,
        fromdate: String,
        todate: String,
        docNum: String,
        status: String,
        zone: String
    ) {
        // Initialize a new RequestQueue instance
        val requestQueue = Volley.newRequestQueue(this)
        // Initialize a new JsonArrayRequest instance
        val jsonObject = JSONObject()
        //        if (selectedUser!=null && !selectedUser.isEmpty()){
//            jsonObject.put("User",selectedUser);
//        }else {
//            jsonObject.put("User",userName);
//        }
        jsonObject.put("CustomerCode", customerCode)
        jsonObject.put("CustomerName", "")
        jsonObject.put("UserCode", user)
        jsonObject.put("FromDate", fromdate)
        jsonObject.put("ToDate", todate)
        jsonObject.put("DocNumber", docNum)
        jsonObject.put("DocStatus", status)
//        jsonObject.put("Zone", zone)
        val url = Utils.getBaseUrl(this) + "SalesOrderwithBatchList"
        Log.w("Given_url:", "$url-$jsonObject")
        pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog!!.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"))
        pDialog!!.setTitleText("Getting PickList...")
        pDialog!!.setCancelable(false)
        pDialog!!.show()

        picklistNew = ArrayList()

        val jsonArrayRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, jsonObject,
            Response.Listener<JSONObject> { response: JSONObject ->
                try {
                    Log.w("picklist_res:", response.toString())

                    pDialog!!.dismiss()
                    val statusCode = response.optString("statusCode")
                    val statusMsg = response.optString("statusMessage")

                    if (statusCode == "1") {
                        val responseData = response.optJSONArray("responseData")!!

                        if (responseData!!.length() > 0) {

                            for (i in 0 until responseData.length()) {
                                val obj = responseData.optJSONObject(i)

                                val model = PickIistResponseNew(
                                    obj.optString("code"),
                                    obj.optString("customerCode"),
                                    obj.optString("customerName"),
                                    obj.optString("docDate"),
                                    obj.optString("docNumber"),
                                    "",
                                    obj.optString("noOfItem"),
                                    obj.optString("pickListStatus"),
                                    obj.optString("pickListNumber"),
                                    obj.optString("salesEmployee"),
                                    obj.optString("ownerName"),
                                    obj.optString("dateTime"),
                                    obj.optString("pickedQty"),
                                    obj.optString("nonPickedQty"),
                                    false
                                )

                                picklistNew!!.add(model)
                            }

                         //   withContext(Dispatchers.Main) {
                                if (picklistNew!!.size > 0) {
                                    searchFilterView!!.visibility = View.GONE
                                    setAdapter(picklistNew!!)
                                    cleartxt()

                                } else {
                                    emptytxt()
                                }
                         //   }
                        } else {
                            emptytxt()
                        }
                    } else {
                        toast(this@NewPickListActivity, statusMsg)
                        emptytxt()
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                pDialog!!.dismiss()
                // Do something when error occurred
                Log.w("Error_throwing:", error.toString())
                Toast.makeText(
                    applicationContext,
                    "Server Error,Please try again..",
                    Toast.LENGTH_LONG
                ).show()
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
        jsonArrayRequest.setRetryPolicy(object : RetryPolicy {
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
        requestQueue.add(jsonArrayRequest)
    }


//    @Throws(JSONException::class)
//    private fun getpicklist_Detail(
//        customerCode: String,
//        user: String,
//        fromdate: String,
//        todate: String,
//        docNum: String,
//        status: String,
//        zone: String
//    ) {
//        CommonMethods.showProgressDialog(this)
//
//        val jsonObject = JSONObject()
//        jsonObject.put("CustomerCode", customerCode)
//        jsonObject.put("CustomerName", "")
//        jsonObject.put("UserCode", user)
//        jsonObject.put("FromDate", fromdate)
//        jsonObject.put("ToDate", todate)
//        jsonObject.put("DocNumber", docNum)
//        jsonObject.put("DocStatus", status)
////        jsonObject.put("Zone", zone)
//
//        val requestQueue = Volley.newRequestQueue(this)
//
//        // val url = Utils.getBaseUrl(this) + "SalesOrderDetailsAll/salesorderdetail?Requestdata=$jsonObject"
//        val url = Utils.getBaseUrl(this@NewPickListActivity) + "SalesOrderwithBatchList"
//        Log.w("url_pickDetail_list:", "" + url + jsonObject)
//
//        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.GET,
//            url,
//            jsonObject,
//            Response.Listener { response: JSONObject ->
//                try {
//                    GlobalScope.launch {
//                        withContext(Dispatchers.Main) {
//                            picklistNew = ArrayList()
//
//                            Log.w("picklis_list_res:", response.toString())
//                            val statusCode = response.optString("StatusCode")
//                            val statusMsg = response.optString("StatusMessage")
//
//                            if (statusCode == "1") {
//                                val responseData = response.optJSONArray("ResponseData")!!
//
//                                if (responseData!!.length() > 0) {
//
//                                    for (i in 0 until responseData.length()) {
//                                        val obj = responseData.optJSONObject(i)
//
//                                        val model = PickIistResponseNew(
//                                            obj.optString("Code"),
//                                            obj.optString("CustomerCode"),
//                                            obj.optString("CustomerName"),
//                                            obj.optString("DocDate"),
//                                            obj.optString("DocNumber"),
//                                            "",
////                                            obj.optString("DocStatus"),
//                                            obj.optString("NoOfItem"),
//                                            obj.optString("PickListStatus"),
//                                            obj.optString("PickListNumber"),
//                                            obj.optString("SalesEmployee"),
//                                            obj.optString("OwnerName"),
//                                            obj.optString("DateTime"), ""
////                                            obj.optString("soNumber")
//                                            ,
//                                            false
//                                        )
//                                        picklistNew!!.add(model)
//                                    }
//
//                                    withContext(Dispatchers.Main) {
//                                        if (picklistNew!!.size > 0) {
//                                            searchFilterView!!.visibility = View.GONE
//                                            setAdapter(picklistNew!!)
//                                            cleartxt()
//
//                                        } else {
//                                            emptytxt()
//                                        }
//                                    }
//                                } else {
//                                    emptytxt()
//                                }
//                            } else {
//                                toast(this@NewPickListActivity, statusMsg)
//                                emptytxt()
//
//                            }
//                            CommonMethods.cancelProgressDialog()
//                        }
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }, Response.ErrorListener { error: VolleyError ->
//                // Do something when error occurred
//                //  pDialog.dismiss();
//                Log.w("Error_throwing:", error.toString())
//                CommonMethods.cancelProgressDialog()
//            }) {
//            override fun getBodyContentType(): String {
//                return "application/json"
//            }
//
//            override fun getHeaders(): Map<String, String> {
//                val params = HashMap<String, String>()
//                val creds =
//                    String.format(
//                        "%s:%s",
//                        Constants.API_SECRET_CODE,
//                        Constants.API_SECRET_PASSWORD
//                    )
//                val auth = "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.DEFAULT)
//                params["Authorization"] = auth
//                return params
//            }
//        }
//        jsonObjectRequest.setRetryPolicy(object : RetryPolicy {
//            override fun getCurrentTimeout(): Int {
//                return 50000
//            }
//
//            override fun getCurrentRetryCount(): Int {
//                return 50000
//            }
//
//            @Throws(VolleyError::class)
//            override fun retry(error: VolleyError) {
//            }
//        })
//        // Add JsonArrayRequest to the RequestQueue
//        requestQueue.add(jsonObjectRequest)
//    }

    override fun onClick(view: View) {
        when (view.id) {

            R.id.picki_fromdatelay ->                 // Get Current Date
            {
                val c = Calendar.getInstance()
                val mYear = c[Calendar.YEAR]
                val mMonth = c[Calendar.MONTH]
                val mDay = c[Calendar.DAY_OF_MONTH]
                val datePickerDialog = DatePickerDialog(
                    Objects.requireNonNull(this),
                    OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        var selectedMonthYearDate: Date? = null
                        try {
                            selectedMonthYearDate = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                                .parse(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year.toString())
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                        if (selectedMonthYearDate != null) {
                            currentDate =
                                SimpleDateFormat(DATE_FORMAT1, Locale.US).format(
                                    selectedMonthYearDate
                                )
                            current_FromDateApi =
                                SimpleDateFormat(DATE_FORMAT_API1, Locale.US).format(
                                    selectedMonthYearDate
                                )
                            sharedPreferenceUtil!!.setStringPreference(
                                Constants.KEY_SELECT_FROMDATE, current_FromDateApi
                            )
                            fromdateShared = current_FromDateApi

                            sharedPreferenceUtil!!.setStringPreference(
                                Constants.KEY_SELECT_FROMDATE_DISPLAY, currentDate
                            )
                            pickifromdate_txt!!.setText(currentDate)
                        }
                    }, mYear, mMonth, mDay
                )
                //  datePickerDialog.datePicker.minDate = c.timeInMillis
                //datePickerDialog.getDatePicker().setMaxDate(twoDaysLater.getTimeInMillis());
                datePickerDialog.show()
            }

            R.id.picki_todatelay ->                 // Get Current Date
            {
                val c = Calendar.getInstance()
                val mYear = c[Calendar.YEAR]
                val mMonth = c[Calendar.MONTH]
                val mDay = c[Calendar.DAY_OF_MONTH]
                val datePickerDialog = DatePickerDialog(
                    Objects.requireNonNull(this),
                    OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        var selectedMonthYearDate: Date? = null
                        try {
                            selectedMonthYearDate = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                                .parse(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year.toString())
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                        if (selectedMonthYearDate != null) {
                            currentDate =
                                SimpleDateFormat(DATE_FORMAT1, Locale.US).format(
                                    selectedMonthYearDate
                                )
                            current_ToDateApi =
                                SimpleDateFormat(DATE_FORMAT_API1, Locale.US).format(
                                    selectedMonthYearDate
                                )
                            sharedPreferenceUtil!!.setStringPreference(
                                Constants.KEY_SELECT_TODATE, current_ToDateApi
                            )
                            todateShared = current_ToDateApi

                            sharedPreferenceUtil!!.setStringPreference(
                                Constants.KEY_SELECTDATE_TODISPLAY, currentDate
                            )
                            pickitodate_txt!!.setText(currentDate)
                        }
                        Log.e("api3", "..")

                    }, mYear, mMonth, mDay
                )
                //   datePickerDialog.datePicker.minDate = c.timeInMillis
                //datePickerDialog.getDatePicker().setMaxDate(twoDaysLater.getTimeInMillis());
                datePickerDialog.show()
            }

            R.id.btn_cancel_pickl -> {
                cleartxt()
                searchFilterView!!.visibility = View.GONE
            }

            R.id.search_dat_picki -> {
                if (pickifromdate_txt!!.text.toString()
                        .isNotEmpty() && pickitodate_txt!!.text.toString()
                        .isNotEmpty()
                ) {
                    val sdformat = SimpleDateFormat("dd/MM/yyyy")
                    var d1: Date? = null
                    var d2: Date? = null
                    try {
                        d1 = sdformat.parse(pickifromdate_txt!!.getText().toString())
                        d2 = sdformat.parse(pickitodate_txt!!.getText().toString())
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    Log.w("datecompa", "" + pickifromdate_txt!!.getText().toString() + "..mm" + d1);
                    Log.w("datecompa11", "" + pickitodate_txt!!.getText().toString() + "..mm" + d2);


                    if (spinner_statusl!!.selectedItem.equals("All")) {
                        spinnertxt = ""
                    } else if (spinner_statusl!!.selectedItem.equals("Pending")) {
                        spinnertxt = "O"
                        Log.w("api2_s1", ".." + spinnertxt)

                    } else if (spinner_statusl!!.selectedItem.equals("partial")) {
                        spinnertxt = "OC"
                        Log.w("api2_s2", ".." + spinnertxt)

                    } else if (spinner_statusl!!.selectedItem.equals("Completed")) {
                        spinnertxt = "C"
                        Log.w("api2_s3", ".." + spinnertxt)
                    }


                    if (so_number_filter_pickl!!.text.toString().isNotEmpty()
                    ) {
                        soNum = so_number_filter_pickl!!.text.toString()

                    } else {
                        soNum = "";
                    }
                    if (d1!!.compareTo(d2) > 0) {

                        // if (pickifromdate_txt!!.getText().toString()!!.compareTo(pickitodate_txt!!.getText().toString()) > 0) {
                        Toast.makeText(
                            this,
                            "From date should not be greater than to date",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Log.w("spinna11", "" + soNum + customerStr)
                        if (custFilterAutol!!.text.isEmpty()) {
                            customerStr = ""
                        }
                        var zoneMutipleCodeStr = ""
                        if (zoneMutipleCode!!.isNotEmpty()) {
                            zoneMutipleCodeStr = zoneMutipleCode!!
                        } else {
                            zoneMutipleCodeStr = zoneStrCode!!
                        }
                        Log.w("selectdateSOToaa", "" + fromdateShared + todateShared)
                        getpicklist_Detail(
                            customerStr!!, "", fromdateShared!!, todateShared!!, soNum!!,
                            spinnertxt!!, zoneMutipleCodeStr!!
                        )
                    }

//                    if(picklistNew.size > 0){
//                        if(spinnertxt.equals("O")) {
//                            val picklistStatus = picklistNew.filter { it.pickListStatus.equals("O") } as ArrayList<PickIistResponseNew>
//                            pickListNewAdapter!!.updateList(picklistStatus)
//                        }
//                        else if(spinnertxt.equals("C")) {
//                            val picklistStatus = picklistNew.filter { it.pickListStatus.equals("C") } as ArrayList<PickIistResponseNew>
//                            pickListNewAdapter!!.updateList(picklistStatus)
//                        }
//                        else if(spinnertxt.equals("R")) {
//                            val picklistStatus = picklistNew.filter { it.pickListStatus.equals("Y") && it.pickListStatus.equals("P")
//                            } as ArrayList<PickIistResponseNew>
//                            pickListNewAdapter!!.updateList(picklistStatus)
//                        }
//                        else if(spinnertxt.equals("")) {
//                            pickListNewAdapter!!.updateList(picklistNew)
//                        }
//                    }
//                    else {
//                        emptytxt()
//                    }
                } else {
                    emptytxt()
                    Toast.makeText(this, "Select from and to date", Toast.LENGTH_LONG)
                        .show()
                }

            }

//            R.id.searchBar_pick ->
//            {
////                search_ed!!.addTextChangedListener(object : TextWatcher {
////                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
////                        Log.e("seacff",""+s.toString())
////
////                    }
////                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
////                        Log.e("seacii",""+s.toString())
////
////                    }
////                    override fun afterTextChanged(s: Editable) {
////                        if (s != null && s.length > 0) {
////                            Log.e("searchgg",""+s.toString())
////                            getPicklistapi(fromdateApi, todateApi, s.toString(), pick_type_api)
////                        }
////                    }
////                })
////        search_ed!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
////            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
////                Log.e("searchff",""+search_ed!!.getText().toString())
////                getPicklistapi(fromdateApi, todateApi, search_ed!!.getText().toString(), pick_type_api)
////
////            }
////            false
////        })
//                Log.e("searchpi","")
//            }

        }
    }

    fun emptytxt() {
        picklistinvoice_rv!!.visibility = View.GONE
        totalSize!!.visibility = View.INVISIBLE
        emptytxt!!.visibility = View.VISIBLE
        spinner_statusl!!.setSelection(0)
        searchFilterView!!.visibility = View.GONE
        //  searchProduct!!.isEnabled = false
    }

    fun cleartxt() {
        // pickifromdate_txt!!.setText(CommonMethods.getCurrentTime())
        // pickitodate_txt!!.setText(CommonMethods.getCurrentTime())

        //  current_FromDateApi = CommonMethods.getCurrentDateApiNOSpace()
        // current_ToDateApi = CommonMethods.getCurrentDateApiNOSpace()
        custFilterAutol!!.setText("")
        customerStr = ""
        spinnertxt = ""
        so_number_filter_pickl!!.setText("")
    }

    fun visibletxt() {
        picklistinvoice_rv!!.visibility = View.VISIBLE
        totalSize!!.visibility = View.VISIBLE
        emptytxt!!.visibility = View.GONE
        // searchProduct!!.isEnabled = true
    }
//    fun getPicklistapi(fromdate:String,todate:String,status: String?,type:String?){
//        val picklistmodel = PickListInvoieRequest("", "",status!!,type!!,fromdate,todate,"","")
//
//      //  val picklistmodel = PickListInvoieRequest("", status!!,fromdate,"INV",todate,"","")
//        presenter?.pickListInvoiceApiCall(this, picklistmodel)
//        CommonMethods.showProgressDialog(this)
//    }

    //    public void setLocationDataToAdapter(ArrayList<String> arrayList) {
    //        // Creating ArrayAdapter using the string array and default spinner layout
    //        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SalesReturnActivity.this, android.R.layout.simple_spinner_item, arrayList);
    //        // Specify layout to be used when list of choices appears
    //        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    //        // Applying the adapter to our spinner
    //        locationSpinner.setAdapter(arrayAdapter);
    //        locationSpinner.setOnItemSelectedListener(this);
    //    }
    fun setZoneAdapter(arrayList: ArrayList<AddressZoneModel>) {

        val nameList: ArrayList<MultipleSelectSpinnerPojo> = arrayList.map {
            MultipleSelectSpinnerPojo(it.name, false)
        } as ArrayList<MultipleSelectSpinnerPojo>

        Log.w("cg_spinner:", zoneStrName.toString())
        nameList.forEach {
            if (zoneStrCode!!.isNotEmpty() && it.text.contains(zoneStrName.toString())) {
                it.isSelected = true
                it.isEnabled = false
            }
        }

        zoneSpinner!!.buildCheckedSpinner(nameList) { selectedPositionList, displayString ->
//                tvSelectedPosition.text = "Selected position:  $selectedPositionList" //if kotlin, python selecteed:returned postion will be 0,2
//                tvDispString.text = "Display String:  $displayString"
            Log.w("zoneSpinnermulti:", displayString.trim())

            var selectedList = TextUtils.split(displayString.trim(), ",")

            var filterList = arrayList.filter { selected ->
                selectedList.contains(selected.name)
            }

            Log.w("zoneSpinnermulti21:", filterList.toString())

            zoneMutipleCode = filterList.joinToString(",") {
                it.code
            }
            Log.w("zoneSpinnermulti2v:", zoneMutipleCode.toString())
        }
    }

    @Throws(JSONException::class)
    private fun setAddressZone() {
        // Initialize a new RequestQueue instance
        val requestQueue = Volley.newRequestQueue(this)
        val url = "http://158.140.143.87:78/api/ProductList/DeliveryAddressZoneList?Requestdata={}"
        // Initialize a new JsonArrayRequest instance
        addressZoneList = arrayListOf()

        Log.w("Given_url_addr:", url)
        CommonMethods.showProgressDialog(this)

        val jsonArrayRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET, url, null,
            Response.Listener { response: JSONObject ->
                try {
                    GlobalScope.launch {
                        withContext(Dispatchers.Main) {
                            Log.w("Res_is_addr:", response.toString())
                            if (response.length() > 0) {

                                //pDialog.dismiss();
                                val statusCode = response.optString("StatusCode")
                                val statusMessage = response.optString("StatusMessage")

                                if (statusCode == "1") {
                                    val resArray = response.optJSONArray("ResponseData")

                                    for (i in 0 until resArray.length()) {
                                        val jsonObject: JSONObject = resArray.getJSONObject(i)
                                        val obj = resArray.optJSONObject(i)

                                        val model = AddressZoneModel(
                                            jsonObject.optString("Code"),
                                            jsonObject.optString("Name")
                                        )
                                        addressZoneList!!.add(model)
                                    }
                                    withContext(Dispatchers.Main) {
                                        if (addressZoneList!!.size > 0) {
                                            Utils.setAddressZonelist(addressZoneList)
                                            setZoneAdapter(addressZoneList!!)
                                        }
                                    }

                                } else {
                                    Toast.makeText(
                                        this@NewPickListActivity,
                                        statusMessage,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }
                    CommonMethods.cancelProgressDialog()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                // Do something when error occurred
                CommonMethods.cancelProgressDialog()
                Log.w("Error_throwing:", error.toString())
                Toast.makeText(
                    this,
                    "Server Error,Please check",
                    Toast.LENGTH_LONG
                ).show()
            }) {
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                val creds = java.lang.String.format(
                    "%s:%s",
                    Constants.API_SECRET_CODE,
                    Constants.API_SECRET_PASSWORD
                )
                val auth = "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.DEFAULT)
                params["Authorization"] = auth
                return params
            }
        }
        jsonArrayRequest.retryPolicy = object : RetryPolicy {
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
        requestQueue.add(jsonArrayRequest)
    }

    private fun showProductdialog() {
        val li = LayoutInflater.from(this)
        val promptsView: View = li.inflate(R.layout.dialog_qty, null)

        val alertDialogBuilder = AlertDialog.Builder(this)
        val qty = promptsView.findViewById<View>(R.id.dialog_Tot_qty) as TextView
        val ed_qty = promptsView.findViewById<View>(R.id.qty_ed) as EditText
        val save_pick = promptsView.findViewById<View>(R.id.save_dailog_pick) as Button

        alertDialogBuilder.setView(promptsView)
        val dialog: Dialog = alertDialogBuilder.create()
        dialog.setCancelable(true)
        dialog.show()
        // close.setOnClickListener { dialog.dismiss() }
//        save_pick.setOnClickListener {
//            val qtytxt: Int = qtyedit.getText().toString().toInt()
//            val itemprice = 10.0
//            display((qtytxt + 1).toString(), itemprice)
//        }
    }

    //    override fun initView() {
//
//    }
    @Throws(JSONException::class)
    private fun getVendorList() {
        // CommonMethods.showProgressDialog(this)
        val jsonObject = JSONObject()
        //  jsonObject.put("WhsCode", whsCode)

        val requestQueue = Volley.newRequestQueue(this)

        val url =
            Utils.getBaseUrl(this@NewPickListActivity) + "vendorList/vendorList?Requestdata=$jsonObject"

        Log.w("url_vendorlist:", url)

        supplierlist = ArrayList()
        searchableCustomerList = ArrayList()

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET,
            url,
            null,
            Response.Listener { response: JSONObject ->
                try {
                    GlobalScope.launch {

                        Log.w("url_vendorlist_res:", response.toString())
                        val statusCode = response.optString("StatusCode")
                        val statusMsg = response.optString("StatusMessage")

                        if (statusCode == "1") {
                            val responseData = response.optJSONArray("ResponseData")!!

                            if (responseData!!.length() > 0) {
                                for (i in 0 until responseData.length()) {
                                    val obj = responseData.optJSONObject(i)

                                    val model = SupplierModel1(
                                        obj.optString("CustomerCode"),
                                        obj.optString("CustomerName")
                                    )

                                    supplierlist!!.add(model)
                                }

                                withContext(Dispatchers.Main) {
                                    if (supplierlist!!.size > 0) {
                                        Utils.setSupplierList(supplierlist)
//                                        for (i in 0 until  supplierlist!!.size) {
//                                            searchableCustomerList!!.add(supplierlist!!.get(i).supplierName + "~" + supplierlist!!.get(i).supplierCode)
//                                            setDataToAdapter(searchableCustomerList)
//                                        }
                                    }
                                }
                            }
                        }
                        CommonMethods.cancelProgressDialog()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error: VolleyError ->
                // Do something when error occurred
                CommonMethods.cancelProgressDialog()
                Log.w("Error_throwing:", error.toString())
            }) {
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                val creds = java.lang.String.format(
                    "%s:%s",
                    Constants.API_SECRET_CODE,
                    Constants.API_SECRET_PASSWORD
                )
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

    @Throws(JSONException::class)
    private fun getCustomerList() {
        // CommonMethods.showProgressDialog(this)
        val jsonObject = JSONObject()
        jsonObject.put("GroupCode", "")

        val requestQueue = Volley.newRequestQueue(this)
        val url = Utils.getBaseUrl(this@NewPickListActivity) + "CustomerList/customerList"

        Log.w("url_custlist:", url + jsonObject)

        customerlist = ArrayList()
        searchableCustomerList = ArrayList()

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            url,
            jsonObject,
            Response.Listener { response: JSONObject ->
                try {
                    GlobalScope.launch {

                        Log.w("url_custlist_res:", response.toString())
                        val statusCode = response.optString("statusCode")
                        val statusMsg = response.optString("statusMessage")

                        if (statusCode == "1") {
                            val responseData = response.optJSONArray("responseData")!!

                            if (responseData!!.length() > 0) {
                                for (i in 0 until responseData.length()) {
                                    val obj = responseData.optJSONObject(i)

                                    val model = SupplierModel1(
                                        obj.optString("customerCode"),
                                        obj.optString("customerName")
                                    )

                                    customerlist!!.add(model)
                                }

                                withContext(Dispatchers.Main) {
                                    if (customerlist!!.size > 0) {
                                        // Utils.setSupplierList(supplierlist)
                                        for (i in 0 until customerlist!!.size) {
                                            searchableCustomerList!!.add(
                                                customerlist!!.get(i).customerName + "~" + customerlist!!.get(
                                                    i
                                                ).customerCode
                                            )
                                            setDataToAdapter(searchableCustomerList)
                                        }

                                    }
                                }
                            }
                        }
                        CommonMethods.cancelProgressDialog()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error: VolleyError ->
                // Do something when error occurred
                CommonMethods.cancelProgressDialog()
                Log.w("Error_throwing:", error.toString())
            }) {
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                val creds = java.lang.String.format(
                    "%s:%s",
                    Constants.API_SECRET_CODE,
                    Constants.API_SECRET_PASSWORD
                )
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

    fun setDataToAdapter(arrayList: ArrayList<String>?) {
        try {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                arrayList!!
            )
            custFilterAutol!!.setAdapter(adapter)
            custFilterAutol!!.setOnItemClickListener(OnItemClickListener { adapterView, view, i, l ->
                val supplierValue = adapterView.getItemAtPosition(i).toString().trim { it <= ' ' }
                    .split("~".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//            supplier_name = supplierValue[0].trim { it <= ' ' }
                customerStr = supplierValue[1].trim { it <= ' ' }
                custFilterAutol!!.clearFocus()
                Log.w("customerStr11:", customerStr!!)
            })
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        //Execute your code here
        val intent = Intent(applicationContext, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }


}
