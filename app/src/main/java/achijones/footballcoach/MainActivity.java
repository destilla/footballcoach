package achijones.footballcoach;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

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
                simLeague = new League(getString(R.string.league_player_names));
                season = 2015;
            } else if (saveFileStr.equals("DONE_RECRUITING")) {
                File saveFile = new File(getFilesDir(), "saveLeagueRecruiting.cfb");
                if (saveFile.exists()) {
                    simLeague = new League(saveFile, getString(R.string.league_player_names));
                    userTeam = simLeague.userTeam;
                    userTeamStr = userTeam.name;
                    userTeam.recruitPlayersFromStr(extras.getString("RECRUITS"));
                    userTeam.recruitWalkOns();
                    simLeague.updateTeamTalentRatings();
                    season = 2015 + userTeam.teamHistory.size();
                    currentTeam = userTeam;
                    loadedLeague = true;
                } else {
                    simLeague = new League(getString(R.string.league_player_names));
                    season = 2015;
                }
            } else {
                File saveFile = new File(getFilesDir(), saveFileStr);
                if (saveFile.exists()) {
                    simLeague = new League(saveFile, getString(R.string.league_player_names));
                    userTeam = simLeague.userTeam;
                    userTeamStr = userTeam.name;
                    simLeague.updateTeamTalentRatings();
                    season = 2015 + userTeam.teamHistory.size();
                    currentTeam = userTeam;
                    loadedLeague = true;
                } else {
                    simLeague = new League(getString(R.string.league_player_names));
                    season = 2015;
                }
            }
        } else {
            simLeague = new League(getString(R.string.league_player_names));
            season = 2015;
        }

        recruitingStage = -1;
        wantUpdateConf = 2; // 0 and 1, dont update, 2 update


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
        simGameButton.setBackgroundColor(Color.parseColor("#336699"));
        simGameButton.setTextColor(Color.parseColor("#ffffff"));
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
                        } else if (userTeam.gameWLSchedule.size() > numGamesPlayed) {
                            // Played a game, show summary
                            Toast.makeText(MainActivity.this, userTeam.weekSummaryStr(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        // Show notification for being invited/not invited to bowl or CCG
                        if (simLeague.currentWeek >= 12) {
                            if (!userTeam.gameSchedule.get(userTeam.gameSchedule.size() - 1).hasPlayed) {
                                Toast.makeText(MainActivity.this, "Congratulations! " + userTeam.name + " was invited to the " +
                                                userTeam.gameSchedule.get(userTeam.gameSchedule.size() - 1).gameName + "!",
                                        Toast.LENGTH_SHORT).show();
                            } else if (simLeague.currentWeek == 12) {
                                Toast.makeText(MainActivity.this, userTeam.name + " was not invited to the Conference Championship.",
                                        Toast.LENGTH_SHORT).show();
                            } else if (simLeague.currentWeek == 13) {
                                Toast.makeText(MainActivity.this, userTeam.name + " was not invited to a bowl game.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        updateCurrTeam();
                        scrollToLatestGame();

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
            examineTeam(userTeam.name);
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
             * Clicked League History in drop down menu
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
        } else if (id == R.id.action_bowlgame_watch) {
            /**
             * Clicked Bowl Game Watch in drop down menu
             */
            String bowlGameWatchStr = simLeague.getBowlGameWatchStr();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(bowlGameWatchStr)
                    .setTitle(getString(R.string.action_bowlgame_watch))
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
        } else if (id == R.id.action_ccg_watch) {
            /**
             * Clicked CCG Watch in drop down menu
             */
            String ccgWatchStr = simLeague.getCCGsStr();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(ccgWatchStr)
                    .setTitle(getString(R.string.action_ccg_watch))
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
            mainList.setSelection(simLeague.currentWeek-3);
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
        if (g.hasPlayed) {
            String[] gameStr = g.getGameSummaryStr();
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
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });

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
                Toast.makeText(MainActivity.this, "Saved league!",
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

