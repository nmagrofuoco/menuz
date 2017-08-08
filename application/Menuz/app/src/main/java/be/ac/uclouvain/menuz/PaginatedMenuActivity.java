package be.ac.uclouvain.menuz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PaginatedMenuActivity extends AppCompatActivity {

    private static String required_item = "";
    private static int item_index = 6;
    private static int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paginated_menu);

        if(!Utility.isTrainingSession()) Utility.startTimer();
        item_index = 8;
        page = 1;

        // Get the required item from previous selection activity
        Intent intent = getIntent();
        required_item = intent.getStringExtra(Utility.REQUIRED_ITEM);

        // Remove previous option (no previous proposal yet available)
        Button button = (Button) findViewById(R.id.id_item_previous);
        button.setVisibility(View.INVISIBLE);
    }

    public void previousProposals(View view) {
        // Select each button and replace its text with the 6th item before it
        for(int i = 1 ; i<=8 ; i++) {
            String string_id = "id_item" + i;
            int button_id = getResources().getIdentifier(string_id, "id", getPackageName());
            Button button = (Button) findViewById(button_id);
            button.setText(Utility.getOrderedItems()[item_index-17+i]);
        }
        item_index-=8;
        page--;

        // Add next option
        Button button_next = (Button) findViewById(R.id.id_item_next);
        button_next.setVisibility(View.VISIBLE);

        // Remove previous option
        Button button = (Button) findViewById(R.id.id_item_previous);
        button.setVisibility(View.INVISIBLE);
    }

    public void nextProposals(View view) {
        // Select each button and replace its text with the 6th item after it
        for(int i = 1 ; i<=8 ; i++) {
            String string_id = "id_item" + i;
            int button_id = getResources().getIdentifier(string_id, "id", getPackageName());
            Button button = (Button) findViewById(button_id);
            button.setText(Utility.getOrderedItems()[item_index]);
            item_index++;
        }
        page++;

        // Add previous option
        Button button = (Button) findViewById(R.id.id_item_previous);
        button.setVisibility(View.VISIBLE);

        // Remove next option
        Button button_next = (Button) findViewById(R.id.id_item_next);
        button_next.setVisibility(View.INVISIBLE);
    }

    public void selectionPerformed(View view) {
        Button pressed_button = (Button) findViewById(view.getId());
        String selected_item = pressed_button.getText().toString();

        // Get the position of the required item
        String[] ordered_items = Utility.getOrderedItems();
        int position_required_item = 0;
        for(int i=0 ; i<ordered_items.length ; i++) {
            if(ordered_items[i].equals(required_item)) {
                position_required_item = i;
                i=ordered_items.length; // directly jump out of loop
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
