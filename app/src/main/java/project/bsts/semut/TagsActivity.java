package project.bsts.semut;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import project.bsts.semut.app.ExceptionHandler;


public class TagsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_tags);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        TagsFragment tagsFragment = new TagsFragment();
        fragmentTransaction.replace(R.id.container, tagsFragment);

        fragmentTransaction.commit();
    }
}
