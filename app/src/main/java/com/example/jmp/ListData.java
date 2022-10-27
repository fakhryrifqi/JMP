package com.example.jmp;

import static com.example.jmp.helper.DataHelper.TABLENAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jmp.helper.DataHelper;
import com.example.jmp.model.DataModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;

public class ListData extends AppCompatActivity {

    FloatingActionButton fab;
    DataHelper helper;
    SQLiteDatabase db;
    ListView lv;
    ArrayList<DataModel> modelArrayList = new ArrayList<>();
    Custom adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        helper = new DataHelper(this);
        lv = (ListView) findViewById(R.id.listBio);
        fab = (FloatingActionButton) findViewById(R.id.btnAdd);
        displayData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add = new Intent(ListData.this, AddData.class);
                startActivity(add);
            }
        });
    }

    //displaydata
    private void displayData() {
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select *from " + TABLENAME + "", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String tlpn = cursor.getString(2);
            String jk = cursor.getString(3);
            String alamat = cursor.getString(4);
            byte[] image = cursor.getBlob(5);

            modelArrayList.add(new DataModel(id, name, tlpn, jk, alamat, image));

        }
        //custom adapter
        adapter = new Custom(this, R.layout.custom_card, modelArrayList);
        lv.setAdapter(adapter);
    }


    //inner class custom adapter
    private class Custom extends BaseAdapter {
        private Context context;
        private int layout;
        private ArrayList<DataModel> modelArrayList = new ArrayList<>();

        //constructor
        public Custom(Context context, int layout, ArrayList<DataModel> modelArrayList) {
            this.context = context;
            this.layout = layout;
            this.modelArrayList = modelArrayList;
        }

        private class ViewHolder {
            TextView txtname, txttlpn, txtjk, txtalamat;
            Button edit, delete;
            ImageView imageView;
            CardView cardView;
        }

        @Override
        public int getCount() {
            return modelArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return modelArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //getting view
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            //find id
            holder.txtname = convertView.findViewById(R.id.txtname);
            holder.txttlpn = convertView.findViewById(R.id.txttlpn);
            holder.txtjk = convertView.findViewById(R.id.txtjk);
            holder.txtalamat = convertView.findViewById(R.id.txtalamat);
            holder.imageView = convertView.findViewById(R.id.imageview);
            holder.edit = convertView.findViewById(R.id.btn_edit);
            holder.delete = convertView.findViewById(R.id.btn_delete);
            holder.cardView = convertView.findViewById(R.id.cardview);
            convertView.setTag(holder);

            //random bg color
            Random random = new Random();
            holder.cardView.setCardBackgroundColor(Color.argb(255, random.nextInt(256), random.nextInt(255), random.nextInt(255)));

            // get position of each data
            DataModel model = modelArrayList.get(position);

            //set text in textview
            holder.txtname.setText(model.getName());
            holder.txttlpn.setText(model.getTelepon());
            holder.txtjk.setText(model.getJk());
            holder.txtalamat.setText(model.getAlamat());

            //set image in imageview
            byte[] image = model.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.imageView.setImageBitmap(bitmap);

            // edit buttons to edit data
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle data = new Bundle();
                    data.putInt("id", model.getId());
                    data.putString("name", model.getName());
                    data.putString("telepon", model.getTelepon());
                    data.putString("jk", model.getJk());
                    data.putString("alamat", model.getAlamat());
                    data.putByteArray("avatar", model.getImage());

                    //send data in main activity
                    Intent intent = new Intent(ListData.this, AddData.class);
                    intent.putExtra("userdata", data);
                    startActivity(intent);
                }
            });

            //delete data
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db = helper.getReadableDatabase();
                    long recdelete = db.delete(TABLENAME, "id=" + model.getId(), null);
                    if (recdelete != -1) {
                        Snackbar.make(v, "record deleted=" + model.getId() + "\n" + model.getName(), Snackbar.LENGTH_LONG).show();
                        //for remove from current position
                        modelArrayList.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });

            return convertView;
        }
    }
}