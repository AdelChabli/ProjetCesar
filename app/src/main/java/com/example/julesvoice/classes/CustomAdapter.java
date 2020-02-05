package com.example.julesvoice.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.julesvoice.R;

import java.util.ArrayList;

// https://medium.com/mindorks/custom-array-adapters-made-easy-b6c4930560dd

public class CustomAdapter extends ArrayAdapter<Musique>
{
    private Context context;
    private ArrayList<Musique> listMusic;

    public CustomAdapter(Context context,ArrayList<Musique> list)
    {
        super(context, 0 , list);
        this.context = context;
        this.listMusic = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.row,parent,false);

        Musique currentMusic = listMusic.get(position);

        ImageView image = listItem.findViewById(R.id.img_row);
        //image.setImageResource(currentMusic.getmImageDrawable());

        TextView titre = listItem.findViewById(R.id.text_row);
        //name.setText(currentMovie.getmName());

        return listItem;
    }
}
