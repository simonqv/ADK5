import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Role {
    int role;
    int[] possibleActors;
    //ArrayList<Role> clashes;
    boolean[] clashes;
    boolean assignedToDiva;
    boolean assigned;
    int assignedTo;


    public Role(int role, int[] actors, int n) {
        this.role = role;
        this.possibleActors = actors;
        //this.clashes = new ArrayList<>();
        this.clashes = new boolean[n];
        Arrays.fill(clashes, Boolean.FALSE);
        this.assignedToDiva = false;
        this.assigned = false;
        this.assignedTo = 0;
    }

    public void addClash(Role r) {
        clashes[r.role - 1] = true;
    }

    public boolean checkClash(Role r) {
        return clashes[r.role - 1];
    }

    public void setDiva() {
        assignedToDiva = true;
        assigned = true;
    }

    public boolean canPlay(int actor) {
        if (!isAssigned()) {
            for (int x : possibleActors) {
                if (x == actor) return true;
            }
        }
        return false;
    }

    public boolean isAssigned() {
        return assigned || assignedToDiva;
    }

    public void setAssigned(){
        assigned = true;
    }

    public boolean isAssignedToDiva() {
        return assignedToDiva;
    }
}
