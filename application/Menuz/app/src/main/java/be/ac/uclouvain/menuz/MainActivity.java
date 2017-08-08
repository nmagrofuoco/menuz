package be.ac.uclouvain.menuz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import be.ac.uclouvain.menuz.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utility.resetAll();
    }

    public void startIntroduction(View view) {
        Intent intent;

        // Save participant name
        EditText typed_name = (EditText) findViewById(R.id.id_plaintext_editparticipant);
        Boolean is_name = Utility.setParticipantName(typed_name.getText().toString());

        // Not a name but a cheat code
        if(!is_name) intent = new Intent(this, MenuIntroductionActivity.class);
        else intent = new Intent(this, IntroductionActivity.class);

        startActivity(intent);
    }
}
