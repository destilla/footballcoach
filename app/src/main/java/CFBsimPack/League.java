package CFBsimPack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * League class. Has 8 conferences.
 * @author Achi
 */
public class League {
    //Lists of conferences/teams
    public ArrayList<String[]> leagueHistory;
    public ArrayList<String> heismanHistory;
    public ArrayList<Conference> conferences;
    public ArrayList<Team> teamList;
    public ArrayList<String> nameList;
    public ArrayList< ArrayList<String> > newsStories;
    
    //Current week, 1-14
    public int currentWeek;
    
    //Bowl Games
    public boolean hasScheduledBowls;
    public Game semiG14;
    public Game semiG23;
    public Game ncg;
    public Game roseBowl;
    public Game sugarBowl;
    public Game orangeBowl;
    public Game peachBowl;
    public Game cottonBowl;
    public Game fiestaBowl;

    //User Team
    public Team userTeam;

    boolean heismanDecided;
    Player heisman;
    ArrayList<Player> heismanCandidates;
    
    /**
     * Creates League, sets up Conferences, reads team names and conferences from file.
     * Also schedules games for every team.
     */
    public League(String namesCSV) {
        heismanDecided = false;
        hasScheduledBowls = false;
        leagueHistory = new ArrayList<String[]>();
        heismanHistory = new ArrayList<String>();
        currentWeek = 0;
        conferences = new ArrayList<Conference>();
        conferences.add( new Conference("SEC", this) );
        conferences.add( new Conference("B10", this) );
        conferences.add( new Conference("PAC", this) );
        conferences.add( new Conference("BIG12", this) );
        conferences.add( new Conference("ACC", this) );
        conferences.add( new Conference("IVY", this) );

        // Initialize new stories lists
        newsStories = new ArrayList< ArrayList<String> >();
        for (int i = 0; i < 16; ++i) {
            newsStories.add( new ArrayList<String>() );
        }
        newsStories.get(0).add("New Season!>Ready for the new season, coach? Whether the National Championship is " +
                "on your mind, or just a winning season, good luck!");

        
        //read names from file
        nameList = new ArrayList<String>();
        String[] namesSplit = namesCSV.split(",");
        for (String n : namesSplit) {
            nameList.add(n.trim());
        }


        //Set up conferences
        //SEC
        conferences.get(0).confTeams.add( new Team( "Alabama", "ALA", "SEC", this, 95 ));
        conferences.get(0).confTeams.add( new Team( "Auburn", "AUB", "SEC", this, 90 ));
        conferences.get(0).confTeams.add( new Team( "Florida", "FL", "SEC", this, 85 ));
        conferences.get(0).confTeams.add( new Team( "Georgia", "GA", "SEC", this, 80 ));
        conferences.get(0).confTeams.add( new Team( "LSU", "LSU", "SEC", this, 75 ));
        conferences.get(0).confTeams.add( new Team( "Tennessee", "TEN", "SEC", this, 75 ));
        conferences.get(0).confTeams.add( new Team( "Texas A&M", "TAM", "SEC", this, 70 ));
        conferences.get(0).confTeams.add( new Team( "Missouri", "MSR", "SEC", this, 65 ));
        conferences.get(0).confTeams.add( new Team( "Arkansas", "ARK", "SEC", this, 65 ));
        conferences.get(0).confTeams.add( new Team( "Vanderbilt", "VAN", "SEC", this, 50 ));

        //Big 10
        conferences.get(1).confTeams.add( new Team( "Ohio State", "OHI", "B10", this, 90 ));
        conferences.get(1).confTeams.add( new Team( "Michigan", "MIC", "B10", this, 90 ));
        conferences.get(1).confTeams.add( new Team( "Michigan St", "MSU", "B10", this, 80 ));
        conferences.get(1).confTeams.add( new Team( "Wisconsin", "WIS", "B10", this, 70 ));
        conferences.get(1).confTeams.add( new Team( "Nebraska", "NEB", "B10", this, 70 ));
        conferences.get(1).confTeams.add( new Team( "Penn St", "PNS", "B10", this, 70 ));
        conferences.get(1).confTeams.add( new Team( "Minnesota", "MIN", "B10", this, 65 ));
        conferences.get(1).confTeams.add( new Team( "Iowa", "IOW", "B10", this, 65 ));
        conferences.get(1).confTeams.add( new Team( "Maryland", "MAR", "B10", this, 55 ));
        conferences.get(1).confTeams.add( new Team( "Purdue", "PUR", "B10", this, 45 ));

        //Pac12
        conferences.get(2).confTeams.add( new Team( "Southern Cal", "USC", "PAC", this, 90 ));
        conferences.get(2).confTeams.add( new Team( "Oregon", "ORE", "PAC", this, 85 ));
        conferences.get(2).confTeams.add( new Team( "Stanford", "STN", "PAC", this, 75 ));
        conferences.get(2).confTeams.add( new Team( "Los Angeles", "ULA", "PAC", this, 75 ));
        conferences.get(2).confTeams.add( new Team( "California", "CAL", "PAC", this, 70 ));
        conferences.get(2).confTeams.add( new Team( "Arizona", "ARI", "PAC", this, 70 ));
        conferences.get(2).confTeams.add( new Team( "Arizona St", "AST", "PAC", this, 65 ));
        conferences.get(2).confTeams.add( new Team( "Washington", "WSH", "PAC", this, 60 ));
        conferences.get(2).confTeams.add( new Team( "Utah", "UTH", "PAC", this, 50 ));
        conferences.get(2).confTeams.add( new Team( "Colorado", "COL", "PAC", this, 45 ));

        //Big 12
        conferences.get(3).confTeams.add( new Team( "Oklahoma", "OKL", "BIG12", this, 90 ));
        conferences.get(3).confTeams.add( new Team( "Texas", "TEX", "BIG12", this, 90 ));
        conferences.get(3).confTeams.add( new Team( "Baylor", "BAY", "BIG12", this, 80 ));
        conferences.get(3).confTeams.add( new Team( "TCU", "TCU", "BIG12", this, 80 ));
        conferences.get(3).confTeams.add( new Team( "Kansas St", "KNS", "BIG12", this, 70 ));
        conferences.get(3).confTeams.add( new Team( "West Virginia", "WVU", "BIG12", this, 70 ));
        conferences.get(3).confTeams.add( new Team( "Iowa St", "IOS", "BIG12", this, 60 ));
        conferences.get(3).confTeams.add( new Team( "Oklahoma St", "OKS", "BIG12", this, 60 ));
        conferences.get(3).confTeams.add( new Team( "Texas Tech", "TTU", "BIG12", this, 55 ));
        conferences.get(3).confTeams.add( new Team( "Kansas", "KAN", "BIG12", this, 50 ));

        //ACC
        conferences.get(4).confTeams.add( new Team( "Notre Dame", "ND", "ACC", this, 90 ));
        conferences.get(4).confTeams.add( new Team( "Miami", "MIA", "ACC", this, 85 ));
        conferences.get(4).confTeams.add( new Team( "Florida St", "FSU", "ACC", this, 80 ));
        conferences.get(4).confTeams.add( new Team( "Clemson", "CLE", "ACC", this, 75 ));
        conferences.get(4).confTeams.add( new Team( "North Carolina", "UNC", "ACC", this, 75 ));
        conferences.get(4).confTeams.add( new Team( "Georgia Tech", "GT", "ACC", this, 70 ));
        conferences.get(4).confTeams.add( new Team( "Duke", "DUKE", "ACC", this, 70 ));
        conferences.get(4).confTeams.add( new Team( "Louisville", "LOU", "ACC", this, 70 ));
        conferences.get(4).confTeams.add( new Team( "Virginia", "UVA", "ACC", this, 60 ));
        conferences.get(4).confTeams.add( new Team( "Boston College", "BC", "ACC", this, 50 ));

        //Ivy
        conferences.get(5).confTeams.add( new Team( "MIT", "MIT", "IVY", this, 80 ));
        conferences.get(5).confTeams.add( new Team( "Harvard", "HAR", "IVY", this, 75 ));
        conferences.get(5).confTeams.add( new Team( "Princeton", "PRN", "IVY", this, 75 ));
        conferences.get(5).confTeams.add( new Team( "Yale", "YAL", "IVY", this, 75 ));
        conferences.get(5).confTeams.add( new Team( "Oxford", "OXF", "IVY", this, 70 ));
        conferences.get(5).confTeams.add( new Team( "Columbia", "CLM", "IVY", this, 65 ));
        conferences.get(5).confTeams.add( new Team( "Cornell", "COR", "IVY", this, 60 ));
        conferences.get(5).confTeams.add( new Team( "Pennsylvania", "PEN", "IVY", this, 55 ));
        conferences.get(5).confTeams.add( new Team( "Brown", "BRN", "IVY", this, 50 ));
        conferences.get(5).confTeams.add( new Team( "Dartmouth", "DAR", "IVY", this, 45 ));

        //set teamList
        teamList = new ArrayList<Team>();
        for (int i = 0; i < conferences.size(); ++i ) {
            for (int j = 0; j < conferences.get(i).confTeams.size(); ++j ) {
                teamList.add( conferences.get(i).confTeams.get(j) );
            }
        }
        
        //set up schedule
        for (int i = 0; i < conferences.size(); ++i ) {
            conferences.get(i).setUpSchedule();
        }
        for (int i = 0; i < conferences.size(); ++i ) {
            conferences.get(i).setUpOOCSchedule();
        }
        for (int i = 0; i < conferences.size(); ++i ) {
            conferences.get(i).insertOOCSchedule();
        }

    }

