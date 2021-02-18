package com.yoc.visx.sdk.mediation;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;

import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.VersionInfo;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;
import com.yoc.visx.sdk.VisxAdManager;
import com.yoc.visx.sdk.util.AdSize;
import com.yoc.visx.sdk.util.PlacementType;
import com.yoc.visx.sdk.BuildConfig;

import java.util.HashMap;
import java.util.Map;

final class MediationUtil {

    /**
     * Private constructor preventing creating instances of this class
     */
    private MediationUtil() {
    }

    private static Map<String, String> paramsMap = new HashMap<>();
    private static com.yoc.visx.sdk.util.AdSize visxAdSize;
    private static final String AUID_KEY = "auid";
    private static final String APP_DOMAIN_KEY = "app_domain";
    private static final String SIZE_KEY = "size";
    public static final String TEST_TAG = "--->";

    /**
     * Getting the SDK Version from
     *
     * @return VersionInfo
     * @see BuildConfig#VERSION_NAME
     * defined in SDK build.gradle and processing for populating
     * @see VersionInfo
     */
    static VersionInfo getVersionInfo(boolean isSDKVersion) {
        Log.i(TEST_TAG, "MediationUtil getSDKVersionInfo()");
        String versionString = BuildConfig.VERSION_NAME;
        String[] splits = versionString.split("\\.");

        if (splits.length >= 4) {
            int major = Integer.parseInt(splits[0]);
            int minor = Integer.parseInt(splits[1]);
            int micro = isSDKVersion ?
                    Integer.parseInt(splits[2]) :
                    Integer.parseInt(splits[2]) * 100 + Integer.parseInt(splits[3]);
            return new VersionInfo(major, minor, micro);
        }

        return new VersionInfo(0, 0, 0);
    }

    /**
     * Creating and setting parameter map from server response from Google Mediation Adapters
     *
     * @param parameters by splitting the response first by ";" for separating different key <> value pair value
     *                   and then by "=" for splitting key and value strings
     *                   (correct string parameters example: auid=910570;app_domain=yoc.com;size=300x250)
     * @see VISXCustomEventBannerGMA#requestBannerAd(Context, CustomEventBannerListener, String
     *, com.google.android.gms.ads.AdSize, MediationAdRequest, Bundle)
     * @see VISXCustomEventInterstitialGMA#requestInterstitialAd(Context, CustomEventInterstitialListener
     *, String, MediationAdRequest, Bundle)
     * <p>
     * Creating and populating parameter map with String key <> String value
     * from String
     */
    static void setParameterMap(String parameters) {
        if (!TextUtils.isEmpty(parameters)) {
            String[] keyValuePairs = parameters.split(";");
            for (String keyValuePair : keyValuePairs) {
                if (!TextUtils.isEmpty(keyValuePair)) {
                    String[] tokens = keyValuePair.split("=");
                    if (!TextUtils.isEmpty(tokens[0]) && !TextUtils.isEmpty(tokens[1])) {
                        paramsMap.put(tokens[0], tokens[1]);
                    }
                }
            }
        } else {
            Log.w(MediationUtil.class.getSimpleName(), "Mediation parameter response null or empty");
        }
    }

    /**
     * Method for getting the appDomain value from
     * serverParameters response that are stored inside paramsMap
     *
     * @return value of appDomain
     * @see MediationUtil#setParameterMap(String)
     */
    static String getAppDomain() {
        String appDomain = "";
        if (paramsMap != null && !TextUtils.isEmpty(paramsMap.get(APP_DOMAIN_KEY))) {
            appDomain = paramsMap.get(APP_DOMAIN_KEY);
        }
        return appDomain;
    }

    /**
     * Method for getting the auid value from
     * serverParameters response that are stored inside paramsMap
     *
     * @return value of auid
     * @see MediationUtil#setParameterMap(String)
     */
    static String getAuid() {
        String auid = "";
        if (paramsMap != null && !TextUtils.isEmpty(paramsMap.get(AUID_KEY))) {
            auid = paramsMap.get(AUID_KEY);
        }
        return auid;
    }

    /**
     * Method for getting the size value from
     * serverParameters response that are stored inside paramsMap and store in
     *
     * @return AdSize
     * @see AdSize object, ready for setting as adSize inside the
     * @see VisxAdManager.Builder#adSize(AdSize)
     *
     * First thing is splitting the 'size' value from the paramsMap by "x", then initiate a new AdSize, with given Size and INLINE PlacementType
     * If SIZE_KEY is null, we will fallback to a 320x50 banner
     * @see MediationUtil#setParameterMap(String)
     */
    static AdSize getBannerAdSize() {
        if (paramsMap != null && !TextUtils.isEmpty(paramsMap.get(SIZE_KEY))) {
            String[] sizeList = paramsMap.get(SIZE_KEY).split("x");
            if (sizeList[0] != null && sizeList[1] != null) {
                visxAdSize = new AdSize(new Size(Integer.parseInt(sizeList[0]), Integer.parseInt(sizeList[1])), PlacementType.INLINE);
            } else {
                visxAdSize = AdSize.SMARTPHONE_320x50;
            }
        } else {
            visxAdSize = AdSize.SMARTPHONE_320x50;
        }
        return visxAdSize;
    }

    /**
     * Method for getting the size value from
     * serverParameters response that are stored inside paramsMap and store in
     *
     * @return AdSize
     * @see AdSize object, ready for setting as adSize inside the
     * @see VisxAdManager.Builder#adSize(AdSize)
     *
     * First thing is splitting the 'size' value from the paramsMap by "x", then initiate a new AdSize, with given Size and INTERSTITIAL PlacementType
     * If SIZE_KEY is null, we will fallback to a 320x480 interstitial
     * @see MediationUtil#setParameterMap(String)
     */
    static AdSize getInterstitialAdSize() {
        if (paramsMap != null && !TextUtils.isEmpty(paramsMap.get(SIZE_KEY))) {
            String[] sizeList = paramsMap.get(SIZE_KEY).split("x");
            if (sizeList[0] != null && sizeList[1] != null) {
                visxAdSize = new AdSize(new Size(Integer.parseInt(sizeList[0]), Integer.parseInt(sizeList[1])), PlacementType.INTERSTITIAL);
            } else {
                visxAdSize = AdSize.INTERSTITIAL_320x480;
            }
        } else {
            visxAdSize = AdSize.INTERSTITIAL_320x480;
        }
        return visxAdSize;
    }

}
