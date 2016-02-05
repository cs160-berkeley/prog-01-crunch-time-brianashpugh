package exersizercom.www.exersizer;

import android.app.ActionBar;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextSwitcher;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CalorieConverter extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private final String[] repsExercises = new String[] {"Pushups", "Situps", "Squats", "Pullups"};
    private final String[] timedExercises = new String[] {"Leg-lifts", "Plank", "Jumping Jacks", "Cycling", "Walking", "Jogging", "Swimming", "Stair-Climbing"};
    private Map<String, String> timedVerbsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_converter);

        HashMap<String, String> timedVerbs = new HashMap<String, String>();
        timedVerbs.put("Leg-lifts", "do leg-lifts");
        timedVerbs.put("Plank", "hold plank");
        timedVerbs.put("Jumping Jacks", "do jumping jacks");
        timedVerbs.put("Cycling", "cycle");
        timedVerbs.put("Walking", "walk");
        timedVerbs.put("Jogging", "jog");
        timedVerbs.put("Swimming", "swim");
        timedVerbs.put("Stair-Climbing", "climb stairs");
        this.timedVerbsMap = timedVerbs;

        Spinner exerciseSpinner = (Spinner) findViewById(R.id.exercise_spinner);
        ArrayAdapter<CharSequence> exerciseSpinnerAdapter =
                ArrayAdapter.createFromResource(this, R.array.exercises_array, android.R.layout.simple_spinner_item);
        exerciseSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(exerciseSpinnerAdapter);
        exerciseSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextSwitcher caloriesBurnedSwitcher = (TextSwitcher) findViewById(R.id.calories_burned_switcher);
        TextSwitcher workoutSuggestionSwitcher = (TextSwitcher) findViewById(R.id.workout_suggestion_switcher);
        caloriesBurnedSwitcher.setVisibility(View.INVISIBLE);
        workoutSuggestionSwitcher.setVisibility(View.INVISIBLE);

        final String selectedExercise = (String) parent.getItemAtPosition(position);
        final TextSwitcher repsDurationSwitcher = (TextSwitcher) findViewById(R.id.reps_duration_switcher);

        TextView repsDurationTextView = (TextView) repsDurationSwitcher.getCurrentView();
        if (repsDurationTextView == null) {
            repsDurationTextView = new TextView(this);
            repsDurationTextView.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            repsDurationTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            repsDurationSwitcher.addView(repsDurationTextView);
        }

        if (selectedExercise.compareTo("") == 0)  {
            return;
        }

        final Collection<String> repsExercisesCol = new HashSet<>();
        Collections.addAll(repsExercisesCol, repsExercises);
        Collection<String> timedExercisesCol = new HashSet<>();
        Collections.addAll(timedExercisesCol, timedExercises);

        StringBuilder promptBuilder = new StringBuilder();
        if (repsExercisesCol.contains(selectedExercise)) {
            promptBuilder.append("How many ");
            promptBuilder.append(selectedExercise.toLowerCase());
            promptBuilder.append(" did you do?");
            repsDurationSwitcher.setCurrentText(promptBuilder.toString());
        }
        else {
            promptBuilder.append("How long did you ");
            promptBuilder.append(timedVerbsMap.get(selectedExercise));
            promptBuilder.append(" (minutes)?");
            repsDurationSwitcher.setCurrentText(promptBuilder.toString());
        }
        repsDurationSwitcher.setVisibility(View.VISIBLE);

        TextView durationText = (TextView) findViewById(R.id.duration_text);
        durationText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        durationText.setText("");
        durationText.setVisibility(View.VISIBLE);
        durationText.addTextChangedListener(new TextWatcher() {

            private String[] exercises = new String[] {"Pushups", "Situps", "Squats", "Pullups", "Leg-lifts", "Plank", "Jumping Jacks", "Cycling", "Walking", "Jogging", "Swimming", "Stair-Climbing"};

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Integer durationEntered = Integer.parseInt(s.toString());
                    TextSwitcher caloriesBurnedSwitcher = (TextSwitcher) findViewById(R.id.calories_burned_switcher);
                    if (caloriesBurnedSwitcher.getCurrentView() == null) {
                        TextView caloriesBurnedTextView = new TextView(getBaseContext());
                        caloriesBurnedTextView.setTextColor(Color.BLACK);
                        caloriesBurnedTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        caloriesBurnedSwitcher.addView(caloriesBurnedTextView);
                    }
                    Map<String, Integer> calorieEquivalences = new HashMap<String, Integer>();
                    calorieEquivalences.put("Pushups", 350);
                    calorieEquivalences.put("Situps", 200);
                    calorieEquivalences.put("Squats", 225);
                    calorieEquivalences.put("Leg-lifts", 25);
                    calorieEquivalences.put("Plank", 25);
                    calorieEquivalences.put("Jumping Jacks", 10);
                    calorieEquivalences.put("Pullups", 100);
                    calorieEquivalences.put("Cycling", 12);
                    calorieEquivalences.put("Walking", 20);
                    calorieEquivalences.put("Jogging", 12);
                    calorieEquivalences.put("Swimming", 13);
                    calorieEquivalences.put("Stair-Climbing", 15);

                    Float caloriesBurned =  100.0f * durationEntered.floatValue() / calorieEquivalences.get(selectedExercise).floatValue();
                    StringBuilder caloriesBurnedStringBuilder = new StringBuilder();
                    caloriesBurnedStringBuilder.append("You burned ");
                    caloriesBurnedStringBuilder.append(caloriesBurned.intValue());
                    caloriesBurnedStringBuilder.append(" calories!\nNext time, burn equal calories with one of these:");
                    caloriesBurnedSwitcher.setCurrentText(caloriesBurnedStringBuilder.toString());

                    StringBuilder suggestionStringBuilder = new StringBuilder();
                    for (String exercise : exercises) {
                        Float durationForEqualCalories = caloriesBurned * calorieEquivalences.get(exercise).floatValue() / 100.0f;
                        durationForEqualCalories = durationForEqualCalories * 100;
                        durationForEqualCalories = ((Integer) durationForEqualCalories.intValue()).floatValue() / 100.0f;

                        if (repsExercisesCol.contains(exercise)) {
                            durationForEqualCalories = durationForEqualCalories + 0.5f;
                            Integer durationRounded = durationForEqualCalories.intValue();
                            suggestionStringBuilder.append(durationRounded);
                            suggestionStringBuilder.append(" ");
                            suggestionStringBuilder.append(exercise.toLowerCase());
                            suggestionStringBuilder.append("\n");

                        } else {
                            suggestionStringBuilder.append(durationForEqualCalories);
                            suggestionStringBuilder.append(" minutes of ");
                            suggestionStringBuilder.append(exercise.toLowerCase());
                            suggestionStringBuilder.append("\n");
                        }
                    }
                    TextSwitcher workoutSuggestionSwitcher = (TextSwitcher) findViewById(R.id.workout_suggestion_switcher);
                    TextView workoutSuggestionTextView = new TextView(getBaseContext());
                    workoutSuggestionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    workoutSuggestionTextView.setTextColor(Color.BLACK);
                    if (workoutSuggestionSwitcher.getCurrentView() == null) {
                        workoutSuggestionSwitcher.addView(workoutSuggestionTextView);
                    }
                    workoutSuggestionSwitcher.setCurrentText(suggestionStringBuilder.toString());
                    workoutSuggestionSwitcher.setVisibility(View.VISIBLE);
                    caloriesBurnedSwitcher.setVisibility(View.VISIBLE);

                }
                catch(NumberFormatException e) {

                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
