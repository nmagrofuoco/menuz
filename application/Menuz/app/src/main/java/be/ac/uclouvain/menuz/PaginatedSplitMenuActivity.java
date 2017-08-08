package be.ac.uclouvain.menuz;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.util.Log;

public class PaginatedSplitMenuActivity extends AppCompatActivity {

    private static String required_item = "";
    private static int item_index = 3;
    private static int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paginated_split_menu);

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
        for(int i = 0 ; i < ordered_items.length && button_index<9 ; i++) {
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
        page=1;

        if(!Utility.isTrainingSession()) Utility.startTimer();

        // Get the required item from previous selection activity
        Intent intent = getIntent();
        required_item = intent.getStringExtra(Utility.REQUIRED_ITEM);

        // Remove previous option (no previous proposal yet available)
        Button button_prev = (Button) findViewById(R.id.id_item_previous);
        button_prev.setVisibility(View.INVISIBLE);
    }

    public void previousProposals(View view) {
        String[] ordered_items = Utility.getOrderedItems();
        int[][] most_frequent = Utility.getMostFrequentItems();

        // Update hot list of most frequently used items
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
        for(int i = 0 ; i < ordered_items.length && button_index<9 ; i++) {
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
        page--;

        // Add next option (if we go back, we can go forward afterwards)
        Button button_next = (Button) findViewById(R.id.id_item_next);
        button_next.setVisibility(View.VISIBLE);

        // Remove previous option (no previous proposal yet available)
        Button button = (Button) findViewById(R.id.id_item_previous);
        button.setVisibility(View.INVISIBLE);
    }

    public void nextProposals(View view) {
        String[] ordered_items = Utility.getOrderedItems();
        int[][] most_frequent = Utility.getMostFrequentItems();

        // Select each button and replace its text
        for(int i = 1 ; i<=8 && item_index<16 ; i++) {
            String string_id = "id_item" + i;
            int button_id = getResources().getIdentifier(string_id, "id", getPackageName());
            Button button = (Button) findViewById(button_id);

            // the item is already displayed in the hot list, don't display anything
            if(most_frequent[0][0]==item_index || most_frequent[1][0]==item_index || most_frequent[2][0]==item_index)
                i--;
            else button.setText(ordered_items[item_index]);
            item_index++;
        }
        page++;

        // Add previous option
        Button button = (Button) findViewById(R.id.id_item_previous);
        button.setVisibility(View.VISIBLE);

        // Remove next option (no previous proposal yet available)
        Button button_next = (Button) findViewById(R.id.id_item_next);
        button_next.setVisibility(View.INVISIBLE);

        // Remove primary item from first 3 button (not hot list anymore)
        for(int i = 1 ; i<=3 ; i++) {
            String string_id = "id_item" + i;
            int button_id = getResources().getIdentifier(string_id, "id", getPackageName());
            Button button2 = (Button) findViewById(button_id);
            button2.setBackgroundColor(ContextCompat.getColor(this, R.color.colorButton));
            button2.setTextColor(Color.BLACK);
        }
    }

    public void selectionPerformed(View view) {
        Button pressed_button = (Button) findViewById(view.getId());
        String selected_item = pressed_button.getText().toString();

        // Get the position of the required item
        String[] ordered_items = Utility.getOrderedItems();
        int[][] most_frequent = Utility.getMostFrequentItems();
        int position_required_item = 0;
        for(int i = 0 ; i<ordered_items.length ; i++) {
            if(ordered_items[i].equals(required_item)) {
                if(i==most_frequent[0][0]) position_required_item = 0;
                else if(i==most_frequent[1][0]) position_required_item = 1;
                else if(i==most_frequent[2][0]) position_required_item = 2;
                else if(i<most_frequent[0][0]) position_required_item = i+3;
                else if(i>most_frequent[0][0] && i<most_frequent[1][0]) position_required_item = i+2;
                else if(i>most_frequent[1][0] && i<most_frequent[2][0]) position_required_item = i+1;
                else position_required_item = i;
            }
        }

        // Get the position of the selected item
        // extract and parse "XX" from the button's name "id_itemXX"
        // subtract by 1 because it starts with id_item1 and not 0
        String name_button = view.getResources().getResourceName(view.getId());
        int position_selected_item = 0;
        if(page==1) position_selected_item = Integer.parseInt(name_button.substring(32,name_button.length()))-1;
        else position_selected_item = Integer.parseInt(name_button.substring(32,name_button.length()))+7;

        Utility.selectionPerformed(this, view, pressed_button, required_item, position_required_item, selected_item, position_selected_item);
    }
}
