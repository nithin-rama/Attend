package michael.attend;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String>{

    Activity context;
    //    private final ArrayList<User> users;
    String[] names;
    String[] emails;

    //    public CustomListAdapter(Activity context, String[] names, String[] emails, ArrayList<User> users){
    public CustomListAdapter(Activity context, String[] names, String[] emails){

        super(context, R.layout.custom_list_item, names);

        this.context = context;
        this.names = names;
        this.emails = emails;
//        this.users = users;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = view;
        rowView = inflater.inflate(R.layout.custom_list_item, parent, false);

        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView email = (TextView) rowView.findViewById(R.id.email);

        name.setText(names[position]);
        email.setText(emails[position]);

        return rowView;
    }

}