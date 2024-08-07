package com.biblia.labibliaa;

import android.app.Activity;
import android.content.Context;

import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm.OnConsentFormDismissedListener;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentInformation.PrivacyOptionsRequirementStatus;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;

public final class GoogleMobileAdsConsentManager {
    private static GoogleMobileAdsConsentManager instance;
    private final ConsentInformation consentInformation;

    private GoogleMobileAdsConsentManager(Context context) {
        this.consentInformation = UserMessagingPlatform.getConsentInformation(context);
    }

    public static GoogleMobileAdsConsentManager getInstance(Context context) {
        if (instance == null) {
            instance = new GoogleMobileAdsConsentManager(context);
        }

        return instance;
    }

    public interface OnConsentGatheringCompleteListener {
        void consentGatheringComplete(FormError error);
    }

    public boolean canRequestAds() {
        return consentInformation.canRequestAds();
    }

    public boolean isPrivacyOptionsRequired() {
        return consentInformation.getPrivacyOptionsRequirementStatus()
                == PrivacyOptionsRequirementStatus.REQUIRED;
    }

    public void gatherConsent(
            Activity activity, OnConsentGatheringCompleteListener onConsentGatheringCompleteListener) {
        // For testing purposes, you can force a DebugGeography of EEA or NOT_EEA.
        ConsentDebugSettings debugSettings =
                new ConsentDebugSettings.Builder(activity)
                        // .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                        .addTestDeviceHashedId(App.getInstance().TEST_DEVICE_HASHED_ID)
                        .build();

        ConsentRequestParameters params =
                new ConsentRequestParameters.Builder().setConsentDebugSettings(debugSettings).build();

        // Requesting an update to consent information should be called on every app launch.
        consentInformation.requestConsentInfoUpdate(
                activity,
                params,
                () ->
                        UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                                activity,
                                formError -> {
                                    // Consent has been gathered.
                                    onConsentGatheringCompleteListener.consentGatheringComplete(formError);
                                }),
                requestConsentError ->
                        onConsentGatheringCompleteListener.consentGatheringComplete(requestConsentError));
    }

    public void showPrivacyOptionsForm(
            Activity activity, OnConsentFormDismissedListener onConsentFormDismissedListener) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity, onConsentFormDismissedListener);
    }
}