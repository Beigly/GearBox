package info.gearboxgame.gearbox;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
;

/**
 * Created by beigly on 02.11.2015.
 *
 */
public class Cursor extends GameObject {
    private Paint  pnt1 ;
    private  long StartTime  ;
    public Cursor(float x ,float y ,float dens){

      float s = 15 * dens  ;
      show = false ;
        setXc(x/2, s);
        setYc(y/2, s);

        pnt1  = new Paint();

        pnt1.setStyle(Paint.Style.FILL);
        pnt1.setStrokeJoin(Paint.Join.ROUND);
        pnt1.setStrokeCap(Paint.Cap.ROUND);
        pnt1.setAntiAlias(true);
        pnt1.setColor(Color.argb(128, 255, 255, 128));

    }
    public void draw(Canvas canvas) {
        if (!show) return ;
        long elapsed = (System.nanoTime() - StartTime)/1000000 ;
        if(elapsed > 4000) {show = false ;
                            return;}
        canvas.drawCircle(xc, yc, radus, pnt1);



    }

    public void set(float x, float y){
        setXc(x ,radus);
        setYc(y ,radus);
        show = true ;
        StartTime =System.nanoTime()  ;
        }
    public void reset(){ show= false ;}

    public boolean getShow(){return show ;}

    public PointF getxy(){return new PointF(xc,yc) ;}

    public void  setShow(){show = true ;
        StartTime =System.nanoTime()  ;
       // float xr = GBApplication.Rec.getMovex() + ( xc * GBApplication.Rec.getZoom()  );
       // float yr = GBApplication.Rec.getMovey() + ( yc * GBApplication.Rec.getZoom()  );


       // GBApplication.Rec.hd(2,"("+  Integer.toString(Math.round(xr))+","+Integer.toString(Math.round(yr)) +")",1,3);
    }



}

