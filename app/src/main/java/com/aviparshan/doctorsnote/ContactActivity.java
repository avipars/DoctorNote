package com.aviparshan.doctorsnote;

/**
 * Created by avi on 2/23/2017 on com.aviparshan.doctorsnote
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.aviparshan.doctorsnote.R.id.radioGroup;

public class ContactActivity extends AppCompatActivity {

    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    //URL derived from form URL
    public static final String URL = "https://docs.google.com/forms/d/e/1FAIpQLSeWDtBj4zVU1nhtcLUQdWxZ0M06kdcLh9YLSwnSwBEBXxPC-w/formResponse";
    //input element ids found from the live form page
    public static final String EMAIL_KEY = "entry_1777033391";
    public static final String POSITION_KEY = "entry_1637835495";
    public static final String SUBJECT_KEY = "entry_1187936896";
    public static final String MESSAGE_KEY = "entry_910085193";
    public static final String email = "avi.pars+krav@gmail.com";

    public static final String JURL = "https://docs.google.com/forms/d/e/1FAIpQLSeJ9J7fX5O2llMoK6uqV5k8m7asxdlgY8rCzrnXMj21qBfjAQ/formResponse";
    public static final String jemail = "avi.pars+job@gmail.com";
    //private final Context context;
    private Toolbar toolbar;
    private EditText subjectEditText;
    private EditText messageEditText;
    private RadioGroup radioFightGroup;
    private RadioButton radioButton;
    private TextInputLayout inputLayoutSubject, inputLayoutMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get references to UI elements in the layout
        Button sendButton = (Button) findViewById(R.id.sendButton);
        Button clearButton = (Button) findViewById(R.id.clearButton);
        final RadioButton kravi = (RadioButton) findViewById(R.id.radioButton);
        final RadioButton job = (RadioButton) findViewById(R.id.radioButton2);

        inputLayoutSubject = (TextInputLayout) findViewById(R.id.input_layout_subject);
        inputLayoutMessage = (TextInputLayout) findViewById(R.id.input_layout_message);

        subjectEditText = (EditText) findViewById(R.id.subjectEditText);
        messageEditText = (EditText) findViewById(R.id.messageEditText);

        radioFightGroup = (RadioGroup) findViewById(radioGroup);

        messageEditText.addTextChangedListener(new ContactActivity.MyTextWatcher(messageEditText));
        subjectEditText.addTextChangedListener(new ContactActivity.MyTextWatcher(subjectEditText));

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kravi.setChecked(false);
                job.setChecked(false);
                subjectEditText.getText().clear();
                messageEditText.getText().clear();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Make sure all the fields are filled with values
                if (TextUtils.isEmpty(subjectEditText.getText().toString()) ||
                        TextUtils.isEmpty(messageEditText.getText().toString())) {
                    Toast.makeText(ContactActivity.this, R.string.mand, Toast.LENGTH_LONG).show();
                    return;
                }
                hideKeyboard();

                int selectedId = radioFightGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                if (selectedId == -1) {
                    Toast.makeText(ContactActivity.this, R.string.mand, Toast.LENGTH_LONG).show(); //em,pty
                    return;
                }
                //https://gist.github.com/sourabh86/4d65c12c93a545904bae
                if (radioButton.getText().toString().equals("Jobnick") || radioButton.getText().toString().equals("מפקדה")) {
                    //Create an object for PostDataTask AsyncTask
                    PostDataTask postDataTask = new PostDataTask();

                    //execute asynctask
                    postDataTask.execute(JURL,
                            jemail,
                            "Jobnick",
                            subjectEditText.getText().toString(),
                            messageEditText.getText().toString());
                } else {
                    //Create an object for PostDataTask AsyncTask
                    PostDataTask postDataTask = new PostDataTask();

                    //execute asynctask
                    postDataTask.execute(URL,
                            email,
                            "Kravi",
                            subjectEditText.getText().toString(),
                            messageEditText.getText().toString());
                }
            }


        });


    }

    //AsyncTask to send data as a http POST request
    private class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... contactData) {
            Boolean result = true;
            String url = contactData[0];
            String email = contactData[1]; //add one for new form
            String position = contactData[2];
            String subject = contactData[3];
            String message = contactData[4];
            String postBody = "";

            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody =
                        EMAIL_KEY + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                                POSITION_KEY + "=" + URLEncoder.encode(position, "UTF-8") +
                                "&" + SUBJECT_KEY + "=" + URLEncoder.encode(subject, "UTF-8") +
                                "&" + MESSAGE_KEY + "=" + URLEncoder.encode(message, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                result = false;
            }

            try {
                //Create OkHttpClient for sending request
                OkHttpClient client = new OkHttpClient();
                //Create the request body with the help of Media Type
                RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                //Send the request
                Response response = client.newCall(request).execute();

                if (!response.isSuccessful())
                    throw new IOException(getString(R.string.unex) + response);
                System.out.println(response.body().string());
            } catch (IOException exception) {
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            //Print Success or failure message accordingly
            Toast.makeText(ContactActivity.this, result ? getString(R.string.success) : getString(R.string.mess_error), Toast.LENGTH_LONG).show();
        }

    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.subjectEditText:
                    //validateSubject();
                    break;
                case R.id.messageEditText:
                    // validateMessage();
                    break;
            }
        }
    }

    public void changeLang() {
        new MaterialDialog.Builder(this)
                .title(R.string.language)
                .items(R.array.languages)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        if (which == 1) {
                            setLocale("iw");
                            Refresh();
                        } else {
                            setLocale("en");
                            Refresh();

                        }
                        return true;
                    }
                })
                .positiveText(R.string.ok)
                .show();
    }

    public void composeEmail() {
        String PhoneModel = android.os.Build.MODEL;
        String AndroidVersion = android.os.Build.VERSION.RELEASE;
        int API = Build.VERSION.SDK_INT;
        String Language = Locale.getDefault().getISO3Language();
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"avi@aviparshan.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, "App Information \n" +
                "Phone Model: " + PhoneModel + "\nAndroid Version: " + AndroidVersion + "\nAPI Level: " + API + "\nVersion Code: " + versionCode + "\nVersion Name: " + versionName + "\nLanguage: " + Language);
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContactActivity.this, R.string.rate_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.about:
                AboutDialog.show(this);
                return true;
            case R.id.feedback:
                composeEmail();
                return true;
            case R.id.lang:
                changeLang();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //test code changes language (call it in oncreate
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    private void Refresh() {
        Intent refresh = new Intent(ContactActivity.this, ContactActivity.class);
        startActivity(refresh);
        finish();
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);
        //LocaleUtils.updateConfig(this, newConfig);

    }

}
