package CFBsimPack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class for storing games. Has all stats for the game.
 * Also is responsible for simming the game.
 * @author Achi
 */
public class Game implements Serializable {

    public Team homeTeam;
    public Team awayTeam;

    public boolean hasPlayed;

    public String gameName;

    public int homeScore;
    public int[] homeQScore;
    public int awayScore;
    public int[] awayQScore;
    public int homeYards;
    public int awayYards;
    public int numOT;
    public int homeTOs;
    public int awayTOs;

    public int[] HomeQBStats;
    public int[] AwayQBStats;

    public int[] HomeRB1Stats;
    public int[] HomeRB2Stats;
    public int[] AwayRB1Stats;
    public int[] AwayRB2Stats;

    public int[] HomeWR1Stats;
    public int[] HomeWR2Stats;
    public int[] HomeWR3Stats;
    public int[] AwayWR1Stats;
    public int[] AwayWR2Stats;
    public int[] AwayWR3Stats;

    public int[] HomeKStats;
    public int[] AwayKStats;

    // Store reference to players, in case starters are changed later		
    private PlayerQB homeQB;		
    private PlayerRB[] homeRBs;		
    private PlayerWR[] homeWRs;		
    private PlayerK homeK;		
    private PlayerQB awayQB;		
    private PlayerRB[] awayRBs;		
    private PlayerWR[] awayWRs;		
    private PlayerK awayK;

    String gameEventLog;
    String playPrefix;
    String tdInfo;
    String fumbleInfo;
    String intInfo;

    //private variables used when simming games
    private int gameTime;
    private boolean gamePoss; //1 if home, 0 if away
    private int gameYardLine;
    private int gameDown;
    private int gameYardsNeed;
    private boolean playingOT;
    private boolean bottomOT;
    private boolean runningClock;
    private boolean firstDownClock; // Clock management for if the ball goes OOB outside 2 minutes left in a half, or if a first down.
    private String playType;
    private boolean firstHalf;
    private int yardsGain;
    private boolean gotTD;
    private boolean gotInt;
    private boolean gotFumble;
    private boolean gotSafety;

    //private ints used for game situations
    private int twoMinuteDrill;
    private int kneelDown;
    private int milkTheClock;

    /**
     * Create game with a name (likely a bowl game).
     * @param home
     * @param away
     * @param name
     */
    public Game( Team home, Team away, String name ) {
        homeTeam = home;
        awayTeam = away;

        gameName = name;

        homeScore = 0;
        homeQScore = new int[10];
        awayScore = 0;
        awayQScore = new int[10];
        numOT = 0;

        homeTOs = 0;
        awayTOs = 0;

        //initialize arrays, set everything to zero
        HomeQBStats = new int[6];
        AwayQBStats = new int[6];

        HomeRB1Stats = new int[4];
        HomeRB2Stats = new int[4];
        AwayRB1Stats = new int[4];
        AwayRB2Stats = new int[4];

        HomeWR1Stats = new int[6];
        HomeWR2Stats = new int[6];
        HomeWR3Stats = new int[6];
        AwayWR1Stats = new int[6];
        AwayWR2Stats = new int[6];
        AwayWR3Stats = new int[6];

        HomeKStats = new int[6];
        AwayKStats = new int[6];

        //playGame();
        hasPlayed = false;

        if (gameName.equals("In Conf") && (homeTeam.rivalTeam.equals(awayTeam.abbr) || awayTeam.rivalTeam.equals(homeTeam.abbr))) {
            // Rivalry game!
            gameName = "Rivalry Game";
        }
    }

    /**
     * Create a game without a name (regular season game).
     * @param home  home team playing
     * @param away  away team playing
     */
    public Game( Team home, Team away ) {
        homeTeam = home;
        awayTeam = away;
        numOT = 0;
        homeTOs = 0;
        awayTOs = 0;

        gameName = "";

        homeScore = 0;
        homeQScore = new int[10];
        awayScore = 0;
        awayQScore = new int[10];

        //initialize arrays, set everything to zero
        HomeQBStats = new int[6];
        AwayQBStats = new int[6];

        HomeRB1Stats = new int[4];
        HomeRB2Stats = new int[4];
        AwayRB1Stats = new int[4];
        AwayRB2Stats = new int[4];

        HomeWR1Stats = new int[6];
        HomeWR2Stats = new int[6];
        HomeWR3Stats = new int[6];
        AwayWR1Stats = new int[6];
        AwayWR2Stats = new int[6];
        AwayWR3Stats = new int[6];

        HomeKStats = new int[6];
        AwayKStats = new int[6];

        //playGame();
        hasPlayed = false;

    }

    /**
     * Gets the game summary, along with all the stats and the game log, to be used by the UI.
     * @return A String array of the left, center, right, and logs.
     */
    public String[] getGameSummaryStr() {
        /**
         * [0] is left side
         * [1] is center
         * [2] is right
         * [3] is bottom (game log)
         */
        String[] gameSum = new String[4];
        StringBuilder gameL = new StringBuilder();
        StringBuilder gameC = new StringBuilder();
        StringBuilder gameR = new StringBuilder();

        gameL.append("\nPoints\nYards\nPass Yards\nRush Yards\nTOs\n\n");
        gameC.append("#"+awayTeam.rankTeamPollScore+" "+awayTeam.abbr+"\n"+awayScore+"\n"+awayYards+" yds\n"+
                getPassYards(true)+" pyds\n"+getRushYards(true)+" ryds\n"+awayTOs+" TOs\n\n");
        gameR.append("#"+homeTeam.rankTeamPollScore+" "+homeTeam.abbr+"\n"+homeScore+"\n"+homeYards+" yds\n"+
                getPassYards(false)+" pyds\n"+getRushYards(false)+" ryds\n"+homeTOs+" TOs\n\n");

        /**
         * QBs
         */
        gameL.append("QBs\nName\nYr Ovr/Pot\nTD/Int\nPass Yards\nComp/Att\n");
        gameC.append(awayTeam.abbr+"\n"+awayQB.getInitialName()+"\n");
        gameC.append(awayQB.getYrStr()+" "+awayQB.ratOvr+"/"+awayQB.ratPot+"\n");
        gameC.append(AwayQBStats[2]+"/"+AwayQBStats[3]+"\n"); //td/int
        gameC.append(AwayQBStats[4]+" yds\n"); //pass yards
        gameC.append(AwayQBStats[0]+"/"+AwayQBStats[1]+"\n"); //pass comp/att
        gameR.append(homeTeam.abbr+"\n"+homeQB.getInitialName()+"\n");
        gameR.append(homeQB.getYrStr()+" "+homeQB.ratOvr+"/"+homeQB.ratPot+"\n");
        gameR.append(HomeQBStats[2]+"/"+HomeQBStats[3]+"\n"); //td/int
        gameR.append(HomeQBStats[4]+" yds\n"); //pass yards
        gameR.append(HomeQBStats[0]+"/"+HomeQBStats[1]+"\n"); //pass comp/att

        /**
         * RBs
         */
        gameL.append("\nRBs\nRB1 Name\nYr Ovr/Pot\nTD/Fum\nRush Yards\nYds/Att\n");
        gameC.append("\n"+awayTeam.abbr+"\n"+awayRBs[0].getInitialName()+"\n");
        gameC.append(awayRBs[0].getYrStr()+" "+awayRBs[0].ratOvr+"/"+awayRBs[0].ratPot+"\n");
        gameC.append(AwayRB1Stats[2]+"/"+AwayRB1Stats[3]+"\n"); //td/fum
        gameC.append(AwayRB1Stats[1]+" yds\n"); //rush yards
        gameC.append(((double)(10*AwayRB1Stats[1]/AwayRB1Stats[0])/10)+"\n");
        gameR.append("\n"+homeTeam.abbr+"\n"+homeRBs[0].getInitialName()+"\n");
        gameR.append(homeRBs[0].getYrStr()+" "+homeRBs[0].ratOvr+"/"+homeRBs[0].ratPot+"\n");
        gameR.append(HomeRB1Stats[2]+"/"+HomeRB1Stats[3]+"\n"); //td/fum
        gameR.append(HomeRB1Stats[1]+" yds\n"); //rush yards
        gameR.append(((double)(10*HomeRB1Stats[1]/HomeRB1Stats[0])/10)+"\n");
        gameL.append("\n"); gameC.append("\n"); gameR.append("\n");
        gameL.append("RB2 Name\nYr Ovr/Pot\nTD/Fum\nRush Yards\nYds/Att\n");
        gameC.append(awayRBs[1].getInitialName()+"\n");
        gameC.append(awayRBs[1].getYrStr()+" "+awayRBs[1].ratOvr+"/"+awayRBs[1].ratPot+"\n");
        gameC.append(AwayRB2Stats[2]+"/"+AwayRB2Stats[3]+"\n"); //td/fum
        gameC.append(AwayRB2Stats[1]+" yds\n"); //rush yards
        gameC.append(((double)(10*AwayRB2Stats[1]/AwayRB2Stats[0])/10)+"\n");
        gameR.append(homeRBs[1].getInitialName()+"\n");
        gameR.append(homeRBs[1].getYrStr()+" "+homeRBs[1].ratOvr+"/"+homeRBs[1].ratPot+"\n");
        gameR.append(HomeRB2Stats[2]+"/"+HomeRB2Stats[3]+"\n"); //td/fum
        gameR.append(HomeRB2Stats[1]+" yds\n"); //rush yards
        gameR.append(((double)(10*HomeRB2Stats[1]/HomeRB2Stats[0])/10)+"\n");

        /**
         * WRs
         */
        gameL.append("\nWRs\nWR1 Name\nYr Ovr/Pot\nTD/Fum\nRec Yards\nRec/Tgts\n");
        gameC.append("\n"+awayTeam.abbr+"\n"+awayWRs[0].getInitialName()+"\n");
        gameC.append(awayWRs[0].getYrStr()+" "+awayWRs[0].ratOvr+"/"+awayWRs[0].ratPot+"\n");
        gameC.append(AwayWR1Stats[3]+"/"+AwayWR1Stats[5]+"\n"); //td/fum
        gameC.append(AwayWR1Stats[2]+" yds\n"); //rec yards
        gameC.append(AwayWR1Stats[0]+"/"+AwayWR1Stats[1]+"\n"); //rec/targets
        gameR.append("\n"+homeTeam.abbr+"\n"+homeWRs[0].getInitialName()+"\n");
        gameR.append(homeWRs[0].getYrStr()+" "+homeWRs[0].ratOvr+"/"+homeWRs[0].ratPot+"\n");
        gameR.append(HomeWR1Stats[3]+"/"+HomeWR1Stats[5]+"\n"); //td/fum
        gameR.append(HomeWR1Stats[2]+" yds\n"); //rec yards
        gameR.append(HomeWR1Stats[0]+"/"+HomeWR1Stats[1]+"\n"); //rec/targets
        gameL.append("\n"); gameC.append("\n"); gameR.append("\n");
        gameL.append("WR2 Name\nYr Ovr/Pot\nTD/Fum\nRec Yards\nRec/Tgts\n");
        gameC.append(awayWRs[1].getInitialName()+"\n");
        gameC.append(awayWRs[1].getYrStr()+" "+awayWRs[1].ratOvr+"/"+awayWRs[1].ratPot+"\n");
        gameC.append(AwayWR2Stats[3]+"/"+AwayWR2Stats[5]+"\n"); //td/fum
        gameC.append(AwayWR2Stats[2]+" yds\n"); //rec yards
        gameC.append(AwayWR2Stats[0]+"/"+AwayWR2Stats[1]+"\n"); //rec/targets
        gameR.append(homeWRs[1].getInitialName()+"\n");
        gameR.append(homeWRs[1].getYrStr()+" "+homeWRs[1].ratOvr+"/"+homeWRs[1].ratPot+"\n");
        gameR.append(HomeWR2Stats[3]+"/"+HomeWR2Stats[5]+"\n"); //td/fum
        gameR.append(HomeWR2Stats[2]+" yds\n"); //rec yards
        gameR.append(HomeWR2Stats[0]+"/"+HomeWR2Stats[1]+"\n"); //rec/targets
        gameL.append("\n"); gameC.append("\n"); gameR.append("\n");
        gameL.append("WR3 Name\nYr Ovr/Pot\nTD/Fum\nRec Yards\nRec/Tgts\n");
        gameC.append(awayWRs[2].getInitialName()+"\n");
        gameC.append(awayWRs[2].getYrStr()+" "+awayWRs[2].ratOvr+"/"+awayWRs[2].ratPot+"\n");
        gameC.append(AwayWR3Stats[3]+"/"+AwayWR3Stats[5]+"\n"); //td/fum
        gameC.append(AwayWR3Stats[2]+" yds\n"); //rec yards
        gameC.append(AwayWR3Stats[0]+"/"+AwayWR3Stats[1]+"\n"); //rec/targets
        gameR.append(homeWRs[2].getInitialName()+"\n");
        gameR.append(homeWRs[2].getYrStr()+" "+homeWRs[2].ratOvr+"/"+homeWRs[2].ratPot+"\n");
        gameR.append(HomeWR3Stats[3]+"/"+HomeWR3Stats[5]+"\n"); //td/fum
        gameR.append(HomeWR3Stats[2]+" yds\n"); //rec yards
        gameR.append(HomeWR3Stats[0]+"/"+HomeWR3Stats[1]+"\n"); //rec/targets

        /**
         * Ks
         */
        gameL.append("\nKs\nName\nYr Ovr/Pot\nFGM/FGA\nXPM/XPA\n");
        gameC.append("\n"+awayTeam.abbr+"\n"+awayK.getInitialName()+"\n");
        gameC.append(awayK.getYrStr()+" "+awayK.ratOvr+"/"+awayK.ratPot+"\n");
        gameC.append(AwayKStats[2]+"/"+AwayKStats[3]+" FG\n"+AwayKStats[0]+"/"+AwayKStats[1]+" XP\n");
        gameR.append("\n"+homeTeam.abbr+"\n"+homeK.getInitialName()+"\n");
        gameR.append(homeK.getYrStr()+" "+homeK.ratOvr+"/"+homeK.ratPot+"\n");
        gameR.append(HomeKStats[2]+"/"+HomeKStats[3]+" FG\n"+HomeKStats[0]+"/"+HomeKStats[1]+" XP\n");

        gameSum[0] = gameL.toString();
        gameSum[1] = gameC.toString();
        gameSum[2] = gameR.toString();
        gameSum[3] = gameEventLog;

        return gameSum;
    }

