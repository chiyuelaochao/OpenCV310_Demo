package com.wongs.facedetectiontest2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    public static final String TAG = "MainActivity";
    private JavaCameraView view;

    BaseLoaderCallback callback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS:
                    view.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (JavaCameraView) findViewById(R.id.view);
        view.setVisibility(View.VISIBLE);
        view.setCvCameraViewListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Log.e(TAG, "OpenCv load success!");
            callback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        } else {
            Log.e(TAG, "OpenCv load failed!");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, callback);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (view != null)
            view.disableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (view != null)
            view.disableView();
    }


    private Mat mRgba;

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        OpencvClass.faceDetection(mRgba.getNativeObjAddr());
        return mRgba;
    }
}
