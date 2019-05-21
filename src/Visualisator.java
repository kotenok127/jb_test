import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Visualisator {
    private static Tree parseTree;
    private static PrintStream out;

    public Visualisator(Tree pT) {
        this.parseTree = pT;
    }


    static void printCh(Tree ch, int cntws) {
        String switespace = "";
        for (int i = 0; i < cntws; i++) switespace += ' ';
        out.print(switespace);
        out.println(ch.node);
        for (int i = 0; i < ch.children.size(); i++) {
            out.println(switespace + " ->");
            printCh(ch.children.get(i), cntws + 5);
        }
    }

    public static void printTree() {
        try {
            out = new PrintStream(new File("parseTree.txt"));
            printCh(parseTree, 0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
