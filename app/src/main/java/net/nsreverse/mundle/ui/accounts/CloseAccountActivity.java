package net.nsreverse.mundle.ui.accounts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import net.nsreverse.mundle.MundleApplication;
import net.nsreverse.mundle.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CloseAccountActivity extends AppCompatActivity {

    @BindView(R.id.edit_text_username) EditText usernameEditText;
    @BindView(R.id.button_close_account) Button closeAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_account);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getString(R.string.other_delete_account));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        setupComponents();
    }

    private void setupComponents() {
        closeAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();

                if (ParseUser.getCurrentUser().getUsername().equals(username)) {
                    if (MundleApplication.closeAccountsEnabled) {
                        ParseUser.getCurrentUser().deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(CloseAccountActivity.this, "Your account has been deleted.",
                                        Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                    }
                    else {
                        Toast.makeText(CloseAccountActivity.this,
                                "Unable to delete account, account deletions disabled.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(CloseAccountActivity.this, "Unable to delete account, username" +
                            " mismatch.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
