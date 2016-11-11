package info.gearboxgame.gearbox;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;



/**
 * Created by beigly on 07.09.2015.
 */
public class Body extends  GameObject {



    private int type;

    private float Rotat;


    private float scx, scy;
  
    private float exp;
    private Path rowpath  ;

    public Body(float r, float cx, float cy,  int Co) {
        setXc(cx, r);
        setYc(cy, r);


        this.Rotat = 0;
        int t = (int) cx ;
        t = t % 15 ;
        this.type = t;
        rowpath = Shape(t);

  }


    public void update(RectF Rec, float z ) {
        RectF R = new RectF(xc-radus,yc-radus ,xc+radus ,yc+radus ) ;

        if (RectF.intersects(R , Rec)) {
            show = true;
            exp = 1 / z;
            float mx = Rec.left;
            float my = Rec.top;

            scx = (xc - mx) / z;
            scy = (yc - my) / z;



        } else show = false;

    }


    public void draw(Canvas canvas,Paint pnt) {

        if (!show) return;


        Matrix mat = new Matrix();
        mat.setScale(exp, exp);
        Path tmpPath = new Path();
        tmpPath.addPath(rowpath, mat);


        mat.reset();
        mat.setRotate(Rotat);
        mat.postTranslate(scx, scy);
        Path curpath = new Path();
        curpath.addPath(tmpPath, mat);



            canvas.drawPath(curpath, pnt);





    }







    public void setRadius(float r) {
        this.radus =  r;
        rowpath.reset();
        rowpath=Shape(type);
    }

    public void setRotat(float rot) {
        Rotat = rot;
    }



