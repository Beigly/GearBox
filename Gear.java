package info.gearboxgame.gearbox;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.SweepGradient;
import android.util.Log;

/**
 * Created by beigly on 21.09.2015.
 */
public class Gear  extends GameObject {


    private double Landa;

    private float alpha;  // gear alpha param
    private float beta;  // gear beta param
    private float teta;  // gear teta param
    private double R;      //  mid tooth radius
    private int dir, dir0;

    private int N; // N teeth x 2
    private boolean fil; //fill mode
    private float Rotat;
    private float scx, scy;
    private long frameTime;
    private boolean showframe;
    private float exp;

    private Body bd;


    private int GearType; // 0 : ordinary 1 : source  2 : distination
    private final int Serial;


    private Path rawPath, rimPath;
    private float speed  , speed0;


    private Paint pnt, pnt1;
    private int[] C = {Color.BLACK, Color.DKGRAY, Color.BLACK, Color.LTGRAY, Color.BLACK, Color.DKGRAY, Color.BLACK, Color.LTGRAY, Color.BLACK};
    private float[] cof = {0f, 0.12f, 0.25f, 0.38f, 0.5f, 0.62f, 0.75f, 0.87f, 1.0f};

    private int frmwidth;
    private boolean blink ;
    private  int cycle ;

    private float frmr, movx, movy,axcl;
    private int proc;
    private int lv; //level
    private boolean chk;
    public boolean updShape ;

    public Gear(float x, float y   ) {
        this.showframe = false;
        this.updShape  = false;
        this.dens = GBApplication.dens ;
        this.frmwidth = Math.round(dens) + 1;

        this.cycle = 0 ;
        this.blink = false ;
        this.setChk(true);
        this.speed = 0;
        this.speed0= 0;
        this.setProc(0);
        this.setLv(0);
        this.dir  = 0;
        this.dir0 = 0;
        this.GearType = 0;

        this.Lay = GBApplication.GRec.getLc();
        this.alpha = GBApplication.GRec.getAlpha() * dens;
        this.beta = GBApplication.GRec.getBeta() * dens;
        this.teta = GBApplication.GRec.getTeta() * dens;
        this.Serial = GBApplication.GRec.getSerialNo();


        int Co = GBApplication.GRec.getColor();
        C[0] = Co;
        C[2] = Co;
        C[4] = Co;
        C[6] = Co;
        C[8] = Co;


        this.fil = false;
        this.Rotat = 0;
        this.N = GBApplication.GRec.getN();


        bd = new Body(100.f, x, y,  Co);

        rawPath = new Path();
        rimPath = new Path();
        setXc(x, 100);
        setYc(y, 100);
        compute();




        pnt = new Paint();

        pnt.setStyle(Paint.Style.STROKE);
        pnt.setStrokeJoin(Paint.Join.ROUND);
        pnt.setStrokeCap(Paint.Cap.ROUND);
        pnt.setAntiAlias(true);
        pnt.setColor(Co);

        pnt1 = new Paint();
        pnt1.setStyle(Paint.Style.STROKE);
        pnt1.setStrokeJoin(Paint.Join.ROUND);
        pnt1.setStrokeCap(Paint.Cap.ROUND);
        pnt1.setAntiAlias(true);


    }

    private void compute() {

        if (N % 2 != 0) N--;
        if (N < 4) N = 4;

        GBApplication.GRec.setN(N);


        GBApplication.GRec.setAlpha(alpha / dens);
        GBApplication.GRec.setBeta(beta / dens);
        GBApplication.GRec.setTeta(teta / dens);


        Landa = (2 * Math.PI) / N;
        R = teta / Math.sin(Landa);

        radus = (float) (R + beta);
        setXc(getXc() , radus);
        setYc(getYc() , radus);

        Shape();

        bd.setRadius((float) (R - beta));

    }

