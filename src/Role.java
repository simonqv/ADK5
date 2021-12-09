import java.util.Arrays;

public class Role {
    int role;               // Name of role
    int[] possibleActors;   // Actors that can play this role
    boolean[] clashes;      // True if clash between roles in at least one scene
    boolean assignedToDiva; // True if assigned to diva
    boolean assigned;       // True if assigned


    public Role(int role, int[] actors, int n) {
        this.role = role;
        this.possibleActors = actors;
        this.clashes = new boolean[n];
        Arrays.fill(clashes, Boolean.FALSE); // Initialize all to False

        // No role is assigned to anyone in the beginning
        this.assignedToDiva = false;
        this.assigned = false;
    }

    public void addClash(Role r) {
        clashes[r.role - 1] = true;
    }

    public void setDiva() {
        assignedToDiva = true;
        assigned = true;
    }

    /**
     * Actor can play Role, if role is not assigned and Actor part of possibleActors array
     * @param actor number of actor
     * @return True if possible
     */
    public boolean canPlay(int actor) {
        if (isNotAssigned()) {
            for (int x : possibleActors) {
                if (x == actor) return true;
            }
        }
        return false;
    }

    public boolean isNotAssigned() {
        return !assigned && !assignedToDiva;
    }

    public void setAssigned(){
        assigned = true;
    }

}
