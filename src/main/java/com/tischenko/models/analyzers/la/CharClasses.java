package com.tischenko.models.analyzers.la;

enum CharClasses {
  L("L", 'a', 'b', 'c', 'd', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
          'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
          'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
          'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'),
  N("N", '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'),
  OP("OP", ',', '(', ')', '{', '}', '[', ']', ';', '+', '-', '/', ':'),
  LESS("<", '<'),
  MORE(">", '>'),
  EQUALS("=", '='),
  NOT("!", '!'),
  DOT(".", '.'),
  STAR("*", '*'),
  E("e", 'e'),
  WHITE("white", (char) 9, (char) 32, (char) 10, (char) 13, '#'),
  COMMENT("comment", '#');

  private final char[] chars;
  private final String name;
  CharClasses(String name, char... chars) {
    this.chars = chars;
    this.name = name;
  }

  public static String defineCharClass(char c) {
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

  public boolean contains(char c) {
    for (char i : chars) {
      if (i == c) return true;
    }
    return false;
  }
}