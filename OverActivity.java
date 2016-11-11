package info.gearboxgame.gearbox;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class OverActivity extends Activity {
    private ImageView imgov , imgbk  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GBApplication.wtrig = false ;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_over);
        imgov = (ImageView)findViewById(R.id.imggo);
        imgbk = (ImageView)findViewById(R.id.imgbak);
        imgbk.setVisibility(View.INVISIBLE);
        imgov.setVisibility(View.INVISIBLE);
        int st = GBApplication.winMode ;
        switch (st){
            case 1 : // halt view
                 break;
            case 2:
                imgbk.setVisibility(View.VISIBLE);
                break;
            case 3 :
                imgov.setVisibility(View.VISIBLE);
                break;
        }



        if (st == 1 ){
               ControlActivity.sndMode = 2;
        }else {
               ControlActivity.sndMode = 0;
        }

    }
    public void ActClsBtn(View v) {
        ControlActivity.sndMode = 1;
        GBApplication.winMode= 0 ;
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ControlActivity.sndMode = 1;
        GBApplication.winMode= 0 ;
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ControlActivity.sndMode = 1;
        GBApplication.winMode= 0 ;
        finish();

    }
}
