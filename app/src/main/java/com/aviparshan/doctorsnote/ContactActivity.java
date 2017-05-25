package com.aviparshan.doctorsnote;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import smtchahal.materialspinner.MaterialSpinner;

import static com.aviparshan.doctorsnote.R.id.lang;

public class ContactActivity extends AppCompatActivity {
    private static final String TAG = "ContactActivity";

    private FirebaseAnalytics mFirebaseAnalytics;
    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    //URL derived from form URL
    public static final String URL = "https://docs.google.com/forms/d/e/randomform/formResponse";
    //input element ids found from the live form page
    public static final String PLUGA_KEY = "entry.123456789"; //pluga, etc. 
    public static final String NAME_KEY = "entry.2921828181"; //name
    public static final String TZEVET_KEY = "entry.223543553355"; //tzevet team
    public static final String PHONE_KEY = "entry.2822981911"; //PHONE
    public static final String MESSAGE_KEY = "entry.144455555"; //chayal and reason

    //private final Context context;
    private Toolbar toolbar;
    private EditText subjectEditText, messageEditText, phoneEditText, tzevetEditText;
    private TextInputLayout inputLayoutSubject, inputLayoutTzevet, inputLayoutPhone,inputLayoutMessage;
    private RadioGroup radioFightGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        FirebaseCrash.log("Activity Created");

                //Get references to UI elements in the layout
        Button sendButton = (Button) findViewById(R.id.sendButton);
        Button clearButton = (Button) findViewById(R.id.clearButton);

        inputLayoutSubject = (TextInputLayout) findViewById(R.id.input_layout_subject);
        inputLayoutTzevet = (TextInputLayout) findViewById(R.id.input_layout_tzevet);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        inputLayoutMessage = (TextInputLayout) findViewById(R.id.input_layout_message);

