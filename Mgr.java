package info.gearboxgame.gearbox;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;

import java.util.ArrayList;

/**
 * Created by beigly on 07.11.2015.
 */
public class Mgr {

    private ArrayList<RulList> rulL;
    private ArrayList<ConnRec> conRecs;

    private int fDig ,lDig ;
    private float  oDig;
    private boolean gearFixed ;
    private float dens ;


   public Mgr( ){
       conRecs = new ArrayList<ConnRec>();
       rulL    = new ArrayList<RulList>() ;
       this.dens = GBApplication.dens ;
   }


    public void update(){

          Gear g = null;
          for (Gear G : MainThread.gears) {
              if (G.isChk()) {
                  continue;
              } else {
                  g = G;
                  break;
              }
          }
          if (g == null) {
              return;
          }
          synchronized(g) {
          while (!g.isChk()) {

              int pro = g.getProc();
              switch (pro) {
                  case 0:
                      boolean find = false;
                      g.setChk(true);
                      for (int i = rulL.size() - 1; i >= 0; i--) {
                          if ((rulL.get(i).getG1().equals(g)) ||
                                  (rulL.get(i).getG2().equals(g))) {
                              rulL.remove(i);
                              find = true;
                          }
                      }
                      if (find) {
                          reorgRulls();
                      }
                      break;
                  case 1:
                      g.setChk(true);
                      priCollision(g);
                      break;
                  case 2:
                      g.setChk(true);
                      break;
                  case 3:
                      g.setChk(true);
                      Collision(g);

                      break;

              }
          }

      }


   }


    public void deleteGear(Gear G){
        synchronized (rulL) {
            // delete rull rec s
            boolean find = false;
            for (int i = rulL.size() - 1; i >= 0; i--) {
                if ((rulL.get(i).getG1().equals(G)) ||
                        (rulL.get(i).getG2().equals(G))) {
                    rulL.remove(i);
                    find = true;
                }
            }
            // update rulls
            if (find) {
                reorgRulls();
            }
        }
        // delete gear from Arraylist
        synchronized (MainThread.gears){
             MainThread.gears.remove(G);
        }
    }


    public  void priCollision(Gear g) {

        gearFixed = false ;

        boolean p = true;
        for (int i = 0 ; i < 5 ;i++ ){
            p = true;
            conRecs.clear();

            p = axlColl(g) & p ;
            p = girColl(g) & p ;
            p = obsColl(g) & p ;
            if(p  ){ break ;}
        }
        if ( !p ) {
            GBApplication.Rec.hd(3,"Gear No:"+g.getSerial()+" Complex Collision  (insufficient area)",1,4);
            g.setBlink(2);
            g.setFil(false);   return;
        }

        if(g.getFil()){
           g.setProc(2);
        }else {
            return;
        }


        if ( conRecs.size()==0 ) return ;
        g.setProc(3);
    }
    /////////////////////////////////////////////////////////////////

