package com.example.bo.nhatkyhoctiengnhat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.bo.nhatkyhoctiengnhat.hometab.HomeTabFragment;


public class TaoMoi extends Activity{

    private FloatingActionButton finishButton;
    private EditText title, mauCau, soNgayLuyenTap;
    private ImageView bellIcon;
    private static int CURRENT_BELL_ICON = android.R.drawable.ic_lock_idle_alarm; // Bell Icon hiện tại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tao_moi);

        prepareLayout();
    }

    /**
     * Định hướng cho các view
     */
    private void prepareLayout() {
        title = (EditText) findViewById(R.id.title_input);
        mauCau = (EditText) findViewById(R.id.grammatical_structure_input);
        soNgayLuyenTap = (EditText) findViewById(R.id.so_ngay_luyen_tap_input);
        bellIcon = (ImageView) findViewById(R.id.initial_bell_icon);

        bellIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBellIcon();
            }
        });

        finishButton = (FloatingActionButton) findViewById(R.id.done_tao_moi);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packInput();
                if (allInputsFilled())
                    finish();
                else alertUser();
            }
        });
    }

    /**
     * Khi click vào Bell Icon, nó sẽ tự đông đổi sang cái khác
     */
    private void switchBellIcon() {
        if (CURRENT_BELL_ICON == android.R.drawable.ic_lock_idle_alarm)
            CURRENT_BELL_ICON = android.R.drawable.ic_lock_idle_charging;
        else CURRENT_BELL_ICON = android.R.drawable.ic_lock_idle_alarm;
        bellIcon.setImageResource(CURRENT_BELL_ICON);

    }

    /**
     *  Bỏ hết tất cả dữ liệu được nhập vào Intent để send back
     */
    private void packInput() {
        Intent dataBack = new Intent();
        dataBack.putExtra(HomeTabFragment.KEY_LAY_TITLE, title.getText().toString());
        dataBack.putExtra(HomeTabFragment.KEY_LAY_SO_NGAY_LUYEN_TAP, soNgayLuyenTap.getText().toString());
        dataBack.putExtra(HomeTabFragment.KEY_LAY_MAU_CAU, mauCau.getText().toString());
        dataBack.putExtra(HomeTabFragment.KEY_LAY_BELL_ICON, CURRENT_BELL_ICON);

        setResult(HomeTabFragment.RESULT_OK, dataBack);
    }

    /**
     * Check whether all inputs are filled, just for the purpose of alerting user
     * @return - true if all inputs are filled
     */
    private boolean allInputsFilled() {
        if (title.getText().toString().equals(""))
            return false;
        if (soNgayLuyenTap.getText().toString().equals(""))
            return false;
        if (mauCau.getText().toString().equals(""))
            return false;
        return true;
    }

    /**
     * Alert user that one or more inputs are not filled in. If it was not intentional, give him or her
     * a chance to fix. Otherwise, finish anyway
     */
    private void alertUser() {
       AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
       builder.setTitle("Alert Dialog");
       builder.setMessage("One or more inputs are not filled in, are you sure you want to finish?")
               .setPositiveButton("Yes, finish anyway", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       TaoMoi.this.finish();
                   }
               })
               .setNegativeButton("Oops, my bad!!!", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();
                   }
               });
    }
}


