package CFBsimPack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.lang.StringBuilder;


/**
 * Class for conferences, which each have 10 teams.
 * @author Achi
 */
public class Conference {

    public String confName;
    public int confPrestige;

    public ArrayList<Team> confTeams;
    public boolean evenYear;

    public League league;

    private Game ccg;

    public int week;
    public int robinWeek;

    public String allConfStr;
    public ArrayList<Player> allConfPlayers;

    /**
     * Sets up Conference with empty list of teams.
     * @param name
     * @param league
     */
    public Conference( String name, League league ) {
        confName = name;
        confPrestige = 75;
        confTeams = new ArrayList<Team>();
        this.league = league;
        week = 0;
        robinWeek = 0;
        allConfPlayers = new ArrayList<Player>();
    }

    /**
     * Sets up schedule for in-conference games using round robin scheduling.
     */
    public void setUpSchedule() {
        //schedule in conf matchups
        robinWeek = 0;
        evenYear = (league.leagueHistory.size()%2==0);


        // Set up int arrays for each team's home/away rotation. Theoretically every year you should change off between having 5 home games and 4 away games in conference
        if (league.leagueHistory.size() == 0) {
            int[][] evenHomeGames = new int[10][];
            evenHomeGames[0] = new int[]{7, 4, 8, 3};
            evenHomeGames[1] = new int[]{8, 9, 5, 0, 4};
            evenHomeGames[2] = new int[]{5, 0, 6, 1};
            evenHomeGames[3] = new int[]{6, 1, 9, 7, 2};
            evenHomeGames[4] = new int[]{3, 7, 2, 8};
            evenHomeGames[5] = new int[]{4, 8, 3, 9, 0};
            evenHomeGames[6] = new int[]{4, 0, 1, 5};
            evenHomeGames[7] = new int[]{2, 6, 1, 5, 9};
            evenHomeGames[8] = new int[]{9, 3, 7, 2, 6};
            evenHomeGames[9] = new int[]{0, 2, 4, 6};



            for (int x = 0; x < evenHomeGames.length; x++) {
                StringBuilder sb = new StringBuilder();
                for (int y = 0; y < evenHomeGames[x].length; y++) {
                   sb.append(confTeams.get(evenHomeGames[x][y]).abbr + ",");
                }
                confTeams.get(x).evenYearHomeOpp = sb.toString();
            }
        }
        for (int r = 0; r < 9; ++r) {
            for (int g = 0; g < 5; ++g) {
                Team a = confTeams.get((robinWeek + g) % 9);
                Team b;
                if ( g == 0 ) {
                    b = confTeams.get(9);
                } else {
                    b = confTeams.get((9 - g + robinWeek) % 9);
                }


                Game gm;

                // Check whether it's an even year and if team B appears in team A's even year home game list, or if it's not an even year and team A appears in team B's list

                if ( (evenYear && a.evenYearHomeOpp.contains(b.abbr)) || (evenYear && !b.evenYearHomeOpp.contains(a.abbr)) || (!evenYear && !a.evenYearHomeOpp.contains((b.abbr))) || (!evenYear && b.evenYearHomeOpp.contains((a.abbr))) ) {
                    gm = new Game( a, b, "In Conf" );
                }

                // Basically check all the reverse scenarios above, anything that would cause B to be the home team.
                else if((evenYear && b.evenYearHomeOpp.contains(a.abbr) || (evenYear && !a.evenYearHomeOpp.contains(b.abbr)) || (!evenYear && a.evenYearHomeOpp.contains((b.abbr))) || (!evenYear && !b.evenYearHomeOpp.contains(a.abbr)))) {
                    gm = new Game( b, a, "In Conf" );
                }
                else{ // I'm 99.9% sure all scenarios and possibilities are covered above, but lets not break the game if I'm wrong
                    gm = new Game(b, a, "In Conf");
                }
                a.gameSchedule.add(gm);
                b.gameSchedule.add(gm);

                if (a.userControlled == true) System.out.println("User Controlled Home Schedule: " + a.evenYearHomeOpp);
            }
            robinWeek++;
        }


    }
    
