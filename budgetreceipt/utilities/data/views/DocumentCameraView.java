package com.example.budgetreceipt.utilities.data.views;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;

import org.opencv.android.JavaCameraView;

import java.io.FileOutputStream;
import java.util.List;

public class DocumentCameraView extends JavaCameraView implements PictureCallback {

    private static final String TAG = "DocumentCameraView";
    private String mPictureFileName;

    public DocumentCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public List<String> getEffectList() {
        return mCamera.getParameters().getSupportedColorEffects();
    }

    public boolean isEffectSupported() {
        return (mCamera.getParameters().getColorEffect() != null);
    }

    public boolean isEffectSupported(String effect) {
        List<String> effectList = getEffectList();
        for(String str: effectList) {
            if(str.trim().contains(effect))
                return true;
        }
        return false;
    }

    public String getEffect() {
        return mCamera.getParameters().getColorEffect();
    }

    public void setEffect(String effect) {
        Camera.Parameters params = mCamera.getParameters();
        params.setColorEffect(effect);
        mCamera.setParameters(params);
    }

    public List<Size> getResolutionList() {
        return mCamera.getParameters().getSupportedPreviewSizes();
    }

    public List<Size> getPictureResolutionList() {
        return mCamera.getParameters().getSupportedPictureSizes();
    }

    public void setMaxPictureResolution() {
        int maxWidth=0;
        Size curRes=null;
        for ( Size r: getPictureResolutionList() ) {
            Log.d(TAG,"supported picture resolution: "+r.width+"x"+r.height);
            if (r.width>maxWidth) {
                maxWidth=r.width;
                curRes=r;
            }
        }

        if (curRes!=null) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPictureSize(curRes.width, curRes.height);
            mCamera.setParameters(parameters);
            Log.d(TAG, "selected picture resolution: " + curRes.width + "x" + curRes.height);
        }

        return;
    }



    public void setMaxPreviewResolution() {
        int maxWidth=0;
        Size curRes=null;

        mCamera.lock();

        for ( Size r: getResolutionList() ) {
            if (r.width>maxWidth) {
                Log.d(TAG,"supported preview resolution: "+r.width+"x"+r.height);
                maxWidth=r.width;
                curRes=r;
            }
        }

        if (curRes!=null) {
            setResolution(curRes);
            Log.d(TAG, "selected preview resolution: " + curRes.width + "x" + curRes.height);
        }

        return;
    }

    public void setResolution(Size resolution) {
        disconnectCamera();
        mMaxHeight = resolution.height;
        mMaxWidth = resolution.width;
        connectCamera(getWidth(), getHeight());
        Log.d(TAG,"resolution: "+resolution.width+" x "+resolution.height);
    }

    public Size getResolution() {
        return mCamera.getParameters().getPreviewSize();
    }


    public void setFlash(boolean stateFlash) {
        /* */
        Camera.Parameters par = mCamera.getParameters();
        par.setFlashMode(stateFlash ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(par);
        Log.d(TAG,"flash: " + (stateFlash?"on":"off"));
        // */
    }

    public void takePicture(final String fileName) {
        Log.i(TAG, "Taking picture");
        this.mPictureFileName = fileName;
        // 캡처를 수행할 때 대기열이 비어 있지 않으면 포스트뷰와 JPEG가 동일한 버퍼로 전송됩니다.
        mCamera.setPreviewCallback(null);

        // PictureCallback은 현재 클래스에 의해 구현됩니다.
        mCamera.takePicture(null, null, this);
    }

    public void takePicture(PictureCallback callback) {
        Log.i(TAG, "Taking picture");

        mCamera.takePicture(null, null, callback);

    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.i(TAG, "Saving a bitmap to file");
        // 카메라 미리 보기가 자동으로 중지되었습니다. 다시 시작함.
        mCamera.startPreview();
        mCamera.setPreviewCallback(this);

        // 이미지를 파일로 쓰기(JPEG 형식)
        try {
            FileOutputStream fos = new FileOutputStream(mPictureFileName);

            fos.write(data);
            fos.close();

        } catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }

    }
}
