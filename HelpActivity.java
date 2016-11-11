package info.gearboxgame.gearbox;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;



public class HelpActivity extends Activity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_help);

        ListView Hlv = (ListView) findViewById(R.id.listView);
        String[] hlist = {"Game", "Gear", "Toolbar", "Toolbar", "Toolbar", "Toolbar", "Toolbar", "Toolbar",
                "Button", "Button", "Button", "Button",   "Button",
                "Rotation", "Rotation"};

        ListAdapter MyLA = new CustomAdp(this, hlist);
        Hlv.setAdapter(MyLA);

        int index = getIntent().getIntExtra("ind", 0);
        Hlv.setSelection(index);
        Hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                finish();
            }
        });



    }
    public void ActClsBtn(View v) {
        finish();
    }


}
