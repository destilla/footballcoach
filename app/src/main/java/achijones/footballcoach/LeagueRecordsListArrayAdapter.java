package achijones.footballcoach;

/**
 * Created by Achi Jones on 3/29/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import achijones.footballcoach.R;

public class LeagueRecordsListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public LeagueRecordsListArrayAdapter(Context context, String[] values) {
        super(context, R.layout.league_record_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.league_record_list_item, parent, false);
        TextView textLeft = (TextView) rowView.findViewById(R.id.textLeagueRecordLeft);
        TextView textCenter = (TextView) rowView.findViewById(R.id.textLeagueRecordCenter);
        TextView textRight = (TextView) rowView.findViewById(R.id.textLeagueRecordRight);

        String[] record = values[position].split(",");
        if (!record[2].equals("XXX")) {
            // Only show record if it exists
            textLeft.setText(record[1]);
            textCenter.setText(record[0]);
            textRight.setText(record[2] + "\n" + record[3]);
        }

        return rowView;
    }
}
