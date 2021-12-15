package com.example.your_voice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;


public class TFragment extends Fragment {
    private EditText edtLanguage;
    private TextView languageCodeTV;
    private Button detectLanguageBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View V= inflater.inflate( R.layout.fragment_t,container,false );
        edtLanguage = V.findViewById(R.id.idEdtLanguage);
        languageCodeTV = V.findViewById( R.id.idTVDetectedLanguageCode);
        detectLanguageBtn = V.findViewById(R.id.idBtnDetectLanguage);
        //adding on click listener for button
        detectLanguageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting string from our edit text.
                String edt_string = edtLanguage.getText().toString();
                //calling method to detect language.
                detectLanguage(edt_string);
            }
        });
      return  V;
    }

    private void detectLanguage(String string) {
        //initializing our firebase language detection.
        FirebaseLanguageIdentification languageIdentifier =
                FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
        //adding method to detect language using identify language method.
        languageIdentifier.identifyLanguage(string).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                //below line we are setting our language code to our text view.
                languageCodeTV.setText(s);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //handling error method and displaying a toast message.
                Toast.makeText(getActivity(), "Fail to detect language : \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