    /**
     * Create League from saved file.
     * @param saveFile
     */
    public League(File saveFile, String namesCSV) {
        heismanDecided = false;
        hasScheduledBowls = false;
        // This will reference one line at a time
        String line = null;
        currentWeek = 0;
        try {
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader( new FileReader(saveFile) );

            //First ignore the save file info
            bufferedReader.readLine();

            //Next get league history
            leagueHistory = new ArrayList<String[]>();
            while((line = bufferedReader.readLine()) != null && !line.equals("END_LEAGUE_HIST")) {
                leagueHistory.add(line.split("%"));
            }

            //Next get heismans
            heismanHistory = new ArrayList<String>();
            while((line = bufferedReader.readLine()) != null && !line.equals("END_HEISMAN_HIST")) {
                heismanHistory.add(line);
            }

            //Next make all the teams
            conferences = new ArrayList<Conference>();
            teamList = new ArrayList<Team>();
            conferences.add( new Conference("SEC", this) );
            conferences.add( new Conference("B10", this) );
            conferences.add( new Conference("PAC", this) );
            conferences.add( new Conference("BIG12", this) );
            conferences.add( new Conference("ACC", this) );
            conferences.add( new Conference("IVY", this) );
            String[] splits;
            for(int i = 0; i < 60; ++i) { //Do for every team (60)
                StringBuilder sbTeam = new StringBuilder();
                while((line = bufferedReader.readLine()) != null && !line.equals("END_PLAYERS")) {
                    sbTeam.append(line);
                }
                Team t = new Team(sbTeam.toString(), this);
                conferences.get( getConfNumber(t.conference) ).confTeams.add(t);
                teamList.add(t);
            }

            //Set up user team
            if ((line = bufferedReader.readLine()) != null) {
                for (Team t : teamList) {
                    if (t.name.equals(line)) {
                        userTeam = t;
                        userTeam.userControlled = true;
                    }
                }
            }
            while((line = bufferedReader.readLine()) != null && !line.equals("END_USER_TEAM")) {
                userTeam.teamHistory.add(line);
            }

            // Always close files.
            bufferedReader.close();

            //read names from file
            nameList = new ArrayList<String>();
            String[] namesSplit = namesCSV.split(",");
            for (String n : namesSplit) {
                nameList.add(n.trim());
            }

            //set up schedule
            for (int i = 0; i < conferences.size(); ++i ) {
                conferences.get(i).setUpSchedule();
            }
            for (int i = 0; i < conferences.size(); ++i ) {
                conferences.get(i).setUpOOCSchedule();
            }
            for (int i = 0; i < conferences.size(); ++i ) {
                conferences.get(i).insertOOCSchedule();
            }

            // Initialize new stories lists
            newsStories = new ArrayList< ArrayList<String> >();
            for (int i = 0; i < 16; ++i) {
                newsStories.add(new ArrayList<String>());
            }
            newsStories.get(0).add("New Season!>Ready for the new season, coach? Whether the National Championship is " +
                    "on your mind, or just a winning season, good luck!");

        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file");
        }
    }

