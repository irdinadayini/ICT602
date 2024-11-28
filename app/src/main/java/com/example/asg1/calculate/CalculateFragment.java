package com.example.asg1.calculate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.asg1.R;

public class CalculateFragment extends Fragment {

    private CalculateViewModel calculateViewModel;

    private TextView tvResult, etRebate;
    private EditText etUnits;
    private SeekBar seekBarRebate;
    private Button btnCalculate, btnClear;
    ImageView ivInfo;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        calculateViewModel = new ViewModelProvider(this).get(CalculateViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calculate, container, false);

        // Initialize views
        etUnits = root.findViewById(R.id.etUnits);
        tvResult = root.findViewById(R.id.tvResult);
        etRebate = root.findViewById(R.id.etRebate);
        seekBarRebate = root.findViewById(R.id.seekBarRebate);
        btnCalculate = root.findViewById(R.id.btnCalculate);
        btnClear = root.findViewById(R.id.btnClear);

        // Set SeekBar listener
        seekBarRebate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                etRebate.setText("Rebate: " + progress + "%"); // Update rebate percentage text
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        ivInfo = root.findViewById(R.id.ivInfo);
        ivInfo.setOnClickListener(v -> {
            // Show an AlertDialog with instructions
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Instructions")
                    .setMessage("1. Enter numerical value for electric used\n\n" +
                            "2. Slide the seekbar to input percentage of rebate (0% to 5%)\n\n" +
                            "3. Click the \"Calculate\" button to determine total electricity bill charge\n\n" +
                            "4. Click \"Clear\" button to re-enter electricity unit and rebate percentage")
                    .setPositiveButton("OK", null)
                    .show();
        });


        // Set Calculate button click listener
        btnCalculate.setOnClickListener(v -> {
            hideKeyboard(v); // Dismiss the keyboard

            String unitsStr = etUnits.getText().toString().trim();

            if (unitsStr.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter numbers", Toast.LENGTH_SHORT).show();
                tvResult.setText("RM0.00"); // Reset result display
                return;
            }

            try {
                double units = Double.parseDouble(unitsStr);

                if (units < 0) {
                    Toast.makeText(getActivity(), "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }

                int rebate = seekBarRebate.getProgress();

                // Perform calculation
                double totalCost = calculateBill(units);
                double finalCost = totalCost - (totalCost * rebate / 100);

                // Update the result
                tvResult.setText(String.format("Total: RM %.2f\nRebate: %d%%\nFinal: RM %.2f", totalCost, rebate, finalCost));

                // Animate the result text
                tvResult.setAlpha(0f);
                tvResult.animate().alpha(1f).setDuration(300).start();

            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "Invalid input. Please enter a valid number.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set Clear button click listener
        btnClear.setOnClickListener(v -> {
            etUnits.setText("");
            seekBarRebate.setProgress(0);
            etRebate.setText("Rebate: 0%");
            tvResult.setText("RM0.00"); // Reset result display
            hideKeyboard(v); // Dismiss the keyboard
        });

        // Enable/Disable Calculate button based on input
        etUnits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnCalculate.setEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return root;
    }

    private double calculateBill(double units) {
        double cost = 0;

        if (units <= 200) {
            cost = units * 0.218;
        } else if (units >= 201 && units <= 300) {
            cost = 200 * 0.218 + (units - 200) * 0.334;
        } else if (units >= 301 && units <= 600) {
            cost = 200 * 0.218 + 100 * 0.334 + (units - 300) * 0.516;
        } else {
            cost = 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (units - 600) * 0.546;
        }

        return cost;
    }

    // Hide the keyboard
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
