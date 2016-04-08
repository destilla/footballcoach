/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CFBsimPack;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Class for the Kicker player (also does punting).
 * @author Achi
 */
public class PlayerK extends Player {
    
    //public String name;
    //Overall rating, combination of other ratings
    //public int ratOvr;
    //Potential, affects how much he gets better in offseason
    //public int ratPot;
    //FootIQ, affects how smart he plays
    //public int ratFootIQ;
    //KickPow affects how far he can kick
    public int ratKickPow;
    //KickAcc affects how accurate his kicks are
    public int ratKickAcc;
    //KickFum affects how often he can fumble the snap
    public int ratKickFum;
    
    //Vector ratingsVector;
    
    //Stats
    public int statsXPAtt;
    public int statsXPMade;
    public int statsFGAtt;
    public int statsFGMade;
    
    public PlayerK( String nm, Team t, int yr, int pot, int iq, int pow, int acc, int fum, boolean rs, int dur ) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (pow + acc + 75)/3;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratKickPow = pow;
        ratKickAcc = acc;
        ratKickFum = fum;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        position = "K";

        cost = (int)(Math.pow((float)ratOvr - 55,2)/3.5) + 100 + (int)(Math.random()*100) - 50;
        
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratKickPow);
        ratingsVector.addElement(ratKickAcc);
        ratingsVector.addElement(ratKickFum);
        
        statsXPAtt = 0;
        statsXPMade = 0;
        statsFGAtt = 0;
        statsFGMade = 0;
    }
    
    public PlayerK( String nm, int yr, int stars, Team t ) {
        name = nm;
        year = yr;
        team = t;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = (int) (50 + 50*Math.random());
        ratFootIQ = (int) (50 + 50*Math.random());
        ratDur = (int) (50 + 50*Math.random());
        ratKickPow = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratKickAcc = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratKickFum = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratOvr = (ratKickPow + ratKickAcc + 75)/3;
        position = "K";

        cost = (int)(Math.pow((float)ratOvr - 55,2)/3.5) + 100 + (int)(Math.random()*100) - 50;
        
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratKickPow);
        ratingsVector.addElement(ratKickAcc);
        ratingsVector.addElement(ratKickFum);
        
        statsXPAtt = 0;
        statsXPMade = 0;
        statsFGAtt = 0;
        statsFGMade = 0;
    }
    
    public Vector getStatsVector() {
        Vector v = new Vector(7);
        v.add(statsXPMade);
        v.add(statsXPAtt);
        v.add((float)((int)(1000*(float)statsXPMade/statsXPAtt))/10);
        v.add(statsFGMade);
        v.add(statsFGAtt);
        v.add((float)((int)(1000*(float)statsFGMade/statsFGAtt))/10);
        return v;
    }
    
    public Vector getRatingsVector() {
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr + " (+" + ratImprovement + ")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratKickPow);
        ratingsVector.addElement(ratKickAcc);
        ratingsVector.addElement(ratKickFum);
        return ratingsVector;
    }
    
    @Override
    public void advanceSeason() {
        year++;
        int oldOvr = ratOvr;
        ratFootIQ += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratKickPow += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratKickAcc += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratKickFum += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        if ( Math.random()*100 < ratPot ) {
            //breakthrough
            ratKickPow += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratKickAcc += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratKickFum += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
        }
        ratOvr = (ratKickPow + ratKickAcc)/2;
        ratImprovement = ratOvr - oldOvr;
        //reset stats (keep career stats?)
        statsXPAtt = 0;
        statsXPMade = 0;
        statsFGAtt = 0;
        statsFGMade = 0;
    }

    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        if (statsXPAtt > 0) {
            pStats.add("XP Made/Att: " + statsXPMade + "/" + statsXPAtt + ">XP Percentage: " + (100 * statsXPMade / (statsXPAtt)) + "%");
        } else {
            pStats.add("XP Made/Att: 0/0>XP Percentage: 0%");
        }

        if (statsFGAtt > 0) {
            pStats.add("FG Made/Att: " + statsFGMade+"/"+statsFGAtt+">FG Percentage: " + (100*statsFGMade/statsFGAtt+"%"));
        } else {
            pStats.add("FG Made/Att: 0/0>FG Percentage: 0%");
        }
        pStats.add("Games Played: " + gamesPlayed + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Kick Strength: " + getLetterGrade(ratKickPow));
        pStats.add("Kick Accuracy: " + getLetterGrade(ratKickAcc) + ">Clumsiness: " + getLetterGrade(ratKickFum));
        return pStats;
    }

    @Override
    public String getInfoForLineup() {
        if (injury != null) return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " (" +
                getLetterGrade(ratKickPow) + ", " + getLetterGrade(ratKickAcc) + ", " + getLetterGrade(ratKickFum) + ")";
    }
}