    /**
     * Gets a scouting summary of a game that is yet to be played.
     * @return a String array with the left, right, center, and "SCOUTING REPORT"
     */
    public String[] getGameScoutStr() {
        /**
         * [0] is left side
         * [1] is center
         * [2] is right
         * [3] is bottom (will be empty for scouting)
         */
        String[] gameSum = new String[4];
        StringBuilder gameL = new StringBuilder();
        StringBuilder gameC = new StringBuilder();
        StringBuilder gameR = new StringBuilder();

        gameL.append("Ranking\nRecord\nPPG\nOpp PPG\nYPG\nOpp YPG\n" +
                "\nPass YPG\nRush YPG\nOpp PYPG\nOpp RYPG\n\nOff Talent\nDef Talent\nPrestige");
        int g = awayTeam.numGames();
        Team t = awayTeam;
        gameC.append("#"+t.rankTeamPollScore+" "+t.abbr+"\n"+t.wins+"-"+t.losses+"\n"+
                t.teamPoints/g+" ("+t.rankTeamPoints+")\n"+t.teamOppPoints/g+" ("+t.rankTeamOppPoints+")\n"+
                t.teamYards/g+" ("+t.rankTeamYards+")\n"+t.teamOppYards/g+" ("+t.rankTeamOppYards+")\n\n"+
                t.teamPassYards/g+" ("+t.rankTeamPassYards+")\n"+t.teamRushYards/g+" ("+t.rankTeamRushYards+")\n"+
                t.teamOppPassYards/g+" ("+t.rankTeamOppPassYards+")\n"+t.teamOppRushYards/g+" ("+t.rankTeamOppRushYards+")\n\n"+
                t.teamOffTalent+" ("+t.rankTeamOffTalent+")\n"+t.teamDefTalent+" ("+t.rankTeamDefTalent+")\n"+
                t.teamPrestige+" ("+t.rankTeamPrestige+")\n");
        g = homeTeam.numGames();
        t = homeTeam;
        gameR.append("#"+t.rankTeamPollScore+" "+t.abbr+"\n"+t.wins+"-"+t.losses+"\n"+
                t.teamPoints/g+" ("+t.rankTeamPoints+")\n"+t.teamOppPoints/g+" ("+t.rankTeamOppPoints+")\n"+
                t.teamYards/g+" ("+t.rankTeamYards+")\n"+t.teamOppYards/g+" ("+t.rankTeamOppYards+")\n\n"+
                t.teamPassYards/g+" ("+t.rankTeamPassYards+")\n"+t.teamRushYards/g+" ("+t.rankTeamRushYards+")\n"+
                t.teamOppPassYards/g+" ("+t.rankTeamOppPassYards+")\n"+t.teamOppRushYards/g+" ("+t.rankTeamOppRushYards+")\n\n"+
                t.teamOffTalent+" ("+t.rankTeamOffTalent+")\n"+t.teamDefTalent+" ("+t.rankTeamDefTalent+")\n"+
                t.teamPrestige+" ("+t.rankTeamPrestige+")\n");

        gameSum[0] = gameL.toString();
        gameSum[1] = gameC.toString();
        gameSum[2] = gameR.toString();

        StringBuilder gameScout = new StringBuilder();
        if (awayTeam.playersInjuredAll != null && !awayTeam.playersInjuredAll.isEmpty()) {
            Collections.sort(awayTeam.playersInjuredAll, new PlayerPositionComparator());
            gameScout.append("\n" + awayTeam.abbr + " Injury Report:\n");
            for (Player p : awayTeam.playersInjuredAll) {
                gameScout.append(p.getPosNameYrOvrPot_OneLine() + "\n");
            }
        }
        if (homeTeam.playersInjuredAll != null && !homeTeam.playersInjuredAll.isEmpty()) {
            Collections.sort(homeTeam.playersInjuredAll, new PlayerPositionComparator());
            gameScout.append("\n" + homeTeam.abbr + " Injury Report:\n");
            for (Player p : homeTeam.playersInjuredAll) {
                gameScout.append(p.getPosNameYrOvrPot_OneLine() + "\n");
            }
        }

        gameSum[3] = gameScout.toString();

        return gameSum;
    }

    /**
     * Gets the amount of pass yards by a certain team for this game.
     * @param ha home/away bool, false for home
     * @return number of pass yards by specified team
     */
    public int getPassYards( boolean ha ) {
        //ha = home/away, false for home, true for away
        if (!ha) return HomeQBStats[4];
        else return AwayQBStats[4];
    }

    /**
     * Gets the amount of rush yards by a certain team for this game.
     * @param ha home/away bool, false for home
     * @return number of rush yards by speicifed team
     */
    public int getRushYards( boolean ha ) {
        //ha = home/away, false for home, true for away
        if (!ha) return HomeRB1Stats[1] + HomeRB2Stats[1];
        else return AwayRB1Stats[1] + AwayRB2Stats[1];
    }

    /**
     * Gets home field advantage, which is a +3 bonus to certain things for the home team
     * @return 3 if for home, 0 for away
     */
    private int getHFadv() {
        //home field advantage
        int footIQadv = (homeTeam.getCompositeFootIQ() - awayTeam.getCompositeFootIQ())/5;
        if (footIQadv > 3) footIQadv = 3;
        if (footIQadv < -3) footIQadv = -3;
        if ( gamePoss ) {
            return 3 + footIQadv;
        }
        else {
            return -footIQadv;
        }
    }

    /**
     * Gets the even prefix for events that happen in the log.
     * Will be of type ALA 3 - 3 GEO, Time: Q2 X:XX, 1st and goal at 95 yard line.
     * @return String of the event prefix.
     */
    private String getEventPrefix() {
        String possStr;
        if ( gamePoss ) possStr = homeTeam.abbr;
        else possStr = awayTeam.abbr;
        String yardsNeedAdj = "" + gameYardsNeed;
        if (gameYardLine + gameYardsNeed >= 100) yardsNeedAdj = "Goal";
        int gameDownAdj;
        if (gameDown > 4) {
            gameDownAdj = 4;
        } else
        {
            gameDownAdj = gameDown;
        }
        return "\n\n" + homeTeam.abbr + " " + homeScore + " - " + awayScore + " " + awayTeam.abbr + ", Time: " + convGameTime() +
                "\n\t" + possStr + " " + gameDownAdj + " and " + yardsNeedAdj + " at " + gameYardLine + " yard line." + "\n";
    }

    /**
     * Converts the game time, which is 0-3600, into readable time, i.e. Q1 12:50
     * @return String of the converted game time
     */
    private String convGameTime() {
        if (!playingOT) {
            int qNum = (3600 - gameTime) / 900 + 1;
            int minTime;
            int secTime;
            String secStr;
            if (gameTime <= 1800 && firstHalf){ //Prevent Q3 1X:XX from displaying during the first half
                return "0:00 Q2";
            }
            if (gameTime <= 0 && numOT <= 0) { // Prevent Q5 1X:XX from displaying in the game log
                return "0:00 Q4";
            }
            else {
                minTime = (gameTime - 900 * (4 - qNum)) / 60;
                secTime = (gameTime - 900 * (4 - qNum)) - 60 * minTime;
                if (secTime < 10) secStr = "0" + secTime;
                else secStr = "" + secTime;
                return minTime + ":" + secStr + " Q" + qNum;
            }
        } else {
            if (!bottomOT) {
                return "TOP OT" + numOT;
            } else {
                return "BOT OT" + numOT;
            }
        }
    }

