package achijones.footballcoach;

/**
 * Created by Achi Jones on 2/20/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import achijones.footballcoach.R;

public class PlayerStatsListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public PlayerStatsListArrayAdapter(Context context, String[] values) {
        super(context, R.layout.child_player_stats, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.child_player_stats, parent, false);

        String[] detailSplit = values[position].split(">");
        TextView itemL = (TextView) rowView.findViewById(R.id.textPlayerStatsLeftChild);
        itemL.setText(detailSplit[0]);
        TextView itemR = (TextView) rowView.findViewById(R.id.textPlayerStatsRightChild);
        itemR.setText(detailSplit[1]);

        Button butt = (Button) rowView.findViewById(R.id.buttonPlayerStatsViewAll);
        butt.setVisibility(View.GONE);

        return rowView;
    }
}
