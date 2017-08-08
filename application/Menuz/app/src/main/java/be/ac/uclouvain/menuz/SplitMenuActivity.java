package be.ac.uclouvain.menuz;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplitMenuActivity extends AppCompatActivity {

    private static String required_item = "";
    private static int item_index = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_menu);

        String[] ordered_items = Utility.getOrderedItems();

        // Update hot list of most frequently used items
        int[][] most_frequent = Utility.getMostFrequentItems();
        for(int i = 1 ; i < most_frequent.length+1 ; i++) {
            String string_id = "id_item" + i;
            int button_id = getResources().getIdentifier(string_id, "id", getPackageName());
            Button button = (Button) findViewById(button_id);
            button.setText(ordered_items[most_frequent[i-1][0]]);
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            button.setTextColor(Color.WHITE);
        }

        // Update remaining items
        int button_index = 4;
        for(int i = 0 ; i < ordered_items.length && button_index<17 ; i++) {
            String string_id = "id_item" + button_index;
            int button_id = getResources().getIdentifier(string_id, "id", getPackageName());
            Button button = (Button) findViewById(button_id);

            // the item is already displayed in the hot list, don't display anything
            if(most_frequent[0][0]==i || most_frequent[1][0]==i || most_frequent[2][0]==i);
            else {
                button.setText(ordered_items[i]);
                button_index++;
            }
            item_index = i+1;
        }

        if(!Utility.isTrainingSession()) Utility.startTimer();

        // Get the required item from previous selection activity
        Intent intent = getIntent();
        required_item = intent.getStringExtra(Utility.REQUIRED_ITEM);
    }

    public void selectionPerformed(View view) {
        Button pressed_button = (Button) findViewById(view.getId());
        String selected_item = pressed_button.getText().toString();

        // Get the position of the required item
        int position_required_item = 0;
        for(int i = 1 ; i<17 ; i++) {
            String string_id = "id_item" + i;
            int button_id = getResources().getIdentifier(string_id, "id", getPackageName());
            Button button = (Button) findViewById(button_id);

            if(button.getText().toString().equals(required_item)) {
                position_required_item = i-1;
                i=17; // directly jump out of loop
            }
        }

        // Get the position of the selected item
        // extract and parse "XX" from the button's name "id_itemXX"
        // subtract by 1 because it starts with id_item1 and not 0
        String name_button = view.getResources().getResourceName(view.getId());
        int position_selected_item = Integer.parseInt(name_button.substring(32,name_button.length()))-1;

        Utility.selectionPerformed(this, view, pressed_button, required_item, position_required_item, selected_item, position_selected_item);
    }
}