    public int getConfNumber(String conf) {
        if (conf.equals("SEC")) return 0;
        if (conf.equals("B10")) return 1;
        if (conf.equals("PAC")) return 2;
        if (conf.equals("BIG12")) return 3;
        if (conf.equals("ACC")) return 4;
        if (conf.equals("IVY")) return 5;
        return 0;
    }
    
    /**
     * Plays week. If normal week, handled by conferences. If bowl week, handled here.
     */
    public void playWeek() {
        if ( currentWeek <= 12 ) {
            for (int i = 0; i < conferences.size(); ++i) {
                conferences.get(i).playWeek();
            }
        } 
        
        if ( currentWeek == 12 ) {
            //bowl week
            for (int i = 0; i < teamList.size(); ++i) {
                teamList.get(i).updatePollScore();
            }
            Collections.sort( teamList, new TeamCompPoll() );
            
            schedBowlGames();
        } else if ( currentWeek == 13 ) {
            heismanHistory.add(getHeisman().get(0).position + " " + getHeisman().get(0).getInitialName() + " [" + getHeisman().get(0).getYrStr() + "], "
                    + getHeisman().get(0).team.abbr + " (" + getHeisman().get(0).team.wins + "-" + getHeisman().get(0).team.losses + ")");
            playBowlGames();
        } else if ( currentWeek == 14 ) {
            ncg.playGame();
            if ( ncg.homeScore > ncg.awayScore ) {
                ncg.homeTeam.semiFinalWL = "";
                ncg.awayTeam.semiFinalWL = "";
                ncg.homeTeam.natChampWL = "NCW";
                ncg.awayTeam.natChampWL = "NCL";
                ncg.homeTeam.totalNCs++;
                newsStories.get(15).add(
                        ncg.homeTeam.name + " wins the National Championship!>" +
                                ncg.homeTeam.strRep() + " defeats " + ncg.awayTeam.strRep() +
                                " in the national championship game " + ncg.homeScore + " to " + ncg.awayScore + "." +
                                " Congratulations " + ncg.homeTeam.name + "!"
                );

            } else {
                ncg.homeTeam.semiFinalWL = "";
                ncg.awayTeam.semiFinalWL = "";
                ncg.awayTeam.natChampWL = "NCW";
                ncg.homeTeam.natChampWL = "NCL";
                ncg.awayTeam.totalNCs++;
                newsStories.get(15).add(
                        ncg.awayTeam.name + " wins the National Championship!>" +
                                ncg.awayTeam.strRep() + " defeats " + ncg.homeTeam.strRep() +
                                " in the national championship game " + ncg.awayScore + " to " + ncg.homeScore + "." +
                                " Congratulations " + ncg.awayTeam.name + "!"
                );
            }
        }

        setTeamRanks();
        currentWeek++;
    }
    
    /**
     * Schedules bowl games based on team rankings.
     */
    public void schedBowlGames() {
        //bowl week
        for (int i = 0; i < teamList.size(); ++i) {
            teamList.get(i).updatePollScore();
        }
        Collections.sort( teamList, new TeamCompPoll() );

        //semifinals
        semiG14 = new Game( teamList.get(0), teamList.get(3), "Semis, 1v4" );
        teamList.get(0).gameSchedule.add(semiG14);
        teamList.get(3).gameSchedule.add(semiG14);

        semiG23 = new Game( teamList.get(1), teamList.get(2), "Semis, 2v3" );
        teamList.get(1).gameSchedule.add(semiG23);
        teamList.get(2).gameSchedule.add(semiG23);
        
        //other bowl games
        roseBowl = new Game( teamList.get(4), teamList.get(6), "Rose Bowl" );
        teamList.get(4).gameSchedule.add(roseBowl);
        teamList.get(6).gameSchedule.add(roseBowl);
        sugarBowl = new Game( teamList.get(5), teamList.get(7), "Sugar Bowl" );
        teamList.get(5).gameSchedule.add(sugarBowl);
        teamList.get(7).gameSchedule.add(sugarBowl);
        orangeBowl = new Game( teamList.get(8), teamList.get(14), "Orange Bowl" );
        teamList.get(8).gameSchedule.add(orangeBowl);
        teamList.get(14).gameSchedule.add(orangeBowl);
        peachBowl = new Game( teamList.get(9), teamList.get(15), "Peach Bowl" );
        teamList.get(9).gameSchedule.add(peachBowl);
        teamList.get(15).gameSchedule.add(peachBowl);
        cottonBowl = new Game( teamList.get(10), teamList.get(11), "Cotton Bowl" );
        teamList.get(10).gameSchedule.add(cottonBowl);
        teamList.get(11).gameSchedule.add(cottonBowl);
        fiestaBowl = new Game( teamList.get(12), teamList.get(13), "Fiesta Bowl" );
        teamList.get(12).gameSchedule.add(fiestaBowl);
        teamList.get(13).gameSchedule.add(fiestaBowl);

        hasScheduledBowls = true;
          
    }
    
