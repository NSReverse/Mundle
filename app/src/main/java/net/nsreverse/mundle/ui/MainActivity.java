package net.nsreverse.mundle.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;

import net.nsreverse.mundle.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Class MainActivity -> AppCompatActivity -
 *
 * This class is the first screen of this app.
 *
 * @author Robert
 * Created on 7/8/2017
 */
public class MainActivity extends AppCompatActivity {

    private static final String KEY_SESSION_ID = "session";
    private static final int LOGIN_REQUEST_CODE = 101;
    private static final int DELETE_REQUEST_CODE = 102;

    @BindView(R.id.card_view_my_classes) private CardView classroomsCardView;
    @BindView(R.id.card_view_my_notes) private CardView notesCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }
}
