package net.nsreverse.mundle.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import net.nsreverse.mundle.MundleApplication;
import net.nsreverse.mundle.R;
import net.nsreverse.mundle.data.Analytics;
import net.nsreverse.mundle.data.UserDefaults;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class AuthenticationActivity extends AppCompatActivity {

    private Context currentContext;

    private static final String KEY_USERNAME_TEXT = "username";
    private static final String KEY_PASSWORD_TEXT = "password";
    private static final String KEY_IS_TEACHER = "is_teacher";

    @BindView(R.id.edit_text_username) EditText usernameEditText;
    @BindView(R.id.edit_text_password) EditText passwordEditText;
    @BindView(R.id.button_login) Button loginButton;
    @BindView(R.id.button_register) Button registerButton;
    @BindView(R.id.checkbox_teacher) CheckBox teacherCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        assert getSupportActionBar() != null;
        getSupportActionBar().hide();

        currentContext = this;

        ButterKnife.bind(this);
        setupComponents();

        if (savedInstanceState != null) {
            usernameEditText.setText(savedInstanceState.getString(KEY_USERNAME_TEXT));
            passwordEditText.setText(savedInstanceState.getString(KEY_PASSWORD_TEXT));
            teacherCheckbox.setChecked(savedInstanceState.getBoolean(KEY_IS_TEACHER));
        }
    }

    private void setupComponents() {
        teacherCheckbox.setChecked(UserDefaults.getDefaultTeacher(this));
        usernameEditText.setText(UserDefaults.getDefaultUsername(this));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logInInBackground(
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (e == null) {
                                    if (user.getBoolean("is_teacher") ==
                                            teacherCheckbox.isChecked()) {
                                        MundleApplication.sessionToken = user.getSessionToken();

                                        UserDefaults.setDefaultTeacher(currentContext,
                                                teacherCheckbox.isChecked());
                                        UserDefaults.setDefaultUsername(currentContext,
                                                user.getUsername());

                                        Analytics.firebaseLogUserLogin(currentContext);

                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(currentContext,
                                                "Failed to Login: Are you a teacher?",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    String errorMessage = e.getMessage();

                                    if (errorMessage.isEmpty()) {
                                        errorMessage = "(Unknown Error)";
                                    }

                                    Toast.makeText(currentContext,
                                            "Failed to Login: " + errorMessage,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ParseUser newUser = new ParseUser();
                newUser.put("username", usernameEditText.getText().toString());
                newUser.put("password", passwordEditText.getText().toString());
                newUser.put("is_teacher", teacherCheckbox.isChecked());
                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            UserDefaults.setDefaultTeacher(currentContext,
                                    teacherCheckbox.isChecked());
                            UserDefaults.setDefaultUsername(currentContext,
                                    newUser.getUsername());

                            MundleApplication.sessionToken = newUser.getSessionToken();

                            Analytics.firebaseLogUserLogin(currentContext);
                            Analytics.firebaseLogNewUserType(currentContext,
                                    teacherCheckbox.isChecked());

                            setResult(RESULT_OK);
                            finish();
                        }
                        else {
                            String message = e.getMessage();

                            if (message.isEmpty()) {
                                message = "(Unknown Error)";
                            }

                            Toast.makeText(currentContext,
                                    "Failed to Register: " + message,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KEY_USERNAME_TEXT, usernameEditText.getText().toString());
        outState.putString(KEY_PASSWORD_TEXT, passwordEditText.getText().toString());
        outState.putBoolean(KEY_IS_TEACHER, teacherCheckbox.isChecked());
    }
}