    private Path Shape(int t) {
        Matrix mat = new Matrix();
        int ri = (int) radus + 1;
        float r = radus;
        Region clip = new Region(-ri, -ri, ri, ri);
        Region reg1 = new Region();

        // full ring
        Path p1 = new Path();
        p1.addCircle(0, 0, r, Path.Direction.CW);
        reg1.setPath(p1, clip);
        // inner hole
        Path p2 = new Path();
        p2.addCircle(0, 0, 0.875f * r , Path.Direction.CW);
        Region reg2 = new Region();
        reg2.setPath(p2, clip);
        // outer rim reg2
        reg1.op(reg2, Region.Op.DIFFERENCE);
        p2.reset();
        if (type == 0 || type == 1) {

            //spike
            Path p3 = new Path();
            p3.addRect(-0.9f * r, -0.05f * r, 0.9f * r, 0.05f * r, Path.Direction.CW);
            p3.addRect(-0.05f * r, -0.9f * r, 0.05f * r, 0.9f * r, Path.Direction.CW);

            if (type == 1) {
                mat.reset();
                mat.setRotate(45);
                p3.addPath(p3, mat);
            }

            Region reg3 = new Region();
            reg3.setPath(p3, clip);

            reg1.op(reg3, Region.Op.UNION);

        }
        //...............................

        if (type == 2 || type == 3 )
        {  // 2 spark
            Path p3 = new Path();
            p3.addCircle(0, 0, r / 2, Path.Direction.CW);

            p3.addRect(-0.1f *r  ,-0.9f *r ,0.1f *r ,0.9f *r ,Path.Direction.CW );


          if ( type == 3 )
            {  // 4 spark
               p3.addRect(-0.9f *r   , -0.1f* r    , 0.9f *r, 0.1f* r ,Path.Direction.CW );
            }
            Region reg3 = new Region();
            reg3.setPath(p3, clip);

            reg1.op(reg3, Region.Op.UNION);


        }
        if ( type == 4 || type == 5){

            reg1.op(reg2, Region.Op.UNION);

            Path p3 = new Path();
            p3.addRect( 0.2f * r , 0.2f * r  , 0.7f * r , 0.7f * r  ,Path.Direction.CW);

            mat.reset();
            mat.setRotate(45);
            Path p4 = new Path();

            for (int i =1 ;i<9 ; i++ ) {
                p3.transform(mat);
                p4.addPath(p3);

            }
            reg2.setEmpty();
            reg2.setPath(p4, clip) ;
            reg1.op(reg2, Region.Op.DIFFERENCE);

            p3.reset();
            p3.addRect(-0.1f * r, -0.9f * r, 0.1f * r, 0.9f * r, Path.Direction.CW);


            if (type==5){
                p3.addRect(-0.9f * r, -0.1f * r, 0.9f * r, 0.1f * r, Path.Direction.CW);
             }
            reg2.setEmpty();
            reg2.setPath(p3, clip);
            reg1.op(reg2,Region.Op.UNION);
        }

        if(type == 6 || type == 7) {
            reg1.op(reg2, Region.Op.UNION);

            Path p3 = new Path();
            p3.addRect(0.6f * r, -0.25f * r, 0.8f * r, 0.25f * r, Path.Direction.CW);
            p3.addRect(0.142f * r, 0.125f * r, 0.475f * r, 0.458f * r, Path.Direction.CW);

            mat.reset();
            mat.setRotate(45);
            Path p4 = new Path();

            for (int i = 1; i < 9; i++) {
                p3.transform( mat);
                p4.addPath(p3);
            }
            reg2.setEmpty();
            reg2.setPath(p4, clip);
            reg1.op(reg2, Region.Op.DIFFERENCE);

            p3.reset();
            p3.addRect(-0.1f * r, -0.9f * r, 0.1f * r, 0.9f * r, Path.Direction.CW);


                if (type == 7) {
                p3.addRect(-0.9f * r, -0.1f * r, 0.9f * r, 0.1f * r, Path.Direction.CW);
               }
            reg2.setEmpty();
            reg2.setPath(p3, clip);
            reg1.op(reg2, Region.Op.UNION);

            }

        if (type == 8 || type==9) {
            reg1.op(reg2, Region.Op.UNION);

            Path p3 = new Path();
            p3.addRect(0.2f * r, -0.16f * r, 0.6f * r, 0, Path.Direction.CW);
            p3.addRect(0.33f * r, -0.5f * r, 0.83f * r, -0.17f * r, Path.Direction.CW);

            mat.reset();
            mat.setRotate(45);
            Path p4 = new Path();

            for (int i = 1; i < 9; i++) {
                p3.transform(mat);
                p4.addPath(p3);
            }
            reg2.setEmpty();
            reg2.setPath(p4, clip);
            reg1.op(reg2, Region.Op.DIFFERENCE);

            p3.reset();
            p3.addRect(-0.1f * r, -0.9f * r, 0.1f * r, 0.9f * r, Path.Direction.CW);


            if (type == 9) {
                p3.addRect(-0.9f * r, -0.1f * r, 0.9f * r, 0.1f * r, Path.Direction.CW);
            }
            reg2.setEmpty();
            reg2.setPath(p3, clip);
            reg1.op(reg2, Region.Op.UNION);

        }

        if ( type == 10) {
            reg1.op(reg2, Region.Op.UNION);

            Path p3 = new Path();
            p3.addRect(0.16f * r, -0.07f * r, 0.76f * r, 0.07f * r, Path.Direction.CW);
            p3.addRect(0.33f * r, -0.25f * r, 0.83f * r, -0.1f * r, Path.Direction.CW);
            p3.addRect(0.5f * r, -0.5f * r, 0.75f * r, -0.35f * r, Path.Direction.CW);


            mat.reset();
            mat.setRotate(45);
            Path p4 = new Path();

            for (int i = 1; i < 9; i++) {
                p3.transform( mat);
                p4.addPath(p3);

            }
            reg2.setEmpty();
            reg2.setPath(p4, clip);
            reg1.op(reg2, Region.Op.DIFFERENCE);

          }


             if (type > 10 ) {
                 Path p4 = new Path();
                 p4.addCircle(0, 0, 0.33f * r, Path.Direction.CW);
                 reg2.setEmpty();
                 reg2.setPath(p4, clip);
                 reg1.op(reg2, Region.Op.UNION);


                 Path p3 = new Path();
                 p3.addRect(-0.1f * r, -0.9f * r, -0.03f * r, 0.9f * r , Path.Direction.CW);

                 mat.reset();
                 mat.setRotate(45);
                 p4.reset();


                 for (int i = 1; i < 9; i++) {
                     p3.transform(mat);
                     p4.addPath(p3);
                 }
                 reg2.setEmpty();
                 reg2.setPath(p4, clip);
                 reg1.op(reg2, Region.Op.UNION);



             }


        //...............................
        Path p8 = new Path();
        p8 = reg1.getBoundaryPath();
         return  p8 ;
    }
}