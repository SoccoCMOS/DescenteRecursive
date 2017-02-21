package compil_descend;

import java.util.regex.Pattern;

public class TestsUnitaires {
    
    /// Test des fonctions sur les expressions régulières
    public static void testRegex()
    {
        String a="afgg2fg";
        String regex="([a-zA-Z]|_[a-zA-Z])([a-zA-Z]|[0-9])*";
        if (Pattern.matches(regex,a)) System.out.println("OK");
        else System.out.println("NOTOK");
    }
    
    public static void testLexeme()
    {
        Lexeme l;
        String s="tan(3)";
        Descente_Recursive d=new Descente_Recursive();
        d.e=s;
        
        l=d.getLexeme();
        while (l!=null)
        {
            l.printLexeme();
            d.e=d.e.substring(l.startIndex+l.length);
            l=d.getLexeme();
        }  
    }
    
    public static void testSyntax()
    {
        Attribut a;
        String s="somme (moyenne(1,1,1,1),cos(0*20),tan(90/2),1)#";
        Descente_Recursive d=new Descente_Recursive();
        d.e=s;
        a=d.Z();
        System.out.println("Valeur = " + a.value+ " Type = " + a.type);
    }  
}
