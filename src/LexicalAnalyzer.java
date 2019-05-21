import java.io.*;
import java.text.ParseException;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.isWhitespace;

public class LexicalAnalyzer {
    InputStream is;
    String analyzed = "";
    int curChar;
    int curPos;
	int cntLines = 1;
    String curName;
    Token curToken;

    public LexicalAnalyzer(InputStream is) throws ParseException {
        this.is = is;
        curPos = 0;
        nextChar();
    }

    private boolean isBlank(int c) {
//        return isWhitespace(c);
        return  (c == '\r');
    }

    private boolean isLetter(int c) {
        return ((c <= 'z')&&(c >= 'a') || (c <= 'Z')&&(c >= 'A')||(c=='_'));
    }
    private boolean isNumber(int c) {
        return ((c <= '9')&&(c >= '0'));
    }
	
    private void nextChar() throws ParseException {
        curPos++;
        try {
            curChar = is.read();
            analyzed+=(char) curChar;
        } catch (IOException e) {
            throw new ParseException(e.getMessage(), curPos);
        }
    }

    public void nextToken() throws ParseException {
        while (isBlank(curChar)) {
            nextChar();
        }
        if (isLetter(curChar)) {
            curName = "" + (char) curChar;
            nextChar();
            while (isLetter(curChar)) {
                curName+=(char) curChar;
                nextChar();
            }
			
            curToken = Token.IDENTIFIER;
        }
		
		else if (isNumber(curChar)) {
			curName = "" + (char) curChar;
            nextChar();
            while (isNumber(curChar)) {
                curName+=(char) curChar;
                nextChar();
            }
            curToken = Token.NUMBER;
		}
		
        else {
            switch (curChar) {
                case '+':
                    nextChar();
                    curToken = Token.PLUS;
                    break;
                case '-':
                    nextChar();
                    curToken = Token.MINUS;
                    break;
				case '*':
                    nextChar();
                    curToken = Token.MUL;
                    break;
				case '/':
                    nextChar();
                    curToken = Token.DIV;
                    break;
				case '%':
                    nextChar();
                    curToken = Token.MOD;
                    break;
				case '>':
                    nextChar();
                    curToken = Token.MORE;
                    break;
				case '<':
                    nextChar();
                    curToken = Token.LESS;
                    break;
				case '=':
                    nextChar();
                    curToken = Token.EQ;
                    break;
                case '(':
                    nextChar();
                    curToken = Token.LPAREN;
                    break;
                case ')':
                    nextChar();
                    curToken = Token.RPAREN;
                    break;
				case '[':
                    nextChar();
                    curToken = Token.LSQPAREN;
                    break;
                case ']':
                    nextChar();
                    curToken = Token.RSQPAREN;
                    break;
				case ',':
                    nextChar();
                    curToken = Token.COMMA;
                    break;
				case '?':
                    nextChar();
                    curToken = Token.QUESTM;
                    break;
				case ':':
                    nextChar();
                    curToken = Token.COLON;
                    break;
				case '{':
                    nextChar();
                    curToken = Token.FBEGIN;
                    break;
				case '}':
                    nextChar();
                    curToken = Token.FEND;
                    break;
                case '\n':
                    nextChar();
					cntLines++;
                    curToken = Token.EOLN;
                    break;
                case -1:
                    curToken = Token.END;
                    break;
                default:
                    throw new ParseException("Illegal character \"" + (char) curChar + "\"", curPos);
            }
        }
    }
	
	public int cntLines() {
		return cntLines;
	}

    public Token curToken() {
        return curToken;
    }
    public String curName() {
        return  curName;
    }

    public int curPos() {
        return curPos;
    }
}