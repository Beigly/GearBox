package info.gearboxgame.gearbox;


import android.app.Activity;



import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.support.v4.view.GestureDetectorCompat;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import java.util.Timer;
import java.util.TimerTask;


public class ControlActivity  extends Activity implements SeekBar.OnSeekBarChangeListener {
    public  static  MediaPlayer mp0,mp1,mp2  ;
    public  static  int sndMode ;
    public  static  boolean sndEn ;
    private Handler hndl ;
    private GestureDetectorCompat gdc ;
    private ScaleGestureDetector sgd ;
    private TextView  hd1 ,hd2 ,hd3 ,hd4 ;
    private Button  btnAlp  ,btnTet , btnToo ,btnBet , btnDel  ;
    private ImageButton  btnLai ,btnsnd;
    private LinearLayout layly  ;
    private RelativeLayout layall ;
    private Button[] btnl = new Button[6];
    private SeekBar sbV ,sbH ;
    private boolean sbVE ,sbHE ;

    private int logstat  ;
    static  int actG ;
    private boolean  layEn  , layallEn;
    private Timer timer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_control);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        hndl = new Handler() ;
        gdc = new GestureDetectorCompat(getApplicationContext(),hndl);
        gdc.setOnDoubleTapListener(hndl);
        sgd = new ScaleGestureDetector(getApplicationContext(),hndl);




        sbV = (SeekBar) findViewById(R.id.seekBarV);
        sbV.setOnSeekBarChangeListener(this);
        hd1 =(TextView)findViewById(R.id.tvHead1);
        hd2 =(TextView)findViewById(R.id.tvHead2);
        hd3 =(TextView)findViewById(R.id.tvHead3);
        hd4 =(TextView)findViewById(R.id.tvHead4);
        layEn = false ;
        layallEn = false ;
        btnAlp =  (Button) findViewById(R.id.btnAlpha);
        btnBet =  (Button) findViewById(R.id.btnBeta);
        btnDel =  (Button) findViewById(R.id.butDel);

        btnTet =  (Button) findViewById(R.id.btnTeta);
        btnToo =  (Button) findViewById(R.id.btnSize);
        btnLai =  (ImageButton) findViewById(R.id.btnlay);
        btnsnd =  (ImageButton) findViewById(R.id.soudbtn);

        layall  = (RelativeLayout)findViewById(R.id.RelativeLayout0);
        layly   = (LinearLayout)findViewById(R.id.layerLay);
        btnl[0] = (Button)findViewById(R.id.btnL1);
        btnl[1] = (Button)findViewById(R.id.btnL2);
        btnl[2] = (Button)findViewById(R.id.btnL3);
        btnl[3] = (Button)findViewById(R.id.btnL4);
        btnl[4] = (Button)findViewById(R.id.btnL5);
        btnl[5] = (Button)findViewById(R.id.btnL6);

        sbH = (SeekBar) findViewById(R.id.seekBarH);
        sbH.setOnSeekBarChangeListener(this);

        logstat = 0 ;
        actG = -1 ;
        EDBtns();

         mp0 = MediaPlayer.create(getBaseContext(), R.raw.start);
         mp1 = MediaPlayer.create(getBaseContext(), R.raw.tik);
         mp1.setLooping(true);
         mp2 = MediaPlayer.create(getBaseContext(), R.raw.end);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
                                      @Override
                                      public void run() {
                                          int chk = GBApplication.Rec.MsgChk();
                                           if (chk>0) msgTrg(chk);
                                           if  (GBApplication.wtrig==true){
                                               GBApplication.wtrig = false ;
                                               Intent goIntent =  new Intent(MainActivity.getAppContext(), OverActivity.class);
                                               startActivity(goIntent);
                                           }
                                          if ( sndEn){
                                              int quiet = 0 ;
                                              if ( mp0.isPlaying()) quiet = 2 ;
                                              if ( mp1.isPlaying()) quiet = 1 ;
                                              if ( mp2.isPlaying()) quiet = 2 ;
                                              switch ( sndMode){
                                                  case 0:
                                                      if (quiet<2){
                                                           mp0.start();
                                                           sndMode = 1 ;
                                                      }
                                                      break;
                                                  case 1:
                                                      if (quiet==0){
                                                           mp1.start();
                                                      }
                                                      break;
                                                  case 2:
                                                      if (quiet<2){
                                                           mp2.start();
                                                           sndMode = 1 ;
                                                      }
                                                      break;

                                              }
                                              
                                          }
                                      }
                                  },0,500   );


    }

    @Override
    protected void onStart() {
        super.onStart();
        actG = -1 ;  logstat =8 ;
        EDBtns() ;

   }

    public void BtnClick(View v){

        int vid = v.getId();
        int index     ;



        switch(vid) {
            case R.id.btnExit:  // Exit
                GBApplication.Rec.hd(0,"", 0, 1);
                logstat =8 ;

                break;

            case R.id.btnZoom:
                if (logstat != 1) {
                    GBApplication.Rec.hd(0, " Zoom ", 1, 2);
                    logstat = 1;

                    sbVE = false;
                    sbV.setMax(GBApplication.Rec.getZx());
                    sbV.setProgress(GBApplication.Rec.getZi());
                    sbVE = true;
                }else logstat = 0;
                break;

            case R.id.btnMove:
                if (logstat != 2) {
                    GBApplication.Rec.hd(0, " Move ", 1, 2);
                    logstat = 2;

                    sbVE = false;
                    sbHE = false;
                    sbV.setMax(GBApplication.Rec.getXmoy());
                    index = (int) GBApplication.Rec.getMovey() / 10;
                    sbV.setProgress(index);
                    sbH.setMax(GBApplication.Rec.getXmox());
                    index = (int) GBApplication.Rec.getMovex() / 10;
                    sbH.setProgress(index);
                    sbVE = true;
                    sbHE = true;
                }else {
                    logstat = 0;

                }

                break;

            case R.id.btnAlpha :

                if (actG > -1 && logstat != 3) {
                    logstat = 3 ;
                    sbVE = false;
                    sbV.setMax(40);
                    float inde = MainThread.gears.get(actG).getAlpha();
                    index = (int) inde;
                    GBApplication.Rec.hd(0, "  Alpha= "+Integer.toString(index) +" mm (Length of Tooth Point)", 1, 4);
                    sbV.setProgress(index);
                    sbVE = true;
                } else {logstat = 0 ;}

                break;

            case R.id.btnBeta :

                if (actG > -1 && logstat != 4 ) {
                    logstat = 4 ;
                    sbVE = false;
                    sbV.setMax(40);
                    float inde = MainThread.gears.get(actG).getBeta();
                    index = (int) inde;
                    GBApplication.Rec.hd(0, " Beta= "+Integer.toString(index)+"  mm (Length of Tooth)", 1, 3);
                    sbV.setProgress(index);
                    sbVE = true;
                }else {logstat = 0 ;}

                break;

            case R.id.btnTeta :

                if (actG > -1 && logstat != 5) {
                    logstat = 5 ;
                    sbVE = false;
                    sbV.setMax(50);
                    float inde = MainThread.gears.get(actG).getTeta();
                    index = (int) inde;
                    GBApplication.Rec.hd(0, " Teta= "+Integer.toString(index)+" : mm (Width of Tooth)", 1, 3);
                    sbV.setProgress(index);
                    sbVE = true;
                }else {logstat = 0 ;}

                break;

            case R.id.btnSize :

                if (actG > -1 && logstat != 6) {
                    logstat = 6 ;
                    sbVE = false;
                    sbV.setMax(50);
                    int inde = MainThread.gears.get(actG).getN();
                    index = inde / 2;
                    GBApplication.Rec.hd(0, " Teeth= "+ Integer.toString(index), 1, 3);
                    sbV.setProgress(index);
                    sbVE = true;
                }else {logstat = 0 ;}

                break;
            case R.id.btnlay :
                    layEn = ! layEn ;
                    if (layEn) layly.setVisibility(View.VISIBLE);
                    else layly.setVisibility(View.INVISIBLE);
                    GBApplication.Rec.hd(0," Layer selection ",1,3);
                    logstat = 7 ;

            break ;
            case R.id.btnIns :
                GBApplication.Rec.hd(0, " Insert Gear ", 1, 4);
                if (  MainThread.cursor.getShow() ) {
                    PointF pf =  MainThread.cursor.getxy();
                    float xr = GBApplication.Rec.getMovex() + (pf.x * GBApplication.Rec.getZoom()  );
                    float yr = GBApplication.Rec.getMovey() + (pf.y * GBApplication.Rec.getZoom()  );
                    //insert gear in layer order
                    Gear G =new Gear(xr, yr   );
                    int N = 0 ;
                    int L = GBApplication.GRec.getLc() ;
                    synchronized(MainThread.gears) {
                       for ( int n = 0 ; n < MainThread.gears.size() ; n++ ){
                           N = n  ;
                           if (MainThread.gears.get(n).getLay() > L ) {   break ;    }
                           N++ ;
                       }

                        MainThread.gears.add(N ,G );
                    }
                    MainThread.score -= MainThread.pgear ;
                    actG = N  ;
                }else{ MainThread.cursor.setShow();     }
                logstat = 0 ;
                break ;
            case R.id.butDel :
                if (actG >-1 && actG < MainThread.gears.size()) {
                    Gear g = MainThread.gears.get(actG);
                    MainThread.mgr.deleteGear(g);
                    logstat = 0 ;actG = -1 ;
                }
                break ;
            case R.id.help :
                 Intent inet = new Intent(this, HelpActivity.class);
                 inet.putExtra("ind" ,0);
                 ControlActivity.this.startActivity(inet);
                 logstat = 0 ;actG = -1 ;
                break ;
            case R.id.soudbtn :
                 sndEn = !  sndEn ;
                if ( sndEn ) {

                           btnsnd.setImageResource(R.drawable.spk2);

                            sndMode = 1;
                }else{
                    btnsnd.setImageResource(R.drawable.spk1);
                    if ( sndMode == 0 ) mp0.pause();
                    if ( sndMode == 1 ) mp1.pause();
                    if ( sndMode == 2 ) mp2.pause();
                }
                logstat = 0 ;actG = -1 ;
                break ;
     

        }
        EDBtns();

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
       switch( logstat ){
           case  0:  break ;
            case 1:   //zoom

                 if (sbVE)  GBApplication.Rec.setZi( progress ) ;
                GBApplication.Rec.hd(0," Zoom : % " + Integer.toString(Math.round(100.f / GBApplication.Rec.getZoom())) ,1,3);


                break;
            case 2: // move
                 if ( seekBar.getId()==sbV.getId()){ //sbV
                     float f = (float )progress * 10 ;
                     if (sbVE ) GBApplication.Rec.setMovey(f);
                     int i1 = (int)GBApplication.Rec.getMovex();
                     int i2 = (int)GBApplication.Rec.getMovey();

                     GBApplication.Rec.hd(0,"move ( " + Integer.toString(i1) + " , " + Integer.toString(i2) + " )",1,3);
                 }
                else { // sbH
                     float f = (float) progress * 10 ;
                     if (sbHE) GBApplication.Rec.setMovex(f);
                     int i1 = (int)GBApplication.Rec.getMovex();
                     int i2 = (int)GBApplication.Rec.getMovey();

                     GBApplication.Rec.hd(0,"move (  " + Integer.toString(i1) + " , " + Integer.toString(i2) + " )",1,3);
                 }
            break ;
            case  3: // alpha
                if ( seekBar.getId()==sbV.getId()){ //sbV
                   float f = (float )progress   ;
                   if (sbVE && actG > -1 ) {
                       synchronized(MainThread.gears) { MainThread.gears.get(actG).setAlpha(f);}
                       GBApplication.Rec.hd(0," Alpha= " + Integer.toString(progress) + " mm (Length of Tooth Point)",1,3);
                   }
               }
            break ;
            case  4: // beta
             if ( seekBar.getId()==sbV.getId()){ //sbV
                 float f = (float )progress   ;
                 if (sbVE && actG > -1 ) {
                     synchronized(MainThread.gears) { MainThread.gears.get(actG).setBeta(f);}
                     GBApplication.Rec.hd(0," Beta= " + Integer.toString(progress) + " mm (Length of Tooth)",1,3);
                 }
             }

             break ;
            case  5: // Teta
             if ( seekBar.getId()==sbV.getId()){ //sbV
                 float f = (float )progress   ;
                 if (sbVE && actG > -1 ) {
                     synchronized(MainThread.gears) {MainThread.gears.get(actG).setTeta(f);}
                     GBApplication.Rec.hd(0," Teta= " + Integer.toString(progress) + " mm (Width of Tooth)",1,3);
                 }
             }

             break ;
            case  6: // Teeth
             if ( seekBar.getId()==sbV.getId()){ //sbV
                 int f =  progress   ;
                 f = 2 * f ;
                 if (sbVE && actG > -1 ) {
                     synchronized(MainThread.gears) {MainThread.gears.get(actG).setN(f);}
                     GBApplication.Rec.hd(0," Teeth = " + Integer.toString(progress),1,3);
                 }
             }

             break ;

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        sgd.onTouchEvent(event);
        gdc.onTouchEvent(event);
        boolean triger = false ;
        if (GBApplication.GRec.getsG() != -1){
            actG = GBApplication.GRec.getsG();
            triger = true ;
            logstat = 0 ;
        }
        if (GBApplication.GRec.getlStat() != -1){
            logstat = GBApplication.GRec.getlStat();
            triger = true ;

        }
        if (triger) {
            GBApplication.GRec.setlStat(-1);
            GBApplication.GRec.setsG(-1);
            EDBtns();
        }

        return true ;

    }


    private void EDBtns() {



        layly.setVisibility(View.INVISIBLE);
        sbH.setVisibility(View.INVISIBLE);
        sbV.setVisibility(View.INVISIBLE);
        if (logstat!= 7) layEn = false ;


        if (actG == -1) {
            btnAlp.setEnabled(false);
            btnBet.setEnabled(false);
            btnTet.setEnabled(false);
            btnToo.setEnabled(false);
            btnDel.setEnabled(false);
        }
        if( actG > -1 && actG < MainThread.gears.size() ) {
             Gear g = MainThread.gears.get(actG);
            if (g.getFil()) {
                btnAlp.setEnabled(false);
                btnBet.setEnabled(false);
                btnTet.setEnabled(false);
                btnToo.setEnabled(false);
                btnDel.setEnabled(false);
            }else {
                btnAlp.setEnabled(true);
                btnBet.setEnabled(true);
                btnTet.setEnabled(true);
                btnToo.setEnabled(true);
                btnDel.setEnabled(true);
            }
            if (logstat < 1 ) {
                String ts = "Normal";
                if (g.getGearType() == 1) ts = "Driver";
                if (g.getGearType() == 2) ts = "Driven";
                GBApplication.Rec.hd(0, "No:" + g.getSerial() + " Teeth:" + Integer.toString(g.getN() / 2) + " Layer:" +
                        Integer.toString(g.getLay() + 1) + " Type:" + ts, 1, 4);
                ts = " ";
                if (g.getSpeed0() > 0) ts = "(" + Float.toString(g.getSpeed0()) + ")";
                if (g.getGearType() == 1) ts = " ";

                int tmpint = Math.round(100 * g.getSpeed());
                float tmpf = (float) tmpint;

                ts = Float.toString(tmpf / 100) + ts;
                String td = " ";
                if (g.getDir0() == 1) td = "(CW)";
                else if (g.getDir0() == -1) td = "(CCW)";
                if (g.getGearType() == 1) td = " ";
                if (g.getDir() == 1) td = "CW" + td;
                else if (g.getDir() == -1) td = "CCW" + td;
                GBApplication.Rec.hd(1, " A=" + Integer.toString((int) g.getAlpha()) + " B=" + Integer.toString((int) g.getBeta())
                        + " T=" + Integer.toString((int) g.getTeta()) + " Rpm:" + ts + " " + td, 1, 4);

            }
        }



        switch (logstat ) {
            case 0:// start and exit
                sbH.setVisibility(View.INVISIBLE);
                sbV.setVisibility(View.INVISIBLE);
                break;
            case 1://zoom

                sbV.setVisibility(View.VISIBLE);
                break;
            case 2://move

                sbH.setVisibility(View.VISIBLE);
                sbV.setVisibility(View.VISIBLE);
                break;
            case 3://Alpha
                if (actG != -1) {
                    sbV.setVisibility(View.VISIBLE);
                    if (MainThread.gears.get(actG).getFil()) sbV.setVisibility(View.INVISIBLE);
                   }
                break;
            case 4://bata
                if (actG != -1) {
                    sbV.setVisibility(View.VISIBLE);
                    if (MainThread.gears.get(actG).getFil()) sbV.setVisibility(View.INVISIBLE);
                }
                break;
            case 5://teta
                if (actG != -1) {
                    sbV.setVisibility(View.VISIBLE);
                    if (MainThread.gears.get(actG).getFil()) sbV.setVisibility(View.INVISIBLE);
                }
                break;
            case 6://size
                if (actG != -1) {
                    sbV.setVisibility(View.VISIBLE);
                    if (MainThread.gears.get(actG).getFil()) sbV.setVisibility(View.INVISIBLE);
                }
                break;
            case 7: //layer
                if (layEn) {
                    layly.setVisibility(View.VISIBLE);

                    int j = GBApplication.GRec.getLx();
                    for(int i = 0 ; i<6 ; i++){
                      if (i<=j) { btnl[i].setVisibility(View.VISIBLE);
                                  btnl[i].setText("Layer "+Integer.toString(i+1));}
                           else { btnl[i].setVisibility(View.INVISIBLE); }
                      btnl[i].setTextColor(Color.BLACK);
                    }
                    j = GBApplication.GRec.getLc();
                    btnl[j].setText("Active");
                    btnl[j].setTextColor(Color.RED);
                }
                break;
            case 8 : //screan clear unclear
                layallEn = ! layallEn ;
                GBApplication.Rec.hd(0,"",0,1);
                for(int k = 0 ;k < layall.getChildCount();k++){
                    if(layallEn) layall.getChildAt(k).setVisibility(View.VISIBLE);
                    else layall.getChildAt(k).setVisibility(View.INVISIBLE);
                }
                if (layallEn) {
                    for (int k = 0; k < 6; k++) {
                        btnl[k].setVisibility(View.INVISIBLE);
                    }
                    layEn = false ;

                    sbH.setVisibility(View.INVISIBLE);
                    sbV.setVisibility(View.INVISIBLE);
                }
                break ;
        }

        if (GBApplication.GRec.getLx() > 0) btnLai.setEnabled(true);
        else btnLai.setEnabled(false);

    }

    public void layBtnClick(View v){
        int vid = v.getId();
        int index ;
        switch(vid) {
            case R.id.btnL1 :
                 GBApplication.GRec.setLc(0);
                break ;
            case R.id.btnL2 :
                GBApplication.GRec.setLc(1);
                break ;
            case R.id.btnL3 :
                GBApplication.GRec.setLc(2);
                break ;
            case R.id.btnL4 :
                GBApplication.GRec.setLc(3);
                break ;
            case R.id.btnL5 :
                GBApplication.GRec.setLc(4);
                break ;
            case R.id.btnL6 :
                GBApplication.GRec.setLc(5);
                break ;
        }
        int j = GBApplication.GRec.getLx();
        for(int i = 0 ; i<6 ; i++){
            if (i<=j) { btnl[i].setVisibility(View.VISIBLE);
                btnl[i].setText("Layer "+Integer.toString(i+1));}
            else { btnl[i].setVisibility(View.INVISIBLE); }
            btnl[i].setTextColor(Color.BLACK);
        }
        j = GBApplication.GRec.getLc();
        btnl[j].setTextColor(Color.RED);
        btnl[j].setText("Active");
        layEn = false ;
        logstat = 7 ;
        EDBtns();
        GBApplication.Rec.hd(0,"Current Layer = " + Integer.toString(GBApplication.GRec.getLc() + 1), 1, 3) ;

    }
    private void msgTrg(int key){
        if  (key>7){
            String ms=GBApplication.Rec.hd(3);
            hd4.post(new h4Runy(ms)) ;

            key -= 8 ;
        }
         if  (key>3){
             String ms=GBApplication.Rec.hd(2);
             hd3.post(new h3Runy(ms)) ;

             key -= 4 ;
         }
        if  (key>1){
            String ms=GBApplication.Rec.hd(1);
            hd2.post(new h2Runy(ms)) ;
            key -= 2 ;
        }
        if  (key==1){
            String ms=GBApplication.Rec.hd(0);
            hd1.post(new h1Runy(ms) );

        }



    }

    private class h4Runy implements Runnable {
        private String st;

        public h4Runy(String s) {this.st = s;  }
        @Override
        public void run() {  hd4.setText(st);  }
    }
    private class h3Runy implements Runnable {
        private String st;

        public h3Runy(String s) {this.st = s;  }
        @Override
        public void run() {  hd3.setText(st);  }
    }
    private class h2Runy implements Runnable {
        private String st;

        public h2Runy(String s) {this.st = s;  }
        @Override
        public void run() {  hd2.setText(st);  }
    }
    private class h1Runy implements Runnable {
        private String st;

        public h1Runy(String s) {this.st = s;  }
        @Override
        public void run() {  hd1.setText(st);  }
    }
    @Override
    public void onBackPressed() {
        timer.cancel();
        setResult(0);
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStop() {
        timer.cancel();
        super.onStop();
        setResult(0);
        finish();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
         MainActivity.loder.save(MainThread.stageNo);
         MainActivity.loder.saveUser(0, MainThread.uname + ";" + Integer.toString(MainThread.stageNo) +
                 ";" + Integer.toString(MainThread.sscore));


          mp0.release();
          mp1.release();
          mp2.release();

    }

}
