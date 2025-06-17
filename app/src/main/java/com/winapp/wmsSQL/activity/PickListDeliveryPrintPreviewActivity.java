package com.winapp.wmsSQL.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tscdll.TSCActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.winapp.wmsSQL.R;
import com.winapp.wmsSQL.adapter.DeliveryPickListNewAdapter;
import com.winapp.wmsSQL.adapter.InvoicePrintPreviewAdapter;
import com.winapp.wmsSQL.adapter.PickDeliveryPrintPreviewAdapter;
import com.winapp.wmsSQL.model.PicklistDeliveryPrintPreviewModel;
import com.winapp.wmsSQL.thermalprinter.PrinterUtils;
import com.winapp.wmsSQL.tscprinter.TSCPrinterActivity;
import com.winapp.wmsSQL.utils.Constants;
import com.winapp.wmsSQL.utils.ImageUtil;
import com.winapp.wmsSQL.utils.SessionManager;
import com.winapp.wmsSQL.utils.Utils;
import com.winapp.wmsSQL.zebraprinter.ZebraPrinterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class  PickListDeliveryPrintPreviewActivity extends AppCompatActivity {

    private String companyId;
    private String locationCode;
    private SweetAlertDialog pDialog;
    private String invoiceNumber;
    private SessionManager session;
    private HashMap<String, String> user;
    private ArrayList<PicklistDeliveryPrintPreviewModel> invoiceHeaderDetails;
    private ArrayList<PicklistDeliveryPrintPreviewModel.InvoiceList> invoiceList;
    private RecyclerView invoiceListView;
    private PickDeliveryPrintPreviewAdapter adapter;
    private TextView invoiceNumberText;
    private TextView invoiceDateText;
    private TextView customerCodetext;
    private TextView customerNameText;
    private TextView addressText;
    private TextView deliveryAddressText;
    private TextView deliveryAddr_print_txtl ;
    private LinearLayout deliveryAddr_print_layl;
    private TextView companyNametext;
    private TextView companyAddress1Text;
    private TextView companyAddress2Text;
    private TextView companyAddress3Text;
    private TextView companyPhoneText;
    private TextView companyGstText;
    private TextView userTxt;
    private TextView dateTimeTxt;
    private String company_name;
    private String company_address1;
    private String company_address2;
    private String company_address3;
    private RelativeLayout rootLayout;
    private LinearLayout addressLayout;
    SharedPreferences sharedPreferences;
    String printerMacId;
    String printerType;
    TSCActivity TscDll;
    AlertDialog alert11;
    DialogInterface alertInterface;
    public static int REQUEST_PERMISSIONS = 154;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    private static final String TAG = PickListDeliveryPrintPreviewActivity.class.getSimpleName();
    public static final String SAMPLE_FILE = "android_tutorial.pdf";
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;
    static BottomSheetBehavior behavior;
    LinearLayout shareLayout;
    File pdfFile;
    LinearLayout printLayout;
    Button cancelButton;
    private static final int PERMISSION_REQUEST_CODE = 100;
    public String outstanding_amount="0.0";
    public String delDateStr="";

    LinearLayout address1Layout;
    LinearLayout address2Layout;
    LinearLayout address3Layout;
    LinearLayout address4Layout;

    TextView customerAddress1;
    TextView customerAddress2;
    TextView customerAddress3;
    TextView customerAddress4;
    String company_phone;
    String company_gst,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        setContentView(R.layout.activity_picklist_delivery_print_preview);
        setTitle();
        TscDll = new TSCActivity();
        session = new SessionManager(this);
        user = session.getUserDetails();
        companyId = user.get(SessionManager.KEY_COMPANY_CODE);
        locationCode=user.get(SessionManager.KEY_LOCATION_CODE);
        company_name = user.get(SessionManager.KEY_COMPANY_NAME);
        company_address1 = user.get(SessionManager.KEY_ADDRESS1);
        company_address2 = user.get(SessionManager.KEY_ADDRESS2);
        company_address3 = user.get(SessionManager.KEY_ADDRESS3);
        company_phone=user.get(SessionManager.KEY_PHONE_NO);
        company_gst=user.get(SessionManager.KEY_COMPANY_REG_NO);

        username = user.get(SessionManager.KEY_USER_NAME);

        Log.w("activity_cg",getClass().getSimpleName().toString());

        invoiceListView = findViewById(R.id.invoiceList);
        invoiceNumberText = findViewById(R.id.sr_no);
        invoiceDateText = findViewById(R.id.sr_date);
        customerCodetext = findViewById(R.id.customer_code);
        customerNameText = findViewById(R.id.customer_name_value);
        deliveryAddressText = findViewById(R.id.delivery_address);
        deliveryAddr_print_txtl = findViewById(R.id.deliveryAddr_print_txt);
        deliveryAddr_print_layl = findViewById(R.id.deliveryAddr_print_lay);
        companyNametext = findViewById(R.id.company_name);
        companyAddress1Text = findViewById(R.id.address1);
        companyAddress2Text = findViewById(R.id.address2);
        companyAddress3Text=findViewById(R.id.address3);
        companyGstText=findViewById(R.id.gst_no);
        companyPhoneText=findViewById(R.id.mobile_no);
        addressLayout = findViewById(R.id.adressLayout);
        rootLayout = findViewById(R.id.rootLayout);
        userTxt = findViewById(R.id.del_preview_user);
        dateTimeTxt = findViewById(R.id.del_preview_date);

        sharedPreferences = getSharedPreferences("PrinterPref", MODE_PRIVATE);
        printerType = sharedPreferences.getString("printer_type", "");
        printerMacId = sharedPreferences.getString("mac_address", "");
        shareLayout=findViewById(R.id.share_layout);
        printLayout=findViewById(R.id.print_layout);
        cancelButton=findViewById(R.id.cancel);

        address1Layout=findViewById(R.id.address1Layout);
        address2Layout=findViewById(R.id.address2Layout);
        address3Layout=findViewById(R.id.address3Layout);
        address4Layout=findViewById(R.id.address4Layout);

        customerAddress1=findViewById(R.id.cus_address1);
        customerAddress2=findViewById(R.id.cus_address2);
        customerAddress3=findViewById(R.id.cus_address3);
        customerAddress4=findViewById(R.id.cus_address4);

        pdfView = findViewById(R.id.pdfView);
        View bottomSheet = findViewById(R.id.design_bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);

        checkPermission();
        requestPermission();

        Log.w("Printer_Mac_Id:", printerMacId);
        Log.w("Printer_Type:", printerType);

        userTxt.setText(username);
        if (getIntent() != null) {
            invoiceNumber = getIntent().getStringExtra("salesCodeDel");
            outstanding_amount=getIntent().getStringExtra("outstandingAmount");
            delDateStr=getIntent().getStringExtra("pick_DatetimeDel");
            Log.w("delDateStr1:", delDateStr);
            if (invoiceNumber != null) {
                try {
                    getInvoiceDetails(invoiceNumber);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(!delDateStr.isEmpty()) {
                dateFormatHour(delDateStr);
            }
        }

        printLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                printPreview();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
    }

    private void dateFormatHour(String dateStr){
        DateFormat readFormat = new SimpleDateFormat( "yyyyMMdd_HHmmss");
        DateFormat writeFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss a");
        Date date = null;
        try {
            date = readFormat.parse( dateStr );
        } catch ( ParseException e ) {
            e.printStackTrace();
        }

        String formattedDate = "";
        if( date != null ) {
            formattedDate = writeFormat.format( date );
        }
        dateTimeTxt.setText(formattedDate);

        Log.w("pickdel_date",""+formattedDate);
    }

    private void getInvoiceDetails(String invoiceNumber) throws JSONException {
        // Initialize a new RequestQueue instance
        JSONObject jsonObject = new JSONObject();
       // jsonObject.put("CompanyCode", companyId);
        jsonObject.put("InvoiceNo", invoiceNumber);
        jsonObject.put("LocationCode",locationCode);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Utils.getBaseUrl(this) + "InvoiceDetails";
        // Initialize a new JsonArrayRequest instance
        Log.w("Given_url:", url+jsonObject.toString());
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Generating Print Preview...");
        pDialog.setCancelable(false);
        pDialog.show();
        invoiceHeaderDetails = new ArrayList<>();
              JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonObject, response -> {
                    try {
                        Log.w("picklist_deli_res:", response.toString());

                        //  Log.w("DetailsResponse::", response.toString());
                        String statusCode=response.optString("statusCode");
                        if (statusCode.equals("1")){
                            JSONArray responseData=response.getJSONArray("responseData");
                            JSONObject object=responseData.optJSONObject(0);
//
                            PicklistDeliveryPrintPreviewModel model = new PicklistDeliveryPrintPreviewModel();

                            model.setInvoiceNumber(object.optString("invoiceNumber"));
                            model.setInvoiceDate(object.optString("invoiceDate"));
                            model.setCustomerCode(object.optString("customerCode"));
                            model.setCustomerName(object.optString("customerName"));
                            model.setAddress(object.optString("address1") + object.optString("address2") + object.optString("address3"));
                            model.setDeliveryAddress(object.optString("shipAddress1")+object.optString("shipAddress2")+object.optString("shipAddress3")+
                                    object.optString("shipStreet"));
                            String delieryAddr = object.optString("shipAddress2")+object.optString("shipAddress3")+
                                    object.optString("shipStreet");
                            model.setSubTotal(object.optString("subTotal"));
                            model.setNetTax(object.optString("taxTotal"));
                            model.setNetTotal(object.optString("netTotal"));
                            model.setTaxType(object.optString("taxType"));
                            model.setTaxValue(object.optString("taxPerc"));
                            model.setOutStandingAmount(object.optString("totalOutstandingAmount"));
                            model.setPaymentTerm(object.optString("paymentTerm"));
                            model.setBalanceAmount(object.optString("balanceAmount"));
                            Utils.setInvoiceOutstandingAmount(object.optString("balanceAmount"));
                            Utils.setInvoiceMode("Invoice");
                            model.setBillDiscount(object.optString("billDiscount"));
                            model.setItemDiscount(object.optString("totalDiscount"));
                            model.setAddress1(object.optString("address1"));
                            model.setAddress2(object.optString("address2"));
                            model.setAddress3(object.optString("address3"));
                            model.setAllowDeliveryAddress(object.optString("showShippingAddress"));

                            if(object.optString("showShippingAddress").equalsIgnoreCase("Yes")){
                                deliveryAddr_print_layl.setVisibility(View.VISIBLE);
                                deliveryAddr_print_txtl.setText(object.optString("shipAddress1")+object.optString("shipAddress2")+object.optString("shipAddress3")+
                                        object.optString("shipStreet"));
                            }
                            else{
                                deliveryAddr_print_layl.setVisibility(View.GONE);
                            }
                            model.setAddressstate(object.optString("street")+" "+
                                    object.optString("block")+" "+object.optString("city"));
                            model.setAddresssZipcode(object.optString("countryName")+" "+object.optString("state")+" "
                                    +object.optString("zipcode"));

                            model.setSoNumber(object.optString("soNumber"));
                            model.setSoDate(object.optString("soDate"));
                            model.setDoDate(object.optString("doDate"));
                            model.setDoNumber(object.optString("doNumber"));
                            String signFlag=object.optString("signFlag");
                            if (signFlag.equals("Y")){
                                String signature=object.optString("signature");
                                Utils.setSignature(signature);
                                createSignature();
                            }else {
                                Utils.setSignature("");
                            }

                            JSONArray detailsArray=object.optJSONArray("invoiceDetails");
                            invoiceList = new ArrayList<>();

                            for (int i=0;i<detailsArray.length();i++) {
                                JSONObject detailObject = detailsArray.optJSONObject(i);
                                if (Double.parseDouble(detailObject.optString("quantity")) > 0) {
                                    PicklistDeliveryPrintPreviewModel.InvoiceList invoiceListModel = new PicklistDeliveryPrintPreviewModel.InvoiceList();
                                    invoiceListModel.setProductCode(detailObject.optString("productCode"));
                                    invoiceListModel.setDescription(detailObject.optString("productName"));
                                    invoiceListModel.setLqty(detailObject.optString("unitQty"));
                                    invoiceListModel.setCqty(detailObject.optString("cartonQty"));
                                    invoiceListModel.setNetQty(detailObject.optString("quantity"));
                                    invoiceListModel.setExcQty(detailObject.optString("exc_Qty"));
                                    invoiceListModel.setNetQuantity(detailObject.optString("netQuantity"));
                                    invoiceListModel.setFocQty(detailObject.optString("foc_Qty"));
                                    invoiceListModel.setUomCode(detailObject.optString("uomCode"));
                                    invoiceListModel.setSaleType("");
                                    if (detailObject.optString("bP_CatalogNo") != null) {
                                        invoiceListModel.setCustomerItemCode(detailObject.optString("bP_CatalogNo"));
                                    }
                                    invoiceListModel.setReturnQty(detailObject.optString("returnQty"));
                                    invoiceListModel.setCartonPrice(detailObject.optString("cartonPrice"));
                                    invoiceListModel.setUnitPrice(detailObject.optString("price"));
                                    double qty = Double.parseDouble(detailObject.optString("quantity"));
                                    double price = Double.parseDouble(detailObject.optString("price"));

                                    double nettotal = qty * price;
                                    invoiceListModel.setTotal(String.valueOf(nettotal));
                                    invoiceListModel.setPricevalue(String.valueOf(price));

                                    invoiceListModel.setPcsperCarton(detailObject.optString("pcsPerCarton"));
                                    invoiceListModel.setItemtax(detailObject.optString("totalTax"));
                                    invoiceListModel.setSubTotal(detailObject.optString("subTotal"));
                                    invoiceList.add(invoiceListModel);
                                    Log.w("invoicSizeEntr1","");
                                }
                            }

                                Log.w("invoicSize",""+invoiceList.size());
                             model.setInvoiceList(invoiceList);
                                invoiceHeaderDetails.add(model);

                            if (invoiceList.size() > 0) {
                                setInvoiceAdapter();
                            }
                            pDialog.dismiss();
                        }else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
            // Do something when error occurred
            pDialog.dismiss();
            Log.w("Error_throwing:", error.toString());
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                String creds = String.format("%s:%s", Constants.API_SECRET_CODE, Constants.API_SECRET_PASSWORD);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    private void createSignature(){
        if (Utils.getSignature()!=null && !Utils.getSignature().isEmpty()){
            try {
                ImageUtil.saveStamp(this,Utils.getSignature(),"Signature");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setInvoiceAdapter() {
        for (PicklistDeliveryPrintPreviewModel model : invoiceHeaderDetails) {
            invoiceNumberText.setText(model.getInvoiceNumber());
            invoiceDateText.setText(model.getInvoiceDate());
            customerCodetext.setText(model.getCustomerCode());
            customerNameText.setText(model.getCustomerName());
            deliveryAddressText.setText(model.getAddressstate() + " " + model.getAddresssZipcode());

//            if (!model.getAddress1().isEmpty()){
//                address1Layout.setVisibility(View.VISIBLE);
//                customerAddress1.setText(model.getAddress1());
//            }
//            if (!model.getAddress2().isEmpty()){
//                address2Layout.setVisibility(View.VISIBLE);
//                customerAddress2.setText(model.getAddress2());
//            }
//            if (!model.getAddress3().isEmpty()){
//                address3Layout.setVisibility(View.VISIBLE);
//                customerAddress3.setText(model.getAddress3());
//            }
//            if (!model.getAddressstate().isEmpty() ) {
//                if (!model.getAddresssZipcode().isEmpty()) {
//                    address4Layout.setVisibility(View.VISIBLE);
//                    customerAddress4.setText(model.getAddressstate() + " " + model.getAddresssZipcode());
//                } else {
//                    address4Layout.setVisibility(View.VISIBLE);
//                    customerAddress4.setText(model.getAddressstate());
//                }
//            }
//            else{
//                    if (!model.getAddresssZipcode().isEmpty() ) {
//                        address4Layout.setVisibility(View.VISIBLE);
//                        customerAddress4.setText(model.getAddresssZipcode());
//                    }
//                }
//        }
//        companyNametext.setText(company_name);
//
//        if (!company_address1.isEmpty()){
//            companyAddress1Text.setVisibility(View.VISIBLE);
//            companyAddress1Text.setText(company_address1);
//        }
//
//        if (!company_address2.isEmpty()){
//            companyAddress2Text.setVisibility(View.VISIBLE);
//            companyAddress2Text.setText(company_address2);
//        }
//
//        if (!company_address3.isEmpty()){
//            companyAddress3Text.setVisibility(View.VISIBLE);
//            companyAddress3Text.setText(company_address3);
//        }
//
//        if (!company_phone.isEmpty()){
//            companyPhoneText.setText("TEL : "+company_phone);
//            companyPhoneText.setVisibility(View.VISIBLE);
//        }
//
//        if (!company_gst.isEmpty()){
//            companyGstText.setText("CO REG NO : "+company_gst);
//            companyGstText.setVisibility(View.VISIBLE);
//        }
            invoiceListView.setHasFixedSize(true);
            // RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            invoiceListView.setLayoutManager(new LinearLayoutManager(PickListDeliveryPrintPreviewActivity.this, LinearLayoutManager.VERTICAL, false));
            adapter = new PickDeliveryPrintPreviewAdapter(PickListDeliveryPrintPreviewActivity.this, invoiceList);
            invoiceListView.setAdapter(adapter);
            rootLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.print_menu, menu);
        MenuItem action_print = menu.findItem(R.id.action_print);
        action_print.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_print) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                // Device does not support Bluetooth
                Toast.makeText(getApplicationContext(), "This device does not support bluetooth", Toast.LENGTH_SHORT).show();
            } else if (!mBluetoothAdapter.isEnabled()) {
                // Bluetooth is not enabled :)
                Toast.makeText(getApplicationContext(), "Enable bluetooth and connect the printer", Toast.LENGTH_SHORT).show();
            } else {
                // Bluetooth is enabled
                if (!printerType.isEmpty()) {
                    //showPrintAlert();
                } else {
                    Toast.makeText(getApplicationContext(), "Please configure Printer", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void printPreview(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(getApplicationContext(), "This device does not support bluetooth", Toast.LENGTH_SHORT).show();
        } else if (!mBluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled :)
            Toast.makeText(getApplicationContext(), "Enable bluetooth and connect the printer", Toast.LENGTH_SHORT).show();
        } else {
            // Bluetooth is enabled
            if (!printerType.isEmpty()) {
             //   showPrintAlert();
            } else {
                Toast.makeText(getApplicationContext(), "Please configure Printer", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            boolean_permission=true;
            return true;
        } else {
            boolean_permission=false;
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            boolean_permission=true;
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    public void closeAlert(){
       if (alertInterface!=null){
            alertInterface.dismiss();
        }
    }


    public void setTitle(){
        //Customize the ActionBar
        final ActionBar abar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Delivery PickList");
        Objects.requireNonNull(abar).setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setHomeButtonEnabled(true);
    }

    public void printInvoice(){
        try {
            TscDll.openport(printerMacId);
            //  TscDll.downloadpcx("UL.PCX");
            // TscDll.downloadbmp("Triangle.bmp");
            // TscDll.downloadttf("ARIAL.TTF");
            TscDll.setup(70, 110, 4, 4, 0, 0, 0);
            TscDll.clearbuffer();
            TscDll.sendcommand("SET TEAR ON\n");
            TscDll.sendcommand("SET COUNTER @1 1\n");
            TscDll.sendcommand("@1 = \"0001\"\n");
            TscDll.sendcommand("TEXT 100,300,\"3\",0,1,1,@1\n");
            //   TscDll.sendcommand("PUTPCX 100,300,\"UL.PCX\"\n");
            //   TscDll.sendcommand("PUTBMP 100,520,\"Triangle.bmp\"\n");
            TscDll.sendcommand("TEXT 100,760,\"2\",0,15,15,\"Sample print Text\"\n");
            TscDll.sendcommand("TEXT 25,190,”TST24.BF2\",0,1,1,”日傑茶坊 TEL:0000–0000\"");
            TscDll.sendcommand("TEXT 0,0,\"FONT001\",0,1,1,\"THIS IS 桂花烏龍奶茶\"\n");
            TscDll.barcode(100, 100, "128", 100, 1, 0, 3, 3, "123456789");
            TscDll.printerfont(100, 250, "3", 0, 1, 1, "Test Printing");
            String status = TscDll.status();
            Log.w("Status_Print:",status);
            TscDll.printlabel(2, 1);
            TscDll.sendfile("zpl.txt");
            TscDll.closeport();
            Toast.makeText(getApplicationContext(),"Printed Successfully",Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}