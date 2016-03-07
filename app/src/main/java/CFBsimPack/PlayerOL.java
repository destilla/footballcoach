package CFBsimPack;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Class for the OL player. 5 on field at a time.
 * @author Achi
 */
public class PlayerOL extends Player {
    
    //public String name;
    //Overall rating, combination of other ratings
    //public int ratOvr;
    //Potential, affects how much he gets better in offseason
    //public int ratPot;
    //FootIQ, affects how smart he plays
    //public int ratFootIQ;
    //OLPow affects how strong he is against defending DL
    public int ratOLPow;
    //OLBkR affects how well he blocks for running plays
    public int ratOLBkR;
    //OLBkP affects how well he blocks for passing plays
    public int ratOLBkP;
    
    //public Vector ratingsVector;
    
    public PlayerOL( String nm, Team t, int yr, int pot, int iq, int pow, int bkr, int bkp ) {
        team = t;
        name = nm;
        year = yr;
        ratOvr = (pow*3 + bkr + bkp)/5;
        ratPot = pot;
        ratFootIQ = iq;
        ratOLPow = pow;
        ratOLBkR = bkr;
        ratOLBkP = bkp;
        position = "OL";
        cost = (int)Math.pow(ratOvr/6,2) + (int)(Math.random()*100) - 50;
        
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratOLPow);
        ratingsVector.addElement(ratOLBkR);
        ratingsVector.addElement(ratOLBkP);
    }
    
    public PlayerOL( String nm, int yr, int stars ) {
        name = nm;
        year = yr;
        ratPot = (int) (50 + 50*Math.random());
        ratFootIQ = (int) (50 + stars*4 + 30*Math.random());
        ratOLPow = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratOLBkR = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratOLBkP = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratOvr = (ratOLPow*3 + ratOLBkR + ratOLBkP)/5;
        position = "OL";
        cost = (int)Math.pow(ratOvr/6,2) + (int)(Math.random()*100) - 50;
        
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratOLPow);
        ratingsVector.addElement(ratOLBkR);
        ratingsVector.addElement(ratOLBkP);
    }
    
    public Vector getRatingsVector() {
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratOLPow);
        ratingsVector.addElement(ratOLBkR);
        ratingsVector.addElement(ratOLBkP);
        return ratingsVector;
    }
    
    @Override
    public void advanceSeason() {
        year++;
        int oldOvr = ratOvr;
        ratFootIQ += (int)(Math.random()*(ratPot - 25))/10;
        ratOLPow += (int)(Math.random()*(ratPot - 25))/10;
        ratOLBkR += (int)(Math.random()*(ratPot - 25))/10;
        ratOLBkP += (int)(Math.random()*(ratPot - 25))/10;
        if ( Math.random()*100 < ratPot ) {
            //breakthrough
            ratOLPow += (int)(Math.random()*(ratPot - 30))/10;
            ratOLBkR += (int)(Math.random()*(ratPot - 30))/10;
            ratOLBkP += (int)(Math.random()*(ratPot - 30))/10;
        }
        ratOvr = (ratOLPow*3 + ratOLBkR + ratOLBkP)/5;
        ratImprovement = ratOvr - oldOvr;
    }

    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Potential: " + ratPot + ">Strength: " + getLetterGrade(ratOLPow));
        pStats.add("Run Block: " + getLetterGrade(ratOLBkR) + ">Pass Block: " + getLetterGrade(ratOLBkP));
        return pStats;
    }
}