    /**
     * Plays game. Home team starts with ball, run plays till time expires.
     */
    public void playGame() {
        if ( !hasPlayed ) {
            gameEventLog = "LOG: #" + awayTeam.rankTeamPollScore + " " + awayTeam.abbr + " (" + awayTeam.wins + "-" + awayTeam.losses + ") @ #" +
                    homeTeam.rankTeamPollScore + " " + homeTeam.abbr + " (" + homeTeam.wins + "-" + homeTeam.losses + ")" + "\n" +
                    "---------------------------------------------------------\n\n" +
                    awayTeam.abbr + " Off Strategy: " + awayTeam.teamStratOff.getStratName() + "\n" +
                    awayTeam.abbr + " Def Strategy: " + awayTeam.teamStratDef.getStratName() + "\n" +
                    homeTeam.abbr + " Off Strategy: " + homeTeam.teamStratOff.getStratName() + "\n" +
                    homeTeam.abbr + " Def Strategy: " + homeTeam.teamStratDef.getStratName() + "\n";

            //probably establish some home field advantage before playing
            //First half -- Away team kicks off
            gameTime = 3600;
            firstHalf = true;
            gameDown = 1;
            gamePoss = true;
            gameYardsNeed = 10;
            gameYardLine = (int) (100 - ( awayTeam.getK(0).ratKickPow + 20 - 40*Math.random() ));

            // Regulation 1st Half
            while ( gameTime > 1800 ) {
                //play ball!
                if (gamePoss) runPlay(homeTeam, awayTeam);
                else runPlay(awayTeam, homeTeam);
            }

            //Second half -- Home team kicks off
            gameTime = 1800; // Q3 15:00
            firstHalf = false;
            gameDown = 1;
            gamePoss = false;
            gameYardsNeed = 10;
            gameYardLine = (int) (100 - ( homeTeam.getK(0).ratKickPow + 20 - 40*Math.random() ));


            // Regulation 2nd Half
            while ( gameTime > 0 ) {
                //play ball!
                if (gamePoss) runPlay(homeTeam, awayTeam);
                else runPlay(awayTeam, homeTeam);
            }


            // Add last play
            if (homeScore != awayScore) {
                gameEventLog += getEventPrefix() + "Time has expired! The game is over.";
            } else {
                gameEventLog += getEventPrefix() + "OVERTIME!\nTie game at 0:00, overtime begins!";
            }

            //Overtime (if needed)
            if (gameTime <= 0 && homeScore == awayScore) {
                playingOT = true;
                gamePoss = false;
                gameYardLine = 75;
                numOT++;
                gameTime = -1;
                gameDown = 1;
                gameYardsNeed = 10;

                while (playingOT) {
                    if (gamePoss) runPlay(homeTeam, awayTeam);
                    else runPlay(awayTeam, homeTeam);
                }
            }

            //game over, add wins
            if (homeScore > awayScore) {
                homeTeam.wins++;
                homeTeam.totalWins++;
                homeTeam.gameWLSchedule.add("W");
                awayTeam.losses++;
                awayTeam.totalLosses++;
                awayTeam.gameWLSchedule.add("L");
                homeTeam.gameWinsAgainst.add(awayTeam);
                homeTeam.winStreak.addWin(homeTeam.league.getYear());
                homeTeam.league.checkLongestWinStreak(homeTeam.winStreak);
                awayTeam.winStreak.resetStreak(awayTeam.league.getYear());
            } else {
                homeTeam.losses++;
                homeTeam.totalLosses++;
                homeTeam.gameWLSchedule.add("L");
                awayTeam.wins++;
                awayTeam.totalWins++;
                awayTeam.gameWLSchedule.add("W");
                awayTeam.gameWinsAgainst.add(homeTeam);
                awayTeam.winStreak.addWin(awayTeam.league.getYear());
                awayTeam.league.checkLongestWinStreak(awayTeam.winStreak);
                homeTeam.winStreak.resetStreak(homeTeam.league.getYear());
            }

            // Add points/opp points
            homeTeam.addGamePlayedPlayers(homeScore > awayScore);
            awayTeam.addGamePlayedPlayers(awayScore > homeScore);

            homeTeam.teamPoints += homeScore;
            awayTeam.teamPoints += awayScore;

            homeTeam.teamOppPoints += awayScore;
            awayTeam.teamOppPoints += homeScore;

            homeTeam.teamYards += homeYards;
            awayTeam.teamYards += awayYards;

            homeTeam.teamOppYards += awayYards;
            awayTeam.teamOppYards += homeYards;

            homeTeam.teamOppPassYards += getPassYards(true);
            awayTeam.teamOppPassYards += getPassYards(false);
            homeTeam.teamOppRushYards += getRushYards(true);
            awayTeam.teamOppRushYards += getRushYards(false);

            homeTeam.teamTODiff += awayTOs-homeTOs;
            awayTeam.teamTODiff += homeTOs-awayTOs;

            hasPlayed = true;

            addNewsStory();

            if (homeTeam.rivalTeam.equals(awayTeam.abbr) || awayTeam.rivalTeam.equals(homeTeam.abbr)) {
                if (homeScore > awayScore) {
                    homeTeam.wonRivalryGame = true;
                } else {
                    awayTeam.wonRivalryGame = true;
                }
            }

            // Set reference to right players
            homeQB = homeTeam.getQB(0);
            homeRBs = new PlayerRB[2];
            for (int i = 0; i < 2; ++i) {
                homeRBs[i] = homeTeam.getRB(i);
            }
            homeWRs = new PlayerWR[3];
            for (int i = 0; i < 3; ++i) {
                homeWRs[i] = homeTeam.getWR(i);
            }
            homeK = homeTeam.getK(0);

            awayQB = awayTeam.getQB(0);
            awayRBs = new PlayerRB[2];
            for (int i = 0; i < 2; ++i) {
                awayRBs[i] = awayTeam.getRB(i);
            }
            awayWRs = new PlayerWR[3];
            for (int i = 0; i < 3; ++i) {
                awayWRs[i] = awayTeam.getWR(i);
            }
            awayK = awayTeam.getK(0);

            homeTeam.checkForInjury();
            awayTeam.checkForInjury();

        }
    }

    /**
     * Add news story to the league depending on the games outcome.
     * Only if it is big upset, or undefeated team goes down, or there are 3+ OTs.
     */
    public void addNewsStory() {
        if (numOT >= 3) {
            // Thriller in OT
            Team winner, loser;
            int winScore, loseScore;
            if (awayScore > homeScore) {
                winner = awayTeam;
                loser = homeTeam;
                winScore = awayScore;
                loseScore = homeScore;
            } else {
                winner = homeTeam;
                loser = awayTeam;
                winScore = homeScore;
                loseScore = awayScore;
            }
            homeTeam.league.newsStories.get(homeTeam.league.currentWeek+1).add(
                    numOT+"OT Thriller!>"+winner.strRep()+" and "+loser.strRep()+" played an absolutely thrilling game "+
                            "that went to "+numOT+" overtimes, with "+winner.name+" finally emerging victorious "+winScore+" to "+loseScore+".");
        }
        else if (homeScore > awayScore && awayTeam.losses == 1 && awayTeam.league.currentWeek > 5) {
            // 5-0 or better team given first loss
            awayTeam.league.newsStories.get(homeTeam.league.currentWeek+1).add(
                    "Undefeated no more! "+awayTeam.name+" suffers first loss!" +
                            ">"+homeTeam.strRep()+" hands "+awayTeam.strRep()+
                            " their first loss of the season, winning "+homeScore+" to "+awayScore+".");
        }
        else if (awayScore > homeScore && homeTeam.losses == 1 && homeTeam.league.currentWeek > 5) {
            // 5-0 or better team given first loss
            homeTeam.league.newsStories.get(homeTeam.league.currentWeek+1).add(
                    "Undefeated no more! "+homeTeam.name+" suffers first loss!" +
                            ">"+awayTeam.strRep()+" hands "+homeTeam.strRep()+
                            " their first loss of the season, winning "+awayScore+" to "+homeScore+".");
        }
        else if (awayScore > homeScore && homeTeam.rankTeamPollScore < 20 &&
                (awayTeam.rankTeamPollScore - homeTeam.rankTeamPollScore) > 20) {
            // Upset!
            awayTeam.league.newsStories.get(awayTeam.league.currentWeek+1).add(
                    "Upset! " + awayTeam.strRep() + " beats " + homeTeam.strRep() +
                            ">#" + awayTeam.rankTeamPollScore + " " + awayTeam.name + " was able to pull off the upset on the road against #" +
                            homeTeam.rankTeamPollScore + " " + homeTeam.name + ", winning " + awayScore + " to " + homeScore + ".");
        }
        else if (homeScore > awayScore && awayTeam.rankTeamPollScore < 20 &&
                (homeTeam.rankTeamPollScore - awayTeam.rankTeamPollScore) > 20) {
            // Upset!
            homeTeam.league.newsStories.get(homeTeam.league.currentWeek+1).add(
                    "Upset! "+homeTeam.strRep()+" beats "+awayTeam.strRep()+
                            ">#"+homeTeam.rankTeamPollScore+" "+homeTeam.name+" was able to pull off the upset at home against #"+
                            awayTeam.rankTeamPollScore+" "+awayTeam.name+", winning "+homeScore+" to "+awayScore+".");
        }

    }

