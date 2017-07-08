package siliconsolutions.cpptourapp.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import siliconsolutions.cpptourapp.R;

public class HomeScreen extends AppCompatActivity {

    private Typeface typeF;
    private TextView homeTextview;
    private Button startButtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        typeF = Typeface.createFromAsset(getAssets(), "fonts/Championship.ttf");
        homeTextview = (TextView) findViewById(R.id.broncoTxtView);
        homeTextview.setTypeface(typeF, typeF.ITALIC);

        startButtn = (Button) findViewById(R.id.startButton);
        startButtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeScreenIntent = new Intent(HomeScreen.this, BaseMap.class);
                startActivity(homeScreenIntent);
            }
        });

    }

}
