package CFBsimPack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Team class.
 * @author Achi
 */
public class Team {
    
    public League league;
    
    public String name;
    public String abbr;
    public String conference;
    public String rivalTeam;
    public boolean wonRivalryGame;
    public ArrayList<String> teamHistory;
    public boolean userControlled;
    public boolean showPopups;
    public int recruitMoney;
    public int numRecruits;
    
    public int wins;
    public int losses;
    public int totalWins;
    public int totalLosses;
    public int totalCCs;
    public int totalNCs;
    public int totalCCLosses;
    public int totalNCLosses;
    public int totalBowls;
    public int totalBowlLosses;

    public TeamStreak winStreak;
    
    //Game Log variables
    public ArrayList<Game> gameSchedule;
    public Game gameOOCSchedule0;
    public Game gameOOCSchedule4;
    public Game gameOOCSchedule9;
    public ArrayList<String> gameWLSchedule;
    public ArrayList<Team> gameWinsAgainst;
    public String confChampion;
    public String semiFinalWL;
    public String natChampWL;
    
    //Team stats
    public int teamPoints;
    public int teamOppPoints;
    public int teamYards;
    public int teamOppYards;
    public int teamPassYards;
    public int teamRushYards;
    public int teamOppPassYards;
    public int teamOppRushYards;
    public int teamTODiff;
    public int teamOffTalent;
    public int teamDefTalent;
    public int teamPrestige;
    public int teamPollScore;
    public int teamStrengthOfWins;
    
    public int rankTeamPoints;
    public int rankTeamOppPoints;
    public int rankTeamYards;
    public int rankTeamOppYards;
    public int rankTeamPassYards;
    public int rankTeamRushYards;
    public int rankTeamOppPassYards;
    public int rankTeamOppRushYards;
    public int rankTeamTODiff;
    public int rankTeamOffTalent;
    public int rankTeamDefTalent;
    public int rankTeamPrestige;
    public int rankTeamPollScore;
    public int rankTeamStrengthOfWins;
    
    //prestige/talent improvements
    public int diffPrestige;
    public int diffOffTalent;
    public int diffDefTalent;
    
    //players on team
    //offense
    public ArrayList<PlayerQB> teamQBs;
    public ArrayList<PlayerRB> teamRBs;
    public ArrayList<PlayerWR> teamWRs;
    public ArrayList<PlayerK> teamKs;
    public ArrayList<PlayerOL> teamOLs;
    //defense
    public ArrayList<PlayerF7> teamF7s;
    public ArrayList<PlayerS> teamSs;
    public ArrayList<PlayerCB> teamCBs;

    public ArrayList<Player> playersLeaving;
    
    public TeamStrategy teamStratOff;
    public TeamStrategy teamStratDef;
    public int teamStratOffNum;
    public int teamStratDefNum;

    private static final int NFL_OVR = 90;
    private static final double NFL_CHANCE = 0.5;

    /**
     * Creates new team, recruiting needed players and setting team stats to 0.
     * @param name name of the team
     * @param abbr abbreviation of the team, 3 letters
     * @param conference conference the team is in
     * @param league reference to the league object all must obey
     * @param prestige prestige of that team, between 0-100
     */
    public Team( String name, String abbr, String conference, League league, int prestige, String rivalTeamAbbr ) {
        this.league = league;
        userControlled = false;
        showPopups = true;
        teamHistory = new ArrayList<String>();
        
        teamQBs = new ArrayList<PlayerQB>();
        teamRBs = new ArrayList<PlayerRB>();
        teamWRs = new ArrayList<PlayerWR>();
        teamKs = new ArrayList<PlayerK>();
        teamOLs = new ArrayList<PlayerOL>();
        teamF7s = new ArrayList<PlayerF7>();
        teamSs = new ArrayList<PlayerS>();
        teamCBs = new ArrayList<PlayerCB>();
        
        gameSchedule = new ArrayList<Game>();
        gameOOCSchedule0 = null;
        gameOOCSchedule4 = null;
        gameOOCSchedule9 = null;
        gameWinsAgainst = new ArrayList<Team>();
        gameWLSchedule = new ArrayList<String>();
        confChampion = "";
        semiFinalWL = "";
        natChampWL = "";
        
        teamPrestige = prestige;
        recruitPlayers(2,4,6,2,10,2,6,14);       
        
        //set stats
        totalWins = 0;
        totalLosses = 0;
        winStreak = new TeamStreak(league.getYear(), league.getYear(), 0, this);
        totalCCs = 0;
        totalNCs = 0;
        totalCCLosses = 0;
        totalNCLosses = 0;
        totalBowls = 0;
        totalBowlLosses = 0;
        this.name = name;
        this.abbr = abbr;
        this.conference = conference;
        rivalTeam = rivalTeamAbbr;
        wonRivalryGame = false;
        teamPoints = 0;
        teamOppPoints = 0;
        teamYards = 0;
        teamOppYards = 0;
        teamPassYards = 0;
        teamRushYards = 0;
        teamOppPassYards = 0;
        teamOppRushYards = 0;
        teamTODiff = 0;
        teamOffTalent = getOffTalent();
        teamDefTalent = getDefTalent();
        
        teamPollScore = teamPrestige + getOffTalent() + getDefTalent();

        teamStratOff = new TeamStrategy();
        teamStratDef = new TeamStrategy();
        teamStratOffNum = 1; // 1 is the default strats
        teamStratDefNum = 1;
        numRecruits = 30;
        playersLeaving = new ArrayList<>();
    }

    /**
     * Constructor for team that is being loaded from file.
     * @param loadStr String containing the team info that can be loaded
     */
    public Team( String loadStr, League league ) {
        this.league = league;
        userControlled = false;
        showPopups = true;
        teamHistory = new ArrayList<String>();

        teamQBs = new ArrayList<PlayerQB>();
        teamRBs = new ArrayList<PlayerRB>();
        teamWRs = new ArrayList<PlayerWR>();
        teamKs = new ArrayList<PlayerK>();
        teamOLs = new ArrayList<PlayerOL>();
        teamF7s = new ArrayList<PlayerF7>();
        teamSs = new ArrayList<PlayerS>();
        teamCBs = new ArrayList<PlayerCB>();

        gameSchedule = new ArrayList<Game>();
        gameOOCSchedule0 = null;
        gameOOCSchedule4 = null;
        gameOOCSchedule9 = null;
        gameWinsAgainst = new ArrayList<Team>();
        gameWLSchedule = new ArrayList<String>();
        confChampion = "";
        semiFinalWL = "";
        natChampWL = "";

        //set stats
        teamPoints = 0;
        teamOppPoints = 0;
        teamYards = 0;
        teamOppYards = 0;
        teamPassYards = 0;
        teamRushYards = 0;
        teamOppPassYards = 0;
        teamOppRushYards = 0;
        teamTODiff = 0;
        teamOffTalent = 0;
        teamDefTalent = 0;
        teamPollScore = 0;
        teamStratOffNum = 1; // 1 is the default strats
        teamStratDefNum = 1;
        winStreak = new TeamStreak(league.getYear(), league.getYear(), 0, this);

        // Actually load the team from the string
        String[] lines = loadStr.split("%");

        // Line 0 is team info
        String[] teamInfo = lines[0].split(",");
        if (teamInfo.length >= 9) {
            conference = teamInfo[0];
            name = teamInfo[1];
            abbr = teamInfo[2];
            teamPrestige = Integer.parseInt(teamInfo[3]);
            totalWins = Integer.parseInt(teamInfo[4]);
            totalLosses = Integer.parseInt(teamInfo[5]);
            totalCCs = Integer.parseInt(teamInfo[6]);
            totalNCs = Integer.parseInt(teamInfo[7]);
            rivalTeam = teamInfo[8];
            if (teamInfo.length >= 13) {
                totalNCLosses = Integer.parseInt(teamInfo[9]);
                totalCCLosses = Integer.parseInt(teamInfo[10]);
                totalBowls = Integer.parseInt(teamInfo[11]);
                totalBowlLosses = Integer.parseInt(teamInfo[12]);
                if (teamInfo.length >= 16) {
                    teamStratOffNum = Integer.parseInt(teamInfo[13]);
                    teamStratDefNum = Integer.parseInt(teamInfo[14]);
                    showPopups = (Integer.parseInt(teamInfo[15]) == 1);
                    if (teamInfo.length == 19) {
                        winStreak = new TeamStreak(Integer.parseInt(teamInfo[16]),
                                Integer.parseInt(teamInfo[17]),
                                Integer.parseInt(teamInfo[18]),
                                this);
                    }
                }
            } else {
                totalCCLosses = 0;
                totalNCLosses = 0;
                totalBowls = 0;
                totalBowlLosses = 0;
            }
        }

        // Rest of lines are player info
        String[] playerInfo;
        for (int i = 1; i < lines.length; ++i) {
            recruitPlayerCSV(lines[i], false);
        }

        wonRivalryGame = false;
        teamStratOff = getTeamStrategiesOff()[teamStratOffNum];
        teamStratDef = getTeamStrategiesDef()[teamStratDefNum];
        numRecruits = 30;
        playersLeaving = new ArrayList<>();
    }

