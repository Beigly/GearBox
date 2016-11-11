package info.gearboxgame.gearbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;





public class MainActivity extends Activity {
    private MainThread thread;
    public  static ImageView iv;
    private static Context context;
    public  static Loader loder ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // full screen

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        MainActivity.context = getApplicationContext();

        setContentView(R.layout.main_layout);
        iv = (ImageView) findViewById(R.id.iView);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);



        loder = new Loader( );
        thread = new MainThread();
        thread.setRunning(false);




        Intent intent = new Intent(MainActivity.getAppContext(), StartActivity.class);
        MainActivity.this.startActivityForResult(intent, 0);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            thread.setRunning(false);
            ControlActivity.mp0.release();
            finish();
        }
        if (resultCode == 5) {

            thread.setRunning(true);
            thread.start();
            Intent intent = new Intent(this, ControlActivity.class);
            MainActivity.this.startActivityForResult(intent, 0);

        }
        if (resultCode == 6) {
           ControlActivity.mp0.release();
            finish();

        }
    }

    public static Context getAppContext() {
        return MainActivity.context;
    }





}
