package be.ac.uclouvain.menuz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class IntroductionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
    }

    public void startAlphabeticalMenuIntroduction(View view) {
        Intent intent = new Intent(this, MenuIntroductionActivity.class);
        startActivity(intent);
    }
}
