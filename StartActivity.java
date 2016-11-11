package info.gearboxgame.gearbox;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends Activity {
    private  TextView tvl , ts ,tl    ;
    private EditText etn;
    private Button bok ;
    private String[][]sarry = new String[5][3];
    private String luser ;
    private Timer timer ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_start);
        tvl = (TextView) findViewById(R.id.textViewst);
        etn = (EditText) findViewById(R.id.editTextst);
        tvl.setVisibility(View.INVISIBLE);
        etn.setVisibility(View.INVISIBLE);
        bok = (Button) findViewById(R.id.butOk);
        ts  = (TextView) findViewById(R.id.textViews);
        tl = (TextView) findViewById(R.id.textViewsv);

        ts.setVisibility(View.INVISIBLE);
        tl.setVisibility(View.INVISIBLE);
        bok.setVisibility(View.INVISIBLE);
        etn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER ||keyCode == KeyEvent.KEYCODE_ESCAPE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etn.getWindowToken(), 0);
                }
                select(v);
                return false;
            }
        });
        for (int i = 0 ; i<5 ;i++){
          for(int j = 0 ; j<3 ;j++ ){ sarry[i][j] = " ";}
        }

        for (int i = 0 ; i < 5 ; i++){

            String line = MainActivity.loder.User(i);
            if (line.isEmpty()) break ;
            String[] cls = new String[4] ;
            cls = line.split(";");
            sarry[i][0] = cls[0];
            sarry[i][1] = cls[1];
            sarry[i][2] = cls[2];
        }
        luser =  sarry[0][0].trim() ;

        if (sarry[0][0].equals(" ")) sarry[0][0] = "NoName" ;
        if (sarry[0][1].equals(" ")) sarry[0][1] = "1" ;
        if (sarry[0][2].equals(" ")) sarry[0][2] = "0" ;
        etn.setText(sarry[0][0]);
        tl.setText(sarry[0][1]);

        GBApplication.winMode = 0 ;
        ControlActivity.sndEn = true ;
        ControlActivity.mp0 = MediaPlayer.create(getBaseContext(), R.raw.start);


        if (ControlActivity.sndEn) {
          ControlActivity.mp0.start();
            ControlActivity.sndMode = 0;
        }
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (ControlActivity.sndEn) {
                    ControlActivity.mp0.start();
                    ControlActivity.sndMode = 0;
                }
            }
        },20500,20500   );

    }




    public void ActClose(View v) {
        if (tvl.getVisibility()== View.INVISIBLE ){
            tvl.setVisibility(View.VISIBLE);
            etn.setVisibility(View.VISIBLE);
            tl.setVisibility(View.VISIBLE);
            bok.setVisibility(View.VISIBLE);
            ts.setVisibility(View.VISIBLE);

        }
        else {

            String name = etn.getText().toString() ;
            name =  name.trim() ;
            boolean find = false ;
            int j = 0;
            for (int i = 0 ; i < 5 ; i++ ){
                if (sarry[i][0].equals( name )) {
                    j = i ;
                    find = true ;
                    break ;
                }
            }
            if (find){ //exchang
                String s0 = sarry[0][0];
                String s1 = sarry[0][1];
                String s2 = sarry[0][2];

                sarry[0][0] = sarry[j][0] ;
                sarry[0][1] = sarry[j][1];
                sarry[0][2] = sarry[j][2];

                sarry[j][0] = s0 ;
                sarry[j][1] = s1 ;
                sarry[j][2] = s2 ;

            }else { //shift
                for (int i = 3 ; i >= 0 ; i-- ){
                    sarry[i+1][0] = sarry[i][0] ;
                    sarry[i+1][1] = sarry[i][1] ;
                    sarry[i+1][2] = sarry[i][2] ;
                }
                sarry[0][0] = name ;
                sarry[0][1] = "1" ;
                sarry[0][2] = "0" ;

            }
            for (int i = 0 ; i < 5 ; i++) {

                MainActivity.loder.saveUser(i, sarry[i][0] + ";" + sarry[i][1] + ";" + sarry[i][2]);
            }

            MainThread.uname = sarry[0][0].trim();
            String tmp = sarry[0][1];
            tmp = tmp.trim() ;
            MainThread.stageNo = Integer.valueOf(tmp);
            tmp = sarry[0][2];
            tmp = tmp.trim() ;
            MainThread.sscore = Integer.valueOf(tmp);
            tmp = sarry[0][0];
            tmp = tmp.trim() ;
            if (tmp.equals(luser)) MainThread.sameuser = true ;
            else MainThread.sameuser = false ;
            GBApplication.winMode = 1 ;
            ControlActivity.sndMode = 1;
             setResult(5);

            finish();
        }
    }



  public void select(View v){

          if (tvl.getVisibility()== View.INVISIBLE ){
              tvl.setVisibility(View.VISIBLE);
              etn.setVisibility(View.VISIBLE);
              tl.setVisibility(View.VISIBLE);
              bok.setVisibility(View.VISIBLE);
              ts.setVisibility(View.VISIBLE);

          }
          else {

              String name = etn.getText().toString() ;
              name =  name.trim() ;
              boolean find = false ;
              int j = 0;
              for (int i = 0 ; i < 5 ; i++ ){
                  if (sarry[i][0].equals( name )) {
                      j = i ;
                      find = true ;
                      break ;
                  }
              }
              if (find){ //exchang
                   tl.setText(sarry[j][1]);
              }else{tl.setText("1");}
          }
      }
    public void onBackPressed() {
        super.onBackPressed();
        setResult(6);
        finish();
    }

    @Override
    protected void onStop() {

        super.onStop();
        setResult(6);
        finish();

    }

    @Override
    protected void onPause() {
        super.onPause();
        setResult(6);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();


    }

}
