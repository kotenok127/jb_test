import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

public class Main {
    public static void main(String[] args) throws IOException {
        InputStream in = new FileInputStream("input.txt");
        LexicalAnalyzer la = null;
        try {
            la = new LexicalAnalyzer(in);
        } catch (ParseException e) {
            System.err.println (e.getMessage());
            return;
        }
        try {
            while (la.curToken != Token.END) {
               // System.out.println(la.curToken);
                la.nextToken();
            }
        } catch (ParseException e) {
			System.err.println("SYNTAX ERROR");
           // System.err.println(e.getMessage() + e.getErrorOffset());
            // System.err.println(la.analyzed);
            // for (int i = 0; i< la.analyzed.length() - 1; i++) {
                // System.err.print('~');
            // }
            // System.err.println('^');
            return;
        }
       // System.out.println();
        in = new FileInputStream("input.txt");
        Tree parseTree;
        LexicalAnalyzer la2;
        Parser p;
        try {
            la2 = new LexicalAnalyzer(in);
            p = new Parser(la2);

        } catch (ParseException e) {
            System.err.print(e.getMessage());
            return;
        }
        try {
            parseTree = p.parse();

        } catch (ParseException e) {
			System.err.println("SYNTAX ERROR");
            // System.err.println (e.getMessage() + (e.getErrorOffset() - 1));
            // System.err.println(la2.analyzed.substring(0, la2.analyzed.length() - 1));
            // for (int i = 0; i< la2.analyzed.length() - 2; i++) {
                // System.err.print('~');
            // }
            // System.err.println('^');
            return;
        }
        Visualisator vs = new Visualisator(parseTree);
        vs.printTree();
        try {
            Interpritator it = new Interpritator(parseTree);
            System.out.println(it.interpret());
        } catch (NoSuchMethodException | RuntimeException e) {
            System.err.println (e.getMessage());
        }


        return;
    }
}