    public void update(RectF rec, float z) {

        if (RectF.intersects(getRectangleF(), rec)) {
            if (updShape) {
                    compute();
                    updShape = false ;
            }
            show = true;
            exp = 1 / z;
            movx = rec.left;
            movy = rec.top;

            scx = (xc - movx) / z;
            scy = (yc - movy) / z;


            SweepGradient grad = new SweepGradient(scx, scy, C, cof);
            pnt.setShader(grad);
            bd.update(rec, z);


            frmr = radus / z;
            axcl = 12*dens / z ;


        } else {
            show = false;

        }
        float step = GBApplication.GRec.getRotStep();
        Turn(dir * speed * step) ;

    }

    public void draw(Canvas canvas) {
        if (!show) return;

        Matrix mat0 = new Matrix();
        mat0.setScale(exp, exp);
        Path p10 = new Path();
        p10.addPath(rimPath, mat0);


        mat0.reset();
        mat0.setRotate(Rotat);
        mat0.postTranslate(scx, scy);

        Path curpath = new Path();
        curpath.addPath(p10, mat0);
        pnt1.setStrokeWidth(3.f);
        pnt1.setColor( Color.rgb(120,90,90) );

        if (fil) {
            pnt.setStyle(Paint.Style.FILL);
            pnt.setStrokeWidth(1.5f);
            canvas.drawPath(curpath, pnt);
            canvas.drawPath(curpath, pnt1);
        } else {
                pnt.setStyle(Paint.Style.STROKE);
                pnt.setStrokeWidth(3.f);
                canvas.drawPath(curpath, pnt);
                canvas.drawCircle(scx, scy, 3, pnt);
        }

        bd.draw(canvas, pnt);

        pnt1.setColor(Color.LTGRAY);
        canvas.drawCircle(scx, scy, axcl, pnt1);

        if (showframe) {
            pnt1.setStrokeWidth(frmwidth);
            pnt1.setColor(Color.WHITE);
            long elapsed = (System.nanoTime() - frameTime)/1000000 ;
            if(elapsed > 4000) {showframe = false ;}
            canvas.drawRect(scx - frmr, scy - frmr, scx + frmr, scy + frmr, pnt1);
        }
        if (blink) {
            pnt1.setStrokeWidth(frmwidth);
            pnt1.setColor(Color.YELLOW);
            long elapsed = (System.nanoTime() - frameTime)/1000000 ;
            if(elapsed > 4000) {
                frameTime = System.nanoTime();
                cycle-- ;
                if (cycle < 1) blink = false ;

            }
            if (elapsed < 3000) pnt1.setColor(Color.WHITE);
            if (elapsed < 2000) pnt1.setColor(Color.CYAN);
            if (elapsed < 1000) pnt1.setColor(Color.BLUE);
            canvas.drawRect(scx - frmr, scy - frmr, scx + frmr, scy + frmr, pnt1);
        }
    }
    public void drawAxl(Canvas canvas) {
        if (!show) return;
        canvas.drawCircle(scx, scy, axcl, pnt);
    }
    private void Shape() {


        double X1, X2, X3, Xamf, Xamb, Xaf1, Xaf2, Xab1, Xab2, Xbmf, Xbmb, Xbf1, Xbf2, Xbb1, Xbb2, Xst;
        double Y1, Y2, Y3, Yamf, Yamb, Yaf1, Yaf2, Yab1, Yab2, Ybmf, Ybmb, Ybf1, Ybf2, Ybb1, Ybb2, Yst;

        X1 = R;
        Y1 = 0;
        X2 = R * Math.cos(Landa);
        Y2 = R * Math.sin(Landa);
        X3 = R * Math.cos(2 * Landa);
        Y3 = R * Math.sin(2 * Landa);


        Xamf = (X1 + X2) / 2;
        Yamf = (Y1 + Y2) / 2;
        Xamb = (X2 + X3) / 2;
        Yamb = (Y2 + Y3) / 2;


        Xaf1 = X1 + (((X2 - X1) / teta) * ((teta - alpha) / 2));
        Xaf2 = X1 + (((X2 - X1) / teta) * ((teta + alpha) / 2));
        Xab1 = X2 + (((X3 - X2) / teta) * ((teta - alpha) / 2));
        Xab2 = X2 + (((X3 - X2) / teta) * ((teta + alpha) / 2));


        Yaf1 = Y1 + (((Y2 - Y1) / teta) * ((teta - alpha) / 2));
        Yaf2 = Y1 + (((Y2 - Y1) / teta) * ((teta + alpha) / 2));
        Yab1 = Y2 + (((Y3 - Y2) / teta) * ((teta - alpha) / 2));
        Yab2 = Y2 + (((Y3 - Y2) / teta) * ((teta + alpha) / 2));


        Xbmf = (R + beta) * Math.cos(Landa / 2);
        Xbmb = (R - beta) * Math.cos(3 * Landa / 2);
        Ybmf = (R + beta) * Math.sin(Landa / 2);
        Ybmb = (R - beta) * Math.sin(3 * Landa / 2);


        Xbf1 = Xbmf + Xaf1 - Xamf;
        Ybf1 = Ybmf + Yaf1 - Yamf;

        Xbf2 = Xbmf + Xaf2 - Xamf;
        Ybf2 = Ybmf + Yaf2 - Yamf;

        Xbb1 = Xbmb + Xab1 - Xamb;
        Ybb1 = Ybmb + Yab1 - Yamb;

        Xbb2 = Xbmb + Xab2 - Xamb;
        Ybb2 = Ybmb + Yab2 - Yamb;

        Path p1 = new Path();


        float[] pts = {(float) X1, (float) Y1
                , (float) Xbf1, (float) Ybf1
                , (float) Xbf2, (float) Ybf2
                , (float) X2, (float) Y2
                , (float) Xbb1, (float) Ybb1
                , (float) Xbb2, (float) Ybb2
                , (float) X3, (float) Y3};


        p1.moveTo(pts[0], pts[1]);
        p1.lineTo(pts[2], pts[3]);
        p1.lineTo(pts[4], pts[5]);
        p1.lineTo(pts[6], pts[7]);
        p1.lineTo(pts[8], pts[9]);
        p1.lineTo(pts[10], pts[11]);
        p1.lineTo(pts[12], pts[13]);


        Matrix mat = new Matrix();
        mat.setRotate((float) Math.toDegrees(2 * Landa));


        for (int i = 0; i < N / 2 - 1; i++) {
            mat.mapPoints(pts);
            p1.lineTo(pts[2], pts[3]);
            p1.lineTo(pts[4], pts[5]);
            p1.lineTo(pts[6], pts[7]);
            p1.lineTo(pts[8], pts[9]);
            p1.lineTo(pts[10], pts[11]);
            p1.lineTo(pts[12], pts[13]);


        }
        p1.close();
        rimPath.rewind();
        rawPath.rewind();
        rawPath.set(p1);

        Path p2 = new Path();
        float rr = (float) (R - beta);
        p2.addCircle(0, 0, rr, Path.Direction.CW);
        p2.close();

        Region reg1 = new Region();
        Region reg2 = new Region();
        int ri = (int) radus + 1;
        Region clip = new Region(-ri, -ri, ri, ri);
        reg1.setPath(p1, clip);
        reg2.setPath(p2, clip);
        reg1.op(reg2, Region.Op.DIFFERENCE);
        rimPath = reg1.getBoundaryPath();


    }


