package com.example.jmp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText txUsername, txPassword;
    Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txUsername = (EditText) findViewById(R.id.etUsername);
        txPassword = (EditText) findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btnLogin);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txUsername.getText().toString().equals("admin") && txPassword.getText().toString().equals("123")){
                    Intent in = new Intent(MainActivity.this, ListData.class);
                    startActivity(in);
                } else {
                    Toast.makeText(MainActivity.this, "Harap input dengan benar...!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}