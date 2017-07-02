package com.smartstrap.contact;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstrap.R;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Person> {
    Context context;
    int layoutResourceId;
    private static final int[] levelColors = {R.color.phoneGreen,R.color.phoneYellow,R.color.phoneRed};

    ArrayList<Person> data=new ArrayList<Person>();
    public ContactAdapter(Context context, int layoutResourceId, ArrayList<Person> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        ImageHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ImageHolder();
            holder.textName = (TextView)row.findViewById(R.id.TV_name);
            holder.textPhoneNumber = (TextView)row.findViewById(R.id.TV_number);
            holder.imgIcon = (ImageView)row.findViewById(R.id.item_levelColor);
            row.setTag(holder);
        }
        else
        {
            holder = (ImageHolder)row.getTag();
        }

        Person person = data.get(position);
        holder.textName.setText(person.name);
        holder.textPhoneNumber.setText(person.phone);
        holder.imgIcon.setImageResource(levelColors[person.levelColor]);
        return row;

    }

    static class ImageHolder
    {
        ImageView imgIcon;
        TextView textName;
        TextView textPhoneNumber;
    }
}