    /**
     * Sets up schedule for OOC games, which are played in 3 weeks.
     */
    public void setUpOOCSchedule() {
        //schedule OOC games
        int confNum = -1;
        if ( "SOUTH".equals(confName) ) {
            confNum = 0;
        } else if ( "LAKES".equals(confName) ) {
            confNum = 1;
        } else if ( "NORTH".equals(confName) ) {
            confNum = 2;
        }
        
        if ( confNum != -1 ) {
            for ( int offsetOOC = 3; offsetOOC < 6; ++offsetOOC ) {
                ArrayList<Team> availTeams = new ArrayList<Team>();
                int selConf = confNum + offsetOOC;
                if (selConf == 6) selConf = 3;
                if (selConf == 7) selConf = 4;
                if (selConf == 8) selConf = 5;
                
                for (int i = 0; i < 10; ++i) {
                    availTeams.add( league.conferences.get(selConf).confTeams.get(i) );
                }
                
                for (int i = 0; i < 10; ++i) {
                    int selTeam = (int)( availTeams.size() * Math.random() );
                    Team a = confTeams.get(i);
                    Team b = availTeams.get( selTeam );
                    
                    Game gm;
                    if ( Math.random() > 0.5 ) {
                        gm = new Game( a, b, a.conference.substring(0,3) + " v " + b.conference.substring(0,3) );
                    } else {
                        gm = new Game( b, a, b.conference.substring(0,3) + " v " + a.conference.substring(0,3) );
                    }
                    
                    if ( offsetOOC == 3 ) {
                        a.gameOOCSchedule0 = gm;
                        b.gameOOCSchedule0 = gm;
                        availTeams.remove(selTeam);
                    } else if ( offsetOOC == 4 ) {
                        a.gameOOCSchedule4 = gm;
                        b.gameOOCSchedule4 = gm;
                        availTeams.remove(selTeam);
                    } else if ( offsetOOC == 5 ) {
                        a.gameOOCSchedule9 = gm;
                        b.gameOOCSchedule9 = gm;
                        availTeams.remove(selTeam);
                    }
                }
            }
        }
    }
    
    /**
     * Inserts the OOC games in the 1st, 5th, and 10th weeks.
     */
    public void insertOOCSchedule() {
        for (int i = 0; i < confTeams.size(); ++i) {
            confTeams.get(i).gameSchedule.add(0, confTeams.get(i).gameOOCSchedule0);
            confTeams.get(i).gameSchedule.add(4, confTeams.get(i).gameOOCSchedule4);
            confTeams.get(i).gameSchedule.add(9, confTeams.get(i).gameOOCSchedule9);
        }
    }
    
    /**
     * Plays week for each team. If CCG week, play the CCG.
     */
    public void playWeek() {
        if ( week == 12 ) {
            playConfChamp();
        } else {
            for ( int i = 0; i < confTeams.size(); ++i ) {
                confTeams.get(i).gameSchedule.get(week).playGame();
            }
            if ( week == 11 ) schedConfChamp();
            week++;
        } 
    }
    
    /**
     * Schedule the CCG based on team rankings.
     */
    public void schedConfChamp() {
        // Play CCG between top 2 teams
        for ( int i = 0; i < confTeams.size(); ++i ) {
            confTeams.get(i).updatePollScore();
        }
        Collections.sort( confTeams, new TeamCompConfWins() );

        int winsFirst = confTeams.get(0).getConfWins();
        Team t = confTeams.get(0);
        int i = 0;
        ArrayList<Team> teamTB = new ArrayList<>();
        while (t.getConfWins() == winsFirst) {
            teamTB.add(t);
            ++i;
            t = confTeams.get(i);
        }
        if (teamTB.size() > 2) {
            // ugh 3 way tiebreaker
            Collections.sort(teamTB, new TeamCompPoll());
            for (int j = 0; j < teamTB.size(); ++j) {
                confTeams.set(j, teamTB.get(j));
            }

        }

        int winsSecond = confTeams.get(1).getConfWins();
        t = confTeams.get(1);
        i = 1;
        teamTB.clear();
        while (t.getConfWins() == winsSecond) {
            teamTB.add(t);
            ++i;
            t = confTeams.get(i);
        }
        if (teamTB.size() > 2) {
            // ugh 3 way tiebreaker
            Collections.sort(teamTB, new TeamCompPoll());
            for (int j = 0; j < teamTB.size(); ++j) {
                confTeams.set(1+j, teamTB.get(j));
            }

        }

        ccg = new Game ( confTeams.get(0), confTeams.get(1), confName + " CCG" );
        confTeams.get(0).gameSchedule.add(ccg);
        confTeams.get(1).gameSchedule.add(ccg);
    }
    
