package com.fingerprint.com.fingerprintdemo;

import android.content.DialogInterface;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View viewById = getWindow().getDecorView().findViewById(android.R.id.content);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAuthVerification();

            }
        });
    }

    private void startAuthVerification() {
        FingerprintManangerUtil.startFingerprinterVerification(getApplicationContext(), new FingerprintManangerUtil.FingerprintListenerAdapter() {
            @Override
            public void onNonsupport() {
                super.onNonsupport();
                Toast.makeText(getApplicationContext(),"不支持指纹识别",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEnrollFailed() {
                super.onEnrollFailed();
                Toast.makeText(getApplicationContext(),"未设置指纹",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationStart() {
                super.onAuthenticationStart();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("指纹识别")
                        .setMessage("请验证指纹")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FingerprintManangerUtil.cancel();
                            }
                        });
                mAlertDialog = builder.create();
                mAlertDialog.show();
            }

            @Override
            public void onAuthenticationSuccessed(FingerprintManagerCompat.AuthenticationResult result) {
                super.onAuthenticationSuccessed(result);
                if (mAlertDialog!=null&&mAlertDialog.isShowing()) {
                    mAlertDialog.dismiss();
                }
                Toast.makeText(getApplicationContext(),"指纹验证成功:"+result,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(),"指纹验证失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errorStr) {
                super.onAuthenticationError(errMsgId, errorStr);
                Toast.makeText(getApplicationContext(),"指纹验证错误:"+errorStr,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpStr) {
                super.onAuthenticationHelp(helpMsgId, helpStr);
                Toast.makeText(getApplicationContext(),"指纹验证帮助:"+helpStr,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
