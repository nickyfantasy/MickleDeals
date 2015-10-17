package com.mickledeals.datamodel;

import com.mickledeals.R;
import com.mickledeals.activities.MDApplication;
import com.mickledeals.utils.Constants;
import com.mickledeals.utils.JSONHelper;
import com.mickledeals.utils.Utils;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by Nicky on 12/7/2014.
 */
public class CouponInfo implements Serializable{

    public int mId;
    public String mCoverPhotoUrl = "";
    public String mThumbnailPhotoUrl = "";
    public String mDescription = "";
    public String mDescriptionCh = "";
    public String mFinePrint = "";
    public String mFinePrintCh = "";
    public String mExpiredDate = "";
    public String mExpiredDays = "";

    public double mPrice;
    public boolean mLimited;
    public boolean mActive;

    public boolean mPurchased;
    public boolean mAvailable = true; //false will show coupon sold out
    public boolean mSaved;
    public String mPurchaseId = "";

    public BusinessInfo mBusinessInfo;

    public int mStatus = Constants.COUPON_STATUS_DEFAULT;


    public CouponInfo(JSONObject jsonobject) {
        mId = JSONHelper.getInteger(jsonobject, "id");
        mCoverPhotoUrl = JSONHelper.getString(jsonobject, "coverPhotoUrl");
        mThumbnailPhotoUrl = JSONHelper.getString(jsonobject, "thumbnailPhotoUrl");
        mDescription = JSONHelper.getString(jsonobject, "title");
        mDescriptionCh = JSONHelper.getString(jsonobject, "chineseTitle");
        mFinePrint = JSONHelper.getString(jsonobject, "finePrint");
        mFinePrintCh = JSONHelper.getString(jsonobject, "chineseFinePrint");
        mExpiredDate = JSONHelper.getString(jsonobject, "expireDate");
        mExpiredDays = JSONHelper.getString(jsonobject, "expireDays");

        mPrice = JSONHelper.getDouble(jsonobject, "price");
        mLimited = JSONHelper.getBoolean(jsonobject, "limited");
        mActive = JSONHelper.getBoolean(jsonobject, "active");
        JSONObject businessInfoObject = JSONHelper.getJSONObject(jsonobject, "company");
        if (businessInfoObject != null) {
            mBusinessInfo = new BusinessInfo(businessInfoObject);
            mBusinessInfo.mCoupons.add(this);
        }
        //user specific
        mSaved = JSONHelper.getBoolean(jsonobject, "favorite");
        mAvailable = JSONHelper.getBoolean(jsonobject, "available");
    }

    public String getDescription() {
        return (Utils.isChineseLocale() && !(mDescriptionCh == null || mDescriptionCh.trim().isEmpty())) ? mDescriptionCh : mDescription;
    }

    public String getFinePrint() {
        return (Utils.isChineseLocale() && !(mFinePrintCh == null || mFinePrintCh.trim().isEmpty())) ? mFinePrintCh : mFinePrint;
    }

    //this is for card adapter
    public String getDisplayedPrice() {
//        MDApplication.sAppContext.getResources().getString(R.string.free)

        String str = null;

        if (mPrice == 0) {
            str = "Free";
        } else if (mPrice < 1) {
            str = (int)(mPrice * 100) + MDApplication.sAppContext.getResources().getString(R.string.cent_symbol);
        } else if (mPrice * 10 % 10 == 0) {
            str = "$" + (int) mPrice;
        } else {
            str = "$" + mPrice;
        }

         return str;
    }


    public String getLocaledDisplayedPrice() {

        String str = null;

        if (mPrice == 0) {
            str = MDApplication.sAppContext.getResources().getString(R.string.free);
        } else if (mPrice < 1) {
            str = (int)(mPrice * 100) + MDApplication.sAppContext.getResources().getString(R.string.cent_symbol);
        } else if (mPrice * 10 % 10 == 0) {
            str = "$" + (int) mPrice;
        } else {
            DecimalFormat df = new DecimalFormat("#.00");
            str = "$" + df.format(mPrice);
        }

        return str;
    }


}
