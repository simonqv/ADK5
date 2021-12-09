import java.util.Arrays;
import java.lang.StringBuilder.*;

public class Main {
    private static final Kattio io = new Kattio(System.in, System.out);

    /**
     * print output to console with desired format
     * @param output 2D int array with each line to print
     */
    private static void printer(int[][] output) {

        StringBuilder sb = new StringBuilder();
        int counter = 0;

        // For each actor, add actors number to sb, followed by how many roles and the roles
        for (int[] actor : output) {
            if (actor != null && actor.length != 0) {
                counter++;
                sb.append(actor[0]).append(" ").append(actor.length - 1);
                for (int i = 1; i < actor.length; i++) {
                    sb.append(" ").append(actor[i]);
                }
                sb.append("\n");
            }
        }

        if(sb.length() != 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        // Print number of actors and the actor's role assignments.
        System.out.println(counter);
        System.out.print(sb);
    }


    public static void main(String[] args){
        RoleAssignment role = new RoleAssignment(io);
        int[][] output = role.assign();
        printer(output);

    }
}
