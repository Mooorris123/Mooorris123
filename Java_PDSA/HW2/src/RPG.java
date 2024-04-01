
class RPG {
    private int[] def;
    private int[] att;
    private int[] max;
    // private int[] damage;
    // private int[] boostdamage;
    // private int[] state ;//0 = boost | 1 = attack

    public RPG(int[] defence, int[] attack) {
        // Initialize some variables
        def = defence;
        att = attack;

    }

    public int maxDamage(int n) {
        // return the highest total damage after n rounds.
        max = new int[att.length];
        // state = new int [n];
        // damage = new int [n] ;
        // boostdamage = new int [n] ;
        max[0] = att[0] - def[0];

        if (max[0] + att[1] - def[1] > 2 * att[1] - def[1]) {
            max[1] = max[0] + att[1] - def[1];

        } else if (max[0] + att[1] - def[1] <= 2 * att[1] - def[1]) {
            max[1] = 2 * att[1] - def[1];
        }
        for (int i = 3; i <= n; i++) {
            int current = i - 1;
            int front = i - 2;
            int frontfront = i - 3;
            if (max[front] + att[current] - def[current] > max[frontfront] + 2 * att[current] - def[current]) {
                max[current] = max[front] + att[current] - def[current];

            } else if (max[front] + att[current] - def[current] <= max[frontfront] + 2 * att[current] - def[current]) {
                max[current] = max[frontfront] + 2 * att[current] - def[current];
            }
        }
        return max[n - 1];
    }

    public static void main(String[] args) {
        RPG sol = new RPG(new int[] { 5, 4, 1, 7, 98, 2 }, new int[] { 200, 200, 200, 200, 200, 200 });
        System.out.println(sol.maxDamage(5));
        // 1: boost, 2: attack, 3: boost, 4: attack, 5: boost, 6: attack
        // maxDamage: 1187
    }
}