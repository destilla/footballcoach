package CFBsimPack;

/**
 * Injury class.
 * Each injured player has one.
 * Describes what the injury is, and how long it lasts.
 * Created by Achi Jones on 4/3/2016.
 */
public class Injury {

    private int duration; // Duration of the injury (in games)
    private String description; // What the injury is
    private Player player; // Player that has this injury

    public Injury(int dur, String descrip, Player p) {
        duration = dur;
        description = descrip;
        player = p;
        player.isInjured = true;
    }

    public Injury(Player p) {
        // Generate an injury
        duration = (int)(Math.random() * 15);
        description = "Injury!";
        player = p;
        player.isInjured = true;
    }

    public int getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public void advanceGame() {
        duration--;
        if (duration <= 0) {
            // Done with injury
            player.isInjured = false;
            player.injury = null;
        }
    }

    public String toString() {
        return description + " (" + duration + " gms)";
    }
}