    /**
     * Run play. Type of play run determined by offensive strengths and type of situation.
     * Split into three main areas: Before the play, During the play, and After the play
     * @param offense offense running the play
     * @param defense defense defending the play
     */
    private void runPlay( Team offense, Team defense ) {

        //                               //
        //                               //
        //        BEFORE THE PLAY        //
        //                               //
        //                               //

        //Simplify the if statements (this is a preference thing, feel free to change it if you find it harder to read)
        int homeScoreDiff = homeScore - awayScore; //Positive for lead, negative for trailing
        int awayScoreDiff = awayScore - homeScore;

        // If it's 1st and Goal to go, adjust yards needed to reflect distance for a TD so that play selection reflects actual yards to go
        // If we don't do this, gameYardsNeed may be higher than the actually distance for a TD and suboptimal plays may be chosen
        if (gameDown == 1 && gameYardLine >= 91) gameYardsNeed = 100 - gameYardLine;

        if (runningClock) { //Burn off time in the huddle/at the line
            switch (checkGameSituation()) {
                case 1: // Two minute drill
                    gameTime -= 10 + (int) (Math.random() * 6); //10-15 seconds run off
                    break;

                case 2: // Milk the clock
                    gameTime -= 25 + (int) (Math.random() * 11); //25-35 seconds run off
                    break;

                case 3: // Kneel down
                    gameTime -= 39; //Snap as late as possible into play clock
                    break;

                default: // No special situation
                    gameTime -= 15 + (int) (Math.random() * 16); //15-30 seconds run off
            }
            if ((gameTime <= 0 && !playingOT) || (gameTime <= 1800 && firstHalf)) { //Check if time expired or if the first half ended while huddling/at the line
                return; //and exit runPlay() if it did
            }

        }

        playPrefix = getEventPrefix();

        //Now we know how time we're taking off the clock, let's figure out what the best play to run is
        double preferPass = (offense.getPassProf() * 2 - defense.getPassDef()) * Math.random() - 10;
        double preferRush = (offense.getRushProf() * 2 - defense.getRushDef()) * Math.random() + offense.teamStratOff.getRYB();


        //                               //
        //                               //
        //         RUN THE PLAY          //
        //                               //
        //                               //


        switch (checkGameSituation()) {

            case 0: //Normal Game Situation

                normalGameSituationPlay(offense, defense, checkGameSituation(), preferPass, preferRush);
                break;

            case 1: //Two Minute Drill

                twoMinuteDrillPlay(offense, defense, checkGameSituation(), preferPass+5, preferRush);
                break;

            case 2: // Milk the Clock

                milkTheClock(offense, defense, checkGameSituation(), preferPass, preferRush+5);
                break;

            default: //Normal

                normalGameSituationPlay(offense, defense, checkGameSituation(), preferPass, preferRush)
                break;
        }


        //                               //
        //                               //
        //        AFTER THE PLAY         //
        //                               //
        //                               //

        if ( gotTD ){//Add touchdown, kick xp, kickoff
            runningClock = false;
            if(gamePoss){
                homeScore += 6;
            } else{
                awayScore += 6;
            }
            addPointsQuarter(6);

            kickXP(offense, defense);
            if (!playingOT) kickOff( offense );
            else resetForOT();
        }

       else if ( gotFumble ) { //Resolve change of possession and reset of downs.
            runningClock = false;
            gameYardLine += yardsGain; //Advance gameYardLine for the sake of determining where the turnover is given to the defense
            gameEventLog += fumbleInfo  + gameYardLine + " yard line.";
            if (gamePoss) { // home possession
                homeTOs++;
            } else {
                awayTOs++;
            }
            if (!playingOT) {
                gameDown = 1;
                gameYardsNeed = 10;
                gamePoss = !gamePoss;
                gameYardLine = 100 - gameYardLine;

            }
            else {
                resetForOT();
            }
        }

        else if ( gotInt ) { //Resolve change of possession and reset of downs
            runningClock = false;
            gameEventLog += playPrefix + "TURNOVER!\n" + offense.abbr + " QB " + offense.getQB(0).name + " was intercepted.";

            offense.getQB(0).statsInt++;
            if (!playingOT) {
                gameDown = 1;
                gameYardsNeed = 10;
                gamePoss = !gamePoss;
                gameYardLine = 100 - gameYardLine;
            }
            else resetForOT();
        }
        else if ( gotSafety ){
            gotSafety = false;
        }
        else{
            gameYardLine += yardsGain;
            gameYardsNeed -= yardsGain;
            gameDown++;
            if(gameYardsNeed <= 0){ //First down!
                gameYardsNeed = 10;
                gameDown = 1;
            }
            if(gameDown > 4){ //Turn over on downs
                if (!playingOT) {
                    //Log the turnover on downs, reset down and distance, give possession to the defense, exit this runPlay()
                    gameEventLog += getEventPrefix() + "TURNOVER ON DOWNS!\n" + offense.abbr + " failed to convert on " + (gameDown - 1) + "th down. " + defense.abbr + " takes over possession on downs.";

                    //Turn over on downs, change possession, set to first down and 10 yards to go
                    gamePoss = !gamePoss;
                    gameDown = 1;
                    gameYardsNeed = 10;
                    //and flip which direction the ball is moving in
                    gameYardLine = 100 - gameYardLine;

                }
                else {
                    //OT is over for the offense, log the turnover on downs, run resetForOT().
                    gameEventLog += getEventPrefix() + "TURNOVER ON DOWNS!\n" + offense.abbr + " failed to convert on " + (gameDown - 1) + "th down in OT and their possession is over.";
                    resetForOT();

                }
            }
        }

        gotTD = false;
        gotInt = false;
        gotFumble = false;

    }


    /**
     * Examine the current time (after running the last play, but before the huddle) and score
     * Based on the time and scores, pick a game situation to influence play selection and clock management
     * @return int that corresponds to game situation
     */

    private int checkGameSituation() {
        //Simplify the if statements (this is a preference thing, feel free to change it if you find it harder to read)
        int homeScoreDiff = homeScore - awayScore; //Positive for lead, negative for trailing
        int awayScoreDiff = awayScore - homeScore;

        twoMinuteDrill = 1;
        milkTheClock = 2;
        kneelDown = 3;

        if (!playingOT) { // None of these scenarios occur in OT and we don't want them to trigger in OT
            // Check for Two Minute Drill - 2 minutes or less left in the half or 2 mins or less left in game and team with ball is trailing or tied
            if ((gameTime <= 1920 && firstHalf) || ((gameTime <= 120) && ((gamePoss && homeScoreDiff < 1) || (!gamePoss && awayScoreDiff < 1)))) {
                return twoMinuteDrill;
            }

            //Check if it's time to milk the clock - Team with the lead has the ball and wants to burn off some clock inside of 5 minutes left in the game
            else if ((gameTime <= 300) && ((gamePoss && homeScoreDiff > 0) || (!gamePoss && awayScoreDiff > 0))) {
                return milkTheClock;
            }

            //Check if team with the ball has a lead with 2 minutes or less to play, if so, kneel the ball three times and end the game
            else if ((gameTime <= 120) && ((gamePoss && homeScoreDiff > 0) || (!gamePoss && awayScoreDiff > 0))) {
                return kneelDown;
            } else return 0; //Normal game situation or special scenario has not been programmed yet
        }
        else return 0;
    }

    /**
     * Give ball to correct team and reset yard line/down for new team.
     * In top OT frame, give the other team the ball, reset down, distance and yard line, and move to bottom OT frame
     * In bottom OT frame, if score is tied, keep playing.
     * If not, the game is over.
     */
    private void resetForOT() {
        if (bottomOT && homeScore == awayScore) {
            gameYardLine = 75;
            gameYardsNeed = 10;
            gameDown = 1;
            numOT++;
            gamePoss = numOT % 2 == 0;
            gameTime = -1;
            bottomOT = false;
            //runPlay( awayTeam, homeTeam );
        } else if (!bottomOT) {
            gamePoss = !gamePoss;
            gameYardLine = 75;
            gameYardsNeed = 10;
            gameDown = 1;
            gameTime = -1;
            bottomOT = true;
            //runPlay( homeTeam, awayTeam );
        } else {
            // game is not tied after both teams had their chance
            playingOT = false;
        }
    }
    /**
     * Passing play.
     * @param offense throwing the ball
     * @param defense defending the pass
     */
    private void passingPlay( Team offense, Team defense, int gameSituation ) {

        switch(gameSituation){
            case 0: //Normal
                normalPass(offense, defense);
                break;

            case 1: //Two Minute
                twoMinutePass(offense, defense);
                break;

            case 2: //Milk The Clock
                clockMilkPass(offense, defense);
                break;

            default:
                normalPass(offense, defense);
                break;
        }

    }

    private void rushingPlay( Team offense, Team defense, int gameSituation ) {

        switch(gameSituation){
            case 0: //Normal
                normalRush(offense, defense);
                break;

            case 1: //Two Minute
                twoMinuteRush(offense, defense);
                break;

            case 2: //Milk The Clock
                clockMilkRush(offense, defense);
                break;

            case 3: //Kneel Down\
                kneelDownRush(offense, defense);
                break;

            default:
                normalPass(offense, defense);
                break;
        }

    }


    /**
     * Normal Passing Play -- Executed Under Most Circumstances
     * @param offense passing the ball
     * @param defense defending the play
     */
    private void normalPass(Team offense, Team defense){
        //choose WR to throw to, better WRs more often
        double WR1pref = Math.pow( offense.getWR(0).ratOvr , 1 ) * Math.random();
        double WR2pref = Math.pow( offense.getWR(1).ratOvr , 1 ) * Math.random();
        double WR3pref = Math.pow( offense.getWR(2).ratOvr , 1 ) * Math.random();

        PlayerWR selWR;
        PlayerCB selCB;
        int [] selWRStats;
        if ( WR1pref > WR2pref && WR1pref > WR3pref ) {
            selWR = offense.getWR(0);
            selCB = defense.getCB(0);
            if (gamePoss) {
                selWRStats = HomeWR1Stats;
            } else selWRStats = AwayWR1Stats;
        } else if ( WR2pref > WR1pref && WR2pref > WR3pref ) {
            selWR = offense.getWR(1);
            selCB = defense.getCB(1);
            if (gamePoss) {
                selWRStats = HomeWR2Stats;
            } else selWRStats = AwayWR2Stats;
        } else {
            selWR = offense.getWR(2);
            selCB = defense.getCB(2);
            if (gamePoss) {
                selWRStats = HomeWR3Stats;
            } else selWRStats = AwayWR3Stats;
        }

        //get how much pressure there is on qb, check if sack
        int pressureOnQB = defense.getCompositeF7Pass()*2 - offense.getCompositeOLPass() - getHFadv();
        if ( Math.random()*100 < pressureOnQB/8 ) {
            //sacked!
            qbSack(offense);
            runningClock = true;
            gameTime -= 3 + (int)(Math.random() * 3); //Burn 3 to 6 seconds
            return;
        }

        //check for int
        double intChance = (pressureOnQB + defense.getS(0).ratOvr - (offense.getQB(0).ratPassAcc+offense.getQB(0).ratFootIQ+100)/3)/18
                + offense.teamStratOff.getPAB() + defense.teamStratDef.getPAB();
        if (intChance < 0.015) intChance = 0.015;
        if ( 100*Math.random() < intChance ) {
            //Interception
            qbInterception( offense );
            runningClock = false;
            gameTime -= 5 + (int)(Math.random() * 5);
            gotInt = true;
            return;
        }

        double completion = ( getHFadv() + normalize(offense.getQB(0).ratPassAcc) + normalize(selWR.ratRecCat)
                        - normalize(selCB.ratCBCov) )/2 + 18.25 - pressureOnQB/16.8 - offense.teamStratOff.getPAB() - defense.teamStratDef.getPAB();

        //throw ball, check for completion
        if ( 100*Math.random() < completion ) {
            if ( 100*Math.random() < (100 - selWR.ratRecCat)/3 ) {
                //drop
                selWRStats[4]++;
                selWR.statsDrops++;
                passAttempt(offense, selWR, selWRStats, yardsGain);

                gameTime -= 5 + (int)(4*Math.random()); //Ball was dropped, burn 5 to 8 seconds
                runningClock = false; //Incompletion stops the clock
                return;

            } else {
                //no drop
                yardsGain = (int) (( normalize(offense.getQB(0).ratPassPow) + normalize(selWR.ratRecSpd) - normalize(selCB.ratCBSpd) )*Math.random()/3.7
                        + offense.teamStratOff.getPYB()/2 - defense.teamStratDef.getPYB());
                //see if receiver can get yards after catch
                double escapeChance = (normalize(selWR.ratRecEva)*3 - selCB.ratCBTkl - defense.getS(0).ratOvr)*Math.random()
                        + offense.teamStratOff.getPYB() - defense.teamStratDef.getPAB();
                if ( escapeChance > 92 || Math.random() > 0.95 ) {
                    yardsGain += 3 + selWR.ratRecSpd*Math.random()/3;
                }
                if ( escapeChance > 75 && Math.random() < (0.1 + (offense.teamStratOff.getPAB()-defense.teamStratDef.getPAB())/200)) {
                    //wr escapes for TD
                    yardsGain = (100 - gameYardLine);
                }

                if ( yardsGain >= (100 - gameYardLine)) { //TD!
                    yardsGain -= gameYardLine - 100;
                    gameYardLine = 100 - yardsGain;
                    addPointsQuarter(6);
                    passingTD(offense, selWR, selWRStats, yardsGain);
                    //offense.teamPoints += 6;
                    //defense.teamOppPoints += 6;
                    gotTD = true;
                }
                else { // Completion but no TD
                    //check for fumble
                    double fumChance = (defense.getS(0).ratSTkl + selCB.ratCBTkl)/2;
                    if ( 100*Math.random() < fumChance/50 ) {
                        //Fumble!
                        gotFumble = true;
                        //Time burned off is 5 seconds (snap, read, throw, catch) + either 50%, 75%, or 100% of the greater of yards gained divided by 10, or 1.
                        gameTime -= 5 + (int)(((Math.random()*3 + 2)/4) * (Math.max(yardsGain/10,1)));
                        runningClock = false; //Turnover, clock stops
                        String recoveredBy;
                        if (normalize(defense.getS(0).ratSTkl)*Math.random() > normalize(selCB.ratCBTkl)*Math.random()) recoveredBy = defense.getS(0).name;
                        else recoveredBy = selCB.name;

                        fumbleInfo = playPrefix + "TURNOVER!\n" + offense.abbr + " WR " + selWR.name + " fumbled the ball after a catch, recovered by " + recoveredBy + " at the ";
                        selWRStats[5]++;
                        selWR.statsFumbles++;
                        return;
                    }
                }

                if (!gotTD && !gotFumble) {
                    if(Math.random() < 0.06){
                        firstDownClock = true; //5% chance catch goes out of bounds -- Clock only stops inside of 5 minutes to go in a half
                    }
                    runningClock = true;
                }

                //stats management
                passCompletion(offense, defense, selWR, selWRStats, yardsGain);

                //Time burned off is 5 seconds (snap, read, throw, catch) + either 50%, 75%, or 100% of the greater of yards gained divided by 10, or 1.
                gameTime -= 5 + (int)(((Math.random()*3 + 2)/4) * (Math.max(yardsGain/10,1)));
            }

        } else {
            //no completion, manage stats
            passAttempt(offense, selWR, selWRStats, yardsGain);
            //Incomplete pass stops the clock, so just run time for how long the play took, then move on
            runningClock = false;
            gameTime -= 5 + (int)(4*Math.random()); //Pass was inc, burn 5 to 8 seconds
            return;
        }

    }