  private  void Collision( Gear G ){
      gearFixed = false ;

      for (ConnRec C : conRecs) {
          int patLv = C.getG().getLv();
          int seral = patLv   ;
          if (patLv == 0) seral += 2000;
          if (!C.isDir()) seral += 1000;
          C.setSer(seral);
          if (seral<1000  ) gearFixed = true ;
          if (seral>=2000 && seral<3000) gearFixed = true ;
      }
      // sort by seral ;
      ConnRec tr0 ,tr1  ;

      for (int i = 0 ; i<conRecs.size() ; i++) {
          tr0 = conRecs.get(i);
          for (int j = i + 1; j < conRecs.size(); j++) {
              tr1 = conRecs.get(j);
              if (tr1.getSer() < tr0.getSer()) {//exchange
                  conRecs.set(i, tr1);
                  conRecs.set(j, tr0);
              }
         }
      }



      int dir = 0 ;
      float spd = 0 ;
      int err = 0 ;
      int lvl = 0 ;
      Gear g0 = conRecs.get(0).getG0();
      Gear g  = conRecs.get(0).getG();
      boolean first = true ;
      for (int inx = 0 ; inx< conRecs.size() ;inx++){
          err = 0 ;
          ConnRec C = conRecs.get(inx);
          int ser = C.getSer();
          g0 = C.getG0();
          g  = C.getG() ;
          if (ser < 1000  ){ // axcle + driver
              if (first){

                  dir = g.getDir();
                   spd = g.getSpeed();
                   lvl = g.getLv()+1;
                  g0.setLv(lvl);
                  g0.setDir(dir);
                  g0.setSpeed(spd);
                   first = false ;
              }else {
                  if ( g.getDir()   != dir ) {  err = 1;       break;   }
                  if ( g.getSpeed() != spd ) {  err = 4;       break;   }
                  if ( g.getLv()    >= lvl ) {  C.setDel(true);         }
              }
          }
          if (ser > 1000 && ser < 2000){ // teeth + driver
              float spd0 =    (float) ( g0.getLanda()/g.getLanda()) * g.getSpeed()   ;
              if (first){

                  dir = C.getG().getDir()*-1;
                  spd = spd0 ;
                  lvl = C.getG().getLv()+1;
                  g0.setLv(lvl);
                  g0.setDir(dir);
                  g0.setSpeed(spd);
                  err=checkGear(C  );

              } else {
                  if ( g.getDir() == dir ) {       err = 1;           break;                 }
                  if ( spd != spd0) {              err = 4;           break;                 }
                  if ( g.getLv() >= lvl) {
                      err = clutch(g0, g);        }


              }
              first = false ;
              if ( err != 0 ) break;
          }
          if (ser >= 2000 && ser < 3000){ // axcle + driven


          }
          if (ser >= 3000  ){ //teeth driven

            //  err=checkGear(C  );
          }
         

      }


      switch(err){
           case 0 : //ok
               break;
           case 1  :// direction err
               GBApplication.Rec.hd(3,"Gears No:"+g.getSerial()+" and No:"+g0.getSerial()+ " Rotation Direction Error", 0, 4);
               conRecs.clear();
               gearReset(g0, g);
               g.setBlink(2);g0.setBlink(2);
               return;
           case 4  :// speed err
                  GBApplication.Rec.hd(3,"Gears No:"+g.getSerial()+" and No:"+g0.getSerial()+ " Two different Rotatation Speed", 0, 4);
                  conRecs.clear();
                  gearReset(g0, g);
                  g.setBlink(2);g0.setBlink(2);
                  return;


           case 10 : // no match father

               conRecs.clear();
               gearReset(g0, g);
               break;
           case 11 : //
               GBApplication.Rec.hd(3,"Gears No:"+g.getSerial()+" and No:"+g0.getSerial()+"  Teeth overlapped",1,4);
               conRecs.clear();
               gearReset(g0, g);
               g.setBlink(2);g0.setBlink(2);
               break;
           case 30 :
               break ;
           case 40 : // not syncronize with two  gears
               GBApplication.Rec.hd(3,"Gears No:"+g.getSerial()+" and No:"+g0.getSerial()+" Not syncronized  ",0,4);
               conRecs.clear();
               gearReset(g0, g);
               g.setBlink(2);g0.setBlink(2);
               return;

      }
          // res == 0
      for (int inx = 0 ; inx< conRecs.size() ;inx++){
              ConnRec C = conRecs.get(inx);
              if (C.isDel()) continue ;
              int ser = C.getSer();

              if (ser < 1000  ){ rulL.add(new RulList(C.getG(),C.getG0())); }
              if (ser > 1000  && ser < 2000){

                  rulL.add(new RulList(C.getG(),C.getG0(), C.getD1(),  C.getD2()  ) );
              }
              if (ser >= 2000 && ser < 3000){rulL.add(new RulList(C.getG0(),C.getG())); }
              if (ser >= 3000  ){      }
      }


      reorgRulls();
      for (int inx = 0 ; inx< conRecs.size() ;inx++){
          ConnRec C = conRecs.get(inx);
          if (C.isDel()) continue ;
          g0 = C.getG0();
          g  = C.getG() ;
          int ser = C.getSer();
          if (ser >= 3000 && g.getLv() < g0.getLv() && g.getFil() ){
              g.setProc(1);
              g.setChk(false);
          }
      }

      conRecs.clear();
      for (RulList R:rulL){

               if (R.isDir() ){  continue;}

               Gear g1 = R.getG1() ;
               float r0 = g1.getRotat() ;
               float perd1 = (float) Math.toDegrees(g1.getLanda()*2);
               while (Math.abs(r0) > perd1 ){
                   if (r0 > 0) r0 = r0 - perd1 ;
                          else r0 = r0 + perd1 ;
               }
               r0 = r0 / perd1 ;

               Gear g2 = R.getG2() ;
               float perd2 = (float)Math.toDegrees( g2.getLanda()*2);
               if (g2.getDir()== -1 ){
                    float disp = ( R.getDx()  - r0+2)       * perd2 ;
                   g2.setRotat(disp );
               }
               else{
                   float disp =( R.getDc()   - r0+2 ) * perd2 ;
                   g2.setRotat( disp );
               }

      }


  }



