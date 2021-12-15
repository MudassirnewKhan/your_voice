package com.example.your_voice;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class HomeFragment extends Fragment {
    private Spinner fromspin,tospin;
    private TextToSpeech mTTS;

    private SeekBar mSeekBarPitch;
    private SeekBar mSeekBarSpeed;
    private EditText srctxt;
    private TextView transtv;
    private Button translatebtn,speakbtn;
    String fromlng[]={"from","english","african","arabic","belarusian","bulgarian","bengali","catalan","czech","welsh","danish","german","greek","esperanto","spanish","estonian","persian","finnish","french","irish","galician","gujarati","hebrew","hindi","croatian","haitian","hungarian","indonesian","icelandic","italian","japanese","georgian","kannada","korean","lithuanian","latvian","macedonian","marathi","malay","maltese","dutch","norwegian","polish","portuguese","romanian","russian","slovenian","albanian","swedish","swahili","tamil","telugu","thai","tagalog","turkish","ukrainian","urdu","vietnamese","chinese"};
    String tolng[]={"to","english","african","arabic","belarusian","bulgarian","bengali","catalan","czech","welsh","danish","german","greek","esperanto","spanish","estonian","persian","finnish","french","irish","galician","gujarati","hebrew","hindi","croatian","haitian","hungarian","indonesian","icelandic","italian","japanese","georgian","kannada","korean","lithuanian","latvian","macedonian","marathi","malay","maltese","dutch","norwegian","polish","portuguese","romanian","russian","slovenian","albanian","swedish","swahili","tamil","telugu","thai","tagalog","turkish","ukrainian","urdu","vietnamese","chinese"};

    int langcode ,langscode,fromlngcode, tolangcode=0;
    String texts;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate( R.layout.fragment_home, container, false );


        fromspin=view.findViewById( R.id.idFromSpinner );
        tospin=view.findViewById( R.id.idToSpinner );
        srctxt=view.findViewById( R.id.idEditSource );
        translatebtn=view.findViewById( R.id.buttontrans );
        speakbtn=view.findViewById( R.id.buttonspeak );
        transtv=view.findViewById( R.id.translatetv );
        fromspin.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                fromlngcode= getLanguageCode(fromlng[pos]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        } );

        ArrayAdapter fromadapter  = new ArrayAdapter( getActivity() ,R.layout.spinner_item,fromlng);

        fromadapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        fromspin.setAdapter( fromadapter );
        tospin.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                tolangcode=getLanguageCode(tolng[pos]);
                langscode=getsLanguageCode( tolng[pos] );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        } );

        ArrayAdapter toadapter  = new ArrayAdapter( getActivity() ,R.layout.spinner_item,tolng);

        toadapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        tospin.setAdapter( toadapter );
        translatebtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transtv.setText( "" );
                if(srctxt.getText().toString().isEmpty())
                {
                    Toast.makeText( getActivity(),"please enter",Toast.LENGTH_SHORT ).show();
                } else if(fromlngcode==0){
                    Toast.makeText( getActivity(),"please select source lang",Toast.LENGTH_SHORT );

                }else if(tolangcode==0){
                    Toast.makeText( getActivity(),"please select translate lang",Toast.LENGTH_SHORT );

                } else{
                    translatetext( fromlngcode,tolangcode,srctxt.getText().toString() );
                }
            }
        } );

        mTTS = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {


                if (status == TextToSpeech.SUCCESS) {
                    int result = langscode;
                    speakbtn.setEnabled( true );


                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e( "TTS", "Language not supported" );
                    }
                } else {
                    Log.e( "TTS", "Initialization failed" );
                }


            }
        } );

        mSeekBarPitch = view.findViewById( R.id.seek_bar_pitch );
        mSeekBarSpeed = view.findViewById( R.id.seek_bar_speed );

        speakbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                speak();
            }
        } );



        return view;
    }



    private void translatetext( int fromlngcode,int tolangcode,String srctxt ){

        transtv.setText( "downloading model" );
        FirebaseTranslatorOptions options =new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromlngcode).setTargetLanguage(tolangcode).build();


        FirebaseTranslator translator= FirebaseNaturalLanguage. getInstance().getTranslator(options);
        FirebaseModelDownloadConditions conditions=new FirebaseModelDownloadConditions.Builder().build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener( new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                transtv.setText( "Translating" );

                translator.translate( srctxt ).addOnSuccessListener( new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        transtv.setText( s );
                        texts=s.toString();

                    }


                } ).addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText( getActivity(),"fail to translate "+e.getMessage(),Toast.LENGTH_SHORT );

                    }
                } );
            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText( getActivity(),"fail to download"+e.getMessage(),Toast.LENGTH_SHORT );

            }
        } );


    }
    public int getsLanguageCode(String language){
        int langscode=0;
        switch (language){

            case "english":
                langscode=mTTS.setLanguage( Locale.ENGLISH );
                break;
            case "african":
                langscode=mTTS.setLanguage( new Locale( "af","ZA" ));
                break;

            case "arabic":
                langscode=mTTS.setLanguage( new Locale( "ar","001" ));
                break;
            case "belarusian":
                langscode=mTTS.setLanguage( new Locale( "be","BY" ));
                break;
            case "bulgarian":
                langscode=mTTS.setLanguage( new Locale("bg","BG"));
                break;

            case "bengali":
                langscode=mTTS.setLanguage( new Locale("bn","IN") );
                break;

            case "catalan":
                langscode=mTTS.setLanguage( new Locale("ca","AD") );
                break;
            case "czech":
                langscode=mTTS.setLanguage( new Locale("cs","CZ") );
                break;
            case "welsh":
                langscode=mTTS.setLanguage( new Locale( "cy","GB" ) );
                break;
            case "danish":
                langscode=mTTS.setLanguage( new Locale("da","DK"));
                break;

            case "greek":
                langscode=mTTS.setLanguage( new Locale("el","GR") );
                break;
            case "esperanto":
                langscode=mTTS.setLanguage( new Locale("es","ES"));
                break;
            case "estonian":
                langscode=mTTS.setLanguage(new  Locale("et","EE") );
                break;
            case "persian":
                langscode=mTTS.setLanguage(new Locale("fa","AF"));
                break;

            case "german":
                langscode=mTTS.setLanguage(new Locale("de","DE") );
                break;
            case "finnish":
                langscode=mTTS.setLanguage( new Locale("fi","FI") );
                break;
            case "french":
                langscode=mTTS.setLanguage(new Locale("fr","FR"));
                break;
            case "irish":
                langscode=mTTS.setLanguage(new Locale("ga","IE") );
                break;
            case "galician":
                langscode=mTTS.setLanguage(new Locale("gl","ES") );
                break;
            case "gujarati":
                langscode=mTTS.setLanguage( new Locale("gu","IN"));
                break;
            case "hebrew":
                langscode=mTTS.setLanguage(new Locale("iw","IL") );
                break;

            case "croatian":
                langscode=mTTS.setLanguage(new Locale("hr","HR") );
                break;
            case "haitian":
                langscode=mTTS.setLanguage( new Locale("ha","GH") );
                break;
            case "hungarian":
                langscode=mTTS.setLanguage( new Locale("hu","HU"));
                break;
            case "indonesian":
                langscode=mTTS.setLanguage( new Locale("in","ID") );
                break;
            case "icelandic":
                langscode=mTTS.setLanguage(new Locale("is","IS"));
                break;
            case "italian":
                langscode=mTTS.setLanguage( new Locale("it","IT") );
                break;
            case "japanese":
                langscode=mTTS.setLanguage( new Locale("ja","JP"));

                break;
            case "georgian":
                langscode=mTTS.setLanguage(new Locale("ka","GE"));
                break;

            case "kannada":
                langscode=mTTS.setLanguage( new Locale("kn","IN") );
                break;
            case "korean":
                langscode=mTTS.setLanguage( new Locale("ko","KR") );
                break;
            case "lithuanian":
                langscode=mTTS.setLanguage( new Locale("lt","LT"));
                break;
            case "latvian":
                langscode=mTTS.setLanguage( new Locale("lv","LV"));
                break;
            case "macedonian":
                langscode=mTTS.setLanguage( new Locale("mk","MK"));
                break;
            case "marathi":
                langscode=mTTS.setLanguage( new Locale("mr","IN"));
                break;
            case "malay":
                langscode=mTTS.setLanguage( new Locale("ms","MY"));
                break;
            case "maltese":
                langscode=mTTS.setLanguage( new Locale("mt","MT"));
                break;
            case "dutch":
                langscode=mTTS.setLanguage( new Locale("nl","CW"));
                break;
            case "norwegian":
                langscode=mTTS.setLanguage( new Locale("nb","NO"));
                break;
            case "polish":
                langscode=mTTS.setLanguage( new Locale("pl","PL"));
                break;
            case "portuguese":
                langscode=mTTS.setLanguage( new Locale("pt","PT"));
                break;
            case "romanian":
                langscode=mTTS.setLanguage( new Locale("ro","RO"));
                break;
            case "russian":
                langscode=mTTS.setLanguage( new Locale("ru","RU"));
                break;
            case "slovak":
                langscode=mTTS.setLanguage( new Locale("sk","SK"));
                break;
            case "slovenian":
                langscode=mTTS.setLanguage( new Locale("sl","SI"));
                break;
            case "albanian":
                langscode=mTTS.setLanguage( new Locale("sq","AL"));
                break;
            case "swedish":
                langscode=mTTS.setLanguage( new Locale("sv","SE"));
                break;
            case "swahili":
                langscode=mTTS.setLanguage( new Locale("sw","UG"));
                break;
            case "tamil":
                langscode=mTTS.setLanguage( new Locale("ta","IN"));
                break;
            case "telugu":
                langscode=mTTS.setLanguage( new Locale("te","IN"));
                break;
            case "thai":
                langscode=mTTS.setLanguage( new Locale("th","TH"));
                break;
            case "tagalog":
                langscode=mTTS.setLanguage( new Locale("to","TO"));
                break;
            case "turkish":
                langscode=mTTS.setLanguage( new Locale("tr","TR"));
                break;
            case "ukrainian":
                langscode=mTTS.setLanguage( new Locale("uk","UA"));
                break;
            case "urdu":
                langscode=mTTS.setLanguage( new Locale("ur","PK"));
                break;
            case "vietnamese":
                langscode=mTTS.setLanguage( new Locale("vi","VN"));
                break;
            case "chinese":
                langscode=mTTS.setLanguage( new Locale("zh","CN"));
                break;

            case "hindi" :
                langscode=mTTS.setLanguage( new Locale( "hi","IN" ) );
                break;

            default:langscode=0;



        }
        return langscode;

    }
    private void speak() {
        String text = texts.toString();
        float pitch = (float) mSeekBarPitch.getProgress() / 50;
        if (pitch < 0.1) pitch = 0.1f;
        float speed = (float) mSeekBarSpeed.getProgress() / 50;
        if (speed < 0.1) speed = 0.1f;

        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);

        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null,null);

    }




    public int getLanguageCode(String language)
    { int langcode=0;

        switch (language){

            case "african":
                langcode= FirebaseTranslateLanguage.AF;
                break;


            case "arabic":
                langcode=FirebaseTranslateLanguage.AR;
                break;

            case "belarusian":
                langcode= FirebaseTranslateLanguage.BE;
                break;
            case "bulgarian":
                langcode= FirebaseTranslateLanguage.BG;
                break;

                case "bengali":
                langcode = FirebaseTranslateLanguage.BN;
                break;
                case "catalan":
                langcode= FirebaseTranslateLanguage.CA;
                break;
            case "czech":
                langcode= FirebaseTranslateLanguage.CS;
                break;
            case "welsh":
                langcode= FirebaseTranslateLanguage.CY;
                break;
            case "danish":
                langcode= FirebaseTranslateLanguage.DA;
                break;

            case "greek":
                langcode= FirebaseTranslateLanguage.EL;
                break;
            case "esperanto":
                langcode= FirebaseTranslateLanguage.EO;
                break;
            case "estonian":
                langcode= FirebaseTranslateLanguage.ET;
                break;
            case "spanish":
                langcode= FirebaseTranslateLanguage.ES;
                break;
            case "persian":
                langcode= FirebaseTranslateLanguage.FA;
                break;

            case "german":
                langcode=FirebaseTranslateLanguage.DE;
                break;
            case "finnish":
                langcode= FirebaseTranslateLanguage.FI;
                break;
            case "french":
                langcode= FirebaseTranslateLanguage.FR;
                break;
            case "irish":
                langcode= FirebaseTranslateLanguage.GA;
                break;
            case "galician":
                langcode= FirebaseTranslateLanguage.GL;
                break;
            case "gujarati":
                langcode= FirebaseTranslateLanguage.GU;
                break;
            case "hebrew":
                langcode= FirebaseTranslateLanguage.HE;
                break;

            case "hindi":
                langcode= FirebaseTranslateLanguage.HI;
                break;
            case "croatian":
                langcode= FirebaseTranslateLanguage.HR;
                break;
            case "haitian":
                langcode= FirebaseTranslateLanguage.HT;
                break;
            case "hungarian":
                langcode= FirebaseTranslateLanguage.HU;
                break;
            case "indonesian":
                langcode= FirebaseTranslateLanguage.ID;
                break;
            case "icelandic":
                langcode= FirebaseTranslateLanguage.IS;
                break;
            case "italian":
                langcode= FirebaseTranslateLanguage.IT;
                break;
            case "japanese":
                langcode= FirebaseTranslateLanguage.JA;
                break;
            case "georgian":
                langcode= FirebaseTranslateLanguage.KA;
                break;

            case "kannada":
                langcode= FirebaseTranslateLanguage.KN;
                break;
            case "korean":
                langcode= FirebaseTranslateLanguage.KO;
                break;
            case "lithuanian":
                langcode= FirebaseTranslateLanguage.LT;
                break;
            case "latvian":
                langcode= FirebaseTranslateLanguage.LV;
                break;
            case "macedonian":
                langcode= FirebaseTranslateLanguage.MK;
                break;
            case "marathi":
                langcode= FirebaseTranslateLanguage.MR;
                break;
            case "malay":
                langcode= FirebaseTranslateLanguage.MT;
                break;
            case "maltese":
                langcode= FirebaseTranslateLanguage.MS;
                break;
            case "dutch":
                langcode= FirebaseTranslateLanguage. NL;
                break;
            case "norwegian":
                langcode= FirebaseTranslateLanguage.NO;
                break;
            case "polish":
                langcode= FirebaseTranslateLanguage.PL;
                break;
            case "portuguese":
                langcode= FirebaseTranslateLanguage.PT;
                break;
            case "romanian":
                langcode= FirebaseTranslateLanguage.RO;
                break;
            case "russian":
                langcode= FirebaseTranslateLanguage.RU;
                break;
            case "slovak":
                langcode= FirebaseTranslateLanguage.SL;
                break;
            case "slovenian":
                langcode= FirebaseTranslateLanguage.SK;
                break;
            case "albanian":
                langcode= FirebaseTranslateLanguage.SQ;
                break;
            case "swedish":
                langcode= FirebaseTranslateLanguage.SV;
                break;
            case "swahili":
                langcode= FirebaseTranslateLanguage.SW;
                break;
            case "tamil":
                langcode= FirebaseTranslateLanguage.TA;
                break;
            case "telugu":
                langcode= FirebaseTranslateLanguage.TE;
                break;
            case "thai":
                langcode= FirebaseTranslateLanguage.TH;
                break;
            case "tagalog":
                langcode= FirebaseTranslateLanguage.TL;
                break;
            case "turkish":
                langcode= FirebaseTranslateLanguage.TR;
                break;
            case "ukrainian":
                langcode= FirebaseTranslateLanguage.UK;
                break;
            case "urdu":
                langcode= FirebaseTranslateLanguage.UR;
                break;
            case "vietnamese":
                langcode= FirebaseTranslateLanguage.VI;
                break;
            case "chinese":
                langcode= FirebaseTranslateLanguage.ZH;
                break;

            case "english" :
                langcode=FirebaseTranslateLanguage.EN;
                break;
            default:langcode=0;



        }
        return langcode;

    }




       public void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }

      /*  if(slept!= null)
        {
            if(slept.isPlaying()){
                slept.stop();
            }
            slept.release();
        }*/

        super.onDestroy();
    }




}
