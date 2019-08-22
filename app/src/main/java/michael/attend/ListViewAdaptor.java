package michael.attend;

import android.app.Activity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

import java.util.ArrayList;


public class ListViewAdaptor extends ArrayAdapter<String> {

    Activity context;
    ArrayList<Event> events;
    String[] dates;
    String[] times;
    String[] records;

    public ListViewAdaptor(Activity context, String[] dates, String[] times, String[] record){

        super(context, R.layout.list_view_item, dates);

        this.context = context;
//        this.events = events;
        this.dates = dates;
        this.times = times;
        this.records = record;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = view;
        rowView = inflater.inflate(R.layout.list_view_item, parent, false);

        TextView date = (TextView) rowView.findViewById(R.id.date);
        TextView time = (TextView) rowView.findViewById(R.id.time);
        TextView record = (TextView) rowView.findViewById(R.id.record);

//        date.setText(events.get(position).date);
//        time.setText(events.get(position).time);
//        record.setText(events.get(position).record);
        date.setText(dates[position]);
        time.setText(times[position]);
        record.setText(records[position]);

        return rowView;
    }

}