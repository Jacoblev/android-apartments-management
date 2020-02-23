package com.example.auth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ListOf extends ArrayAdapter<Users> {


    private AppCompatActivity context;
    private List<Users> UsersList;

    public ListOf(AppCompatActivity context, List<Users> UsersList) {
        super(context, R.layout.list_layout, UsersList);
        this.context = context;
        this.UsersList = UsersList;
    }



    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem2 = inflater.inflate(R.layout.list_layout, null, true);
        TextView textViewName = listViewItem2.findViewById(R.id.textView1);
        TextView textViewName2 = listViewItem2.findViewById(R.id.textView2);


        Users artist = UsersList.get(position);

        textViewName.setText(artist.getUltimateEmail());
        textViewName2.setText(artist.getUltimateID());


        return listViewItem2;




    }
}