    /**
     * Passing Play within the Two Minute Drill
     * @param offense passing the ball
     * @param defense defending the play
     */
    private void twoMinutePass(Team offense, Team defense){
        //Check to see if we need to spike the ball
        if((gameDown == 1 && gameTime < 60 && runningClock) || (gameDown == 2 && gameTime < 40 && runningClock)){
            //We're in a two minute situation with not much time left, so spike the ball. Willing to trade a down for seconds on the clock
            runningClock = false;
            gameTime -= 2;

            offense.getQB(0).statsPassAtt++; //Count the spike as an incompletion with no targets

            if(gamePoss){
                HomeQBStats[1]++;
            }else{
                AwayQBStats[1]++;
            }
            return;
        }
        //Not spiking it, so lets run a play

        //choose WR to throw to, better WRs more often
        double WR1pref = Math.pow( offense.getWR(0).ratOvr , 1 ) * Math.random();
        double WR2pref = Math.pow( offense.getWR(1).ratOvr , 1 ) * Math.random();
        double WR3pref = Math.pow( offense.getWR(2).ratOvr , 1 ) * Math.random();

        PlayerWR selWR;
        PlayerCB selCB;
        int [] selWRStats;
        if ( WR1pref > WR2pref && WR1pref > WR3pref ) {
            selWR = offense.getWR(0);
            selCB = defense.getCB(0);
            if (gamePoss) {
                selWRStats = HomeWR1Stats;
            } else selWRStats = AwayWR1Stats;
        } else if ( WR2pref > WR1pref && WR2pref > WR3pref ) {
            selWR = offense.getWR(1);
            selCB = defense.getCB(1);
            if (gamePoss) {
                selWRStats = HomeWR2Stats;
            } else selWRStats = AwayWR2Stats;
        } else {
            selWR = offense.getWR(2);
            selCB = defense.getCB(2);
            if (gamePoss) {
                selWRStats = HomeWR3Stats;
            } else selWRStats = AwayWR3Stats;
        }

        //get how much pressure there is on qb, check if sack
        int pressureOnQB = defense.getCompositeF7Pass()*2 - offense.getCompositeOLPass() - getHFadv();
        if ( Math.random()*100 < pressureOnQB/8 ) {
            //sacked!
            qbSack(offense);
            runningClock = true;
            gameTime -= 3 + (int)(Math.random() * 3); //Burn 3 to 6 seconds
            return;
        }

        //check for int
        double intChance = (pressureOnQB + defense.getS(0).ratOvr - (offense.getQB(0).ratPassAcc+offense.getQB(0).ratFootIQ+100)/3)/18
                + offense.teamStratOff.getPAB() + defense.teamStratDef.getPAB();
        if (intChance < 0.015) intChance = 0.015;
        if ( 100*Math.random() < intChance ) {
            //Interception
            qbInterception( offense );
            runningClock = false;
            gameTime -= 5 + (int)(Math.random() * 5);
            gotInt = true;
            return;
        }

        double completion = ( getHFadv() + normalize(offense.getQB(0).ratPassAcc) + normalize(selWR.ratRecCat)
                - normalize(selCB.ratCBCov) )/2 + 18.25 - pressureOnQB/16.8 - offense.teamStratOff.getPAB() - defense.teamStratDef.getPAB();

        //throw ball, check for completion
        if ( 100*Math.random() < completion ) {
            if ( 100*Math.random() < (100 - selWR.ratRecCat)/3 ) {
                //drop
                selWRStats[4]++;
                selWR.statsDrops++;
                passAttempt(offense, selWR, selWRStats, yardsGain);

                gameTime -= 5 + (int)(4*Math.random()); //Ball was dropped, burn 5 to 8 seconds
                runningClock = false; //Incompletion stops the clock
                return;

            } else {
                //no drop
                yardsGain = (int) (( normalize(offense.getQB(0).ratPassPow) + normalize(selWR.ratRecSpd) - normalize(selCB.ratCBSpd) )*Math.random()/3.7
                        + offense.teamStratOff.getPYB()/2 - defense.teamStratDef.getPYB());
                //see if receiver can get yards after catch
                double escapeChance = (normalize(selWR.ratRecEva)*3 - selCB.ratCBTkl - defense.getS(0).ratOvr)*Math.random()
                        + offense.teamStratOff.getPYB() - defense.teamStratDef.getPAB();
                if ( escapeChance > 95 || Math.random() > 0.98 ) { //Typically a two minute drill type pass will be short passes to the sideline without much YAC
                    yardsGain += 1 + selWR.ratRecSpd*Math.random()/4;
                }
                if ( escapeChance > 90 && Math.random() < (0.1 + (offense.teamStratOff.getPAB()-defense.teamStratDef.getPAB())/200)) { //If you're shooting for the sideline you need a lot of luck or skill to break away for a TD
                    //wr escapes for TD
                    yardsGain = (100 - gameYardLine);
                }

                if ( yardsGain >= (100 - gameYardLine)) { //TD!
                    yardsGain -= gameYardLine - 100;
                    gameYardLine = 100 - yardsGain;
                    addPointsQuarter(6);
                    passingTD(offense, selWR, selWRStats, yardsGain);
                    //offense.teamPoints += 6;
                    //defense.teamOppPoints += 6;
                    gotTD = true;
                }
                else { // Completion but no TD
                    //check for fumble
                    double fumChance = (defense.getS(0).ratSTkl + selCB.ratCBTkl)/2;
                    if ( 100*Math.random() < fumChance/50 ) {
                        //Fumble!
                        gotFumble = true;
                        //Time burned off is 5 seconds (snap, read, throw, catch) + either 50%, 75%, or 100% of the greater of yards the ball traveled/was carried divided by 10, or 1.
                        gameTime -= 7 + (int)(((Math.random()*3 + 2)/4) * (Math.max(yardsGain/10,1)));
                        runningClock = false; //Turnover, clock stops
                        String recoveredBy;
                        if (normalize(defense.getS(0).ratSTkl)*Math.random() > normalize(selCB.ratCBTkl)*Math.random()) recoveredBy = defense.getS(0).name;
                        else recoveredBy = selCB.name;

                        fumbleInfo = playPrefix + "TURNOVER!\n" + offense.abbr + " WR " + selWR.name + " fumbled the ball after a catch, recovered by " + recoveredBy + " at the ";
                        selWRStats[5]++;
                        selWR.statsFumbles++;
                        return;
                    }
                }

                if (!gotTD && !gotFumble) { // Ball was caught and the runner was tackled without it being a TD. Check to see if the QB was smart enough to throw to the sideline, if the WR was smart enough to make the catch near the sideline, and get OOB,
                    //Check to see if average of QB IQ + (Avg of WR IQ + Spd) is greater than half of average of CB speed and tackling, after factoring home field advantage for the defense only. Essentially this only stays in bounds if the CB makes an amazing play or the QB/WR really screw the pooch. (CB stats intentionally not normalized to make it harder to keep the receiver inbounds)
                    if((normalize(offense.getQB(0).ratFootIQ) + (normalize(selWR.ratFootIQ) + normalize(selWR.ratRecSpd))/2 )/2 > ((selCB.ratCBSpd/2) + (selCB.ratCBTkl/2))/2 + Math.min(0, getHFadv()) - (int)(5*Math.random()) ){
                       runningClock = false; // Ball was caught and brought OOB -- Inside of two minutes this stops the clock
                    }
                    runningClock = true; //Ball was caught and tackled in bounds
                }

                //stats management
                passCompletion(offense, defense, selWR, selWRStats, yardsGain);

                //Time burned off is 5 seconds (snap, read, throw, catch) + either 50%, 75%, or 100% of the greater of yards gained divided by 10, or 1.
                //A 99 yard catch and run that burns off the max possible time will take 14 seconds, which seems about right after looking at replays of this happening in real games.
                gameTime -= 5 + (int)(((Math.random()*3 + 2)/4) * (Math.max(yardsGain/10,1)));
            }

        } else {
            //no completion, manage stats
            passAttempt(offense, selWR, selWRStats, yardsGain);
            //Incomplete pass stops the clock, so just run time for how long the play took, then move on
            runningClock = false;
            gameTime -= 5 + (int)(4*Math.random()); //Pass was inc, burn 5 to 8 seconds
            return;
        }
    }