              ///////////////////////////////////////////////////////////////////////////
   private  int checkGear(ConnRec C     ) {
        int result = 0 ;
        oDig = 0 ;


        Gear  Gir = C.getG0();
        Gear  g   = C.getG() ;

        float dm = C.getDm();
        float dx = C.getDx();
        float Gx = Gir.getXc();
        float Gy = Gir.getYc();
        float Gr = Gir.getRadus();

        float gx = g.getXc();
        float gy = g.getYc();
        float gr = g.getRadus();
        float dist0 = Gr + gr;
        float dist1 = (float) Math.sqrt(Math.pow((Gx - gx), 2) + Math.pow((Gy - gy), 2));
        if (dist1 >= dist0) {   C.setDel(true); return 0;   }

        if (dist1 < dm ) {  shiftGear(Gir, dm-dist1 +2 , Gx - gx, Gy - gy); }

        if ( dist1 > dx ) {  shiftGear(Gir, dist1-dx+2 ,  gx - Gx ,  gy - Gy); }

        float step = (dx - dist1)/3 ;
        if(step == 0) step = 5 ;
        int ite = 0;

        while (Gir.getFil() && ite <4  ) {
            ite++ ;
            int  res = GearRotateChk(Gir, g);

            if (res == 0) {  oDig++ ;
                if (oDig >= 4){//no connection
                    C.setDel(true);
                     result = 0 ;
                    break;
                     }
            }
            if (res >= 3) { //three connection
                if (fDig != -1 && lDig != -1){
                    result =100 ;
                    break;
                }else { res = 2;}
            }
            if (res == 1  || res == 2) { // full connecton

                    dist1 = dist1 + step ;
                    if (dist1 > dx   ) {
                        result = 10 ;
                        GBApplication.Rec.hd(3,"Gears No:"+g.getSerial()+" and No:"+Gir.getSerial()+ " teeth Overlapped",0,4);
                        g.setBlink(2);Gir.setBlink(2);
                        break ;
                    }
                    shiftGear(Gir, step, Gx - gx, Gy - gy);

            }

        }
        if (ite == 4 ){   return 11; }
        if (result < 20 || C.isDel()) return result;

        result = GearFineRotate(Gir, g);
        while (result==30 && Gir.getFil()  ) {

                dist1 = dist1 + step;
                if (dist1 > dx) {
                    GBApplication.Rec.hd(3,"Gears No:"+g.getSerial()+" and No:"+Gir.getSerial()+ " teeth Overlapped",0,4);
                    g.setBlink(2);Gir.setBlink(2);
                    return 10;

                }
                shiftGear(Gir, step, Gx - gx, Gy - gy);
                result = GearFineRotate(Gir, g);

        }
        if (result == 0){
             float Landa = (float) Math.toDegrees( Gir.getLanda()*2);

            C.setD1(fDig  / Landa - oDig /4);

            C.setD2( lDig/ Landa - oDig/4 );

       }

        return  result ;
   }

