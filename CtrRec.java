package info.gearboxgame.gearbox;


import android.graphics.RectF;


public class CtrRec {


    private float zoom, xzoom, mzoom;
    private int zi, zx;
    private float movex, movey;
    private int Xmox, Xmoy;
    private float Vw, Vh;
    private float Pw, Ph;

    private float Dw, Dh;

    private String[] msg = new String[4];
    private int[] nMg = new int[4];
    private int[] pMg = new int[4];
    private boolean[] bMg = new boolean[4];
//------------------------------------------------------------------------

    public CtrRec() {
        msg[0] = ""; msg[1] = "";
        msg[2] = ""; msg[3] = "";

        nMg[0] = 0; nMg[1] = 0;
        nMg[2] = 0; nMg[3] = 0;

        pMg[0] = 0; pMg[1] = 0;
        pMg[2] = 0; pMg[3] = 0;

        bMg[0] = false; bMg[1] = false;
        bMg[2] = false; bMg[3] = false;
    }

    public int MsgChk() {
        int res = 0;
        for (int i = 0; i < 4; i++)
        {
            if(nMg[i]>0)nMg[i]--;
            if(nMg[i]==1){
                 msg[i] = "";
                 bMg[i] = true;

            }
        }
       if (bMg[0]) res++  ;
       if (bMg[1]) res+=2 ;
       if (bMg[2]) res+=4 ;
       if (bMg[3]) res+=8 ;
       return res ;
    }
    public  String hd(int i){
            bMg[i] = false ;
            return msg[i] ;
      }
    public  void hd(int i,String s , int p ,int t){

        if (nMg[i]==0){
            msg[i]= s ;
            nMg[i]= t*2  ;
            pMg[i]= p ;
            bMg[i]= true ;
            return ;
        }
        if (pMg[i] < p ) {return;}
        msg[i]= s ;
        nMg[i]= t*2  ;
        pMg[i]= p ;
        bMg[i]= true ;
    }





   //public void setVwh( float w , float h) { Vw = w ; Vh = h ;}
    public void setPwh( float w , float h) {
        w = w * GBApplication.dens ;
        h = h * GBApplication.dens ;
        int rem = (int)   w   ;
        rem = rem % 50 ;
        if (rem > 0 )   w = (50 - rem) + w  ;
        rem = (int)   h   ;
        rem = rem % 50 ;
        if (rem > 0)    h = (50 - rem) + h  ;
        Pw = w ; Ph = h ;

    }
    public  int getZi() {    return zi;    }
    public  void setZi(int i) {
                                if (i < 0     )   return ;
                                if (i > zx )   return;
                                this.zi = i ;
                                float zu = i ;
                                zu = mzoom + ( 0.1f * zu ) ;


                                setZoom(zu);
                              }
    public  int getZx() {    return zx;    }
    public  float  getDw() { return Dw; }
    public  float  getDh() { return Dh; }
    public  float  getPw() { return Pw; }
    public  float  getPh() { return Ph; }
    public  float  getVw() { return Vw; }
    public  float  getVh() { return Vh; }
    public  int  getXmox() { return Xmox; }
    public  int  getXmoy() { return Xmoy; }


    public void setWH(float w , float h ) {Dw = w ; Dh = h ;}
    public  float getZoom() {       return zoom;      }
    public  float getMzoom() {       return mzoom;    }
    public  float getXzoom() {       return xzoom;    }

    public  void setZoom(float z) {


        Vw  = ( Dw * z )  ;
        Vh =  ( Dh * z )  ;
        zoom = z;
        checkMove() ;


    }

    public  float getMovey() {   return movey;    }
    public  float getMovex() {   return movex;    }

    public  void setMovex(float  x) {
                movex =  x;
                checkMove();
    }

    public  void setMovey(float  y) {
                movey =  y;
                checkMove();
  }
    public  void setMovexy(float  x ,float y) {
        movey =  y;
        movex =  x;
        checkMove();
    }


    public  void initial(){
        Vw = Dw ;
        Vh = Dh ;


        setMovex(0) ;
        setMovey(0) ;

        float rx =  (  Pw )  / Vw ;
        float rm =     Ph   / Vh;
        float rt = 0 ;

        if ( rx < rm ) {rt = rx ;
                        rx = rm ;
                        rm = rt;} //exchange
        xzoom = (rx + 0.2f);
        mzoom = 0.2f   ;
        if (rm < 2) {
            setZoom(rm);
        }else {
            setZoom(2f);
        }

        zx  =  (int)  ( (xzoom -mzoom) /0.1f ) ;
        zi  =  (int)  ( (zoom  -mzoom)/ 0.1f ) ;


        // middle set
        movex = (0.8f*Pw - Vw)/2 ;
        int rem = (int) movex ;
        int base = rem ;
        rem = rem % 10 ;
        movex = (base - rem) ;

        if (movex < 9 ) movex = 0f ;
        movey = (Ph - Vh)/2 ;
        rem = (int) movey ;
        base = rem ;
        rem = rem % 10 ;
        movey = (base - rem) ;
        if (movey < 9 ) movey = 0f ;

    }
  private void checkMove(){
      int rem ;
      int base ;
      if (movex + Vw > Pw) {
                movex = Pw - Vw ;
                rem = (int) movex ;
                base = rem ;
                rem = rem % 10 ;
                movex = (base - rem) ;}
      if (movex < 0 ) movex = 0f ;

      Xmox = (int)( ( Pw - Vw ) / 10) ;
      if(Xmox < 0) Xmox = 0 ;

      if (movey + Vh > Ph) {
                movey = Ph - Vh ;
                rem = (int) movey ;
                base = rem ;
                rem = rem % 10 ;
                movey = base - rem ;}
      if (movey < 0 ) movey = 0f ;

      Xmoy = (int)( ( Ph - Vh) /10 ) ;
      if (Xmoy < 0 ) Xmoy = 0 ;


  }

    public RectF getDisply(){
        return new RectF(movex,movey,movex+Vw,movey+Vh);
    }


}
