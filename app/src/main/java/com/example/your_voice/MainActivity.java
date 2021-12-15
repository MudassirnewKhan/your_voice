package com.example.your_voice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private DrawerLayout drawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );




        Toolbar toolbar=findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        drawer=findViewById( R.id.drawer_layout );


       NavigationView navigationView =findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this);





        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle( this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        if(savedInstanceState== null) {
            getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, new HomeFragment() ).commit();
            navigationView.setCheckedItem( R.id.nav_home );
        }


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container,new ProfileFragment() ).commit();

                break;
            case R.id.nav_stv:
                getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container,new TranslateFragment() ).commit();

                break;
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container,new HomeFragment() ).commit();

                break;

            case R.id.nav_template:
                getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container,new TemplateFragment() ).commit();

                break;

        }

        drawer.closeDrawer( GravityCompat.START );

        return true;
    }


  @Override


    public void onBackPressed()

    {
        if(drawer.isDrawerOpen( GravityCompat.START ))
        {
            drawer.closeDrawer( GravityCompat.START );
        }
        else{
            super.onBackPressed();
        }
    }
}