package info.gearboxgame.gearbox;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;



/**
 * Created by beigly on 31.08.2015.
 */
public class Obstacle extends GameObject{

        private  int T ; //type


        private int[] C = { Color.BLACK, Color.DKGRAY, Color.BLACK, Color.LTGRAY, Color.BLACK} ;
        private float[]  cof = {0f , 0.25f , 0.5f , 0.75f , 1.0f } ;

        private float scx ,scy ,tx,ty;
        private float scSize   ;
        private float mx ,my ,mz  ;
        private RectF box ,box1 ;

    

        public Obstacle(float xc,float yc, float r , int l   ,int t   ){
            this.Lay = l ;
            C[0]= GBApplication.GRec.getColor(l);
            C[2]= C[0];
            C[4]= C[0];



            setXc(xc  , r   );
            setYc(yc  , r   );
            this.T = t ;



            box  = new RectF();
            box1 = new RectF();

        }






        public void update(RectF Rec , float z) {

            if ( mx ==  Rec.left && my ==  Rec.top && mz == z ) {return;}
            if (RectF.intersects(getRectangleF(),Rec)) {
                mx =  Rec.left   ;
                my =  Rec.top    ;
                mz = z ;
                show = true ;

                scx = (x1  - mx)/  mz   ;
                scy = (y1  - my)/  mz ;

                scSize =  2 * radus    /  mz  ;

                tx = (xc  - mx) / mz   ;
                ty = (yc -  my)/  mz ;


                box.set(scx , scy  ,scx + scSize ,scy+scSize);
                box1.set(scx + scSize/4,scy + scSize/4 ,scx + scSize*0.75f ,scy+scSize*0.75f);

            }else  show = false;
        }



        public void draw(Canvas canvas) {
            if (!show) return ;
            Paint pnt  = new Paint();
            pnt.setStrokeWidth(5);
            pnt.setStyle(Paint.Style.FILL);
            pnt.setStrokeJoin(Paint.Join.ROUND);
            pnt.setStrokeCap(Paint.Cap.ROUND);
            pnt.setAntiAlias(true);

            Paint pnt1  = new Paint();
            pnt1.setStrokeWidth(5);
            pnt1.setStyle(Paint.Style.FILL);
            pnt1.setStrokeJoin(Paint.Join.ROUND);
            pnt1.setStrokeCap(Paint.Cap.ROUND);
            pnt1.setAntiAlias(true);
            SweepGradient grad  = new SweepGradient(tx  , ty  , C ,cof);
            pnt.setShader(grad);

            C[2] = C[1];C[1] = C[3];
            C[3] = C[2];C[2] = C[0];

            SweepGradient grad1  = new SweepGradient(tx , ty  , C ,cof);
            pnt1.setShader(grad1);

            C[2] = C[1];C[1] = C[3];
            C[3] = C[2];C[2] = C[0];
        if(T==0) {
              canvas.drawArc(box, 0, 360, false, pnt);
              canvas.drawArc(box1, 0, 360, false, pnt1);
          }
            else {
              canvas.drawRect(box,  pnt);
              canvas.drawRect(box1, pnt1);
          }
        }


    public int getType(){return T;}
    public void setType(int t){ T = t;  }
    }







