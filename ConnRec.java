package info.gearboxgame.gearbox;

/**
 * Created by beigly on 16.11.2015.
 */
public class ConnRec {
    private Gear    g0    ;
    private Gear    g    ;
    private float   dm   ;
    private float   dx   ;
    private float   d1   ;
    private float   d2   ;
    private boolean dir  ;
    private boolean del  ;
    private int     ser  ;



    public   ConnRec(Gear g0 , Gear g ,float  m ,float  x ){
           this.setG0(g0);
           this.setG(g);
           this.setDm(m);
           this.setDx(x);
           this.setDel(false);
           this.setSer(-1);
           this.setDir(false);
           this.setD1(-1);
           this.setD2(-1);


   }
    public   ConnRec(Gear g0 , Gear g  ){
        this.setG0(g0);
        this.setG(g);
        this.setDm(0);
        this.setDx(0);
        this.setDel(false);
        this.setSer(-1);
        this.setDir(true);
        this.setD1(-1);
        this.setD2(-1);


    }

    public Gear getG0() {  return g0;    }

    public void setG0(Gear gi) {  this.g0 = gi;    }

    public Gear getG() {  return g;    }

    public void setG(Gear gi) {  this.g = gi;    }

    public float getDm() {   return dm;    }

    public void setDm(float dm) {   this.dm = dm;    }

    public float getDx() {     return dx;    }

    public void setDx(float dx) {    this.dx = dx;    }

    public float getD1() {     return d1;    }

    public void setD1(float d1) {     this.d1 = d1;    }

    public float getD2() {      return d2;    }

    public void setD2(float d2) {     this.d2 = d2;    }

    public boolean isDir() {       return dir;    }

    public void setDir(boolean dir) {      this.dir = dir;    }

    public boolean isDel() {        return del;    }

    public void setDel(boolean del) {        this.del = del;    }

    public int getSer() {        return ser;    }

    public void setSer(int ser) {        this.ser = ser;    }
}
