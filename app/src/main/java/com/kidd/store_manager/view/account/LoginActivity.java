package com.kidd.store_manager.view.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kidd.store_manager.MainActivity;
import com.kidd.store_manager.R;
import com.kidd.store_manager.SQLiteHelper.DBManager;
import com.kidd.store_manager.common.Constants;
import com.kidd.store_manager.common.Utils;
import com.kidd.store_manager.models.User;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edt_username;
    EditText edt_password;
    Button btn_login;
    TextView txt_signup;
    DBManager db;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initWidget();
        getDataFromIntent();
    }

    void initWidget() {
        edt_password = findViewById(R.id.edt_pass);
        edt_username = findViewById(R.id.edt_user_name);
        btn_login = findViewById(R.id.btn_login);
        txt_signup = findViewById(R.id.txt_signup);
        progressBar = findViewById(R.id.progress);

        txt_signup.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        db = new DBManager(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login: {
                setupError();
                if (db.login(edt_username.getText().toString(), edt_password.getText().toString())) {
                    Utils.setSharePreferenceValues(this, Constants.STATUS_LOGIN, Constants.LOGIN_TRUE);
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                    }, 2000);
                } else {
                    Toast.makeText(this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            }
            case R.id.txt_signup: {
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent,Constants.REQUEST_CODE_SIGNUP);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constants.REQUEST_CODE_SIGNUP:{
                if(resultCode==Constants.RESULT_CODE_SIGNUP){
                    edt_username.setText(data.getStringExtra(Constants.USER));
                }
                break;
            }
        }
    }

    void setupError() {
        if (edt_username.getText().toString().isEmpty()) {
            edt_username.setError("Wrong Info !");
            return;
        }
        if (edt_password.getText().toString().isEmpty()) {
            edt_username.setError("Wrong Info !");
            return;
        }
    }

    void getDataFromIntent() {
        if (getIntent().getExtras() != null) {
            User u = (User) getIntent().getSerializableExtra(Constants.USER);
            edt_username.setText(u.getUsername());
        }
    }
}