    /**
     * Passing Play run while trying to burn time off the clock
     * @param offense passing the ball
     * @param defense defending the play
     */
    private void clockMilkPass(Team offense, Team defense) {
            //choose WR to throw to, better WRs more often
            double WR1pref = Math.pow(offense.getWR(0).ratOvr, 1) * Math.random();
            double WR2pref = Math.pow(offense.getWR(1).ratOvr, 1) * Math.random();
            double WR3pref = Math.pow(offense.getWR(2).ratOvr, 1) * Math.random();

            PlayerWR selWR;
            PlayerCB selCB;
            int[] selWRStats;
            if (WR1pref > WR2pref && WR1pref > WR3pref) {
                selWR = offense.getWR(0);
                selCB = defense.getCB(0);
                if (gamePoss) {
                    selWRStats = HomeWR1Stats;
                } else selWRStats = AwayWR1Stats;
            } else if (WR2pref > WR1pref && WR2pref > WR3pref) {
                selWR = offense.getWR(1);
                selCB = defense.getCB(1);
                if (gamePoss) {
                    selWRStats = HomeWR2Stats;
                } else selWRStats = AwayWR2Stats;
            } else {
                selWR = offense.getWR(2);
                selCB = defense.getCB(2);
                if (gamePoss) {
                    selWRStats = HomeWR3Stats;
                } else selWRStats = AwayWR3Stats;
            }

            //get how much pressure there is on qb, check if sack
            int pressureOnQB = defense.getCompositeF7Pass() * 2 - offense.getCompositeOLPass() - getHFadv();
            if (Math.random() * 100 < pressureOnQB / 8) {
                //sacked!
                qbSack(offense);
                runningClock = true;
                gameTime -= 3 + (int) (Math.random() * 3); //Burn 3 to 6 seconds
                return;
            }

            //check for int
            double intChance = (pressureOnQB + defense.getS(0).ratOvr - (offense.getQB(0).ratPassAcc + offense.getQB(0).ratFootIQ + 100) / 3) / 18
                    + offense.teamStratOff.getPAB() + defense.teamStratDef.getPAB() - 2;
            if (intChance < 0.0075) intChance = 0.0075;
            if (100 * Math.random() < intChance) {
                //Interception
                qbInterception(offense);
                runningClock = false;
                gameTime -= 5 + (int) (Math.random() * 5);
                gotInt = true;
                return;
            }

            double completion = (getHFadv() + normalize(offense.getQB(0).ratPassAcc) + normalize(selWR.ratRecCat)
                    - normalize(selCB.ratCBCov)) / 2 + 18.25 - pressureOnQB / 16.8 - offense.teamStratOff.getPAB() - defense.teamStratDef.getPAB();

            //throw ball, check for completion
            if (100 * Math.random() - 5 < completion) {
                if (100 * Math.random() < (100 - selWR.ratRecCat) / 3) {
                    //drop
                    selWRStats[4]++;
                    selWR.statsDrops++;
                    passAttempt(offense, selWR, selWRStats, yardsGain);

                    gameTime -= 5 + (int) (4 * Math.random()); //Ball was dropped, burn 5 to 8 seconds
                    runningClock = false; //Incompletion stops the clock
                    return;

                } else {
                    //no drop
                    yardsGain = Math.max((int) (((normalize(offense.getQB(0).ratPassPow)*0.8) + (normalize(selWR.ratRecSpd)*0.8) - normalize(selCB.ratCBSpd)) * Math.random() / 3.7
                            + offense.teamStratOff.getPYB() / 2 - defense.teamStratDef.getPYB()),1);
                    //see if receiver can get yards after catch -- Severely limited by the fact that this is a short high percentage pass.
                    double escapeChance = (normalize(selWR.ratRecEva) * 3 - selCB.ratCBTkl - defense.getS(0).ratOvr) * Math.random()
                            + offense.teamStratOff.getPYB() - defense.teamStratDef.getPAB();
                    if (escapeChance > 95 || Math.random() > 0.97) {
                        yardsGain += 1 + selWR.ratRecSpd * Math.random() / 5;
                    }
                    if (escapeChance > 98 && Math.random() < (0.1 + (offense.teamStratOff.getPAB() - defense.teamStratDef.getPAB()) / 200)) {
                        //wr escapes for TD
                        yardsGain = (100 - gameYardLine);
                    }

                    if (yardsGain >= (100 - gameYardLine)) { //TD!
                        yardsGain -= gameYardLine - 100;
                        gameYardLine = 100 - yardsGain;
                        addPointsQuarter(6);
                        passingTD(offense, selWR, selWRStats, yardsGain);
                        //offense.teamPoints += 6;
                        //defense.teamOppPoints += 6;
                        gotTD = true;
                    } else { // Completion but no TD
                        //check for fumble
                        double fumChance = (defense.getS(0).ratSTkl + selCB.ratCBTkl) / 2;
                        if (100 * Math.random() < fumChance / 50) {
                            //Fumble!
                            gotFumble = true;
                            //Time burned off is 5 seconds (snap, read, throw, catch) + either 50%, 75%, or 100% of the greater of yards gained divided by 10, or 1.
                            gameTime -= 5 + (int) (((Math.random() * 3 + 2) / 4) * (Math.max(yardsGain / 10, 1)));
                            runningClock = false; //Turnover, clock stops
                            String recoveredBy;
                            if (normalize(defense.getS(0).ratSTkl)*Math.random() > normalize(selCB.ratCBTkl)*Math.random()) recoveredBy = defense.getS(0).name;
                            else recoveredBy = selCB.name;

                            fumbleInfo = playPrefix + "TURNOVER!\n" + offense.abbr + " WR " + selWR.name + " fumbled the ball after a catch, recovered by " + recoveredBy + " at the ";
                            selWRStats[5]++;
                            selWR.statsFumbles++;
                            return;
                        }
                    }

                    if (!gotTD && !gotFumble) {
                        runningClock = true;
                    }

                    //stats management
                    passCompletion(offense, defense, selWR, selWRStats, yardsGain);

                    //Time burned off is 5 seconds (snap, read, throw, catch) + either 50%, 75%, or 100% of the greater of yards gained divided by 10, or 1.
                    gameTime -= 5 + (int) (((Math.random() * 3 + 2) / 4) * (Math.max(yardsGain / 10, 1)));
                }

            } else {
                //no completion, manage stats
                passAttempt(offense, selWR, selWRStats, yardsGain);
                //Incomplete pass stops the clock, so just run time for how long the play took, then move on
                runningClock = false;
                gameTime -= 5 + (int) (4 * Math.random()); //Pass was inc, burn 5 to 8 seconds
                return;
            }

        }

    /**
     * Rushing Play using running backs.
     * @param offense running the ball
     * @param defense defending the run
     */
    private void normalRush( Team offense, Team defense ) {
        //pick RB to run
        PlayerRB selRB;
        double RB1pref = Math.pow( offense.getRB(0).ratOvr , 1.5 ) * Math.random();
        double RB2pref = Math.pow( offense.getRB(1).ratOvr , 1.5 ) * Math.random();

        if (RB1pref > RB2pref) {
            selRB = offense.getRB(0);
        } else {
            selRB = offense.getRB(1);
        }

        int blockAdv = offense.getCompositeOLRush() - defense.getCompositeF7Rush();
        int yardsGain = (int) ((selRB.ratRushSpd + blockAdv + getHFadv()) * Math.random() / 10 + offense.teamStratOff.getRYB()/2 - defense.teamStratDef.getRYB()/2);
        if (yardsGain < 2) {
            yardsGain += selRB.ratRushPow/20 - 3 - defense.teamStratDef.getRYB()/2;
        } else {
            //break free from tackles
            if (Math.random() < ( 0.28 + ( offense.teamStratOff.getRAB() - defense.teamStratDef.getRYB()/2 )/50 )) {
                yardsGain += selRB.ratRushEva/5 * Math.random();
            }
        }

        gameTime -= 1 + (Math.max(3,yardsGain/10) * ((int)(Math.random()*2 + 4)/4)); //Min time burned 4 seconds (0-10 yard gain), Max 14 seconds

        if ( gameYardLine + yardsGain >= 100 ) { //TD!
            addPointsQuarter(6);
            yardsGain -= gameYardLine - 100;
            gameYardLine = 100 - yardsGain;
            if ( gamePoss ) { // home possession
                if (RB1pref > RB2pref) {
                    HomeRB1Stats[2]++;
                } else {
                    HomeRB2Stats[2]++;
                }
            } else {
                if (RB1pref > RB2pref) {
                    AwayRB1Stats[2]++;
                } else {
                    AwayRB2Stats[2]++;
                }
            }
            tdInfo = offense.abbr + " RB " + selRB.name + " rushed " + yardsGain + " yards for a TD.";
            selRB.statsTD++;
            //offense.teamPoints += 6;
            //defense.teamOppPoints += 6;

            gotTD = true;
        }

        //stats management
        rushAttempt(offense, defense, selRB, RB1pref, RB2pref, yardsGain);

        if ( !gotTD ){
            //check for fumble
            double fumChance = (defense.getS(0).ratSTkl + defense.getCompositeF7Rush() - getHFadv())/2 + offense.teamStratOff.getRAB();
            if ( 100*Math.random() < fumChance/50 ) {
                //Fumble!
                if ( gamePoss ) {
                    homeTOs++;
                    if (RB1pref > RB2pref) {
                        HomeRB1Stats[3]++;
                    } else {
                        HomeRB2Stats[3]++;
                    }
                } else {
                    awayTOs++;
                    if (RB1pref > RB2pref) {
                        AwayRB1Stats[3]++;
                    } else {
                        AwayRB2Stats[3]++;
                    }
                }
                String recoveredBy;
                //Determine who recovered the fumble, random chance for the safety, or a random F7 depending on who gets the higher roll on the skill check
                if (normalize(defense.getS(0).ratSTkl)*Math.random() > (normalize(defense.getCompositeF7Rush())*Math.random())/7) recoveredBy = defense.getS(0).name; // Safety recovers
                else recoveredBy = defense.getF7((int)(Math.random()*7)).name; // F7 member recovers
                fumbleInfo = playPrefix + "TURNOVER!\n" + offense.abbr + " RB " + selRB.name + " fumbled the ball while rushing, recovered by " + recoveredBy + " at the "; //Set the fumble info up to be reported at the end of runPlay()
                selRB.statsFumbles++;
                gotFumble = true;
            }
        }

        if (!gotTD && !gotFumble) {
            if(Math.random() < 0.03){
                firstDownClock = true; //3% chance catch goes out of bounds -- Clock only stops inside of 5 minutes to go in a half
            }
            runningClock = true;
        }
    }

    /**
     * Attempt a field goal using the offense's kicker.
     * If successful, add 3 points and kick off.
     * If not, turn the ball over.
     * @param offense kicking the ball
     * @param defense defending the kick
     */
    private void fieldGoalAtt( Team offense, Team defense ) {
        double fgDistRatio = Math.pow((110 - gameYardLine)/50,2);
        double fgAccRatio = Math.pow((110 - gameYardLine)/50,1.25);
        double fgDistChance = ( getHFadv() + offense.getK(0).ratKickPow - fgDistRatio*80 );
        double fgAccChance = ( getHFadv() + offense.getK(0).ratKickAcc - fgAccRatio*80 );
        if ( fgDistChance > 20 && fgAccChance*Math.random() > 15 ) {
            // made the fg
            if ( gamePoss ) { // home possession
                homeScore += 3;
                HomeKStats[3]++;
                HomeKStats[2]++;
            } else {
                awayScore += 3;
                AwayKStats[3]++;
                AwayKStats[2]++;
            }
            gameEventLog += getEventPrefix() + offense.abbr + " K " + offense.getK(0).name + " made the " + (110-gameYardLine) + " yard FG.";
            addPointsQuarter(3);
            offense.getK(0).statsFGMade++;
            offense.getK(0).statsFGAtt++;
            if (!playingOT) kickOff( offense );
            else resetForOT();

        } else {
            //miss
            gameEventLog += getEventPrefix() + offense.abbr + " K " + offense.getK(0).name + " missed the " + (110-gameYardLine) + " yard FG.";
            offense.getK(0).statsFGAtt++;
            if (!playingOT) {
                gameYardLine = Math.max(100 - gameYardLine, 20); //Misses inside the 20 = defense takes over on the 20
                gameDown = 1;
                gameYardsNeed = 10;
                if (gamePoss) { // home possession
                    HomeKStats[3]++;
                } else {
                    AwayKStats[3]++;
                }
                gamePoss = !gamePoss;
            }
            else resetForOT();


        }

        gameTime -= 20;

    }

