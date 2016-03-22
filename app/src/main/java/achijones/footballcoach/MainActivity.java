package achijones.footballcoach;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import CFBsimPack.Player;
import CFBsimPack.TeamStrategy;
import achijones.footballcoach.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import CFBsimPack.Conference;
import CFBsimPack.Game;
import CFBsimPack.League;
import CFBsimPack.Team;

public class MainActivity extends AppCompatActivity {

    int season;
    League simLeague;
    Conference currentConference;
    Team currentTeam;
    Team userTeam;
    File saveLeagueFile;

    List<String> teamList;
    List<String> confList;

    int currTab;
    String userTeamStr;
    Spinner examineTeamSpinner;
    ArrayAdapter<String> dataAdapterTeam;
    Spinner examineConfSpinner;
    ArrayAdapter<String> dataAdapterConf;
    ListView mainList;
    ExpandableListView expListPlayerStats;

    //recruiting
    int recruitingStage;

    int wantUpdateConf;

    boolean showToasts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set up list
        mainList = (ListView) findViewById(R.id.mainList);
        expListPlayerStats = (ExpandableListView) findViewById(R.id.playerStatsExpandList);

        /**
         * Determine whether to load League or start new one
         */
        Bundle extras = getIntent().getExtras();
        boolean loadedLeague = false;
        if(extras != null) {
            String saveFileStr = extras.getString("SAVE_FILE");
            if (saveFileStr.equals("NEW_LEAGUE")) {
                simLeague = new League(getString(R.string.league_player_first_names), getString(R.string.league_player_last_names));
                season = 2015;
            } else if (saveFileStr.equals("DONE_RECRUITING")) {
                File saveFile = new File(getFilesDir(), "saveLeagueRecruiting.cfb");
                if (saveFile.exists()) {
                    simLeague = new League(saveFile, getString(R.string.league_player_first_names), getString(R.string.league_player_last_names));
                    userTeam = simLeague.userTeam;
                    userTeamStr = userTeam.name;
                    userTeam.recruitPlayersFromStr(extras.getString("RECRUITS"));
                    userTeam.recruitWalkOns();
                    simLeague.updateTeamTalentRatings();
                    season = 2015 + userTeam.teamHistory.size();
                    currentTeam = userTeam;
                    loadedLeague = true;
                } else {
                    simLeague = new League(getString(R.string.league_player_first_names), getString(R.string.league_player_last_names));
                    season = 2015;
                }
            } else {
                File saveFile = new File(getFilesDir(), saveFileStr);
                if (saveFile.exists()) {
                    simLeague = new League(saveFile, getString(R.string.league_player_first_names), getString(R.string.league_player_last_names));
                    userTeam = simLeague.userTeam;
                    userTeamStr = userTeam.name;
                    simLeague.updateTeamTalentRatings();
                    season = 2015 + userTeam.teamHistory.size();
                    currentTeam = userTeam;
                    loadedLeague = true;
                } else {
                    simLeague = new League(getString(R.string.league_player_first_names), getString(R.string.league_player_last_names));
                    season = 2015;
                }
            }
        } else {
            simLeague = new League(getString(R.string.league_player_first_names), getString(R.string.league_player_last_names));
            season = 2015;
        }

        recruitingStage = -1;
        wantUpdateConf = 2; // 0 and 1, dont update, 2 update
        showToasts = true;


