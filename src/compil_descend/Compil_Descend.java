
package compil_descend;

import java.util.Scanner;

public class Compil_Descend {

    public static void main(String[] args) {
        //TestsUnitaires.testRegex();  // TESTED
        //TestsUnitaires.testLexeme();   // TESTED
        //TestsUnitaires.testSyntax();   // TESTED
        
        String expression="";
        Scanner sc=new Scanner(System.in);
        expression=sc.nextLine();
        Attribut a=null;
        
        if (expression.charAt(0)=='=') 
        {
            expression=expression.substring(1)+"#";
            Descente_Recursive d=new Descente_Recursive();
            d.e=expression;
            a=d.Z();
            System.out.println("Valeur = " + a.value+ " Type = " + a.type);            
        }
    } 
}