    /**
     * Actually plays each bowl game.
     */
    public void playBowlGames() {
        Game[] bowls = {roseBowl, sugarBowl, orangeBowl, peachBowl, cottonBowl, fiestaBowl};
        for (Game g : bowls) {
            playBowl(g);
        }
        
        semiG14.playGame();
        semiG23.playGame();
        Team semi14winner;
        Team semi23winner;
        if ( semiG14.homeScore > semiG14.awayScore ) {
            semiG14.homeTeam.semiFinalWL = "SFW";
            semiG14.awayTeam.semiFinalWL = "SFL";
            semi14winner = semiG14.homeTeam;
            newsStories.get(14).add(
                    semiG14.homeTeam.name + " wins the " + semiG14.gameName +"!>" +
                            semiG14.homeTeam.strRep() + " defeats " + semiG14.awayTeam.strRep() +
                            " in the semifinals, winning " + semiG14.homeScore + " to " + semiG14.awayScore + ". " +
                            semiG14.homeTeam.name + " advances to the National Championship!"

            );
        } else {
            semiG14.homeTeam.semiFinalWL = "SFL";
            semiG14.awayTeam.semiFinalWL = "SFW";
            semi14winner = semiG14.awayTeam;
            newsStories.get(14).add(
                    semiG14.awayTeam.name + " wins the " + semiG14.gameName +"!>" +
                            semiG14.awayTeam.strRep() + " defeats " + semiG14.homeTeam.strRep() +
                            " in the semifinals, winning " + semiG14.awayScore + " to " + semiG14.homeScore + ". " +
                            semiG14.awayTeam.name + " advances to the National Championship!"

            );
        }
        if ( semiG23.homeScore > semiG23.awayScore ) {
            semiG23.homeTeam.semiFinalWL = "SFW";
            semiG23.awayTeam.semiFinalWL = "SFL";
            semi23winner = semiG23.homeTeam;
            newsStories.get(14).add(
                    semiG23.homeTeam.name + " wins the " + semiG23.gameName +"!>" +
                            semiG23.homeTeam.strRep() + " defeats " + semiG23.awayTeam.strRep() +
                            " in the semifinals, winning " + semiG23.homeScore + " to " + semiG23.awayScore + ". " +
                            semiG23.homeTeam.name + " advances to the National Championship!"

            );
        } else {
            semiG23.homeTeam.semiFinalWL = "SFL";
            semiG23.awayTeam.semiFinalWL = "SFW";
            semi23winner = semiG23.awayTeam;
            newsStories.get(14).add(
                    semiG23.awayTeam.name + " wins the " + semiG23.gameName +"!>" +
                            semiG23.awayTeam.strRep() + " defeats " + semiG23.homeTeam.strRep() +
                            " in the semifinals, winning " + semiG23.awayScore + " to " + semiG23.homeScore + ". " +
                            semiG23.awayTeam.name + " advances to the National Championship!"

            );
        }
        
        //schedule NCG
        ncg = new Game( semi14winner, semi23winner, "NCG" );
        semi14winner.gameSchedule.add( ncg );
        semi23winner.gameSchedule.add( ncg );
        
    }

    private void playBowl(Game g) {
        g.playGame();
        if ( g.homeScore > g.awayScore ) {
            g.homeTeam.semiFinalWL = "BW";
            g.awayTeam.semiFinalWL = "BL";
            newsStories.get(14).add(
                    g.homeTeam.name + " wins the " + g.gameName +"!>" +
                    g.homeTeam.strRep() + " defeats " + g.awayTeam.strRep() +
                    " in the " + g.gameName + ", winning " + g.homeScore + " to " + g.awayScore + "."
            );
        } else {
            g.homeTeam.semiFinalWL = "BL";
            g.awayTeam.semiFinalWL = "BW";
            newsStories.get(14).add(
                    g.awayTeam.name + " wins the " + g.gameName +"!>" +
                            g.awayTeam.strRep() + " defeats " + g.homeTeam.strRep() +
                            " in the " + g.gameName + ", winning " + g.awayScore + " to " + g.homeScore + "."
            );
        }
    }
    
    /**
     * At the end of the year, record the top 10 teams for the League's History.
     */
    public void updateLeagueHistory() {
        //update league history
        for (int i = 0; i < teamList.size(); ++i) {
            teamList.get(i).updatePollScore();
        }
        Collections.sort( teamList, new TeamCompPoll() );
        String[] yearTop10 = new String[10];
        Team tt;
        for (int i = 0; i < 10; ++i) {
            tt = teamList.get(i);
            yearTop10[i] = tt.abbr + " (" + tt.wins + "-" + tt.losses + ")";
        }
        leagueHistory.add(yearTop10);
    }
    
