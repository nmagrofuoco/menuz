package be.ac.uclouvain.menuz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SurveyRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_request);

        TextView intro2 = (TextView) findViewById(R.id.id_introduction2);;
        TextView intro3 = (TextView) findViewById(R.id.id_introduction3);;

        int selection_count = Utility.getSelectionCount();
        int evaluation_length = Utility.getEvaluationLength();
        int menu_count = Utility.getMenuCount();

        if(selection_count==evaluation_length && Utility.getMenuCount()<8) { // after evaluation
            String completed_text = getString(R.string.id_survey_afterevaluation2) +
                    " \"Menu " + menu_count + "/8 - Session d'évaluation\".\n";
            intro2.setText(R.string.id_survey_afterevaluation);
            intro3.setText(completed_text);
        }
        else if(Utility.getMenuCount()==8) { // experiment completed
            intro2.setText(R.string.id_experiment_ended);
            intro3.setText(R.string.id_experiment_ended2);

            Button button = (Button) findViewById(R.id.id_button_survey);
            button.setText(R.string.button_back_home);
        }
    }

    public void startSelection(View view) {
        Intent intent = new Intent(this, NextSelectionActivity.class);

        if(Utility.getSelectionCount()==Utility.getEvaluationLength() && Utility.getMenuCount()==8)
            intent = new Intent(this, MainActivity.class);
        else if(Utility.getSelectionCount()==Utility.getEvaluationLength())
            intent = new Intent(this, MenuIntroductionActivity.class);

        startActivity(intent);
    }
}
