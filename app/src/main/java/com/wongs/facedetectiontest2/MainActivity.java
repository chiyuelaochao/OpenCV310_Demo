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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

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

        if (!checkSupportFileExist(FACE_CASCADE_NAME)) {
            copyResToSdcard("haarcascade_frontalface_default.xml", FACE_CASCADE_NAME);
        } else {
            Log.e(TAG, "support file is exist!");
        }

        if (!checkSupportFileExist(EYES_CASCADE_NAME)) {
            copyResToSdcard("haarcascade_eye_tree_eyeglasses.xml", EYES_CASCADE_NAME);
        } else {
            Log.e(TAG, "support file is exist!");
        }
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

    /*
* 将raw里的文件copy到sd卡下
* */

    String DATABASE_PATH = "/storage/emulated/0/data/";

    public void copyResToSdcard(String fileName, String outFileName) {//name为sd卡下制定的路径
        try {
            File dir = new File(DATABASE_PATH);
            if (!dir.exists()) {
                dir.mkdir();
            }
            if (!(new File(outFileName)).exists()) {
//                InputStream is = getResources().openRawResource(rawFileId);
                InputStream is = getResources().getAssets().open(fileName);
                FileOutputStream fos = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                Log.e(TAG, "copy support files to storage!");
                fos.close();
                is.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception : " + e.getMessage());
        }
    }

    public static final String FACE_CASCADE_NAME = "/storage/emulated/0/data/haarcascade_frontalface_default.xml";
    public static final String EYES_CASCADE_NAME = "/storage/emulated/0/data/haarcascade_eye_tree_eyeglasses.xml";

    public boolean checkSupportFileExist(String FilePath) {
        return new File(FilePath).exists();
    }
}
