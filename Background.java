package info.gearboxgame.gearbox;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class Background   {

    private Paint pnt ;
    private float  x0 , y0  ,Dw ,Dh   , Vw ,Vh  ;
    private float z ;


    public Background(float w, float h) {

       this.Dw = w ;
       this.Dh = h ;

        pnt  = new Paint();
        pnt.setStrokeJoin(Paint.Join.ROUND);
        pnt.setStrokeCap(Paint.Cap.ROUND);
        pnt.setAntiAlias(true);


    }


    public void draw(Canvas canvas) {

        //background

        pnt.setStyle(Paint.Style.FILL);
        pnt.setColor(Color.BLACK) ;
        canvas.drawRect(0, 0, Dw, Dh, pnt);

        // draw horizontal lines
        pnt.setStrokeWidth(1);
         pnt.setStyle(Paint.Style.STROKE);
         Vw = Dw * z ;
         Vh = Dh * z ;

            for ( float fi =   x0 ; fi <= x0+Vw  ; fi += 10.f )
            {
                float stp10 = (int)( (fi-x0  ) / z ) ;
                if (stp10 > 8 ) {
                    pnt.setColor(Color.GRAY) ;
                    canvas.drawLine(stp10, 0, stp10, Dh, pnt);
                }
                int i = (int) fi ;
                if ( i % 50 == 0 ) {
                    pnt.setColor(Color.LTGRAY) ;
                    canvas.drawLine(stp10, 0, stp10, Dh, pnt);
                }
                if ( i % 100 == 0 ) {
                    pnt.setColor(Color.RED) ;
                    canvas.drawLine(stp10, 0, stp10, Dh, pnt);
                }
            }

            for ( float fi = y0  ; fi <=  y0+Vh  ; fi += 10.f )
            {


                float stp10 = (int)( (fi-y0 ) / z ) ;
                if (stp10 > 8 ) {
                    pnt.setColor(Color.GRAY);
                    canvas.drawLine(0, stp10, Dw, stp10, pnt);
                }
                int i = (int) fi ;
                if ( i % 50 == 0 ) {
                    pnt.setColor(Color.LTGRAY);
                    canvas.drawLine(0, stp10, Dw, stp10, pnt);
                }
                if ( i % 100 == 0 ) {
                    pnt.setColor(Color.RED);
                    canvas.drawLine(0, stp10, Dw, stp10, pnt);
                }
            }



        pnt.setStrokeWidth(10);
        pnt.setColor(Color.WHITE) ;
        canvas.drawLine(0  ,  0  , Dw  , 0   ,pnt);
        canvas.drawLine(Dw  ,  0  , Dw  , Dh   ,pnt);
        canvas.drawLine(Dw  ,  Dh  , 0  , Dh   ,pnt );
        canvas.drawLine(0  ,   Dh  , 0  , 0   ,pnt);



    }

    public void update(float zc , float mx , float my ){
          z = zc ;
          x0 = mx ;
          y0 = my ;



    }

}