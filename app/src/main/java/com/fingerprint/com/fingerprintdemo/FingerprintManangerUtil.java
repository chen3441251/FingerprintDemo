package com.fingerprint.com.fingerprintdemo;

import android.content.Context;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

/*
 * @创建者   chenchao
 * @创建时间 2019/3/26 15:17
 * @描述     ${TODO}
 *
 **/
public class FingerprintManangerUtil {


    private static FingerprintManagerCompat mFingerprintManagerCompat;
    private static CancellationSignal       mCancellationSignal;

    private FingerprintManangerUtil() {
    }

    /**
     * 指纹识别监听
     */
    interface FingerprintListener {
        //不支持指纹识别
        void onNonsupport();

        //硬件支持但是还未设置指纹
        void onEnrollFailed();

        //开始识别(显示浮层)
        void onAuthenticationStart();

        //指纹识别成功
        void onAuthenticationSuccessed(FingerprintManagerCompat.AuthenticationResult result);

        /**
         * 验证失败回调
         * 指纹识别失败后，指纹传感器不会立即关闭指纹验证，系统会默认提供5次重试机会，即调用5次onAuthenticationFailed()后才会
         * 调用onAuthenticationError
         */
        void onAuthenticationFailed();

        /**
         * 验证出错时回调，指纹传感器关闭一段时间，具体时间根据厂商不同有所区别
         *
         * @param errMsgId
         * @param errorStr
         */
        void onAuthenticationError(int errMsgId, CharSequence errorStr);

        /**
         * 验证帮助回调
         *
         * @param helpMsgId
         * @param helpStr
         */
        void onAuthenticationHelp(int helpMsgId, CharSequence helpStr);
    }

    public abstract static class FingerprintListenerAdapter implements FingerprintListener {
        @Override
        public void onNonsupport() {

        }

        @Override
        public void onEnrollFailed() {

        }

        @Override
        public void onAuthenticationStart() {

        }

        @Override
        public void onAuthenticationSuccessed(FingerprintManagerCompat.AuthenticationResult result) {

        }

        @Override
        public void onAuthenticationFailed() {

        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errorStr) {

        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpStr) {

        }
    }

    /**
     * @param context
     * @param fingerprintListener
     */
    public static void startFingerprinterVerification(Context context, final FingerprintListener fingerprintListener) {
        //创建fingerprintmanager对象
        mFingerprintManagerCompat = FingerprintManagerCompat.from(context);
        //硬件是否支持指纹识别
        if (mFingerprintManagerCompat == null || !mFingerprintManagerCompat.isHardwareDetected()) {
            if (fingerprintListener != null) {
                fingerprintListener.onNonsupport();
            }
            return;
        }
        //是否录入了指纹
        if (!mFingerprintManagerCompat.hasEnrolledFingerprints()) {
            if (fingerprintListener != null) {
                fingerprintListener.onEnrollFailed();
            }
            return;
        }
        //可以进行指纹识别
        if (fingerprintListener != null) {
            fingerprintListener.onAuthenticationStart();
        }
        //创建指纹识别取消对象
        mCancellationSignal = new CancellationSignal();
        mFingerprintManagerCompat.authenticate(null, 0, mCancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                super.onAuthenticationError(errMsgId, errString);
                //指纹验证错误，指纹传感器会关闭一段时间
                if (fingerprintListener != null) {
                    fingerprintListener.onAuthenticationError(errMsgId,errString);
                }
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                super.onAuthenticationHelp(helpMsgId, helpString);
                if (fingerprintListener != null) {
                    fingerprintListener.onAuthenticationHelp(helpMsgId,helpString);
                }
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                if (fingerprintListener != null) {
                    fingerprintListener.onAuthenticationSuccessed(result);
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                if (fingerprintListener != null) {
                    fingerprintListener.onAuthenticationFailed();
                }
            }
        },null);

    }

    /**
     * 取消指纹识别操作
     */
    public static void cancel(){
        if (mCancellationSignal!=null && !mCancellationSignal.isCanceled()) {
            mCancellationSignal.cancel();
        }
    }
}
