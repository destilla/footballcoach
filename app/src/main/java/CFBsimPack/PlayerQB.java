package CFBsimPack;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Class for QB player.
 * @author Achi
 */
public class PlayerQB extends Player {
    
    //public String name;
    //Overall rating, combination of other ratings
    //public int ratOvr;
    //Potential, affects how much he gets better in offseason
    //public int ratPot;
    //FootIQ, affects how smart he plays
    //public int ratFootIQ;
    //PassPow affects how far he can throw
    public int ratPassPow;
    //PassAcc affects how accurate his passes are
    public int ratPassAcc;
    //PassEva (evasiveness) affects how easily he can dodge sacks
    public int ratPassEva;
    
    //Stats
    public int statsPassAtt;
    public int statsPassComp;
    public int statsTD;
    public int statsInt;
    public int statsPassYards;
    public int statsSacked;
    
    public PlayerQB( String nm, Team t, int yr, int pot, int iq, int pow, int acc, int eva, boolean rs, int dur ) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (pow*3 + acc*4 + eva)/8;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratPassPow = pow;
        ratPassAcc = acc;
        ratPassEva = eva;
        isRedshirt = rs;
        if (isRedshirt) year = 0;

        cost = (int)(Math.pow((float)ratOvr - 55,2)/1.5) + 150 + (int)(Math.random()*100) - 50;
        
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratPassPow);
        ratingsVector.addElement(ratPassAcc);
        ratingsVector.addElement(ratPassEva);
        
        statsPassAtt = 0;
        statsPassComp = 0;
        statsTD = 0;
        statsInt = 0;
        statsPassYards = 0;
        statsSacked = 0;

        position = "QB";
    }
    
    public PlayerQB( String nm, int yr, int stars, Team t ) {
        name = nm;
        year = yr;
        team = t;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = (int) (50 + 50*Math.random());
        ratFootIQ = (int) (50 + 50*Math.random());
        ratDur = (int) (50 + 50*Math.random());
        ratPassPow = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratPassAcc = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratPassEva = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratOvr = (ratPassPow*3 + ratPassAcc*4 + ratPassEva)/8;
        
        cost = (int)(Math.pow((float)ratOvr - 55,2)/1.5) + 150 + (int)(Math.random()*100) - 50;
        
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratPassPow);
        ratingsVector.addElement(ratPassAcc);
        ratingsVector.addElement(ratPassEva);
        
        statsPassAtt = 0;
        statsPassComp = 0;
        statsTD = 0;
        statsInt = 0;
        statsPassYards = 0;
        statsSacked = 0;

        position = "QB";
    }
    
    public Vector getStatsVector() {
        Vector v = new Vector(9);
        v.add(statsPassComp);
        v.add(statsPassAtt);
        v.add((float)((int)((float)statsPassComp/statsPassAtt*1000))/10);
        v.add(statsTD);
        v.add(statsInt);
        v.add(statsPassYards);
        v.add(statsPassYards);
        v.add((float)((int)((float)statsPassYards/statsPassAtt*100))/100);
        v.add(statsSacked);
        return v;
    }
    
    public Vector getRatingsVector() {
        ratingsVector = new Vector();
        ratingsVector.addElement(name + " (" + getYrStr() + ")");
        ratingsVector.addElement(ratOvr + " (+" + ratImprovement + ")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratPassPow);
        ratingsVector.addElement(ratPassAcc);
        ratingsVector.addElement(ratPassEva);
        return ratingsVector;
    }
    
    @Override
    public void advanceSeason() {
        year++;
        int oldOvr = ratOvr;
        ratFootIQ += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratPassPow += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratPassAcc += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratPassEva += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        if ( Math.random()*100 < ratPot ) {
            //breakthrough
            ratPassPow += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratPassAcc += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratPassEva += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
        }
        ratOvr = (ratPassPow*3 + ratPassAcc*4 + ratPassEva)/8;
        ratImprovement = ratOvr - oldOvr;
        //reset stats (keep career stats?)
        statsPassAtt = 0;
        statsPassComp = 0;
        statsTD = 0;
        statsInt = 0;
        statsPassYards = 0;
        statsSacked = 0; 
    }
    
    @Override
    public int getHeismanScore() {
        return statsTD * 140 - statsInt * 250 + statsPassYards;
    }

    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("TD/Int: " + statsTD + "/" + statsInt + ">Comp Percent: " + (100*statsPassComp/(statsPassAtt+1))+"%");
        pStats.add("Pass Yards: " + statsPassYards + " yds>Yards/Att: " + ((double)(10*statsPassYards/(statsPassAtt+1))/10) + " yds");
        pStats.add("Yds/Game: " + (statsPassYards/games) + " yds/g>Sacks: " + statsSacked);
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Pass Strength: " + getLetterGrade(ratPassPow));
        pStats.add("Accuracy: " + getLetterGrade(ratPassAcc) + ">Evasion: " + getLetterGrade(ratPassEva));
        return pStats;
    }

    @Override
    public String getInfoForLineup() {
        if (injury != null) return  getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + ratPot + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + ratPot + " (" +
                getLetterGrade(ratPassPow) + ", " + getLetterGrade(ratPassAcc) + ", " + getLetterGrade(ratPassEva) + ")";
    }
    
}