    public void Turn(float step) {
        Rotat += step;

        if (Rotat >= 360f) Rotat = Rotat - 360;
        if (Rotat <= -360f) Rotat = Rotat + 360;
        bd.setRotat(Rotat);

    }

    public boolean inside(float x, float y) {
        boolean result = true;
        // real cordinate
        x = movx + (x / exp);
        y = movy + (y / exp);
        float d2 = (x - xc) * (x - xc) + (y - yc) * (y - yc) - radus * radus;
        if (d2 > 0) result = false;
        return result;
    }

    public PointF centerVec(float w, float h) {
        PointF result = new PointF(0, 0);
        float dx = (w / 2 - scx) / exp;
        float dy = (h / 2 - scy) / exp;
        int rem = (int) dx;
        int base = rem;
        rem = rem % 10;
        dx = (base - rem);

        rem = (int) dy;
        base = rem;
        rem = rem % 10;
        dy = (base - rem);
        result.set(dx, dy);
        return result;
    }


    public void setSpeed(float s) {      speed = s;    }

    public float getSpeed() {        return speed;    }
    public void setSpeed0(float s) {      speed0 = s;    }

    public float getSpeed0() {        return speed0;    }


    public int getSerial() {
        return Serial;
    }

    public float getRotat() {
        return Rotat;
    }

