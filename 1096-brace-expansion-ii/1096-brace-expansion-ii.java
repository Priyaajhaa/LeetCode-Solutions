import java.util.*;

class Solution {
    public List<String> braceExpansionII(String expression) {
        return new ArrayList<>(parse(expression, 0).set);
    }

    static class Result {
        Set<String> set;
        int idx;

        Result(Set<String> set, int idx) {
            this.set = set;
            this.idx = idx;
        }
    }

    private Result parse(String s, int i) {
        Set<String> result = new TreeSet<>();
        Set<String> current = new TreeSet<>();
        current.add("");

        while (i < s.length() && s.charAt(i) != '}') {
            char c = s.charAt(i);

            if (c == '{') {
                Result r = parse(s, i + 1);
                current = multiply(current, r.set);
                i = r.idx + 1;
            } 
            else if (c == ',') {
                result.addAll(current);
                current = new TreeSet<>();
                current.add("");
                i++;
            } 
            else {
                Set<String> word = new TreeSet<>();
                word.add(String.valueOf(c));
                current = multiply(current, word);
                i++;
            }
        }

        result.addAll(current);
        return new Result(result, i);
    }

    private Set<String> multiply(Set<String> a, Set<String> b) {
        Set<String> res = new TreeSet<>();

        for (String x : a) {
            for (String y : b) {
                res.add(x + y);
            }
        }

        return res;
    }
}
