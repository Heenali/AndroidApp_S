package com.trukker.trukkershipperuae.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.UserFunctions;

public class PlaceAutocompleteActivity extends AppCompatActivity {
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    String click;
    UserFunctions UF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_autocomplete);

        Intent i = getIntent();
        click = i.getStringExtra("click");
        UF = new UserFunctions(PlaceAutocompleteActivity.this);

        try {
            if (click.equalsIgnoreCase("1")) {
                Constants.LatLong_source = null;
                Constants.source_add = "";
            } else if (click.equalsIgnoreCase("2")) {
                Constants.LatLong_destination = null;
                Constants.destination_add = "";
            }
        } catch (Exception e) {

        }
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);
       /* autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("name", "Place: " + place.getName());

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });*/
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("name", "Place: " + place.getName());
                Log.i("address", "address: " + place.getAddress());
                Log.i("id", "id: " + place.getId());
                Log.i("local", "Local: " + place.getLocale());
                Log.i("lat-long", "Lat-long: " + place.getLatLng());
                try {
                    if (click.equalsIgnoreCase("1")) {
                        Constants.LatLong_source = place.getLatLng();
                        Constants.source_add = (String) place.getAddress();
                    } else if (click.equalsIgnoreCase("2")) {
                        Constants.LatLong_destination = place.getLatLng();
                        Constants.destination_add = (String) place.getAddress();
                    }
                } catch (Exception e) {
                    UF.msg("location didn't get");
                }
                finish();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("TAG", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