        subjectEditText = (EditText) findViewById(R.id.subjectEditText);
        tzevetEditText = (EditText) findViewById(R.id.tzevetEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        messageEditText = (EditText) findViewById(R.id.messageEditText);

        //radioFightGroup = (RadioGroup) findViewById(radioGroup);
        //Spinner staticSpinner = (Spinner) findViewById(R.id.spinner);
         final MaterialSpinner spinner;
        subjectEditText.addTextChangedListener(new ContactActivity.MyTextWatcher(subjectEditText));
        tzevetEditText.addTextChangedListener(new ContactActivity.MyTextWatcher(tzevetEditText));
        phoneEditText.addTextChangedListener(new ContactActivity.MyTextWatcher(phoneEditText));
        messageEditText.addTextChangedListener(new ContactActivity.MyTextWatcher(messageEditText));

        String[] ITEMS = {getString(R.string.pluga_a), getString(R.string.pluga_b), getString(R.string.pluga_c), getString(R.string.pluga_d), getString(R.string.radio_mifkada), getString(R.string.bet), getString(R.string.amlach)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (MaterialSpinner) findViewById(R.id.spinner1);
        spinner.setAdapter(adapter);
        //spinner.setHint(R.string.pluga);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kravi.setChecked(false);

                subjectEditText.getText().clear();
                tzevetEditText.getText().clear();
                phoneEditText.getText().clear();
                messageEditText.getText().clear();
                subjectEditText.requestFocus();
                //spinner.requestFocus();
                FirebaseCrash.log("Clear Button Pressed");
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Make sure all the fields are filled with values
                if (TextUtils.isEmpty(subjectEditText.getText().toString()) ||  TextUtils.isEmpty(tzevetEditText.getText().toString()) || phoneEditText.length() == 0  ||
                        TextUtils.isEmpty(messageEditText.getText().toString())) {
                    Toast.makeText(ContactActivity.this, R.string.mand, Toast.LENGTH_LONG).show();
                    return;
                }
                hideKeyboard();

                //(String) spinner.getSelectedItem();
               // int selectedId = radioFightGroup.getCheckedRadioButtonId();
                //long id =  spinner.getSelectedItemId();
                //radioButton = (RadioButton) findViewById(selectedId);
                //if (id == -1) {
                 //  Toast.makeText(ContactActivity.this, R.string.mand, Toast.LENGTH_LONG).show(); //em,pty
                  //  return;
             //  }
                String itemText = spinner.getSelectedItem().toString();

                //String itemText = "Amlac";
                String pos = "test";

                if(itemText.equals("Pluga") || itemText.equals("פלוגה")){
                    //pos = "לוחמים";
                    Toast.makeText(ContactActivity.this, R.string.mand, Toast.LENGTH_LONG).show(); //em,pty
                     return;
               }
                switch (itemText) {
                    case "Battalion A":
                    case "פלגה א":
                        pos = "פלגה א";
                        break;
                    case "Battalion B":
                    case "פלגה ב":
                        pos = "פלגה ב";
                        break;
                    case "Battalion C":
                    case "פלגה ג":
                        pos = "פלגה ג";
                        break;
                    case "Battalion D":
                    case "פלגה ד":
                        pos = "פלגה ד";
                        break;
                    case "Mifkada":
                    case "מפקדה":
                        pos = "מפקדה";
                        break;
                    case "Bet Sefer":
                    case "בית ספר":
                        pos = "בית ספר";
                        break;
                    case "Amlach":
                    case "אמלח":
                        pos = "אמלח";
                        break;
                    default:
                        Toast.makeText(ContactActivity.this, "Error with radio buttons: " + pos, Toast.LENGTH_SHORT).show();
                        return;
                }

                //https://gist.github.com/sourabh86/4d65c12c93a545904bae
                //Create an object for PostDataTask AsyncTask
                PostDataTask postDataTask = new PostDataTask();

                //execute asynctask
                postDataTask.execute(URL,
                        pos,
                        subjectEditText.getText().toString(),
                        tzevetEditText.getText().toString(),
                        phoneEditText.getText().toString(),
                        messageEditText.getText().toString());

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, subjectEditText.getText().toString());
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, pos);
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "submission");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                FirebaseCrash.log("Send Button Pressed");

            }
        });
    }


    //AsyncTask to send data as a http POST request
    private class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... contactData) {
            Boolean result = true;
            String url = contactData[0];
            String position = contactData[1];
            String tzevet = contactData[2];
            String subject = contactData[3];
            String phone = contactData[4];
            String message = contactData[5];
            String postBody = "";

            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody =
                                PLUGA_KEY + "=" + URLEncoder.encode(position, "UTF-8") +
                                "&" + NAME_KEY + "=" + URLEncoder.encode(subject, "UTF-8") +
                                        "&" + TZEVET_KEY + "=" + URLEncoder.encode(tzevet, "UTF-8") +
                                "&" + PHONE_KEY + "=" + URLEncoder.encode(phone, "UTF-8") + "&" +
                                        MESSAGE_KEY + "=" + URLEncoder.encode(message, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                result = false;
                FirebaseCrash.logcat(Log.ERROR, TAG, "UnsupportedEncodingException caught");
                FirebaseCrash.report(ex);
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
                FirebaseCrash.logcat(Log.ERROR, TAG, "IOException caught");
                FirebaseCrash.report(exception);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            //Print Success or failure message accordingly
            Toast.makeText(ContactActivity.this, result ? getString(R.string.success) : getString(R.string.mess_error), Toast.LENGTH_LONG).show();
            FirebaseCrash.log("Post Execute");

        }

    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            FirebaseCrash.log("Hide Keyboard");

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
                case R.id.tzevetEditText:
                    //validateSubject();
                    break;
                case R.id.phoneEditText:
                   // validateName();
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
                        String language = "";
                        if (which == 1) {
                            setLocale("iw");
                            language = "Hebrew";
                            FirebaseAnalytics analytics = FirebaseAnalytics.getInstance( ContactActivity.this );
                            analytics.setUserProperty( "Language", language );
                            FirebaseCrash.log("Change Language iw");

                        } else {
                            setLocale("en");
                            language = "English";
                            FirebaseAnalytics analytics = FirebaseAnalytics.getInstance( ContactActivity.this );
                            analytics.setUserProperty( "Language", language );
                            FirebaseCrash.log("Change Language en");

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
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"apps@aviparshan.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, "App Information \n" +
                "Phone Model: " + PhoneModel + "\nAndroid Version: " + AndroidVersion + "\nAPI Level: " + API + "\nVersion Code: " + versionCode + "\nVersion Name: " + versionName + "\nLanguage: " + Language);
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContactActivity.this, R.string.rate_error, Toast.LENGTH_SHORT).show();
            FirebaseCrash.logcat(Log.ERROR, TAG, "ActivityNotFound");
            FirebaseCrash.report(ex);
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
            case R.id.feedback:
                composeEmail();
                return true;
            case lang:
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
            Refresh(); //restarts activity with new changes
    }

    private void Refresh() {
        Intent refresh = new Intent(ContactActivity.this, ContactActivity.class);
        startActivity(refresh);
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);
        //LocaleUtils.updateConfig(this, newConfig);

    }

}
