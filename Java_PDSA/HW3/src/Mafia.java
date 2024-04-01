
import java.util.Arrays; // Used to print the arrays
import edu.princeton.cs.algs4.ResizingArrayStack;

class member {
    int Level;
    int Range;
    int Index;

    member(int _level, int _range, int i) {
        Level = _level;
        Range = _range;
        Index = i;
    }
}

class Mafia {

    public int[] result(int[] levels, int[] ranges) {
        ResizingArrayStack<Integer> stack = new ResizingArrayStack<Integer>();
        int[] Level = levels;
        int[] Range = ranges;
        int[] answer;
        int n = levels.length;
        answer = new int[2 * n];
        stack.push(0);
        int nowrecording = 0;
        int[] statecount = new int[n];

        for (int i = 1; i < n; i++) {
            while (Level[i] > Level[stack.peek()]) {
                nowrecording = stack.pop();
                if (statecount[nowrecording] == 0) {
                    // System.out.println(nowrecording);
                    answer[2 * nowrecording + 1] = i - 1;// right
                    if (nowrecording + Range[nowrecording] < answer[2 * nowrecording + 1]) {
                        answer[2 * nowrecording + 1] = nowrecording + Range[nowrecording];
                    }
                    if (stack.isEmpty()) {
                        answer[2 * nowrecording] = 0;// left
                        if (nowrecording - Range[nowrecording] > answer[2 * nowrecording]) {
                            answer[2 * nowrecording] = nowrecording - Range[nowrecording];
                        }
                        break;
                    } else {
                        answer[2 * nowrecording] = stack.peek() + 1;// left
                        if (nowrecording - Range[nowrecording] > answer[2 * nowrecording]) {
                            answer[2 * nowrecording] = nowrecording - Range[nowrecording];
                        }
                    }
                }
                if (stack.isEmpty()) {
                    break;
                }
                // System.out.println(Arrays.toString(answer));
            }
            // if (Level[i] < Level[stack.peek()]) {
            // stack.push(i);
            // } else
            if (!stack.isEmpty()) {

                if (Level[i] == Level[stack.peek()]) {
                    nowrecording = stack.pop();
                    answer[2 * nowrecording + 1] = i - 1;// right
                    if (nowrecording + Range[nowrecording] < answer[2 * nowrecording + 1]) {
                        answer[2 * nowrecording + 1] = nowrecording + Range[nowrecording];
                    }
                    if (stack.isEmpty()) {
                        answer[2 * nowrecording] = 0;// left
                        if (nowrecording - Range[nowrecording] > answer[2 * nowrecording]) {
                            answer[2 * nowrecording] = nowrecording - Range[nowrecording];
                        }
                    } else {
                        answer[2 * nowrecording] = stack.peek() + 1;// left
                        if (nowrecording - Range[nowrecording] > answer[2 * nowrecording]) {
                            answer[2 * nowrecording] = nowrecording - Range[nowrecording];
                        }
                    }
                    statecount[nowrecording] = 1;
                    stack.push(nowrecording);
                }
            }
            stack.push(i);
            // if (i == n - 1) {
            // answer[2 * i + 1] = i;// right
            // if (stack.isEmpty()) {
            // answer[2 * i] = 0;// left
            // } else {
            // answer[2 * i] = stack.peek() + 1;// left
            // }
            // }
            // System.out.println(i+","+i);

        }
        while (!stack.isEmpty()) {
            // System.out.println(stack.peek());
            nowrecording = stack.pop();
            if (statecount[nowrecording] == 0) {
                answer[2 * nowrecording + 1] = n - 1;// right
                if (nowrecording + Range[nowrecording] < answer[2 * nowrecording + 1]) {
                    answer[2 * nowrecording + 1] = nowrecording + Range[nowrecording];
                }
                if (stack.isEmpty()) {
                    answer[2 * nowrecording] = 0;// left
                    if (nowrecording - Range[nowrecording] > answer[2 * nowrecording]) {
                        answer[2 * nowrecording] = nowrecording - Range[nowrecording];
                    }
                } else {
                    answer[2 * nowrecording] = stack.peek() + 1;// left
                    if (nowrecording - Range[nowrecording] > answer[2 * nowrecording]) {
                        answer[2 * nowrecording] = nowrecording - Range[nowrecording];
                    }
                }
            }
        }
        // Given the traits of each member and output
        // the leftmost and rightmost index of member
        // can be attacked by each member.

        return answer;
        // complete the code by returning an int[]
        // flatten the results since we only need an 1-dimentional array.
    }

    public static void main(String[] args) {
        Mafia sol = new Mafia();
        System.out.println(Arrays.toString(
                sol.result(new int[] { 11, 13, 11, 7, 15 },
                        new int[] { 1, 8, 1, 7, 2 })));
        // Output: [0, 0, 0, 3, 2, 3, 3, 3, 2, 4]
        // => [a0, b0, a1, b1, a2, b2, a3, b3, a4, b4]
    }
}