    public void setRotat(float r) {
        Rotat = r;
    }


    public int getGearType() {
        return GearType;
    }

    public void setGearType(int gt) {
        GearType = gt;
        if (GearType == 1) setLv(1);
    }


    public boolean getFil() {
        return fil;
    }

    public void setFil(boolean b) {
        if (GearType == 0) {
            fil = b;
            if (!b) {
                dir = 0;
                speed = 0 ;
                setProc(0);

            } else {
                setProc(1);
            }

        }
    }

    public Path getPath() {
        return rawPath;
    }

    public double getLanda() {
        return Landa;
    }

    public int getDir() {        return dir;    }

    public int getDir0() {       return dir0;   }

    public void setDir(int s) {     dir = s;    }

    public void setDir0(int s) {   dir0 = s;    }

    public float getAlpha() {
        return alpha / dens;
    }

    public float getBeta() {
        return beta / dens;
    }

    public float getTeta() {
        return teta / dens;
    }

    public int getN() {
        return N;
    }

    public void setAlpha(float a) {
        if (!fil) {
            alpha = a * dens;
            updShape = true;
        }
    }

    public void setABTN(float a, float b, float t, int n) {
        this.alpha = a * dens;
        this.beta = b * dens;
        this.teta = t * dens;
        this.N = n;
        updShape = true;
    }

    public void setBeta(float b) {
        if (!fil) {
            beta = b * dens;
            updShape = true;
        }
    }

    public void setTeta(float t) {
        if (!fil) {
            teta = t * dens;
            updShape = true;
        }
    }

    public void setN(int n) {
        if (!fil) {
            if ( n < 4 ) n = 4 ;
            if ( n >100) n = 100 ;
            n =( n / 2 ) * 2 ;
            this.N = n;
            updShape = true;
        }
    }



    public void MoveVec(PointF v) {
        MoveV(v);
        bd.MoveV(v);

    }


    public int getProc() {
        return proc;
    }

    public void setProc(int p) {

        if (p != proc ) { setChk(false);    }
        this.proc = p;

    }


    public int getLv() {
        return lv;
    }

    public void setLv(int v) {
        if (GearType == 1) this.lv = 1;
        else {
            this.lv = v;
            if (v == 0) {dir = 0; speed = 0 ;}
        }
    }

    public boolean isChk() {     return chk;    }

    public void setChk(boolean chk) {   this.chk = chk;  }

    public void setFrame() {
        showframe = true;
        frameTime = System.nanoTime();

    }
    public void setBlink(int i) {
        cycle = i ;
        blink = true;


    }
    public boolean getFrame() {return   showframe ;  }
    public void colorCorr(){
        int Co = GBApplication.GRec.getColor();
        C[0] = Co;        C[2] = Co;        C[4] = Co;
        C[6] = Co;        C[8] = Co;
        pnt.setColor(Co);
    }
}




