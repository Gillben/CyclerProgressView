package com.ricky.cyclerprogressview;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.ricky.cyclerprogressview.view.CustomCircleProgressView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private CustomCircleProgressView circleProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        circleProgressView = (CustomCircleProgressView) findViewById(R.id.progress);
        ValueAnimator mAnimator = ValueAnimator.ofFloat(0,100);
        mAnimator.setDuration(5000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleProgressView.updateCurrentProgress((Float) animation.getAnimatedValue());
            }
        });
        mAnimator.start();
        circleProgressView.setLoadingCompletedListener(new CustomCircleProgressView.LoadingCompletedListener() {
            @Override
            public void complete() {
                Log.e(TAG, "complete: 加载完成");
            }
        });

    }
}
