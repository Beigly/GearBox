package info.gearboxgame.gearbox;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.ArrayList;

import static android.graphics.Bitmap.*;

/**
 * Created by beigly on 28.06.2015.
 */
public class MainThread extends Thread{

    public   static ArrayList<Obstacle> obs = new ArrayList<Obstacle>() ;
    public   static ArrayList<Gear> gears = new ArrayList<Gear>();

    public  static Mgr    mgr    ;
    public  static Cursor cursor  ;
    public  static   int   time  ,stageNo ,pgear ,ptime ;
    public  static   int  sscore   ,score ;
    public  static   String  comm ,uname  ;
    public  static boolean sameuser ;

    private  Background bkg ;
    private boolean running   ;
    private Bitmap bit ;
    private Canvas canvas ;
    private int state   ;
    private int interval ;
    private boolean mgrSwich ;
   
    public MainThread(   )   {
        super();

        bkg = new Background( GBApplication.Rec.getDw()  ,    GBApplication.Rec.getDh() );
        int wi = (int) GBApplication.Rec.getDw() ;
        int hi = (int) GBApplication.Rec.getDh() ;
        cursor = new Cursor(300,300,GBApplication.dens) ;
        mgr = new Mgr( );
        time = 0 ;
        score = 0 ;sscore = 0 ;
        comm = "" ;
        state = 0 ;
        obs   = new ArrayList<Obstacle>();
        gears = new ArrayList<Gear>();

         bit = Bitmap.createBitmap( wi, hi, Config.ARGB_8888);
         canvas = new Canvas(bit);

        bkg.update(2.f, 0.f, 0.f);
        bkg.draw(canvas);
        MainActivity.iv.post(new Runy(bit));


    }



