package achijones.footballcoach;

/**
 * Created by Achi Jones on 2/21/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import achijones.footballcoach.R;

import java.util.List;
import java.util.Map;

/**
 * Class used for the recruiting expandable list view
 */
public class ExpandableListAdapterPlayerStats extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> playersInfo;
    private List<String> players;

    public ExpandableListAdapterPlayerStats(Activity context, List<String> players, Map<String, List<String>> playersInfo) {
        this.context = context;
        this.players = players;
        this.playersInfo = playersInfo;
    }

    public String getChild(int groupPosition, int childPosition) {
        return playersInfo.get(players.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String playerDetail = getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_player_stats, null);
        }

        // Set up Text for player details
        String[] detailSplit = playerDetail.split(">");
        TextView itemL = (TextView) convertView.findViewById(R.id.textPlayerStatsLeftChild);
        itemL.setText(detailSplit[0]);
        TextView itemR = (TextView) convertView.findViewById(R.id.textPlayerStatsRightChild);
        itemR.setText(detailSplit[1]);

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return playersInfo.get(players.get(groupPosition)).size();
    }

    public String getGroup(int groupPosition) {
        return players.get(groupPosition);
    }

    public int getGroupCount() {
        return players.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_player_stats, null);
        }
        String[] detailSplit = getGroup(groupPosition).split(">");
        TextView itemL = (TextView) convertView.findViewById(R.id.textPlayerStatsLeft);
        itemL.setText(detailSplit[0]);
        itemL.setTypeface(null, Typeface.BOLD);
        TextView itemR = (TextView) convertView.findViewById(R.id.textPlayerStatsRight);
        itemR.setText(detailSplit[1]);
        itemR.setTypeface(null, Typeface.BOLD);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

} //end class