    /**
     * Advances season for each team and sets up schedules for the new year.
     */
    public void advanceSeason() {
        currentWeek = 0;
        //updateTeamHistories();
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).advanceSeason();
        }
        for (int c = 0; c < conferences.size(); ++c) {
            conferences.get(c).robinWeek = 0;
            conferences.get(c).week = 0;
        }
        //set up schedule
        for (int i = 0; i < conferences.size(); ++i ) {
            conferences.get(i).setUpSchedule();
        }
        for (int i = 0; i < conferences.size(); ++i ) {
            conferences.get(i).setUpOOCSchedule();
        }
        for (int i = 0; i < conferences.size(); ++i ) {
            conferences.get(i).insertOOCSchedule();
        }

        hasScheduledBowls = false;
    }
    
    /**
     * Updates team history for each team.
     */
    public void updateTeamHistories() {
        for ( int i = 0; i < teamList.size(); ++i ) {
            teamList.get(i).updateTeamHistory();
        }
    }

    /**
     * Update all teams off talent, def talent, etc
     */
    public void updateTeamTalentRatings() {
        for (Team t : teamList) {
            t.updateTalentRatings();
        }
    }
    
    /**
     * Gets a random player name.
     * @return random name
     */
    public String getRandName() {
        int fn = (int)(Math.random()*nameList.size());
        int ln = (int)(Math.random()*nameList.size());
        return nameList.get(fn) + " " + nameList.get(ln);
    }
    
    /**
     * Updates poll scores for each team and updates their ranking.
     */
    public void setTeamRanks() {
        //get team ranks for PPG, YPG, etc
        for (int i = 0; i < teamList.size(); ++i) {
            teamList.get(i).updatePollScore();
        }
        
        Collections.sort( teamList, new TeamCompPoll() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamPollScore = t+1;
        }
        
        Collections.sort( teamList, new TeamCompSoW() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamStrengthOfWins = t+1;
        }
        
        Collections.sort( teamList, new TeamCompPPG() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamPoints = t+1;
        }
        
        Collections.sort( teamList, new TeamCompOPPG() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOppPoints = t+1;
        }
        
        Collections.sort( teamList, new TeamCompYPG() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamYards = t+1;
        }
        
        Collections.sort( teamList, new TeamCompOYPG() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOppYards = t+1;
        }
        
        Collections.sort( teamList, new TeamCompPYPG() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamPassYards = t+1;
        }
        
        Collections.sort( teamList, new TeamCompRYPG() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamRushYards = t+1;
        }

        Collections.sort( teamList, new TeamCompTODiff() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamTODiff= t+1;
        }
        
        Collections.sort( teamList, new TeamCompOffTalent() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOffTalent = t+1;
        }
        
        Collections.sort( teamList, new TeamCompDefTalent() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamDefTalent = t+1;
        }
        
        Collections.sort( teamList, new TeamCompPrestige() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamPrestige = t+1;
        }
        
    }
    
    /**
     * Calculates who wins the Heisman.
     * @return Heisman Winner
     */
    public ArrayList<Player> getHeisman() {
        heisman = null;
        int heismanScore = 0;
        int tempScore = 0;
        ArrayList<Player> heismanCandidates = new ArrayList<Player>();
        for ( int i = 0; i < teamList.size(); ++i ) {
            //qb
            heismanCandidates.add( teamList.get(i).teamQBs.get(0) );
            tempScore = teamList.get(i).teamQBs.get(0).getHeismanScore() + teamList.get(i).wins*100;
            if ( tempScore > heismanScore ) {
                heisman = teamList.get(i).teamQBs.get(0);
                heismanScore = tempScore;
            }
            
            //rb
            for (int rb = 0; rb < 2; ++rb) {
                heismanCandidates.add( teamList.get(i).teamRBs.get(rb) );
                tempScore = teamList.get(i).teamRBs.get(rb).getHeismanScore() + teamList.get(i).wins*100;
                if ( tempScore > heismanScore ) {
                    heisman = teamList.get(i).teamRBs.get(rb);
                    heismanScore = tempScore;
                }
            }
            
            //wr
            for (int wr = 0; wr < 3; ++wr) {
                heismanCandidates.add( teamList.get(i).teamWRs.get(wr) );
                tempScore = teamList.get(i).teamWRs.get(wr).getHeismanScore() + teamList.get(i).wins*100;
                if ( tempScore > heismanScore ) {
                    heisman = teamList.get(i).teamWRs.get(wr);
                    heismanScore = tempScore;
                }
            }
        }
        Collections.sort( heismanCandidates, new PlayerHeismanComp() );
        
        return heismanCandidates;
    }

    public String getTop5HeismanStr() {
        if (heismanDecided) {
            return getHeismanCeremonyStr();
        } else {
            ArrayList<Player> heismanCandidates = getHeisman();
            //full results string
            String heismanTop5 = "";
            for (int i = 0; i < 5; ++i) {
                Player p = heismanCandidates.get(i);
                heismanTop5 += (i + 1) + ". " + p.team.abbr + "(" + p.team.wins + "-" + p.team.losses + ")" + " - ";
                if (p instanceof PlayerQB) {
                    PlayerQB pqb = (PlayerQB) p;
                    heismanTop5 += " QB " + pqb.name + " [" + pqb.getYrStr() +
                            "]\n \t\t(" + pqb.statsTD + " TDs, " + pqb.statsInt + " Int, " + pqb.statsPassYards + " Yds)\n\n";
                } else if (p instanceof PlayerRB) {
                    PlayerRB prb = (PlayerRB) p;
                    heismanTop5 += " RB " + prb.name + " [" + prb.getYrStr() +
                            "]\n \t\t(" + prb.statsTD + " TDs, " + prb.statsFumbles + " Fum, " + prb.statsRushYards + " Yds)\n\n";
                } else if (p instanceof PlayerWR) {
                    PlayerWR pwr = (PlayerWR) p;
                    heismanTop5 += " WR " + pwr.name + " [" + pwr.getYrStr() +
                            "]\n \t\t(" + pwr.statsTD + " TDs, " + pwr.statsFumbles + " Fum, " + pwr.statsRecYards + " Yds)\n\n";
                }
            }
            return heismanTop5;
        }
    }

    public String getHeismanCeremonyStr() {
        boolean putNewsStory = false;
        if (!heismanDecided) {
            heismanDecided = true;
            heismanCandidates = getHeisman();
            heisman = heismanCandidates.get(0);
            putNewsStory = true;
        }
        //full results string
        String heismanTop5 = "\n";
        for (int i = 0; i < 5; ++i) {
            Player p = heismanCandidates.get(i);
            heismanTop5 += (i+1) + ". " + p.team.abbr + "(" + p.team.wins + "-" + p.team.losses + ")" + " - ";
            if ( p instanceof PlayerQB ) {
                PlayerQB pqb = (PlayerQB) p;
                heismanTop5 += " QB " + pqb.getInitialName() + ": " + p.getHeismanScore() + " votes\n\t("
                        +  pqb.statsTD + " TDs, " + pqb.statsInt + " Int, " + pqb.statsPassYards + " Yds)\n";
            } else if ( p instanceof PlayerRB ) {
                PlayerRB prb = (PlayerRB) p;
                heismanTop5 += " RB " + prb.getInitialName()+ ": " + p.getHeismanScore() + " votes\n\t("
                        +  prb.statsTD + " TDs, " + prb.statsFumbles + " Fum, " + prb.statsRushYards + " Yds)\n";
            } else if ( p instanceof PlayerWR ) {
                PlayerWR pwr = (PlayerWR) p;
                heismanTop5 += " WR " + pwr.getInitialName() + ": " + p.getHeismanScore() + " votes\n\t("
                        +  pwr.statsTD + " TDs, " + pwr.statsFumbles + " Fum, " + pwr.statsRecYards + " Yds)\n";
            }
        }
        String heismanStats = "";
        String heismanWinnerStr = "";
        if ( heisman instanceof PlayerQB ) {
            //qb heisman
            PlayerQB heisQB = (PlayerQB) heisman;
            heismanWinnerStr = "Congratulations to the Heisman Trophy Winner, " + heisQB.team.abbr +
                    " QB " + heisQB.name + " [" + heisman.getYrStr() + "], who had " +
                    heisQB.statsTD + " TDs, just " + heisQB.statsInt + " interceptions, and " +
                    heisQB.statsPassYards + " passing yards. He led " + heisQB.team.name +
                    " to a " + heisQB.team.wins + "-" + heisQB.team.losses + " record and a #" + heisQB.team.rankTeamPollScore +
                    " poll ranking.";
            heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
        } else if ( heisman instanceof PlayerRB ) {
            //rb heisman
            PlayerRB heisRB = (PlayerRB) heisman;
            heismanWinnerStr = "Congratulations to the Heisman Trophy Winner, " + heisRB.team.abbr +
                    " RB " + heisRB.name + " [" + heisman.getYrStr() + "], who had " +
                    heisRB.statsTD + " TDs, just " + heisRB.statsFumbles + " fumbles, and " +
                    heisRB.statsRushYards + " rushing yards. He led " + heisRB.team.name +
                    " to a " + heisRB.team.wins + "-" + heisRB.team.losses + " record and a #" + heisRB.team.rankTeamPollScore +
                    " poll ranking.";
            heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
        } else if ( heisman instanceof PlayerWR ) {
            //wr heisman
            PlayerWR heisWR = (PlayerWR) heisman;
            heismanWinnerStr = "Congratulations to the Heisman Trophy Winner, " + heisWR.team.abbr +
                    " WR " + heisWR.name + " [" + heisman.getYrStr() + "], who had " +
                    heisWR.statsTD + " TDs, just " + heisWR.statsFumbles + " fumbles, and " +
                    heisWR.statsRecYards + " receiving yards. He led " + heisWR.team.name +
                    " to a " + heisWR.team.wins + "-" + heisWR.team.losses + " record and a #" + heisWR.team.rankTeamPollScore +
                    " poll ranking.";
            heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
        }

        // Add news story
        if (putNewsStory) {
            newsStories.get(13).add(heisman.name+" wins the Heisman!>" + heismanWinnerStr);
        }

        return heismanStats;
    }

    public ArrayList<String> getTeamRankingsStr(int selection) {
        /*
        0 = poll score
        1 = sos
        2 = points
        3 = opp points
        4 = yards
        5 = opp yards
        6 = pass yards
        7 = rush yards
        8 = TO diff
        9 = off talent
        10 = def talent
        11 = prestige
         */
        ArrayList<Team> teams = teamList; //(ArrayList<Team>) teamList.clone();
        ArrayList<String> rankings = new ArrayList<String>();
        Team t;
        switch (selection) {
            case 0: Collections.sort( teams, new TeamCompPoll() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + t.teamPollScore);
                }
                break;
            case 1: Collections.sort( teams, new TeamCompSoW() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.teamStrengthOfWins);
                }
                break;
            case 2: Collections.sort( teams, new TeamCompPPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamPoints/t.numGames()));
                }
                break;
            case 3: Collections.sort( teams, new TeamCompOPPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamOppPoints/t.numGames()));
                }
                break;
            case 4: Collections.sort( teams, new TeamCompYPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamYards/t.numGames()));
                }
                break;
            case 5: Collections.sort( teams, new TeamCompOYPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamOppYards/t.numGames()));
                }
                break;
            case 6: Collections.sort( teams, new TeamCompPYPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamPassYards/t.numGames()));
                }
                break;
            case 7: Collections.sort( teams, new TeamCompRYPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamRushYards/t.numGames()));
                }
                break;
            case 8: Collections.sort( teams, new TeamCompTODiff() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    if (t.teamTODiff > 0) rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + ",+" + t.teamTODiff);
                    else rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.teamTODiff);
                }
                break;
            case 9: Collections.sort( teams, new TeamCompOffTalent() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.teamOffTalent);
                }
                break;
            case 10: Collections.sort( teams, new TeamCompDefTalent() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.teamDefTalent);
                }
                break;
            case 11: Collections.sort( teams, new TeamCompPrestige() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + t.teamPrestige);
                }
                break;
            default: Collections.sort( teams, new TeamCompPoll() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.teamPollScore);
                }
                break;
        }

        return rankings;
    }

    public String getLeagueHistoryStr() {
        String hist = "";
        for (int i = 0; i < leagueHistory.size(); ++i) {
            hist += (2015+i) + ":\n";
            hist += "\tChampions: " + leagueHistory.get(i)[0] + "\n";
            hist += "\tHeis: " + heismanHistory.get(i) + "\n";
        }
        return hist;
    }

    public String[] getTeamListStr() {
        String[] teams = new String[teamList.size()];
        for (int i = 0; i < teamList.size(); ++i){
            teams[i] = teamList.get(i).conference + ": " +
                    teamList.get(i).name + ", Prestige: " + teamList.get(i).teamPrestige;
        }
        return teams;
    }

    /**
     * Get list of all bowl games and their predicted teams
     * @return
     */
    public String getBowlGameWatchStr() {
        //if bowls arent scheduled yet, give predictions
        if (!hasScheduledBowls) {

            for (int i = 0; i < teamList.size(); ++i) {
                teamList.get(i).updatePollScore();
            }
            Collections.sort(teamList, new TeamCompPoll());

            StringBuilder sb = new StringBuilder();
            sb.append("Bowl Game Forecast:\n\n");
            Team t1;
            Team t2;

            sb.append("Semifinal 1v4:\n\t\t");
            t1 = teamList.get(0);
            t2 = teamList.get(3);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append("Semifinal 2v3:\n\t\t");
            t1 = teamList.get(1);
            t2 = teamList.get(2);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append("Rose Bowl:\n\t\t");
            t1 = teamList.get(4);
            t2 = teamList.get(6);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append("Sugar Bowl:\n\t\t");
            t1 = teamList.get(5);
            t2 = teamList.get(7);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append("Orange Bowl:\n\t\t");
            t1 = teamList.get(8);
            t2 = teamList.get(14);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append("Peach Bowl:\n\t\t");
            t1 = teamList.get(9);
            t2 = teamList.get(15);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append("Cotton Bowl:\n\t\t");
            t1 = teamList.get(10);
            t2 = teamList.get(11);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append("Fiesta Bowl:\n\t\t");
            t1 = teamList.get(12);
            t2 = teamList.get(13);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            return sb.toString();

        } else {
            // Games have already been scheduled, give actual teams
            StringBuilder sb = new StringBuilder();
            sb.append("Bowl Game Results:\n\n");

            sb.append("Semifinal 1v4:\n");
            sb.append(getGameSummaryBowl(semiG14));

            sb.append("\n\nSemifinal 2v3:\n");
            sb.append(getGameSummaryBowl(semiG23));

            sb.append("\n\nRose Bowl:\n");
            sb.append(getGameSummaryBowl(roseBowl));

            sb.append("\n\nSugar Bowl:\n");
            sb.append(getGameSummaryBowl(sugarBowl));

            sb.append("\n\nOrange Bowl:\n");
            sb.append(getGameSummaryBowl(orangeBowl));

            sb.append("\n\nPeach Bowl:\n");
            sb.append(getGameSummaryBowl(peachBowl));

            sb.append("\n\nCotton Bowl:\n");
            sb.append(getGameSummaryBowl(cottonBowl));

            sb.append("\n\nFiesta Bowl:\n");
            sb.append(getGameSummaryBowl(fiestaBowl));

            return sb.toString();
        }
    }

    public String getGameSummaryBowl(Game g) {
        StringBuilder sb = new StringBuilder();
        Team winner, loser;
        if (!g.hasPlayed) {
            return g.homeTeam.strRep() + " vs " + g.awayTeam.strRep();
        } else {
            if (g.homeScore > g.awayScore) {
                winner = g.homeTeam;
                loser = g.awayTeam;
                sb.append(winner.strRep() + " W ");
                sb.append(g.homeScore + "-" + g.awayScore + " ");
                sb.append("vs " + loser.strRep());
                return sb.toString();
            } else {
                winner = g.awayTeam;
                loser = g.homeTeam;
                sb.append(winner.strRep() + " W ");
                sb.append(g.awayScore + "-" + g.homeScore + " ");
                sb.append("@ " + loser.strRep());
                return sb.toString();
            }
        }
    }

    public String getCCGsStr() {
        StringBuilder sb = new StringBuilder();
        for (Conference c : conferences) {
            sb.append(c.getCCGStr()+"\n\n");
        }
        return sb.toString();
    }

    public Team findTeam(String name) {
        for (int i = 0; i < teamList.size(); i++){
            if (teamList.get(i).strRep().equals(name)) {
                return teamList.get(i);
            }
        }
        return teamList.get(0);
    }

    public Conference findConference(String name) {
        for (int i = 0; i < teamList.size(); i++){
            if (conferences.get(i).confName.equals(name)) {
                return conferences.get(i);
            }
        }
        return conferences.get(0);
    }

    public String ncgSummaryStr() {
        // Give summary of what happened in the NCG
        if (ncg.homeScore > ncg.awayScore) {
            return ncg.homeTeam.name + " (" + ncg.homeTeam.wins + "-" + ncg.homeTeam.losses + ") won the National Championship, " +
                    "winning against " + ncg.awayTeam.name + " (" + ncg.awayTeam.wins + "-" + ncg.awayTeam.losses + ") in the NCG " +
                    ncg.homeScore + "-" + ncg.awayScore + ".";
        } else {
            return ncg.awayTeam.name + " (" + ncg.awayTeam.wins + "-" + ncg.awayTeam.losses + ") won the National Championship, " +
                    "winning against " + ncg.homeTeam.name + " (" + ncg.homeTeam.wins + "-" + ncg.homeTeam.losses + ") in the NCG " +
                    ncg.awayScore + "-" + ncg.homeScore + ".";
        }
    }

    public String seasonSummaryStr() {
        StringBuilder sb = new StringBuilder();
        sb.append(ncgSummaryStr());
        sb.append("\n\n" + userTeam.seasonSummaryStr());
        return sb.toString();
    }

    /**
     * Save League in a file.
     * @param saveFile
     * @return
     */
    public boolean saveLeague(File saveFile) {
        StringBuilder sb = new StringBuilder();
        sb.append((2015+leagueHistory.size())+": " + userTeam.abbr + " (" + userTeam.totalWins + "-" + userTeam.totalLosses + ") " +
                    userTeam.totalCCs + " CCs, " + userTeam.totalNCs + " NCs%\n");

        for (int i = 0; i < leagueHistory.size(); ++i) {
            for (int j = 0; j < leagueHistory.get(i).length; ++j) {
                sb.append(leagueHistory.get(i)[j] + "%");
            }
            sb.append("\n");
        }
        sb.append("END_LEAGUE_HIST\n");

        for (int i = 0; i < heismanHistory.size(); ++i) {
            sb.append(heismanHistory.get(i) + "\n");
        }
        sb.append("END_HEISMAN_HIST\n");

        for (Team t : teamList) {
            sb.append(t.conference + "," + t.name + "," + t.abbr + "," + t.teamPrestige + "," + t.totalWins + "," + t.totalLosses + "," + t.totalCCs + "," + t.totalNCs + "%\n");
            sb.append(t.getPlayerInfoSaveFile());
            sb.append("END_PLAYERS\n");
        }

        sb.append(userTeam.name + "\n");
        for (String s : userTeam.teamHistory) {
            sb.append(s + "\n");
        }
        sb.append("END_USER_TEAM\n");

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(saveFile), "utf-8"))) {
            writer.write(sb.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

class PlayerHeismanComp implements Comparator<Player> {
    @Override
    public int compare( Player a, Player b ) {
        return a.getHeismanScore() > b.getHeismanScore() ? -1 : a.getHeismanScore() == b.getHeismanScore() ? 0 : 1;
    }
}

class TeamCompPoll implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamPollScore > b.teamPollScore ? -1 : a.teamPollScore == b.teamPollScore ? 0 : 1;
    }
}

