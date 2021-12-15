package com.example.your_voice;

import android.annotation.SuppressLint;
import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TranslateFragment extends Fragment {
    Button translate;

    EditText enterText;

    TextView displayText;

    ImageView aslImages;

    MainActivity thisClass;

    // Variables for Translation and Display

    int phraseIndex = 0; // Keep track of the array indexes
    String letters;     // Message to be Translated
    String display;     // Will hold the letters already displayed and show them

    // Array Libraries For Characters and Image References
    char letterIndex[] = {'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p',
            'q', 'r', 's', 't',
            'u', 'v', 'w', 'x',
            'y', 'z', ' '};

    int aslPics[] = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d,
            R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h,
            R.drawable.i, R.drawable.j, R.drawable.k, R.drawable.l,
            R.drawable.m, R.drawable.n, R.drawable.o, R.drawable.p,
            R.drawable.q, R.drawable.r, R.drawable.s, R.drawable.t,
            R.drawable.u, R.drawable.v, R.drawable.w, R.drawable.x,
            R.drawable.y, R.drawable.z, R.drawable.space};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate( R.layout.fragment_translate,container,false );


        translate = (Button) view.findViewById( R.id.buttonTranslate );
        translate.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setString( v );


                translateLetter();


            }
        } );
        enterText = (EditText) view.findViewById( R.id.textInput );
        displayText = (TextView) view.findViewById( R.id.displayText );
        aslImages = (ImageView) view.findViewById( R.id.aslViewer );


        enterText.setSelectAllOnFocus( true );





   return view  ;
    }



    public void setString(View v) {

        displayText.setText( "----" );

        // Close Keyboard on lost focus
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService( Service.INPUT_METHOD_SERVICE );
        imm.hideSoftInputFromWindow( v.getWindowToken(), 0 );


        phraseIndex = 0;
        display = "";

        //Get the Input Text
        Editable input = enterText.getText();

        //Convert to a string
        String phrase = input.toString();
        letters = phrase.toLowerCase();


    }


    @SuppressLint("SetTextI18n")
    public void translateLetter() {


        if (letters == null) {
            displayText.setText( "Press the Translate Button " );

        }

        int v = letters.length();

        final int[] imagearray = new int[v];

        int j = 0;

        if (letters != null) {


            for (int x = 0; x < v; x++) {
                char currentLetter = letters.charAt( phraseIndex );


                for (int i = 0; i < letterIndex.length; i++) {
                    if (letterIndex[i] == currentLetter) {


                        imagearray[j] = aslPics[i];

                        j++;
                    }


                }

                phraseIndex++;


            }

            //Check to see if you reach the end of the phrase
            if (phraseIndex > letters.length() - 1) {
                // Reset back to the first character
                phraseIndex = 0;

            } //end if


        }

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int k = 0;

            @Override
            public void run() {
                aslImages.setImageResource( imagearray[k] );
                k++;
                if (k > imagearray.length - 1) {
                    return;

                }
                handler.postDelayed( this, 2000 );
            }
        };
        handler.postDelayed( runnable, 5000 );


    }



}
