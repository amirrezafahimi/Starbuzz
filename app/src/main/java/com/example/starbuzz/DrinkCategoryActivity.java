package com.example.starbuzz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DrinkCategoryActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_category);


        ListView listDrinks = findViewById(R.id.list_drinks);
        SQLiteOpenHelper startbuzzDatabaseHelper = new StartbuzzDatabaseHelper(this);
        try {
            db = startbuzzDatabaseHelper.getReadableDatabase();
            cursor = db.query("DRINK",
                    new String[]{"_id", "NAME"},
                    null, null, null, null, null);
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1},
                    0);
            listDrinks.setAdapter(listAdapter);
        } catch (SQLiteException e) {
            Toast.makeText(this, "Database Unavailable", Toast.LENGTH_SHORT).show();
        }

        // create a listener to listen for clicks in the list view
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View itemView,
                                            int position,
                                            long id) {

                        Intent intent = new Intent(DrinkCategoryActivity.this,
                                DrinkActivity.class);
                        intent.putExtra(DrinkActivity.EXTRA_DRINKID, (int) id);
                        startActivity(intent);
                    }
                };
        // Assign the listener to the list view
        listDrinks.setOnItemClickListener(itemClickListener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