    @Override
    public void run() {
        long period ,startTime   ;

        interval = 0 ;

        if (sameuser) { stageNo = MainActivity.loder.Init();

        }
        else {stageNo = MainActivity.loder.loadStage(stageNo);
            score =  score + sscore; ;
        }
        if (stageNo < 1) {
            stageNo = MainActivity.loder.loadStage(1);
            sscore = 0 ;
        }

        GBApplication.Rec.hd(3, comm,0,10);
        GBApplication.Rec.hd(2,"Stage : "+ Integer.toString(stageNo),0,6);

        GBApplication.winMode = 0 ;
         state = 1 ;


        while (running) {

            startTime = System.nanoTime();
            try {  this.sleep(100);  } catch (Exception e) {  e.printStackTrace();    }

                  update();
            try {  this.sleep(100);  } catch (Exception e) {  e.printStackTrace();    }
                  canvas = Draw(canvas);

            MainActivity.iv.post(new Runy(bit));

            period = (System.nanoTime() - startTime) / 1000000;

            if (period < 250) {
                try {
                    this.sleep(250 - period);
                } catch (InterruptedException e) {      e.printStackTrace(); }
            }

            period = (System.nanoTime() - startTime) / 1000000;
            float step = (float) period;
            if (GBApplication.GRec.isRotSw()) GBApplication.GRec.setRotStep(step * 0.006f);
            else GBApplication.GRec.setRotStep(0);


            if (GBApplication.winMode > 0) {     continue;         }

            if (state!=1) state = processState(state);

            state = TimeScoreUpdate(step, state);


            if (mgrSwich){

                    mgr.update();
                    state = StageCheck(state);

            }

        }


    }
    private  int processState(int st){
        int outst = 1 ;
        Intent Gointend ;
        switch (st){

            case 0:
                 synchronized(gears) {        gears.clear();}
                 synchronized(obs)   {        obs.clear(); }

                 stageNo++;
                  GBApplication.GRec.resetSeralNo();
                  ControlActivity.actG = -1 ;
                  sscore = score ;
                  stageNo = MainActivity.loder.loadStage(stageNo);
                  if (stageNo == 0) stageNo = MainActivity.loder.loadStage(1);
                  GBApplication.Rec.hd(2,"Stage : "+ Integer.toString(stageNo) ,0,6);
                  GBApplication.Rec.hd(3, comm, 0, 10);
                  score = score + sscore ;
                  MainActivity.loder.saveUser(0, uname + ";" + Integer.toString(stageNo) +";" + Integer.toString(sscore));

                interval = 0 ;
                  outst = 1 ;
                  break;

            case 10 :

                  GBApplication.Rec.hd(0, "You Win ", 0, 7);
                  GBApplication.Rec.hd(1, "", 0, 1);
                   GBApplication.Rec.hd(3," You Win  Stage No: " + Integer.toString(stageNo) + "   Congratulation ! "
                    ,1,9);


                  if (stageNo > 59) {
                      GBApplication.Rec.hd(3, " You Win all of Stages \n Therefore you have learn many thing about Gears. " +
                              " \n Congratulation ! "
                              , 0, 20);
                      stageNo = 1; score = 0 ;
                  }
                  GBApplication.winMode = 1  ;
                  GBApplication.wtrig = true ;
                  outst = 15 ;
                break;
            case 15 :
                if (GBApplication.winMode==0) {
                    GBApplication.winMode = 2;
                    GBApplication.wtrig = true;
                   outst = 16;
                }
                break;
            case 16 :
                if(GBApplication.winMode==0) outst = 0 ;
                break;
            case 50 :


                   GBApplication.winMode = 1  ;
                   GBApplication.wtrig = true ;


                     outst = 60 ;
                break;
            case 60 :
                if (GBApplication.winMode==0) {
                    GBApplication.winMode = 3;
                    GBApplication.wtrig = true;
                    outst = 70;

                }
                 break ;
            case 70:

                if(GBApplication.winMode==0) outst = 80 ;
                break;
            case 80:
                synchronized(gears) {        gears.clear();}
                synchronized(obs)   {        obs.clear(); }
                GBApplication.GRec.resetSeralNo();
                ControlActivity.actG = -1 ;
                stageNo = MainActivity.loder.loadStage(stageNo);
                if (stageNo == 0) stageNo = MainActivity.loder.loadStage(1);
                GBApplication.Rec.hd(2,"Stage : "+ Integer.toString(stageNo)  ,0,6);
                GBApplication.Rec.hd(3,comm,0,10);
                score = score + sscore ;

                interval = 0 ;
                outst = 1 ;
                break;
        }

        return outst;
    }
    private int TimeScoreUpdate(float per ,int st ) {
        if (st!=1) return st ;
        int step =(int)per ;
        interval  += step ;

        if (   interval < 2000 )  return st ;
        time -= interval ;
        interval -= 2000 ;

        if (time <= 0){
            time  += 60000 ;
            score = score - ptime ;
        }

        int  sec2 = time /1000 ;
        int min =  (sec2 / 60) ;
        int sec0 =  sec2 -  min * 60 ;
        String smin = Integer.toString(min) ;
        if (smin.length()==1) smin = "0" + smin ;
        String ssec = Integer.toString(sec0) ;
        if (ssec.length()==1) ssec = "0"+ ssec ;

        String SMS  ="Score :"+ Integer.toString(score)+"   "+smin +":"+ssec+ "  " ;
        GBApplication.Rec.hd(2, SMS, 1,4);

        if (score <= 0){
            score = 0;
            // end stage
            GBApplication.Rec.hd(2,"Score : 0",0,6);
            GBApplication.Rec.hd(1,"Stage Termination.",0,6);
            GBApplication.Rec.hd(0, "Sorry : Game Over  "   , 0, 6);
            GBApplication.Rec.hd(3, "sorry : Score = 0    Try Again Stage : " + Integer.toString(stageNo)
                    , 1, 10);
            return  50 ;   //Game over

        }
        return st ;
    }
    private int  StageCheck(int st){
      synchronized( gears) {
          if ( gears.size() == 0) return 0;
          int result = 10;
          for (int i = 0; i <  gears.size(); i++) {
              Gear g =  gears.get(i);
              if (g.getGearType() == 2) {
                  if (g.getDir() == 0) result = 1;
                  else if (g.getDir() != g.getDir0() && g.getDir0() != 0) {
                      // error msg  destination gear No : 5   rotate in wrong direction
                      String ts = "CW";
                      if (g.getDir() == -1) ts = "CCW";
                      GBApplication.Rec.hd(0, ts + " :is Wrong ", 0, 9);
                      GBApplication.Rec.hd(3,
                              " Gear No: " + Integer.toString(g.getSerial()) + " Rotates in Wrong direction(" + ts + ")"
                              , 0, 12);
                      GBApplication.Rec.hd(1, "Stage Termination. ", 0, 9);
                      g.setBlink(50);
                      result = 50;
                      break;
                  }
                  if (g.getSpeed() == 0) result = 1;
                  else {
                      float gSpd;
                      int tmpint = Math.round(100 * g.getSpeed());
                      float tmpf = (float) tmpint;
                      gSpd = tmpf / 100;
                      if (gSpd != g.getSpeed0() && g.getSpeed0() != 0) {
                          String ts;
                          ts = Float.toString(g.getSpeed0());

                          ts = " " + Float.toString(gSpd) + " Rpm --> (" + ts + ") Rpm";


                          GBApplication.Rec.hd(0, "Rpm :" + Float.toString(g.getSpeed()) + " :is Wrong  " + ts, 0, 9);
                          GBApplication.Rec.hd(3,
                                  "Gear No: " + Integer.toString(g.getSerial()) + " Rotates with Wrong Rotation Speed (Rpm) \n" + ts + "  "
                                  , 0, 12);
                          GBApplication.Rec.hd(1, "Stage Termination. ", 0, 9);
                          g.setBlink(50);
                          result = 50;
                          break;
                      }
                  }

              }
          }

          return result;
      }
//***********************************************************
    }



    public void setRunning(boolean b){        running = b ;    }



    public  void  update(){
        float mx = GBApplication.Rec.getMovex() ;
        float my = GBApplication.Rec.getMovey() ;
        float z  = GBApplication.Rec.getZoom()  ;
        RectF rectf = GBApplication.Rec.getDisply() ;

        bkg.update( GBApplication.Rec.getZoom(),mx ,my );

        synchronized( obs) {
           for (Obstacle b :  obs){
            b.update(rectf,z);
           }
        }
        mgrSwich = false ;
        synchronized( gears) {
            for (Gear gir :  gears) {
                gir.update(rectf, z);
                if (!gir.isChk()) mgrSwich = true;
            }
        }


    }




    public Canvas Draw(Canvas canvas) {

        if (canvas!=null) {
            bkg.draw(canvas);
            int CurLay = GBApplication.GRec.getLc() ;
            synchronized( obs) {
               for (Obstacle b :   obs){
                 if ( b.getLay() <= CurLay)  b.draw(canvas);
                 else break;
               }
            }
            synchronized( gears) {
                for (Gear gir :  gears) {
                    if (gir.getLay() <= CurLay) gir.draw(canvas);
                    else gir.drawAxl(canvas);
                }
            }
            synchronized(cursor) {
               cursor.draw(canvas);
            }
        }
       return canvas ;
    }





    ///////////////////////////////////////////////////////////////////////////////////
    private class Runy implements Runnable {
      private Bitmap bit;

    public Runy(Bitmap b) {this.bit = b;
    }


        @Override
        public void run() {

                MainActivity.iv.setImageBitmap (bit);

        }
    }

}