    private int GearFineRotate(Gear G ,Gear g ){
        int result = 0 ;
        float Gx = G.getXc();
        float Gy = G.getYc();

        double Lan= G.getLanda()  ;

        float gx = g.getXc();
        float gy = g.getYc();
        float gr = g.getRadus();
        double lan= g.getLanda()  ;

        float land = (float)Math.toDegrees(  lan);
        float Land = (float)Math.toDegrees(  Lan);



        Region Reg  = new Region();
        Region reg  = new Region();
        float dim = Math.abs(gx-Gx)+1;
        if (Math.abs(gy-Gy)> dim ) dim = Math.abs(gy-Gy)+1;
        dim = dim + gr   ;
        int dimi = (int) dim ;
        Region clip = new Region(-dimi ,-dimi,dimi ,dimi ) ;

        Matrix mat = new Matrix();
        Matrix Mat1 = new Matrix();
        Matrix Mat  = new Matrix();
        Mat.setRotate(1);
        Path pt = new Path();
        Path Pt = new Path();
        int mDig = ( fDig + lDig ) /2 ;
        for (float step =1 ; step < 6 ; step++){

            Pt.set(G.getPath());
            pt.set(g.getPath());

            mat.setRotate(-step * land / 3 - oDig * land / 2);
            pt.transform(mat);
            pt.offset(gx - Gx, gy - Gy);
            reg.setPath(pt, clip);

            Mat1.setRotate(step * Land / 3 + fDig);
            Pt.transform(Mat1);


            boolean ok = false ;
              for ( int i = fDig ;i <= mDig ; i++){
                      Pt.transform(Mat);
                      Reg.setPath(Pt, clip);
                      Reg.op(reg, Region.Op.INTERSECT) ;
                      fDig = i ;
                      if (Reg.isEmpty()) {
                          ok = true ;
                          break ;
                      }
              }
              if (!ok) return 30 ;
        }
        mat.reset();
        Mat1.reset();
        Mat.reset();
        Mat.setRotate(-1);
        for (float step =1 ; step < 6 ; step++){
            pt.set(   g.getPath() );
            Pt.set(   G.getPath() );

            mat.setRotate( -step * land / 3 - oDig * land /2);
            pt.transform(mat);
            pt.offset(gx - Gx, gy - Gy);
            reg.setPath(pt, clip);

            Mat1.setRotate(  step * Land / 3 + lDig);
            Pt.transform(Mat1);

            boolean ok = false ;
            for ( int i = lDig ;i >= mDig ; i--){
                Pt.transform(Mat);
                Reg.setPath(Pt, clip);
                Reg.op(reg, Region.Op.INTERSECT) ;
                lDig = i ;
                if (Reg.isEmpty()) {
                    ok = true ;
                    break ;
                }
            }
            if (!ok) return 30 ;
        }
        // end of fine rotate
        return result ;

    }
    ///////////////////////////////////////////////////////////////////
    private int clutch(Gear G ,Gear g ){


        float Gx = G.getXc();
        float Gy = G.getYc();

        double Lan= G.getLanda()  ;

        float gx = g.getXc();
        float gy = g.getYc();
        float gr = g.getRadus();
        double lan= g.getLanda()  ;

        float land = (float)Math.toDegrees(  lan);
        float Land = (float)Math.toDegrees(  Lan);



        Region Reg  = new Region();
        Region reg  = new Region();
        float dim = Math.abs(gx-Gx)+1;
        if (Math.abs(gy-Gy)> dim ) dim = Math.abs(gy-Gy)+1;
        dim = dim + gr   ;
        int dimi = (int) dim ;
        Region clip = new Region(-dimi ,-dimi,dimi ,dimi ) ;

        Matrix Mat0 = new Matrix();
        Matrix Mat1 = new Matrix();
        Matrix Mat  = new Matrix();

        for (float step =1 ; step < 6 ; step++){
            Path pt = new Path();
            pt.set(g.getPath());
            Path Pt =  new Path();
            Pt.set(G.getPath()) ;
            float rot = g.getRotat() ;
            float Rot = G.getRotat() ;



            Mat1.setRotate(Rot + step * Land / 3 );
            Pt.transform(Mat1);
            Mat0.setRotate( rot - step* land / 3 );
            pt.transform(Mat0);
            pt.offset(gx - Gx, gy - Gy);
            reg.setPath(pt, clip);
            Reg.setPath(Pt, clip);

            Reg.op(reg, Region.Op.INTERSECT) ;
            if (!Reg.isEmpty()) {return 40  ;  }


        }



        return  0 ;
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    private int GearRotateChk(Gear G ,Gear g  ){

        float Gx = G.getXc();
        float Gy = G.getYc();

        double Lan= G.getLanda()  ;
        float LanD = (float)Math.toDegrees(5 * Lan);
        int  LanDI =  Math.round(LanD)    ;

        float gx = g.getXc();
        float gy = g.getYc();
        float gr = g.getRadus();
        double lan= g.getLanda()  ;
        float lanD = (float)Math.toDegrees(lan);
        lanD  =  Math.round(lanD) / 2    ;
        lanD  = -lanD  * oDig ;
        Matrix mat = new Matrix();
        mat.setRotate(lanD );

        Matrix Mat = new Matrix();
        Mat.setRotate(1);
        int[] bit = new int[LanDI+2];
        for (int i=0 ; i<=LanDI ; i++){ bit[i] = 2 ; }

        float dim = Math.abs(gx-Gx)+1;
        if (Math.abs(gy-Gy)> dim ) dim = Math.abs(gy-Gy)+1;
        dim = dim + gr   ;
        int dimi = (int) dim ;
        Region clip = new Region(-dimi ,-dimi,dimi ,dimi ) ;

        Path Pt = new Path() ;
        Pt.set(G.getPath());
        Region Reg  = new Region();
        Reg.setPath(Pt, clip);

        Path pt = new Path()  ;
        pt.set(  g.getPath() );
        pt.transform(mat);
        Region reg  = new Region();
        pt.offset(gx - Gx, gy - Gy);
        reg.setPath(pt, clip);



        for (int i = 0 ; i<=LanDI ; i++){

            Reg.op(reg, Region.Op.INTERSECT) ;
            if (Reg.isEmpty()) bit[i] = 0 ;
            else bit[i] = 1 ;

            Reg.setEmpty();
            Pt.transform(Mat);
            Reg.setPath(Pt, clip);
        }
        int cnt = 0 ;
        int state = bit[0] ;
        for(int i= 0 ; i<=LanDI ;i++ ){
          if (bit[i] != state){
                              cnt++;
                              state = bit[i];
          }
        }

        if (cnt == 0 && bit[0]==1){  cnt = 1 ;   }

        fDig = -1 ;
        lDig = -1 ;


        int[] dS = new int[5];
        int[] dE = new int[5];
        int[] dW = new int[5];
        for (int i = 0 ; i<5 ; i++) {dS[i] = -1 ; dE[i] = -1 ;dW[i] = 0;}
        int inx = 0 ;
        for(int i= 1 ; i < LanDI ;i++ ){
           if (  bit[i]==0 && bit[i-1]==1) {
               dS[inx] = i ;

           }
           if (dS[inx]!= -1) {
               if (bit[i] == 0 && bit[i + 1] == 1) {
                   dE[inx] = i;
                   inx++;
                   if (inx == 5) break;

               }
           }
        }
        inx = 0 ;
        for (int i = 0 ; i<5 ; i++) {
            dW[i] = dE[i]  - dS[i] ;
            if (dW[inx] < dW[i]) inx = i ;
        }
        //end of rotate check
        fDig = dS[inx]; lDig = dE[inx];


        return cnt ;
    }






    //////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean shiftGear(Gear G , double d , float delx ,float dely){

        if(gearFixed){
            G.setFil(false);
            GBApplication.Rec.hd(3,"Gear Fixed with Axcle",0,4);

            return false ;
        }
        if (G.getGearType()==0 && G.getFil()== true) {
            if (delx == 0 && dely == 0) {
                delx = 1;
                dely = 1;
            }
            double Cofi = d / Math.sqrt(Math.pow(dely, 2) + Math.pow(delx, 2));

            float x = (float) Cofi * delx;
            float y = (float) Cofi * dely;


            G.MoveVec(new PointF(x, y));
            return true;
        }
        return false ;
    }


