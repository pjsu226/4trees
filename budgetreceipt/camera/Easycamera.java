package com.example.budgetreceipt.camera;
import com.example.budgetreceipt.activities.DocumentScannerActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EasyCamera {
    public static final int REQUEST_CAPTURE = 10;
    public static final String EXTRA_OUTPUT_URI = "Output Uri: ";
    public static final String EXTRA_OUTPUT_IMAGE_WIDTH = "ImageWidth";
    public static final String EXTRA_OUTPUT_IMAGE_HEIGHT = "Image Height:";
    public static final String EXTRA_MARGIN_BY_WIDTH = "Margin By Width: ";
    public static final String EXTRA_MARGIN_BY_HEIGHT = "Margin By Height: ";


    private Intent mCameraIntent;
    private Bundle mCameraBundle;

    public static EasyCamera create(@NonNull Uri destination) {
        return new EasyCamera(destination);
    }

    private EasyCamera( @NonNull Uri destination) {
        mCameraIntent = new Intent();
        mCameraBundle = new Bundle();
        mCameraBundle.putParcelable(EXTRA_OUTPUT_URI, destination);
    }

    /**
     * 프레임 프레임의 여백 설정
     * @param leftRight 너비는 양쪽 값(px)을 기준으로 합니다.
     * @param topBottom 높이는 거리 값(px)의 영향을 받습니다.
     * @return
     */
    public EasyCamera withMarginCameraEdge(int leftRight,int topBottom){
        mCameraBundle.putInt(EXTRA_MARGIN_BY_WIDTH,leftRight);
        mCameraBundle.putInt(EXTRA_MARGIN_BY_HEIGHT,topBottom);
        return this;
    }


    public void start(@NonNull Activity activity) {
        start(activity, REQUEST_CAPTURE);
    }

    

    public void start(@NonNull Activity activity, int requestCode) {
        activity.startActivityForResult(getIntent(activity), requestCode);
    }

    public Intent getIntent(@NonNull Context context) {
        mCameraIntent.setClass(context, DocumentScannerActivity.class);
        mCameraIntent.putExtras(mCameraBundle);
        return mCameraIntent;
    }

    /**
     * 파일의 URI
     * @param intent
     * @return
     */
    public static Uri getOutput(@NonNull Intent intent) {
        return intent.getParcelableExtra(EXTRA_OUTPUT_URI);
    }

    /**
     * 사진의 너비
     * @param intent
     * @return
     */
    public static int getImageWidth(@NonNull Intent intent){
        return intent.getIntExtra(EXTRA_OUTPUT_IMAGE_WIDTH,0);
    }

    /**
     * 사진의 높이
     * @param intent
     * @return
     */
    public static int getImageHeight(@NonNull Intent intent){
        return intent.getIntExtra(
                EXTRA_OUTPUT_IMAGE_HEIGHT,0);
    }


}