    /**
     * Gets the OffTalent, DefTalent, poll score
     */
    public void updateTalentRatings() {
        teamOffTalent = getOffTalent();
        teamDefTalent = getDefTalent();
        teamPollScore = teamPrestige + getOffTalent() + getDefTalent();
    }
    
    /**
     * Advance season, hiring new coach if needed and calculating new prestige level.
     */
    public void advanceSeason() {
        // subtract for rivalry first
        if ( wonRivalryGame && (teamPrestige - league.findTeamAbbr(rivalTeam).teamPrestige < 20) ) {
            teamPrestige += 2;
        } else if (!wonRivalryGame && (league.findTeamAbbr(rivalTeam).teamPrestige - teamPrestige < 20 || name.equals("American Samoa"))) {
            teamPrestige -= 2;
        }

        int expectedPollFinish = 100 - teamPrestige;
        int diffExpected = expectedPollFinish - rankTeamPollScore;
        int oldPrestige = teamPrestige;

        if ( (teamPrestige > 45 && !name.equals("American Samoa")) || diffExpected > 0 ) {
            teamPrestige = (int)Math.pow(teamPrestige, 1 + (float)diffExpected/1500);// + diffExpected/2500);
        }

        if ( rankTeamPollScore == 1 ) {
            // NCW
            teamPrestige += 3;
        }

        if (teamPrestige > 95) teamPrestige = 95;
        if (teamPrestige < 45 && !name.equals("American Samoa")) teamPrestige = 45;

        diffPrestige = teamPrestige - oldPrestige;
        //checkLeagueRecords();
        advanceSeasonPlayers();
        
    }

    /**
     * Checks if any of the league records were broken by this team.
     */
    public void checkLeagueRecords() {
        league.leagueRecords.checkRecord("Team PPG", teamPoints/numGames(), abbr, league.getYear());
        league.leagueRecords.checkRecord("Team Opp PPG", teamOppPoints/numGames(), abbr, league.getYear());
        league.leagueRecords.checkRecord("Team YPG", teamYards/numGames(), abbr, league.getYear());
        league.leagueRecords.checkRecord("Team Opp YPG", teamOppYards/numGames(), abbr, league.getYear());
        league.leagueRecords.checkRecord("Team PPG", teamPoints/numGames(), abbr, league.getYear());
        league.leagueRecords.checkRecord("Team TO Diff", teamTODiff, abbr, league.getYear());

        league.leagueRecords.checkRecord("Pass Yards", getQB(0).statsPassYards, abbr + " " + getQB(0).getInitialName(), league.getYear());
        league.leagueRecords.checkRecord("Pass TDs", getQB(0).statsTD, abbr + " " + getQB(0).getInitialName(), league.getYear());
        league.leagueRecords.checkRecord("Interceptions", getQB(0).statsInt, abbr + " " + getQB(0).getInitialName(), league.getYear());
        league.leagueRecords.checkRecord("Comp Percent", (100*getQB(0).statsPassComp)/getQB(0).statsPassAtt, abbr + " " + getQB(0).getInitialName(), league.getYear());

        for (int i = 0; i < 2; ++i) {
            league.leagueRecords.checkRecord("Rush Yards", getRB(i).statsRushYards, abbr + " " + getRB(i).getInitialName(), league.getYear());
            league.leagueRecords.checkRecord("Rush TDs", getRB(i).statsTD, abbr + " " + getRB(i).getInitialName(), league.getYear());
            league.leagueRecords.checkRecord("Rush Fumbles", getRB(i).statsFumbles, abbr + " " + getRB(i).getInitialName(), league.getYear());
        }

        for (int i = 0; i < 3; ++i) {
            league.leagueRecords.checkRecord("Rec Yards", getWR(i).statsRecYards, abbr + " " + getWR(i).getInitialName(), league.getYear());
            league.leagueRecords.checkRecord("Rec TDs", getWR(i).statsTD, abbr + " " + getWR(i).getInitialName(), league.getYear());
            league.leagueRecords.checkRecord("Catch Percent", (100*getWR(i).statsReceptions)/getWR(i).statsTargets, abbr + " " + getWR(i).getInitialName(), league.getYear());
        }

    }

