package com.example.task21p;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerCategory, spinnerFrom, spinnerTo;
    private EditText inputValue;
    private TextView resultText;
    private Button convertButton;

    private final String[] categories = {"Length", "Weight", "Temperature"};
    private final String[] lengthUnits = {"Inch", "Foot", "Yard", "Mile"};
    private final String[] weightUnits = {"Pound", "Ounce", "Ton"};
    private final String[] tempUnits = {"Celsius", "Fahrenheit", "Kelvin"};

    private final HashMap<String, Double> conversionFactors = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        inputValue = findViewById(R.id.inputValue);
        resultText = findViewById(R.id.resultText);
        convertButton = findViewById(R.id.convertButton);

        // Populate Category Spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(categoryAdapter);

        // Set up event listener to change unit spinners dynamically
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateUnitSpinners(categories[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Initialize conversion factors
        initializeConversionFactors();

        // Button Click Listener
        convertButton.setOnClickListener(v -> convertUnits());
    }

    private void updateUnitSpinners(String category) {
        String[] units;
        if (category.equals("Length")) {
            units = lengthUnits;
        } else if (category.equals("Weight")) {
            units = weightUnits;
        } else {
            units = tempUnits;
        }

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units);
        spinnerFrom.setAdapter(unitAdapter);
        spinnerTo.setAdapter(unitAdapter);
    }

    private void initializeConversionFactors() {
        // Length Conversions (Base: CM)
        conversionFactors.put("Inch", 2.54);
        conversionFactors.put("Foot", 30.48);
        conversionFactors.put("Yard", 91.44);
        conversionFactors.put("Mile", 160934.0);

        // Weight Conversions (Base: KG)
        conversionFactors.put("Pound", 0.453592);
        conversionFactors.put("Ounce", 0.0283495);
        conversionFactors.put("Ton", 907.185);
    }

    private void convertUnits() {
        String category = spinnerCategory.getSelectedItem().toString();
        String fromUnit = spinnerFrom.getSelectedItem().toString();
        String toUnit = spinnerTo.getSelectedItem().toString();
        String input = inputValue.getText().toString();

        if (input.isEmpty()) {
            resultText.setText("Please enter a value.");
            return;
        }

        double inputValue = Double.parseDouble(input);
        double result = 0.0;

        if (fromUnit.equals(toUnit)) {
            resultText.setText("Same unit selected!");
            return;
        }

        if (category.equals("Length") || category.equals("Weight")) {
            double fromFactor = conversionFactors.get(fromUnit);
            double toFactor = conversionFactors.get(toUnit);
            result = (inputValue * fromFactor) / toFactor;
        } else {
            result = convertTemperature(fromUnit, toUnit, inputValue);
        }

        resultText.setText("Converted Value: " + result);
    }

    private double convertTemperature(String from, String to, double value) {
        if (from.equals("Celsius") && to.equals("Fahrenheit")) return (value * 1.8) + 32;
        if (from.equals("Fahrenheit") && to.equals("Celsius")) return (value - 32) / 1.8;
        if (from.equals("Celsius") && to.equals("Kelvin")) return value + 273.15;
        if (from.equals("Kelvin") && to.equals("Celsius")) return value - 273.15;
        return 0.0;
    }
}