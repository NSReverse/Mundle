package net.nsreverse.mundle.ui.accounts;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import net.nsreverse.mundle.MundleApplication;
import net.nsreverse.mundle.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created on 7/21/2017
 */
public class ChangePasswordActivity extends AppCompatActivity {

    @BindView(R.id.edit_text_password) EditText passwordEditText;
    @BindView(R.id.button_change_password) Button changePasswordButton;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getString(R.string.other_change_pass));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        ButterKnife.bind(this);

        setupComponents();
    }

    private void setupComponents() {
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = passwordEditText.getText().toString();

                if (newPassword.length() == 0) {
                    if (MundleApplication.isLoggingEnabled) {
                        Timber.d("Password is empty.");
                        Toast.makeText(context,
                                context.getString(R.string.content_failed_password_empty),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    ParseUser.getCurrentUser().setPassword(newPassword);
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Toast.makeText(context,
                                        context.getString(R.string.content_failed_set_new_password)
                                                + " " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }

                            finish();
                        }
                    });
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
