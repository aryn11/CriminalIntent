package com.example.aryn.criminalintent;

import android.graphics.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;
import java.util.List;

/**
 * Created by user on 12.03.2016.
 */
public class CrimeCameraFragment extends Fragment {
    private static final String TAG = "CrimeCameraFragment";

    private android.hardware.Camera mCamera;
    private SurfaceView mSurfaceView;

    /** Простой алгоритм для получения наибольшего доступного размера.
     * Более мощная версия представлена в файле CameraPreview.java
     * приложения-примера ApiDemos от Android. */
    private android.hardware.Camera.Size getBestSupportedSize(List<android.hardware.Camera.Size> sizes, int width, int height) {
        android.hardware.Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (android.hardware.Camera.Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_camera, container, false);
        Button takePictureCutton = (Button)v.findViewById(R.id.crime_camera_takePictureButton);
        takePictureCutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mSurfaceView = (SurfaceView)v.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // Приказываем камере использовать указанную
                // поверхность как область предварительного просмотра
                try{
                    if(mCamera != null){
                        mCamera.setPreviewDisplay(holder);
                    }
                } catch (IOException ex){
                    Log.e(TAG, "Error setting up preview display", ex);
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                if (mCamera == null) return;

                // Размер поверхности изменился; обновить размер
                // области предварительного просмотра камеры
                android.hardware.Camera.Parameters parameters = mCamera.getParameters();
                android.hardware.Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), width, height);
                parameters.setPreviewSize(s.width, s.height);
                mCamera.setParameters(parameters);

                try {
                    mCamera.startPreview();
                } catch (Exception e){
                    Log.e(TAG, "Could not start preview", e);
                    mCamera.release();
                    mCamera = null;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            mCamera = android.hardware.Camera.open(0);
        } else {
            mCamera = android.hardware.Camera.open();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }
}
