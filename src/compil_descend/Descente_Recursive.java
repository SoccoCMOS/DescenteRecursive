package compil_descend;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Descente_Recursive {
    
       /* Expressions régulières utilisées */
    private final String integer="[0-9]+";
    private final String floating="[0-9]+.[0-9]+";
    private final String letter="[a-zA-Z]+";
    private final String operator="[-+*/,]";
    private final String brackets="[()]";
    private final String separator="[ ,\t]+";
    private final String cosRegex="cos";
    private final String sinRegex="sin";
    private final String tanRegex="tan";
    private final String sommeRegex="somme";
    private final String moyenneRegex="moyenne";
    private final String varianceRegex="variance";    
    private final String ecarttypeRegex="ecarttype";
    private final String identifiant="([a-zA-Z]|_[a-zA-Z])([a-zA-Z]|[0-9])*";
    
    private final String endofexpression="[#\n]";
    private final String spacedelimiters="[\n\t \n]";
    private final String caracdelimiters="[-+*/,()?!%#]";
    
        /* Messages d'erreur syntaxiques */
    private final String MER1="Debut de chaine attendu";
    private final String MER2="Operateur attendu";
    private final String MER3="Operande attendu";
    
        /* Messages d'erreur lexicales */
    private final String MEL0="No more lexeme retrievable";
    private final String MEL1="Caractere invalide";
    
        /* Messages d'erreur sémantiques */
    private final String MERS0="Erreur : Division par zero !";
    
    /* Routines d'analyse lexicale */
    String e; // chaine à analyser 
    StringTokenizer mainst; //Entités lexicales
    int currentIndex=-1; // Utilisé pour retourner la position de l'erreur
    int lineIndex=-1; // Sera utilisé dans le cas des fichiers
    
    /* Attributs pour la récupération sur erreur */
    int nbLexError=0;
    int nbSyntError=0;
    int nbSemError=0;
    
    public void Tokenizer()
    {
        mainst=new StringTokenizer(e,spacedelimiters);
    }
    public Lexeme getLexeme()
    {
        Lexeme lex=new Lexeme();
        StringTokenizer st2;
        String el="";
        
        if (e.length()==0) return null;
        
        Tokenizer();
        try
        {
            if (mainst.hasMoreTokens())
            {
                st2=new StringTokenizer(mainst.nextToken(),caracdelimiters,true);
                // Construction du lexeme à retourner
                el=st2.nextToken();
                if (el.matches(integer))
                {
                    lex.symbol="n";
                    lex.length=el.length();
                    lex.a=new Attribut("integer",Integer.parseInt(el));
                    lex.startIndex=e.indexOf(el);
                }
                else if (el.matches(floating))
                {
                    lex.symbol="f";
                    lex.length=el.length();
                    lex.a=new Attribut("float",Float.parseFloat(el));
                    lex.startIndex=e.indexOf(el);
                }
                else if (el.matches(operator) | el.matches(brackets) | el.matches(endofexpression))
                {
                    lex.symbol=el;
                    lex.length=el.length();
                    lex.a=null;
                    lex.startIndex=e.indexOf(el);
                }
                else if (el.matches(cosRegex))
                {
                    lex.startIndex=e.indexOf("cos");
                    lex.symbol="cos";
                    lex.length=3;     
                }
                
                else if (el.matches(sinRegex))
                {
                    lex.startIndex=e.indexOf("sin");
                    lex.symbol="sin";
                    lex.length=3;     
                }
                
                else if (el.matches(tanRegex))
                {
                    lex.startIndex=e.indexOf("tan");
                    lex.symbol="tan";
                    lex.length=3;     
                }
                else if (el.matches(sommeRegex))
                {
                    lex.startIndex=e.indexOf("somme");
                    lex.symbol="somme";
                    lex.length=5; 
                }
                else if (el.matches(moyenneRegex))
                {
                    lex.startIndex=e.indexOf("moyenne");
                    lex.symbol="moyenne";
                    lex.length=7; 
                }
                else if (el.matches(varianceRegex))
                {
                    lex.startIndex=e.indexOf("variance");
                    lex.symbol="variance";
                    lex.length=8; 
                }
                else if (el.matches(ecarttypeRegex))
                {
                    lex.startIndex=e.indexOf("ecarttype");
                    lex.symbol="ecarttype";
                    lex.length=9; 
                }
                else if (el.matches(identifiant))
                {
                    lex.startIndex=e.indexOf(el);
                    lex.symbol="id";
                    lex.length=el.length();
                }
                else 
                {
                    System.out.println(MEL1 + el);
                    lex=null;
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return lex;
    }
        /* Routines d'analyse syntaxique */
    
    public Attribut Z()
    {
        Attribut val=new Attribut(); // Valeur à retourner
        Lexeme lc=getLexeme();
        
        System.out.println("Procedure Z");
        lc.printLexeme();
        
        switch (lc.getSymbol())
        {
            case "(" :
            case "-":
            case "n" :
            case "f":
            case "id":
            case "cos":
            case "sin":
            case "tan":
            case "somme":
            case "moyenne":
            case "variance":
            case "ecarttype":
            {
                val=E();
                if (getLexeme().symbol.matches(endofexpression)) System.out.println("Chaine correcte");
                else {System.out.println("Caractere de fin attendu"); System.exit(-1);}
                break;
            }
            default: {System.out.println("Erreur syntaxique : "+MER1);}
        }
        return val;
    }
    
    private Attribut E()
    {
        
        Attribut val=new Attribut(); // Valeur à retourner
        Attribut tmp=new Attribut();
        
        Lexeme lc=getLexeme();
        System.out.println("Procedure E");
        lc.printLexeme();
        
        switch (lc.getSymbol())
        {
            case "(" :
            case "-":
            case "n" :
            case "f":
            case "id":
            case "cos":
            case "sin":
            case "tan":
            case "somme": 
            case "moyenne":
            case "variance":
            case "ecarttype":
            {
                tmp=T();
                val=Ep(tmp);
                break;
            }
            default: {System.out.println(("Erreur syntaxique : "+MER1)); System.exit(1);}
        }
        return val;
    }
    
    private Attribut Ep(Attribut h)
    {
      Attribut val=new Attribut(); // Valeur à retourner
      Attribut tmp=new Attribut();
      Lexeme lc=getLexeme();
      
      System.out.println("Procedure Ep");
      lc.printLexeme();
      
      switch(lc.getSymbol())
        {
            case "+":
            {
                e=e.substring(lc.startIndex+lc.length);
                tmp=T();
                val=Ep(new Attribut(getSuitableType(tmp.type,h.type),tmp.value+h.value));
                break;
            }
            case "-":
            {
                e=e.substring(lc.startIndex+lc.length);
                tmp=T();
                val=Ep(new Attribut(getSuitableType(tmp.type,h.type),h.value-tmp.value));
                break;
            }
            case "#":
            case ",":
            case ")": { val=h; break;}
            default: {System.out.println("Erreur Syntaxique : "+MER2); System.exit(1);}
        }
        return val;
    }
    
    private Attribut T()
    {
      Attribut val=new Attribut(); // Valeur à retourner
      Attribut tmp=new Attribut();
      Lexeme lc=getLexeme();
      
      System.out.println("Procedure T");
      lc.printLexeme();
      switch(lc.getSymbol())
        {
            case "(":
            case "-":
            case "n":
            case "f":
            case "id":
            case "cos":
            case "sin":
            case "tan":
            case "somme":
            case "moyenne":
            case "variance":
            case "ecarttype":                
            {
                tmp=F();
                val=Tp(tmp);
                break;
            }
            default: {System.out.println(MER3); System.exit(1);}
        }
        return val;
    }
    
    private Attribut Tp(Attribut h)
    {
      Attribut val=new Attribut(); // Valeur à retourner
      Attribut tmp=new Attribut();
      Lexeme lc=getLexeme();
      
      System.out.println("Procedure Tp");
      lc.printLexeme();
      switch (lc.getSymbol())
        {
            case "*":
            {
                e=e.substring(lc.startIndex+lc.length);
                tmp=F();
                val=Tp(new Attribut(getSuitableType(tmp.type,h.type),h.value*tmp.value));
                break;
            }
            case "/":
            {
                e=e.substring(lc.startIndex+lc.length);
                tmp=F();
                // Traitement de la division par zéro
                if (tmp.value==0) 
                {
                    System.out.println(MERS0);
                    System.exit(-1);
                }
                val=Tp(new Attribut("float",h.value/tmp.value));
                break;
            }
            case "+":
            case "-":
            case ",":
            case "#": 
            case ")": { val=h; break;}
            default: {System.out.println("Erreur syntaxique : operateur attendu"); }
        }
        
      return val;
    }
    
    private Attribut F()
    {
      Attribut val=new Attribut(); // Valeur à retourner
      Attribut tmp=new Attribut();
      Lexeme lc=getLexeme();
      
      System.out.println("Procedure F");
      lc.printLexeme();
      switch(lc.getSymbol())
        {
            case "n":
            {
                val.type="integer";
                val.value=lc.getA().value;
                e=e.substring(lc.startIndex+lc.length);
                break;
            }
            case "f":
            {
                val.type="float";
                val.value=lc.getA().value;
                e=e.substring(lc.startIndex+lc.length);
                break;
            }
            case "id":
            {
                
                break;
            }
            case "(":
            {
                e=e.substring(lc.startIndex+lc.length);
                val=E();
                if (e.charAt(0)==')') e=e.substring(1);
                else {System.out.println("Erreur syntaxique, parenthese ) attendue"); System.exit(1);}
                break;
            }
            case "-":
            {
                e=e.substring(lc.startIndex+lc.length);
                val=F();
                val.value*=-1;
                break;
            }
            
            case "cos":
            {
                e=e.substring(lc.length+lc.startIndex);
                lc=getLexeme();
                if (lc.symbol.equalsIgnoreCase("(")) 
                {
                    e=e.substring(lc.startIndex+lc.length);
                    System.out.println(lc.startIndex + e);
                }
                else 
                {
                    System.out.println("Erreur syntaxique ( attendue"); System.exit(-1);
                }
                // Récupération de l'argument
                val=E();
                lc=getLexeme();
                if (lc.symbol.equalsIgnoreCase(")")) 
                {
                    e=e.substring(lc.startIndex+lc.length);
                    System.out.println(e);
                }
                else 
                {
                    System.out.println("Erreur syntaxique ) attendue"); System.exit(-1);
                }             
                // Calcul du cosinus
                val.type="float";
                val.value=(float)Math.cos(val.value*Math.PI/180);
                break;
            }
            case "sin":
            {
                e=e.substring(lc.length+lc.startIndex);
                lc=getLexeme();
                if (lc.symbol.equalsIgnoreCase("(")) 
                {
                    e=e.substring(lc.startIndex+lc.length);
                    System.out.println(lc.startIndex + e);
                }
                else 
                {
                    System.out.println("Erreur syntaxique ( attendue"); System.exit(-1);
                }
                // Récupération de l'argument
                val=E();
                lc=getLexeme();
                if (lc.symbol.equalsIgnoreCase(")")) 
                {
                    e=e.substring(lc.startIndex+lc.length);
                    System.out.println(e);
                }
                else 
                {
                    System.out.println("Erreur syntaxique ) attendue"); System.exit(-1);
                }  
                // calcul du sinus
                val.type="float";
                val.value=(float)Math.sin(val.value*Math.PI/180);
                break;
            }
            case "tan":
            {
                e=e.substring(lc.length+lc.startIndex);
                lc=getLexeme();
                if (lc.symbol.equalsIgnoreCase("(")) 
                {
                    e=e.substring(lc.startIndex+lc.length);
                    System.out.println(lc.startIndex + e);
                }
                else 
                {
                    System.out.println("Erreur syntaxique ( attendue"); System.exit(-1);
                }
                // Récupération de l'argument
                val=E();
                lc=getLexeme();
                if (lc.symbol.equalsIgnoreCase(")")) 
                {
                    e=e.substring(lc.startIndex+lc.length);
                    System.out.println(e);
                }
                else 
                {
                    System.out.println("Erreur syntaxique ) attendue"); System.exit(-1);
                }  
                
                // Calcul de la tangente
                val.type="float";
                val.value=(float)Math.tan(val.value*Math.PI/180);
                break;
            }
            case "somme":
            {
                e=e.substring(lc.length+lc.startIndex);
                lc=getLexeme();
                if (lc.symbol.equalsIgnoreCase("(")) e=e.substring(lc.startIndex+lc.length);
                else 
                {
                    System.out.println("Erreur syntaxique ( attendue"); System.exit(-1);
                } 
                // Récupération de la liste d'arguments
                ArrayList<Attribut> arg=new ArrayList<Attribut>();
                arg=LA();
    
                // Evaluation de la somme
                val=Somme(arg);
                
                lc=getLexeme();
                if (lc.symbol.equalsIgnoreCase(")")) e=e.substring(lc.startIndex+lc.length);
                else
                {
                    System.out.println("Erreur syntaxique ) attendue"); System.exit(-1);
                }  
                break;
            }
            case "moyenne":
            {
                e=e.substring(lc.length+lc.startIndex);
                lc=getLexeme();
                if (lc.symbol.equalsIgnoreCase("(")) e=e.substring(lc.startIndex+lc.length);
                else 
                {
                    System.out.println("Erreur syntaxique ( attendue"); System.exit(-1);
                } 
                // Récupération de la liste d'arguments
                ArrayList<Attribut> arg=new ArrayList<Attribut>();
                arg=LA();
    
                // Evaluation de la moyenne
                val=Moyenne(arg);
                lc=getLexeme();
                if (lc.symbol.equalsIgnoreCase(")")) e=e.substring(lc.startIndex+lc.length);
                else
                {
                    System.out.println("Erreur syntaxique ) attendue"); System.exit(-1);
                }  
                break;
            }
            
            case "variance":
            {
                e=e.substring(lc.length+lc.startIndex);
                lc=getLexeme();
                if (lc.symbol.equalsIgnoreCase("(")) e=e.substring(lc.startIndex+lc.length);
                else 
                {
                    System.out.println("Erreur syntaxique ( attendue"); System.exit(-1);
                } 
                // Récupération de la liste d'arguments
                ArrayList<Attribut> arg=new ArrayList<Attribut>();
                arg=LA();
    
                // Evaluation de la variance
                val=Variance(arg);
                lc=getLexeme();
                if (lc.symbol.equalsIgnoreCase(")")) e=e.substring(lc.startIndex+lc.length);
                else
                {
                    System.out.println("Erreur syntaxique ) attendue"); System.exit(-1);
                }  
                break;
            }
            
            case "ecarttype": 
            {
                e=e.substring(lc.length+lc.startIndex);
                lc=getLexeme();
                if (lc.symbol.equalsIgnoreCase("(")) e=e.substring(lc.startIndex+lc.length);
                else 
                {
                    System.out.println("Erreur syntaxique ( attendue"); System.exit(-1);
                } 
                // Récupération de la liste d'arguments
                ArrayList<Attribut> arg=new ArrayList<Attribut>();
                arg=LA();
    
                // Evaluation de l'écart-type
                val=Ecarttype(arg);
                
                lc=getLexeme();
                if (lc.symbol.equalsIgnoreCase(")")) e=e.substring(lc.startIndex+lc.length);
                else
                {
                    System.out.println("Erreur syntaxique ) attendue"); System.exit(-1);
                }  
                break;
            }
            
            default: {System.out.println("Erreur syntaxique : identificateur attendu"); System.exit(1);}
        }
      return val;
    }
    
    private ArrayList<Attribut> LA()
    {
      System.out.println("Procedure LA");
      ArrayList<Attribut> arg=new ArrayList<Attribut>();
      Attribut val=new Attribut(); // Valeur à retourner
      Lexeme lc=getLexeme();
      
      switch(lc.symbol)
      {
          case "(":
          case "n":
          case "f":
          case "id":
          case "somme":
          case "moyenne":
          case "variance":
          case "ecarttype":
          case "cos":
          case "sin":
          case "tan":
          { 
            // Traitement DEBUT(EG)
            val=E();
            arg=G(val);
            break;
          }
          default: System.out.println("( ou identifiant attendu");
      }
      return arg;
    }
    
    private ArrayList<Attribut> G(Attribut h)
    {
        System.out.println("Procedure G");
        ArrayList<Attribut> l=new ArrayList<Attribut>();
        Lexeme lc=getLexeme();
        Attribut tmp;
        
        switch(lc.symbol)
        {
            case ",":
            {
                e=e.substring(lc.startIndex+lc.length);
                l.add(h);
                tmp=E();
                l.addAll(G(tmp));
                break;
            }
            case ")":
            {
                l.add(h); break;
            }
            default: System.out.println("Erreur Syntaxique , ou ) attendue");
        }
        return l;
    }
    
    /** Fonctions arithmétiques implémentées **/
    
    private Attribut Somme(ArrayList<Attribut> arg)
    {
        Attribut res=new Attribut("integer",0);
        for (int i=0; i<arg.size(); i++)
        {
            if (arg.get(i).type.equalsIgnoreCase("float")) res.type="float";
            res.value+=arg.get(i).value;
        }
        return res;
    }
    
    private Attribut Moyenne(ArrayList<Attribut> arg)
    {
        Attribut res=new Attribut("float",0);
        if (!arg.isEmpty()) res.value=Somme(arg).value/arg.size();
        return res;
    }
    private Attribut Variance(ArrayList<Attribut> arg)
    {
        Attribut res=new Attribut("float",0);
        float moyenne=Moyenne(arg).value;
        
        if (!arg.isEmpty())
        {
            for (int i=0; i<arg.size(); i++)
            {
                res.value+=Math.pow((arg.get(i).value-moyenne),2);
            }
            res.value=res.value/arg.size();
        }
        return res;
    }
    
    private Attribut Ecarttype(ArrayList<Attribut> arg)
    {
        return new Attribut("float",(float)Math.sqrt(Variance(arg).value));
    }    
    private String getSuitableType(String op1,String op2) //Retourne le type du résultat de l'opération binaire op1 et op2 sont des types d'opérandes
    {
        if (op1==op2) return op1;
        else return "float";
    }
}
