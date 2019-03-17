package com.tischenko.controllers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class RelationTableWriter {

  private int maxTokenLength;
  private int totalLength;
  private LinkedHashMap<String, LinkedHashMap<String, String>> relationTable;

  RelationTableWriter(LinkedHashMap<String, LinkedHashMap<String, String>> relationTable, int maxTokenLength) {
    this.relationTable = relationTable;
    this.maxTokenLength = maxTokenLength;
    this.totalLength = maxTokenLength;
  }

  void saveRelationsTable(File file) throws BIOException {
    String relationTableFileName = addFileNameSpecification(file.getPath());
    try (Formatter formatter = new Formatter(new OutputStreamWriter(
            new FileOutputStream(relationTableFileName), StandardCharsets.UTF_8))) {
      formatTableHeader(formatter);
      for (HashMap.Entry<String, LinkedHashMap<String, String>> firstSymbol : relationTable.entrySet()) {
        writeALine(formatter, firstSymbol);
      }
    } catch (IOException e) {
      throw new BIOException(e);
    }
  }

  private void writeALine(Formatter formatter, Map.Entry<String, LinkedHashMap<String, String>> firstSymbol) {
    formatter.format(" %-" + maxTokenLength + "s |", firstSymbol.getKey());
    for (HashMap.Entry<String, String> secondSymbol : firstSymbol.getValue().entrySet()) {
      writeTheRelation(formatter, secondSymbol);
    }
    formatter.format(System.getProperty("line.separator"));
    writeSeparatorLine(formatter);
  }

  private void writeTheRelation(Formatter formatter, Map.Entry<String, String> secondSymbol) {
    formatter.format(" %-" + secondSymbol.getKey().length() + "s |", secondSymbol.getValue());
  }

  private void formatTableHeader(Formatter formatter) {
    formatter.format(" %-" + maxTokenLength + "s |", "Symbol");
    for (String oneSymbol : relationTable.keySet()){
      formatter.format(" %-" + oneSymbol.length() + "s |", oneSymbol);
      totalLength += oneSymbol.length() + 3;
    }
    formatter.format(System.getProperty("line.separator"));
    writeSeparatorLine(formatter);
  }

  private void writeSeparatorLine(Formatter formatter) {
    for (int j = 0; j < totalLength; j++) {
      formatter.format("_");
    }
    formatter.format(System.getProperty("line.separator"));
  }

  private String addFileNameSpecification(String name) {
    final int lastPeriodPos = name.lastIndexOf('.');
    if (lastPeriodPos <= 0) {
      return name + "RelationTable" + ".txt";
    } else {
      return name.substring(0, lastPeriodPos) + "RelationTable" + ".txt";
    }
  }

}
