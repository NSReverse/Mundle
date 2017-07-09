package net.nsreverse.mundle.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.nsreverse.mundle.R;

/**
 * Class MainActivity -> AppCompatActivity -
 *
 * This class is the first screen of this app.
 *
 * @author Robert
 * Created on 7/8/2017
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
