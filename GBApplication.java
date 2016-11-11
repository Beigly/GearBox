package info.gearboxgame.gearbox;

import android.app.Application;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import java.util.ArrayList;


public class GBApplication extends Application {



    public  static  int winMode ;
    public  static  boolean  wtrig ;
    public  static  CtrRec Rec  ;
    public  static float dens ;
    public  static CtrGear  GRec ;


    @Override
    public void onCreate() {
        super.onCreate();

        Rec = new CtrRec() ;
        // find display property
        DisplayMetrics metrics = new DisplayMetrics();
        metrics =  getResources().getDisplayMetrics();
        float wid = metrics.widthPixels ;
        float hei = metrics.heightPixels ;
        dens = metrics.density;

        Rec.setWH(wid, hei);

        GRec = new CtrGear();



        wtrig = false ;



    }



}