    /**
     * Kick the extra point after the touchdown.
     * Will decide to go for 2 under certain circumstances.
     * @param offense going for the point after
     * @param defense defending the point after
     */
    private void kickXP( Team offense, Team defense ) {
        // No XP/2pt try if the TD puts the bottom OT offense ahead (aka wins the game)
        if (playingOT && bottomOT && (((numOT % 2 == 0) && awayScore > homeScore) || ((numOT % 2 != 0) && homeScore > awayScore)))
        {
            gameEventLog += getEventPrefix() + " " + tdInfo + "\n" + offense.abbr + " wins on a walk-off touchdown!";
        }
        // If a TD is scored as time expires, there is no XP/2pt if the score difference is greater than 2
        else if (!playingOT && gameTime <= 0 && ((homeScore - awayScore > 2) || (awayScore - homeScore > 2))) {
            //Walkoff TD!
            if ((Math.abs(homeScore - awayScore) < 7) && ((gamePoss && homeScore > awayScore) || (!gamePoss && awayScore > homeScore))) gameEventLog += getEventPrefix() + " " + tdInfo + "\n" + offense.abbr + " wins on a walk-off touchdown!";
            //Just rubbing in the win or saving some pride
            else gameEventLog += getEventPrefix() + " " + tdInfo;
        }
        else {
        if ( (numOT >= 3) || (((gamePoss && (awayScore - homeScore) == 2) || (!gamePoss && (homeScore - awayScore) == 2)) && gameTime < 300 )) {
            //go for 2
            boolean successConversion = false;
            if ( Math.random() <= 0.50 ) {
                //rushing
                int blockAdv = offense.getCompositeOLRush() - defense.getCompositeF7Rush();
                int yardsGain = (int) ((offense.getRB(0).ratRushSpd + blockAdv) * Math.random() / 6);
                if ( yardsGain > 5 ) {
                    successConversion = true;
                    if ( gamePoss ) { // home possession
                        homeScore += 2;
                    } else {
                        awayScore += 2;
                    }
                    addPointsQuarter(2);
                    gameEventLog += getEventPrefix() + " " + tdInfo + " " + offense.getRB(0).name + " rushed for the 2pt conversion.";
                } else {
                    gameEventLog += getEventPrefix() + " " + tdInfo + " " + offense.getRB(0).name + " stopped at the line of scrimmage, failed the 2pt conversion.";
                }
            } else {
                int pressureOnQB = defense.getCompositeF7Pass()*2 - offense.getCompositeOLPass();
                double completion = ( normalize(offense.getQB(0).ratPassAcc) + offense.getWR(0).ratRecCat - defense.getCB(0).ratCBCov )/2 + 25 - pressureOnQB/20;
                if ( 100*Math.random() < completion ) {
                    successConversion = true;
                    if ( gamePoss ) { // home possession
                        homeScore += 2;
                    } else {
                        awayScore += 2;
                    }
                    addPointsQuarter(2);
                    gameEventLog += getEventPrefix() + " " + tdInfo + " " + offense.getQB(0).name + " completed the pass to " + offense.getWR(0).name + " for the 2pt conversion.";
                } else {
                    gameEventLog += getEventPrefix() + " " + tdInfo + " " + offense.getQB(0).name + "'s pass incomplete to " + offense.getWR(0).name + " for the failed 2pt conversion.";
                }
            }

        } else {
            //kick XP
            if ( Math.random()*100 < 23 + offense.getK(0).ratKickAcc && Math.random() > 0.01 ) {
                //made XP
                if ( gamePoss ) { // home possession
                    homeScore += 1;
                    HomeKStats[0]++;
                    HomeKStats[1]++;
                } else {
                    awayScore += 1;
                    AwayKStats[0]++;
                    AwayKStats[1]++;
                }
                gameEventLog += getEventPrefix() + " " + tdInfo + " " + offense.getK(0).name + " made the XP.";
                addPointsQuarter(1);
                offense.getK(0).statsXPMade++;
            } else {
                gameEventLog += getEventPrefix() + " " + tdInfo + " " + offense.getK(0).name + " missed the XP.";
                // missed XP
                if ( gamePoss ) { // home possession
                    HomeKStats[1]++;
                } else {
                    AwayKStats[1]++;
                }
            }
            offense.getK(0).statsXPAtt++;
        }
    }
}
    /**
     * Kick the ball off, turning the ball over to the other team.
     * @param offense kicking the ball off
     */
    private void kickOff( Team offense ) {
        if (gameTime <= 0) return;
        else {
        //Decide whether to onside kick. Only if losing but within 8 points with < 3 min to go
        if ( gameTime < 180 && ((gamePoss && (awayScore - homeScore) <= 8 && (awayScore - homeScore) > 0)
                || (!gamePoss && (homeScore - awayScore) <=8 && (homeScore - awayScore) > 0))) {
            // Yes, do onside
            if (offense.getK(0).ratKickFum * Math.random() > 60 || Math.random() < 0.1) {
                //Success!
                gameEventLog += getEventPrefix() + offense.abbr + " K " + offense.getK(0).name + " successfully executes onside kick! " + offense.abbr + " has possession!";
            } else {
                // Fail
                gameEventLog += getEventPrefix() + offense.abbr + " K " + offense.getK(0).name + " failed the onside kick and lost possession.";
                gamePoss = !gamePoss;
            }
            gameYardLine = 50;
            gameDown = 1;
            gameYardsNeed = 10;

            gameTime -= 4 + 5*Math.random(); //Onside kicks are very fast, unless there's a weird fight for the ball. Chance to burn a lot of time, odds are you'll burn a little time.
        } else {
            // Just regular kick off
            gameYardLine = (int) (100 - ( offense.getK(0).ratKickPow + 20 - 40*Math.random() ));
            if ( gameYardLine <= 0 ) gameYardLine = 25;
            gameDown = 1;
            gameYardsNeed = 10;
            gamePoss = !gamePoss;
        }

        gameTime -= 15*Math.random();
    }
}
    /**
     * Kick the ball off following a safety, turning the ball over to the other team.
     * Safety free kicks happen from the 20 instead of the 35, so start the kicker from further back.
     * @param offense kicking the ball off
     */
    private void freeKick( Team offense ) {
        if (gameTime <= 0) return;
        else {
        //Decide whether to onside kick. Only if losing but within 8 points with < 3 min to go
        if ( gameTime < 180 && ((gamePoss && (awayScore - homeScore) <= 8 && (awayScore - homeScore) > 0)
                || (!gamePoss && (homeScore - awayScore) <=8 && (homeScore - awayScore) > 0))) {
            // Yes, do onside
            if (offense.getK(0).ratKickFum * Math.random() > 60 || Math.random() < 0.1) {
                //Success!
                gameEventLog += getEventPrefix() + offense.abbr + " K " + offense.getK(0).name + " successfully executes onside kick! " + offense.abbr + " has possession!";
                gameYardLine = 35;
                gameDown = 1;
                gameYardsNeed = 10;
            } else {
                // Fail
                gameEventLog += getEventPrefix() + offense.abbr + " K " + offense.getK(0).name + " failed the onside kick and lost possession.";
                gamePoss = !gamePoss;
                gameYardLine = 65;
                gameDown = 1;
                gameYardsNeed = 10;
            }

            gameTime -= 4 + 4*Math.random(); //Onside kicks are very fast, unless there's a weird fight for the ball. Chance to burn a lot of time, odds are you'll burn a little time.
        } else {
            // Just regular kick off
            gameYardLine = (int) (115 - ( offense.getK(0).ratKickPow + 20 - 40*Math.random() ));
            if ( gameYardLine <= 0 ) gameYardLine = 25;
            gameDown = 1;
            gameYardsNeed = 10;
            gamePoss = !gamePoss;
            gameTime -= 15*Math.random();
        
            }
        }
    }

    /**
     * Punt the ball if it is a 4th down and decided not to go for it.
     * Will turnover possession.
     * @param offense kicking the punt
     */
    private void puntPlay( Team offense ) {
        gameYardLine = (int) (100 - ( gameYardLine + offense.getK(0).ratKickPow/3 + 20 - 10*Math.random() ));
        if ( gameYardLine < 0 ) {
            gameYardLine = 20;
        }
        gameDown = 1;
        gameYardsNeed = 10;
        gamePoss = !gamePoss;

        gameTime -= 20 + 15*Math.random();
    }

    /**
     * Sack the offense's QB. If it is past 0 yard line, call for a safety.
     * @param offense offense that is gettign sacked
     */
    private void qbSack( Team offense ) {
        offense.getQB(0).statsSacked++;
        gameYardsNeed += 3;
        gameYardLine -= 3;
        if ( gamePoss ) { // home possession
            HomeQBStats[5]++;
        } else {
            AwayQBStats[5]++;
        }

        if (gameYardLine < 0) safety(); // Safety!
    }

    /**
     * Perform safety. Will add 2 to the correct team and give the ball over via a free kick.
     */
    private void safety() {
        gotSafety = true;
        //addPointsQuarter(2);
        if (gamePoss) {
            awayScore += 2;
            gameEventLog += getEventPrefix() + "SAFETY!\n" + homeTeam.abbr + " QB " + homeTeam.getQB(0).name +
                    " was tackled in the endzone! Result is a safety and " + awayTeam.abbr + " will get possession.";
            freeKick(homeTeam);
        } else {
            homeScore += 2;
            gameEventLog += getEventPrefix() + "SAFETY!\n" + awayTeam.abbr + " QB " + awayTeam.getQB(0).name +
                    " was tackled in the endzone! Result is a safety and " + homeTeam.abbr + " will get possession.";
            freeKick(awayTeam);
        }
    }

    /**
     * Perform an interception on the offense's QB. Will turn the ball over and add the needed stats.
     * @param offense offense that has been intercepted.
     */
    private void qbInterception( Team offense ) {
        if ( gamePoss ) { // home possession
            HomeQBStats[3]++;
            HomeQBStats[1]++;
            homeTOs++;
        } else {
            AwayQBStats[3]++;
            AwayQBStats[1]++;
            awayTOs++;
        }
    }

    /**
     * Passing touchdown stat tracking. Add 6 points and yards for QB/WR.
     * @param offense offense who got the TD
     * @param selWR WR who caught the TD
     * @param selWRStats stats array for that WR
     * @param yardsGain number of yards gained by the TD
     */
    private void passingTD( Team offense, PlayerWR selWR, int[] selWRStats, int yardsGain ){
        if ( gamePoss ) { // home possession
            homeScore += 6;
            HomeQBStats[2]++;
            selWRStats[3]++;
        } else {
            awayScore += 6;
            AwayQBStats[2]++;
            selWRStats[3]++;
        }
        tdInfo = offense.abbr + " QB " + offense.getQB(0).name + " threw a " + yardsGain + " yard TD to " + selWR.name + ".";
        offense.getQB(0).statsTD++;
        selWR.statsTD++;
    }

