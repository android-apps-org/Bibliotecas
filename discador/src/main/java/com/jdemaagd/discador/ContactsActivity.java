package com.jdemaagd.discador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.VisibleForTesting;

public class ContactsActivity extends Activity {

    static final String KEY_PHONE_NUMBER = "key_phone_number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setResult(Activity.RESULT_OK, createResultData("896-745-231"));
        finish();
    }

    @VisibleForTesting
    static Intent createResultData(String phoneNumber) {
        final Intent resultData = new Intent();
        resultData.putExtra(KEY_PHONE_NUMBER, phoneNumber);
        return resultData;
    }
}