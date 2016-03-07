package achijones.footballcoach;

/**
 * Created by Achi Jones on 2/21/2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import achijones.footballcoach.R;

import CFBsimPack.Game;
import CFBsimPack.Team;

public class GameScheduleListArrayAdapter extends ArrayAdapter<Game> {
    private final Context context;
    private final Game[] games;
    private final Team team;
    private final MainActivity mainAct;

    public GameScheduleListArrayAdapter(Context context, MainActivity mainAct, Team team, Game[] games) {
        super(context, R.layout.game_schedule_list_item, games);
        this.context = context;
        this.mainAct = mainAct;
        this.games = games;
        this.team = team;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.game_schedule_list_item, parent, false);
        TextView textLeft = (TextView) rowView.findViewById(R.id.gameScheduleLeft);
        Button gameButton = (Button) rowView.findViewById(R.id.gameScheduleButtonList);
        TextView textRight = (TextView) rowView.findViewById(R.id.gameScheduleRight);

        String[] gameSummary = team.getGameSummaryStr(position);
        textLeft.setText( gameSummary[0] );
        gameButton.setText( gameSummary[1] );
        textRight.setText( gameSummary[2] );

        gameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                mainAct.showGameDialog( games[position] );
            }
        });

        return rowView;
    }
}
