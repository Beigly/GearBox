package info.gearboxgame.gearbox;


import android.content.Context;
import android.content.SharedPreferences;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Loader {



    public int loadStage (int st  )  {

        InputStream is = MainActivity.getAppContext().getResources().openRawResource(R.raw.stage);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));


        GBApplication.GRec.setLx(0);
        GBApplication.GRec.setLc(0);
        MainThread.comm ="";
                // getStage
       String line = "" ;
       String c ;
       String[] s = new String[2];
       String[] cls = new String[15] ;
       if (st<=0) st=1 ;
       int  stg = 0;
       while (stg < st){
           try {   line = br.readLine() ;
           } catch (Exception e) {return 0 ;  }

           if ( line==null )  {return 0 ; }
           if (line.isEmpty()){continue;}
           cls = line.split(";");
           s = cls[0].split("=");
           c = s[0].trim() ;
           if (c.equals("S")){
           stg  = Integer.valueOf(s[1].trim());

           }
       }
        float den = GBApplication.dens ;
        cls = line.split(";");
        s = cls[1].split("=");
        float w  = Float.valueOf(s[1]);
        s = cls[2].split("=");
        float h  = Float.valueOf(s[1]);
       GBApplication.Rec.setPwh(w, h);
       GBApplication.Rec.initial();

        while (stg == st ){
            try {   line = br.readLine() ;
            } catch (EOFException e) { break ;  }
              catch (IOException e ) { break ;  }
            if (line==null){break;}
            if (line.isEmpty()){continue;}
            cls = line.split(";");
            s = cls[0].split("=");
            String key = s[0].trim();

            // MainThread.obs stcle
            if(key.equals("O")){  unpackObs(line,den);      }

            // gear
            if(key.equals("G")){  unpackGear(line,den);     }

            if(key.equals("T")){
                s = cls[0].split("=");
                MainThread.time  = Integer.valueOf(s[1].trim());
                s = cls[1].split("=");

                MainThread.score   = Integer.valueOf(s[1].trim() );
                s = cls[2].split("=");
                MainThread.pgear  = Integer.valueOf(s[1].trim() );
                s = cls[3].split("=");
                MainThread.ptime  = Integer.valueOf(s[1].trim() );

            }
            if(key.equals("C")){

                MainThread.comm =  s[1] ;
            }
            //stage
            if(key.equals("S")){  break;  }

       }

        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int TLx = GBApplication.GRec.getLx() ;
        if ( TLx > 0) { // color correction
            GBApplication.GRec.setLc(0);
            for (int i = 0 ; i <MainThread.gears.size() ; i++ ){
                if (0 == MainThread.gears.get(i).getLay()) MainThread.gears.get(i).colorCorr();
                else break;
            }
            GBApplication.GRec.setLc(TLx);

        }
        String sent =  MainThread.comm ;
        if (sent.indexOf("#")!= -1){
            int i = sent.indexOf("#");
            String sent1 = sent.substring(0, i );
            String sent2 = sent.substring(i+1);
            MainThread.comm = sent1  + "\n" + sent2 ;
        }

        return stg ;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    private String packGear(Gear g ,float den){
        String line = "";
        float x = g.getXc() ;
        float y = g.getYc() ;
        int   l = g.getLay() ;
        int   t = g.getGearType() ;

        line =  " ; X="+Float.toString(x/den)+"; Y="+Float.toString(y/den) ;
        line = line + "; L="+Integer.toString(l)+"; T="+Integer.toString(t);
        float a = g.getAlpha();
        float b = g.getBeta();
        float te = g.getTeta();
        int   n = g.getN();
        line = line + "; A="+Float.toString(a)+"; B="+Float.toString(b)+"; T="+Float.toString(te)+"; N="+Integer.toString(n);
        int  f = 0;
        if (g.getFil()) f = 1;
        int d = g.getDir() ;
        int d0 = g.getDir0() ;
        float s= g.getSpeed() ;
        float s0= g.getSpeed0() ;
        line = line + "; E="+Float.toString(s)+ "; E="+Float.toString(s0)+"; D="+Integer.toString(d)+"; D="+Integer.toString(d0)+"; F="+Integer.toString(f)+";";


        return line ;
    }
    private void unpackGear(String  line ,float den){

        String c ;
        String[] s = new String[2];
        String[] cls = new String[15] ;
        cls = line.split(";");
        s = cls[1].split("=");
        float x = Float.valueOf(s[1]);
        s = cls[2].split("=");
        float y = Float.valueOf(s[1]);
        s = cls[3].split("=");
        int L = Integer.valueOf(s[1]);
        s = cls[4].split("=");
        int T = Integer.valueOf(s[1]);
        s = cls[5].split("=");
        float a = Float.valueOf(s[1]);
        s = cls[6].split("=");
        float b = Float.valueOf(s[1]);
        s = cls[7].split("=");
        float t = Float.valueOf(s[1]);
        s = cls[8].split("=");
        int n = Integer.valueOf(s[1]);
        s = cls[9].split("=");
        float e = Float.valueOf(s[1]);
        s = cls[10].split("=");
        float e0 = Float.valueOf(s[1]);
        s = cls[11].split("=");
        int d = Integer.valueOf(s[1]);
        s = cls[12].split("=");
        int d0 = Integer.valueOf(s[1]);
        s = cls[13].split("=");
        int f = Integer.valueOf(s[1]);
        GBApplication.GRec.setLc(L);
        GBApplication.GRec.setAlpha(a);
        GBApplication.GRec.setBeta(b);
        GBApplication.GRec.setTeta(t);

        int TLx = GBApplication.GRec.getLx();
        if (TLx < L) GBApplication.GRec.setLx(L);
        GBApplication.GRec.setN(n);

        Gear g = new Gear(x*den,y*den  );

        g.setSpeed(e);
        g.setSpeed0(e0);
        g.setDir(d);
        g.setDir0(d0);
        if (f==0)g.setFil(false);
        else g.setFil(true);
        g.setGearType(T);
        synchronized(MainThread.gears) {    MainThread.gears.add(g);      }


    }
    private String packObs(Obstacle o ,float den){
        String line = "";
        float x = o.getXc()  ;
        x = x / den ;
        float y = o.getYc()  ;
        y = y / den ;
        float r = o.getRadus() ;
        r = r / den ;

        int   l = o.getLay() ;
        int   t = o.getType() ;
        line =  " ; X="+Float.toString(x)+"; Y="+Float.toString(y) ;
        line = line + "; R="+Float.toString(r)+"; L="+Integer.toString(l)+"; T="+Integer.toString(t)+";";

        return line ;
    }
    private void unpackObs(String  line , float den){
        String c ;
        String[] s = new String[2];
        String[] cls = new String[15] ;
        cls = line.split(";");
        s = cls[1].split("=");
        float x = Float.valueOf(s[1]);
        s = cls[2].split("=");
        float y = Float.valueOf(s[1])   ;
        s = cls[3].split("=");
        float R = Float.valueOf(s[1]);
        s = cls[4].split("=");
        int L = Integer.valueOf(s[1]);
        s = cls[5].split("=");
        int T = Integer.valueOf(s[1]);
        Obstacle ob =new Obstacle(x*den, y*den, R*den, L, T);
        synchronized(MainThread.obs)   {
                MainThread.obs.add(ob);
        }
        int TLx = GBApplication.GRec.getLx();
        if (TLx < L) GBApplication.GRec.setLx(L);
        int TLc = GBApplication.GRec.getLc();
        if (TLc < L)GBApplication.GRec.setLc(L);
    }
    //////////////////////////////////////////////////
    public int   Init ( )   {
        SharedPreferences sp =  MainActivity.getAppContext().getSharedPreferences("stage", Context.MODE_PRIVATE);
        String line = sp.getString("Header","");
        if (line.isEmpty()) return 0 ;

        GBApplication.GRec.setLx(0);
        GBApplication.GRec.setLc(0);
        float den = GBApplication.dens ;

        String c ;
        String[] s = new String[2];
        String[] cls = new String[16] ;

        cls = line.split(";");
        s = cls[0].split("=");
        int stg  = Integer.valueOf(s[1].trim());
        if (stg < 1 ) return 0 ;
        s = cls[1].split("=");
        float w  = Float.valueOf(s[1]);
        s = cls[2].split("=");
        float h  = Float.valueOf(s[1]);
        s = cls[3].split("=");
        int nG  = Integer.valueOf(s[1]);
        s = cls[4].split("=");
        int nO  = Integer.valueOf(s[1]);
        s = cls[5].split("=");
        int ttime = Integer.valueOf(s[1]);
         MainThread.time = ttime  ;
        s = cls[6].split("=");
        int tscore  = Integer.valueOf(s[1]);
         MainThread.score = tscore ;
        s = cls[7].split("=");
        MainThread.pgear  = Integer.valueOf(s[1]);
        s = cls[8].split("=");
        MainThread.ptime  = Integer.valueOf(s[1]);
        MainThread.comm = "The Last Gearbox arrangement" ;
        GBApplication.Rec.setPwh(w ,h);
        GBApplication.Rec.initial();



        if (nG < 1 || w < 500 || h <300 ) return 0 ;
        if (ttime < 1 || tscore < 1   ) return 0 ;
        for (int i=0 ; i < nG ; i++){
            String key = "G"+Integer.toString(i);
            line = sp.getString(key,"");
            unpackGear(line ,den);
        }
        for (int i=0 ; i < nO ; i++){
            String key = "O"+Integer.toString(i);
            line = sp.getString(key,"");
            unpackObs(line ,den);
        }
        int TLx = GBApplication.GRec.getLx() ;
        if ( TLx > 0) { // color correction
            GBApplication.GRec.setLc(0);
            for (int i = 0 ; i <MainThread.gears.size() ; i++ ) {
                if (0 == MainThread.gears.get(i).getLay()) MainThread.gears.get(i).colorCorr();
                else break;
            }
            GBApplication.GRec.setLc(TLx);
            }

        if (stg==1)MainThread.sscore = 0 ;
        return stg ;
    }
 ////////////////////////////////////////////////////////////////////////
    public void save(int stg){
        SharedPreferences sp =  MainActivity.getAppContext().getSharedPreferences("stage", Context.MODE_PRIVATE);
        SharedPreferences.Editor edi = sp.edit() ;
        String line ="";
        int nG = MainThread.gears.size() ;
        int nO = MainThread.obs.size() ;
        float den = GBApplication.dens ;
        float w = GBApplication.Rec.getPw() / den ;
        float h = GBApplication.Rec.getPh() / den ;
        if (MainThread.score < 0) MainThread.score = 0 ;
        line = "S="+Integer.toString(stg)+"; W="+ Float.toString(w)+"; H="+ Float.toString(h)+"; G="+Integer.toString(nG);
        line = line + "; O="+Integer.toString(nO) +"; T="+Integer.toString(MainThread.time) +"; S="+Integer.toString(MainThread.score);
        line = line + "; P="+Integer.toString(MainThread.pgear) +"; P="+Integer.toString(MainThread.ptime)+";";
        edi.putString("Header",line);


        for(int i = 0 ; i < nG ; i++){

            String key = "G"+Integer.toString(i);
            Gear g = MainThread.gears.get(i);
            line = packGear(g ,den) ;
            edi.putString(key ,line);

        }
        for(int i = 0 ; i < nO ; i++){

            String key = "O"+Integer.toString(i);
            Obstacle o = MainThread.obs.get(i);
            line = packObs(o ,den);
            edi.putString(key ,line);

        }
        edi.apply();




    }
    ///////////////////////////////////////////////////////////////////////
     public void saveUser(int ind,String line){
         SharedPreferences sp =  MainActivity.getAppContext().getSharedPreferences("user", Context.MODE_PRIVATE);
         SharedPreferences.Editor edi = sp.edit() ;

         String key = "user"+Integer.toString(ind);
         edi.putString(key,line);

         edi.apply();

     }
    /////////////////////////////////////////////////////////////////////////////////////////////
    public String  User(int n)   {
        SharedPreferences sp =  MainActivity.getAppContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String key = "user"+Integer.toString(n);
        String line = sp.getString(key,"");
        return line ;
    }



}