        if (!loadedLeague) {
            // Set it to alabama until they pick
            userTeam = simLeague.teamList.get(0);
            simLeague.userTeam = userTeam;
            userTeam.userControlled = true;
            userTeamStr = userTeam.name;
            currentTeam = userTeam;
            // Set toolbar text to '2015 Season' etc
            getSupportActionBar().setTitle(userTeam.name + " " + season + " Season");

            currentTeam = simLeague.teamList.get(0);
            currentConference = simLeague.conferences.get(0);
            //get user team from list dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose your team:");
            final String[] teams = simLeague.getTeamListStr();
            builder.setItems(teams, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Do something with the selection
                    userTeam.userControlled = false;
                    userTeam = simLeague.teamList.get(item);
                    simLeague.userTeam = userTeam;
                    userTeam.userControlled = true;
                    userTeamStr = userTeam.name;
                    currentTeam = userTeam;
                    // set rankings so that not everyone is rank #0
                    simLeague.setTeamRanks();
                    // Set toolbar text to '2015 Season' etc
                    getSupportActionBar().setTitle(userTeam.name + " " + season + " Season");
                    examineTeam(currentTeam.name);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            // Set toolbar text to '2015 Season' etc
            getSupportActionBar().setTitle(userTeam.name + " " + season + " Season");
        }

        TextView currentTeamText = (TextView) findViewById(R.id.currentTeamText);
        currentTeamText.setText(currentTeam.name + " (" + currentTeam.wins + "-" + currentTeam.losses + ")");

        /**
         * Set up spinner for examining team.
         */
        examineTeamSpinner = (Spinner) findViewById(R.id.examineTeamSpinner);
        teamList = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            teamList.add(simLeague.teamList.get(i).strRep());
        }
        dataAdapterTeam = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, teamList);
        dataAdapterTeam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        examineTeamSpinner.setAdapter(dataAdapterTeam);
        examineTeamSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        currentTeam = simLeague.findTeam(parent.getItemAtPosition(position).toString());
                        updateCurrTeam();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        //heh
                        //updateCurrTeam();
                    }
                });

        /**
         * Set up spinner for examining conference.
         */
        examineConfSpinner = (Spinner) findViewById(R.id.examineConfSpinner);
        confList = new ArrayList<String>();
        for (int i = 0; i < 6; i++) {
            confList.add(simLeague.conferences.get(i).confName);
        }
        dataAdapterConf = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, confList);
        dataAdapterConf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        examineConfSpinner.setAdapter(dataAdapterConf);
        examineConfSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        currentConference = simLeague.findConference(parent.getItemAtPosition(position).toString());
                        updateCurrConference();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        //heh
                    }
                });

        /**
         * Set up "Play Week" button
         */
        final Button simGameButton = (Button) findViewById(R.id.simGameButton);
        simGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (recruitingStage == -1) {
                    // Perform action on click
                    if (simLeague.currentWeek == 15) {
                        recruitingStage = 0;
                        beginRecruiting();
                    } else {
                        int numGamesPlayed = userTeam.gameWLSchedule.size();
                        simLeague.playWeek();

                        if (simLeague.currentWeek == 15) {
                            // Show NCG summary
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage(simLeague.seasonSummaryStr())
                                    .setTitle((2015 + userTeam.teamHistory.size()) + " Season Summary")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //do nothing?
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        } else if (userTeam.gameWLSchedule.size() > numGamesPlayed) {
                            // Played a game, show summary
                            if (showToasts) Toast.makeText(MainActivity.this, userTeam.weekSummaryStr(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        // Show notification for being invited/not invited to bowl or CCG
                        if (simLeague.currentWeek >= 12) {
                            if (!userTeam.gameSchedule.get(userTeam.gameSchedule.size() - 1).hasPlayed) {
                                String weekGameName = userTeam.gameSchedule.get(userTeam.gameSchedule.size() - 1).gameName;
                                if (weekGameName.equals("NCG")) {
                                    if (showToasts) Toast.makeText(MainActivity.this, "Congratulations! " + userTeam.name + " was invited to the National Championship Game!",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    if (showToasts) Toast.makeText(MainActivity.this, "Congratulations! " + userTeam.name + " was invited to the " +
                                                    weekGameName + "!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else if (simLeague.currentWeek == 12) {
                                if (showToasts) Toast.makeText(MainActivity.this, userTeam.name + " was not invited to the Conference Championship.",
                                        Toast.LENGTH_SHORT).show();
                            } else if (simLeague.currentWeek == 13) {
                                if (showToasts) Toast.makeText(MainActivity.this, userTeam.name + " was not invited to a bowl game.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        if (simLeague.currentWeek < 12) {
                            simGameButton.setText("Play Week");
                        } else if (simLeague.currentWeek == 12) {
                            simGameButton.setText("Play Conf Championships");
                            examineTeam(currentTeam.name);
                        } else if (simLeague.currentWeek == 13) {
                            heismanCeremony();
                            simGameButton.setText("Play Bowl Games");
                            examineTeam(currentTeam.name);
                        } else if (simLeague.currentWeek == 14) {
                            simGameButton.setText("Play National Championship");
                        } else {
                            simGameButton.setText("Begin Recruiting");
                        }

                        updateCurrTeam();
                        scrollToLatestGame();

                    }
                } else {
                    //in process of recruiting
                    beginRecruiting();
                }
            }
        });

        //Set up "Team Stats" Button
        Button teamStatsButton = (Button) findViewById(R.id.teamStatsButton);
        teamStatsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                currTab = 0;
                updateTeamStats();
            }
        });

        //Set up "Player Stats" Button
        Button playerStatsButton = (Button) findViewById(R.id.playerStatsButton);
        playerStatsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                currTab = 1;
                updatePlayerStats();
            }
        });

        //Set up "Schedule" Button
        Button teamScheduleButton = (Button) findViewById(R.id.teamScheduleButton);
        teamScheduleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                currTab = 2;
                updateSchedule();
            }
        });

        // Plz work
        if (loadedLeague) {
            // set rankings so that not everyone is rank #0
            simLeague.setTeamRanks();
            examineTeam(userTeam.name);
            showToasts = userTeam.showPopups;
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_team_strategy) {
            /**
             * Clicked Team Strategy. Bring up team strat dialog
             */
            showTeamStrategyDialog();
        } else if (id == R.id.action_heisman) {
            /**
             * Clicked Heisman watch in drop down menu
             */
            String heismanTop5Str = simLeague.getTop5HeismanStr();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(heismanTop5Str)
                    .setTitle("Player of the Year Watch")
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing?
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        } else if (id == R.id.action_rankings) {
            /**
             * Clicked Team Rankings in drop down menu
             */
            showTeamRankingsDialog();
        } else if (id == R.id.action_news_stories) {
            /**
             * Clicked News Stories in drop down menu
             */
            showNewsStoriesDialog();
        } else if (id == R.id.action_league_history) {
            /**
             * Clicked League History in drop down menu
             */
            String historyStr = simLeague.getLeagueHistoryStr();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(historyStr)
                    .setTitle("League History")
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing?
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        } else if (id == R.id.action_team_history) {
            /**
             * Clicked Team History in drop down menu
             */
            String historyStr = userTeam.getTeamHistoryStr();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(historyStr)
                    .setTitle(userTeam.name + " Team History")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing?
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (id == R.id.action_ccg_bowl_watch) {
            /**
             * Clicked CCG / Bowl Watch in drop down menu
             */
            showBowlCCGDialog();
        } else if (id == R.id.action_save_league) {
            /**
             * Clicked Save League in drop down menu
             */
            saveLeague();
        } else if (id == R.id.action_return_main_menu) {
            /**
             * Let user confirm that they actually do want to go to main menu
             */
            exitMainActivity();
        } else if (id == R.id.action_change_team_name) {
            /**
             * Let user change their team name and abbr
             */
            changeTeamNameDialog();
        } else if (id == R.id.action_team_lineup) {
            /**
             * Let user set their team's lineup
             */
            showTeamLineupDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void examineTeam(String teamName) {
        wantUpdateConf = 0;
        // Find team
        Team tempT = simLeague.teamList.get(0);
        for (Team t : simLeague.teamList) {
            if (t.name.equals(teamName)) {
                currentTeam = t;
                tempT = t;
                break;
            }
        }
        // Find conference
        for (int i = 0; i < simLeague.conferences.size(); ++i) {
            Conference c = simLeague.conferences.get(i);
            if (c.confName.equals(currentTeam.conference)) {
                if (c == currentConference) wantUpdateConf = 1;
                currentConference = c;
                examineConfSpinner.setSelection(i);
                break;
            }
        }

        teamList = new ArrayList<String>();
        dataAdapterTeam.clear();
        for (int i = 0; i < currentConference.confTeams.size(); i++) {
            teamList.add(currentConference.confTeams.get(i).strRep());
            dataAdapterTeam.add(teamList.get(i));
        }
        dataAdapterTeam.notifyDataSetChanged();

        for (int i = 0; i < currentConference.confTeams.size(); ++i) {
            String[] spinnerSplit = dataAdapterTeam.getItem(i).split(" ");
            System.out.println("Looking for " + spinnerSplit[1]);
            if (spinnerSplit[1].equals(tempT.abbr)) {
                examineTeamSpinner.setSelection(i);
                currentTeam = tempT;
                TextView currentTeamText = (TextView) findViewById(R.id.currentTeamText);
                currentTeamText.setText("#" + currentTeam.rankTeamPollScore +
                        " " + currentTeam.name + " (" + currentTeam.wins + "-" + currentTeam.losses + ") " +
                        currentTeam.confChampion + " " + currentTeam.semiFinalWL + currentTeam.natChampWL);
                if (currTab == 0) {
                    updateTeamStats();
                } else if (currTab == 1) {
                    updatePlayerStats();
                } else {
                    updateSchedule();
                }
                break;
            }
        }
    }

    private void updateCurrTeam() {
        teamList = new ArrayList<String>();
        dataAdapterTeam.clear();
        for (int i = 0; i < 10; i++) {
            teamList.add(currentConference.confTeams.get(i).strRep());
            dataAdapterTeam.add(teamList.get(i));
        }
        dataAdapterTeam.notifyDataSetChanged();
        TextView currentTeamText = (TextView) findViewById(R.id.currentTeamText);
        currentTeamText.setText("#" + currentTeam.rankTeamPollScore +
                " " + currentTeam.name + " (" + currentTeam.wins + "-" + currentTeam.losses + ") " +
                currentTeam.confChampion + " " + currentTeam.semiFinalWL + currentTeam.natChampWL);
        if (currTab == 0) {
            updateTeamStats();
        } else if (currTab == 1) {
            updatePlayerStats();
        } else {
            updateSchedule();
        }
    }

    private void updateCurrConference() {
        if (wantUpdateConf >= 1) {
            teamList = new ArrayList<String>();
            dataAdapterTeam.clear();
            for (int i = 0; i < 10; i++) {
                teamList.add(currentConference.confTeams.get(i).strRep());
                dataAdapterTeam.add(teamList.get(i));
            }
            dataAdapterTeam.notifyDataSetChanged();
            examineTeamSpinner.setSelection(0);
            currentTeam = currentConference.confTeams.get(0);
            updateCurrTeam();
        } else {
            wantUpdateConf++;
        }
    }

    private void updateTeamStats(){
        mainList.setVisibility(View.VISIBLE);
        expListPlayerStats.setVisibility(View.GONE);
        TextView textTabDescription = (TextView) findViewById(R.id.textTabDescription);
        textTabDescription.setText(currentTeam.name + " Team Stats:");

        String[] teamStatsStr = currentTeam.getTeamStatsStrCSV().split("%\n");
        mainList.setAdapter(new TeamStatsListArrayAdapter(this, teamStatsStr));
    }

    private void updatePlayerStats(){
        mainList.setVisibility(View.GONE);
        expListPlayerStats.setVisibility(View.VISIBLE);
        TextView textTabDescription = (TextView) findViewById(R.id.textTabDescription);
        textTabDescription.setText(currentTeam.name + " Team Roster:");

        List<String> playerHeaders = currentTeam.getPlayerStatsExpandListStr();
        Map<String, List<String>> playerInfos = currentTeam.getPlayerStatsExpandListMap(playerHeaders);
        ExpandableListAdapterPlayerStats expListAdapterPlayerStats =
                new ExpandableListAdapterPlayerStats(this, playerHeaders, playerInfos);
        expListPlayerStats.setAdapter(expListAdapterPlayerStats);
    }

    private void updateSchedule(){
        mainList.setVisibility(View.VISIBLE);
        expListPlayerStats.setVisibility(View.GONE);
        TextView textTabDescription = (TextView) findViewById(R.id.textTabDescription);
        textTabDescription.setText(currentTeam.name + " Game Schedule:");

        Game[] games = new Game[currentTeam.gameSchedule.size()];
        for (int i = 0; i < games.length; ++i) {
            games[i] = currentTeam.gameSchedule.get(i);
        }
        mainList.setAdapter(new GameScheduleListArrayAdapter(this, this, currentTeam, games));
    }

    private void scrollToLatestGame() {
        if (currTab == 2 && simLeague.currentWeek > 2) {
            mainList.setSelection(currentTeam.numGames()-3);
        }

    }

    private void heismanCeremony() {
        String heismanStr = simLeague.getHeismanCeremonyStr();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(heismanStr)
                .setTitle("Player of the Year")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView msgTxt = (TextView) dialog.findViewById(android.R.id.message);
        msgTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    public void showGameDialog(Game g) {
        String[] gameStr;
        if (g.hasPlayed) {
            gameStr = g.getGameSummaryStr();
        } else {
            gameStr = g.getGameScoutStr();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(g.awayTeam.abbr + " @ " + g.homeTeam.abbr + ": " + g.gameName)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.game_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        final TextView gameL = (TextView) dialog.findViewById(R.id.gameDialogLeft);
        gameL.setText(gameStr[0]);
        final TextView gameC = (TextView) dialog.findViewById(R.id.gameDialogCenter);
        gameC.setText(gameStr[1]);
        final TextView gameR = (TextView) dialog.findViewById(R.id.gameDialogRight);
        gameR.setText(gameStr[2]);
        final TextView gameB = (TextView) dialog.findViewById(R.id.gameDialogBottom);
        gameB.setText(gameStr[3] + "\n\n");
    }

    public void showTeamRankingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Team Rankings")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        ArrayList<String> rankings = new ArrayList<String>();// = simLeague.getTeamRankingsStr(0);
        String[] rankingsSelection = {"Poll Votes", "Strength of Sched", "Points Per Game", "Opp Points Per Game",
                "Yards Per Game", "Opp Yards Per Game", "Pass Yards Per Game", "Rush Yards Per Game",
                "Opp Pass YPG", "Opp Rush YPG", "TO Differential", "Off Talent", "Def Talent", "Prestige"};
        Spinner teamRankingsSpinner = (Spinner) dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> teamRankingsSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, rankingsSelection);
        teamRankingsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamRankingsSpinner.setAdapter(teamRankingsSpinnerAdapter);

        final ListView teamRankingsList = (ListView) dialog.findViewById(R.id.listViewTeamRankings);
        final TeamRankingsListArrayAdapter teamRankingsAdapter =
                new TeamRankingsListArrayAdapter(this, rankings, userTeam.strRepWithBowlResults());
        teamRankingsList.setAdapter(teamRankingsAdapter);

        teamRankingsSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> rankings = simLeague.getTeamRankingsStr(position);
                        teamRankingsAdapter.clear();
                        teamRankingsAdapter.addAll(rankings);
                        teamRankingsAdapter.notifyDataSetChanged();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    public void showBowlCCGDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CCGs / Bowl Games")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.bowl_ccg_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        String[] selection = {"Conf Championships", "Bowl Games"};
        Spinner bowlCCGSpinner = (Spinner) dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> bowlCCGadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, selection);
        bowlCCGadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bowlCCGSpinner.setAdapter(bowlCCGadapter);

        final TextView bowlCCGscores = (TextView) dialog.findViewById(R.id.textViewBowlCCGDialog);

        bowlCCGSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            bowlCCGscores.setText(simLeague.getCCGsStr());
                        } else {
                            bowlCCGscores.setText(simLeague.getBowlGameWatchStr());
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    public void showNewsStoriesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("News Stories")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        ArrayList<String> rankings = new ArrayList<String>();// = simLeague.getTeamRankingsStr(0);
        String[] weekSelection = new String[simLeague.currentWeek+1];
        for (int i = 0; i < weekSelection.length; ++i) {
            if (i == 13) weekSelection[i] = "Conf Champ Week";
            else if (i == 14) weekSelection[i] = "Bowl Game Week";
            else if (i == 15) weekSelection[i] = "National Champ";
            else weekSelection[i] = "Week " + i;
        }
        Spinner weekSelectionSpinner = (Spinner) dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> weekSelectionSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, weekSelection);
        weekSelectionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekSelectionSpinner.setAdapter(weekSelectionSpinnerAdapter);
        weekSelectionSpinner.setSelection(simLeague.currentWeek);

        final ListView newsStoriesList = (ListView) dialog.findViewById(R.id.listViewTeamRankings);
        final NewsStoriesListArrayAdapter newsStoriesAdapter = new NewsStoriesListArrayAdapter(this, rankings);
        newsStoriesList.setAdapter(newsStoriesAdapter);

        weekSelectionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> rankings = simLeague.newsStories.get(position);
                        boolean isempty = false;
                        if (rankings.size() == 0) {
                            isempty = true;
                            rankings.add("No news stories.>I guess this week was a bit boring, huh?");
                        }
                        newsStoriesAdapter.clear();
                        newsStoriesAdapter.addAll(rankings);
                        newsStoriesAdapter.notifyDataSetChanged();
                        if (isempty) {
                            rankings.remove(0);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    /**
     * Dialog for coaches to select their team's strategy for offense and defense.
     */
    private void showTeamStrategyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Team Strategy")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_strategy_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        // Get the options for team strategies in both offense and defense
        final TeamStrategy[] tsOff = userTeam.getTeamStrategiesOff();
        final TeamStrategy[] tsDef = userTeam.getTeamStrategiesDef();
        int offStratNum = 0;
        int defStratNum = 0;

        String[] stratOffSelection = new String[ tsOff.length ];
        for (int i = 0; i < tsOff.length; ++i) {
            stratOffSelection[i] = tsOff[i].getStratName();
            if (stratOffSelection[i].equals(userTeam.teamStratOff.getStratName())) offStratNum = i;
        }

        String[] stratDefSelection = new String[ tsDef.length ];
        for (int i = 0; i < tsDef.length; ++i) {
            stratDefSelection[i] = tsDef[i].getStratName();
            if (stratDefSelection[i].equals(userTeam.teamStratDef.getStratName())) defStratNum = i;
        }

        final TextView offStratDescription = (TextView) dialog.findViewById(R.id.textOffenseStrategy);
        final TextView defStratDescription = (TextView) dialog.findViewById(R.id.textDefenseStrategy);

        // Offense Strategy Spinner
        Spinner stratOffSelectionSpinner = (Spinner) dialog.findViewById(R.id.spinnerOffenseStrategy);
        ArrayAdapter<String> stratOffSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stratOffSelection);
        stratOffSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stratOffSelectionSpinner.setAdapter(stratOffSpinnerAdapter);
        stratOffSelectionSpinner.setSelection(offStratNum);

        stratOffSelectionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        offStratDescription.setText(tsOff[position].getStratDescription());
                        userTeam.teamStratOff = tsOff[position];
                        userTeam.teamStratOffNum = position;
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });

        // Defense Spinner Adapter
        Spinner stratDefSelectionSpinner = (Spinner) dialog.findViewById(R.id.spinnerDefenseStrategy);
        ArrayAdapter<String> stratDefSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stratDefSelection);
        stratDefSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stratDefSelectionSpinner.setAdapter(stratDefSpinnerAdapter);
        stratDefSelectionSpinner.setSelection(defStratNum);

        stratDefSelectionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        defStratDescription.setText(tsDef[position].getStratDescription());
                        userTeam.teamStratDef = tsDef[position];
                        userTeam.teamStratDefNum = position;
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });

    }

    /**
     * Open dialog that allows users to change their name and/or abbr.
     */
    private void changeTeamNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Settings / Change Name")
                .setView(getLayoutInflater().inflate(R.layout.change_team_name_dialog, null));
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText changeNameEditText = (EditText) dialog.findViewById(R.id.editTextChangeName);
        changeNameEditText.setText(userTeam.name);
        final EditText changeAbbrEditText = (EditText) dialog.findViewById(R.id.editTextChangeAbbr);
        changeAbbrEditText.setText(userTeam.abbr);

        final TextView invalidNameText = (TextView) dialog.findViewById(R.id.textViewChangeName);
        final TextView invalidAbbrText = (TextView) dialog.findViewById(R.id.textViewChangeAbbr);

        changeNameEditText.addTextChangedListener(new TextWatcher() {
            String newName;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                newName = s.toString().trim();
                if (!simLeague.isNameValid(newName)) {
                    invalidNameText.setText("Name already in use or has illegal characters!");
                } else {
                    invalidNameText.setText("");
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newName = s.toString().trim();
                if (!simLeague.isNameValid(newName)) {
                    invalidNameText.setText("Name already in use or has illegal characters!");
                } else {
                    invalidNameText.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                newName = s.toString().trim();
                if (!simLeague.isNameValid(newName)) {
                    invalidNameText.setText("Name already in use or has illegal characters!");
                } else {
                    invalidNameText.setText("");
                }
            }
        });

        changeAbbrEditText.addTextChangedListener(new TextWatcher() {
            String newAbbr;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                newAbbr = s.toString().trim().toUpperCase();
                if (!simLeague.isAbbrValid(newAbbr)) {
                    invalidAbbrText.setText("Abbreviation already in use or has illegal characters!");
                } else {
                    invalidAbbrText.setText("");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newAbbr = s.toString().trim().toUpperCase();
                if (!simLeague.isAbbrValid(newAbbr)) {
                    invalidAbbrText.setText("Abbreviation already in use or has illegal characters!");
                } else {
                    invalidAbbrText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                newAbbr = s.toString().trim().toUpperCase();
                if (!simLeague.isAbbrValid(newAbbr)) {
                    invalidAbbrText.setText("Abbr already in use or has illegal characters!");
                } else {
                    invalidAbbrText.setText("");
                }
            }
        });

        final CheckBox checkboxShowPopup = (CheckBox) dialog.findViewById(R.id.checkboxShowPopups);
        checkboxShowPopup.setChecked(showToasts);

        Button cancelChangeNameButton = (Button) dialog.findViewById(R.id.buttonCancelChangeName);
        Button okChangeNameButton = (Button) dialog.findViewById(R.id.buttonOkChangeName);

        cancelChangeNameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                showToasts = checkboxShowPopup.isChecked();
                userTeam.showPopups = showToasts;
                dialog.dismiss();
            }
        });

        okChangeNameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                String newName = changeNameEditText.getText().toString().trim();
                String newAbbr = changeAbbrEditText.getText().toString().trim().toUpperCase();
                ;
                if (simLeague.isNameValid(newName) && simLeague.isAbbrValid(newAbbr)) {
                    userTeam.name = newName;
                    userTeam.abbr = newAbbr;
                    getSupportActionBar().setTitle(userTeam.name + " " + season + " Season");
                    // Have to update rival's rival too!
                    Team rival = simLeague.findTeamAbbr(userTeam.rivalTeam);
                    rival.rivalTeam = userTeam.abbr;
                    examineTeam(userTeam.name);
                } else {
                    if (showToasts) Toast.makeText(MainActivity.this, "Invalid name/abbr! Name not changed.",
                            Toast.LENGTH_SHORT).show();
                }
                showToasts = checkboxShowPopup.isChecked();
                userTeam.showPopups = showToasts;
                dialog.dismiss();
            }
        });

    }

    /**
     * Allow users to set lineups here.
     */
    private void showTeamLineupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Team Lineup")
                .setView(getLayoutInflater().inflate(R.layout.team_lineup_dialog, null));
        final AlertDialog dialog = builder.create();
        dialog.show();

        final String[] positionSelection = {"QB (1 starter)", "RB (2 starters)", "WR (3 starters)", "OL (5 starters)",
                                        "K (1 starter)", "S (1 starter)", "CB (3 starters)", "F7 (7 starters)"};
        final int[] positionNumberRequired = {1, 2, 3, 5, 1, 1, 3, 7};
        final Spinner teamLineupPositionSpinner = (Spinner) dialog.findViewById(R.id.spinnerTeamLineupPosition);
        ArrayAdapter<String> teamLineupPositionSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, positionSelection);
        teamLineupPositionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamLineupPositionSpinner.setAdapter(teamLineupPositionSpinnerAdapter);

        // Text to show what each attr is
        final TextView textLineupPositionDescription = (TextView) dialog.findViewById(R.id.textViewLineupPositionDescription);

        // List of team's players for selected position
        final ArrayList<Player> positionPlayers = new ArrayList<>();
        positionPlayers.addAll(userTeam.teamQBs);

        final ListView teamPositionList = (ListView) dialog.findViewById(R.id.listViewTeamLineup);
        final TeamLineupArrayAdapter teamLineupAdapter = new TeamLineupArrayAdapter(this, positionPlayers, 1);
        teamPositionList.setAdapter(teamLineupAdapter);

        teamLineupPositionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        updateLineupList(position, teamLineupAdapter, positionNumberRequired, positionPlayers, textLineupPositionDescription);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });

        Button saveLineupsButton = (Button) dialog.findViewById(R.id.buttonSaveLineups);
        Button doneWithLineupsButton = (Button) dialog.findViewById(R.id.buttonDoneWithLineups);

        doneWithLineupsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                dialog.dismiss();
            }
        });

        saveLineupsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Save the lineup that player set for the position
                int positionSpinner = teamLineupPositionSpinner.getSelectedItemPosition();
                if (teamLineupAdapter.playersSelected.size() == teamLineupAdapter.playersRequired) {
                    // Set starters to new selection
                    userTeam.setStarters(teamLineupAdapter.playersSelected, positionSpinner);

                    // Update list to show the change
                    updateLineupList(positionSpinner, teamLineupAdapter, positionNumberRequired, positionPlayers, textLineupPositionDescription);

                    Toast.makeText(MainActivity.this, "Saved lineup for " + positionSelection[positionSpinner] + "!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, teamLineupAdapter.playersSelected.size() + " players selected.\nNot the correct number of starters (" + teamLineupAdapter.playersRequired + ")",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * Update the lineup list to a new position.
     * Has checked boxes for all the starters.
     * @param position position looking at (0-7)
     * @param teamLineupAdapter TeamLineupArrayAdapter storing the data
     * @param positionNumberRequired number of players required by the position (1 for QB, 3 for WR, etc)
     * @param positionPlayers arraylist of players
     */
    private void updateLineupList(int position, TeamLineupArrayAdapter teamLineupAdapter, int[] positionNumberRequired,
                                  ArrayList<Player> positionPlayers, TextView textLineupPositionDescription) {
        teamLineupAdapter.playersRequired = positionNumberRequired[position];
        teamLineupAdapter.playersSelected.clear();
        teamLineupAdapter.players.clear();
        positionPlayers.clear();
        // Change position players to correct position
        switch (position) {
            case 0:
                textLineupPositionDescription.setText("Name [Yr] Ovr/Pot (Str, Acc, Eva)");
                positionPlayers.addAll( userTeam.teamQBs );
                break;
            case 1:
                textLineupPositionDescription.setText("Name [Yr] Ovr/Pot (Pow, Spd, Eva)");
                positionPlayers.addAll( userTeam.teamRBs );
                break;
            case 2:
                textLineupPositionDescription.setText("Name [Yr] Ovr/Pot (Cat, Spd, Eva)");
                positionPlayers.addAll( userTeam.teamWRs );
                break;
            case 3:
                textLineupPositionDescription.setText("Name [Yr] Ovr/Pot (Str, RunBlk, PassBlk)");
                positionPlayers.addAll( userTeam.teamOLs );
                break;
            case 4:
                textLineupPositionDescription.setText("Name [Yr] Ovr/Pot (KStr, KAcc, Clum)");
                positionPlayers.addAll( userTeam.teamKs );
                break;
            case 5:
                textLineupPositionDescription.setText("Name [Yr] Ovr/Pot (Cov, Spd, Tack)");
                positionPlayers.addAll( userTeam.teamSs );
                break;
            case 6:
                textLineupPositionDescription.setText("Name [Yr] Ovr/Pot (Cov, Spd, Tack)");
                positionPlayers.addAll( userTeam.teamCBs );
                break;
            case 7:
                textLineupPositionDescription.setText("Name [Yr] Ovr/Pot (Str, RunDef, PassDef)");
                positionPlayers.addAll( userTeam.teamF7s ); break;
        }

        // Change starters to correct starters
        for (int i = 0; i < teamLineupAdapter.playersRequired; ++i) {
            teamLineupAdapter.playersSelected.add( positionPlayers.get(i) );
        }
        teamLineupAdapter.notifyDataSetChanged();
    }

    /**
     * Start Recruiting Activity, sending over the user team's players and budget.
     */
    private void beginRecruiting() {
        String gradPlayersStr = userTeam.getGraduatingPlayersStr();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(gradPlayersStr)
                .setTitle(userTeam.abbr+" Players Leaving")
                .setPositiveButton("Begin Recruiting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        simLeague.updateLeagueHistory();
                        simLeague.updateTeamHistories();
                        userTeam.resetStats();
                        simLeague.advanceSeason();
                        saveLeagueFile = new File(getFilesDir(), "saveLeagueRecruiting.cfb");
                        simLeague.saveLeague(saveLeagueFile);

                        //Get String of user team's players and such
                        StringBuilder sb = new StringBuilder();
                        sb.append(userTeam.conference + "," + userTeam.name + "," + userTeam.abbr + "," + userTeam.teamPrestige + "%\n");
                        sb.append(userTeam.getPlayerInfoSaveFile());
                        sb.append("END_TEAM_INFO%\n");
                        sb.append(userTeam.getRecruitsInfoSaveFile());

                        //Start Recruiting Activity
                        Intent myIntent = new Intent(MainActivity.this, RecruitingActivity.class);
                        myIntent.putExtra("USER_TEAM_INFO", sb.toString());
                        MainActivity.this.startActivity(myIntent);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView msgTxt = (TextView) dialog.findViewById(android.R.id.message);
        msgTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    @Override
    public void onBackPressed() {
        exitMainActivity();
    }

    /**
     * Exit the main activity. Show dialog to ask if they are sure they want to quit, and give info about saving.
     */
    private void exitMainActivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to return to main menu? Any progress from the beginning of the season will be lost.")
                .setPositiveButton("Yes, Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Actually go back to main menu
                        Intent myIntent = new Intent(MainActivity.this, Home.class);
                        MainActivity.this.startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Save League, show dialog to choose which save file to save onto.
     */
    private void saveLeague() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Save File to Overwrite:");
        final String[] fileInfos = getSaveFileInfos();
        builder.setItems(fileInfos, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                saveLeagueFile = new File(getFilesDir(), "saveFile" + item + ".cfb");
                simLeague.saveLeague(saveLeagueFile);
                if (showToasts) Toast.makeText(MainActivity.this, "Saved league!",
                        Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Get info of the 5 save files for printing in the save file list
     */
    private String[] getSaveFileInfos() {
        String[] infos = new String[10];
        String fileInfo;
        File saveFile;
        for (int i = 0; i < 10; ++i) {
            saveFile = new File(getFilesDir(), "saveFile" + i + ".cfb");
            if (saveFile.exists()) {
                try {
                    BufferedReader bufferedReader = new BufferedReader( new FileReader(saveFile) );
                    fileInfo = bufferedReader.readLine();
                    infos[i] = fileInfo.substring(0, fileInfo.length()-1); //gets rid of % at end
                } catch(FileNotFoundException ex) {
                    System.out.println(
                            "Unable to open file");
                } catch(IOException ex) {
                    System.out.println(
                            "Error reading file");
                }
            } else {
                infos[i] = "EMPTY";
            }
        }
        return infos;
    }


}

