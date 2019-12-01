package com.example.starbuzz;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

public class DrinkActivity extends AppCompatActivity {

    public static final String EXTRA_DRINKID = "drinkid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        int drinkId = getIntent().getExtras().getInt(EXTRA_DRINKID);

        // creating a cursor
        SQLiteOpenHelper startbuzzDatabaseHelper = new StartbuzzDatabaseHelper(this);
        try {
            SQLiteDatabase db = startbuzzDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("DRINK",
                    new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id = ?",
                    new String[]{Integer.toString(drinkId)},
                    null, null, null);

            // Move to the first record in the Cursor
            if (cursor.moveToFirst()) {
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);

                TextView name = findViewById(R.id.name);
                name.setText(nameText);

                TextView description = findViewById(R.id.description);
                description.setText(descriptionText);

                ImageView photo = findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);

                CheckBox favorite = findViewById(R.id.favourite);
                favorite.setChecked(isFavorite);
            }
            cursor.close();
            db.close();

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public void onFavouriteClicked(View view) {
        int drinkId = (Integer) getIntent().getExtras().get(EXTRA_DRINKID);

        new UpdateDrinkTask().execute(drinkId);
    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateDrinkTask extends AsyncTask<Integer, Void, Boolean> {

        private ContentValues drinkValues;

        @Override
        protected void onPreExecute() {
            CheckBox favorite = findViewById(R.id.favourite);
            drinkValues = new ContentValues();
            drinkValues.put("FAVORITE", favorite.isChecked());
        }

        @Override
        protected Boolean doInBackground(@NotNull Integer... drinks) {
            int drinkId = drinks[0];
            SQLiteOpenHelper startbuzzDatabaseHelper =
                    new StartbuzzDatabaseHelper(DrinkActivity.this);
            try {
                SQLiteDatabase db = startbuzzDatabaseHelper.getWritableDatabase();
                db.update("DRINK", drinkValues,
                        "_id = ?", new String[]{Integer.toString(drinkId)});
                db.close();
                return true;
            } catch (SQLiteException ex) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast.makeText(DrinkActivity.this,
                        "Database unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
