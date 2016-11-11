package info.gearboxgame.gearbox;


import android.graphics.PointF;
import android.graphics.RectF;


/**
 * Created by beigly on 30.06.2015.
 */
public abstract class GameObject {
    protected float x1 , xc  ;
    protected float y1 , yc  ;
    protected float radus ;
    protected int Lay ;
    protected boolean show ;
    protected float dens ;


    public float getX1() {    return x1;    }
    public float getY1() {    return y1;    }
    public float getRadus() {    return radus;    }

    public void setX1(float x) {
        this.x1 = x;
        if (radus > 0) xc = x + radus;
    }
    public void setXc(float x, float s) {
        this.xc = x;
        this.radus = s ;
        this.x1  = x - radus ;
    }
    public float getXc() { return xc ;}
    public float getYc() { return yc ;}


    public void setY1(float y) {
        this.y1 = y;
        if (radus > 0) yc = y + radus;
    }
    public void setYc(float y ,float s) {
        this.yc = y;
        this.radus = s ;
        this.y1  = y - radus;
    }


    public int getLay() {return Lay ;}
    public void  setLay(int l) {this.Lay = l  ;}



    public RectF getRectangleF(){
        float size = 2 * radus ;
        return new RectF(x1,y1,x1+size,y1+size);

    }
    public  void MoveV(PointF v){
        x1 += v.x ;
        y1 += v.y ;
        xc += v.x ;
        yc += v.y ;

    }
}
