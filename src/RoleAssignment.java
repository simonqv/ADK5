import java.util.ArrayList;

public class RoleAssignment {
    private final int n;        // number of roles
    private final int s;        // number of scenes
    private final int k;        // number of actors
    private ArrayList<Role> roles;      // list of all roles
    private int[][] scenes;     // roles in scenes
    private final ArrayList<Integer> divaOneRoles = new ArrayList<>();
    private final ArrayList<Integer> divaTwoRoles = new ArrayList<>();
    private int superActor;

    public RoleAssignment(Kattio io) {
        n = io.getInt();        // number of roles
        s = io.getInt();        // number of scenes
        k = io.getInt();        // number of actors
        superActor = k + 1;     // superactor start value

        // read rest of input
        read(io);
    }

    /**
     * Read everything but the three first lines
     * @param io io stuff
     */
    private void read(Kattio io) {
        // initialize list of all roles
        roles = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int rowLen = io.getInt();
            int[] restOfRow = new int[rowLen];

            for (int j = 0; j < rowLen; j++) {
                int actor = io.getInt();
                restOfRow[j] = actor;

                // If actor number is 1 or 2 it's a diva
                if (actor == 1) {
                    divaOneRoles.add(i + 1);
                } else if (actor == 2) {
                    divaTwoRoles.add(i + 1);
                }
            }

            // Create a role and add to list
            Role role = new Role(i + 1, restOfRow, n);
            roles.add(role);
        }

        // Create int[][] of all scenes
        readScenes(io, roles);
    }

    /**
     * Reads the rows of scenens and creates 2D array
     * @param io io stuff
     * @param roles list of all roles to save clashes between roles in scenes
     */
    private void readScenes(Kattio io, ArrayList<Role> roles) {
        scenes = new int[s][];

        // to read each scene
        for (int i = 0; i < s; i++) {
            int rowLen = io.getInt();
            int[] restOfRow = new int[rowLen];

            // read the rest of the row
            for (int j = 0; j < rowLen; j++) {
                int y = io.getInt();
                restOfRow[j] = y;
            }
            scenes[i] = restOfRow;

            // for each role in the scene, add clash between roles
            for (int role : restOfRow) {
                for (int roleInner : restOfRow) {
                    roles.get(role - 1).addClash(roles.get(roleInner - 1));
                }
            }
        }
    }

    /**
     * Checks if roles appears in the same scene
     * @param divaOne role for diva one
     * @param divaTwo role for diva two
     * @return true if they do not appear in the same scene
     */
    private boolean divaSceneCheck(int divaOne, int divaTwo) {
        for (int[] scene : scenes) {
            boolean divaOneCheck = false;
            boolean divaTwoCheck = false;
            for (int role : scene) {
                if (divaOne == role) {
                    divaOneCheck = true;
                }
                if (divaTwo == role) {
                    divaTwoCheck = true;
                }
                if (divaOneCheck && divaTwoCheck) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Finds possible assignemnt of diva one and two, so that they don't play against each other, always picks first possible match
     * @return array of length 2, where fist element is diva one's role, and second element is diva two's role
     */
    private int[] assignDivas() {
        for (Integer one : divaOneRoles) {
            for (Integer two : divaTwoRoles) {
                boolean check = divaSceneCheck(one, two);
                if (check) {
                    return new int[]{one, two};
                }
            }
        }
        return new int[]{-1};
    }

    /**
     * Assign roles to actors
     * @return 2D array where each inner array is contains actor followed by roles they play
     */
    public int[][] assign() {
        // assign divas roles.
        int[] divaRoles = assignDivas();
        if (divaRoles[0] == -1) {
            System.exit(-1);
        }
        int divaOneRole = divaRoles[0];
        int divaTwoRole = divaRoles[1];

        roles.get(divaOneRole - 1).setDiva();
        roles.get(divaTwoRole - 1).setDiva();

        // format [[actorNum, role, role, ...], ...]
        int[][] output = new int[k + n][];

        // place divas in output
        output[0] = new int[]{1, divaOneRole};
        output[1] = new int[]{2, divaTwoRole};

        // list of roles for one actor
        ArrayList<Integer> rolesToActor;

        // start loop at 3, cuase diva 1 and 2 are done
        for (int actor = 3; actor < k + 1; actor++) {

            rolesToActor = assignRolesToActor(actor);

            // translate list to array and add to output
            output[actor - 1] = rolesToActor.stream().mapToInt(i->i).toArray();
        }

        // if roles are unassigned, assign superactor to them
        for (Role role : roles) {
            int[] superRole = new int[2];
            if (role.isNotAssigned()) {
                superRole[0] = superActor;
                superRole[1] = role.role;
                output[superActor] = superRole;
                superActor++;
            }
        }
        return output;
    }


    /**
     * Checks which roles an actor can play and assigns those roles to the actor
     * @param actor the actor to find roles for
     * @return a list of roles with actor number first
     */
    private ArrayList<Integer> assignRolesToActor(int actor) {
        ArrayList<Integer> rolesToActor = new ArrayList<>();
        rolesToActor.add(actor);

        // Checks every role, if true add the role and set role as assigned
        for (Role role : roles) {
            if (role.canPlay(actor)) {
                rolesToActor.add(role.role);
                role.setAssigned();

                // for each element in clashes, check if there is a clash and if actor can play the non-clash roles
                for (int i = 0; i < role.clashes.length; i++) {
                    if (!role.clashes[i] && roles.get(i).canPlay(actor)) {

                        // Update clashlist
                        for (int j = 0; j < role.clashes.length; j++) {
                            if (roles.get(i).clashes[j]) {
                                role.clashes[j] = roles.get(i).clashes[j];
                            }
                        }

                        rolesToActor.add(roles.get(i).role);
                        roles.get(i).setAssigned();
                    }
                }
                return rolesToActor;
            }
        }
        return new ArrayList<>();
    }
}
