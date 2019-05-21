import java.text.ParseException;
import java.util.ArrayList;

public class Parser {
    private static LexicalAnalyzer lex;

    public Parser(LexicalAnalyzer la) throws ParseException {
        lex = la;
    }

    static Tree binaryExpression() throws ParseException {
        Tree e1 = expression();
        int n = lex.cntLines();
        String op = "";
        switch (lex.curToken()) {
            case PLUS: {
                op = "PLUS";
                break;
            }
            case MINUS: {
                op = "MINUS";
                break;
            }
            case MUL: {
                op = "MUL";
                break;
            }
            case DIV: {
                op = "DIV";
                break;
            }
            case MOD: {
                op = "MOD";
                break;
            }
            case MORE: {
                op = "MORE";
                break;
            }
            case LESS: {
                op = "LESS";
                break;
            }
            case EQ: {
                op = "EQ";
                break;
            }
            default: {

                throw new ParseException(" expected binary operand" +
                        " at position ", lex.curPos());
            }
        }
        lex.nextToken();
        Tree e2 = expression();
        return new Tree(op, n, e1, e2);
    }

    static Tree expression() throws ParseException {
        int n = lex.cntLines();
        switch (lex.curToken()) {
            case IDENTIFIER: {
                String cn = lex.curName();
                n = lex.cntLines();
                lex.nextToken();
                if (lex.curToken() == Token.LPAREN) {
                    lex.nextToken();
                    ArrayList<Tree> ch = new ArrayList<Tree>();
                    ch.add(new Tree(cn, n));
                    while (lex.curToken() != Token.RPAREN) {
                        Tree ee = expression();
                        ch.add(ee);
                        if (!(lex.curToken() == Token.COMMA || lex.curToken() == Token.RPAREN)) {
                            throw new ParseException(" expected ) or , " +
                                    " at position ", lex.curPos());
                        }
                    }
                    lex.nextToken();
                    return new Tree("CallExpression", n, ch);
                }
                //id || <call-expression>. Считаем, что call-expression пока не существует
                else return new Tree("identifier",n,  new Tree(cn, n));
                // return null;
            }
            case LPAREN: {
                lex.nextToken();
                Tree be = binaryExpression();
                // exp = expressin();
                if (lex.curToken() != Token.RPAREN) {
                    throw new ParseException(" expected ) " +
                            " at position ", lex.curPos());
                }
                lex.nextToken();
                return be;
//                return new Tree("expression", new Tree("("), be, new Tree(")"));
            }
            case LSQPAREN: {
                lex.nextToken();
                Tree e1 = expression();
                if (lex.curToken() != Token.RSQPAREN) {
                    throw new ParseException(" expected ] " +
                            " at position ", lex.curPos());
                }
                lex.nextToken();

                if (lex.curToken() != Token.QUESTM) {
                    throw new ParseException(" expected ? " +
                            " at position ", lex.curPos());
                }
                lex.nextToken();
                if (lex.curToken() != Token.LPAREN) {
                    throw new ParseException(" expected ( " +
                            " at position ", lex.curPos());
                }
                lex.nextToken();
                Tree e2 = expression();
//                lex.nextToken();
                if (lex.curToken() != Token.RPAREN) {
                    throw new ParseException(" expected ) " +
                            " at position ", lex.curPos());
                }
                lex.nextToken();
                if (lex.curToken() != Token.COLON) {
                    throw new ParseException(" expected : " +
                            " at position ", lex.curPos());
                }
                lex.nextToken();
                if (lex.curToken() != Token.LPAREN) {
                    throw new ParseException(" expected ( " +
                            " at position ", lex.curPos());
                }
                lex.nextToken();
                Tree e3 = expression();
                if (lex.curToken() != Token.RPAREN) {
                    throw new ParseException(" expected ) " +
                            " at position ", lex.curPos());
                }
                lex.nextToken();
                return new Tree("if", n, e1, e2, e3);
            }
            case NUMBER: {
                String cn = lex.curName();
                lex.nextToken();
                return new Tree("const", n, new Tree(cn, n));
                // return null;
            }
            case MINUS: {
                String tokenString = "-";
                lex.nextToken();
                if (lex.curToken != Token.NUMBER) {
                    throw new ParseException(" expected DIGIT " +
                            " at position ", lex.curPos());
                } else {
                    tokenString += lex.curName();
                    lex.nextToken();
                }
                return new Tree("const", n, new Tree(tokenString, n));
                // return null;
            }
            default: {
                throw new ParseException(" expected expression" +
                        " at position ", lex.curPos());
            }
            //<constant-expression>


        }
//        return null;
    }

