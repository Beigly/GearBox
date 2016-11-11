package info.gearboxgame.gearbox;

/**
 * Created by beigly on 20.11.2015.
 */
public class RulList {
    private  Gear g1 ;

    private  Gear g2 ;

    private boolean dir ;
    private float fDig ;
    private float lDig ;
    private float dc ;
    private float dx ;



    public RulList(Gear g1, Gear g2){
        this.setG1(g1);
        this.setG2(g2);
        this.setDir(true);

        this.fDig = 0 ;
        this.lDig = 0 ;
        this.setDc(0);
        this.setDx(0);

    }
    public RulList(Gear g1, Gear g2, float  c ,float  x  ){
        this.setG1(g1);
        this.setG2(g2);
        this.setDir(false);


        this.setDc(c);
        this.setDx(x);
    }

    public Gear getG1() {       return g1;    }

    public void setG1(Gear g1) {     this.g1 = g1;    }

    public Gear getG2() {    return g2;    }

    public void setG2(Gear g2) {   this.g2 = g2;  }

    public boolean isDir() {
        return dir;
    }

    public void setDir(boolean dir) {
        this.dir = dir;
    }





    public float getDc() {    return dc;   }

    public void setDc(float dc) {   this.dc = dc;    }

    public float getDx() {    return dx;   }

    public void setDx(float dx) {     this.dx = dx;    }
}
