package com.example.jmp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import static com.example.jmp.helper.DataHelper.TABLENAME;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.jmp.helper.DataHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddData extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude;

    Uri uri;
    DataHelper helper;
    SQLiteDatabase db;
    EditText name, tlpn, alamat;
    RadioGroup jk;
    Button location, submit, back, edit;
    private ImageView avatar;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        helper = new DataHelper(this);

        name = (EditText) findViewById(R.id.edtname);
        tlpn = (EditText) findViewById(R.id.edttlpn);
        jk = (RadioGroup) findViewById(R.id.radioJk);
        alamat = (EditText) findViewById(R.id.edtalamat);
        avatar = (ImageView) findViewById(R.id.edtimage);
        location = (Button) findViewById(R.id.btn_location);
        submit = (Button) findViewById(R.id.btn_submit);
        back = (Button) findViewById(R.id.btn_back);
        edit = (Button) findViewById(R.id.btn_edit);

        insertData();
        editData();

        avatar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setAction(Intent.ACTION_GET_CONTENT);
                in.setType("image/*");
                openGallery.launch(in);
            }
        });

        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if
                (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    getLocation();
                }
            }
        });
    }

    //edit data
    private void editData() {

        int gender = jk.getCheckedRadioButtonId();
        RadioButton jenisKelamin = (RadioButton) findViewById(gender);

        if (getIntent().getBundleExtra("userdata") != null) {
            Bundle bundle = getIntent().getBundleExtra("userdata");
            id = bundle.getInt("id");
            name.setText(bundle.getString("name"));
            tlpn.setText(bundle.getString("telepon"));
            alamat.setText(bundle.getString("alamat"));

            //set image from database mean old image that you want ot update
            byte[] bytes = bundle.getByteArray("avatar");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            avatar.setImageBitmap(bitmap);
            edit.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        if (ActivityCompat.checkSelfPermission(AddData.this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddData.this, ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{
                    ACCESS_FINE_LOCATION
            }, REQUEST_LOCATION);
        }
        else {
            Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (LocationGps != null) {
                double lat = LocationGps.getLatitude();
                double longi = LocationGps.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                try {
                    List<Address> addresses = geocoder.getFromLocation(lat, longi, 1);
                    String AddressLine = addresses.get(0).getAddressLine(0);
                    alamat.setText(AddressLine);
                    alamat.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // insert data into SQLite database
    private void insertData() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int gender = jk.getCheckedRadioButtonId();
                RadioButton jenisKelamin = (RadioButton) findViewById(gender);

                ContentValues cv = new ContentValues();
                cv.put("name", name.getText().toString());
                cv.put("telepon", tlpn.getText().toString());
                cv.put("jk", String.valueOf(jenisKelamin.getText().toString()));
                cv.put("alamat", alamat.getText().toString());
                cv.put("avatar", imageViewToBy(avatar));

                db = helper.getWritableDatabase();
                Long rec = db.insert("biodata", null, cv);
                if (rec != null) {
                    Toast.makeText(AddData.this, "data inserted", Toast.LENGTH_SHORT).show();
                    name.setText("");
                    tlpn.setText("");
                    alamat.setText("");
                    avatar.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Toast.makeText(AddData.this, "something wrong", Toast.LENGTH_SHORT).show();
                }

                Intent in = new Intent(AddData.this, ListData.class);
                startActivity(in);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddData.this, ListData.class));
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put("name", name.getText().toString());
                cv.put("telepon", tlpn.getText().toString());
                cv.put("alamat", alamat.getText().toString());
                cv.put("avatar", imageViewToBy(avatar));

                db = helper.getWritableDatabase();
                long rededit = db.update(TABLENAME, cv, "id=" + id, null);
                if (rededit != -1) {
                    Toast.makeText(AddData.this, "update successfully", Toast.LENGTH_SHORT).show();
                    submit.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.GONE);
                    name.setText("");
                    tlpn.setText("");
                    alamat.setText("");
                } else {
                    Toast.makeText(AddData.this, "Data does not update", Toast.LENGTH_SHORT).show();
                }

                Intent in = new Intent(AddData.this, ListData.class);
                startActivity(in);
            }
        });
    }

    //convert image to byte array
    public static byte[] imageViewToBy(ImageView avatar) {
        Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }

    //open internal storage
    ActivityResultLauncher<Intent> openGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            // do your operation from here....
            if (data != null && data.getData() != null) {
                uri = data.getData();
                Bitmap selectedImageBitmap = null;

                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                avatar.setImageBitmap(selectedImageBitmap);
            }
        }
    });
}