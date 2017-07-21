package net.nsreverse.mundle.ui.accounts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import net.nsreverse.mundle.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangeNameActivity extends AppCompatActivity {

    @BindView(R.id.edit_text_first_name) EditText firstNameEditText;
    @BindView(R.id.edit_text_last_name) EditText lastNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getString(R.string.other_set_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_set_name, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        else if (item.getItemId() == R.id.menu_action_save_name) {
            ParseUser.getCurrentUser().put("first_name", firstNameEditText.getText().toString());
            ParseUser.getCurrentUser().put("last_name", lastNameEditText.getText().toString());
            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    finish();
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
