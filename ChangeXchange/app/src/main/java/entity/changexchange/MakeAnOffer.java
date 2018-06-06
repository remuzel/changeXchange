package entity.changexchange;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import entity.changexchange.utils.Airport;
import entity.changexchange.utils.Currency;
import entity.changexchange.utils.RequestDatabase;


public class MakeAnOffer extends AppCompatActivity {

    private static final float NEG_THRESHOLD = (float) 0.001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_an_offer);

        // Create adapter for list of currencies and link to dropdown objects.
        ArrayAdapter<Currency> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, Currency.values()
        );
        this.<Spinner>findViewById(R.id.new_offer_currency_from).setAdapter(adapter);
        this.<Spinner>findViewById(R.id.new_offer_currency_to).setAdapter(adapter);

        // Create adapter for list of airports.
        ArrayAdapter<Airport> adapter1 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, Airport.values()
        );
        this.<Spinner>findViewById(R.id.new_offer_location).setAdapter(adapter1);

        // Submitting an offer triggers the migration of all the data to the database.
        this.<Button>findViewById(R.id.new_offer_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount = ((EditText) findViewById(R.id.new_offer_price)).getText().toString();

                //Erroneous amount entered. Deny clicking effect.
                if (amount.isEmpty() || Float.parseFloat(amount) <= NEG_THRESHOLD) return;

                String name = "John"; // TODO: Get poster name from login.

                String from = ((Spinner) findViewById(R.id.new_offer_currency_from))
                        .getSelectedItem().toString();
                String to = ((Spinner) findViewById(R.id.new_offer_currency_to))
                        .getSelectedItem().toString();
                String location = ((Spinner) findViewById(R.id.new_offer_location))
                        .getSelectedItem().toString();
                String note = ((EditText) findViewById(R.id.new_offer_note))
                        .getText().toString();

                // If note hasn't been filled, replace with default value.
                if (note.isEmpty()) note = "//" + name + " did not add a note.";

                // Erroneous amount entered. Deny clicking effect.
                if (from.isEmpty() || to.isEmpty() || location.isEmpty()) return;

                // TODO: Add note to database.

                new RequestDatabase().execute(
                        "INSERT INTO offers VALUES ('John', '"
                                + from + "', '"
                                + to + "', '"
                                + amount + "', '"
                                + location + "', '"
                                + note + "');"
                );

                startActivity(new Intent(MakeAnOffer.this, MainActivity.class));
            }
        });
    }
}
