import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tree {
    String node;
    int nline;
    List<Tree> children = new ArrayList<>();


//    public String toString() {
//        switch (this.node) {
//            case "PLUS":
//            case "MINUS":
//            case "MUL":
//            case "DIV":
//            case "MOD":
//            case "MORE":
//            case "LESS":
//            case "EQ":{
//                String ch1 = this.children.get(0).toString();
//                String ch2 = this.children.get(1).toString();
//            }
//        }
//    }

    public Tree(String node, int nline, Tree... children) {
        this.node = node;
        this.nline = nline;
        this.children = Arrays.asList(children);
    }

    public Tree(String node, int nline, ArrayList<Tree> children) {
        this.node = node;
        this.nline = nline;
        this.children = children;
    }

    public Tree(String node, int nline) {
        this.nline = nline;
        this.node = node;
    }

//    public Tree(String node, ArrayList<Tree> ch) {
//
//    }
}
