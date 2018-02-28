import compiler.lexan.LexAn;
import compiler.lexan.Symbol;
import compiler.lexan.Token;


public class Neki {
    public static void main(String[] args) {
        LexAn l = new LexAn("./src/test.pins", false);
        Symbol s;
        do {
             s = l.lexAn();
            System.out.println(s.token + " " + s.lexeme + " [" + s.position + "]");
        }while (s.token != Token.EOF);



    }
}
