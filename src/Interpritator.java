import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Interpritator {
    private static Tree parseTree;
    private static PrintStream out;
    private static ArrayList<Tree> functionList = new ArrayList<>();
    public Interpritator(Tree pT) {
        parseTree = pT;
    }

    private static int interpretCH(HashMap<String, Integer> hm, Tree ch) throws NoSuchMethodException, RuntimeException {
//        String switespace = "";

        switch (ch.node) {
            case "functionDefinitionList": {
                functionList.addAll(ch.children);
                break;
            }
            case "identifier": {
                Integer r = hm.get(ch.children.get(0).node);
                if (r==null) {
                    throw new NullPointerException("PARAMETER NOT FOUND "+ch.children.get(0).node+":" + ch.nline);
                }
                return r;
            }
            case "CallExpression": {
                Tree fun = null;
                int numArgs = ch.children.size() - 1;
                String fName = ch.children.get(0).node;
                for (Tree tree : functionList) {
                    if (tree.node.equals(fName)) {
                        fun = tree;
                        if (numArgs == fun.children.get(0).children.size()) break;
                    }
                }
                if (fun==null) {
                    throw new NullPointerException("FUNCTION NOT FOUND " + fName+":"+ch.nline);
                }
                if (numArgs!=fun.children.get(0).children.size()) {
                    throw new NoSuchMethodException("ARGUMENT NUMBER MISMATCH " + fName + ":" + ch.nline);
                }
                HashMap<String, Integer> localVars = new HashMap<>();
                for (int i = 0; i<numArgs; i++) {
                    localVars.put(fun.children.get(0).children.get(i).node, interpretCH(hm, ch.children.get(i + 1)));
                }
                return (interpretCH(localVars, fun.children.get(1)));
            }
            case "const": {
                return (Integer.parseInt(ch.children.get(0).node));
            }
            case "PLUS":
            case "MINUS":
            case "MUL":
            case "DIV":
            case "MOD":
            case "MORE":
            case "LESS":
            case "EQ":{
                int ch1 = interpretCH(hm, ch.children.get(0));
                int ch2 = interpretCH(hm, ch.children.get(1));
                if (ch.node.equals("PLUS")) return ch1 + ch2;
                if (ch.node.equals("MUL")) return ch1 * ch2;
                if (ch.node.equals("MINUS")) return ch1 - ch2;
                if (ch.node.equals("DIV")) {
                    //todo адекватный toString
                    if (ch2 == 0) throw new RuntimeException("RUNTIME ERROR " + ch.node +  ":" + ch.nline);
                    else return ch1 / ch2;
                }
                if (ch.node.equals("MOD")) {
                    if (ch2 == 0)  throw new RuntimeException("RUNTIME ERROR " + ch.node +  ":" + ch.nline);
                    return ch1 % ch2;
                }
                if (ch.node.equals("MORE")) return (ch1 > ch2)?1:0;
                if (ch.node.equals("LESS")) return  (ch1 < ch2)?1:0;
                if (ch.node.equals("EQ")) {
                    return  ((ch1 == ch2)?1:0);
                }
            }
            case "if": {
                int cond = interpretCH(hm, ch.children.get(0));
                if (cond != 0) {
                    return interpretCH(hm, ch.children.get(1));
                } else {
                    return interpretCH(hm, ch.children.get(2));
                }
            }
            case "PROGRAM": {
                interpretCH(hm, ch.children.get(0));
                return interpretCH(hm, ch.children.get(1));
            }
        }
        return 0;
    }

    public static int interpret() throws NoSuchMethodException, RuntimeException {
        return interpretCH(new HashMap<>(), parseTree);
    }

}
