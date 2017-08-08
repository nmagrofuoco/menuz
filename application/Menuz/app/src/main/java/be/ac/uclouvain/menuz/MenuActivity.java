package be.ac.uclouvain.menuz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private static String required_item = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if(!Utility.isTrainingSession()) Utility.startTimer();

        // Get the required item from previous selection activity
        Intent intent = getIntent();
        required_item = intent.getStringExtra(Utility.REQUIRED_ITEM);
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
        int position_selected_item = Integer.parseInt(name_button.substring(32,name_button.length()))-1;

        Utility.selectionPerformed(this, view, pressed_button, required_item, position_required_item, selected_item, position_selected_item);
    }
}
