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
 * League class. Has 6 conferences of 10 teams each.
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

    public LeagueRecords leagueRecords;
    public TeamStreak longestWinStreak;

    public ArrayList<String> newsBless;
    public ArrayList<String> newsCurse;
    public Team saveBless;
    public Team saveCurse;


    //Current week, 1-14
    public int currentWeek;
    
    //Bowl Games
    public boolean hasScheduledBowls;
    public Game semiG14;
    public Game semiG23;
    public Game ncg;
    public Game[] bowlGames;

    //User Team
    public Team userTeam;

    boolean heismanDecided;
    Player heisman;
    ArrayList<Player> heismanCandidates;
    private String heismanWinnerStrFull;

    ArrayList<Player> allAmericans;		
    private String allAmericanStr;

    public String[] bowlNames = {"Lilac Bowl", "Apple Bowl", "Salty Bowl", "Salsa Bowl", "Mango Bowl",
            "Patriot Bowl", "Salad Bowl", "Frost Bowl", "Tropical Bowl", "I'd Rather Bowl"};
    
    /**
     * Creates League, sets up Conferences, reads team names and conferences from file.
     * Also schedules games for every team.
     */
    public League(String namesCSV) {
        heismanDecided = false;
        hasScheduledBowls = false;
        bowlGames = new Game[10];
        leagueHistory = new ArrayList<String[]>();
        heismanHistory = new ArrayList<String>();
        currentWeek = 0;
        conferences = new ArrayList<Conference>();
        conferences.add( new Conference("SOUTH", this) );
        conferences.add( new Conference("LAKES", this) );
        conferences.add( new Conference("NORTH", this) );
        conferences.add( new Conference("COWBY", this) );
        conferences.add( new Conference("PACIF", this) );
        conferences.add( new Conference("MOUNT", this) );
        allAmericans = new ArrayList<Player>();
        
        // Initialize new stories lists
        newsStories = new ArrayList< ArrayList<String> >();
        for (int i = 0; i < 16; ++i) {
            newsStories.add( new ArrayList<String>() );
        }
        newsStories.get(0).add("New Season!>Ready for the new season, coach? Whether the National Championship is " +
                "on your mind, or just a winning season, good luck!");

        leagueRecords = new LeagueRecords();
        longestWinStreak = new TeamStreak(getYear(), getYear(), 0, null);

        //read names from file
        nameList = new ArrayList<String>();
        String[] namesSplit = namesCSV.split(",");
        for (String n : namesSplit) {
            nameList.add(n.trim());
        }


        //Set up conferences
        //SOUTH
        conferences.get(0).confTeams.add( new Team( "Alabama", "ALA", "SOUTH", this, 95, "GEO" ));
        conferences.get(0).confTeams.add( new Team( "Georgia", "GEO", "SOUTH", this, 90, "ALA" ));
        conferences.get(0).confTeams.add( new Team( "Florida", "FLA", "SOUTH", this, 85, "TEN" ));
        conferences.get(0).confTeams.add( new Team( "Tennessee", "TEN", "SOUTH", this, 80, "FLA" ));
        conferences.get(0).confTeams.add( new Team( "Atlanta", "ATL", "SOUTH", this, 75, "KYW" ));
        conferences.get(0).confTeams.add( new Team( "New Orleans", "NOR", "SOUTH", this, 75, "LOU" ));
        conferences.get(0).confTeams.add( new Team( "Arkansas", "ARK", "SOUTH", this, 70, "KTY" ));
        conferences.get(0).confTeams.add( new Team( "Louisiana", "LOU", "SOUTH", this, 65, "NOR" ));
        conferences.get(0).confTeams.add( new Team( "Key West", "KYW", "SOUTH", this, 65, "ATL" ));
        conferences.get(0).confTeams.add( new Team( "Kentucky", "KTY", "SOUTH", this, 50, "ARK" ));

        //LAKES
        conferences.get(1).confTeams.add( new Team( "Ohio State", "OHI", "LAKES", this, 90, "MIC" ));
        conferences.get(1).confTeams.add( new Team( "Michigan", "MIC", "LAKES", this, 90, "OHI" ));
        conferences.get(1).confTeams.add( new Team( "Michigan St", "MSU", "LAKES", this, 80, "MIN" ));
        conferences.get(1).confTeams.add( new Team( "Wisconsin", "WIS", "LAKES", this, 70, "IND" ));
        conferences.get(1).confTeams.add( new Team( "Minnesota", "MIN", "LAKES", this, 70, "MSU" ));
        conferences.get(1).confTeams.add( new Team( "Univ of Chicago", "CHI", "LAKES", this, 70, "DET" ));
        conferences.get(1).confTeams.add( new Team( "Detroit St", "DET", "LAKES", this, 65, "CHI" ));
        conferences.get(1).confTeams.add( new Team( "Indiana", "IND", "LAKES", this, 65, "WIS" ));
        conferences.get(1).confTeams.add( new Team( "Cleveland St", "CLE", "LAKES", this, 55, "MIL" ));
        conferences.get(1).confTeams.add( new Team( "Milwaukee", "MIL", "LAKES", this, 45, "CLE" ));

        //NORTH
        conferences.get(2).confTeams.add( new Team( "New York St", "NYS", "NORTH", this, 90, "NYC" ));
        conferences.get(2).confTeams.add( new Team( "New Jersey", "NWJ", "NORTH", this, 85, "PEN" ));
        conferences.get(2).confTeams.add( new Team( "New York City", "NYC", "NORTH", this, 75, "NYS" ));
        conferences.get(2).confTeams.add( new Team( "Pennsylvania", "PEN", "NORTH", this, 75, "NWJ" ));
        conferences.get(2).confTeams.add( new Team( "Maryland", "MAR", "NORTH", this, 70, "WDC" ));
        conferences.get(2).confTeams.add( new Team( "Washington DC", "WDC", "NORTH", this, 70, "MAR" ));
        conferences.get(2).confTeams.add( new Team( "Boston St", "BOS", "NORTH", this, 65, "VER" ));
        conferences.get(2).confTeams.add( new Team( "Pittsburgh", "PIT", "NORTH", this, 60, "MAI" ));
        conferences.get(2).confTeams.add( new Team( "Maine", "MAI", "NORTH", this, 50, "PIT" ));
        conferences.get(2).confTeams.add( new Team( "Vermont", "VER", "NORTH", this, 45, "BOS" ));

        //COWBY
        conferences.get(3).confTeams.add( new Team( "Oklahoma", "OKL", "COWBY", this, 90, "TEX" ));
        conferences.get(3).confTeams.add( new Team( "Texas", "TEX", "COWBY", this, 90, "OKL" ));
        conferences.get(3).confTeams.add( new Team( "Houston", "HOU", "COWBY", this, 80, "DAL" ));
        conferences.get(3).confTeams.add( new Team( "Dallas", "DAL", "COWBY", this, 80, "HOU" ));
        conferences.get(3).confTeams.add( new Team( "Alamo St", "AMO", "COWBY", this, 70, "PAS" ));
        conferences.get(3).confTeams.add( new Team( "Oklahoma St", "OKS", "COWBY", this, 70, "TUL" ));
        conferences.get(3).confTeams.add( new Team( "El Paso St", "PAS", "COWBY", this, 60, "AMO" ));
        conferences.get(3).confTeams.add( new Team( "Texas St", "TXS", "COWBY", this, 60, "AUS" ));
        conferences.get(3).confTeams.add( new Team( "Tulsa", "TUL", "COWBY", this, 55, "OKS" ));
        conferences.get(3).confTeams.add( new Team( "Univ of Austin", "AUS", "COWBY", this, 50, "TXS" ));

        //PACIF
        conferences.get(4).confTeams.add( new Team( "California", "CAL", "PACIF", this, 90, "ULA" ));
        conferences.get(4).confTeams.add( new Team( "Oregon", "ORE", "PACIF", this, 85, "WAS" ));
        conferences.get(4).confTeams.add( new Team( "Los Angeles", "ULA", "PACIF", this, 80, "CAL" ));
        conferences.get(4).confTeams.add( new Team( "Oakland St", "OAK", "PACIF", this, 75, "HOL" ));
        conferences.get(4).confTeams.add( new Team( "Washington", "WAS", "PACIF", this, 75, "ORE" ));
        conferences.get(4).confTeams.add( new Team( "Hawaii", "HAW", "PACIF", this, 70, "SAM" ));
        conferences.get(4).confTeams.add( new Team( "Seattle", "SEA", "PACIF", this, 70, "SAN" ));
        conferences.get(4).confTeams.add( new Team( "Hollywood St", "HOL", "PACIF", this, 70, "OAK" ));
        conferences.get(4).confTeams.add( new Team( "San Diego St", "SAN", "PACIF", this, 60, "SEA" ));
        conferences.get(4).confTeams.add( new Team( "American Samoa", "SAM", "PACIF", this, 25, "HAW" ));

        //MOUNT
        conferences.get(5).confTeams.add( new Team( "Colorado", "COL", "MOUNT", this, 80, "DEN" ));
        conferences.get(5).confTeams.add( new Team( "Yellowstone St", "YEL", "MOUNT", this, 75, "ALB" ));
        conferences.get(5).confTeams.add( new Team( "Utah", "UTA", "MOUNT", this, 75, "SAL" ));
        conferences.get(5).confTeams.add( new Team( "Univ of Denver", "DEN", "MOUNT", this, 75, "COL" ));
        conferences.get(5).confTeams.add( new Team( "Albuquerque", "ALB", "MOUNT", this, 70, "YEL" ));
        conferences.get(5).confTeams.add( new Team( "Salt Lake St", "SAL", "MOUNT", this, 65, "UTA" ));
        conferences.get(5).confTeams.add( new Team( "Wyoming", "WYO", "MOUNT", this, 60, "MON" ));
        conferences.get(5).confTeams.add( new Team( "Montana", "MON", "MOUNT", this, 55, "WYO" ));
        conferences.get(5).confTeams.add( new Team( "Las Vegas", "LSV", "MOUNT", this, 50, "PHO" ));
        conferences.get(5).confTeams.add( new Team( "Phoenix", "PHO", "MOUNT", this, 45, "LSV" ));

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
     * @param saveFile file that league is saved in
     */
    public League(File saveFile, String namesCSV) {
        heismanDecided = false;
        hasScheduledBowls = false;
        bowlGames = new Game[10];
        // This will reference one line at a time
        String line = null;
        currentWeek = 0;

        leagueRecords = new LeagueRecords();
        longestWinStreak = new TeamStreak(2016, 2016, 0, null);

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
            conferences.add( new Conference("SOUTH", this) );
            conferences.add( new Conference("LAKES", this) );
            conferences.add( new Conference("NORTH", this) );
            conferences.add( new Conference("COWBY", this) );
            conferences.add( new Conference("PACIF", this) );
            conferences.add( new Conference("MOUNT", this) );
            allAmericans = new ArrayList<Player>();
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

            newsBless = new ArrayList<String>();
            while((line = bufferedReader.readLine()) != null && !line.equals("END_BLESS_TEAM")) {
                if (!line.equals("NULL")) newsBless.add(line);
            }

            newsCurse = new ArrayList<String>();
            while((line = bufferedReader.readLine()) != null && !line.equals("END_CURSE_TEAM")) {
                if (!line.equals("NULL")) newsCurse.add(line);
            }

            String[] record;
            while((line = bufferedReader.readLine()) != null && !line.equals("END_LEAGUE_RECORDS")) {
                record = line.split(",");
                System.out.println("Checking record:" + line);
                leagueRecords.checkRecord(record[0], Integer.parseInt(record[1]), record[2], Integer.parseInt(record[3]));
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
            if (newsCurse.size()> 0 && newsCurse.get(0) != null) {
                newsStories.get(0).add(newsCurse.get(0) + " Rocked by Infractions Scandal!>After an investigation during the offseason, "+newsCurse.get(0)+" has been placed on probation and assigned on-campus vistation limits for recruits. Athletic Director " + getRandName() + " released a statment vowing that the institution would work to repair the damage done to its prestige.");
            }
            if (newsBless.size()> 0 && newsBless.get(0) != null){
                newsStories.get(0).add("Blue Chip hire for Bad Break University>" + newsBless.get(0) + " announced the hire of alumnus and former professional coach " +getRandName()+", today. It was long rumored that the highly touted coach considered the position a \"dream job\", but talks between the two didn't heat up until this offseason. The hire certainly helps boost the prestige of the University's football program, which has fallen on hard times as of late.");
            }
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

    /**
     * Get conference nmber from string
     * @param conf conference name
     * @return int of number 0-5
     */
    public int getConfNumber(String conf) {
        if (conf.equals("SOUTH")) return 0;
        if (conf.equals("LAKES")) return 1;
        if (conf.equals("NORTH")) return 2;
        if (conf.equals("COWBY")) return 3;
        if (conf.equals("PACIF")) return 4;
        if (conf.equals("MOUNT")) return 5;
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
                ncg.awayTeam.totalNCLosses++;
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
                ncg.homeTeam.totalNCLosses++;
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
        //bowlNames = {"Lilac Bowl", "Apple Bowl", "Salty Bowl", "Salsa Bowl", "Mango Bowl",
        //             "Patriot Bowl", "Salad Bowl", "Frost Bowl", "Tropical Bowl", "Music Bowl"};
        bowlGames[0] = new Game( teamList.get(4), teamList.get(6), bowlNames[0] );
        teamList.get(4).gameSchedule.add(bowlGames[0]);
        teamList.get(6).gameSchedule.add(bowlGames[0]);

        bowlGames[1] = new Game( teamList.get(5), teamList.get(7), bowlNames[1] );
        teamList.get(5).gameSchedule.add(bowlGames[1]);
        teamList.get(7).gameSchedule.add(bowlGames[1]);

        bowlGames[2] = new Game( teamList.get(8), teamList.get(14), bowlNames[2] );
        teamList.get(8).gameSchedule.add(bowlGames[2]);
        teamList.get(14).gameSchedule.add(bowlGames[2]);

        bowlGames[3] = new Game( teamList.get(9), teamList.get(15), bowlNames[3] );
        teamList.get(9).gameSchedule.add(bowlGames[3]);
        teamList.get(15).gameSchedule.add(bowlGames[3]);

        bowlGames[4] = new Game( teamList.get(10), teamList.get(11), bowlNames[4] );
        teamList.get(10).gameSchedule.add(bowlGames[4]);
        teamList.get(11).gameSchedule.add(bowlGames[4]);

        bowlGames[5] = new Game( teamList.get(12), teamList.get(13), bowlNames[5] );
        teamList.get(12).gameSchedule.add(bowlGames[5]);
        teamList.get(13).gameSchedule.add(bowlGames[5]);

        bowlGames[6] = new Game( teamList.get(16), teamList.get(20), bowlNames[6] );
        teamList.get(16).gameSchedule.add(bowlGames[6]);
        teamList.get(20).gameSchedule.add(bowlGames[6]);

        bowlGames[7] = new Game( teamList.get(17), teamList.get(21), bowlNames[7] );
        teamList.get(17).gameSchedule.add(bowlGames[7]);
        teamList.get(21).gameSchedule.add(bowlGames[7]);

        bowlGames[8] = new Game( teamList.get(18), teamList.get(22), bowlNames[8] );
        teamList.get(18).gameSchedule.add(bowlGames[8]);
        teamList.get(22).gameSchedule.add(bowlGames[8]);

        bowlGames[9] = new Game( teamList.get(19), teamList.get(23), bowlNames[9] );
        teamList.get(19).gameSchedule.add(bowlGames[9]);
        teamList.get(23).gameSchedule.add(bowlGames[9]);

        hasScheduledBowls = true;
          
    }
    
    /**
     * Actually plays each bowl game.
     */

    public void playBowlGames() {
        for (Game g : bowlGames) {
            playBowl(g);
        }
        
        semiG14.playGame();
        semiG23.playGame();
        Team semi14winner;
        Team semi23winner;
        if ( semiG14.homeScore > semiG14.awayScore ) {
            semiG14.homeTeam.semiFinalWL = "SFW";
            semiG14.awayTeam.semiFinalWL = "SFL";
            semiG14.awayTeam.totalBowlLosses++;
            semiG14.homeTeam.totalBowls++;
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
            semiG14.homeTeam.totalBowlLosses++;
            semiG14.awayTeam.totalBowls++;
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
            semiG23.homeTeam.totalBowls++;
            semiG23.awayTeam.totalBowlLosses++;
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
            semiG23.awayTeam.totalBowls++;
            semiG23.homeTeam.totalBowlLosses++;
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
        semi23winner.gameSchedule.add(ncg);
        
    }

    /**
     * Plays a particular bowl game
     * @param g bowl game to be played
     */
    private void playBowl(Game g) {
        g.playGame();
        if ( g.homeScore > g.awayScore ) {
            g.homeTeam.semiFinalWL = "BW";
            g.awayTeam.semiFinalWL = "BL";
            g.homeTeam.totalBowls++;
            g.awayTeam.totalBowlLosses++;
            newsStories.get(14).add(
                    g.homeTeam.name + " wins the " + g.gameName +"!>" +
                    g.homeTeam.strRep() + " defeats " + g.awayTeam.strRep() +
                    " in the " + g.gameName + ", winning " + g.homeScore + " to " + g.awayScore + "."
            );
        } else {
            g.homeTeam.semiFinalWL = "BL";
            g.awayTeam.semiFinalWL = "BW";
            g.homeTeam.totalBowlLosses++;
            g.awayTeam.totalBowls++;
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

        // Bless a random team with lots of prestige
        int blessNumber = (int)(Math.random()*9);
        Team blessTeam = teamList.get(50 + blessNumber);
        if (!blessTeam.userControlled && !blessTeam.name.equals("American Samoa")) {
            blessTeam.teamPrestige += 35;
            saveBless = blessTeam;
            if (blessTeam.teamPrestige > 90) blessTeam.teamPrestige = 90;
        }
        else saveBless = null;

        //Curse a good team
        int curseNumber = (int)(Math.random()*7);
        Team curseTeam = teamList.get(3 + curseNumber);
        if (!curseTeam.userControlled && curseTeam.teamPrestige > 85) {
            curseTeam.teamPrestige -= 25;
            saveCurse = curseTeam;
        }
        else saveCurse = null;

        for (int c = 0; c < conferences.size(); ++c) {
            conferences.get(c).robinWeek = 0;
            conferences.get(c).week = 0;
        }
        //set up schedule (not needed anymore?)
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
     * Check the longest win streak. If the given streak is longer, replace.
     * @param streak streak to check
     */
    public void checkLongestWinStreak(TeamStreak streak) {
        if (streak.getStreakLength() > longestWinStreak.getStreakLength()) {
            longestWinStreak = new TeamStreak(streak.getStartYear(), streak.getEndYear(), streak.getStreakLength(), streak.getTeam());
        }
    }

    /**
     * Checks if any of the league records were broken by teams.
     */
    public void checkLeagueRecords() {
        for (Team t : teamList) {
            t.checkLeagueRecords();
        }
    }

    /**
     * Gets the current year, starting from 2016
     * @return the current year
     */
    public int getYear() {
        return 2016 + leagueHistory.size();
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
        if (Math.random() > 0.001) {
            int fn = (int) (Math.random() * nameList.size());
            int ln = (int) (Math.random() * nameList.size());
            return nameList.get(fn) + " " + nameList.get(ln);
        } else return "Mark Eeslee";
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

        Collections.sort( teamList, new TeamCompOPYPG() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOppPassYards = t+1;
        }

        Collections.sort( teamList, new TeamCompORYPG() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOppRushYards = t+1;
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

    /**
     * Get string of the top 5 heisman candidates. If the heisman is already decided, get the ceremony str.
     * @return string of top 5 players and their stats
     */
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

    /**
     * Perform the heisman ceremony. Congratulate winner and give top 5 vote getters.
     * @return string of the heisman ceremony.
     */
    public String getHeismanCeremonyStr() {
        boolean putNewsStory = false;
        if (!heismanDecided) {
            heismanDecided = true;
            heismanCandidates = getHeisman();
            heisman = heismanCandidates.get(0);
            putNewsStory = true;
            //full results string
            String heismanTop5 = "\n";
            for (int i = 0; i < 5; ++i) {
                Player p = heismanCandidates.get(i);
                heismanTop5 += (i + 1) + ". " + p.team.abbr + "(" + p.team.wins + "-" + p.team.losses + ")" + " - ";
                if (p instanceof PlayerQB) {
                    PlayerQB pqb = (PlayerQB) p;
                    heismanTop5 += " QB " + pqb.getInitialName() + ": " + p.getHeismanScore() + " votes\n\t("
                            + pqb.statsTD + " TDs, " + pqb.statsInt + " Int, " + pqb.statsPassYards + " Yds)\n\n";
                } else if (p instanceof PlayerRB) {
                    PlayerRB prb = (PlayerRB) p;
                    heismanTop5 += " RB " + prb.getInitialName() + ": " + p.getHeismanScore() + " votes\n\t("
                            + prb.statsTD + " TDs, " + prb.statsFumbles + " Fum, " + prb.statsRushYards + " Yds)\n\n";
                } else if (p instanceof PlayerWR) {
                    PlayerWR pwr = (PlayerWR) p;
                    heismanTop5 += " WR " + pwr.getInitialName() + ": " + p.getHeismanScore() + " votes\n\t("
                            + pwr.statsTD + " TDs, " + pwr.statsFumbles + " Fum, " + pwr.statsRecYards + " Yds)\n\n";
                }
            }
            String heismanStats = "";
            String heismanWinnerStr = "";
            if (heisman instanceof PlayerQB) {
                //qb heisman
                PlayerQB heisQB = (PlayerQB) heisman;
                heismanWinnerStr = "Congratulations to the Player of the Year, " + heisQB.team.abbr +
                        " QB " + heisQB.name + " [" + heisman.getYrStr() + "], who had " +
                        heisQB.statsTD + " TDs, just " + heisQB.statsInt + " interceptions, and " +
                        heisQB.statsPassYards + " passing yards. He led " + heisQB.team.name +
                        " to a " + heisQB.team.wins + "-" + heisQB.team.losses + " record and a #" + heisQB.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            } else if (heisman instanceof PlayerRB) {
                //rb heisman
                PlayerRB heisRB = (PlayerRB) heisman;
                heismanWinnerStr = "Congratulations to the Player of the Year, " + heisRB.team.abbr +
                        " RB " + heisRB.name + " [" + heisman.getYrStr() + "], who had " +
                        heisRB.statsTD + " TDs, just " + heisRB.statsFumbles + " fumbles, and " +
                        heisRB.statsRushYards + " rushing yards. He led " + heisRB.team.name +
                        " to a " + heisRB.team.wins + "-" + heisRB.team.losses + " record and a #" + heisRB.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            } else if (heisman instanceof PlayerWR) {
                //wr heisman
                PlayerWR heisWR = (PlayerWR) heisman;
                heismanWinnerStr = "Congratulations to the Player of the Year, " + heisWR.team.abbr +
                        " WR " + heisWR.name + " [" + heisman.getYrStr() + "], who had " +
                        heisWR.statsTD + " TDs, just " + heisWR.statsFumbles + " fumbles, and " +
                        heisWR.statsRecYards + " receiving yards. He led " + heisWR.team.name +
                        " to a " + heisWR.team.wins + "-" + heisWR.team.losses + " record and a #" + heisWR.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            }

            // Add news story
            if (putNewsStory) {
                newsStories.get(13).add(heisman.name + " is the Player of the Year!>" + heismanWinnerStr);
                heismanWinnerStrFull = heismanStats;
            }

            return heismanStats;

        } else {
            return heismanWinnerStrFull;
        }
    }
    public String getAllAmericanStr() {
        if (allAmericans.isEmpty()) {
            ArrayList<PlayerQB> qbs = new ArrayList<>();
            ArrayList<PlayerRB> rbs = new ArrayList<>();
            ArrayList<PlayerWR> wrs = new ArrayList<>();

            for (Conference c : conferences) {
                c.getAllConfPlayers();
                qbs.add((PlayerQB) c.allConfPlayers.get(0));
                rbs.add((PlayerRB) c.allConfPlayers.get(1));
                rbs.add((PlayerRB) c.allConfPlayers.get(2));
                wrs.add((PlayerWR) c.allConfPlayers.get(3));
                wrs.add((PlayerWR) c.allConfPlayers.get(4));
                wrs.add((PlayerWR) c.allConfPlayers.get(5));
            }

            Collections.sort(qbs, new PlayerHeismanComp());
            Collections.sort(rbs, new PlayerHeismanComp());
            Collections.sort(wrs, new PlayerHeismanComp());

            allAmericans.add(qbs.get(0));
            allAmericans.add(rbs.get(0));
            allAmericans.add(rbs.get(1));
            allAmericans.add(wrs.get(0));
            allAmericans.add(wrs.get(1));
            allAmericans.add(wrs.get(2));
        }

        StringBuilder allAmerican = new StringBuilder();
        for (int i = 0; i < 6; ++i) {
            Player p = allAmericans.get(i);
            allAmerican.append(p.team.abbr + "(" + p.team.wins + "-" + p.team.losses + ")" + " - ");
            if (p instanceof PlayerQB) {
                PlayerQB pqb = (PlayerQB) p;
                allAmerican.append(" QB " + pqb.name + " [" + pqb.getYrStr() + "]\n \t\t" +
                        pqb.statsTD + " TDs, " + pqb.statsInt + " Int, " + pqb.statsPassYards + " Yds\n");
            } else if (p instanceof PlayerRB) {
                PlayerRB prb = (PlayerRB) p;
                allAmerican.append(" RB " + prb.name + " [" + prb.getYrStr() + "]\n \t\t" +
                        prb.statsTD + " TDs, " + prb.statsFumbles + " Fum, " + prb.statsRushYards + " Yds\n");
            } else if (p instanceof PlayerWR) {
                PlayerWR pwr = (PlayerWR) p;
                allAmerican.append(" WR " + pwr.name + " [" + pwr.getYrStr() + "]\n \t\t" +
                        pwr.statsTD + " TDs, " + pwr.statsFumbles + " Fum, " + pwr.statsRecYards + " Yds\n");
            }
            allAmerican.append(" \t\tOverall: " + p.ratOvr + ", Potential: " + p.ratPot + "\n\n");
        }

        // Go through all the all conf players to get the all americans
        return allAmerican.toString();
    }

    public String getAllConfStr(int confNum) {
        ArrayList<Player> allConfPlayers = conferences.get(confNum).getAllConfPlayers();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; ++i) {
            Player p = allConfPlayers.get(i);
            sb.append(p.team.abbr + "(" + p.team.wins + "-" + p.team.losses + ")" + " - ");
            if (p instanceof PlayerQB) {
                PlayerQB pqb = (PlayerQB) p;
                sb.append(" QB " + pqb.name + " [" + pqb.getYrStr() + "]\n \t\t" +
                        pqb.statsTD + " TDs, " + pqb.statsInt + " Int, " + pqb.statsPassYards + " Yds\n");
            } else if (p instanceof PlayerRB) {
                PlayerRB prb = (PlayerRB) p;
                sb.append(" RB " + prb.name + " [" + prb.getYrStr() + "]\n \t\t" +
                        prb.statsTD + " TDs, " + prb.statsFumbles + " Fum, " + prb.statsRushYards + " Yds\n");
            } else if (p instanceof PlayerWR) {
                PlayerWR pwr = (PlayerWR) p;
                sb.append(" WR " + pwr.name + " [" + pwr.getYrStr() + "]\n \t\t" +
                        pwr.statsTD + " TDs, " + pwr.statsFumbles + " Fum, " + pwr.statsRecYards + " Yds\n");
            }
            sb.append(" \t\tOverall: " + p.ratOvr + ", Potential: " + p.ratPot + "\n\n");
        }

        return sb.toString();
    }
    /**
     * Get list of all the teams and their rankings based on selection
     * @param selection stat to sort by, 0-13
     * @return list of the teams: ranking,str rep,stat
     */
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
        8 = opp pass yards
        9 = opp rush yards
        10 = TO diff
        11 = off talent
        12 = def talent
        13 = prestige
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
            case 8: Collections.sort( teams, new TeamCompOPYPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamOppPassYards/t.numGames()));
                }
                break;
            case 9: Collections.sort( teams, new TeamCompORYPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamOppRushYards/t.numGames()));
                }
                break;
            case 10: Collections.sort( teams, new TeamCompTODiff() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    if (t.teamTODiff > 0) rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + ",+" + t.teamTODiff);
                    else rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.teamTODiff);
                }
                break;
            case 11: Collections.sort( teams, new TeamCompOffTalent() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.teamOffTalent);
                }
                break;
            case 12: Collections.sort( teams, new TeamCompDefTalent() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.teamDefTalent);
                }
                break;
            case 13: Collections.sort( teams, new TeamCompPrestige() );
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

    /**
     * Get String of the league's history, year by year.
     * Saves the NCG winner and the POTY.
     * @return list of the league's history.
     */
    public String getLeagueHistoryStr() {
        String hist = "";
        for (int i = 0; i < leagueHistory.size(); ++i) {
            hist += (2016+i) + ":\n";
            hist += "\tChampions: " + leagueHistory.get(i)[0] + "\n";
            hist += "\tPOTY: " + heismanHistory.get(i) + "\n%";
        }
        return hist;
    }

    /**
     * Get list of teams and their prestige, used for selecting when a new game is started
     * @return array of all the teams
     */
    public String[] getTeamListStr() {
        String[] teams = new String[teamList.size()];
        for (int i = 0; i < teamList.size(); ++i){
            teams[i] = teamList.get(i).conference + ": " +
                    teamList.get(i).name + ", Pres: " + teamList.get(i).teamPrestige;
        }
        return teams;
    }

    /**
     * Get list of all bowl games and their predicted teams
     * @return string of all the bowls and their predictions
     */
    public String getBowlGameWatchStr() {
        //if bowls arent scheduled yet, give predictions
        if (!hasScheduledBowls) {

            for (int i = 0; i < teamList.size(); ++i) {
                teamList.get(i).updatePollScore();
            }
            Collections.sort(teamList, new TeamCompPoll());

            StringBuilder sb = new StringBuilder();
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

            sb.append(bowlNames[0]+":\n\t\t");
            t1 = teamList.get(4);
            t2 = teamList.get(6);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[1]+":\n\t\t");
            t1 = teamList.get(5);
            t2 = teamList.get(7);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[2]+":\n\t\t");
            t1 = teamList.get(8);
            t2 = teamList.get(14);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[3]+":\n\t\t");
            t1 = teamList.get(9);
            t2 = teamList.get(15);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[4]+":\n\t\t");
            t1 = teamList.get(10);
            t2 = teamList.get(11);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[5]+":\n\t\t");
            t1 = teamList.get(12);
            t2 = teamList.get(13);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            for (int i = 6; i < 10; ++i) {
                sb.append(bowlNames[i]+":\n\t\t");
                t1 = teamList.get(10 + i);
                t2 = teamList.get(14 + i);
                sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");
            }

            return sb.toString();

        } else {
            // Games have already been scheduled, give actual teams
            StringBuilder sb = new StringBuilder();

            sb.append("Semifinal 1v4:\n");
            sb.append(getGameSummaryBowl(semiG14));

            sb.append("\n\nSemifinal 2v3:\n");
            sb.append(getGameSummaryBowl(semiG23));

            for (int i = 0; i < bowlGames.length; ++i) {
                sb.append("\n\n"+bowlNames[i]+":\n");
                sb.append(getGameSummaryBowl(bowlGames[i]));
            }

            return sb.toString();
        }
    }

    /**
     * Get string of what happened in a particular bowl
     * @param g Bowl game to be examined
     * @return string of its summary, ALA W 24 - 40 @ GEO, etc
     */
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

    /**
     * Get a list of all the CCGs and their teams
     * @return
     */
    public String getCCGsStr() {
        StringBuilder sb = new StringBuilder();
        for (Conference c : conferences) {
            sb.append(c.getCCGStr()+"\n\n");
        }
        return sb.toString();
    }

    /**
     * Find team based on a name
     * @param name team name
     * @return reference to the Team object
     */
    public Team findTeam(String name) {
        for (int i = 0; i < teamList.size(); i++){
            if (teamList.get(i).strRep().equals(name)) {
                return teamList.get(i);
            }
        }
        return teamList.get(0);
    }

    /**
     * Find team based on a abbr
     * @param abbr team abbr
     * @return reference to the Team object
     */
    public Team findTeamAbbr(String abbr) {
        for (int i = 0; i < teamList.size(); i++){
            if (teamList.get(i).abbr.equals(abbr)) {
                return teamList.get(i);
            }
        }
        return teamList.get(0);
    }

    /**
     * Find conference based on a name
     * @param name conf name
     * @return reference to the Conference object
     */
    public Conference findConference(String name) {
        for (int i = 0; i < teamList.size(); i++){
            if (conferences.get(i).confName.equals(name)) {
                return conferences.get(i);
            }
        }
        return conferences.get(0);
    }

    /**
     * See if team name is in use, or has illegal characters.
     * @param name team name
     * @return true if valid, false if not
     */
    public boolean isNameValid(String name) {
        if (name.length() == 0) {
            return false;
        }

        if (name.contains(",") || name.contains(">") || name.contains("%") || name.contains("\\")) {
            // Illegal character!
            return false;
        }

        for (int i = 0; i < teamList.size(); i++) {
            // compare using all lower case so no dumb duplicates
            if (teamList.get(i).name.toLowerCase().equals(name.toLowerCase()) &&
                    !teamList.get(i).userControlled) {
                return false;
            }
        }

        return true;
    }

    /**
     * See if team abbr is in use, or has illegal characters, or is not 3 characters
     * @param abbr new abbr
     * @return true if valid, false if not
     */
    public boolean isAbbrValid(String abbr) {
        if (abbr.length() > 3 || abbr.length() == 0) {
            // Only 3 letter abbr allowed
            return false;
        }

        if (abbr.contains(",") || abbr.contains(">") || abbr.contains("%") || abbr.contains("\\")) {
            // Illegal character!
            return false;
        }

        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i).abbr.equals(abbr) &&
                    !teamList.get(i).userControlled) {
                return false;
            }
        }

        return true;
    }

    /**
     * Get summary of what happened in the NCG
     * @return string of summary
     */
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

    /**
     * Get summary of season.
     * @return ncgSummary, userTeam's summary
     */
    public String seasonSummaryStr() {
        setTeamRanks();
        StringBuilder sb = new StringBuilder();
        sb.append(ncgSummaryStr());
        sb.append("\n\n" + userTeam.seasonSummaryStr());
        return sb.toString();
    }

    /**
     * Save League in a file.
     * @param saveFile file to be overwritten
     * @return true if successful
     */
    public boolean saveLeague(File saveFile) {
        StringBuilder sb = new StringBuilder();

        // Save information about the save file, user team info
        sb.append((2016+leagueHistory.size())+": " + userTeam.abbr + " (" + (userTeam.totalWins-userTeam.wins) + "-" + (userTeam.totalLosses-userTeam.losses) + ") " +
                    userTeam.totalCCs + " CCs, " + userTeam.totalNCs + " NCs%\n");

        // Save league history of who was #1 each year
        for (int i = 0; i < leagueHistory.size(); ++i) {
            for (int j = 0; j < leagueHistory.get(i).length; ++j) {
                sb.append(leagueHistory.get(i)[j] + "%");
            }
            sb.append("\n");
        }
        sb.append("END_LEAGUE_HIST\n");

        // Save POTY history of who won each year
        for (int i = 0; i < heismanHistory.size(); ++i) {
            sb.append(heismanHistory.get(i) + "\n");
        }
        sb.append("END_HEISMAN_HIST\n");

        // Save information about each team like W-L records, as well as all the players
        for (Team t : teamList) {
            sb.append(t.conference + "," + t.name + "," + t.abbr + "," + t.teamPrestige + "," +
                    (t.totalWins-t.wins) + "," + (t.totalLosses-t.losses) + "," + t.totalCCs + "," + t.totalNCs + "," + t.rivalTeam + "," +
                    t.totalNCLosses + "," + t.totalCCLosses + "," + t.totalBowls + "," + t.totalBowlLosses + "," +
                    t.teamStratOffNum + "," + t.teamStratDefNum + "," + (t.showPopups ? 1 : 0) + "%\n");
            sb.append(t.getPlayerInfoSaveFile());
            sb.append("END_PLAYERS\n");
        }

        // Save history of the user's team of the W-L and bowl results each year
        sb.append(userTeam.name + "\n");
        for (String s : userTeam.teamHistory) {
            sb.append(s + "\n");
        }
        sb.append("END_USER_TEAM\n");

        // Save who was blessed and cursed this year for news stories the following year
        if (saveBless != null) {
            sb.append(saveBless.name + "\n");
            sb.append("END_BLESS_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_BLESS_TEAM\n");
        }
        if (saveCurse != null) {
            sb.append(saveCurse.name + "\n");
            sb.append("END_CURSE_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_CURSE_TEAM\n");
        }

        // Save league records
        System.out.println("Saving Records!\n" + leagueRecords.getRecordsStr());
        sb.append(leagueRecords.getRecordsStr());
        sb.append("END_LEAGUE_RECORDS\n");

        // Actually write to the file
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

class TeamCompOPYPG implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamOppPassYards/a.numGames() < b.teamOppPassYards/b.numGames() ? -1 : a.teamOppPassYards/a.numGames() == b.teamOppPassYards/b.numGames() ? 0 : 1;
    }
}

class TeamCompORYPG implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamOppRushYards/a.numGames() < b.teamOppRushYards/b.numGames() ? -1 : a.teamOppRushYards/a.numGames() == b.teamOppRushYards/b.numGames() ? 0 : 1;
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






