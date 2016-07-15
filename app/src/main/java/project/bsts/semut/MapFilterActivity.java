package project.bsts.semut;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import project.bsts.semut.app.ExceptionHandler;


public class MapFilterActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private View mainView;
    private int[] ids = {R.id.checkUsers, R.id.checkCCTV, R.id.checkPolice, R.id.checkAccident, R.id.checkTraffic, R.id.checkOther, R.id.checkCommuterTrain, R.id.checkAngkot};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_map_filter);

        mainView = findViewById(R.id.mainView);
        mainView.setClickable(true);
        mainView.setOnClickListener(this);

        int filter = SemutActivity.FilterMapValue;
        for(int i=0; i<ids.length; i++){
            CheckBox check = (CheckBox) findViewById(ids[i]);
            check.setChecked((1 & (filter >> i)) > 0);
            check.setOnCheckedChangeListener(this);
            check.setTag(""+ (1 << i));
        }
    }

    @Override
    public void onBackPressed() {
        mainView.setBackgroundResource(android.R.color.transparent);
        finish();
        this.overridePendingTransition(R.anim.come_from_right, R.anim.out_to_right);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        mainView.postDelayed(new Runnable() {
            public void run() {
                mainView.setBackgroundResource(R.color.semi_transparent);
            }
        }, 500);
    }

    @Override
    public void onClick(View v) {
        if(v == mainView){
            onBackPressed();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int index = Integer.parseInt((String) buttonView.getTag());
        int filter = SemutActivity.FilterMapValue;

        if(isChecked){
            filter |= index;
            Log.i("SHIT", String.valueOf(index));
        }else {
            Log.i("SHIT", String.valueOf(index));
            filter &= (~index);
        }

        Log.i("SHIT", String.valueOf(filter));

        SemutActivity.FilterMapValue = filter;
    }
}
