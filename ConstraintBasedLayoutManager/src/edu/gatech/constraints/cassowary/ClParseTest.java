// $Id: ClParseTest.java,v 1.1 2008/08/12 22:32:43 larrymelia Exp $
// ClParseTest.java
// Needs JavaCUP 10k or newer, from:
// http://www.cs.princeton.edu/~appel/modern/java/CUP/
// And JLex from:
// http://www.cs.princeton.edu/~appel/modern/java/JLex/index.html

package edu.gatech.constraints.cassowary;

import java.io.StringReader;
import java_cup.runtime.*;
import java.util.*;

public class ClParseTest {
    @SuppressWarnings("unchecked")
    public final static void main(String[] args) {
        try {
            System.out.println("Parsing: " + args[0]);
            StringReader string_reader = new StringReader(args[0]);
            Yylex yylex = new Yylex(string_reader);
            Hashtable varmap = new Hashtable();
            yylex.setVariableNameObjectHash(varmap);
            parser constraint_parser = new parser(yylex);
            constraint_parser.setVariableNameObjectHash(varmap);
            Symbol symbol = constraint_parser.parse();
            System.out.println("Parsed as: " + symbol.value.toString());
        } catch (Exception exception) {
            System.out.println("Exception: " + exception.getMessage());
            exception.printStackTrace();
        }
    }
}