    public void getPlayersLeaving() {
        if (playersLeaving.isEmpty()) {
            int i = 0;
            while (i < teamQBs.size()) {
                if (teamQBs.get(i).year == 4 || (teamQBs.get(i).year == 3 && teamQBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    playersLeaving.add(teamQBs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamRBs.size()) {
                if (teamRBs.get(i).year == 4 || (teamRBs.get(i).year == 3 && teamRBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    playersLeaving.add(teamRBs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamWRs.size()) {
                if (teamWRs.get(i).year == 4 || (teamWRs.get(i).year == 3 && teamWRs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    playersLeaving.add(teamWRs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamKs.size()) {
                if (teamKs.get(i).year == 4) {
                    playersLeaving.add(teamKs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamOLs.size()) {
                if (teamOLs.get(i).year == 4 || (teamOLs.get(i).year == 3 && teamOLs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    playersLeaving.add(teamOLs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamSs.size()) {
                if (teamSs.get(i).year == 4 || (teamSs.get(i).year == 3 && teamSs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    playersLeaving.add(teamSs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamCBs.size()) {
                if (teamCBs.get(i).year == 4 || (teamCBs.get(i).year == 3 && teamCBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    playersLeaving.add(teamCBs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamF7s.size()) {
                if (teamF7s.get(i).year == 4 || (teamF7s.get(i).year == 3 && teamF7s.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    playersLeaving.add(teamF7s.get(i));
                }
                ++i;
            }
        }
    }
    
    /**
     * Advance season for players. Removes seniors and develops underclassmen.
     */
    public void advanceSeasonPlayers() {
        int qbNeeds=0, rbNeeds=0, wrNeeds=0, kNeeds=0, olNeeds=0, sNeeds=0, cbNeeds=0, f7Needs=0;
        if (playersLeaving.isEmpty()) {
            int i = 0;
            while (i < teamQBs.size()) {
                if (teamQBs.get(i).year == 4 || (teamQBs.get(i).year == 3 && teamQBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    teamQBs.remove(i);
                    qbNeeds++;
                } else {
                    teamQBs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamRBs.size()) {
                if (teamRBs.get(i).year == 4 || (teamRBs.get(i).year == 3 && teamRBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    teamRBs.remove(i);
                    rbNeeds++;
                } else {
                    teamRBs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamWRs.size()) {
                if (teamWRs.get(i).year == 4 || (teamWRs.get(i).year == 3 && teamWRs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    teamWRs.remove(i);
                    wrNeeds++;
                } else {
                    teamWRs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamKs.size()) {
                if (teamKs.get(i).year == 4) {
                    teamKs.remove(i);
                    kNeeds++;
                } else {
                    teamKs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamOLs.size()) {
                if (teamOLs.get(i).year == 4 || (teamOLs.get(i).year == 3 && teamOLs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    teamOLs.remove(i);
                    olNeeds++;
                } else {
                    teamOLs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamSs.size()) {
                if (teamSs.get(i).year == 4 || (teamSs.get(i).year == 3 && teamSs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    teamSs.remove(i);
                    sNeeds++;
                } else {
                    teamSs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamCBs.size()) {
                if (teamCBs.get(i).year == 4 || (teamCBs.get(i).year == 3 && teamCBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    teamCBs.remove(i);
                    cbNeeds++;
                } else {
                    teamCBs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamF7s.size()) {
                if (teamF7s.get(i).year == 4 || (teamF7s.get(i).year == 3 && teamF7s.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    teamF7s.remove(i);
                    f7Needs++;
                } else {
                    teamF7s.get(i).advanceSeason();
                    i++;
                }
            }

            if (!userControlled) {
                recruitPlayersFreshman(qbNeeds, rbNeeds, wrNeeds, kNeeds, olNeeds, sNeeds, cbNeeds, f7Needs);
                resetStats();
            }

        } else {
            // Just remove the players that are in playersLeaving
            int i = 0;
            while (i < teamQBs.size()) {
                if (playersLeaving.contains(teamQBs.get(i))) {
                    teamQBs.remove(i);
                    qbNeeds++;
                } else {
                    teamQBs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamRBs.size()) {
                if (playersLeaving.contains(teamRBs.get(i))) {
                    teamRBs.remove(i);
                    rbNeeds++;
                } else {
                    teamRBs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamWRs.size()) {
                if (playersLeaving.contains(teamWRs.get(i))) {
                    teamWRs.remove(i);
                    wrNeeds++;
                } else {
                    teamWRs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamKs.size()) {
                if (playersLeaving.contains(teamKs.get(i))) {
                    teamKs.remove(i);
                    kNeeds++;
                } else {
                    teamKs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamOLs.size()) {
                if (playersLeaving.contains(teamOLs.get(i))) {
                    teamOLs.remove(i);
                    olNeeds++;
                } else {
                    teamOLs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamSs.size()) {
                if (playersLeaving.contains(teamSs.get(i))) {
                    teamSs.remove(i);
                    sNeeds++;
                } else {
                    teamSs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamCBs.size()) {
                if (playersLeaving.contains(teamCBs.get(i))) {
                    teamCBs.remove(i);
                    cbNeeds++;
                } else {
                    teamCBs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamF7s.size()) {
                if (playersLeaving.contains(teamF7s.get(i))) {
                    teamF7s.remove(i);
                    f7Needs++;
                } else {
                    teamF7s.get(i).advanceSeason();
                    i++;
                }
            }
        }
    }
    
    /**
     * Recruits the needed amount of players at each position.
     * Rating of each player based on team prestige.
     * This is used when first creating a team.
     * @param qbNeeds
     * @param rbNeeds
     * @param wrNeeds
     * @param kNeeds
     * @param olNeeds
     * @param sNeeds
     * @param cbNeeds
     * @param f7Needs 
     */
    public void recruitPlayers( int qbNeeds, int rbNeeds, int wrNeeds, int kNeeds, 
                                int olNeeds, int sNeeds, int cbNeeds, int f7Needs ) {
        //make team
        int stars = teamPrestige/20 + 1;
        int chance = 20 - (teamPrestige - 20*( teamPrestige/20 )); //between 0 and 20
        
        for( int i = 0; i < qbNeeds; ++i ) {
            //make QBs
            if ( 100*Math.random() < 5*chance ) {
                teamQBs.add( new PlayerQB(league.getRandName(), (int)(4*Math.random() + 1), stars-1, this) );
            } else {
                teamQBs.add( new PlayerQB(league.getRandName(), (int)(4*Math.random() + 1), stars, this) );
            }
        }
        
        for( int i = 0; i < kNeeds; ++i ) {
            //make Ks
            if ( 100*Math.random() < 5*chance ) {
                teamKs.add( new PlayerK(league.getRandName(), (int)(4*Math.random() + 1), stars-1) );
            } else {
                teamKs.add( new PlayerK(league.getRandName(), (int)(4*Math.random() + 1), stars) );
            }
        }
        
        for( int i = 0; i < rbNeeds; ++i ) {
            //make RBs
            if ( 100*Math.random() < 5*chance ) {
                teamRBs.add( new PlayerRB(league.getRandName(), (int)(4*Math.random() + 1), stars-1, this) );
            } else {
                teamRBs.add( new PlayerRB(league.getRandName(), (int)(4*Math.random() + 1), stars, this) );
            }
        }
        
        for( int i = 0; i < wrNeeds; ++i ) {
            //make WRs
            if ( 100*Math.random() < 5*chance ) {
                teamWRs.add( new PlayerWR(league.getRandName(), (int)(4*Math.random() + 1), stars-1, this) );
            } else {
                teamWRs.add( new PlayerWR(league.getRandName(), (int)(4*Math.random() + 1), stars, this) );
            }
        }
        
        for( int i = 0; i < olNeeds; ++i ) {
            //make OLs
            if ( 100*Math.random() < 5*chance ) {
                teamOLs.add( new PlayerOL(league.getRandName(), (int)(4*Math.random() + 1), stars-1) );
            } else {
                teamOLs.add( new PlayerOL(league.getRandName(), (int)(4*Math.random() + 1), stars) );
            }
        }
        
        for( int i = 0; i < cbNeeds; ++i ) {
            //make CBs
            if ( 100*Math.random() < 5*chance ) {
                teamCBs.add( new PlayerCB(league.getRandName(), (int)(4*Math.random() + 1), stars-1) );
            } else {
                teamCBs.add( new PlayerCB(league.getRandName(), (int)(4*Math.random() + 1), stars) );
            }
        }
        
        for( int i = 0; i < f7Needs; ++i ) {
            //make F7s
            if ( 100*Math.random() < 5*chance ) {
                teamF7s.add( new PlayerF7(league.getRandName(), (int)(4*Math.random() + 1), stars-1) );
            } else {
                teamF7s.add( new PlayerF7(league.getRandName(), (int)(4*Math.random() + 1), stars) );
            }
        }
        
        for( int i = 0; i < sNeeds; ++i ) {
            //make Ss
            if ( 100*Math.random() < 5*chance ) {
                teamSs.add( new PlayerS(league.getRandName(), (int)(4*Math.random() + 1), stars-1) );
            } else {
                teamSs.add( new PlayerS(league.getRandName(), (int)(4*Math.random() + 1), stars) );
            }
        }
        
        //done making players, sort them
        sortPlayers();
    }
    
    /**
     * Recruit freshman at each position.
     * This is used after each season.
     * @param qbNeeds
     * @param rbNeeds
     * @param wrNeeds
     * @param kNeeds
     * @param olNeeds
     * @param sNeeds
     * @param cbNeeds
     * @param f7Needs 
     */
    public void recruitPlayersFreshman( int qbNeeds, int rbNeeds, int wrNeeds, int kNeeds, 
                                int olNeeds, int sNeeds, int cbNeeds, int f7Needs ) {
        //make team
        int stars = teamPrestige/20 + 1;
        int chance = 20 - (teamPrestige - 20*( teamPrestige/20 )); //between 0 and 20

        double starsBonusChance = 0.15;
        double starsBonusDoubleChance = 0.05;
        
        for( int i = 0; i < qbNeeds; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make QBs
            if ( 100*Math.random() < 5*chance ) {
                teamQBs.add( new PlayerQB(league.getRandName(), 1, stars-1, this) );
            } else {
                teamQBs.add( new PlayerQB(league.getRandName(), 1, stars, this) );
            }
        }
        
        for( int i = 0; i < kNeeds; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make Ks
            if ( 100*Math.random() < 5*chance ) {
                teamKs.add( new PlayerK(league.getRandName(), 1, stars-1) );
            } else {
                teamKs.add( new PlayerK(league.getRandName(), 1, stars) );
            }
        }
        
        for( int i = 0; i < rbNeeds; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make RBs
            if ( 100*Math.random() < 5*chance ) {
                teamRBs.add( new PlayerRB(league.getRandName(), 1, stars-1, this) );
            } else {
                teamRBs.add( new PlayerRB(league.getRandName(), 1, stars, this) );
            }
        }
        
        for( int i = 0; i < wrNeeds; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make WRs
            if ( 100*Math.random() < 5*chance ) {
                teamWRs.add( new PlayerWR(league.getRandName(), 1, stars-1, this) );
            } else {
                teamWRs.add( new PlayerWR(league.getRandName(), 1, stars, this) );
            }
        }
        
        for( int i = 0; i < olNeeds; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make OLs
            if ( 100*Math.random() < 5*chance ) {
                teamOLs.add( new PlayerOL(league.getRandName(), 1, stars-1) );
            } else {
                teamOLs.add( new PlayerOL(league.getRandName(), 1, stars) );
            }
        }
        
        for( int i = 0; i < cbNeeds; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make CBs
            if ( 100*Math.random() < 5*chance ) {
                teamCBs.add( new PlayerCB(league.getRandName(), 1, stars-1) );
            } else {
                teamCBs.add( new PlayerCB(league.getRandName(), 1, stars) );
            }
        }
        
        for( int i = 0; i < f7Needs; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make F7s
            if ( 100*Math.random() < 5*chance ) {
                teamF7s.add( new PlayerF7(league.getRandName(), 1, stars-1) );
            } else {
                teamF7s.add( new PlayerF7(league.getRandName(), 1, stars) );
            }
        }
        
        for( int i = 0; i < sNeeds; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make Ss
            if ( 100*Math.random() < 5*chance ) {
                teamSs.add( new PlayerS(league.getRandName(), 1, stars-1) );
            } else {
                teamSs.add( new PlayerS(league.getRandName(), 1, stars) );
            }
        }
        
        //done making players, sort them
        sortPlayers();
    }
    
    /**
     * Recruits walk ons at each needed position.
     * This is used by user teams if there is a dearth at any position.
     */
    public void recruitWalkOns() {
        //recruit walk ons (used for player teams who dont recruit all needs)
        int needs = 2 - teamQBs.size();
        for( int i = 0; i < needs; ++i ) {
            //make QBs
            teamQBs.add( new PlayerQB(league.getRandName(), 1, 2, this) );
        }
        
        needs = 4 - teamRBs.size();
        for( int i = 0; i < needs; ++i ) {
            //make RBs
            teamRBs.add( new PlayerRB(league.getRandName(), 1, 2, this) );
        }
        
        needs = 6 - teamWRs.size();
        for( int i = 0; i < needs; ++i ) {
            //make WRs
            teamWRs.add( new PlayerWR(league.getRandName(), 1, 2, this) );
        }
        
        needs = 10 - teamOLs.size();
        for( int i = 0; i < needs; ++i ) {
            //make OLs
            teamOLs.add( new PlayerOL(league.getRandName(), 1, 2) );
        }
        
        needs = 2 - teamKs.size();
        for( int i = 0; i < needs; ++i ) {
            //make Ks
            teamKs.add( new PlayerK(league.getRandName(), 1, 2) );
        }
        
        needs = 2 - teamSs.size();
        for( int i = 0; i < needs; ++i ) {
            //make Ss
            teamSs.add( new PlayerS(league.getRandName(), 1, 2) );
        }
        
        needs = 6 - teamCBs.size();
        for( int i = 0; i < needs; ++i ) {
            //make Ss
            teamCBs.add( new PlayerCB(league.getRandName(), 1, 2) );
        }
        
        needs = 14 - teamF7s.size();
        for( int i = 0; i < needs; ++i ) {
            //make Ss
            teamF7s.add( new PlayerF7(league.getRandName(), 1, 2) );
        }
        
        //done making players, sort them
        sortPlayers();
    }

    /**
     * Recruit all players given in a string
     */
    public void recruitPlayersFromStr(String playersStr) {
        String[] players = playersStr.split("%\n");
        String currLine = players[0];
        int i = 0;
        while (!currLine.equals("END_RECRUITS")) {
            recruitPlayerCSV(currLine, false);
            currLine = players[++i];
        }

        // Recruit Walk-ons before redshirts so that they don't affect position needs
        recruitWalkOns();

        currLine = players[++i]; // skip over END_RECRUITS line
        while (!currLine.equals("END_REDSHIRTS")) {
            recruitPlayerCSV(currLine, true);
            currLine = players[++i];
        }

    }

    /**
     * Recruit player given a CSV string
     * @param line player to be recruited
     * @param isRedshirt whether that player should be recruited as a RS
     */
    private void recruitPlayerCSV(String line, boolean isRedshirt) {
        String[] playerInfo = line.split(",");
        if (playerInfo[0].equals("QB")) {
            teamQBs.add( new PlayerQB(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt));
        } else if (playerInfo[0].equals("RB")) {
            teamRBs.add( new PlayerRB(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt));
        } else if (playerInfo[0].equals("WR")) {
            teamWRs.add( new PlayerWR(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt));
        } else if (playerInfo[0].equals("OL")) {
            teamOLs.add( new PlayerOL(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt));
        } else if (playerInfo[0].equals("K")) {
            teamKs.add( new PlayerK(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt));
        } else if (playerInfo[0].equals("S")) {
            teamSs.add( new PlayerS(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt));
        } else if (playerInfo[0].equals("CB")) {
            teamCBs.add( new PlayerCB(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt));
        } else if (playerInfo[0].equals("F7")) {
            teamF7s.add( new PlayerF7(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt));
        }
    }
    
    /**
     * Resets all team stats to 0.
     */
    public void resetStats() {
        //reset stats
        gameSchedule = new ArrayList<Game>();
        gameOOCSchedule0 = null;
        gameOOCSchedule4 = null;
        gameOOCSchedule9 = null;
        gameWinsAgainst = new ArrayList<Team>();
        gameWLSchedule = new ArrayList<String>();
        confChampion = "";
        semiFinalWL = "";
        natChampWL = "";   
        wins = 0;
        losses = 0;

        //set stats
        teamPoints = 0;
        teamOppPoints = 0;
        teamYards = 0;
        teamOppYards = 0;
        teamPassYards = 0;
        teamRushYards = 0;
        teamOppPassYards = 0;
        teamOppRushYards = 0;
        teamTODiff = 0;
        /*diffOffTalent = getOffTalent() - teamOffTalent;
        teamOffTalent = getOffTalent();
        diffDefTalent = getOffTalent() - teamDefTalent;
        teamDefTalent = getDefTalent();

        teamPollScore = teamPrestige + getOffTalent() + getDefTalent();*/
    }
    
    /**
     * Updates poll score based on team stats.
     */
    public void updatePollScore() {
        updateStrengthOfWins();
        int preseasonBias = 8 - (wins + losses);
        if (preseasonBias < 0) preseasonBias = 0;
        teamPollScore = (wins*200 + 3*(teamPoints-teamOppPoints) +
                (teamYards-teamOppYards)/40 +
                3*(preseasonBias)*(teamPrestige + getOffTalent() + getDefTalent()) +
                teamStrengthOfWins)/10;
        if ( "CC".equals(confChampion) ) {
            //bonus for winning conference
            teamPollScore += 50;
        }
        if ( "NCW".equals(natChampWL) ) {
            //bonus for winning champ game
            teamPollScore += 100;
        }
        if ( losses == 0 ) {
            teamPollScore += 30;
        } else if ( losses == 1 ) {
            teamPollScore += 15;
        }
    }
    
    /**
     * Updates team history.
     */
    public void updateTeamHistory() {
        teamHistory.add(league.getYear() + ": #" + rankTeamPollScore + " " + abbr + " (" + wins + "-" + losses + ") "
                + confChampion + " " + semiFinalWL + natChampWL);
    }

    /**
     * Gets a string of the entire team history
     */
    public String getTeamHistoryStr() {
        String hist = "";
        hist += "Overall W-L: " + totalWins + "-" + totalLosses + "\n";
        hist += "Conf Champ Record: " + totalCCs + "-" + totalCCLosses + "\n";
        hist += "Bowl Game Record: " + totalBowls + "-" + totalBowlLosses + "\n";
        hist += "National Champ Record: " + totalNCs + "-" + totalNCLosses + "\n";
        hist += "\nYear by year summary:\n";
        for (int i = 0; i < teamHistory.size(); ++i) {
            hist += teamHistory.get(i) + "\n";
        }
        return hist;
    }
    
    /**
     * Updates strength of wins based on how opponents have fared.
     */
    public void updateStrengthOfWins() {
        int strWins = 0;
        for ( int i = 0; i < 12; ++i ) {
            Game g = gameSchedule.get(i);
            if (g.homeTeam == this) {
                strWins += Math.pow(60 - g.awayTeam.rankTeamPollScore,2);
            } else {
                strWins += Math.pow(60 - g.homeTeam.rankTeamPollScore,2);
            }
        }
        teamStrengthOfWins = strWins/50;
        for (Team t : gameWinsAgainst) {
            teamStrengthOfWins += Math.pow(t.wins,2);
        }
    }
    
    /**
     * Sorts players so that best players are higher in depth chart.
     */
    public void sortPlayers() {
        //sort players based on overall ratings to assemble best starting lineup
        Collections.sort( teamQBs, new PlayerComparator() );
        Collections.sort( teamRBs, new PlayerComparator() );
        Collections.sort( teamWRs, new PlayerComparator() );
        Collections.sort( teamKs, new PlayerComparator() );
        Collections.sort( teamOLs, new PlayerComparator() );
        Collections.sort( teamCBs, new PlayerComparator() );
        Collections.sort( teamSs, new PlayerComparator() );
        Collections.sort(teamF7s, new PlayerComparator());
    }
    
    
    /**
     * Calculates offensive talent level of team.
     * @return Offensive Talent Level
     */
    public int getOffTalent() {
        return ( getQB(0).ratOvr*5 + 
                 teamWRs.get(0).ratOvr + teamWRs.get(1).ratOvr + teamWRs.get(2).ratOvr +
                 teamRBs.get(0).ratOvr + teamRBs.get(1).ratOvr +
                 getCompositeOLPass() + getCompositeOLRush() ) / 12;
    }
    
    /**
     * Calculates defensive talent level of team.
     * @return Defensive Talent Level
     */
    public int getDefTalent() {
        return ( getRushDef() + getPassDef() ) / 2;
    }
    
    public PlayerQB getQB(int depth) {
        if ( depth < teamQBs.size() && depth >= 0 ) {
            return teamQBs.get(depth);
        } else {
            return teamQBs.get(0);
        }
    }
    
    public PlayerRB getRB(int depth) {
        if ( depth < teamRBs.size() && depth >= 0 ) {
            return teamRBs.get(depth);
        } else {
            return teamRBs.get(0);
        }
    }
    
    public PlayerWR getWR(int depth) {
        if ( depth < teamWRs.size() && depth >= 0 ) {
            return teamWRs.get(depth);
        } else {
            return teamWRs.get(0);
        }
    }
    
    public PlayerK getK(int depth) {
        if ( depth < teamKs.size() && depth >= 0 ) {
            return teamKs.get(depth);
        } else {
            return teamKs.get(0);
        }
    }
    
    public PlayerOL getOL(int depth) {
        if ( depth < teamOLs.size() && depth >= 0 ) {
            return teamOLs.get(depth);
        } else {
            return teamOLs.get(0);
        }
    }
    
    public PlayerS getS(int depth) {
        if ( depth < teamSs.size() && depth >= 0 ) {
            return teamSs.get(depth);
        } else {
            return teamSs.get(0);
        }
    }
    
    public PlayerCB getCB(int depth) {
        if ( depth < teamCBs.size() && depth >= 0 ) {
            return teamCBs.get(depth);
        } else {
            return teamCBs.get(0);
        }
    }
    
    public PlayerF7 getF7(int depth) {
        if ( depth < teamF7s.size() && depth >= 0 ) {
            return teamF7s.get(depth);
        } else {
            return teamF7s.get(0);
        }
    }

    /**
     * Get pass proficiency. The higher the more likely the team is to pass.
     * @return integer of how good the team is at passing
     */
    public int getPassProf() {
        int avgWRs = ( teamWRs.get(0).ratOvr + teamWRs.get(1).ratOvr + teamWRs.get(2).ratOvr)/3;
        return (getCompositeOLPass() + getQB(0).ratOvr*2 + avgWRs)/4;
    }

    /**
     * Get run proficiency. The higher the more likely the team is to run.
     * @return integer of how good the team is at rushing
     */
    public int getRushProf() {
        int avgRBs = ( teamRBs.get(0).ratOvr + teamRBs.get(1).ratOvr )/2;
        return (getCompositeOLRush() + avgRBs )/2;
    }

    /**
     * Get how good the team is at defending the pass
     * @return integer of how good
     */
    public int getPassDef() {
        int avgCBs = ( teamCBs.get(0).ratOvr + teamCBs.get(1).ratOvr + teamCBs.get(2).ratOvr)/3;
        return (avgCBs*3 + teamSs.get(0).ratOvr + getCompositeF7Pass()*2)/6;
    }

    /**
     * Get how good the team is at defending the rush
     * @return integer of how good
     */
    public int getRushDef() {
        return getCompositeF7Rush();
    }

    /**
     * Get how good the OL is at defending the pass
     * Is the average of power and pass blocking.
     * @return how good they are at blocking the pass.
     */
    public int getCompositeOLPass() {
        int compositeOL = 0;
        for ( int i = 0; i < 5; ++i ) {
            compositeOL += (teamOLs.get(i).ratOLPow + teamOLs.get(i).ratOLBkP)/2;
        }
        return compositeOL / 5;
    }

    /**
     * Get how good the OL is at defending the rush
     * Is the average of power and rush blocking.
     * @return how good they are at blocking the rush.
     */
    public int getCompositeOLRush() {
        int compositeOL = 0;
        for ( int i = 0; i < 5; ++i ) {
            compositeOL += (teamOLs.get(i).ratOLPow + teamOLs.get(i).ratOLBkR)/2;
        }
        return compositeOL / 5;
    }

    /**
     * Get how good the F7 is at defending the pass.
     * Is the average of power and pass pressure.
     * @return how good they are at putting pressure on passer.
     */
    public int getCompositeF7Pass() {
        int compositeF7 = 0;
        for ( int i = 0; i < 7; ++i ) {
            compositeF7 += (teamF7s.get(i).ratF7Pow + teamF7s.get(i).ratF7Pas)/2;
        }
        return compositeF7 / 7;
    }

    /**
     * Get how good the F7 is at defending the run.
     * Is the average of power and run stopping.
     * @return how good they are at stopping the RB.
     */
    public int getCompositeF7Rush() {
        int compositeF7 = 0;
        for ( int i = 0; i < 7; ++i ) {
            compositeF7 += (teamF7s.get(i).ratF7Pow + teamF7s.get(i).ratF7Rsh)/2;
        }
        return compositeF7 / 7;
    }

    /**
     * Get comma separated value of the team stats and their rankings.
     * @return String of CSV stat,name,ranking
     */
    public String getTeamStatsStrCSV() {
        StringBuilder ts0 = new StringBuilder();

        ts0.append(teamPollScore + ",");
        ts0.append("AP Votes" + ",");
        ts0.append(getRankStr(rankTeamPollScore) + "%\n");

        ts0.append(teamStrengthOfWins + ",");
        ts0.append("SOS" + ",");
        ts0.append(getRankStr(rankTeamStrengthOfWins) + "%\n");

        ts0.append(teamPoints/numGames() + ",");
        ts0.append("Points" + ",");
        ts0.append(getRankStr(rankTeamPoints) + "%\n");

        ts0.append(teamOppPoints/numGames() + ",");
        ts0.append("Opp Points" + ",");
        ts0.append(getRankStr(rankTeamOppPoints) + "%\n");

        ts0.append(teamYards/numGames() + ",");
        ts0.append("Yards" + ",");
        ts0.append(getRankStr(rankTeamYards) + "%\n");

        ts0.append(teamOppYards/numGames() + ",");
        ts0.append("Opp Yards" + ",");
        ts0.append(getRankStr(rankTeamOppYards) + "%\n");

        ts0.append(teamPassYards/numGames() + ",");
        ts0.append("Pass Yards" + ",");
        ts0.append(getRankStr(rankTeamPassYards) + "%\n");

        ts0.append(teamRushYards/numGames() + ",");
        ts0.append("Rush Yards" + ",");
        ts0.append(getRankStr(rankTeamRushYards) + "%\n");

        ts0.append(teamOppPassYards/numGames() + ",");
        ts0.append("Opp Pass YPG" + ",");
        ts0.append(getRankStr(rankTeamOppPassYards) + "%\n");

        ts0.append(teamOppRushYards/numGames() + ",");
        ts0.append("Opp Rush YPG" + ",");
        ts0.append(getRankStr(rankTeamOppRushYards) + "%\n");

        if (teamTODiff > 0) ts0.append("+" + teamTODiff + ",");
        else ts0.append(teamTODiff + ",");
        ts0.append("TO Diff" + ",");
        ts0.append(getRankStr(rankTeamTODiff) + "%\n");

        ts0.append(teamOffTalent + ",");
        ts0.append("Off Talent" + ",");
        ts0.append(getRankStr(rankTeamOffTalent) + "%\n");

        ts0.append(teamDefTalent + ",");
        ts0.append("Def Talent" + ",");
        ts0.append(getRankStr(rankTeamDefTalent) + "%\n");

        ts0.append(teamPrestige + ",");
        ts0.append("Prestige" + ",");
        ts0.append(getRankStr(rankTeamPrestige) + "%\n");

        return ts0.toString();
    }

    /**
     * Get the game summary of a played game.
     * [gameName, score summary, who they played]
     * @param gameNumber number of the game desired
     * @return array of name, score, who was played
     */
    public String[] getGameSummaryStr(int gameNumber) {
        String[] gs = new String[3];
        Game g = gameSchedule.get(gameNumber);
        gs[0] = g.gameName;
        if (gameNumber < gameWLSchedule.size()) {
            gs[1] = gameWLSchedule.get(gameNumber) + " " + gameSummaryStrScore(g);
            if (g.numOT > 0) gs[1] += " (" + g.numOT + "OT)";
        } else {
            gs[1] = "---";
        }
        gs[2] = gameSummaryStrOpponent(g);
        return gs;
    }

    /**
     * Get a summary of your team's season.
     * Tells how they finished, if they beat/fell short of expecations, and if they won rivalry game.
     * @return String of season summary
     */
    public String seasonSummaryStr() {
        String summary = "Your team, " + name + ", finished the season ranked #" + rankTeamPollScore + " with " + wins + " wins and " + losses + " losses.";
        int expectedPollFinish = 100 - teamPrestige;
        int diffExpected = expectedPollFinish - rankTeamPollScore;
        int oldPrestige = teamPrestige;
        int newPrestige = oldPrestige;
        if ( teamPrestige > 45 || diffExpected > 0 ) {
            newPrestige = (int)Math.pow(teamPrestige, 1 + (float)diffExpected/1500);// + diffExpected/2500);
        }

        if ( natChampWL.equals("NCW") ) {
            summary += "\n\nYou won the National Championship! Recruits want to play for winners and you have proved that you are one. You gain +3 prestige!";
        }

        if ((newPrestige - oldPrestige) > 0) {
            summary += "\n\nGreat job coach! You exceeded expectations and gained " + (newPrestige - oldPrestige) + " prestige! This will help your recruiting.";
        } else if ((newPrestige - oldPrestige) < 0) {
            summary += "\n\nA bit of a down year, coach? You fell short expectations and lost " + (oldPrestige - newPrestige) + " prestige. This will hurt your recruiting.";
        } else {
            summary += "\n\nWell, your team performed exactly how many expected. This won't hurt or help recruiting, but try to improve next year!";
        }

        if ( wonRivalryGame && (teamPrestige - league.findTeamAbbr(rivalTeam).teamPrestige < 20) ) {
            summary += "\n\nFuture recruits were impressed that you won your rivalry game. You gained 2 prestige.";
        } else if (!wonRivalryGame && (league.findTeamAbbr(rivalTeam).teamPrestige - teamPrestige < 20 || name.equals("American Samoa"))) {
            summary += "\n\nSince you couldn't win your rivalry game, recruits aren't excited to attend your school. You lost 2 prestige.";
        } else if ( wonRivalryGame ) {
            summary += "\n\nGood job winning your rivalry game, but it was expected given the state of their program. You gain no prestige for this.";
        } else {
            summary += "\n\nYou lost your rivalry game, but this was expected given your rebuilding program. You lost no prestige for this.";
        }

        return summary;
    }

    /**
     * Gets player name or detail strings for displaying in the roster tab via expandable list.
     * Should be separated by a '>' from left text and right text.
     * @return list of players with their name,ovr,por,etc
     */
    public List<String> getPlayerStatsExpandListStr() {
        ArrayList<String> pList = new ArrayList<String>();

        pList.add(getQB(0).getPosNameYrOvrPot_Str());

        for (int i = 0; i < 2; ++i) {
            pList.add(getRB(i).getPosNameYrOvrPot_Str());
        }

        for (int i = 0; i < 3; ++i) {
            pList.add(getWR(i).getPosNameYrOvrPot_Str());
        }

        for (int i = 0; i < 5; ++i) {
            pList.add(getOL(i).getPosNameYrOvrPot_Str());
        }

        pList.add(getK(0).getPosNameYrOvrPot_Str());

        pList.add(getS(0).getPosNameYrOvrPot_Str());

        for (int i = 0; i < 3; ++i) {
            pList.add(getCB(i).getPosNameYrOvrPot_Str());
        }

        for (int i = 0; i < 7; ++i) {
            pList.add(getF7(i).getPosNameYrOvrPot_Str());
        }

        pList.add("BENCH > BENCH");

        return pList;
    }

    /**
     * Creates the map needed for making the expandable list view used in the player stats.
     * @param playerStatsGroupHeaders list of players by name,overall,pot,etc
     * @return mapping of each player to their detail ratings
     */
    public Map<String, List<String>> getPlayerStatsExpandListMap(List<String> playerStatsGroupHeaders) {
        Map<String, List<String>> playerStatsMap = new LinkedHashMap<String, List<String>>();

        String ph; //player header
        ArrayList<String> pInfoList;

        //QB
        ph = playerStatsGroupHeaders.get(0);
        playerStatsMap.put(ph, getQB(0).getDetailStatsList(numGames()));

        for (int i = 1; i < 3; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getRB(i - 1).getDetailStatsList(numGames()));
        }

        for (int i = 3; i < 6; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getWR(i - 3).getDetailStatsList(numGames()));
        }

        for (int i = 6; i < 11; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getOL(i - 6).getDetailStatsList(numGames()));
        }

        ph = playerStatsGroupHeaders.get(11);
        playerStatsMap.put(ph, getK(0).getDetailStatsList(numGames()));

        ph = playerStatsGroupHeaders.get(12);
        playerStatsMap.put(ph, getS(0).getDetailStatsList(numGames()));

        for (int i = 13; i < 16; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getCB(i - 13).getDetailStatsList(numGames()));
        }

        for (int i = 16; i < 23; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getF7(i - 16).getDetailStatsList(numGames()));
        }

        //Bench
        ph = playerStatsGroupHeaders.get(23);
        ArrayList<String> benchStr = new ArrayList<>();
        for ( int i = 1; i < teamQBs.size(); ++i) {
            benchStr.add( getQB(i).getPosNameYrOvrPot_Str() );
        }
        for ( int i = 2; i < teamRBs.size(); ++i) {
            benchStr.add( getRB(i).getPosNameYrOvrPot_Str() );
        }
        for ( int i = 3; i < teamWRs.size(); ++i) {
            benchStr.add( getWR(i).getPosNameYrOvrPot_Str() );
        }
        for ( int i = 5; i < teamOLs.size(); ++i) {
            benchStr.add( getOL(i).getPosNameYrOvrPot_Str() );
        }
        for ( int i = 1; i < teamKs.size(); ++i) {
            benchStr.add( getK(i).getPosNameYrOvrPot_Str() );
        }
        for ( int i = 1; i < teamSs.size(); ++i) {
            benchStr.add(getS(i).getPosNameYrOvrPot_Str());
        }
        for ( int i = 3; i < teamCBs.size(); ++i) {
            benchStr.add( getCB(i).getPosNameYrOvrPot_Str() );
        }
        for ( int i = 7; i < teamF7s.size(); ++i) {
            benchStr.add( getF7(i).getPosNameYrOvrPot_Str() );
        }
        playerStatsMap.put(ph, benchStr);

        return playerStatsMap;
    }

    /**
     * Gets rank str, i.e. 12 -> 12th, 3 -> 3rd
     * @param num ranking
     * @return string of the ranking with correct ending
     */
    public String getRankStr(int num) {
        if (num == 11) {
            return "11th";
        } else if (num == 12) {
            return "12th";
        } else if (num == 13) {
            return "13th";
        } else if (num%10 == 1) {
            return num + "st";
        } else if (num%10 == 2) {
            return num + "nd";
        } else if (num%10 == 3){
            return num + "rd";
        } else {
            return num + "th";
        }
    }

    /**
     * Get rank string of the user (no longer used?)
     * @param num ranking
     * @return ranking with correct ending
     */
    public String getRankStrStarUser(int num) {
        if (true) {
            if (num == 11) {
                return "11th";
            } else if (num == 12) {
                return "12th";
            } else if (num == 13) {
                return "13th";
            } else if (num % 10 == 1) {
                return num + "st";
            } else if (num % 10 == 2) {
                return num + "nd";
            } else if (num % 10 == 3) {
                return num + "rd";
            } else {
                return num + "th";
            }
        } else {
            if (num == 11) {
                return "** 11th **";
            } else if (num == 12) {
                return "** 12th **";
            } else if (num == 13) {
                return "** 13th **";
            } else if (num % 10 == 1) {
                return "** " + num + "st **";
            } else if (num % 10 == 2) {
                return "** " + num + "nd **";
            } else if (num % 10 == 3) {
                return "** " + num + "rd **";
            } else {
                return "** " + num + "th **";
            }
        }
    }

    /**
     * Gets the number of games played so far
     * @return number of games played
     */
    public int numGames() {
        if ( wins + losses > 0 ) {
            return wins + losses;
        } else return 1;
    }

    /**
     * Gets the number of in-conference wins, used for CCG rankings
     * @return number of in-conf wins
     */
    public int getConfWins() {
        int confWins = 0;
        Game g;
        for (int i = 0; i < gameWLSchedule.size(); ++i) {
            g = gameSchedule.get(i);
            if ( g.gameName.equals("In Conf") || g.gameName.equals("Rivalry Game") ) {
                // in conference game, see if was won
                if ( g.homeTeam == this && g.homeScore > g.awayScore ) {
                    confWins++;
                } else if ( g.awayTeam == this && g.homeScore < g.awayScore ) {
                    confWins++;
                }
            }
        }
        return confWins;
    }

    /**
     * Str rep of team, no bowl results
     * @return ranking abbr (w-l)
     */
    public String strRep() {
        return "#" + rankTeamPollScore + " " + abbr + " (" + wins + "-" + losses + ")";
    }

    /**
     * Str rep of team, with bowl results
     * @return ranking abbr (w-l) BW
     */
    public String strRepWithBowlResults() {
        return "#" + rankTeamPollScore + " " + abbr + " (" + wins + "-" + losses + ") " + confChampion + " " + semiFinalWL + natChampWL;
    }

    /**
     * Get what happened during the week for the team
     * @return name W/L gameSum, new poll rank #1
     */
    public String weekSummaryStr() {
        int i = wins + losses - 1;
        Game g = gameSchedule.get(i);
        String gameSummary = gameWLSchedule.get(i) + " " + gameSummaryStr(g);
        String rivalryGameStr = "";
        if (g.gameName.equals("Rivalry Game")) {
            if ( gameWLSchedule.get(i).equals("W") ) rivalryGameStr = "Won against Rival!\n";
            else rivalryGameStr = "Lost against Rival!\n";
        }
        return rivalryGameStr + name + " " + gameSummary + "\nNew poll rank: #" + rankTeamPollScore + " " + abbr + " (" + wins + "-" + losses + ")";
    }

    /**
     * Gets the one-line summary of a game
     * @param g Game to get summary from
     * @return 31 - 43 @ GEO #60
     */
    public String gameSummaryStr(Game g) {
        if (g.homeTeam == this) {
            return g.homeScore + " - " + g.awayScore + " vs " + g.awayTeam.abbr + " #" + g.awayTeam.rankTeamPollScore;
        } else {
            return g.awayScore + " - " + g.homeScore + " @ " + g.homeTeam.abbr + " #" + g.homeTeam.rankTeamPollScore;
        }
    }

    /**
     * Get just the score of the game
     * @param g Game to get score from
     * @return "myTeamScore - otherTeamScore"
     */
    public String gameSummaryStrScore(Game g) {
        if (g.homeTeam == this) {
            return g.homeScore + " - " + g.awayScore;
        } else {
            return g.awayScore + " - " + g.homeScore;
        }
    }

    /**
     * Get the vs/@ part of the game summary
     * @param g Game to get from
     * @return vs OPP #45
     */
    public String gameSummaryStrOpponent(Game g) {
        if (g.homeTeam == this) {
            return "vs " + g.awayTeam.abbr + " #" + g.awayTeam.rankTeamPollScore;
        } else {
            return "@ " + g.homeTeam.abbr + " #" + g.homeTeam.rankTeamPollScore;
        }
    }

    /**
     * Get String of who all is graduating from the team
     * @return string of everyone who is graduating sorted by position
     */
    public String getGraduatingPlayersStr() {
        StringBuilder sb = new StringBuilder();
        for (Player p : playersLeaving) {
            sb.append(p.getPosNameYrOvrPot_OneLine() +"\n");
        }
        return sb.toString();
    }

    /**
     * Get string of the current team needs (not used anymore?)
     * @return String of all the position needs
     */
    public String getTeamNeeds() {
        StringBuilder needs = new StringBuilder();
        needs.append("\t\t"+(2-teamQBs.size())+ "QBs, ");
        needs.append((4-teamRBs.size())+ "RBs, ");
        needs.append((6-teamWRs.size())+ "WRs, ");
        needs.append((2-teamKs.size())+ "Ks\n");
        needs.append("\t\t"+(10-teamOLs.size())+ "OLs, ");
        needs.append((2-teamSs.size())+ "Ss, ");
        needs.append((6-teamCBs.size())+ "CBs, ");
        needs.append((14-teamF7s.size())+ "F7s");
        return needs.toString();
    }

    public PlayerQB[] getQBRecruits() {
        PlayerQB[] recruits = new PlayerQB[numRecruits];
        int stars;
        for (int i = 0; i < numRecruits; ++i) {
            stars = (int)(5*(float)(numRecruits - i/2)/numRecruits);
            recruits[i] = new PlayerQB(league.getRandName(), 1, stars, null);
        }
        Arrays.sort(recruits, new PlayerComparator());
        return recruits;
    }

    public PlayerRB[] getRBRecruits() {
        int numRBrecruits = 2*numRecruits;
        PlayerRB[] recruits = new PlayerRB[numRBrecruits];
        int stars;
        for (int i = 0; i < numRBrecruits; ++i) {
            stars = (int)(5*(float)(numRBrecruits - i/2)/numRBrecruits);
            recruits[i] = new PlayerRB(league.getRandName(), 1, stars, null);
        }
        Arrays.sort(recruits, new PlayerComparator());
        return recruits;
    }

    public PlayerWR[] getWRRecruits() {
        int adjNumRecruits = 2*numRecruits;
        PlayerWR[] recruits = new PlayerWR[adjNumRecruits];
        int stars;
        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int)(5*(float)(adjNumRecruits - i/2)/adjNumRecruits);
            recruits[i] = new PlayerWR(league.getRandName(), 1, stars, null);
        }
        Arrays.sort(recruits, new PlayerComparator());
        return recruits;
    }

    public PlayerOL[] getOLRecruits() {
        int adjNumRecruits = 3*numRecruits;
        PlayerOL[] recruits = new PlayerOL[adjNumRecruits];
        int stars;
        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int)(5*(float)(adjNumRecruits - i/2)/adjNumRecruits);
            recruits[i] = new PlayerOL(league.getRandName(), 1, stars);
        }
        Arrays.sort(recruits, new PlayerComparator());
        return recruits;
    }

    public PlayerK[] getKRecruits() {
        int adjNumRecruits = numRecruits;
        PlayerK[] recruits = new PlayerK[adjNumRecruits];
        int stars;
        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int)(5*(float)(adjNumRecruits - i/2)/adjNumRecruits);
            recruits[i] = new PlayerK(league.getRandName(), 1, stars);
        }
        Arrays.sort(recruits, new PlayerComparator());
        return recruits;
    }

    public PlayerS[] getSRecruits() {
        int adjNumRecruits = numRecruits;
        PlayerS[] recruits = new PlayerS[adjNumRecruits];
        int stars;
        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int)(5*(float)(adjNumRecruits - i/2)/adjNumRecruits);
            recruits[i] = new PlayerS(league.getRandName(), 1, stars);
        }
        Arrays.sort(recruits, new PlayerComparator());
        return recruits;
    }

    public PlayerCB[] getCBRecruits() {
        int adjNumRecruits = 2*numRecruits;
        PlayerCB[] recruits = new PlayerCB[adjNumRecruits];
        int stars;
        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int)(5*(float)(adjNumRecruits - i/2)/adjNumRecruits);
            recruits[i] = new PlayerCB(league.getRandName(), 1, stars);
        }
        Arrays.sort(recruits, new PlayerComparator());
        return recruits;
    }

    public PlayerF7[] getF7Recruits() {
        int adjNumRecruits = 3*numRecruits;
        PlayerF7[] recruits = new PlayerF7[adjNumRecruits];
        int stars;
        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int)(5*(float)(adjNumRecruits - i/2)/adjNumRecruits);
            recruits[i] = new PlayerF7(league.getRandName(), 1, stars);
        }
        Arrays.sort(recruits, new PlayerComparator());
        return recruits;
    }

    /**
     * Save all the recruits into a string to be used by RecruitingActivity
     * @return String of all the recruits
     */
    public String getRecruitsInfoSaveFile() {
        StringBuilder sb = new StringBuilder();
        PlayerQB[] qbs = getQBRecruits();
        for (PlayerQB qb : qbs) {
            sb.append("QB," + qb.name + "," + qb.year + "," + qb.ratPot + "," + qb.ratFootIQ + "," + qb.ratPassPow + "," + qb.ratPassAcc + "," + qb.ratPassEva + "," + qb.ratOvr + "," + qb.cost + ",0%\n");
        }
        PlayerRB[] rbs = getRBRecruits();
        for (PlayerRB rb : rbs) {
            sb.append("RB," + rb.name + "," + rb.year + "," + rb.ratPot + "," + rb.ratFootIQ + "," + rb.ratRushPow + "," + rb.ratRushSpd + "," + rb.ratRushEva + "," + rb.ratOvr + "," + rb.cost + ",0%\n");
        }
        PlayerWR[] wrs = getWRRecruits();
        for (PlayerWR wr : wrs) {
            sb.append("WR," + wr.name + "," + wr.year + "," + wr.ratPot + "," + wr.ratFootIQ + "," + wr.ratRecCat + "," + wr.ratRecSpd + "," + wr.ratRecEva + "," + wr.ratOvr + "," + wr.cost + ",0%\n");
        }
        PlayerK[] ks = getKRecruits();
        for (PlayerK k : ks) {
            sb.append("K," + k.name + "," + k.year + "," + k.ratPot + "," + k.ratFootIQ + "," + k.ratKickPow + "," + k.ratKickAcc + "," + k.ratKickFum + "," + k.ratOvr + "," + k.cost + ",0%\n");
        }
        PlayerOL[] ols = getOLRecruits();
        for (PlayerOL ol : ols) {
            sb.append("OL," + ol.name + "," + ol.year + "," + ol.ratPot + "," + ol.ratFootIQ + "," + ol.ratOLPow + "," + ol.ratOLBkR + "," + ol.ratOLBkP + "," + ol.ratOvr + "," + ol.cost + ",0%\n");
        }
        PlayerS[] ss = getSRecruits();
        for (PlayerS s : ss) {
            sb.append("S," + s.name + "," + s.year + "," + s.ratPot + "," + s.ratFootIQ + "," + s.ratSCov + "," + s.ratSSpd + "," + s.ratSTkl + "," + s.ratOvr + "," + s.cost + ",0%\n");
        }
        PlayerCB[] cbs = getCBRecruits();
        for (PlayerCB cb : cbs) {
            sb.append("CB," + cb.name + "," + cb.year + "," + cb.ratPot + "," + cb.ratFootIQ + "," + cb.ratCBCov + "," + cb.ratCBSpd + "," + cb.ratCBTkl + "," + cb.ratOvr + "," + cb.cost + ",0%\n");
        }
        PlayerF7[] f7s = getF7Recruits();
        for (PlayerF7 f7 : f7s) {
            sb.append("F7," + f7.name + "," + f7.year + "," + f7.ratPot + "," + f7.ratFootIQ + "," + f7.ratF7Pow + "," + f7.ratF7Rsh + "," + f7.ratF7Pas + "," + f7.ratOvr + "," + f7.cost + ",0%\n");
        }
        return sb.toString();
    }

    /**
     * Save all the current players into a string to be loaded from later
     * @return string of all the players in csv form
     */
    public String getPlayerInfoSaveFile() {
        StringBuilder sb = new StringBuilder();
        for (PlayerQB qb : teamQBs) {
            sb.append("QB," + qb.name + "," + qb.year + "," + qb.ratPot + "," + qb.ratFootIQ + "," +
                    qb.ratPassPow + "," + qb.ratPassAcc + "," + qb.ratPassEva + "," + qb.ratOvr + "," + qb.ratImprovement + "%\n");
        }
        for (PlayerRB rb : teamRBs) {
            sb.append("RB," + rb.name + "," + rb.year + "," + rb.ratPot + "," + rb.ratFootIQ + "," +
                    rb.ratRushPow + "," + rb.ratRushSpd + "," + rb.ratRushEva + "," + rb.ratOvr + "," + rb.ratImprovement + "%\n");
        }
        for (PlayerWR wr : teamWRs) {
            sb.append("WR," + wr.name + "," + wr.year + "," + wr.ratPot + "," + wr.ratFootIQ + "," +
                    wr.ratRecCat + "," + wr.ratRecSpd + "," + wr.ratRecEva + "," + wr.ratOvr + "," + wr.ratImprovement + "%\n");
        }
        for (PlayerK k : teamKs) {
            sb.append("K," + k.name + "," + k.year + "," + k.ratPot + "," + k.ratFootIQ + "," +
                    k.ratKickPow + "," + k.ratKickAcc + "," + k.ratKickFum + "," + k.ratOvr + "," + k.ratImprovement + "%\n");
        }
        for (PlayerOL ol : teamOLs) {
            sb.append("OL," + ol.name + "," + ol.year + "," + ol.ratPot + "," + ol.ratFootIQ + "," +
                    ol.ratOLPow + "," + ol.ratOLBkR + "," + ol.ratOLBkP + "," + ol.ratOvr + "," + ol.ratImprovement + "%\n");
        }
        for (PlayerS s : teamSs) {
            sb.append("S," + s.name + "," + s.year + "," + s.ratPot + "," + s.ratFootIQ + "," +
                    s.ratSCov + "," + s.ratSSpd + "," + s.ratSTkl + "," + s.ratOvr + "," + s.ratImprovement + "%\n");
        }
        for (PlayerCB cb : teamCBs) {
            sb.append("CB," + cb.name + "," + cb.year + "," + cb.ratPot + "," + cb.ratFootIQ + "," +
                    cb.ratCBCov + "," + cb.ratCBSpd + "," + cb.ratCBTkl + "," + cb.ratOvr + "," + cb.ratImprovement + "%\n");
        }
        for (PlayerF7 f7 : teamF7s) {
            sb.append("F7," + f7.name + "," + f7.year + "," + f7.ratPot + "," + f7.ratFootIQ + "," +
                    f7.ratF7Pow + "," + f7.ratF7Rsh + "," + f7.ratF7Pas + "," + f7.ratOvr + "," + f7.ratImprovement + "%\n");
        }
        return sb.toString();
    }

    /**
     * Generate all the offense team strategies that can be selected
     * @return array of all the offense team strats
     */
    public TeamStrategy[] getTeamStrategiesOff() {
        TeamStrategy[] ts = new TeamStrategy[3];

        ts[0] = new TeamStrategy("Aggressive",
                "Play a more aggressive offense. Will pass with lower completion percentage and higher chance of interception." +
                        " However, catches will go for more yards.", -1, 2, 3, 1);

        ts[1] = new TeamStrategy("No Preference",
                "Will play a normal offense with no bonus either way, but no penalties either.", 0, 0, 0, 0);

        ts[2] = new TeamStrategy("Conservative",
                "Play a more conservative offense, running a bit more and passing slightly less. Passes are more accurate but shorter." +
                        " Rushes are more likely to gain yards but less likely to break free for big plays.", 1, -2, -3, -1);

        return ts;
    }

    /**
     * Generate all the defense team strategies that can be selected
     * @return array of all the defense team strats
     */
    public TeamStrategy[] getTeamStrategiesDef() {
        TeamStrategy[] ts = new TeamStrategy[3];

        ts[0] = new TeamStrategy("Stack the Box",
                "Focus on stopping the run. Will give up more big passing plays but will allow less rushing yards and far less big plays from rushing.", 1, 0, -1, -1);

        ts[1] = new TeamStrategy("No Preference",
                "Will play a normal defense with no bonus either way, but no penalties either.", 0, 0, 0, 0);

        ts[2] = new TeamStrategy("No Fly Zone",
                "Focus on stopping the pass. Will give up less yards on catches and will be more likely to intercept passes, " +
                "but will allow more rushing yards.", -1, 0, 1, 1);

        return ts;
    }

    /**
     * Set the starters for a particular position.
     * @param starters new starters to be set
     * @param position position, 0 - 7
     */
    public void setStarters(ArrayList<Player> starters, int position) {
        switch (position) {
            case 0:
                ArrayList<PlayerQB> oldQBs = new ArrayList<>();
                oldQBs.addAll(teamQBs);
                teamQBs.clear();
                for (Player p : starters) {
                    teamQBs.add( (PlayerQB) p );
                }
                Collections.sort(teamQBs, new PlayerComparator());
                for (PlayerQB oldP : oldQBs) {
                    if (!teamQBs.contains(oldP)) teamQBs.add(oldP);
                }
                break;
            case 1:
                ArrayList<PlayerRB> oldRBs = new ArrayList<>();
                oldRBs.addAll(teamRBs);
                teamRBs.clear();
                for (Player p : starters) {
                    teamRBs.add( (PlayerRB) p );
                }
                Collections.sort(teamRBs, new PlayerComparator());
                for (PlayerRB oldP : oldRBs) {
                    if (!teamRBs.contains(oldP)) teamRBs.add(oldP);
                }
                break;
            case 2:
                ArrayList<PlayerWR> oldWRs = new ArrayList<>();
                oldWRs.addAll(teamWRs);
                teamWRs.clear();
                for (Player p : starters) {
                    teamWRs.add( (PlayerWR) p );
                }
                Collections.sort(teamWRs, new PlayerComparator());
                for (PlayerWR oldP : oldWRs) {
                    if (!teamWRs.contains(oldP)) teamWRs.add(oldP);
                }
                break;
            case 3:
                ArrayList<PlayerOL> oldOLs = new ArrayList<>();
                oldOLs.addAll(teamOLs);
                teamOLs.clear();
                for (Player p : starters) {
                    teamOLs.add( (PlayerOL) p );
                }
                Collections.sort(teamOLs, new PlayerComparator());
                for (PlayerOL oldP : oldOLs) {
                    if (!teamOLs.contains(oldP)) teamOLs.add(oldP);
                }
                break;
            case 4:
                ArrayList<PlayerK> oldKs = new ArrayList<>();
                oldKs.addAll(teamKs);
                teamKs.clear();
                for (Player p : starters) {
                    teamKs.add( (PlayerK) p );
                }
                Collections.sort(teamKs, new PlayerComparator());
                for (PlayerK oldP : oldKs) {
                    if (!teamKs.contains(oldP)) teamKs.add(oldP);
                }
                break;
            case 5:
                ArrayList<PlayerS> oldSs = new ArrayList<>();
                oldSs.addAll(teamSs);
                teamSs.clear();
                for (Player p : starters) {
                    teamSs.add( (PlayerS) p );
                }
                Collections.sort(teamSs, new PlayerComparator());
                for (PlayerS oldP : oldSs) {
                    if (!teamSs.contains(oldP)) teamSs.add(oldP);
                }
                break;
            case 6:
                ArrayList<PlayerCB> oldCBs = new ArrayList<>();
                oldCBs.addAll(teamCBs);
                teamCBs.clear();
                for (Player p : starters) {
                    teamCBs.add( (PlayerCB) p );
                }
                Collections.sort(teamCBs, new PlayerComparator());
                for (PlayerCB oldP : oldCBs) {
                    if (!teamCBs.contains(oldP)) teamCBs.add(oldP);
                }
                break;
            case 7:
                ArrayList<PlayerF7> oldF7s = new ArrayList<>();
                oldF7s.addAll(teamF7s);
                teamF7s.clear();
                for (Player p : starters) {
                    teamF7s.add( (PlayerF7) p );
                }
                Collections.sort(teamF7s, new PlayerComparator());
                for (PlayerF7 oldP : oldF7s) {
                    if (!teamF7s.contains(oldP)) teamF7s.add(oldP);
                }
                break;
        }
    }

    /**
     * Add one gamePlayed to all the starters.
     * The number of games played affects how much players improve.
     */
    public void addGamePlayedPlayers() {
        addGamePlayedList(teamQBs, 1);
        addGamePlayedList(teamRBs, 2);
        addGamePlayedList(teamWRs, 3);
        addGamePlayedList(teamOLs, 5);
        addGamePlayedList(teamKs, 1);
        addGamePlayedList(teamSs, 1);
        addGamePlayedList(teamCBs, 3);
        addGamePlayedList(teamF7s, 7);
    }

    private void addGamePlayedList(ArrayList<? extends Player> playerList, int starters) {
        for (int i = 0; i < starters; ++i) {
            playerList.get(i).gamesPlayed++;
        }
    }
}

/**
 * Comparator used to sort players by overall
 */
class PlayerComparator implements Comparator<Player> {
    @Override
    public int compare( Player a, Player b ) {
        if (a.year>0 && b.year>0)
            return a.ratOvr > b.ratOvr ? -1 : a.ratOvr == b.ratOvr ? 0 : 1;
        else if (a.year>0)
            return -1;
        else if (b.year>0)
            return 1;
        else
            return a.ratOvr > b.ratOvr ? -1 : a.ratOvr == b.ratOvr ? 0 : 1;
    }
}