    /**
     * Pass completion stat tracking. used for team mostly.
     * @param offense offense who threw the pass
     * @param defense defense who defended the pass
     * @param selWR WR who caught the pass
     * @param selWRStats stat array for that WR
     * @param yardsGain number of yards gained by that pass
     */
    private void passCompletion( Team offense, Team defense, PlayerWR selWR, int[] selWRStats, int yardsGain ) {
        offense.getQB(0).statsPassComp++;
        offense.getQB(0).statsPassYards += yardsGain;
        selWR.statsReceptions++;
        selWR.statsRecYards += yardsGain;
        offense.teamPassYards += yardsGain;
        //offense.teamYards += yardsGain;
        //defense.teamOppYards += yardsGain;

        if ( gamePoss ) { // home possession
            HomeQBStats[0]++;
            selWRStats[0]++;
        } else {
            AwayQBStats[0]++;
            selWRStats[0]++;
        }
    }

    /**
     * Stat tracking for a pass attempt (not necessarily completion). Used for QB mostly.
     * @param offense offense who threw the pass
     * @param selWR WR who tries to catch the pass
     * @param selWRStats stat array for that WR
     * @param yardsGain yards
     */
    private void passAttempt( Team offense, PlayerWR selWR, int[] selWRStats, int yardsGain ) {
        offense.getQB(0).statsPassAtt++;
        selWR.statsTargets++;

        if ( gamePoss ) { // home possession
            homeYards += yardsGain;
            HomeQBStats[4] += yardsGain;
            HomeQBStats[1]++;
            selWRStats[2] += yardsGain;
            selWRStats[1]++;
        } else {
            awayYards += yardsGain;
            AwayQBStats[4] += yardsGain;
            AwayQBStats[1]++;
            selWRStats[2] += yardsGain;
            selWRStats[1]++;
        }
    }

    /**
     * Rush attempt stat tracking
     * @param offense offense who ran the run play
     * @param defense defense who defended the play
     * @param selRB RB who ran the ball
     * @param RB1pref number to compare to see who was chosen
     * @param RB2pref number to compare to see who was chosen
     * @param yardsGain yards gained by the rush
     */
    private void rushAttempt( Team offense, Team defense, PlayerRB selRB, double RB1pref, double RB2pref, int yardsGain ) {
        selRB.statsRushAtt++;
        selRB.statsRushYards += yardsGain;
        offense.teamRushYards += yardsGain;
        //offense.teamYards += yardsGain;
        //defense.teamOppYards += yardsGain;

        if ( gamePoss ) { // home possession
            homeYards += yardsGain;
            if (RB1pref > RB2pref) {
                HomeRB1Stats[0]++;
                HomeRB1Stats[1] += yardsGain;
            } else {
                HomeRB2Stats[0]++;
                HomeRB2Stats[1] += yardsGain;
            }
        } else {
            awayYards += yardsGain;
            if (RB1pref > RB2pref) {
                AwayRB1Stats[0]++;
                AwayRB1Stats[1] += yardsGain;
            } else {
                AwayRB2Stats[0]++;
                AwayRB2Stats[1] += yardsGain;
            }
        }
    }

    /**
     * Add points to the correct quarter (not used anymore?)
     * @param points points added
     */
    private void addPointsQuarter( int points ) {
        if ( gamePoss ) {
            //home poss
            if ( gameTime > 2700 ) {
                homeQScore[0] += points;
            } else if ( gameTime > 1800 ) {
                homeQScore[1] += points;
            } else if ( gameTime > 900 ) {
                homeQScore[2] += points;
            } else if ( numOT == 0 ) {
                homeQScore[3] += points;
            } else {
                if ( 3+numOT < 10 ) homeQScore[3+numOT] += points;
                else homeQScore[9] += points;
            }
        } else {
            //away
            if ( gameTime > 2700 ) {
                awayQScore[0] += points;
            } else if ( gameTime > 1800 ) {
                awayQScore[1] += points;
            } else if ( gameTime > 900 ) {
                awayQScore[2] += points;
            } else if ( numOT == 0 ) {
                awayQScore[3] += points;
            } else {
                if ( 3+numOT < 10 ) awayQScore[3+numOT] += points;
                else awayQScore[9] += points;
            }
        }
    }

    /**
     * Select and run a play based on no special game situation
     * @param offense running the play
     * @param defense defending the play
     * @param gameSituation int for the current game situation so that the play functions operate as expected
     * @param preferPass Number generated to determine how much the team wants to pass the ball
     * @param preferRush Number generated to determine how much the team wants to run the ball
     */
 private void normalGameSituationPlay(Team offense, Team defense, int gameSituation, double preferPass, double preferRush){
     int homeScoreDiff = homeScore - awayScore; //Positive for lead, negative for trailing
     int awayScoreDiff = awayScore - homeScore;

     if (gameDown >= 4) {
         if (((gamePoss && awayScoreDiff > 3) || (!gamePoss && homeScoreDiff > 3)) && gameTime < 300) {
             //go for it since we need 7 to win -- This also forces going for it if down by a TD in BOT OT
             if (gameYardsNeed < 3) {
                 rushingPlay(offense, defense);
             } else {
                 passingPlay(offense, defense, gameSituation);
             }
         } else {
             //4th down
             if (gameYardsNeed < 3) {
                 if (gameYardLine > 65) {
                     //fga
                     fieldGoalAtt(offense, defense);
                 } else if (gameYardLine > 55) {
                     // run play, go for it!
                     rushingPlay(offense, defense);
                 } else {
                     //punt
                     puntPlay(offense);
                 }
             } else if (gameYardLine > 60) {
                 //fga
                 fieldGoalAtt(offense, defense);
             } else {
                 //punt
                 puntPlay(offense);
             }
         }
     }

     else if ((gameDown == 3 && gameYardsNeed > 4)  ||  ((gameDown == 1 || gameDown == 2) && (preferPass >= preferRush))) {
         // pass play
         passingPlay(offense, defense, checkGameSituation());
     } else {
         //run play
         rushingPlay(offense, defense);
     }

 }

    /**
     * Select and run a play based in the Two Minute Drill
     * @param offense running the play
     * @param defense defending the play
     * @param gameSituation int for the current game situation so that the play functions operate as expected
     * @param preferPass Number generated to determine how much the team wants to pass the ball
     * @param preferRush Number generated to determine how much the team wants to run the ball
     */
    private void twoMinuteDrillPlay(Team offense,Team defense,int gameSituation, double preferPass, double preferRush) {
        int homeScoreDiff = homeScore - awayScore; //Positive for lead, negative for trailing
        int awayScoreDiff = awayScore - homeScore;


        //Under 20 seconds to play, check that the team with the ball is trailing or tied, do something based on the score difference
        if (gameTime <= 20 && !playingOT && ((gamePoss && (awayScore >= homeScore)) || (!gamePoss && (homeScore >= awayScore)))) {
            //Down by 3 or less, or tied, and you have the ball
            if (((gamePoss && awayScoreDiff <= 3) || (!gamePoss && homeScoreDiff <= 3)) && gameYardLine > 60) {
                //last second FGA
                fieldGoalAtt(offense, defense);
            } else {
                //hail mary
                passingPlay(offense, defense, gameSituation);
            }
        }
        else if(gameDown >= 4) {
            if (((gamePoss && awayScoreDiff > 3) || (!gamePoss && homeScoreDiff > 3)) && !firstHalf) {
                //go for it since we need 7 to win
                if (gameYardsNeed < 3) {
                    if (gameTime < 60) { //Not enough time to safely rush the ball
                        passingPlay(offense, defense, gameSituation);
                    } else { //Still a bit of time, so rushing is an option
                        rushingPlay(offense, defense);
                    }
                } else {
                    passingPlay(offense, defense, gameSituation);
                }

            } else {
                //4th down
                if (gameYardsNeed < 3) {
                    if (gameYardLine > 65) {
                        //fga
                        fieldGoalAtt(offense, defense);
                    } else if (gameYardLine > 55) {
                        //go for it!
                        if (gameTime < 60 || (firstHalf && gameTime < 1860)) { //Not enough time to rush the ball
                            passingPlay(offense, defense, gameSituation);
                        } else { //Run it!
                            rushingPlay(offense, defense);
                        }
                    } else if (homeScore == awayScore && gameYardLine >= 50) { //Tied in the Two Minute Drill past the 50
                        if (gameTime < 45 || (firstHalf && gameTime < 1845)) { //Not much time for the other team to respond, punting is safe here
                            puntPlay(offense);
                        } else { //Still enough time left for the other team to respond in a tied game; don't want to hand them a win or a free score
                            rushingPlay(offense, defense);
                        }
                    } else {
                        if (homeScore != awayScore && !firstHalf) { //We're in a 2nd Half 2 minute drill and the game is not tied (so offense is trailing)
                            rushingPlay(offense, defense); // No choice but to go for it, you need to move the ball to stay alive
                        } else { //You have to punt it, otherwise you're handing the other team a chance to score (and win if it's the second half)
                            puntPlay(offense);
                        }

                    }
                } else if (gameYardLine > 60) {
                    //fga
                    fieldGoalAtt(offense, defense);
                } else {
                    //punt
                    puntPlay(offense);
                }
            }
        }

            //Throw the ball unless very specific circumstances are met: First/second/third-and-short with more than minute left, and the team prefers running over passing runs the ball. Every other circumstance is a pass.
            else if ((gameDown == 3 && gameYardsNeed > 4)  ||  ((gameDown == 1 || gameDown == 2 || (gameDown == 3 && gameYardsNeed <= 4)) && (preferPass >= preferRush) && gameTime > 60)  ||  (gameTime < 61)) {
                // pass play
                passingPlay(offense, defense, gameSituation);
            } else {
                //run play
                rushingPlay(offense, defense);
            }
    }

    /**
     * Run a play while trying to burn time off the clock
     * @param offense running the play
     * @param defense defending the play
     * @param gameSituation int for the current game situation so that the play functions operate as expected
     * @param preferPass Number generated to determine how much the team wants to pass the ball
     * @param preferRush Number generated to determine how much the team wants to run the ball
     */
    private void milkTheClock(Team offense, Team defense, int gameSituation, double preferPass, double preferRush){

        if (gameDown >= 4) {
                //4th down
                if (gameYardsNeed < 3) {
                    if (gameYardLine > 65) {
                        //fga
                        fieldGoalAtt(offense, defense);
                    } else if (gameYardLine > 55) {
                        // run play, go for it!
                        rushingPlay(offense, defense);
                    } else {
                        //punt
                        puntPlay(offense);
                    }
                } else if (gameYardLine > 60) {
                    //fga
                    fieldGoalAtt(offense, defense);
                } else {
                    //punt
                    puntPlay(offense);
                }
            }

        else if ((gameDown == 3 && gameYardsNeed > 4)  ||  ((gameDown == 1 || gameDown == 2) && (preferPass >= preferRush)) && gameYardsNeed > 7) {
            // pass play
            passingPlay(offense, defense, gameSituation);
        } else {
            //run play
            rushingPlay(offense, defense);
        }
    }

    /**
     * Normalize a rating to make it more even. 80->90, 90->95, etc
     * @param rating rating to be normalized
     * @return normalized rating
     */
    private int normalize(int rating) {
        return (100 + rating)/2;
    }
}
