import java.util.Arrays;

public class Main {
    private static final Kattio io = new Kattio(System.in, System.out);

    private static void printer(int[][] output) {
        System.out.println(output.length);
        for (int[] actor : output) {
            System.out.println(Arrays.toString(actor));
        }
    }

    public static void main(String[] args){
        RoleAssignment role = new RoleAssignment(io);
        int[][] output = role.assign();

        printer(output);

    }
}
