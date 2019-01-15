package com.danarossa.compiler.models.analyzers.lexical;

enum CharClasses {
  OP( ',' , '[' , ']' , '(' , ')' , '{' , '}' , '?' , ';' , '+' , '-', '*' , '/'),
  POSSIBLE_DOUBLE('<', '>', '=', '!'),
  DOUBLE_ENDING('='),
  WHITE((char)9, (char)32, (char)10, (char)13, '#');

  CharClasses(char ...chars) {
    this.chars = chars;
  }

  private final char[] chars;

  public boolean contains(char c) {
    for(char i : chars){
      if(i == c) return true;
    }
    return false;
  }

}