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
    public int recruitMoney;
    public int numRecruits;
    
    public int wins;
    public int losses;
    public int totalWins;
    public int totalLosses;
    public int totalCCs;
    public int totalNCs;
    
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
    
    public TeamStrategy teamStratOff;
    public TeamStrategy teamStratDef;
    
    /**
     * Creates new team, recruiting needed players and setting team stats to 0.
     * @param name
     * @param abbr
     * @param conference
     * @param league
     * @param prestige 
     */
    public Team( String name, String abbr, String conference, League league, int prestige, String rivalTeamAbbr ) {
        this.league = league;
        userControlled = false;
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
        totalCCs = 0;
        totalNCs = 0;
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
        numRecruits = 30;
    }

    /**
     * Constructor for team that is being loaded from file.
     * @param loadStr
     */
    public Team( String loadStr, League league ) {
        this.league = league;
        userControlled = false;
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

        // Actually load the team from the string
        String[] lines = loadStr.split("%");

        // Line 0 is team info
        String[] teamInfo = lines[0].split(",");
        if (teamInfo.length == 9) {
            conference = teamInfo[0];
            name = teamInfo[1];
            abbr = teamInfo[2];
            teamPrestige = Integer.parseInt(teamInfo[3]);
            totalWins = Integer.parseInt(teamInfo[4]);
            totalLosses = Integer.parseInt(teamInfo[5]);
            totalCCs = Integer.parseInt(teamInfo[6]);
            totalNCs = Integer.parseInt(teamInfo[7]);
            rivalTeam = teamInfo[8];
        }

        // Rest of lines are player info
        String[] playerInfo;
        for (int i = 1; i < lines.length; ++i) {
            recruitPlayerCSV(lines[i]);
        }

        wonRivalryGame = false;
        teamStratOff = new TeamStrategy();
        teamStratDef = new TeamStrategy();
        numRecruits = 30;
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
     * Advance season, hiring new coach if needed and calculating new perstige level.
     */
    public void advanceSeason() {
        int expectedPollFinish = 100 - teamPrestige;
        int diffExpected = expectedPollFinish - rankTeamPollScore;
        int oldPrestige = teamPrestige;

        if ( teamPrestige > 55 || diffExpected > 0 ) {
            teamPrestige = (int)Math.pow(teamPrestige, 1 + (float)diffExpected/1500);// + diffExpected/2500);
        }

        if ( wonRivalryGame ) {
            teamPrestige += 2;
        } else {
            teamPrestige -= 2;
        }

        if (teamPrestige > 95) teamPrestige = 95;
        if (teamPrestige < 45) teamPrestige = 45;

        diffPrestige = teamPrestige - oldPrestige;
        advanceSeasonPlayers();
        
    }
    
    /**
     * Advance season for players. Removes seniors and develops underclassmen.
     */
    public void advanceSeasonPlayers() {
        int qbNeeds=0, rbNeeds=0, wrNeeds=0, kNeeds=0, olNeeds=0, sNeeds=0, cbNeeds=0, f7Needs=0;
        
        int i = 0;
        while ( i < teamQBs.size() ) {
            if ( teamQBs.get(i).year == 4 ) {
                teamQBs.remove(i);
                qbNeeds++;
            } else {
                teamQBs.get(i).advanceSeason();
                i++;
            }
        }
        
        i = 0;
        while ( i < teamRBs.size() ) {
            if ( teamRBs.get(i).year == 4 ) {
                teamRBs.remove(i);
                rbNeeds++;
            } else {
                teamRBs.get(i).advanceSeason();
                i++;
            }
        }
        
        i = 0;
        while ( i < teamWRs.size() ) {
            if ( teamWRs.get(i).year == 4 ) {
                teamWRs.remove(i);
                wrNeeds++;
            } else {
                teamWRs.get(i).advanceSeason();
                i++;
            }
        }
        
        i = 0;
        while ( i < teamKs.size() ) {
            if ( teamKs.get(i).year == 4 ) {
                teamKs.remove(i);
                kNeeds++;
            } else {
                teamKs.get(i).advanceSeason();
                i++;
            }
        }
        
        i = 0;
        while ( i < teamOLs.size() ) {
            if ( teamOLs.get(i).year == 4 ) {
                teamOLs.remove(i);
                olNeeds++;
            } else {
                teamOLs.get(i).advanceSeason();
                i++;
            }
        }
        
        i = 0;
        while ( i < teamSs.size() ) {
            if ( teamSs.get(i).year == 4 ) {
                teamSs.remove(i);
                sNeeds++;
            } else {
                teamSs.get(i).advanceSeason();
                i++;
            }
        }
        
        i = 0;
        while ( i < teamCBs.size() ) {
            if ( teamCBs.get(i).year == 4 ) {
                teamCBs.remove(i);
                cbNeeds++;
            } else {
                teamCBs.get(i).advanceSeason();
                i++;
            }
        }
        
        i = 0;
        while ( i < teamF7s.size() ) {
            if ( teamF7s.get(i).year == 4 ) {
                teamF7s.remove(i);
                f7Needs++;
            } else {
                teamF7s.get(i).advanceSeason();
                i++;
            }
        }
        
        if ( !userControlled ) {
            recruitPlayersFreshman( qbNeeds, rbNeeds, wrNeeds, kNeeds, olNeeds, sNeeds, cbNeeds, f7Needs );
            resetStats();
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
        
        for( int i = 0; i < qbNeeds; ++i ) {
            //make QBs
            if ( 100*Math.random() < 5*chance ) {
                teamQBs.add( new PlayerQB(league.getRandName(), 1, stars-1, this) );
            } else {
                teamQBs.add( new PlayerQB(league.getRandName(), 1, stars, this) );
            }
        }
        
        for( int i = 0; i < kNeeds; ++i ) {
            //make Ks
            if ( 100*Math.random() < 5*chance ) {
                teamKs.add( new PlayerK(league.getRandName(), 1, stars-1) );
            } else {
                teamKs.add( new PlayerK(league.getRandName(), 1, stars) );
            }
        }
        
        for( int i = 0; i < rbNeeds; ++i ) {
            //make RBs
            if ( 100*Math.random() < 5*chance ) {
                teamRBs.add( new PlayerRB(league.getRandName(), 1, stars-1, this) );
            } else {
                teamRBs.add( new PlayerRB(league.getRandName(), 1, stars, this) );
            }
        }
        
        for( int i = 0; i < wrNeeds; ++i ) {
            //make WRs
            if ( 100*Math.random() < 5*chance ) {
                teamWRs.add( new PlayerWR(league.getRandName(), 1, stars-1, this) );
            } else {
                teamWRs.add( new PlayerWR(league.getRandName(), 1, stars, this) );
            }
        }
        
        for( int i = 0; i < olNeeds; ++i ) {
            //make OLs
            if ( 100*Math.random() < 5*chance ) {
                teamOLs.add( new PlayerOL(league.getRandName(), 1, stars-1) );
            } else {
                teamOLs.add( new PlayerOL(league.getRandName(), 1, stars) );
            }
        }
        
        for( int i = 0; i < cbNeeds; ++i ) {
            //make CBs
            if ( 100*Math.random() < 5*chance ) {
                teamCBs.add( new PlayerCB(league.getRandName(), 1, stars-1) );
            } else {
                teamCBs.add( new PlayerCB(league.getRandName(), 1, stars) );
            }
        }
        
        for( int i = 0; i < f7Needs; ++i ) {
            //make F7s
            if ( 100*Math.random() < 5*chance ) {
                teamF7s.add( new PlayerF7(league.getRandName(), 1, stars-1) );
            } else {
                teamF7s.add( new PlayerF7(league.getRandName(), 1, stars) );
            }
        }
        
        for( int i = 0; i < sNeeds; ++i ) {
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
        String[] pi; //PlayerInfo
        for (String p : players) {
            recruitPlayerCSV(p);
        }
    }

    /**
     * Recruit individual player given in a string
     */
    private void recruitPlayerCSV(String line) {
        String[] playerInfo = line.split(",");
        if (playerInfo[0].equals("QB")) {
            teamQBs.add( new PlayerQB(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7])));
        } else if (playerInfo[0].equals("RB")) {
            teamRBs.add( new PlayerRB(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7])));
        } else if (playerInfo[0].equals("WR")) {
            teamWRs.add( new PlayerWR(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7])));
        } else if (playerInfo[0].equals("OL")) {
            teamOLs.add( new PlayerOL(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7])));
        } else if (playerInfo[0].equals("K")) {
            teamKs.add( new PlayerK(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7])));
        } else if (playerInfo[0].equals("S")) {
            teamSs.add( new PlayerS(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7])));
        } else if (playerInfo[0].equals("CB")) {
            teamCBs.add( new PlayerCB(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7])));
        } else if (playerInfo[0].equals("F7")) {
            teamF7s.add( new PlayerF7(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7])));
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
        diffOffTalent = getOffTalent() - teamOffTalent;
        teamOffTalent = getOffTalent();
        diffDefTalent = getOffTalent() - teamDefTalent;
        teamDefTalent = getDefTalent();

        teamPollScore = teamPrestige + getOffTalent() + getDefTalent();
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
        teamHistory.add((teamHistory.size() + 2015) + ": #" + rankTeamPollScore + " " + abbr + " (" + wins + "-" + losses + ") "
                + confChampion + " " + semiFinalWL + natChampWL);
    }

    /**
     * Gets a string of the entire team history
     */
    public String getTeamHistoryStr() {
        String hist = "";
        hist += "Overall W-L: " + totalWins + "-" + totalLosses + "\n";
        hist += "Conference Championships: " + totalCCs + "\n";
        hist += "National Championships: " + totalNCs + "\n";
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
    
    public int getPassProf() {
        int avgWRs = ( teamWRs.get(0).ratOvr + teamWRs.get(1).ratOvr + teamWRs.get(2).ratOvr)/3;
        return (getCompositeOLPass() + getQB(0).ratOvr*2 + avgWRs)/4;
    }
    
    public int getRushProf() {
        int avgRBs = ( teamRBs.get(0).ratOvr + teamRBs.get(1).ratOvr )/2;
        return (getCompositeOLRush() + avgRBs )/2;
    }
    
    public int getPassDef() {
        int avgCBs = ( teamCBs.get(0).ratOvr + teamCBs.get(1).ratOvr + teamCBs.get(2).ratOvr)/3;
        return (avgCBs*3 + teamSs.get(0).ratOvr + getCompositeF7Pass()*2)/6;
    }
    
    public int getRushDef() {
        return getCompositeF7Rush();
    }
    
    public int getCompositeOLPass() {
        int compositeOL = 0;
        for ( int i = 0; i < 5; ++i ) {
            compositeOL += (teamOLs.get(i).ratOLPow + teamOLs.get(i).ratOLBkP)/2;
        }
        return compositeOL / 5;
    }
    
    public int getCompositeOLRush() {
        int compositeOL = 0;
        for ( int i = 0; i < 5; ++i ) {
            compositeOL += (teamOLs.get(i).ratOLPow + teamOLs.get(i).ratOLBkR)/2;
        }
        return compositeOL / 5;
    }
    
    public int getCompositeF7Pass() {
        int compositeF7 = 0;
        for ( int i = 0; i < 7; ++i ) {
            compositeF7 += (teamF7s.get(i).ratF7Pow + teamF7s.get(i).ratF7Pas)/2;
        }
        return compositeF7 / 7;
    }
    
    public int getCompositeF7Rush() {
        int compositeF7 = 0;
        for ( int i = 0; i < 7; ++i ) {
            compositeF7 += (teamF7s.get(i).ratF7Pow + teamF7s.get(i).ratF7Rsh)/2;
        }
        return compositeF7 / 7;
    }

    public String[] getTeamStatsStr() {
        String[] ts = new String[3];
        StringBuilder ts0 = new StringBuilder();
        StringBuilder ts1 = new StringBuilder();
        StringBuilder ts2 = new StringBuilder();
        ts[0] = "";
        ts[1] = "";
        ts[2] = "";

        ts0.append(teamPollScore + "\n");
        ts1.append("AP Votes" + "\n");
        ts2.append(getRankStr(rankTeamPollScore) + "\n");

        ts0.append(teamStrengthOfWins + "\n");
        ts1.append("SOS" + "\n");
        ts2.append(getRankStr(rankTeamStrengthOfWins) + "\n");

        ts0.append(teamPoints/numGames() + "\n");
        ts1.append("Points" + "\n");
        ts2.append(getRankStr(rankTeamPoints) + "\n");

        ts0.append(teamOppPoints/numGames() + "\n");
        ts1.append("Opp Points" + "\n");
        ts2.append(getRankStr(rankTeamOppPoints) + "\n");

        ts0.append(teamYards/numGames() + "\n");
        ts1.append("Yards" + "\n");
        ts2.append(getRankStr(rankTeamYards) + "\n");

        ts0.append(teamOppYards/numGames() + "\n");
        ts1.append("Opp Yards" + "\n");
        ts2.append(getRankStr(rankTeamOppYards) + "\n");

        ts0.append(teamPassYards/numGames() + "\n");
        ts1.append("Pass Yards" + "\n");
        ts2.append(getRankStr(rankTeamPassYards) + "\n");

        ts0.append(teamRushYards/numGames() + "\n");
        ts1.append("Rush Yards" + "\n");
        ts2.append(getRankStr(rankTeamRushYards) + "\n");

        ts0.append(teamOffTalent + "\n");
        ts1.append("Off Talent" + "\n");
        ts2.append(getRankStr(rankTeamOffTalent) + "\n");

        ts0.append(teamDefTalent + "\n");
        ts1.append("Def Talent" + "\n");
        ts2.append(getRankStr(rankTeamDefTalent) + "\n");

        ts0.append(teamPrestige + "\n");
        ts1.append("Prestige" + "\n");
        ts2.append(getRankStr(rankTeamPrestige) + "\n");

        ts[0] = ts0.toString();
        ts[1] = ts1.toString();
        ts[2] = ts2.toString();
        return ts;
    }

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

    public String[] getGameScheduleStr() {
        String[] gs = new String[3];
        StringBuilder gs0 = new StringBuilder();
        StringBuilder gs1 = new StringBuilder();
        StringBuilder gs2 = new StringBuilder();
        gs[0] = "";
        gs[1] = "";
        gs[2] = "";

        Game g;
        for (int i = 0; i < gameSchedule.size(); ++i) {
            g = gameSchedule.get(i);
            gs0.append(g.gameName + "\n\n");
            if (i < gameWLSchedule.size()) {
                gs1.append(gameWLSchedule.get(i) + " " + gameSummaryStrScore(g) + "\n\n");
            } else {
                gs1.append("---" + "\n\n");
            }
            gs2.append(gameSummaryStrOpponent(g) + "\n\n");
        }

        gs[0] = gs0.toString();
        gs[1] = gs1.toString();
        gs[2] = gs2.toString();
        return gs;
    }

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

    public String seasonSummaryStr() {
        String summary = "Your team, " + name + ", finished the season ranked #" + rankTeamPollScore + " with " + wins + " wins and " + losses + " losses.";
        int expectedPollFinish = 100 - teamPrestige;
        int diffExpected = expectedPollFinish - rankTeamPollScore;
        int oldPrestige = teamPrestige;
        int newPrestige = oldPrestige;
        if ( teamPrestige > 55 || diffExpected > 0 ) {
            newPrestige = (int)Math.pow(teamPrestige, 1 + (float)diffExpected/1500);// + diffExpected/2500);
        }

        if ((newPrestige - oldPrestige) > 0) {
            summary += "\n\nGreat job coach! You exceeded expectations and gained " + (newPrestige - oldPrestige) + " prestige! This will help your recruiting.";
        } else if ((newPrestige - oldPrestige) < 0) {
            summary += "\n\nA bit of a down year, coach? You fell short expectations and lost " + (oldPrestige - newPrestige) + " prestige. This will hurt your recruiting.";
        } else {
            summary += "\n\nWell, your team performed exactly how many expected. This won't hurt or help recruiting, but try to improve next year!";
        }

        if (wonRivalryGame) {
            summary += "\n\nFuture recruits were impressed that you won your rivalry game. You gained 2 prestige.";
        } else {
            summary += "\n\nSince you couldn't win your rivalry game, recruits aren't excited to attend your school. You lost 2 prestige.";
        }

        return summary;
    }

    public String[] getPlayerStatsStr() {
        String[] ps = new String[3];
        StringBuilder ps0 = new StringBuilder();
        StringBuilder ps1 = new StringBuilder();
        StringBuilder ps2 = new StringBuilder();
        ps[0] = "";
        ps[1] = "";
        ps[2] = "";

        ps0.append("\n");
        ps1.append("QB\n");
        ps2.append("\n");
        ps0.append(getQB(0).getInitialName() + "\n");
        ps1.append(getQB(0).statsTD + " TD\n");
        ps2.append(getQB(0).statsPassYards + " yds\n");
        ps0.append(getQB(0).getYrStr() + " " + getQB(0).ratOvr+"/"+getQB(0).ratPot + "\n");
        ps1.append(getQB(0).statsInt + " Int\n");
        ps2.append((100*getQB(0).statsPassComp/(getQB(0).statsPassAtt+1)) + " Pass%\n");

        for (int i = 0; i < 2; ++i) {
            ps0.append("\n\n");
            ps1.append("\nRB\n");
            ps2.append("\n\n");
            ps0.append(getRB(i).getInitialName() + "\n");
            ps1.append(getRB(i).statsTD + " TD\n");
            ps2.append(getRB(i).statsRushYards + " yds\n");
            ps0.append(getRB(i).getYrStr() + " " + getRB(i).ratOvr+"/"+ getRB(i).ratPot + "\n");
            ps1.append(getRB(i).statsFumbles + " Fum\n");
            ps2.append(((float) ((int) ((float) getRB(i).statsRushYards / getRB(i).statsRushAtt * 100)) / 100) + " Y/A\n");
        }

        for (int i = 0; i < 3; ++i) {
            ps0.append("\n\n");
            ps1.append("\nWR\n");
            ps2.append("\n\n");
            ps0.append(getWR(i).getInitialName() + "\n");
            ps1.append(getWR(i).statsTD + " TD\n");
            ps2.append(getWR(i).statsRecYards + " yds\n");
            ps0.append(getWR(i).getYrStr() + " " + getWR(i).ratOvr + "/" + getWR(i).ratPot + "\n");
            ps1.append(getWR(i).statsFumbles + " Fum\n");
            ps2.append(getWR(i).statsTargets + " Tgts\n");
        }

        ps0.append("\n\n");
        ps1.append("\nK\n");
        ps2.append("\n\n");
        ps0.append(getK(0).getInitialName() + "\n");
        ps1.append(getK(0).statsFGMade + " FGM\n");
        ps2.append((100*getK(0).statsFGMade/(getK(0).statsFGAtt+1)) + " FG%\n");
        ps0.append(getK(0).getYrStr() + " " + getK(0).ratOvr + "/" + getK(0).ratPot + "\n");
        ps1.append(getK(0).statsXPMade + " XPM\n");
        ps2.append((100*getK(0).statsXPMade/(getK(0).statsXPAtt+1)) + " XP%\n");

        ps0.append("\n\n");
        ps1.append("\nOL\n");
        ps2.append("\n\n");
        for (int i = 0; i < 5; ++i) {
            ps0.append(getOL(i).getInitialName() + "\n");
            ps1.append(getOL(i).ratOvr + "/" + getOL(i).ratPot + "\n");
            ps2.append(getOL(i).getYrStr() + "\n");
        }

        ps0.append("\n\n");
        ps1.append("\nS\n");
        ps2.append("\n\n");
        for (int i = 0; i < 1; ++i) {
            ps0.append(getS(i).getInitialName() + "\n");
            ps1.append(getS(i).ratOvr + "/" + getS(i).ratPot + "\n");
            ps2.append(getS(i).getYrStr() + "\n");
        }

        ps0.append("\n\n");
        ps1.append("\nCB\n");
        ps2.append("\n\n");
        for (int i = 0; i < 3; ++i) {
            ps0.append(getCB(i).getInitialName() + "\n");
            ps1.append(getCB(i).ratOvr + "/" + getCB(i).ratPot + "\n");
            ps2.append(getCB(i).getYrStr() + "\n");
        }

        ps0.append("\n\n");
        ps1.append("\nLB/DT\n");
        ps2.append("\n\n");
        for (int i = 0; i < 7; ++i) {
            ps0.append(getF7(i).getInitialName() + "\n");
            ps1.append(getF7(i).ratOvr + "/" + getF7(i).ratPot + "\n");
            ps2.append(getF7(i).getYrStr() + "\n");
        }

        ps0.append("\n\n");
        ps1.append("\nBackups\n");
        ps2.append("\n\n");
        for (int i = 1; i < teamQBs.size(); ++i) {
            ps0.append(getQB(i).getInitialName() + "\n");
            ps1.append("QB\n");
            ps2.append(getQB(i).getYrStr() + " " + getQB(i).ratOvr+"/"+getQB(i).ratPot + "\n");
        }
        for (int i = 2; i < teamRBs.size(); ++i) {
            ps0.append(getRB(i).getInitialName() + "\n");
            ps1.append("RB\n");
            ps2.append(getRB(i).getYrStr() + " " + getRB(i).ratOvr + "/" + getRB(i).ratPot + "\n");
        }
        for (int i = 3; i < teamWRs.size(); ++i) {
            ps0.append(getWR(i).getInitialName() + "\n");
            ps1.append("WR\n");
            ps2.append(getWR(i).getYrStr() + " " + getWR(i).ratOvr + "/" + getWR(i).ratPot + "\n");
        }
        for (int i = 1; i < teamKs.size(); ++i) {
            ps0.append(getK(i).getInitialName() + "\n");
            ps1.append("K\n");
            ps2.append(getK(i).getYrStr() + " " + getK(i).ratOvr + "/" + getK(i).ratPot + "\n");
        }
        for (int i = 5; i < teamOLs.size(); ++i) {
            ps0.append(getOL(i).getInitialName() + "\n");
            ps1.append("OL\n");
            ps2.append(getOL(i).getYrStr() + " " + getOL(i).ratOvr + "/" + getOL(i).ratPot + "\n");
        }
        for (int i = 1; i < teamSs.size(); ++i) {
            ps0.append(getS(i).getInitialName() + "\n");
            ps1.append("S\n");
            ps2.append(getS(i).getYrStr() + " " + getS(i).ratOvr + "/" + getS(i).ratPot + "\n");
        }
        for (int i = 3; i < teamCBs.size(); ++i) {
            ps0.append(getCB(i).getInitialName() + "\n");
            ps1.append("CB\n");
            ps2.append(getCB(i).getYrStr() + " " + getCB(i).ratOvr + "/" + getCB(i).ratPot + "\n");
        }
        for (int i = 7; i < teamF7s.size(); ++i) {
            ps0.append(getF7(i).getInitialName() + "\n");
            ps1.append("LB/DT\n");
            ps2.append(getF7(i).getYrStr() + " " + getF7(i).ratOvr + "/" + getF7(i).ratPot + "\n");
        }

        ps[0] = ps0.toString();
        ps[1] = ps1.toString();
        ps[2] = ps2.toString();
        return ps;
    }

    /**
     * Gets player name or detail strings for displaying in the roster tab via expandable list.
     * Should be separated by a '>' from left text and right text.
     * @return
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
     * @param playerStatsGroupHeaders
     * @return
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

    // Gets rank str, like 21st, 45th, etc
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

    public int numGames() {
        if ( wins + losses > 0 ) {
            return wins + losses;
        } else return 1;
    }

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

    public String strRep() {
        return "#" + rankTeamPollScore + " " + abbr + " (" + wins + "-" + losses + ")";
    }

    public String strRepWithBowlResults() {
        return "#" + rankTeamPollScore + " " + abbr + " (" + wins + "-" + losses + ") " + confChampion + " " + semiFinalWL + natChampWL;
    }

    public String weekSummaryStr() {
        int i = wins + losses - 1;
        Game g = gameSchedule.get(i);
        String gameSummary = gameWLSchedule.get(i) + " " + gameSummaryStr(g);
        String rivalryGameStr = "";
        if (g.homeTeam.rivalTeam.equals(g.awayTeam.abbr)) {
            if ( gameWLSchedule.get(i).equals("W") ) rivalryGameStr = "Won vs Rival! +2 Prestige\n";
            else rivalryGameStr = "Lost vs Rival! -2 Prestige\n";
        }
        return rivalryGameStr + name + " " + gameSummary + "\nNew poll rank: #" + rankTeamPollScore + " " + abbr + " (" + wins + "-" + losses + ")";
    }

    public String gameSummaryStr(Game g) {
        if (g.homeTeam == this) {
            return g.homeScore + " - " + g.awayScore + " vs " + g.awayTeam.abbr + " #" + g.awayTeam.rankTeamPollScore;
        } else {
            return g.awayScore + " - " + g.homeScore + " @ " + g.homeTeam.abbr + " #" + g.homeTeam.rankTeamPollScore;
        }
    }

    public String gameSummaryStrScore(Game g) {
        if (g.homeTeam == this) {
            return g.homeScore + " - " + g.awayScore;
        } else {
            return g.awayScore + " - " + g.homeScore;
        }
    }

    public String gameSummaryStrOpponent(Game g) {
        if (g.homeTeam == this) {
            return "vs " + g.awayTeam.abbr + " #" + g.awayTeam.rankTeamPollScore;
        } else {
            return "@ " + g.homeTeam.abbr + " #" + g.homeTeam.rankTeamPollScore;
        }
    }

    public String getGraduatingPlayersStr() {
        StringBuilder sb = new StringBuilder();
        for ( PlayerQB p : teamQBs ) {
            if (p.year == 4) {
                sb.append(p.getPosNameYrOvrPot_OneLine() +"\n");
            }
        }
        for ( PlayerRB p : teamRBs ) {
            if (p.year == 4) {
                sb.append(p.getPosNameYrOvrPot_OneLine() +"\n");
            }
        }
        for ( PlayerWR p : teamWRs ) {
            if (p.year == 4) {
                sb.append(p.getPosNameYrOvrPot_OneLine() +"\n");
            }
        }
        for ( PlayerOL p : teamOLs ) {
            if (p.year == 4) {
                sb.append(p.getPosNameYrOvrPot_OneLine() +"\n");
            }
        }
        for ( PlayerK p : teamKs ) {
            if (p.year == 4) {
                sb.append(p.getPosNameYrOvrPot_OneLine() +"\n");
            }
        }
        for ( PlayerS p : teamSs ) {
            if (p.year == 4) {
                sb.append(p.getPosNameYrOvrPot_OneLine() +"\n");
            }
        }
        for ( PlayerCB p : teamCBs ) {
            if (p.year == 4) {
                sb.append(p.getPosNameYrOvrPot_OneLine() +"\n");
            }
        }
        for ( PlayerF7 p : teamF7s ) {
            if (p.year == 4) {
                sb.append(p.getPosNameYrOvrPot_OneLine() +"\n");
            }
        }
        return sb.toString();
    }

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

    public String getRecruitsInfoSaveFile() {
        StringBuilder sb = new StringBuilder();
        PlayerQB[] qbs = getQBRecruits();
        for (PlayerQB qb : qbs) {
            sb.append("QB," + qb.name + "," + qb.year + "," + qb.ratPot + "," + qb.ratFootIQ + "," + qb.ratPassPow + "," + qb.ratPassAcc + "," + qb.ratPassEva + "," + qb.ratOvr + "," + qb.cost + "%\n");
        }
        PlayerRB[] rbs = getRBRecruits();
        for (PlayerRB rb : rbs) {
            sb.append("RB," + rb.name + "," + rb.year + "," + rb.ratPot + "," + rb.ratFootIQ + "," + rb.ratRushPow + "," + rb.ratRushSpd + "," + rb.ratRushEva + "," + rb.ratOvr + "," + rb.cost + "%\n");
        }
        PlayerWR[] wrs = getWRRecruits();
        for (PlayerWR wr : wrs) {
            sb.append("WR," + wr.name + "," + wr.year + "," + wr.ratPot + "," + wr.ratFootIQ + "," + wr.ratRecCat + "," + wr.ratRecSpd + "," + wr.ratRecEva + "," + wr.ratOvr + "," + wr.cost + "%\n");
        }
        PlayerK[] ks = getKRecruits();
        for (PlayerK k : ks) {
            sb.append("K," + k.name + "," + k.year + "," + k.ratPot + "," + k.ratFootIQ + "," + k.ratKickPow + "," + k.ratKickAcc + "," + k.ratKickFum + "," + k.ratOvr + "," + k.cost + "%\n");
        }
        PlayerOL[] ols = getOLRecruits();
        for (PlayerOL ol : ols) {
            sb.append("OL," + ol.name + "," + ol.year + "," + ol.ratPot + "," + ol.ratFootIQ + "," + ol.ratOLPow + "," + ol.ratOLBkR + "," + ol.ratOLBkP + "," + ol.ratOvr + "," + ol.cost + "%\n");
        }
        PlayerS[] ss = getSRecruits();
        for (PlayerS s : ss) {
            sb.append("S," + s.name + "," + s.year + "," + s.ratPot + "," + s.ratFootIQ + "," + s.ratSCov + "," + s.ratSSpd + "," + s.ratSTkl + "," + s.ratOvr + "," + s.cost + "%\n");
        }
        PlayerCB[] cbs = getCBRecruits();
        for (PlayerCB cb : cbs) {
            sb.append("CB," + cb.name + "," + cb.year + "," + cb.ratPot + "," + cb.ratFootIQ + "," + cb.ratCBCov + "," + cb.ratCBSpd + "," + cb.ratCBTkl + "," + cb.ratOvr + "," + cb.cost + "%\n");
        }
        PlayerF7[] f7s = getF7Recruits();
        for (PlayerF7 f7 : f7s) {
            sb.append("F7," + f7.name + "," + f7.year + "," + f7.ratPot + "," + f7.ratFootIQ + "," + f7.ratF7Pow + "," + f7.ratF7Rsh + "," + f7.ratF7Pas + "," + f7.ratOvr + "," + f7.cost + "%\n");
        }
        return sb.toString();
    }


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

    public TeamStrategy[] getTeamStrategiesOff() {
        TeamStrategy[] ts = new TeamStrategy[3];

        ts[0] = new TeamStrategy("Aggressive",
                "Play a more aggressive offense. Will pass with lower completion percentage and higher chance of interception." +
                        " However, catches will go for more yards.", -1, 2, 3, 2);

        ts[1] = new TeamStrategy("No Preference",
                "Will play a normal offense with no bonus either way, but no penalties either.", 0, 0, 0, 0);

        ts[2] = new TeamStrategy("Conservative",
                "Play a more conservative offense, running a bit more and passing slightly less. Passes are more accurate but shorter." +
                        " Rushes are more likely to gain yards but less likely to break free for big plays.", 1, -2, -3, -2);

        return ts;
    }

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
    
}

class PlayerComparator implements Comparator<Player> {
    @Override
    public int compare( Player a, Player b ) {
        return a.ratOvr > b.ratOvr ? -1 : a.ratOvr == b.ratOvr ? 0 : 1;
    }
}
