package be.ac.uclouvain.menuz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MenuIntroductionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_introduction);

        Utility.incMenuCount();
        Utility.resetSelectionCount();
        Utility.resetItemFrequency();
        Utility.resetRequiredItems();
        Utility.resetSelectedItems();
        Utility.resetPositionSelectedItems();
        Utility.resetPositionRequiredItems();
        Utility.resetWrongSelection();
        Utility.resetSelectionTime();

        // Update the introduction according to the desired menu (menu_count)
        TextView title = (TextView) findViewById(R.id.id_menu_title);
        TextView intro1 = (TextView) findViewById(R.id.id_menu_introduction1);
        TextView intro2 = (TextView) findViewById(R.id.id_menu_introduction2);
        if(Utility.isTrainingSession()) { // during the training session
            switch(Utility.getMenuCount()) {
                case 1:
                    title.setText(R.string.id_menu);
                    intro1.setText(R.string.id_menu_introduction1);
                    intro2.setText(R.string.id_menu_introduction2);
                    break;
                case 2:
                    title.setText(R.string.id_paginatedmenu);
                    intro1.setText(R.string.id_paginatedmenu_introduction1);
                    intro2.setText(R.string.id_paginatedmenu_introduction2);
                    break;
                case 3:
                    title.setText(R.string.id_splitmenu);
                    intro1.setText(R.string.id_splitmenu_introduction1);
                    intro2.setText(R.string.id_splitmenu_introduction2);
                    break;
                case 4:
                    title.setText(R.string.id_paginatedsplitmenu);
                    intro1.setText(R.string.id_paginatedsplitmenu_introduction1);
                    intro2.setText(R.string.id_paginatedsplitmenu_introduction2);
                    break;
                case 5:
                    title.setText(R.string.id_multicolmenu);
                    intro1.setText(R.string.id_multicolmenu_introduction1);
                    intro2.setText(R.string.id_multicolmenu_introduction2);
                    break;
                case 6:
                    title.setText(R.string.id_multicolpaginatedmenu);
                    intro1.setText(R.string.id_multicolsplitmenu_introduction1);
                    intro2.setText(R.string.id_multicolsplitmenu_introduction2);

                    break;
                case 7:
                    title.setText(R.string.id_multicolsplitmenu);
                    intro1.setText(R.string.id_multicolpaginatedmenu_introduction1);
                    intro2.setText(R.string.id_multicolpaginatedmenu_introduction2);
                    break;
                case 8:
                    title.setText(R.string.id_multicolpaginatedsplitmenu);
                    intro1.setText(R.string.id_multicolpaginatedsplitmenu_introduction1);
                    intro2.setText(R.string.id_multicolpaginatedsplitmenu_introduction2);
                    break;
            }
        }
        else { // during the evaluation session
            switch(Utility.getMenuCount()) {
                case 1:
                    title.setText(R.string.id_menu);
                    intro1.setText(R.string.id_menu_evaluation_introduction1);
                    break;
                case 2:
                    title.setText(R.string.id_paginatedmenu);
                    intro1.setText(R.string.id_menu_evaluation_introduction2);
                    break;
                case 3:
                    title.setText(R.string.id_splitmenu);
                    intro1.setText(R.string.id_menu_evaluation_introduction2);
                    break;
                case 4:
                    title.setText(R.string.id_paginatedsplitmenu);
                    intro1.setText(R.string.id_menu_evaluation_introduction2);
                    break;
                case 5:
                    title.setText(R.string.id_multicolmenu);
                    intro1.setText(R.string.id_menu_evaluation_introduction2);
                    break;
                case 6:
                    title.setText(R.string.id_multicolpaginatedmenu);
                    intro1.setText(R.string.id_menu_evaluation_introduction2);
                    break;
                case 7:
                    title.setText(R.string.id_multicolsplitmenu);
                    intro1.setText(R.string.id_menu_evaluation_introduction2);
                    break;
                case 8:
                    title.setText(R.string.id_multicolpaginatedsplitmenu);
                    intro1.setText(R.string.id_menu_evaluation_introduction2);
                    break;
            }
            intro2.setVisibility(View.GONE);
        }
    }

    public void requestNextSelection(View view) {
        Intent intent = new Intent(this, NextSelectionActivity.class);
        startActivity(intent);
    }
}
