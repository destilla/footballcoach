package CFBsimPack;

import java.io.Serializable;
import java.util.ArrayList;

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
    
    String gameEventLog;
    String tdInfo;
    
    //private variables used when simming games
    private int gameTime;
    private boolean gamePoss; //1 if home, 0 if away
    private int gameYardLine;
    private int gameDown;
    private int gameYardsNeed;
    
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

        gameEventLog = "LOG: #" + awayTeam.rankTeamPollScore + " " + awayTeam.abbr + " (" + awayTeam.wins + "-" + awayTeam.losses + ") @ #" +
                homeTeam.rankTeamPollScore + " " + homeTeam.abbr + " (" + homeTeam.wins + "-" + homeTeam.losses + ")" + "\n" +
                "---------------------------------------------------------";
        
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

        if (gameName.equals("In Conf") && homeTeam.rivalTeam.equals(awayTeam.abbr)) {
            // Rivalry game!
            gameName = "Rivalry Game";
        }
    }
    
    /**
     * Create a game without a name (regular season game).
     * @param home
     * @param away 
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
        
        gameEventLog = "LOG: #" + awayTeam.rankTeamPollScore + " " + awayTeam.abbr + " (" + awayTeam.wins + "-" + awayTeam.losses + ") @ #" +
                       homeTeam.rankTeamPollScore + " " + homeTeam.abbr + " (" + homeTeam.wins + "-" + homeTeam.losses + ")" + "\n" +
                       "---------------------------------------------------------";
        
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
        gameC.append(awayTeam.abbr+"\n"+awayTeam.getQB(0).getInitialName()+"\n");
        gameC.append(awayTeam.getQB(0).getYrStr()+" "+awayTeam.getQB(0).ratOvr+"/"+awayTeam.getQB(0).ratPot+"\n");
        gameC.append(AwayQBStats[2]+"/"+AwayQBStats[3]+"\n"); //td/int
        gameC.append(AwayQBStats[4]+" yds\n"); //pass yards
        gameC.append(AwayQBStats[0]+"/"+AwayQBStats[1]+"\n"); //pass comp/att
        gameR.append(homeTeam.abbr+"\n"+homeTeam.getQB(0).getInitialName()+"\n");
        gameR.append(homeTeam.getQB(0).getYrStr()+" "+homeTeam.getQB(0).ratOvr+"/"+homeTeam.getQB(0).ratPot+"\n");
        gameR.append(HomeQBStats[2]+"/"+HomeQBStats[3]+"\n"); //td/int
        gameR.append(HomeQBStats[4]+" yds\n"); //pass yards
        gameR.append(HomeQBStats[0]+"/"+HomeQBStats[1]+"\n"); //pass comp/att

        /**
         * RBs
         */
        gameL.append("\nRBs\nRB1 Name\nYr Ovr/Pot\nTD/Fum\nRush Yards\nYds/Att\n");
        gameC.append("\n"+awayTeam.abbr+"\n"+awayTeam.getRB(0).getInitialName()+"\n");
        gameC.append(awayTeam.getRB(0).getYrStr()+" "+awayTeam.getRB(0).ratOvr+"/"+awayTeam.getRB(0).ratPot+"\n");
        gameC.append(AwayRB1Stats[2]+"/"+AwayRB1Stats[3]+"\n"); //td/fum
        gameC.append(AwayRB1Stats[1]+" yds\n"); //rush yards
        gameC.append(((double)(10*AwayRB1Stats[1]/AwayRB1Stats[0])/10)+"\n");
        gameR.append("\n"+homeTeam.abbr+"\n"+homeTeam.getRB(0).getInitialName()+"\n");
        gameR.append(homeTeam.getRB(0).getYrStr()+" "+homeTeam.getRB(0).ratOvr+"/"+homeTeam.getRB(0).ratPot+"\n");
        gameR.append(HomeRB1Stats[2]+"/"+HomeRB1Stats[3]+"\n"); //td/fum
        gameR.append(HomeRB1Stats[1]+" yds\n"); //rush yards
        gameR.append(((double)(10*HomeRB1Stats[1]/HomeRB1Stats[0])/10)+"\n");
        gameL.append("\n"); gameC.append("\n"); gameR.append("\n");
        gameL.append("RB2 Name\nYr Ovr/Pot\nTD/Fum\nRush Yards\nYds/Att\n");
        gameC.append(awayTeam.getRB(1).getInitialName()+"\n");
        gameC.append(awayTeam.getRB(1).getYrStr()+" "+awayTeam.getRB(1).ratOvr+"/"+awayTeam.getRB(1).ratPot+"\n");
        gameC.append(AwayRB2Stats[2]+"/"+AwayRB2Stats[3]+"\n"); //td/fum
        gameC.append(AwayRB2Stats[1]+" yds\n"); //rush yards
        gameC.append(((double)(10*AwayRB2Stats[1]/AwayRB2Stats[0])/10)+"\n");
        gameR.append(homeTeam.getRB(1).getInitialName()+"\n");
        gameR.append(homeTeam.getRB(1).getYrStr()+" "+homeTeam.getRB(1).ratOvr+"/"+homeTeam.getRB(1).ratPot+"\n");
        gameR.append(HomeRB2Stats[2]+"/"+HomeRB2Stats[3]+"\n"); //td/fum
        gameR.append(HomeRB2Stats[1]+" yds\n"); //rush yards
        gameR.append(((double)(10*HomeRB2Stats[1]/HomeRB2Stats[0])/10)+"\n");

        /**
         * WRs
         */
        gameL.append("\nWRs\nWR1 Name\nYr Ovr/Pot\nTD/Fum\nRec Yards\nRec/Tgts\n");
        gameC.append("\n"+awayTeam.abbr+"\n"+awayTeam.getWR(0).getInitialName()+"\n");
        gameC.append(awayTeam.getWR(0).getYrStr()+" "+awayTeam.getWR(0).ratOvr+"/"+awayTeam.getWR(0).ratPot+"\n");
        gameC.append(AwayWR1Stats[3]+"/"+AwayWR1Stats[5]+"\n"); //td/fum
        gameC.append(AwayWR1Stats[2]+" yds\n"); //rec yards
        gameC.append(AwayWR1Stats[0]+"/"+AwayWR1Stats[1]+"\n"); //rec/targets
        gameR.append("\n"+homeTeam.abbr+"\n"+homeTeam.getWR(0).getInitialName()+"\n");
        gameR.append(homeTeam.getWR(0).getYrStr()+" "+homeTeam.getWR(0).ratOvr+"/"+homeTeam.getWR(0).ratPot+"\n");
        gameR.append(HomeWR1Stats[3]+"/"+HomeWR1Stats[5]+"\n"); //td/fum
        gameR.append(HomeWR1Stats[2]+" yds\n"); //rec yards
        gameR.append(HomeWR1Stats[0]+"/"+HomeWR1Stats[1]+"\n"); //rec/targets
        gameL.append("\n"); gameC.append("\n"); gameR.append("\n");
        gameL.append("WR2 Name\nYr Ovr/Pot\nTD/Fum\nRec Yards\nRec/Tgts\n");
        gameC.append(awayTeam.getWR(1).getInitialName()+"\n");
        gameC.append(awayTeam.getWR(1).getYrStr()+" "+awayTeam.getWR(1).ratOvr+"/"+awayTeam.getWR(1).ratPot+"\n");
        gameC.append(AwayWR2Stats[3]+"/"+AwayWR2Stats[5]+"\n"); //td/fum
        gameC.append(AwayWR2Stats[2]+" yds\n"); //rec yards
        gameC.append(AwayWR2Stats[0]+"/"+AwayWR2Stats[1]+"\n"); //rec/targets
        gameR.append(homeTeam.getWR(1).getInitialName()+"\n");
        gameR.append(homeTeam.getWR(1).getYrStr()+" "+homeTeam.getWR(1).ratOvr+"/"+homeTeam.getWR(1).ratPot+"\n");
        gameR.append(HomeWR2Stats[3]+"/"+HomeWR2Stats[5]+"\n"); //td/fum
        gameR.append(HomeWR2Stats[2]+" yds\n"); //rec yards
        gameR.append(HomeWR2Stats[0]+"/"+HomeWR2Stats[1]+"\n"); //rec/targets
        gameL.append("\n"); gameC.append("\n"); gameR.append("\n");
        gameL.append("WR3 Name\nYr Ovr/Pot\nTD/Fum\nRec Yards\nRec/Tgts\n");
        gameC.append(awayTeam.getWR(2).getInitialName()+"\n");
        gameC.append(awayTeam.getWR(2).getYrStr()+" "+awayTeam.getWR(2).ratOvr+"/"+awayTeam.getWR(2).ratPot+"\n");
        gameC.append(AwayWR3Stats[3]+"/"+AwayWR3Stats[5]+"\n"); //td/fum
        gameC.append(AwayWR3Stats[2]+" yds\n"); //rec yards
        gameC.append(AwayWR3Stats[0]+"/"+AwayWR3Stats[1]+"\n"); //rec/targets
        gameR.append(homeTeam.getWR(2).getInitialName()+"\n");
        gameR.append(homeTeam.getWR(2).getYrStr()+" "+homeTeam.getWR(2).ratOvr+"/"+homeTeam.getWR(2).ratPot+"\n");
        gameR.append(HomeWR3Stats[3]+"/"+HomeWR3Stats[5]+"\n"); //td/fum
        gameR.append(HomeWR3Stats[2]+" yds\n"); //rec yards
        gameR.append(HomeWR3Stats[0]+"/"+HomeWR3Stats[1]+"\n"); //rec/targets

        /**
         * Ks
         */
        gameL.append("\nKs\nName\nYr Ovr/Pot\nFGM/FGA\nXPM/XPA\n");
        gameC.append("\n"+awayTeam.abbr+"\n"+awayTeam.getK(1).getInitialName()+"\n");
        gameC.append(awayTeam.getK(1).getYrStr()+" "+awayTeam.getK(1).ratOvr+"/"+awayTeam.getK(1).ratPot+"\n");
        gameC.append(AwayKStats[2]+"/"+AwayKStats[3]+" FG\n"+AwayKStats[0]+"/"+AwayKStats[1]+" XP\n");
        gameR.append("\n"+homeTeam.abbr+"\n"+homeTeam.getK(1).getInitialName()+"\n");
        gameR.append(homeTeam.getK(1).getYrStr()+" "+homeTeam.getK(1).ratOvr+"/"+homeTeam.getK(1).ratPot+"\n");
        gameR.append(HomeKStats[2]+"/"+HomeKStats[3]+" FG\n"+HomeKStats[0]+"/"+HomeKStats[1]+" XP\n");

        gameSum[0] = gameL.toString();
        gameSum[1] = gameC.toString();
        gameSum[2] = gameR.toString();
        gameSum[3] = gameEventLog;

        return gameSum;
    }

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
                "\nPass YPG\nRush YPG\nOpp PYPG\nOpp RYPG\n");
        int g = awayTeam.numGames();
        Team t = awayTeam;
        gameC.append("#"+t.rankTeamPollScore+" "+t.abbr+"\n"+t.wins+"-"+t.losses+"\n"+
                t.teamPoints/g+" ("+t.rankTeamPoints+")\n"+t.teamOppPoints/g+" ("+t.rankTeamOppPoints+")\n"+
                t.teamYards/g+" ("+t.rankTeamYards+")\n"+t.teamOppYards/g+" ("+t.rankTeamOppYards+")\n\n"+
                t.teamPassYards/g+" ("+t.rankTeamPassYards+")\n"+t.teamRushYards/g+" ("+t.rankTeamRushYards+")\n"+
                t.teamOppPassYards/g+" ("+t.rankTeamOppPassYards+")\n"+t.teamOppRushYards/g+" ("+t.rankTeamOppRushYards+")\n");
        g = homeTeam.numGames();
        t = homeTeam;
        gameR.append("#"+t.rankTeamPollScore+" "+t.abbr+"\n"+t.wins+"-"+t.losses+"\n"+
                t.teamPoints/g+" ("+t.rankTeamPoints+")\n"+t.teamOppPoints/g+" ("+t.rankTeamOppPoints+")\n"+
                t.teamYards/g+" ("+t.rankTeamYards+")\n"+t.teamOppYards/g+" ("+t.rankTeamOppYards+")\n\n"+
                t.teamPassYards/g+" ("+t.rankTeamPassYards+")\n"+t.teamRushYards/g+" ("+t.rankTeamRushYards+")\n"+
                t.teamOppPassYards/g+" ("+t.rankTeamOppPassYards+")\n"+t.teamOppRushYards/g+" ("+t.rankTeamOppRushYards+")\n");

        gameSum[0] = gameL.toString();
        gameSum[1] = gameC.toString();
        gameSum[2] = gameR.toString();
        gameSum[3] = "";

        return gameSum;
    }
    
    public int getPassYards( boolean ha ) {
        //ha = home/away, false for home, true for away
        if (!ha) return HomeQBStats[4];
        else return AwayQBStats[4];
    }
    
    public int getRushYards( boolean ha ) {
        //ha = home/away, false for home, true for away
        if (!ha) return HomeRB1Stats[1] + HomeRB2Stats[1];
        else return AwayRB1Stats[1] + AwayRB2Stats[1];
    }
    
    private int getHFadv() {
        //home field advantage
        if ( gamePoss ) return 3;
        else return 0;
    }
    
    private String getEventPrefix() {
        String possStr;
        if ( gamePoss ) possStr = homeTeam.abbr;
        else possStr = awayTeam.abbr;
        String yardsNeedAdj = "" + gameYardsNeed;
        if (gameYardLine + gameYardsNeed >= 100) yardsNeedAdj = "Goal";
        return "\n\n" + homeTeam.abbr + " " + homeScore + " - " + awayScore + " " + awayTeam.abbr + ", Time: " + convGameTime() + 
                "\n\t" + possStr + " " + gameDown + " and " + yardsNeedAdj + " at " + gameYardLine + " yard line." + "\n";
    }
    
    private String convGameTime() {
        int qNum = (3600 - gameTime) / 900 + 1;
        int minTime;
        int secTime;
        String secStr;
        if ( qNum >= 4 && numOT > 0 ) {
            minTime = gameTime / 60;
            secTime = gameTime - 60*minTime;
            if (secTime < 10) secStr = "0" + secTime;
            else secStr = "" + secTime;
            return minTime + ":" + secStr + " OT" + numOT;
        } else {
            minTime = (gameTime - 900*(4-qNum)) / 60;
            secTime = (gameTime - 900*(4-qNum)) - 60*minTime;
            if (secTime < 10) secStr = "0" + secTime;
            else secStr = "" + secTime;
            return minTime + ":" + secStr + " Q" + qNum;
        }
    }
    
    /**
     * Plays game. Home team starts with ball, run plays till time expires.
     */
    public void playGame() {
        if ( !hasPlayed ) {
            gameEventLog = "LOG: #" + awayTeam.rankTeamPollScore + " " + awayTeam.abbr + " (" + awayTeam.wins + "-" + awayTeam.losses + ") @ #" +
                    homeTeam.rankTeamPollScore + " " + homeTeam.abbr + " (" + homeTeam.wins + "-" + homeTeam.losses + ")" + "\n" +
                    "---------------------------------------------------------";
            //probably establish some home field advantage before playing
            gameTime = 3600;
            gameDown = 1;
            gamePoss = true;
            gameYardsNeed = 10;
            gameYardLine = 20;

            while ( gameTime > 0 ) {
                //play ball!
                if (gamePoss) {
                    runPlay( homeTeam, awayTeam );
                } else {
                    runPlay( awayTeam, homeTeam );
                }
                if (gameTime <= 0 && homeScore == awayScore) {
                    gameTime = 900; //OT
                    gameYardLine = 20;
                    numOT++;
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
            } else {
                homeTeam.losses++;
                homeTeam.totalLosses++;
                homeTeam.gameWLSchedule.add("L");
                awayTeam.wins++;
                awayTeam.totalWins++;
                awayTeam.gameWLSchedule.add("W");
                awayTeam.gameWinsAgainst.add(homeTeam);
            }

            // Add points/opp points
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

            if (gameName.equals("Rivalry Game")) {
                if (homeScore > awayScore) {
                    homeTeam.wonRivalryGame = true;
                } else {
                    awayTeam.wonRivalryGame = true;
                }
            }
        }
    }

    public void addNewsStory() {
        if (homeScore > awayScore && awayTeam.losses == 1 && awayTeam.league.currentWeek > 5) {
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
        else if (numOT >= 3) {
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

    }
    
    /**
     * Run play. Type of play run determined by offensive strengths and type of situation.
     * @param offense
     * @param defense 
     */
    private void runPlay( Team offense, Team defense ) {
        if ( gameDown > 4 ) {
            gamePoss = !gamePoss;
            gameDown = 1;
        }
        double preferPass = (offense.getPassProf()*2 - defense.getPassDef()) * Math.random() - 10;
        double preferRush = (offense.getRushProf()*2 - defense.getRushDef()) * Math.random() + offense.teamStratOff.getRYB();
        
        if ( gameTime <= 30 ) {
            if ( ((gamePoss && (awayScore - homeScore) <= 3) || (!gamePoss && (homeScore - awayScore) <= 3)) && gameYardLine > 60 ) {
                //last second FGA
                fieldGoalAtt( offense, defense );
            } else {
                //hail mary
                passingPlay( offense, defense );
            }
        }
        else if ( gameDown >= 4 ) {
            if ( ((gamePoss && (awayScore - homeScore) > 3) || (!gamePoss && (homeScore - awayScore) > 3)) && gameTime < 300 ) {
                //go for it since we need 7 to win
                if ( gameYardsNeed < 3 ) {
                    rushingPlay( offense, defense );
                } else {
                    passingPlay( offense, defense );
                }
            } else {
                //4th down
                if ( gameYardsNeed < 3 ) {
                    if ( gameYardLine > 65 ) {
                        //fga
                        fieldGoalAtt( offense, defense );
                    } else if ( gameYardLine > 55 ) {
                        // run play, go for it!
                        rushingPlay( offense, defense );
                    } else {
                        //punt
                        puntPlay( offense );
                    }
                } else if ( gameYardLine > 60 ) {
                    //fga
                    fieldGoalAtt( offense, defense );
                } else {
                    //punt
                    puntPlay( offense );
                }
            }
        } else if ( (gameDown == 3 && gameYardsNeed > 4) || ((gameDown==1 || gameDown==2) && (preferPass >= preferRush)) ) {
            // pass play
            passingPlay( offense, defense );
        } else {
            //run play
            rushingPlay( offense, defense );
        }
    }
    
    /**
     * Passing play.
     * @param offense
     * @param defense 
     */
    private void passingPlay( Team offense, Team defense ) {
        int yardsGain = 0;
        boolean gotTD = false;
        boolean gotFumble = false;
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
            return;
        }
        
        //check for int
        double intChance = (pressureOnQB + defense.getS(0).ratOvr - (offense.getQB(0).ratPassAcc+offense.getQB(0).ratFootIQ+100)/3)/18
                + offense.teamStratOff.getPAB() + defense.teamStratDef.getPAB();
        if (intChance < 0.015) intChance = 0.015;
        if ( 100*Math.random() < intChance ) {
            //Interception
            qbInterception( offense );
            return;
        }
        
        //throw ball, check for completion
        double completion = ( getHFadv() + normalize(offense.getQB(0).ratPassAcc) + normalize(selWR.ratRecCat)
                - normalize(selCB.ratCBCov) )/2 + 18.25 - pressureOnQB/16.8 - offense.teamStratOff.getPAB() - defense.teamStratDef.getPAB();
        if ( 100*Math.random() < completion ) {
            if ( 100*Math.random() < (100 - selWR.ratRecCat)/3 ) {
                //drop
                gameDown++;
                selWRStats[4]++;
                selWR.statsDrops++;
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
                    yardsGain += 100;
                }

                //add yardage
                gameYardLine += yardsGain;
                if ( gameYardLine >= 100 ) { //TD!
                    yardsGain -= gameYardLine - 100;
                    gameYardLine = 100 - yardsGain;
                    addPointsQuarter(6);
                    passingTD(offense, selWR, selWRStats, yardsGain);
                    //offense.teamPoints += 6;
                    //defense.teamOppPoints += 6;
                    gotTD = true;
                } else {
                    //check for fumble
                    double fumChance = (defense.getS(0).ratSTkl + selCB.ratCBTkl)/2;
                    if ( 100*Math.random() < fumChance/40 ) {
                        //Fumble!
                        gotFumble = true;
                    }
                }
                
                //check downs
                gameYardsNeed -= yardsGain;
                if ( gameYardsNeed <= 0 ) {
                    gameDown = 1;
                    gameYardsNeed = 10;
                } else gameDown++;

                //stats management
                passCompletion(offense, defense, selWR, selWRStats, yardsGain);  
            }
            
        } else {
            //no completion, advance downs
            gameDown++;
        }
        
        passAttempt(offense, selWR, selWRStats, yardsGain);
        
        
        if ( gotFumble ) {
            gameEventLog += getEventPrefix() + "TURNOVER!\n" + offense.abbr + " WR " + selWR.name + " fumbled the ball after a catch.";
            selWRStats[5]++;
            selWR.statsFumbles++;
            gameDown = 1;
            gameYardsNeed = 10;
            if ( gamePoss ) { // home possession
                homeTOs++;
            } else {
                awayTOs++;
            }
            gamePoss = !gamePoss;
            gameYardLine = 100 - gameYardLine;
        }
        
        if ( gotTD ) {
            kickXP( offense, defense );
            kickOff( offense );
        }
        
        gameTime -= 15 + 15*Math.random();
        
    }
    
    /**
     * Rushing Play using running backs.
     * @param offense
     * @param defense 
     */
    private void rushingPlay( Team offense, Team defense ) {
        boolean gotTD = false;
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
        
        //add yardage
        gameYardLine += yardsGain;
        if ( gameYardLine >= 100 ) { //TD!
            addPointsQuarter(6);
            yardsGain -= gameYardLine - 100;
            gameYardLine = 100 - yardsGain;
            if ( gamePoss ) { // home possession
                homeScore += 6;
                if (RB1pref > RB2pref) {
                    HomeRB1Stats[2]++;
                } else {
                    HomeRB2Stats[2]++;
                }
            } else {
                awayScore += 6;
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
        
        //check downs
        gameYardsNeed -= yardsGain;
        if ( gameYardsNeed <= 0 ) {
            gameDown = 1;
            gameYardsNeed = 10;
        } else gameDown++;
            
        //stats management
        rushAttempt(offense, defense, selRB, RB1pref, RB2pref, yardsGain);
        
        if ( gotTD ) {
            kickXP( offense, defense );
            kickOff( offense );
        } else {
            gameTime -= 25 + 15*Math.random();
            //check for fumble
            double fumChance = (defense.getS(0).ratSTkl + defense.getCompositeF7Rush() - getHFadv())/2 + offense.teamStratOff.getRAB();
            if ( 100*Math.random() < fumChance/40 ) {
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
                gameEventLog += getEventPrefix() + "TURNOVER!\n" + offense.abbr + " RB " + selRB.name + " fumbled the ball while rushing.";
                selRB.statsFumbles++;
                gameDown = 1;
                gameYardsNeed = 10;
                gamePoss = !gamePoss;
                gameYardLine = 100 - gameYardLine;
            }
        }
        
    }
    
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
            //offense.teamPoints += 3;
            //defense.teamOppPoints += 3;
            offense.getK(0).statsFGMade++;
            offense.getK(0).statsFGAtt++;
            kickOff( offense );
            
        } else {
            //miss
            gameEventLog += getEventPrefix() + offense.abbr + " K " + offense.getK(0).name + " missed the " + (110-gameYardLine) + " yard FG.";
            offense.getK(0).statsFGAtt++;
            gameYardLine = 100 - gameYardLine;
            gameDown = 1;
            gameYardsNeed = 10;
            gamePoss = !gamePoss;
            if ( gamePoss ) { // home possession
                HomeKStats[3]++;
            } else {
                AwayKStats[3]++;
            }
        }
        
        gameTime -= 20;
        
    }
    
    private void kickXP( Team offense, Team defense ) {
        if ( ((gamePoss && (awayScore - homeScore) == 2) || (!gamePoss && (homeScore - awayScore) == 2)) && gameTime < 300 ) {
            //go for 2
            boolean successConversion = false;
            if ( Math.random() < 0.5 ) {
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
            if ( Math.random()*100 < 20 + offense.getK(0).ratKickAcc ) {
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
                //offense.teamPoints += 1;
                //defense.teamOppPoints += 1;
                offense.getK(0).statsXPMade++;
            } else {
                gameEventLog += getEventPrefix() + " " + tdInfo + " " + offense.getK(0).name + " missed the XP.";
            }
            offense.getK(0).statsXPAtt++;
        }
    }
    
    private void kickOff( Team offense ) {
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
        } else {
            // Just regular kick off
            gameYardLine = (int) (100 - ( offense.getK(0).ratKickPow + 20 - 40*Math.random() ));
            if ( gameYardLine <= 0 ) gameYardLine = 20;
            gameDown = 1;
            gameYardsNeed = 10;
            gamePoss = !gamePoss;
        }

        gameTime -= 15*Math.random();
    }
    
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
    
    private void qbSack( Team offense ) {
        offense.getQB(0).statsSacked++;
        gameYardsNeed += 3;
        gameYardLine -= 3;
        gameDown++;
        if ( gamePoss ) { // home possession
            HomeQBStats[5]++;
        } else {
            AwayQBStats[5]++;
        }

        if (gameYardLine < 0) {
            // Safety!
            safety();
        }
    }

    private void safety() {
        //addPointsQuarter(2);
        if (gamePoss) {
            awayScore += 2;
            gameEventLog += getEventPrefix() + "SAFETY!\n" + homeTeam.abbr + " QB " + homeTeam.getQB(0).name +
                " was tackled in the endzone! Result is a safety and " + awayTeam.abbr + " will get possession.";
            kickOff(homeTeam);
        } else {
            homeScore += 2;
            gameEventLog += getEventPrefix() + "SAFETY!\n" + awayTeam.abbr + " QB " + awayTeam.getQB(0).name +
                    " was tackled in the endzone! Result is a safety and " + homeTeam.abbr + " will get possession.";
            kickOff(awayTeam);
        }
    }
    
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
        gameEventLog += getEventPrefix() + "TURNOVER!\n" + offense.abbr + " QB " + offense.getQB(0).name + " was intercepted.";
        offense.getQB(0).statsInt++;
        gameDown = 1;
        gameYardsNeed = 10;
        gamePoss = !gamePoss;
        gameYardLine = 100 - gameYardLine;
    }
    
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

    private int normalize(int rating) {
        return (100 + rating)/2;
    }
    
}
