package com.example.jmp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jmp.helper.DataHelper;

public class DetailData extends AppCompatActivity {

    protected Cursor cursor;
    DataHelper dbHelper;
    Button btBack;
    TextView txtNomor, txtNama, txtTelepon, txtJk, txtAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data);

        dbHelper = new DataHelper(this);
        txtNomor = (TextView) findViewById(R.id.tvNomor);
        txtNama = (TextView) findViewById(R.id.tvNama);
        txtTelepon = (TextView) findViewById(R.id.tvTelepon);
        txtJk = (TextView) findViewById(R.id.tvJk);
        txtAlamat = (TextView) findViewById(R.id.tvAlamat);
        btBack = (Button) findViewById(R.id.btnBack);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM biodata WHERE nama = '" + getIntent().getStringExtra("nama") + "'" ,null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            txtNomor.setText(cursor.getString(0).toString());
            txtNama.setText(cursor.getString(1).toString());
            txtTelepon.setText(cursor.getString(2).toString());
            txtJk.setText(cursor.getString(3).toString());
            txtAlamat.setText(cursor.getString(4).toString());
        }

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }
}