import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The class {@code LeftRecursiveToRightRecursive} has only one static method: {@code parse(String)},
 * which convert left recursive to right recursive of rules {@code String}. Each rule is separated by a newline.
 * Each terminal, non-terminal, arrow, and union operation is separated by a space. <p>
 * 
 * The following statement is a sample of valid rule set:
 * 
 * <blockquote><pre>
 *  String rules = """
 *  exp -> exp addop term | term
 *  addop -> + | -
 *  term -> term mulop factor | factor
 *  mulop -> *
 *  factor -> ( exp ) | number
 *  """;
 *  </pre></blockquote><p>
 * 
 * Result of {@code parse(String)} is a string, which can be viewed by standard output.
 * 
 * @author 6310401033 Thinnarat J.
 * @author 6310401041 Thiti T.
 * @author 6310403974 Natdanai T.
 * 
 * @see LeftRecursiveToRightRecursive#parse(String)
 */


public class LeftRecursiveToRightRecursive {
    public static String parse(String rules) {
        RuleList ruleList = new RuleList();
        RuleList newRuleList = new RuleList();

        String[] splittedRule = rules.strip().split("\n");
        int ruleCount = splittedRule.length;

        for (int i = 0 ; i < ruleCount ; ++i) {
            String rule = splittedRule[i].strip();
            ruleList.add(new Rule(rule));
        }

        for (Rule rule : ruleList) {
            Rule newRule = new Rule(rule.getRuleName() + Rule.ARROW);
            Rule newLeftFactorRule = null;

            String[] tokens = rule.getTokens();
            for (String token : tokens) {
                String[] word = token.split(" ");
                if (word[0].equals(rule.getRuleName())) {
                    if (newLeftFactorRule == null) {
                        newLeftFactorRule = new Rule(rule.getRuleName() + "E" + Rule.ARROW + "<epsilon>");
                    }
                    newLeftFactorRule.addRuleToken(token.replace(rule.getRuleName() + " ", "") + " " + newLeftFactorRule.getRuleName());
                } else {
                    newRule.addRuleToken(token);
                }
            }

            if (newLeftFactorRule != null) {
                Rule modifiedNewRule = new Rule(rule.getRuleName() + Rule.ARROW);
                for (String token : newRule.getTokens()) {
                    modifiedNewRule.addRuleToken(token + " " + newLeftFactorRule.getRuleName());
                }

                newRule = modifiedNewRule;
            }

            newRuleList.add(newRule);

            if (newLeftFactorRule != null) {
                newRuleList.add(newLeftFactorRule);
            }
        }

        return newRuleList.toString();
    }

}

class RuleList implements Iterable<Rule> {
    private List<Rule> rules;

    public RuleList() {
        rules = new ArrayList<>();
    }

    public boolean add(Rule rule) {
        if (this.containsRule(rule.getRuleName())) {
            throw new IllegalArgumentException("Rule \"" + rule.getRuleName() + "\" has already existed in this list.");
        }
        return rules.add(rule);
    }

    public boolean remove(Rule rule) {
        return rules.remove(rule);
    }

    private boolean containsRule(String ruleName) {
        for (Rule rule : rules) if (rule.getRuleName().equals(ruleName)) return true;
        return false;
    }

    @Override
    public Iterator<Rule> iterator() {
        return rules.iterator();
    }

    @Override
    public String toString() {
        String string = "";
        for (Rule rule : rules) {
            string += rule.toString() + "\n";
        }
        return string;
    }

}

class Rule {
    public static final String UNION = " | ";
    public static final String ARROW = " -> ";

    private String rule;
    private String ruleName;
    private String[] tokens;

    public Rule(String rule) {
        this.rule = rule;
        calculateTokens();
    }

    public String getRuleName() {
        return ruleName;
    }

    public String[] getTokens() {
        return tokens;
    }

    public void addRuleToken(String token) {
        if (this.tokens.length != 0) {
            this.rule += UNION;
        }

        this.rule += token;
        calculateTokens();
    }

    public void removeRuleToken(String token) {
        String[] tokens = rule.split(" \\| ");
        for (int i = 0 ; i < tokens.length ; ++i) {
            if (tokens[i].equals(token)) {
                tokens[i] = "";
            }
        }
        String newRule = this.ruleName + ARROW + String.join(UNION, tokens);
        this.rule = newRule;
        calculateTokens();
    }

    @Override
    public String toString() {
        return rule;
    }

    private void calculateTokens() {
        String[] tokens = this.rule.split(ARROW);
        this.ruleName = tokens[0];

        if (tokens.length == 2) {
            this.tokens = tokens[1].split(" \\| ");
        } else {
            this.tokens = new String[0];
        }
    }

}

class Main {
    public static void main(String[] args) {
        String rules = """
        exp -> exp addop term | term
        addop -> + | -
        term -> term mulop factor | factor
        mulop -> *
        factor -> ( exp ) | number
        """;

        String newRules = LeftRecursiveToRightRecursive.parse(rules);
        
        System.out.println(rules);
        System.out.println(newRules);
    }
}