    static Tree program() throws ParseException {
        //если начинается с id может одинаково начинаться deflist, либо уже expr (id или call).
        // Не будем преобразовывать гамматику в LL1,  просто сделаем хитрость
//        Tree fdl
        int n = lex.cntLines();
        if (lex.curToken() != Token.IDENTIFIER) {
            Tree expr = expression();
            return new Tree("PROGRAM", n, new Tree("functionDefinitionList", n), expr);
        }
        ArrayList<Tree> ch = new ArrayList<>();
        //lastIS
        while (lex.curToken() == Token.IDENTIFIER) {
            n = lex.cntLines();
            String id = lex.curName();
            lex.nextToken();
            if (lex.curToken() == Token.LPAREN) {
                lex.nextToken();
                ArrayList<Tree> ch1 = new ArrayList<Tree>();
//                ch1.add(new Tree(id));
                while (lex.curToken() != Token.RPAREN) {
                    Tree ee = expression();
                    ch1.add(ee);
                    if (!(lex.curToken() == Token.COMMA || lex.curToken() == Token.RPAREN)) {
                        throw new ParseException(" expected ) or , " +
                                " at position ", lex.curPos());
                    }
                    if (lex.curToken() == Token.COMMA) lex.nextToken();
                }
                lex.nextToken();
                //проверяем, что это decl
                if (lex.curToken==Token.EQ) {
                    //если хоть один из детей не id => err.
                    ArrayList<Tree> ch2 = new ArrayList<>();
                    for (int i = 0; i<ch1.size(); i++) {
                        if (!ch1.get(i).node.equals("identifier")) {
                            throw new ParseException("params should be indetifiers" +
                                    " at position ", lex.curPos());
                        }
                        ch2.add(ch1.get(i).children.get(0));
                    }
                    lex.nextToken();
                    if (lex.curToken() != Token.FBEGIN) {
                        throw new ParseException(" expected { " +
                                " at position ", lex.curPos());
                    }
                    lex.nextToken();
                    Tree ee = expression();
                    if (lex.curToken() != Token.FEND) {
                        throw new ParseException(" expected } " +
                                " at position ", lex.curPos());
                    }
                    lex.nextToken();
                    if (lex.curToken() != Token.EOLN) {
                        throw new ParseException(" expected EOLN " +
                                " at position ", lex.curPos());
                    }
                    lex.nextToken();
                    ch.add(new Tree(id, n, new Tree("parameterList", n,  ch2), ee));
                } else  {
                    Tree fdl = new Tree("functionDefinitionList",n,  ch);
                    ch1.add(0, (new Tree (id, n) ));
                    Tree expr =  new Tree("CallExpression", n, ch1);
                    return new Tree("PROGRAM", n, fdl, expr);
                }

                //
                //либо объявление, либо вызов
            } else {
//                Tree expr = expression();
                Tree expr = new Tree("identifier",n,  new Tree(id, n));
                Tree fdl = new Tree("functionDefinitionList",n,  ch);
                return new Tree("PROGRAM",n,  fdl, expr);
                //точно было выражение состояще из одного id
            }
        }
        Tree fdl = new Tree("functionDefinitionList", n ,ch);
        Tree expr =  expression();
        return new Tree("PROGRAM", n, fdl, expr);


        // <function-definition-list> <expression>
//		return null;
    }

    public static Tree parse() throws ParseException {

        lex.nextToken();
        Tree parseTree = program();
        if (lex.curToken() != Token.END) {
            throw new ParseException(" expected end " +
                    " at position ", lex.curPos());
        }
        return parseTree;
    }
}