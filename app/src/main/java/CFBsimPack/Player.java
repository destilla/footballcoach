package CFBsimPack;

import java.util.ArrayList;
import java.util.Vector;
import java.io.Serializable;

/**
 * Base player class that others extend. Has name, overall, potential, and football IQ.
 * @author Achi
 */
public class Player {
    
    public Team team;
    public String name;
    public String position;
    public int year;
    public int ratOvr;
    public int ratPot;
    public int ratFootIQ;
    public int ratDur;
    public int ratImprovement;
    public int cost;
    public int gamesPlayed;

    public boolean isRedshirt;

    public boolean isInjured;
    public Injury injury;

    protected final String[] letterGrades = {"F", "F+", "D", "D+", "C", "C+", "B", "B+", "A", "A+"};
    
    public Vector ratingsVector;
    
    public String getYrStr() {
        if (year == 0) {
            return "RS";
        } else if ( year == 1 ) {
            return "Fr";
        } else if ( year == 2 ) {
            return "So";
        } else if ( year == 3 ) {
            return "Jr";
        } else if ( year == 4 ) {
            return "Sr";
        }
        return "ERROR";
    }
    
    public void advanceSeason() {
        //add stuff
        year++;
    }
    
    public int getHeismanScore() {
        return 0;
    }

    public String getInitialName() {
        String[] names = name.split(" ");
        return names[0].substring(0,1) + ". " + names[1];
    }

    public String getPosNameYrOvrPot_Str() {
        if (injury != null) {
            return position + " " + name + " [" + getYrStr() + "]>" + injury.toString();
        }
        return position + " " + name + " [" + getYrStr() + "]>" + ratOvr + " / " + getLetterGrade(ratPot) + " / " + getLetterGrade(ratDur);
    }

    public String getPosNameYrOvrPot_OneLine() {
        if (injury != null) {
            return position + " " + name + " [" + getYrStr() + "] " + injury.toString();
        }
        return position + " " + getInitialName() + " [" + getYrStr() + "] " + ratOvr + " / " + getLetterGrade(ratPot) + " / " + getLetterGrade(ratDur);
    }

    /**
     * Convert a rating into a letter grade. 90 -> A, 80 -> B, etc
     */
    protected String getLetterGrade(String num) {
        int ind = (Integer.parseInt(num) - 50)/5;
        if (ind > 9) ind = 9;
        if (ind < 0) ind = 0;
        return letterGrades[ind];
    }

    /**
     * Convert a rating into a letter grade for potential, so 50 is a C instead of F
     */
    protected String getLetterGradePot(String num) {
        int ind = (Integer.parseInt(num))/10;
        if (ind > 9) ind = 9;
        if (ind < 0) ind = 0;
        return letterGrades[ind];
    }

    /**
     * Convert a rating into a letter grade. 90 -> A, 80 -> B, etc
     */
    protected String getLetterGrade(int num) {
        int ind = (num-50)/5;
        if (ind > 9) ind = 9;
        if (ind < 0) ind = 0;
        return letterGrades[ind];
    }

    /**
     * Convert a rating into a letter grade for potential, so 50 is a C instead of F
     */
    protected String getLetterGradePot(int num) {
        int ind = num/10;
        if (ind > 9) ind = 9;
        if (ind < 0) ind = 0;
        return letterGrades[ind];
    }

    public ArrayList<String> getDetailStatsList(int games) {
        return null;
    }

    public String getInfoForLineup() {
        return null;
    }
    
}
