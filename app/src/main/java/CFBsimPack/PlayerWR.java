/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CFBsimPack;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Class for the WR player.
 * @author Achi
 */
public class PlayerWR extends Player {
    
    //public String name;
    //Overall rating, combination of other ratings
    //public int ratOvr;
    //Potential, affects how much he gets better in offseason
    //public int ratPot;
    //FootIQ, affects how smart he plays
    //public int ratFootIQ;
    //RecCat affects how good he is at catching
    public int ratRecCat;
    //RecSpd affects how long his passes are
    public int ratRecSpd;
    //RecEva affects how easily he can dodge tackles
    public int ratRecEva;
    
    //public Vector ratingsVector;
    
    //Stats
    public int statsTargets;
    public int statsReceptions;
    public int statsRecYards;
    public int statsTD;
    public int statsDrops;
    public int statsFumbles;
    
    public PlayerWR( String nm, Team t, int yr, int pot, int iq, int cat, int spd, int eva, boolean rs, int dur ) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (cat*2 + spd + eva)/4;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratRecCat = cat;
        ratRecSpd = spd;
        ratRecEva = eva;
        isRedshirt = rs;
        if (isRedshirt) year = 0;

        cost = (int)(Math.pow((float)ratOvr - 55,2)/3.5) + 80 + (int)(Math.random()*100) - 50;
        
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratRecCat);
        ratingsVector.addElement(ratRecSpd);
        ratingsVector.addElement(ratRecEva);
        
        statsTargets = 0;
        statsReceptions = 0;
        statsRecYards = 0;
        statsTD = 0;
        statsDrops = 0;
        statsFumbles = 0;

        position = "WR";
    }
    
    public PlayerWR( String nm, int yr, int stars, Team t ) {
        name = nm;
        year = yr;
        team = t;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = (int) (50 + 50*Math.random());
        ratFootIQ = (int) (50 + 50*Math.random());
        ratDur = (int) (50 + 50*Math.random());
        ratRecCat = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratRecSpd = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratRecEva = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratOvr = (ratRecCat*2 + ratRecSpd + ratRecEva)/4;

        cost = (int)(Math.pow((float)ratOvr - 55,2)/3.5) + 80 + (int)(Math.random()*100) - 50;
        
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratRecCat);
        ratingsVector.addElement(ratRecSpd);
        ratingsVector.addElement(ratRecEva);
        
        statsTargets = 0;
        statsReceptions = 0;
        statsRecYards = 0;
        statsTD = 0;
        statsDrops = 0;
        statsFumbles = 0;

        position = "WR";
    }
    
    public Vector getStatsVector() {
        Vector v = new Vector(7);
        v.add(statsReceptions);
        v.add(statsTargets);
        v.add(statsRecYards);
        v.add(statsTD);
        v.add(statsFumbles);
        v.add(statsDrops);
        v.add((float)((int)((float)statsRecYards/statsReceptions*100))/100);
        return v;
    }
    
    public Vector getRatingsVector() {
        ratingsVector = new Vector();
        ratingsVector.addElement(name + " (" + getYrStr() + ")");
        ratingsVector.addElement(ratOvr + " (+" + ratImprovement + ")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratRecCat);
        ratingsVector.addElement(ratRecSpd);
        ratingsVector.addElement(ratRecEva);
        return ratingsVector;
    }
    
    @Override
    public void advanceSeason() {
        year++;
        int oldOvr = ratOvr;
        ratFootIQ += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratRecCat += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratRecSpd += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratRecEva += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        if ( Math.random()*100 < ratPot ) {
            //breakthrough
            ratRecCat += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratRecSpd += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratRecEva += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
        }
        ratOvr = (ratRecCat*2 + ratRecSpd + ratRecEva)/4;
        ratImprovement = ratOvr - oldOvr;
        //reset stats (keep career stats?)
        statsTargets = 0;
        statsReceptions = 0;
        statsRecYards = 0;
        statsTD = 0;
        statsDrops = 0;
        statsFumbles = 0;
    }
    
    @Override
    public int getHeismanScore() {
        return statsTD * 150 - statsFumbles * 100 - statsDrops * 50 + statsRecYards * 2;
    }

    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("TDs/Fumbles: " + statsTD + "/" + statsFumbles + ">Catch Percent: " + (100*statsReceptions/(statsTargets+1))+"%");
        pStats.add("Rec Yards: " + statsRecYards + " yds" + ">Yards/Tgt: " + ((double)(10*statsRecYards/(statsTargets+1))/10) + " yds");
        pStats.add("Yds/Game: " + (statsRecYards/getGamesPlayed()) + " yds/g>Drops: " + statsDrops);
        pStats.add("Games Played: " + gamesPlayed + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Catching: " + getLetterGrade(ratRecCat));
        pStats.add("Rec Speed: " + getLetterGrade(ratRecSpd) + ">Evasion: " + getLetterGrade(ratRecEva));
        return pStats;
    }

    @Override
    public String getInfoForLineup() {
        if (injury != null) return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " (" +
                getLetterGrade(ratRecCat) + ", " + getLetterGrade(ratRecSpd) + ", " + getLetterGrade(ratRecEva) + ")";
    }
}
