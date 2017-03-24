package msc.project.sticktoyourbudget;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

public class ExpensesFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private Expense expense;
    private GoogleApiClient apiClient;
    //private LocationRequest mLocationRequest;
    private Location lastLocation;

    private double longitude = 0.0;
    private double latitude = 0.0;

    public void getCoordinates() {
        //Random coordinates generator if google api doesn't work *** just for demonstration purposes***
        longitude = (Math.random() - 0.5) * 360;
        latitude = (Math.random() - 0.5) * 180;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_expenses, container, false);

        apiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        apiClient.connect();
        Button Coordsbutton = (Button) v.findViewById(R.id.Coordsbutton);
        Coordsbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //createLocationRequest();
                if(apiClient.isConnected()){
                    if (lastLocation != null) {
                        longitude = lastLocation.getLongitude();
                        latitude = lastLocation.getLatitude();
                        Log.d("ff", toString().valueOf(latitude));
                        Log.d("ff", toString().valueOf(longitude));
                    } else {
                        Toast.makeText(getContext(), "No location detected, will get random coords", Toast.LENGTH_LONG).show();
                        //generate random coords since Google API doesn't work
                        getCoordinates();
                }
                Log.d("ExpensesFrag", "Lat: " + latitude + ", Lon: " + longitude);
                }
            }
        });
        Button button = (Button) v.findViewById(R.id.Submbutton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TextView amount = (TextView) v.findViewById(R.id.numAmount);
                TextView descr = (TextView) v.findViewById(R.id.textDescription);
                TextView loc = (TextView) v.findViewById(R.id.textLocation);
                Spinner spin = (Spinner) v.findViewById(R.id.Catspinner);
                Date date = new Date();
                Double price = 0.0;
                try {
                    price = Double.valueOf(amount.getText().toString());
                } catch (NumberFormatException nfe) {
                    Toast.makeText(getContext(), "Εσφαλμένο ποσό", Toast.LENGTH_SHORT).show();
                    return;
                }
                String description = descr.getText().toString();
                // Log.d("ExpensesFrag", "Η τιμή είναι "+description);
                String location = loc.getText().toString();
                String category = spin.getSelectedItem().toString();
                //Αν ο χρήστης δεν έχει πατήσει το κουμπί, τον ενημερώνουμε να πατήσει το κουμπί
                // και αν παλι δεν το κανει, παίρνουμε τις συντεταγμένες μέσω κώδικα
                //σαν μέτρο ελέγχου για να μην μείνει κενό
                if (latitude == 0.0 && longitude == 0.0) {
                    Toast.makeText(getContext(), "Press COORDINATES button, otheswise the system will get them automatically during Add", Toast.LENGTH_LONG).show();
                    if(apiClient.isConnected()){
                        if (lastLocation != null) {
                            longitude = lastLocation.getLongitude();
                            latitude = lastLocation.getLatitude();
                            Log.d("ff", toString().valueOf(latitude));
                            Log.d("ff", toString().valueOf(longitude));
                        } else {
                            //Toast.makeText(getContext(), "No location detected, will get random coords", Toast.LENGTH_LONG).show();
                            //τυχαίες τιμές για συντεταγμένες αφου δεν δούλεψε το google api
                            getCoordinates();
                        }
                        Log.d("ExpensesFrag", "Lat: " + latitude + ", Lon: " + longitude);
                    }
                    return;
                }
                //ελεγχος για καταχωρηση του εξοδου
                if (!expense.addExpense(price, category, description, date, latitude, longitude, location, getContext())) {
                    Toast.makeText(getContext(), "Problem occured with Add Expense", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Expense has been added successfully", Toast.LENGTH_SHORT).show();
                }
                //clear text fields
                amount.setText("");
                descr.setText("");
                loc.setText("");
                spin.setSelection(0);
                longitude = 0.0;
                latitude = 0.0;
                //Log.d("ExpensesFrag", "You pressed the button!");
            }
        });
        return v;
    }

    public void updateSpinner() {
        final View v = getView();
        Category cat = new Category(getContext());
        ArrayList<String> categories = cat.getCategoryName();
        Spinner catSpinner = (Spinner) v.findViewById(R.id.Catspinner);
        String[] cats = categories.toArray(new String[0]);
       /* for (String temp : cats) {
            Log.d("Categories", temp);
        }*/
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, cats);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSpinner.setAdapter(spinnerArrayAdapter);
        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        Toast.makeText(getContext(), "Inflated!", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onStart() { //για να φορτώνει κάθε φορά τις τιμές ο spinner που έχουν προστεθεί
        Log.d("ExpensesFrag", "OnStart");
        super.onStart();
        //για ανανεωση του spinner οταν παιρναμε νεα κατηγορια
        updateSpinner();
        apiClient.connect();
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d("ExpensesFrag", "OnStop");
        if (apiClient.isConnected()) {
            apiClient.disconnect();
        }
    }
    @Override
    public void onResume() {
        Log.d("ExpensesFrag", "OnResume");
        super.onResume();
        //για ανανεωση του spinner οταν παιρναμε νεα κατηγορια
        updateSpinner();
    }
    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        /*if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        if (mLocationPermissionGranted) {*/
           /* lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
            if (lastLocation != null) {
                longitude = lastLocation.getLongitude();
                latitude = lastLocation.getLatitude();
                Log.d("ff", toString().valueOf(latitude));
                Log.d("ff", toString().valueOf(longitude));
            } else {
                Toast.makeText(getContext(), "No location detected", Toast.LENGTH_LONG).show();
            }
        //}*/
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        Log.d("location on connect", toString().valueOf(lastLocation));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("api", "Η συνδεση διακοπηκε");
        apiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("api", "Η συνδεση απετυχε:" + connectionResult.getErrorCode());
    }
    /*@Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // Αν το αίτημα ακυρώθηκε, το array grantResults
                // είναι κενό.
                if (grantResults.length > 0
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }*/
}


