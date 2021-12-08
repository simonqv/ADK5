import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class RoleAssignment {
    private final int n;        // number of roles
    private final int s;        // number of scenes
    private final int k;        // number of actors
    private ArrayList<Role> roles;      // possible actors for each role
    private int[][] scenes;     // roles in scenes
    private final ArrayList<Integer> divaOneRoles = new ArrayList<>();
    private final ArrayList<Integer> divaTwoRoles = new ArrayList<>();
    private int superActor;

    private int[][] assignedRoles;

    public RoleAssignment(Kattio io) {
        n = io.getInt();
        s = io.getInt();
        k = io.getInt();
        superActor = k + 1;
        //roles  = readRow(n, io, true);


        read(n, s, io);
        //Role role = new Role(readRow(n, io, true), readRow(s, io, false));

    }

    private void read(int n, int s, Kattio io) {
        roles = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x = io.getInt();
            int[] restOfRow = new int[x];
            for (int j = 0; j < x; j++) {
                int y = io.getInt();
                restOfRow[j] = y;
                if (y == 1) {
                    divaOneRoles.add(i + 1);
                } else if (y == 2) {
                    divaTwoRoles.add(i + 1);
                }
            }
            Role role = new Role(i + 1, restOfRow, n);
            roles.add(role);
        }
        scenes = readRow(s, io, roles);
    }


    private int[][] readRow(int a, Kattio io, ArrayList<Role> roles) {
        int[][] res = new int[a][];
        for (int i = 0; i < a; i++) {
            int x = io.getInt();
            int[] restOfRow = new int[x];
            for (int j = 0; j < x; j++) {
                int y = io.getInt();
                restOfRow[j] = y;
            }
            res[i] = restOfRow;

            for (int role : restOfRow) {
                for (int roleInner : restOfRow) {
                    //if (role != roleInner) {
                    roles.get(role - 1).addClash(roles.get(roleInner - 1));
                    //}
                }
            }
        }
        return res;
    }

    private boolean assignDivasHelper(int divaOne, int divaTwo) {
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


    private int[] assignDivas() {
        for (Integer one : divaOneRoles) {
            for (Integer two : divaTwoRoles) {
                boolean check = assignDivasHelper(one, two);
                if (check) {
                    return new int[]{one, two};
                }
            }
        }
        return new int[]{-1};
    }

    public int[][] assign() {
        // assign divas roles.
        int[] divaRoles = assignDivas();
        if (divaRoles[0] == -1) {
            System.exit(-1);
        }
        int divaOneRole = divaRoles[0];
        int divaTwoRole = divaRoles[1];

        roles.get(divaOneRole).setDiva();
        roles.get(divaTwoRole).setDiva();

        // format [[name, role],]
        int[][] output = new int[k + n][];

        output[0] = new int[]{1, divaOneRole};
        output[1] = new int[]{2, divaTwoRole};

        ArrayList<Integer> rolesToActor;
        for (int actor = 3; actor < k + 1; actor++) {
            rolesToActor = helper(actor);
            int[] partOfOutput = rolesToActor.stream().mapToInt(i->i).toArray();
            output[actor - 1] = partOfOutput;
        }
        return output;
    }



    private ArrayList<Integer> helper(int actor) {
        ArrayList<Integer> rolesToActor = new ArrayList<>();
        rolesToActor.add(actor);
        for (Role role : roles) {
            System.out.println("acotor " + actor + " " + );
            if (role.canPlay(actor)) {
                rolesToActor.add(role.role);
                role.setAssigned();
                for (int i = 0; i < role.clashes.length; i++) {
                    if (!role.clashes[i]) {
                        rolesToActor.add(roles.get(i).role);
                        roles.get(i).setAssigned();
                    }
                }
                return rolesToActor;
            }
        }

        for (Role role : roles) {
            if (!role.isAssigned()) {
                rolesToActor.set(0, superActor);
                rolesToActor.add(role.role);
                superActor++;
                return rolesToActor;
            }
        }

        return new ArrayList<Integer>();
    }
}
