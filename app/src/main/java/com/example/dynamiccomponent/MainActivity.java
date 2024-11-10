package com.example.dynamiccomponent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText etNumberOfFields;
    private Button btnCalculateSum;
    private Button btnClearFields;
    private LinearLayout containerLayout;
    private TextView tvResult;

    private ArrayList<EditText> credits = new ArrayList<>();
    private ArrayList<EditText> inputFields = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNumberOfFields = findViewById(R.id.et_number_of_fields);
        Button btnGenerateFields = findViewById(R.id.btn_generate_fields);
        btnCalculateSum = findViewById(R.id.btn_calculate_sum);
        btnClearFields = findViewById(R.id.btn_clear);
        containerLayout = findViewById(R.id.container_layout);
        tvResult = findViewById(R.id.tv_result);

        btnGenerateFields.setOnClickListener(view -> generateInputFields());

        btnCalculateSum.setOnClickListener(view -> calculateSum());

        btnClearFields.setOnClickListener(view -> clearFields());
    }

    private void clearFields(){
        inputFields.clear();
        credits.clear();
        containerLayout.removeAllViews();
        btnClearFields.setVisibility(View.INVISIBLE);
        btnCalculateSum.setVisibility(View.INVISIBLE);
        tvResult.setText(null);
    }

    private void generateInputFields() {
        // Clear previous fields and reset the input fields list
        containerLayout.removeAllViews();
        inputFields.clear();
        credits.clear();

        // Get the number of fields from user input
        String numFieldsStr = etNumberOfFields.getText().toString().trim();

        if (numFieldsStr.isEmpty()) {
            Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show();
            return;
        }

        int numFields = Integer.parseInt(numFieldsStr);

        for (int i = 0; i < numFields; i++) {
            // Create a horizontal layout for grade point and credit fields
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            rowParams.setMargins(0, 8, 0, 8);
            rowLayout.setLayoutParams(rowParams);

            // Create EditText for grade point
            EditText editText = new EditText(this);
            editText.setHint("Course " + (i + 1) + " grade point");
            editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            editText.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            // Create EditText for credit
            EditText editText2 = new EditText(this);
            editText2.setHint("Course " + (i + 1) + " credit");
            editText2.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            // Add EditTexts to the row layout
            rowLayout.addView(editText);
            rowLayout.addView(editText2);

            // Add the row layout to the container layout
            containerLayout.addView(rowLayout);

            // Add EditTexts to respective lists
            inputFields.add(editText);
            credits.add(editText2);
        }

        // Make the Calculate Sum button visible
        btnClearFields.setVisibility(View.VISIBLE);
        btnCalculateSum.setVisibility(View.VISIBLE);
    }

    private void calculateSum() {
        double sum = 0;
        double totalCredits = 0;

        for (int i = 0; i < inputFields.size(); i++) {
            EditText gradePointField = inputFields.get(i);
            EditText creditField = credits.get(i);
            String gradePointStr = gradePointField.getText().toString().trim();
            String creditStr = creditField.getText().toString().trim();

            if (!gradePointStr.isEmpty() && !creditStr.isEmpty()) {
                try {
                    double gradePoint = Integer.parseInt(gradePointStr);
                    double credit = Double.parseDouble(creditStr);
                    sum += gradePoint * credit;
                    totalCredits += credit;
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        if (totalCredits == 0) {
            Toast.makeText(this, "Total credits cannot be zero", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate and display the CGPA
        double cgpa = sum / totalCredits;
        tvResult.setText("CGPA: " + String.format("%.3f", cgpa));
    }
}
