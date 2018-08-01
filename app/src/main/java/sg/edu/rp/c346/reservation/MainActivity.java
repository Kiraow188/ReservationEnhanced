package sg.edu.rp.c346.reservation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Button btnReset, btnReserve;
    CheckBox cbSmoke;
    EditText etName, etPhone, etSize, etDay, etTime;
    int theHour, theMin, theDate, theMonth, theYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReserve = findViewById(R.id.buttonReserve);
        btnReset = findViewById(R.id.buttonReset);
        cbSmoke = findViewById(R.id.checkBox);
        etDay = findViewById(R.id.editTextDay);
        etTime = findViewById(R.id.editTextTime);
        etName = findViewById(R.id.editTextName);
        etPhone = findViewById(R.id.editTextPhone);
        etSize = findViewById(R.id.editTextGroup);
        Calendar now = Calendar.getInstance();
        theDate = now.get(Calendar.DAY_OF_MONTH);
        theHour = now.get(Calendar.HOUR);
        theMin = now.get(Calendar.MINUTE);
        theMonth = now.get(Calendar.MONTH);
        theYear = now.get(Calendar.YEAR);


        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        etTime.setText("Time: " + hourOfDay + ":" + minute);
                        theHour = hourOfDay;
                        theMin = minute;
                    }
                };
                TimePickerDialog myTimeDialog = new TimePickerDialog(MainActivity.this, myTimeListener, theHour, theMin, true);
                myTimeDialog.show();
            }
        });

        etDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etDay.setText("Date: " + dayOfMonth + "/" + (month + 1) + "/" + year);
                        theDate = dayOfMonth;
                        theMonth = month;
                        theYear = year;
                    }
                };

                //create date picker dialog
                DatePickerDialog myDateDialog = new DatePickerDialog(MainActivity.this, myDateListener, theYear, theMonth, theDate);
                myDateDialog.show();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDay.setText(null);
                etTime.setText(null);
                etTime.setHint(R.string.timeSelect);
                etDay.setHint(R.string.daySelect);
                etName.setText(null);
                etPhone.setText(null);
                etSize.setText(null);
                cbSmoke.setChecked(false);
                Calendar now = Calendar.getInstance();
                theDate = now.get(Calendar.DAY_OF_MONTH);
                theHour = now.get(Calendar.HOUR);
                theMin = now.get(Calendar.MINUTE);
                theMonth = now.get(Calendar.MONTH);
                theYear = now.get(Calendar.YEAR);
            }
        });

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create dialog builder
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivity.this);

                //Set dialog details
                myBuilder.setTitle("Confirm Your Order");
                String name = etName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String pax = etSize.getText().toString().trim();
                String date = etDay.getText().toString().trim();
                String time = etTime.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    Toast.makeText(MainActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(phone)){
                    Toast.makeText(MainActivity.this, "Please enter your phone no.", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(pax)){
                    Toast.makeText(MainActivity.this, "Please enter size of group", Toast.LENGTH_SHORT).show();
                }
                else{
                    String isSmoke = " ";
                    if (cbSmoke.isChecked()){
                        isSmoke="Yes";
                    }
                    else{
                        isSmoke="No";
                    }
                    myBuilder.setMessage("New Reservation \nName: " + name + "\nSmoking: " + isSmoke + "\nSize: " + pax + "\n" + date + "\n" + time);
                    myBuilder.setCancelable(false);
                    myBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast toast = Toast.makeText(MainActivity.this, "Your reservations is confirmed.", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                    myBuilder.setNegativeButton("Cancel", null);
                    AlertDialog myDialog = myBuilder.create();
                    myDialog.show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String pax = etSize.getText().toString().trim();
        String date = etDay.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String isSmoke = " ";
        if (cbSmoke.isChecked()){
            isSmoke="Yes";
        }
        else{
            isSmoke="No";
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        //Step 1c: Obtain an instance of the SharedPreferences Editor for update later
        SharedPreferences.Editor prefEdit = prefs.edit();
        //Step 1d: Add the key value pair
        prefEdit.putString("name", name);
        prefEdit.putString("phone", phone);
        prefEdit.putString("pax", pax);
        prefEdit.putString("date", date);
        prefEdit.putString("time", time);
        prefEdit.putString("isSmoke", isSmoke);
        prefEdit.putInt("year", theYear);
        prefEdit.putInt("month", theMonth);
        prefEdit.putInt("day", theDate);
        prefEdit.putInt("hour", theHour);
        prefEdit.putInt("min", theMin);
        //Step 1e: Call commit() method to save the changes into the SharedPreferences
        prefEdit.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Step 2a: Obtain an instance of the SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Step 2b: Retrieve the saved data with the key "greeting" from the SharedPreference object
        String name = prefs.getString("name","");
        String phone = prefs.getString("phone","");
        String pax = prefs.getString("pax","");
        String date = prefs.getString("date","");
        String time = prefs.getString("time","");
        String isSmoke = prefs.getString("smoke", "no");
        theYear = prefs.getInt("year", Calendar.getInstance().get(Calendar.YEAR));
        theMonth = prefs.getInt("month", Calendar.getInstance().get(Calendar.MONTH));
        theDate = prefs.getInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        theMin = prefs.getInt("min", Calendar.getInstance().get(Calendar.MINUTE));
        theHour = prefs.getInt("hour", Calendar.getInstance().get(Calendar.HOUR));
        etDay.setText(date);
        etTime.setText(time);
        etPhone.setText(phone);
        etSize.setText(pax);
        etName.setText(name);
        if (isSmoke.equalsIgnoreCase("no")){
            cbSmoke.setChecked(false);
        }
        else{
            cbSmoke.setChecked(true);
        }
    }
}
