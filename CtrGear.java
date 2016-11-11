package info.gearboxgame.gearbox;

import android.graphics.Color;


import java.util.Random;

/**
 * Created by beigly on 24.09.2015.
 */
public class CtrGear {



    private boolean rotSw = true;
    private float  rotStep ;
    private int Lc=0 , Lx=0 ;

    private float[] Alpha ={40,14,0,10, 5,10} ;
    private float[] Beta  ={20,20,40,20,15,35} ;
    private float[] Teta  ={50,35,40,30,30,35} ;
    private int[] N = {18,20,22,24,26,30} ;
    private int[] CRang={
            Color.WHITE           , Color.rgb(255,230,230), Color.rgb(230,255,230)  ,Color.rgb(230,230,255),
            Color.rgb(255,0,0)    , Color.rgb(128,0,0)    , Color.rgb(255,128,128)  ,Color.rgb(255,128,0),
            Color.rgb(100,255,100), Color.rgb(128,255,128), Color.rgb(0,128,128)    ,Color.rgb(0,128,0),
            Color.rgb(255,255,0)  , Color.rgb(255,255,128), Color.rgb(255,255,64)   ,Color.rgb(145,145,0),
            Color.rgb(0,0,255)    , Color.rgb(0,0,160)    , Color.rgb(0,128,200)    ,Color.rgb(0,64,128),
            Color.rgb(255,0,255)  , Color.rgb(255,128,255), Color.rgb(128,0,128)    ,Color.rgb(128,0,64)
    };
    private int Csq = 0 ;
    private int serialNo = 0;
    private int lStat = -1;
    private int sG = -1 ;

    public int getSerialNo(){serialNo++ ; return serialNo;}
    public void resetSeralNo() {serialNo = 0 ;}
    public int getN(){return N[Lc];}
    public void setN(int n){N[Lc]=n;}
    public  int getColor(){

        Random rand = new Random();
        int  n = rand.nextInt(4) ;

        if (Lx > 0 || Lc > 0) Csq = Lc ;

        int index = Csq*4 + n ;
        Csq++ ;
        if (Csq > 5) Csq = 0 ;
        return CRang[index] ;
    }


    //-----------------------------------------------------------------------
    public int getColor(int L ){return CRang[L*4];}

    public int getLc() {return Lc; }
    public int getLx() {return Lx; }

    public void setLc(int i) { Lc = i; }
    public void setLx(int i) { Lx = i; }

    public float getAlpha() {return Alpha[Lc]; }
    public float getBeta()  {return Beta[Lc]; }
    public float getTeta()  {return Teta[Lc]; }
    public void setAlpha(float a ) { Alpha[Lc]= a; }
    public void setBeta(float b )  {  Beta[Lc]= b; }
    public void setTeta(float t )  {  Teta[Lc]= t; }


    public boolean isRotSw() {
        return rotSw;
    }

    public void setRotSw(boolean rotSw) {
        this.rotSw = rotSw;
    }

    public float getRotStep() {
        return rotStep;
    }

    public void setRotStep(float rotStep) {
        this.rotStep = rotStep;
    }

    public int getlStat() { return lStat;}


    public void setlStat(int lStat) {   this.lStat = lStat;  }

    public int getsG() { return sG;    }

    public void setsG(int sG) {  this.sG = sG;    }





}