    /**
     * Play the CCG. Add the "CC" tag to the winner.
     */
    public void playConfChamp() {
        // Play CCG between top 2 teams
        ccg.playGame();
        if ( ccg.homeScore > ccg.awayScore ) {
            confTeams.get(0).confChampion = "CC";
            confTeams.get(0).totalCCs++;
            confTeams.get(1).totalCCLosses++;
            league.newsStories.get(13).add(
                    ccg.homeTeam.name + " wins the " + confName +"!>" +
                    ccg.homeTeam.strRep() + " took care of business in the conference championship against " + ccg.awayTeam.strRep() +
                    ", winning at home with a score of " + ccg.homeScore + " to " + ccg.awayScore + "."
            );
        } else { 
            confTeams.get(1).confChampion = "CC"; 
            confTeams.get(1).totalCCs++;
            confTeams.get(0).totalCCLosses++;
            league.newsStories.get(13).add(
                    ccg.awayTeam.name + " wins the " + confName +"!>" +
                            ccg.awayTeam.strRep() + " surprised many in the conference championship against " + ccg.homeTeam.strRep() +
                            ", winning on the road with a score of " + ccg.awayScore + " to " + ccg.homeScore + "."
            );
        }
        Collections.sort(confTeams, new TeamCompPoll());
    }

    /**
     * String of who is playing in the CCG and the result if available.
     * @return conf champ summary
     */
    public String getCCGStr() {
        if (ccg == null) {
            // Give prediction, find top 2 teams
            Team team1 = null, team2 = null;
            int score1 = 0, score2 = 0;
            for (int i = confTeams.size()-1; i >= 0; --i) { //count backwards so higher ranked teams are predicted
                Team t = confTeams.get(i);
                if (t.getConfWins() >= score1) {
                    score2 = score1;
                    score1 = t.getConfWins();
                    team2 = team1;
                    team1 = t;
                } else if (t.getConfWins() > score2) {
                    score2 = t.getConfWins();
                    team2 = t;
                }
            }
            return confName + " Conference Championship:\n\t\t" +
                    team1.strRep() + " vs " + team2.strRep();
        } else {
            if (!ccg.hasPlayed) {
                return confName + " Conference Championship:\n\t\t" +
                        ccg.homeTeam.strRep() + " vs " + ccg.awayTeam.strRep();
            } else {
                StringBuilder sb = new StringBuilder();
                Team winner, loser;
                sb.append(confName + " Conference Championship:\n");
                if (ccg.homeScore > ccg.awayScore) {
                    winner = ccg.homeTeam;
                    loser = ccg.awayTeam;
                    sb.append(winner.strRep() + " W ");
                    sb.append(ccg.homeScore + "-" + ccg.awayScore + " ");
                    sb.append("vs " + loser.strRep());
                    return sb.toString();
                } else {
                    winner = ccg.awayTeam;
                    loser = ccg.homeTeam;
                    sb.append(winner.strRep() + " W ");
                    sb.append(ccg.awayScore + "-" + ccg.homeScore + " ");
                    sb.append("@ " + loser.strRep());
                    return sb.toString();
                }
            }
        }
    }

    /**
     * Get the allConfPlayers by sorting all the conf's players by their Heisman score
     * Should be only called after week 13
     */
    public ArrayList<Player> getAllConfPlayers() {
        if (allConfPlayers.isEmpty()) {
            ArrayList<PlayerQB> qbs = new ArrayList<>();
            ArrayList<PlayerRB> rbs = new ArrayList<>();
            ArrayList<PlayerWR> wrs = new ArrayList<>();

            for (Team t : confTeams) {
                qbs.add(t.getQB(0));
                rbs.add(t.getRB(0));
                rbs.add(t.getRB(1));
                wrs.add(t.getWR(0));
                wrs.add(t.getWR(1));
                wrs.add(t.getWR(2));
            }

            Collections.sort(qbs, new PlayerHeismanComp());
            Collections.sort(rbs, new PlayerHeismanComp());
            Collections.sort(wrs, new PlayerHeismanComp());

            allConfPlayers.add(qbs.get(0));
            allConfPlayers.add(rbs.get(0));
            allConfPlayers.add(rbs.get(1));
            allConfPlayers.add(wrs.get(0));
            allConfPlayers.add(wrs.get(1));
            allConfPlayers.add(wrs.get(2));
        }

        return allConfPlayers;
    }
   
}

class TeamCompConfWins implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        if (a.getConfWins() > b.getConfWins()) {
            return -1;
        } else if (a.getConfWins() == b.getConfWins()) {
            //check for h2h tiebreaker
            if (a.gameWinsAgainst.contains(b)) {
                return -1;
            } else if (b.gameWinsAgainst.contains(a)) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 1;
        }
    }
}
