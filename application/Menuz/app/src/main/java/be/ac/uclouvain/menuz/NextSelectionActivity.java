package be.ac.uclouvain.menuz;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NextSelectionActivity extends AppCompatActivity {

    private static String required_item = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_selection);

        // Update title and selection_count
        if(Utility.isTrainingSession()) { // training session
            TextView selectioncount = (TextView) findViewById(R.id.id_selectioncount);
            int selection_count = Utility.getSelectionCount() + 1;
            selectioncount.setText(selection_count+"/"+Utility.getTrainingLength());
        }
        else { // evaluation session
            if(Utility.getSelectionCount()==0) {
                TextView next_word = (TextView) findViewById(R.id.id_nextword);
                next_word.setText(R.string.id_evaluationstarts);
                next_word.setTextColor(Color.argb(255,63,81,181));
            }

            TextView title = (TextView) findViewById(R.id.id_session);
            title.setText(getString(R.string.id_evaluationsession));

            TextView selectioncount = (TextView) findViewById(R.id.id_selectioncount);
            int selection_count = Utility.getSelectionCount()+1;
            selectioncount.setText(selection_count+"/"+Utility.getEvaluationLength());
        }

        // Pick a random item
        required_item = Utility.getRandomItem();

        // Update item name on screen
        TextView item = (TextView) findViewById(R.id.id_randomselection);
        item.setText(required_item);
    }

    public void startMenu(View view) {
        Intent intent;
        switch(Utility.getMenuCount()) {
            case 1:
                intent = new Intent(this, MenuActivity.class);
                break;
            case 2:
                intent = new Intent(this, PaginatedMenuActivity.class);
                break;
            case 3:
                intent = new Intent(this, SplitMenuActivity.class);
                break;
            case 4:
                intent = new Intent(this, PaginatedSplitMenuActivity.class);
                break;
            case 5:
                intent = new Intent(this, MultiColMenuActivity.class);
                break;
            case 6:
                intent = new Intent(this, MultiColSplitMenuActivity.class);
                break;
            case 7:
                intent = new Intent(this, MultiColPaginatedMenuActivity.class);
                break;
            case 8:
                intent = new Intent(this, MultiColPaginatedSplitMenuActivity.class);
                break;
            default:
                intent = new Intent(this, MainActivity.class);
                break;
        }

        Utility.incSelectionCount();
        intent.putExtra(Utility.REQUIRED_ITEM, required_item);
        startActivity(intent);
    }

}
