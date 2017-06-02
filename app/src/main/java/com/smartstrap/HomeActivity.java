package com.smartstrap;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;


public class HomeActivity extends FragmentActivity {

    private FragmentManager fragMgr;
    private FragmentTransaction transaction;
    private FragmentHome fragmentHome = new FragmentHome();
    private FragmentSetting fragmentSetting = new FragmentSetting();
    private View.OnClickListener changeView = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            transaction = fragMgr.beginTransaction();
            switch (view.getId()) {
                case R.id.btnHome:
                    transaction.replace(R.id.frameLayout, fragmentHome, "fragment1");

                    break;
                case R.id.btnSetting:
                    transaction.replace(R.id.frameLayout, fragmentSetting, "fragment2");
                    break;
            }
//呼叫commit讓變更生效。
            transaction.commit();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragMgr = getSupportFragmentManager();
        fragMgr.beginTransaction()
                .add(R.id.frameLayout, fragmentHome)
                .commit();

        ImageButton btnHome = (ImageButton)findViewById(R.id.btnHome);
        ImageButton btnSetting = (ImageButton)findViewById(R.id.btnSetting);
        btnHome.setOnClickListener(changeView);
        btnSetting.setOnClickListener(changeView);
    }


}