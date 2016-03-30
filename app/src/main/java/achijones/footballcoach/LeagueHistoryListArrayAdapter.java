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

public class LeagueHistoryListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public LeagueHistoryListArrayAdapter(Context context, String[] values) {
        super(context, R.layout.league_history_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.league_history_list_item, parent, false);
        TextView textTop = (TextView) rowView.findViewById(R.id.textViewLeagueHistoryTop);
        TextView textMiddle = (TextView) rowView.findViewById(R.id.textViewLeagueHistoryMiddle);
        TextView textBottom = (TextView) rowView.findViewById(R.id.textViewLeagueHistoryBottom);

        String[] record = values[position].split("\n");
        if (record.length == 3) {
            textTop.setText(record[0]);
            textMiddle.setText(record[1]);
            textBottom.setText(record[2]);
        }

        return rowView;
    }
}
