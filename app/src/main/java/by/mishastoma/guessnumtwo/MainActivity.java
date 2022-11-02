package by.mishastoma.guessnumtwo;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import by.mishastoma.guessnumtwo.user.User;


public class MainActivity extends AppCompatActivity {

    private ConstraintLayout container;
    private FragmentManager manager;
    private AboutFragment about = new AboutFragment();
    private MainFragment main = new MainFragment();
    private OptionsFragment options;
    private User user = new User("SuperPlayer");


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        manager = getFragmentManager();

        container = (ConstraintLayout) findViewById(R.id.container);

        setContentView(R.layout.activity_main);

        Button button1 = (Button) findViewById(R.id.btn_main);
        Button button2 = (Button) findViewById(R.id.btn_options);
        Button button3 = (Button) findViewById(R.id.btn_about);



        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                androidx.fragment.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, main);
                fragmentTransaction.commit();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                options = new OptionsFragment(user);
                androidx.fragment.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, options);
                fragmentTransaction.commit();

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                androidx.fragment.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, about);
                fragmentTransaction.commit();
            }
        });

        if(savedInstanceState == null) {
            androidx.fragment.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, main);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}