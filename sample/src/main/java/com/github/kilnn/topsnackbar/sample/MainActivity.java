package com.github.kilnn.topsnackbar.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.github.kilnn.topsnackbar.BaseTransientTopBar;
import com.github.kilnn.topsnackbar.TopSnackbar;


public class MainActivity extends AppCompatActivity {

    TopSnackbar topSnackbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullScreenActivityUtil.setFullScreen(this, true, false);
        setContentView(R.layout.activity_main);

        topSnackbar = TopSnackbar.make(
                getWindow().getDecorView().findViewById(Window.ID_ANDROID_CONTENT), "12", TopSnackbar.LENGTH_LONG);
        topSnackbar.addCallback(new BaseTransientTopBar.BaseCallback<TopSnackbar>() {
            @Override
            public void onDismissed(TopSnackbar transientBottomBar, @DismissEvent int event) {
                super.onDismissed(transientBottomBar, event);
                Log.e("Kilnn", "onDismissed");
            }

            @Override
            public void onShown(TopSnackbar transientBottomBar) {
                super.onShown(transientBottomBar);
                Log.e("Kilnn", "onShown");
            }
        });
        findViewById(R.id.btn1)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        topSnackbar.dismiss();
                        topSnackbar.setAction("asdasd", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        topSnackbar.setDuration(BaseTransientTopBar.LENGTH_SHORT);
                        topSnackbar.show();
                    }
                });

        findViewById(R.id.btn2)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        topSnackbar.setDuration(BaseTransientTopBar.LENGTH_INDEFINITE);
                        topSnackbar.show();
                    }
                });
    }
}
