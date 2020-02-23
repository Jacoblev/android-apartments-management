package com.example.auth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ListOfLists extends ArrayAdapter<Artist> {


    private AppCompatActivity context;
    private List<Artist> artistList;



    public ListOfLists(AppCompatActivity context, List<Artist> artistList) {
        super(context, R.layout.list_layout, artistList);
        this.context = context;
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView textViewName = listViewItem.findViewById(R.id.textView1);
        TextView textViewName2 = listViewItem.findViewById(R.id.textView2);



        Artist artist = artistList.get(position);

        textViewName.setText(artist.getPersonTitle());
        textViewName2.setText(artist.getPersonContent());




        return listViewItem;




    }
}
