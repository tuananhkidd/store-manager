package com.kidd.store_manager.view.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.kidd.store_manager.R;
import com.kidd.store_manager.SQLiteHelper.DBManager;
import com.kidd.store_manager.common.Constants;
import com.kidd.store_manager.models.User;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edt_username;
    EditText edt_password;
    EditText edt_confirm_password;
    Button btn_register;
    TextView txt_login;
    User u;

    DBManager db;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initWidget();
    }

    void initWidget() {
        edt_password = findViewById(R.id.edt_pass);
        edt_username = findViewById(R.id.edt_user_name);
        edt_confirm_password = findViewById(R.id.edt_confirm_pass);
        btn_register = findViewById(R.id.btn_register);
        txt_login = findViewById(R.id.txt_login);
        progressBar = findViewById(R.id.progress);

        btn_register.setOnClickListener(this);
        txt_login.setOnClickListener(this);

        db = new DBManager(this);
        u = new User();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register: {
                if(!setupError()){
                    return;
                }
                if(db.check_user(edt_username.getText().toString())){
                    db.register(u);
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.putExtra(Constants.USER,u.getUsername());
                            setResult(Constants.RESULT_CODE_SIGNUP,intent);
                            finish();
                        }
                    }, 2000);
                }else {
                    Toast.makeText(this, "Account exist !!", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            }
            case R.id.txt_login: {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            }
        }

    }

    boolean setupError() {
        if (edt_username.getText().toString().isEmpty()) {
            edt_username.setError("Must not empty!");
            return false;
        }
        if (edt_password.getText().toString().isEmpty()) {
            edt_username.setError("Must not empty !");
            return false;
        }

        if (!edt_confirm_password.getText().toString().equals(edt_password.getText().toString())) {
            edt_confirm_password.setError("Password not match!");
            return false;
        }

        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        u = new User(id,edt_username.getText().toString(), edt_password.getText().toString());

        Log.i("user123",id);
        return true;
    }
}
