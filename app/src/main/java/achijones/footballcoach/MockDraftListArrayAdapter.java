package achijones.footballcoach;

/**
 * Created by Achi Jones on 2/20/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import achijones.footballcoach.R;

public class MockDraftListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final String userTeamStrRep;

    public MockDraftListArrayAdapter(Context context, String[] values, String userTeamStrRep) {
        super(context, R.layout.child_player_stats, values);
        this.context = context;
        this.values = values;
        this.userTeamStrRep = userTeamStrRep;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.child_player_stats, parent, false);

        String[] detailSplit = values[position].split(">");
        TextView itemL = (TextView) rowView.findViewById(R.id.textPlayerStatsLeftChild);
        itemL.setText((1+position) + ". " + detailSplit[0]);
        TextView itemR = (TextView) rowView.findViewById(R.id.textPlayerStatsRightChild);
        itemR.setText(detailSplit[1]);

        Button butt = (Button) rowView.findViewById(R.id.buttonPlayerStatsViewAll);
        butt.setVisibility(View.GONE);

        if (detailSplit[1].equals(userTeamStrRep)) {
            itemL.setTextColor(Color.parseColor("#1A75FF"));
            itemR.setTextColor(Color.parseColor("#1A75FF"));
        }

        return rowView;
    }
}