    /////////////////////////////////////////////////////////////////////////////////////
    private void reorgRulls( ){ // update all
        for(Gear G : MainThread.gears){    G.setLv(0);  }

        Gear[] fG = new Gear[50];
        int cnt = 0 ;

        int lvl  = 1 ;
        for (Gear G : MainThread.gears){
            if (G.getLv()==1){fG[cnt]= G;
                              cnt++;}
        }
        while(cnt>0){
            //fetch one member and delete member
            Gear g0 = fG[0];
            cnt-- ;
            for(int i=1 ;i<=cnt ;i++){ fG[i-1] = fG[i]; }
            setNextLevel(g0, lvl) ;
            if (cnt>0) continue ;
            lvl++ ;
            for (Gear G :MainThread.gears){
                if (G.getLv()==lvl){fG[cnt]= G;
                                     cnt++;}
            }
        }
    }
    private void setNextLevel(Gear g ,int l ){
        int Direc = g.getDir() ;
        float spd = g.getSpeed() ;
        for(RulList R :rulL) {
            if (R.getG1().equals(g) && R.getG2().getLv() == 0) {
                                 R.getG2().setLv(l + 1);
                                 if (R.isDir()){
                                     R.getG2().setDir(Direc);
                                     R.getG2().setSpeed(spd);
                                 }
                                 else {
                                     R.getG2().setDir(-1 * Direc);
                                     R.getG2().setSpeed(  (float) ( R.getG2().getLanda()/R.getG1().getLanda())*spd  );
                                 }

            }
        }
        for(RulList R :rulL) {
                if (R.getG2().equals(g) && R.getG1().getLv()== 0 ) {
                                               R.getG1().setLv(l + 1);
                                               Gear g0 = R.getG1();
                                               R.setG1(g);
                                               R.setG2(g0);
                                               if (R.isDir()) {
                                                      R.getG2().setDir(Direc);
                                                      R.getG2().setSpeed(spd);
                                               }
                                               else{
                                                   R.getG2().setDir(-1 * Direc);
                                                   R.getG2().setSpeed((float)(R.getG2().getLanda() / R.getG1().getLanda())*spd );
                                               }

                }
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////

    //-------------------------------------------------------------------------
    private boolean obsColl(Gear Gir) {


        boolean result = true ;

        float Gx  ;
        float Gy  ;
        float Gr = Gir.getRadus();
        int L = Gir.getLay();

        for (Obstacle o : MainThread.obs) {
            int l = o.getLay();
            if (L != l) continue;
            RectF box = o.getRectangleF();
            RectF Box = Gir.getRectangleF();
            if (!Box.intersect(box)) continue;

            Gx = Gir.getXc();
            Gy = Gir.getYc();

            float ox = o.getXc();
            float oy = o.getYc();
            float or = o.getRadus();
            float dist0 = or + Gr;
            float dist1 = (float) Math.sqrt(Math.pow((Gx - ox), 2) + Math.pow((Gy - oy), 2));

            if (dist1 < dist0) {
                float dif = dist0 - dist1 + 2;
                shiftGear(Gir, dif, Gx - ox, Gy - oy  );
                result = false ;
            }
            if (o.getType() == 0) { //cycle
                continue;
            }

            // squire
            Gx = Gir.getXc();
            Gy = Gir.getYc();
            double[] dist = new double[4];
            dist[0] = Math.sqrt(Math.pow((ox - or - Gx), 2) + Math.pow((oy - or - Gy), 2));
            dist[1] = Math.sqrt(Math.pow((ox - or - Gx), 2) + Math.pow((oy + or - Gy), 2));
            dist[2] = Math.sqrt(Math.pow((ox + or - Gx), 2) + Math.pow((oy + or - Gy), 2));
            dist[3] = Math.sqrt(Math.pow((ox + or - Gx), 2) + Math.pow((oy - or - Gy), 2));

            int ind = 0;
            if (dist[1] < dist[0]  ) ind = 1;
            if (dist[2] < dist[ind]) ind = 2;
            if (dist[3] < dist[ind]) ind = 3;

            if (dist[ind] >= Gr) continue;
            float dif = (float) (Gr - dist[ind]+2);
            shiftGear(Gir ,dif, Gx - ox, Gy - oy);
            result = false ;



        }
        return result;


    }




    private boolean  axlColl (Gear Gir) {

        boolean result = true ;
        float Gx = Gir.getXc();
        float Gy = Gir.getYc();
        float Gr = Gir.getRadus();
        float ar = 12 * GBApplication.dens  ;
        int L = Gir.getLay();


        for (Gear g : MainThread.gears) {
            if (g.equals(Gir)) continue;
            if (!g.getFil()) continue;

            int l = g.getLay();
            if (L == l) continue;
            float ax = g.getXc();
            float ay = g.getYc();


            float dist0;
            if (L > l) {
                dist0 = ar +  g.getRadus() ;
            }else{
                dist0 = ar +  Gr  ;
            }
            float dist1 = (float) Math.sqrt(Math.pow((Gx - ax), 2) + Math.pow((Gy - ay), 2));

            if (dist1 >= dist0) continue;
            if (dist1 < 2 * ar) {// axle fixed
                GBApplication.Rec.hd(3,"Gear Fixed with Axcle",1,2);
                Gir.MoveVec(new PointF(ax-Gx , ay - Gy));
                conRecs.add(new ConnRec( Gir ,g ));
                gearFixed = true ;

                continue;
            }
            float dif = dist0 - dist1 + 2;
            shiftGear(Gir , dif, Gx - ax, Gy - ay);
            return false ;

        }
        return result ;
    }


    private boolean girColl(Gear Gir) {

        boolean result = true;
        float Gx = Gir.getXc();
        float Gy = Gir.getYc();
        float Gr = Gir.getRadus();
        int L = Gir.getLay();

        float B = Gir.getBeta() * dens ;
        double Lan = Gir.getLanda() ;

        for (Gear g : MainThread.gears) {
            int l = g.getLay();
            if (L != l) continue ;
            if (g.equals(Gir)) continue;
            if (!g.getFil()) continue;

            float gx = g.getXc();
            float gy = g.getYc();




            float gr = g.getRadus();
            float dist0 = Gr +  gr ;
            float dist1 = (float) Math.sqrt(Math.pow((Gx - gx), 2) + Math.pow((Gy - gy), 2));
            if (dist1 >= dist0) continue;

            float b = g.getBeta() * dens;
            double lan = g.getLanda() ;
            float bmin = Math.min(b , B );
            float Grm = Gr -    bmin ;
            float grm = gr -    bmin ;
            double Arcm = Lan * Grm ;
            double Arcx = Lan * Gr ;
            double arcm = lan * grm ;
            double arcx = lan * gr ;
            if ( Arcx < arcm || arcx < Arcm ){ // no match gear seperate
                shiftGear(Gir ,dist0 - dist1   ,Gx-gx ,Gy-gy);
                result = false ;
                GBApplication.Rec.hd(3, "Gears No:" + g.getSerial() + " and No:" + Gir.getSerial() + "   Teeth height or Gears Radius Mismatch", 0, 4);
                g.setBlink(2);Gir.setBlink(2);
                continue ;
            }

            double MinArc = Math.max(arcm , Arcm) ;
            double MaxArc = Math.min(arcx, Arcx);
            float dm = (float) (MinArc / Lan + MinArc /lan) ;
            float dx = (float) (MaxArc / Lan + MaxArc/ lan) ;

            if ( dm > dist1    ) {
                boolean res = shiftGear(Gir, dm - dist1 + 2   ,Gx-gx ,Gy-gy);
                result = res ;
                if (!res){
                    GBApplication.Rec.hd(3,"Gears No:"+g.getSerial()+" and No:"+Gir.getSerial()+ " are very close!"
                            ,0,6);
                    gearReset(Gir, g);
                    g.setBlink(2);Gir.setBlink(2);
                    break ;
                }


            }
            if (dist1 > dx     ) {
                boolean res =shiftGear(Gir, dist1 - dx + 2   ,gx-Gx ,gy-Gy);
                result = res ;
                if (!res){
                    GBApplication.Rec.hd(3,"Gears No:"+g.getSerial()+" and No:"+Gir.getSerial()+ " should be closer."
                            ,0,6);
                    gearReset(Gir,g);
                    g.setBlink(2);Gir.setBlink(2);
                    break ;
                }

            }

            conRecs.add(new ConnRec(Gir,g, dm, dx));

        }

        return result;
    }


private void gearReset(Gear g0 , Gear g1){

    if (g0.getGearType()==0){
        g0.setFil(false);
    }else {
        if (g1.getGearType()==0){
            g1.setFil(false);
        }
    }

}








}
