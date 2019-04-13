package com.tischenko.models.analyzers.sa;


import java.util.Objects;

class Relation {
  private final LanguageSymbol firstSymbol;
  private final LanguageSymbol secondSymbol;
  private RelationType relation;

  Relation(LanguageSymbol firstSymbol, LanguageSymbol secondSymbol) {
    this.firstSymbol = firstSymbol;
    this.secondSymbol = secondSymbol;
  }

  Relation(LanguageSymbol firstSymbol, LanguageSymbol secondSymbol, RelationType relation) {
    this.firstSymbol = firstSymbol;
    this.secondSymbol = secondSymbol;
    this.relation = relation;
  }

  RelationType getRelationType() {
    return relation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Relation)) return false;
    Relation relation = (Relation) o;
    return Objects.equals(firstSymbol, relation.firstSymbol) &&
            Objects.equals(secondSymbol, relation.secondSymbol);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstSymbol, secondSymbol);
  }

  LanguageSymbol getFirstSymbol() {
    return firstSymbol;
  }

  LanguageSymbol getSecondSymbol() {
    return secondSymbol;
  }

  @Override
  public String toString() {
    return  "  {" + firstSymbol + " " + relation +
            " " + secondSymbol + '}';
  }
}