package achijones.footballcoach;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import CFBsimPack.Team;
import achijones.footballcoach.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RecruitingActivity extends AppCompatActivity {

    // Variables use during recruiting
    private String teamName;
    private String teamAbbr;
    private int recruitingBudget;
    private final String[] letterGrades = {"F", "F+", "D", "D+", "C", "C+", "B", "B+", "A", "A+"};

    private ArrayList<String> playersRecruited;
    private ArrayList<String> playersGraduating;
    private ArrayList<String> teamQBs;
    private ArrayList<String> teamRBs;
    private ArrayList<String> teamWRs;
    private ArrayList<String> teamOLs;
    private ArrayList<String> teamKs;
    private ArrayList<String> teamSs;
    private ArrayList<String> teamCBs;
    private ArrayList<String> teamF7s;
    private ArrayList<String> teamPlayers; //all players

    private ArrayList<String> availQBs;
    private ArrayList<String> availRBs;
    private ArrayList<String> availWRs;
    private ArrayList<String> availOLs;
    private ArrayList<String> availKs;
    private ArrayList<String> availSs;
    private ArrayList<String> availCBs;
    private ArrayList<String> availF7s;
    private ArrayList<String> availAll;

    private int needQBs;
    private int needRBs;
    private int needWRs;
    private int needOLs;
    private int needKs;
    private int needSs;
    private int needCBs;
    private int needF7s;

    // Keep track of which position is selected in spinner
    private String currentPosition;

    // Android Components to keep track of
    private TextView positionText;
    private TextView budgetText;
    private Spinner positionSpinner;
    private ExpandableListView recruitList;
    private ArrayList<String> positions;
    private ArrayAdapter dataAdapterPosition;
    private ExpandableListView expListView;
    private ExpandableListAdapterRecruiting expListAdapter;
    private Map<String, List<String>> playersInfo;
    private List<String> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Init all the ArrayLists
        playersRecruited = new ArrayList<String>();
        playersGraduating = new ArrayList<String>();
        teamQBs = new ArrayList<String>();
        teamRBs = new ArrayList<String>();
        teamWRs = new ArrayList<String>();
        teamOLs = new ArrayList<String>();
        teamKs = new ArrayList<String>();
        teamSs = new ArrayList<String>();
        teamCBs = new ArrayList<String>();
        teamF7s = new ArrayList<String>();
        teamPlayers = new ArrayList<String>();
        availQBs = new ArrayList<String>();
        availRBs = new ArrayList<String>();
        availWRs = new ArrayList<String>();
        availOLs = new ArrayList<String>();
        availKs = new ArrayList<String>();
        availSs = new ArrayList<String>();
        availCBs = new ArrayList<String>();
        availF7s = new ArrayList<String>();
        availAll = new ArrayList<String>();

        // Get User Team's player info and team info for recruiting
        Bundle extras = getIntent().getExtras();
            String userTeamStr = "";
        if(extras != null) {
            userTeamStr = extras.getString("USER_TEAM_INFO");
        }

        // Parse through string
        String[] lines = userTeamStr.split("%\n");
        String[] teamInfo = lines[0].split(",");
        teamName = teamInfo[1];
        teamAbbr = teamInfo[2];
        recruitingBudget = Integer.parseInt(teamInfo[3])*15;
        getSupportActionBar().setTitle(teamName + " Recruiting");

        // First get user team's roster info
        String[] playerInfo;
        int i = 1;
        while ( !lines[i].equals("END_TEAM_INFO") ) {
            playerInfo = lines[i].split(",");
            if (playerInfo[2].equals("5")) {
                // Graduating player
                playersGraduating.add( getReadablePlayerInfo(lines[i]) );
            } else {
                teamPlayers.add(getReadablePlayerInfo(lines[i]));
                if (playerInfo[0].equals("QB")) {
                    teamQBs.add( getReadablePlayerInfo(lines[i]) );
                } else if (playerInfo[0].equals("RB")) {
                    teamRBs.add( getReadablePlayerInfo(lines[i]) );
                } else if (playerInfo[0].equals("WR")) {
                    teamWRs.add( getReadablePlayerInfo(lines[i]) );
                } else if (playerInfo[0].equals("K")) {
                    teamKs.add( getReadablePlayerInfo(lines[i]) );
                } else if (playerInfo[0].equals("OL")) {
                    teamOLs.add( getReadablePlayerInfo(lines[i]) );
                } else if (playerInfo[0].equals("S")) {
                    teamSs.add( getReadablePlayerInfo(lines[i]) );
                } else if (playerInfo[0].equals("CB")) {
                    teamCBs.add( getReadablePlayerInfo(lines[i]) );
                } else if (playerInfo[0].equals("F7")) {
                    teamF7s.add( getReadablePlayerInfo(lines[i]) );
                }
            }
            ++i; // go to next line
        }

        // Next get recruits info
        ++i;
        while ( i < lines.length ) {
            playerInfo = lines[i].split(",");
            availAll.add( lines[i] );
            if (playerInfo[0].equals("QB")) {
                availQBs.add( lines[i] );
            } else if (playerInfo[0].equals("RB")) {
                availRBs.add( lines[i] );
            } else if (playerInfo[0].equals("WR")) {
                availWRs.add( lines[i] );
            } else if (playerInfo[0].equals("K")) {
                availKs.add( lines[i] );
            } else if (playerInfo[0].equals("OL")) {
                availOLs.add( lines[i] );
            } else if (playerInfo[0].equals("S")) {
                availSs.add( lines[i] );
            } else if (playerInfo[0].equals("CB")) {
                availCBs.add( lines[i] );
            } else if (playerInfo[0].equals("F7")) {
                availF7s.add( lines[i] );
            }
            ++i;
        }

        // Sort to get top 50 overall players
        Collections.sort( availAll, new PlayerRecruitStrCompOverall() );
        availAll = new ArrayList<String>( availAll.subList(0, 100) );

        // Get needs for each position
        updatePositionNeeds();

        /**
         * Assign components to private variables for easier access later
         */
        positionText = (TextView) findViewById(R.id.textRecPosition);
        budgetText = (TextView) findViewById(R.id.textRecBudget);
        String budgetStr = teamAbbr + " budget: $" + recruitingBudget;
        budgetText.setText(budgetStr);

        /**
         * Set up spinner for examining choosing position to recruit
         */
        positionSpinner = (Spinner) findViewById(R.id.spinnerRec);
        positions = new ArrayList<String>();
        positions.add("QB (Need: " + needQBs + ")");
        positions.add("RB (Need: " + needRBs + ")");
        positions.add("WR (Need: " + needWRs + ")");
        positions.add("OL (Need: " + needOLs + ")");
        positions.add("K (Need: " + needKs + ")");
        positions.add("S (Need: " + needSs + ")");
        positions.add("CB (Need: " + needCBs + ")");
        positions.add("F7 (Need: " + needF7s + ")");
        positions.add("Top 100 Recruits");
        dataAdapterPosition = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, positions);
        dataAdapterPosition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionSpinner.setAdapter(dataAdapterPosition);
        positionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        currentPosition = parent.getItemAtPosition(position).toString();
                        updateForNewPosition();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        //heh
                    }
                });

        /**
         * Set up the "Done" button for returning back to MainActivity
         */
        Button doneRecrutingButton = (Button) findViewById(R.id.buttonDoneRecruiting);
        doneRecrutingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                exitRecruiting();
            }
        });

        /**
         * Set up the "Roster" button for displaying dialog of all players in roster
         */
        Button viewRosterButton = (Button) findViewById(R.id.buttonRecRoster);
        viewRosterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Make dialog
                makeRosterDialog();
            }
        });

        /**
         * Set up expandable list view
         */
        recruitList = (ExpandableListView) findViewById(R.id.recruitExpandList);
        setPlayerList("QB");
        setPlayerInfoMap("QB");
        expListAdapter = new ExpandableListAdapterRecruiting(this);
        recruitList.setAdapter(expListAdapter);
    }

    @Override
    public void onBackPressed() {
        exitRecruiting();
    }

    /**
     * Exit the recruiting activity. Called when the "Done" button is pressed or when user presses back button.
     */
    private void exitRecruiting() {
        StringBuilder sb = new StringBuilder();
        sb.append("Are you sure you are done recruiting? Any unfilled positions will be filled by walk-ons.\n\n");
        for (int i = 0; i < positions.size()-1; ++i) {
            sb.append("\t\t" + positions.get(i) + "\n");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(RecruitingActivity.this);
        builder.setMessage(sb.toString())
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Send info about what recruits were selected back
                        Intent myIntent = new Intent(RecruitingActivity.this, MainActivity.class);
                        myIntent.putExtra("SAVE_FILE", "DONE_RECRUITING");
                        myIntent.putExtra("RECRUITS", getRecruitsStr());
                        RecruitingActivity.this.startActivity(myIntent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Set current player list used by expandable list to correct position players
     */
    private void setPlayerList(String pos) {
        if (pos.equals("QB")) {
            players = availQBs;
        } else if (pos.equals("RB")) {
            players = availRBs;
        } else if (pos.equals("WR")) {
            players = availWRs;
        } else if (pos.equals("OL")) {
            players = availOLs;
        } else if (pos.equals("K")) {
            players = availKs;
        } else if (pos.equals("S")) {
            players = availSs;
        } else if (pos.equals("CB")) {
            players = availCBs;
        } else if (pos.equals("F7")) {
            players = availF7s;
        }
    }

    /**
     * Converts player string into '$500 QB A. Name, Overall: 89' or similar
     */
    private String getPlayerNameCost(String player) {
        String[] ps = player.split(",");
        return "$" + ps[9] + " " + ps[0] + " " + ps[1] + ", Overall: " + ps[8];
    }

    /**
     * Sets up map to align player's info with correct player
     */
    private void setPlayerInfoMap(String pos) {
        playersInfo = new LinkedHashMap<String, List<String>>();
        for (String p : players) {
            ArrayList<String> pInfoList = new ArrayList<String>();
            pInfoList.add(getPlayerDetails(p, pos));
            playersInfo.put(p, pInfoList);
        }
    }

    /**
     * Converts the player string into details, i.e. Accuracy: A, Evasion: A, etc
     */
    private String getPlayerDetails(String player, String pos) {
        String[] ps = player.split(",");
        if (pos.equals("QB")) {
            return "Potential: " + getLetterGradePot(ps[3]) +
                    ", Strength: " + getLetterGrade(ps[5]) +
                    "\nAccuracy: " + getLetterGrade(ps[6]) +
                    ", Evasion: " + getLetterGrade(ps[7]);
        } else if (pos.equals("RB")) {
            return "Potential: " + getLetterGradePot(ps[3]) +
                    ", Power: " + getLetterGrade(ps[5]) +
                    "\nSpeed: " + getLetterGrade(ps[6]) +
                    ", Evasion: " + getLetterGrade(ps[7]);
        } else if (pos.equals("WR")) {
            return "Potential: " + getLetterGradePot(ps[3]) +
                    ", Catching: " + getLetterGrade(ps[5]) +
                    "\nSpeed: " + getLetterGrade(ps[6]) +
                    ", Evasion: " + getLetterGrade(ps[7]);
        } else if (pos.equals("OL")) {
            return "Potential: " + getLetterGradePot(ps[3]) +
                    ", Strength: " + getLetterGrade(ps[5]) +
                    "\nRush Blk: " + getLetterGrade(ps[6]) +
                    ", Pass Blk: " + getLetterGrade(ps[7]);
        } else if (pos.equals("K")) {
            return "Potential: " + getLetterGradePot(ps[3]) +
                    ", Kick Power: " + getLetterGrade(ps[5]) +
                    "\nAccuracy: " + getLetterGrade(ps[6]) +
                    ", Clumsiness: " + getLetterGrade(ps[7]);
        } else if (pos.equals("S")) {
            return "Potential: " + getLetterGradePot(ps[3]) +
                    ", Coverage: " + getLetterGrade(ps[5]) +
                    "\nSpeed: " + getLetterGrade(ps[6]) +
                    ", Tackling: " + getLetterGrade(ps[7]);
        } else if (pos.equals("CB")) {
            return "Potential: " + getLetterGradePot(ps[3]) +
                    ", Coverage: " + getLetterGrade(ps[5]) +
                    "\nSpeed: " + getLetterGrade(ps[6]) +
                    ", Tackling: " + getLetterGrade(ps[7]);
        } else if (pos.equals("F7")) {
            return "Potential: " + getLetterGradePot(ps[3]) +
                    ", Strength: " + getLetterGrade(ps[5]) +
                    "\nRun Stop: " + getLetterGrade(ps[6]) +
                    ", Pass Pressure: " + getLetterGrade(ps[7]);
        }
        return "ERROR";
    }

    /**
     * Convert a rating into a letter grade. 90 -> A, 80 -> B, etc
     */
    private String getLetterGrade(String num) {
        int ind = (Integer.parseInt(num) - 50)/5;
        if (ind > 9) ind = 9;
        if (ind < 0) ind = 0;
        return letterGrades[ind];
    }

    /**
     * Convert a rating into a letter grade for potential, so 50 is a C instead of F
     */
    private String getLetterGradePot(String num) {
        int ind = (Integer.parseInt(num)-50)/5;
        if (ind > 9) ind = 9;
        if (ind < 0) ind = 0;
        return letterGrades[ind];
    }

    /**
     * Converts the lines from the file into readable lines
     */
    private String getReadablePlayerInfo(String p) {
        String[] pi = p.split(",");
        return pi[0] + " " + getInitialName(pi[1]) + " " + getYrStr(pi[2]) + " " + pi[8] + " Ovr, " + pi[3] + " Pot";
    }

    /**
     * Convert year from number to String, i.e. 3 -> Junior
     */
    private String getYrStr(String yr) {
        if (yr.equals("1")) {
            return "[Fr]";
        } else if (yr.equals("2")) {
            return "[So]";
        } else if (yr.equals("3")) {
            return "[Jr]";
        } else if (yr.equals("4")) {
            return "[Sr]";
        }
        return "[XX]";
    }

    /**
     * Convert full name into initial name
     */
    private String getInitialName(String name) {
        String[] names = name.split(" ");
        return names[0].substring(0,1) + ". " + names[1];
    }

    /**
     * Called whenever new position is selected, updates all the components
     */
    private void updateForNewPosition() {
        if (!currentPosition.equals("Top 100 Recruits")) {
            String[] splitty = currentPosition.split(" ");
            setPlayerList(splitty[0]);
            setPlayerInfoMap(splitty[0]);
            expListAdapter.notifyDataSetChanged();
        } else {
            // See top 100 recruits
            players = availAll;
            playersInfo = new LinkedHashMap<String, List<String>>();
            for (String p : players) {
                ArrayList<String> pInfoList = new ArrayList<String>();
                pInfoList.add(getPlayerDetails(p, p.split(",")[0]));
                playersInfo.put(p, pInfoList);
            }
            expListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Update needs for each position
     */
    private void updatePositionNeeds() {
        // Get needs for each position
        needQBs = 2  - teamQBs.size();
        needRBs = 4  - teamRBs.size();
        needWRs = 6  - teamWRs.size();
        needOLs = 10 - teamOLs.size();
        needKs  = 2  - teamKs.size();
        needSs  = 2  - teamSs.size();
        needCBs = 6  - teamCBs.size();
        needF7s = 14 - teamF7s.size();
        if (dataAdapterPosition != null) {
            positions = new ArrayList<String>();
            positions.add("QB (Need: " + needQBs + ")");
            positions.add("RB (Need: " + needRBs + ")");
            positions.add("WR (Need: " + needWRs + ")");
            positions.add("OL (Need: " + needOLs + ")");
            positions.add("K (Need: " + needKs + ")");
            positions.add("S (Need: " + needSs + ")");
            positions.add("CB (Need: " + needCBs + ")");
            positions.add("F7 (Need: " + needF7s + ")");
            positions.add("Top 100 Recruits");
            dataAdapterPosition.clear();
            for (String p : positions) {
                dataAdapterPosition.add(p);
            }
            dataAdapterPosition.notifyDataSetChanged();
        }
    }

    /**
     * Get String of team roster, used for displaying in dialog
     */
    private String getRosterStr() {
        updatePositionNeeds();
        StringBuilder sb = new StringBuilder();
        sb.append("QBs (Need: " + needQBs + ")\n");
        for (String p : teamQBs) {
            sb.append("\t\t" + p + "\n");
        }
        sb.append("\nRBs (Need: " + needRBs + ")\n");
        for (String p : teamRBs) {
            sb.append("\t\t" + p + "\n");
        }
        sb.append("\nWRs (Need: " + needWRs + ")\n");
        for (String p : teamWRs) {
            sb.append("\t\t" + p + "\n");
        }
        sb.append("\nOLs (Need: " + needOLs + ")\n");
        for (String p : teamOLs) {
            sb.append("\t\t" + p + "\n");
        }
        sb.append("\nKs (Need: " + needKs + ")\n");
        for (String p : teamKs) {
            sb.append("\t\t" + p + "\n");
        }
        sb.append("\nSs (Need: " + needSs + ")\n");
        for (String p : teamSs) {
            sb.append("\t\t" + p + "\n");
        }
        sb.append("\nCBs (Need: " + needCBs + ")\n");
        for (String p : teamCBs) {
            sb.append("\t\t" + p + "\n");
        }
        sb.append("\nF7s (Need: " + needF7s + ")\n");
        for (String p : teamF7s) {
            sb.append("\t\t" + p + "\n");
        }
        return sb.toString();
    }

    /**
     * Gets all the recruits in a string to send back to MainActivity to be added to user team
     */
    public String getRecruitsStr() {
        StringBuilder sb = new StringBuilder();
        for (String p : playersRecruited) {
            sb.append(p+"%\n");
        }
        return sb.toString();
    }

    /**
     * Makes the Roster dialog for viewing who is on team
     */
    private void makeRosterDialog() {
        String rosterStr = getRosterStr();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(rosterStr)
                .setTitle(teamName + " Roster")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Dismiss dialog
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView msgTxt = (TextView) dialog.findViewById(android.R.id.message);
        msgTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    /**
     * Used for parsing through string to get cost
     */
    public int getRecruitCost(String p) {
        // format is '$500 P. Name'
        String[] pSplit = p.split(",");
        return Integer.parseInt( pSplit[9] );
    }

    /**
     * Recruit player, add to correct list and remove from available players list
     */
    private boolean recruitPlayer(String player) {
        int moneyNeeded = getRecruitCost( player );
        if (recruitingBudget >= moneyNeeded) {
            recruitingBudget -= moneyNeeded;
            budgetText.setText("Budget: $" + recruitingBudget);
            playersRecruited.add(player);
            players.remove(player);

            // Also need to add recruited player to correct team list and remove from avail list
            String[] ps = player.split(",");
            if (ps[0].equals("QB")) {
                availQBs.remove(player);
                teamQBs.add(getReadablePlayerInfo(player));
                Collections.sort(teamQBs, new PlayerTeamStrCompOverall());
            } else if (ps[0].equals("RB")) {
                availRBs.remove(player);
                teamRBs.add(getReadablePlayerInfo(player));
                Collections.sort(teamRBs, new PlayerTeamStrCompOverall());
            } else if (ps[0].equals("WR")) {
                availWRs.remove(player);
                teamWRs.add(getReadablePlayerInfo(player));
                Collections.sort(teamWRs, new PlayerTeamStrCompOverall());
            } else if (ps[0].equals("OL")) {
                availOLs.remove(player);
                teamOLs.add(getReadablePlayerInfo(player));
                Collections.sort(teamOLs, new PlayerTeamStrCompOverall());
            } else if (ps[0].equals("K")) {
                availKs.remove(player);
                teamKs.add(getReadablePlayerInfo(player));
                Collections.sort(teamKs, new PlayerTeamStrCompOverall());
            } else if (ps[0].equals("S")) {
                availSs.remove(player);
                teamSs.add(getReadablePlayerInfo(player));
                Collections.sort(teamSs, new PlayerTeamStrCompOverall());
            } else if (ps[0].equals("CB")) {
                availCBs.remove(player);
                teamCBs.add(getReadablePlayerInfo(player));
                Collections.sort(teamCBs, new PlayerTeamStrCompOverall());
            } else if (ps[0].equals("F7")) {
                availF7s.remove(player);
                teamF7s.add(getReadablePlayerInfo(player));
                Collections.sort(teamF7s, new PlayerTeamStrCompOverall());
            }

            Toast.makeText(this, "Recruited " + ps[0] + " " + ps[1],
                    Toast.LENGTH_SHORT).show();

            updatePositionNeeds();
            return true;

        } else {
            Toast.makeText(this, "Not enough money!",
                    Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    /**
     * Inner Class used for the recruiting expandable list view
     */
    public class ExpandableListAdapterRecruiting extends BaseExpandableListAdapter {

        private Activity context;

        public ExpandableListAdapterRecruiting(Activity context) {//, List<String> players, Map<String, List<String>> playersInfo) {
            this.context = context;
            //this.playersInfo = playersInfo;
            //this.players = players;
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
                convertView = inflater.inflate(R.layout.child_recruit, null);
            }

            // Set up Text for player details
            TextView item = (TextView) convertView.findViewById(R.id.textRecruitDetails);
            item.setText(playerDetail);

            // Set up button for recruiting player
            Button recruitPlayerButton = (Button) convertView.findViewById(R.id.buttonRecruitPlayer);
            recruitPlayerButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Save who is currently expanded
                    List<Integer> groupsExpanded = new ArrayList<>();
                    recruitList.collapseGroup(groupPosition);
                    for (int i = groupPosition+1; i < players.size(); ++i) {
                        if ( recruitList.isGroupExpanded(i) ) {
                            groupsExpanded.add(i);
                        }
                        recruitList.collapseGroup(i);
                    }

                    // recruit player if there is enough money
                    if (recruitPlayer(getGroup(groupPosition))) {
                        // successful recruit
                        notifyDataSetChanged();
                        for (int group : groupsExpanded) {
                            recruitList.expandGroup(group - 1);
                        }
                    } else {
                        // not successful
                        recruitList.expandGroup(groupPosition);
                        notifyDataSetChanged();
                        for (int group : groupsExpanded) {
                            recruitList.expandGroup(group);
                        }
                    }

                }
            });

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
            String[] playerCost = getPlayerNameCost( getGroup(groupPosition) ).split(",");
            String playerLeft = playerCost[0];
            String playerRight = playerCost[1];
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.group_recruit,
                        null);
            }
            TextView itemL = (TextView) convertView.findViewById(R.id.textRecruitLeft);
            itemL.setTypeface(null, Typeface.BOLD);
            itemL.setText(playerLeft);
            TextView itemR = (TextView) convertView.findViewById(R.id.textRecruitRight);
            itemR.setTypeface(null, Typeface.BOLD);
            itemR.setText(playerRight);
            return convertView;
        }

        public boolean hasStableIds() {
            return true;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    } //end class

}

class PlayerRecruitStrCompOverall implements Comparator<String> {
    @Override
    public int compare( String a, String b ) {
        String[] psA = a.split(",");
        String[] psB = b.split(",");
        int ovrA = Integer.parseInt(psA[8]);
        int ovrB = Integer.parseInt(psB[8]);
        return ovrA > ovrB ? -1 : ovrA == ovrB ? 0 : 1;
    }
}

class PlayerTeamStrCompOverall implements Comparator<String> {
    @Override
    public int compare( String a, String b ) {
        String[] psA = a.split(" ");
        String[] psB = b.split(" ");
        int ovrA = Integer.parseInt(psA[4]);
        int ovrB = Integer.parseInt(psB[4]);
        return ovrA > ovrB ? -1 : ovrA == ovrB ? 0 : 1;
    }
}

