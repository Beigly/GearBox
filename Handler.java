package info.gearboxgame.gearbox;


import android.graphics.PointF;
import android.view.GestureDetector.*;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;


/**
 * Created by beigly on 23.12.2015.
 */
public class Handler extends ScaleGestureDetector.SimpleOnScaleGestureListener implements

         OnGestureListener, OnDoubleTapListener {


    private int activity = 0;
    private int counter = 0;

    // 0 : non    1:click field     2:gear fill nor    3 : gear fil anor  4: gear nfil  nor
    private int gNo = -1;
    private float mX, mY;
    private float cx, cy;

    public Handler() {

    }


    @Override
    public boolean onDown(MotionEvent e) {
        cx = 0;
        cy = 0;
        mX = 0;
        mY = 0;
        float X = e.getX();
        float Y = e.getY();
        if (e.getPointerCount() > 1) {
            activity = 0;
            //hide cursor
           MainThread.cursor.reset();
            return true;
        }

        activity = 1;  // field
        gNo = onGear(X, Y);

        if (gNo > -1) {  //gear
            if (MainThread.gears.get(gNo).getFil()) {
                if (MainThread.gears.get(gNo).getGearType() == 0) activity = 2;
                else activity = 3;
            } else activity = 4;
        }


        return true;
    }


    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        switch (activity) {

            case 1:
                float X = e.getX();
                float Y = e.getY();
                //"ShowCursor;"
                MainThread.cursor.set(X, Y);
                break;
            case 2:   // not fil normal gear
                GBApplication.GRec.setsG(gNo);//show gear
                GBApplication.GRec.setlStat(0);
                GBApplication.GRec.setsG(gNo);
                if (MainThread.gears.get(gNo).getFrame()) {
                    MainThread.gears.get(gNo).setFil(false);
                }else {
                    MainThread.gears.get(gNo).setFrame();
                }
                break;
            case 3: // fil  Anor gear
                GBApplication.GRec.setsG(gNo); //show gear
                GBApplication.GRec.setlStat(0);
                GBApplication.GRec.setsG(gNo);
                //show frame
                MainThread.gears.get(gNo).setFrame();
                break;
            case 4: // not fil  normal gear
                GBApplication.GRec.setsG(gNo); //show gear
                GBApplication.GRec.setlStat(0);
                GBApplication.GRec.setsG(gNo);
                if (MainThread.gears.get(gNo).getFrame()) {
                        MainThread.gears.get(gNo).setFil(true);
                }else {
                    //show frame
                    MainThread.gears.get(gNo).setFrame();
                }
                break;


        }
        activity = 0;
        gNo = -1 ;
        return true;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        if (activity < 9) {
            activity = activity * 10;
            GBApplication.GRec.setlStat(0);
        }

        switch (activity) {
            case 10:
            case 20:
            case 30: // field move
                float z = GBApplication.Rec.getZoom();
                mX += (distanceX) * z;
                mY += (distanceY) * z;
                int imx = (int) mX;
                int xcof = (imx / 10)  ;
                int imy = (int) mY;
                int ycof = (imy / 10) ;
                xcof *=10 ;
                ycof *=10 ;
                GBApplication.Rec.setMovexy(GBApplication.Rec.getMovex() + xcof, GBApplication.Rec.getMovey() + ycof);
                mX = mX - xcof  ;
                mY = mY - ycof  ;
                //show cursor   x,y
                MainThread.cursor.set(e2.getX(), e2.getY());


                break;
            case 40: //  move gear
                z = GBApplication.Rec.getZoom();
                float x = -distanceX * z;
                float y = -distanceY * z;

                synchronized(MainThread.gears) {MainThread.gears.get(gNo).MoveVec(new PointF(x, y));}
                // hide cursor
                MainThread.cursor.reset();
                break;
            default:
        }

        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (activity != 4 && activity != 40) {
            GBApplication.GRec.setlStat(8);
            //"showHideButtons"
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();

        float x = detector.getFocusX();
        float y = detector.getFocusY();
        if (cx == 0 && cy == 0) {
            float z = GBApplication.Rec.getZoom();
            cx = GBApplication.Rec.getMovex() + x * z;
            cy = GBApplication.Rec.getMovey() + y * z;
            counter = 0;
        }
        if (Math.abs(counter) > 50) return true;
        if (1.0f > scaleFactor) {
            float z = GBApplication.Rec.getZoom();
            float xm = GBApplication.Rec.getMovex() + GBApplication.Rec.getDw() * z / 2;
            float ym = GBApplication.Rec.getMovey() + GBApplication.Rec.getDh() * z / 2;
            float xf = 0;
            float yf = 0;
            if (cx - xm > 50) xf = 10;
            if (cx - xm < -50) xf = -10;

            if (cy - ym > 50) yf = 10;
            if (cy - ym < -50) yf = -10;
            GBApplication.Rec.setMovexy(GBApplication.Rec.getMovex() + xf, GBApplication.Rec.getMovey() + yf);

            if (GBApplication.Rec.getZoom() < GBApplication.Rec.getXzoom())
                GBApplication.Rec.setZoom(GBApplication.Rec.getZoom() + 0.01f);

            // move point
            //"pinch   ;
            counter--;
        } else {
            if (GBApplication.Rec.getZoom() > GBApplication.Rec.getMzoom())
                GBApplication.Rec.setZoom(GBApplication.Rec.getZoom() - 0.01f);
            float z = GBApplication.Rec.getZoom();
            float xm = GBApplication.Rec.getMovex() + GBApplication.Rec.getDw() * z / 2;
            float ym = GBApplication.Rec.getMovey() + GBApplication.Rec.getDh() * z / 2;
            float xf = 0;
            float yf = 0;
            if (cx - xm > 50) xf = 10;
            if (cx - xm < -50) xf = -10;

            if (cy - ym > 50) yf = 10;
            if (cy - ym < -50) yf = -10;
            GBApplication.Rec.setMovexy(GBApplication.Rec.getMovex() + xf, GBApplication.Rec.getMovey() + yf);

            //"zoom    ;
            // move point
            counter++;
        }
        return true;
    }

    private int onGear(float x, float y) {
        int result = -1;
        for (int i = MainThread.gears.size() - 1; i >= 0; i--) {
            Gear G = MainThread.gears.get(i);
            synchronized(G) {
                if (G.getLay() != GBApplication.GRec.getLc()) continue;
                if (G.inside(x, y)) return i;
            }
        }

        return result;
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {

        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        //"showHideButtons"
        GBApplication.GRec.setlStat(8);
        //hide cursor
        MainThread.cursor.reset();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }


}
