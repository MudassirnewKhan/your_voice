package com.example.your_voice;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TemplateFragment extends Fragment {

    @Nullable

    private ImageView sleep,eat,water,play;
    MediaPlayer mp= new MediaPlayer();


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate( R.layout.fragment_template,container,false );




        sleep=view.findViewById( R.id.sleep );

        water=view.findViewById( R.id.water );

        play=view.findViewById( R.id.play );







        sleep.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp =MediaPlayer.create( getActivity(),R.raw.sleep );

                mp.start();
            }
        } );
        water.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp =MediaPlayer.create( getActivity(),R.raw.water );

                mp.start();
            }
        } );

        play.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp =MediaPlayer.create( getActivity(),R.raw.play );

                mp.start();
            }
        } );



      return view;
    }
}
