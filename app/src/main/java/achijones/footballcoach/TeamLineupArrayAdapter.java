package achijones.footballcoach;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import CFBsimPack.Player;

/**
 * Created by Achi Jones on 3/18/2016.
 */
public class TeamLineupArrayAdapter extends ArrayAdapter<Player> {
    private final Context context;
    public final ArrayList<Player> players;
    public ArrayList<Player> playersSelected;
    public int playersRequired;

    public TeamLineupArrayAdapter(Context context, ArrayList<Player> values, int playersRequired) {
        super(context, R.layout.team_lineup_list_item, values);
        this.context = context;
        this.players = values;
        this.playersRequired = playersRequired;
        playersSelected = new ArrayList<>();
        for (int i = 0; i < playersRequired; ++i) {
            playersSelected.add( players.get(i) );
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.team_lineup_list_item, parent, false);

        boolean showPlayer = true;
        for (int i = 0; i < position; ++i) {
            if (players.get(i) == players.get(position)) showPlayer = false;
        }

        if (showPlayer) {
            TextView playerInfo = (TextView) rowView.findViewById(R.id.textViewLineupPlayerInfo);

            playerInfo.setText(players.get(position).getPosNameYrOvrPot_OneLine());

            CheckBox isPlayerStarting = (CheckBox) rowView.findViewById(R.id.checkboxPlayerStartingLineup);
            if (playersSelected.contains(players.get(position))) {
                isPlayerStarting.setChecked(true);
            } else {
                isPlayerStarting.setChecked(false);
            }

            isPlayerStarting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        playersSelected.add(players.get(position));
                    } else {
                        playersSelected.remove(players.get(position));
                    }
                }
            });

        }

        return rowView;


    }
}
