package info.gearboxgame.gearbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by beigly on 19.05.2016.
 */
  class CustomAdp extends ArrayAdapter<String> {
    private  TextView ind  , hmt ,txt;
    private  ImageView img ;
    public CustomAdp(Context context, String[] items) {

        super(context, R.layout.help_raw, items);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater  myLayInf = LayoutInflater.from(getContext());
        View CustomView = myLayInf.inflate(R.layout.help_raw, parent, false);
        ind = (TextView) CustomView.findViewById(R.id.textView1);
        hmt = (TextView) CustomView.findViewById(R.id.textView2);
        img = (ImageView) CustomView.findViewById(R.id.imggo);
        txt = (TextView) CustomView.findViewById(R.id.textView3);

        ind.setText(Integer.toString(position + 1) + ". " + getItem(position));
        getText(position);
        getImg(position);

        return CustomView ;
    }
    private  void getText(int i){
        String[] cls = {"Goal is :",
                        "Tap" ,
                        "Close" ,
                        "Zoom" ,
                        "Gear Insert" ,
                        "Sound" ,
                        "Help" ,
                        "Layers",
                        "Alpha",
                        "Beta",
                        "Teta",
                        "Teeth",

                        "move",
                        "Rotation Direction",
                        "Rotation Speed RPM",};
        String[] des = { "Connecting Gears from Source (Driver) gear to Destination (Driven) gears.  Insert gears and transfer rotation"
            ,"Tap on Gear Shows Gear Parameters.  Tap on Normal gears make them Enable/Disable.  In disable mode, Gear can be modified (e.g. Teeth shapes, size ) or dragged"
            , "Close toolbar \n Double tap to switch on/off Toolbar \n Long tap to switch on/off Toolbar "
            , "Change Zoom \n  also with screen Pinch zoom could be changed"
            , "Tap icon to show Insert location \n Tap screen to change insert location \n 2th tap icon to Insert Gear on specified loc."
            ,"On/Off Sound of Game "
            ,"Open Help list"

                ,"Open Layer Buttons \n For Multi-Layer Stages  \n To select Active Layer or Change Layer "+
            "\n It should be used in Multi-Layer stages "

                ,"Alpha : The length of Tooth point (mm) \n it should be changed in multi-shape tooth gear box."+
            "\n With small value : it help to fit teeth "

                ,"Beta : The length of Tooth height (mm) \n it should be changed in multi-shape tooth gear box."+
            "\n With bigger value :it make possible that:\n gears has variable rotation radius \n and match different size gears"

                ,"Teta : The length of Tooth width (mm) \n  it should be changed in multi-shape tooth gear box."+
            "\n between the gears it must changed gradually \n about 5mm otherwise they can not be connected "

            ,"Increase / Decrease Teeth number \n it changes the radius and size of gear"+
            "\n Two gears with similar tooth shape \n maybe can not connect because they have \n big size difference (rotation radius difference)"+
            "\n In this case size(teeth) should be changed gradually  "

            ,"Displace game area. \n it is possible with screen drag  "
            ," CW : Clock Wise   CCW :  Counter Clock Wise \n(CW) : desire rotation direction is clock wise.\n(CCW): desire is counter clock wise rotation.  "
            ,"RPM : Revolutions Per Minute \n Rpm :(1.5) means  gear desire rotation speed is 1.5 Rpm \n"
            +"Rpm depends of source gear Rpm1 and its teeth No. n1 and destination gear teeth No. n2 "
            +"\n Rpm1 x n1 =  Rpm2 x n2  \n     Rpm2 =  ( Rpm1 x n1 ) / n2    \n"
            +"In multi-layer Gearbox you can transfer rotation to upper layer then change n1 , n2 to get your desire Rpm "
            +"\n (in one layer) No matter how many gears connect first gear and last gear above rotation speed formula can be "
            + "used between first and last gear \n   Rpm1 x n1 = Rpm2 x n2  "} ;
        hmt.setText(cls[i]);
        txt.setText(des[i]);
    }
    private  void getImg(int i){

        switch (i) {
            case 0:
            img.setImageResource(R.drawable.h00);
                break ;
            case 1:
                img.setImageResource(R.drawable.gai);
                break ;
            case 2:
                img.setImageResource(R.drawable.end1);
                break ;
            case 3:
                img.setImageResource(R.drawable.zoom);
                break ;
            case 4:
                img.setImageResource(R.drawable.gear);
                break ;
            case 5:
                img.setImageResource(R.drawable.spk2);
                break ;
            case 6:
                img.setImageResource(R.drawable.help2);
                break ;
            case 7:
                img.setImageResource(R.drawable.lay2);
                break ;
            case 8:
                img.setImageResource(R.drawable.alpha);
                break ;
            case 9:
                img.setImageResource(R.drawable.beta);
                break ;
            case 10:
                img.setImageResource(R.drawable.teta);
                break ;
            case 11:
                img.setImageResource(R.drawable.teeth);
                break ;
            case 12:
                img.setImageResource(R.drawable.nu);
                break ;
            case 13:
                img.setImageResource(R.drawable.ccw);
                break ;
            case 14:
                img.setImageResource(R.drawable.rpm);
                break ;
        }
    }

}
