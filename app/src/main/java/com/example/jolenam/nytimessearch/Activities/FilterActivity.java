package com.example.jolenam.nytimessearch.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.example.jolenam.nytimessearch.R;
import com.example.jolenam.nytimessearch.SearchFilters;

public class FilterActivity extends AppCompatActivity {

    SearchFilters searchFilters;

    Spinner spinnerDay;
    Spinner spinnerMonth;
    Spinner spinnerYear;
    Spinner spinnerSort;
    boolean chkArts = false;
    boolean chkFashion;
    boolean chkSports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        spinnerSort = (Spinner) findViewById(R.id.spinSort);
        spinnerMonth = (Spinner) findViewById(R.id.spinMonth);
        spinnerDay = (Spinner) findViewById(R.id.spinDay);
        spinnerYear = (Spinner) findViewById(R.id.spinYear);

    }




    public void onFilterSubmit(View view) {

        String sortValue = spinnerSort.getSelectedItem().toString();
        //String etDate = etBeginDate.getText().toString();
        String month = spinnerMonth.getSelectedItem().toString();
        String day = spinnerDay.getSelectedItem().toString();
        String year = spinnerYear.getSelectedItem().toString();

        //MAKE A CATCH AND TRY TO MAKE SURE THE DATE FORMAT IS OK

        //pass info in as Filter object



        searchFilters = new SearchFilters(sortValue, chkArts, chkFashion, chkSports, day, month, year);

        //Intent data = new Intent(this,SearchActivity.class);
        Intent data = new Intent(this, SearchActivity.class);
        data.putExtra("filter", searchFilters);
        setResult(RESULT_OK, data);
        finish();

    }



    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        //check which checkbox was clicked
        switch(view.getId()) {

            case R.id.chkArts:
                if (checked) {
                    chkArts = true;
                }
                else{
                    chkArts = false;
                }
                break;

            case R.id.chkFashion:
                if (checked) {
                    chkFashion = true;
                }
                else{
                    chkFashion = false;
                }
                break;

            case R.id.chkSports:
                if (checked) {
                    chkSports = true;
                }
                else{
                    chkSports = false;
                }
                break;
        }
    }
}
