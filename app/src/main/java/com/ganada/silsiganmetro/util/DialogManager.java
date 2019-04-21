package com.ganada.silsiganmetro.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.ganada.silsiganmetro.activity.CaptureActivity;

public class DialogManager {
    Context context;
    public DialogManager(Context context) {
        this.context = context;
    }

    public void alertTrainDialog(final String text) {
        String[] arr = {"메뉴1", "메뉴2"};

        AlertDialog.Builder alert = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog);
        alert.setTitle("다이얼로그");
        alert.setItems(arr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                switch(which) {
                    case 0:
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(text));
                        context.startActivity(intent);
                        break;

                    case 1:
                        intent = new Intent(context, CaptureActivity.class);
                        context.startActivity(intent);
                        break;
                }
            }
        });

        AlertDialog alert1 = alert.create();
        alert1.show();
    }
}
