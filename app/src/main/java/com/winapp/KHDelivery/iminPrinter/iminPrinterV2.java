package com.winapp.KHDelivery.iminPrinter;

import android.content.Context;

import com.winapp.KHDelivery.db.DBHelper;
import com.winapp.KHDelivery.model.SettingsModel;
import com.winapp.KHDelivery.utils.SessionManager;
import com.winapp.KHDelivery.utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class iminPrinterV2 {
    private Context context;
    private String company_name;
    private String company_code;
    private String company_address1;
    private String company_address2;
    private String company_address3;
    private String company_phone;
    private String company_gst;
    private SessionManager session;
    private HashMap<String, String> user;
    public ArrayList<SettingsModel> settingsList;
    private DBHelper dbHelper;
    private String showUserName = "";
    private String showSignature = "";
    public static String shortCodeStr = "" ;

    private int height = 100;

    private String showLogo = "";
    private String showReturn = "";
    private String showUom = "";
    private String showQrCode = "";
    private String showStamp = "";
    private String latLongLoc = "";
    private String payNow = "";
    private String salesManName = "";
    private String userMiddleName = "";
    private String salesManPhone = "";
    private String salesManMail = "";
    private String salesManOffice = "";
    private SharedPreferenceUtil sharedPreferenceUtil;
    int finalHeight = 0;
    private final int invoiceDefaultHeight = 60;

    private int invoiceBottomLine = 50;
    private final int invoiveSubTotalHeight = 30;
    private int invoiveReturnHeight = 0;
    private int invoivePaynowHeight = 0;
    private int invoiveSalesManHeight = 0;

    private final int invoiceLineHeight = 10;
    private String bankCode = "";
    private String cheque = "";

}