class TeamCompSoW implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamStrengthOfWins > b.teamStrengthOfWins ? -1 : a.teamStrengthOfWins == b.teamStrengthOfWins ? 0 : 1;
    }
}

class TeamCompPPG implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamPoints/a.numGames() > b.teamPoints/b.numGames() ? -1 : a.teamPoints/a.numGames() == b.teamPoints/b.numGames() ? 0 : 1;
    }
}

class TeamCompOPPG implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamOppPoints/a.numGames() < b.teamOppPoints/b.numGames() ? -1 : a.teamOppPoints/a.numGames() == b.teamOppPoints/b.numGames() ? 0 : 1;
    }
}

class TeamCompYPG implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamYards/a.numGames() > b.teamYards/b.numGames() ? -1 : a.teamYards/a.numGames() == b.teamYards/b.numGames() ? 0 : 1;
    }
}

class TeamCompOYPG implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamOppYards/a.numGames() < b.teamOppYards/b.numGames() ? -1 : a.teamOppYards/a.numGames() == b.teamOppYards/b.numGames() ? 0 : 1;
    }
}

class TeamCompPYPG implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamPassYards/a.numGames() > b.teamPassYards/b.numGames() ? -1 : a.teamPassYards/a.numGames() == b.teamPassYards/b.numGames() ? 0 : 1;
    }
}

class TeamCompRYPG implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamRushYards/a.numGames() > b.teamRushYards/b.numGames() ? -1 : a.teamRushYards/a.numGames() == b.teamRushYards/b.numGames() ? 0 : 1;
    }
}

class TeamCompTODiff implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamTODiff > b.teamTODiff ? -1 : a.teamTODiff == b.teamTODiff ? 0 : 1;
    }
}

class TeamCompOffTalent implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamOffTalent > b.teamOffTalent ? -1 : a.teamOffTalent == b.teamOffTalent ? 0 : 1;
    }
}

class TeamCompDefTalent implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamDefTalent > b.teamDefTalent ? -1 : a.teamDefTalent == b.teamDefTalent ? 0 : 1;
    }
}

class TeamCompPrestige implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamPrestige > b.teamPrestige ? -1 : a.teamPrestige == b.teamPrestige ? 0 : 1;
    }
}






