package com.example.mid_exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Search extends AppCompatActivity {

    public String activities_se = "";
    BottomNavigationView bottomNavigationView;
    TextView searchbypname;
    private CheckBox gardenchk_se, nightchk_se, waterchk_se, walkingchk_se, mountainchk_se, fishingchk_se;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        Button searchbyactivities = (Button) findViewById(R.id.btn_searchbyactiv);
        gardenchk_se = findViewById(R.id.chkgarden_se);
        nightchk_se = findViewById(R.id.chknight_se);
        waterchk_se = findViewById(R.id.chkwater_se);
        walkingchk_se = findViewById(R.id.chkwalking_se);
        mountainchk_se = findViewById(R.id.chkmountain_se);
        fishingchk_se = findViewById(R.id.chkfishing_se);

        searchbypname = (TextView) findViewById(R.id.textviewgotosearch);

        searchbypname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(Search.this, SearchbyplacenameActivity.class);
                startActivity(j);

            }
        });


        searchbyactivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gardenchk_se.isChecked()) {
                    activities_se += " " + gardenchk_se.getText().toString();
                }
                if (nightchk_se.isChecked()) {
                    activities_se += " " + nightchk_se.getText().toString();
                }
                if (waterchk_se.isChecked()) {
                    activities_se += " " + waterchk_se.getText().toString();
                }
                if (walkingchk_se.isChecked()) {
                    activities_se += " " + walkingchk_se.getText().toString();
                }
                if (mountainchk_se.isChecked()) {
                    activities_se += " " + mountainchk_se.getText().toString();
                }
                if (fishingchk_se.isChecked()) {
                    activities_se += " " + fishingchk_se.getText().toString();
                }
                Intent i = new Intent(Search.this, ActivitiesSearchActivity.class);
                i.putExtra("selectedactvi", activities_se);
                startActivity(i);

            }
        });


        //bottom navigation bar code
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.news);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.news:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), AllPlacesActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext(), FavoritePlaces.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });


    }
}