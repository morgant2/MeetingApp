package com.meetingapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.meetingapp.R;
import com.meetingapp.fragments.SettingsFragment;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);
        /*
          We will not use setContentView in this activty
         Rather than we will use layout inflater to add view in FrameLayout of our base activity layout
         Adding our layout to parent class frame layout.
         */
        getLayoutInflater().inflate(R.layout.activity_settings, mFrameLayout);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, getCallingActivity().getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
