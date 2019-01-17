package com.tischenko.models.analyzers.lexical;

enum CharClasses {
//  A-Za-z (без e)	L
//0-9	N
//  {};,():+-/	OP
//  e	e
//.	.
//        *	*
//<	<
//          >	>
//          =	=
//          !	!
  L("L",(char)65, (char)66, (char)67, (char)68, (char)69, (char)70, (char)71, (char)72, (char)73,
        (char)74, (char)75, (char)76, (char)77, (char)78, (char)79, (char)80,
        (char)81, (char)82, (char)83, (char)84, (char)85, (char)86, (char)87,
        (char)88, (char)89, (char)90, (char)97, (char)98, (char)99, (char)100,
        (char)102, (char)103, (char)104, (char)105, (char)106, (char)107, (char)108,
        (char)109, (char)110, (char)111, (char)112, (char)113, (char)114, (char)115,
        (char)116, (char)117, (char)118, (char)119, (char)120, (char)121, (char)122),
  N("N", '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'),
  OP("OP", ',' , '(' , ')' , '{' , '}', '[', ']' , ';' , '+' , '-',  '/', ':'),
  LESS("<", '<'),
  MORE(">", '>'),
  EQUALS("=", '='),
  NOT("!", '!'),
  DOT(".", '.'),
  STAR("*", '*'),
  E("e", 'e'),
  WHITE("white",(char)9, (char)32, (char)10, (char)13),
  COMMENT("comment", '#');
  CharClasses(String name, char ...chars) {
    this.chars = chars;
    this.name = name;
  }

  private final char[] chars;
  private final String name;

  public boolean contains(char c) {
    for(char i : chars){
      if(i == c) return true;
    }
    return false;
  }

  public static String defineCharClass(char c){
    if (CharClasses.L.contains(c)) return CharClasses.L.name;
    else if (CharClasses.N.contains(c)) return CharClasses.N.name;
    else if (CharClasses.OP.contains(c)) return CharClasses.OP.name;
    else if (CharClasses.LESS.contains(c)) return CharClasses.LESS.name;
    else if (CharClasses.MORE.contains(c)) return CharClasses.MORE.name;
    else if (CharClasses.EQUALS.contains(c)) return CharClasses.EQUALS.name;
    else if (CharClasses.NOT.contains(c)) return CharClasses.NOT.name;
    else if (CharClasses.DOT.contains(c)) return CharClasses.DOT.name;
    else if (CharClasses.E.contains(c)) return CharClasses.E.name;
    else if (CharClasses.STAR.contains(c)) return CharClasses.STAR.name;
    else return "";
  }
}