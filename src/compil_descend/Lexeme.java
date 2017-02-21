
package compil_descend;

public class Lexeme {
    String symbol; // Codification du end-state correspondant
    Attribut a;
    int length=0;
    int startIndex=-1;

    public Lexeme() {
    }

    public String getSymbol() {
        return symbol;
    }

    public Attribut getA() {
        return a;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setA(Attribut a) {
        this.a = a;
    }
    
    public void printLexeme()
    {
        System.out.println("Symbole = " +symbol +" ");
        if (a!=null) System.out.println("type = " +a.type +" " + "value= " + a.value);            
    }
}
