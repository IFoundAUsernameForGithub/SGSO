package it.uniroma2.is.sgso;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class RegisterActivity extends AppCompatActivity {

    EditText emailText;
    EditText passwordText;
    EditText numberText;

    private UserRegisterTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "Myriad.ttf");
        emailText = (EditText) findViewById(R.id.email_login);
        passwordText = (EditText) findViewById(R.id.password_login);
        numberText = (EditText) findViewById(R.id.number_login);

        Button registerButton = (Button) findViewById(R.id.register);
        TextView loginText = (TextView) findViewById(R.id.login_text);

        emailText.setTypeface(myTypeface);
        passwordText.setTypeface(myTypeface);
        numberText.setTypeface(myTypeface);
        registerButton.setTypeface(myTypeface);
        loginText.setTypeface(myTypeface);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getEmail();
                String pass = getPass();
                String number = getNumber();

                attemptRegistration();

            }
        });


        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
                loginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginActivity);
            }
        });

    }

    private void attemptRegistration() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        emailText.setError(null);
        passwordText.setError(null);

        // Store values at the time of the login attempt.
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String number = numberText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordText.setError(getString(R.string.error_invalid_password));
            focusView = passwordText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailText.setError(getString(R.string.error_field_required));
            focusView = emailText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailText.setError(getString(R.string.error_invalid_email));
            focusView = emailText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new RegisterActivity.UserRegisterTask(email, password, number);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mNumber;

        UserRegisterTask(String email, String password, String number) {
            mEmail = email;
            mPassword = password;
            mNumber = number;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                URL url = new URL("https://whispering-reaches-88332.herokuapp.com/users/register");
                HttpsURLConnection client = (HttpsURLConnection) url.openConnection();
                String urlParameters = "";
                // codifico le coppie di dati da inviare
                String datiPost = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(mEmail, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(mPassword, "UTF-8") + "&" +
                        URLEncoder.encode("numeroTelefonico", "UTF-8") + "=" + URLEncoder.encode(mNumber, "UTF-8");

                // se devo inviare il dato in POST
                client.setDoOutput(true);
                client.setChunkedStreamingMode(0);

                // scrivo nello stream di uscita
                OutputStreamWriter wr = new OutputStreamWriter(client.getOutputStream());
                wr.write(datiPost);
                wr.flush();
                wr.close();

                int responseCode = client.getResponseCode();


                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            } catch (Exception e){
                return false;
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            //to change
            if (success) {
                Toast.makeText(RegisterActivity.this, "Registrazione effettuata", Toast.LENGTH_SHORT).show();

                Intent mainActivity = new Intent(RegisterActivity.this, LoginActivity.class);
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainActivity);
            } else {
                Toast.makeText(RegisterActivity.this, "Registrazione fallita", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    public String getEmail(){
        return emailText.getText().toString();
    }

    public String getPass(){
        return passwordText.getText().toString();
    }

    public String getNumber(){
        return  numberText.getText().toString();
    }
}
