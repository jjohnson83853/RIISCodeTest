package com.routeone.interview;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bullprog3 on 11/3/15.
 */
public class CSVFile extends HashMap<String,Map<String,String>> {

    private Map<String, Map<String,String>> contents = null;
    final private File file;
    final private List<String> columns;
    final private String indexColumn;
    final static private Pattern splitSearchPattern = Pattern.compile("[\",]");

    protected final static String WRONG_NUMBER_COLUMN = "Wrong number of column in CSV file.";

    public CSVFile(String indexColumn, List<String> columns, File file) {
        this.file = file;
        this.columns = columns;
        this.indexColumn=indexColumn;
    }


    private List<String> splitByCommas(String stringToSplit) {
        if (stringToSplit == null)
            return Collections.emptyList();

        final List<String> myCommaSeperatedValues = new ArrayList<String>();
        final Matcher mySplitPatternMatcher = splitSearchPattern.matcher(stringToSplit);
        int pos = 0;
        boolean myInQuoteMode = false;
        while (mySplitPatternMatcher.find())
        {
            final String mySeperator = mySplitPatternMatcher.group();
            if ("\"".equals(mySeperator))
            {
                myInQuoteMode = !myInQuoteMode;
            }
            else if (!myInQuoteMode && ",".equals(mySeperator))
            {
                final int toPos = mySplitPatternMatcher.start();
                myCommaSeperatedValues.add(stringToSplit.substring(pos, toPos).replace("\",\"",","));
                pos = mySplitPatternMatcher.end();
            }
        }
        if (pos < stringToSplit.length()) {
            myCommaSeperatedValues.add(stringToSplit.substring(pos).replace("\",\"",","));
        }
        return myCommaSeperatedValues;
    }

    private void parseFile() {
            if(contents == null) {
                try {
                    this.contents = new HashMap<String,Map<String,String>>();
                    final BufferedReader myReader = new BufferedReader(new FileReader(file));
                    String line = null;
                    while ((line = myReader.readLine()) != null) {
                        final List<String> myColumns = splitByCommas(line);
                        final Map<String,String> myRow = new HashMap<String, String>();
                        int i = 0;
                        for( String column : columns) {
                            myRow.put(column,myColumns.get(i));
                            ++i;
                        }
                        if(myRow.size() != myColumns.size()) {
                            throw new RuntimeException(WRONG_NUMBER_COLUMN);
                        }
                        contents.put(myRow.get(indexColumn), myRow);
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
    }

    @Override
    public int size() {
        parseFile();
        return contents.size();
    }

    @Override
    public boolean isEmpty() {
        parseFile();
        return contents.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        parseFile();
        return contents.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        parseFile();
        return contents.containsValue(value);
    }

    @Override
    public Map<String, String> get(Object key) {
        parseFile();
        return contents.get(key);
    }

    @Override
    public Map<String, String> put(String key, Map<String, String> value) {
        throw new RuntimeException(this.getClass().getName() + " is readonly");
    }

    @Override
    public Map<String, String> remove(Object key) {
        throw new RuntimeException(this.getClass().getName() + " is readonly");
    }

    @Override
    public void putAll(Map<? extends String, ? extends Map<String, String>> m) {
        throw new RuntimeException(this.getClass().getName() + " is readonly");
    }


    @Override
    public void clear() {
        throw new RuntimeException(this.getClass().getName() + " is readonly");
    }

    @Override
    public Set<String> keySet() {
        parseFile();
        return contents.keySet();
    }

    @Override
    public Collection<Map<String, String>> values() {
        parseFile();
        return contents.values();
    }

    @Override
    public Set<Map.Entry<String, Map<String, String>>> entrySet() {
        parseFile();
        return contents.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        parseFile();
        return contents.equals(o);
    }

    @Override
    public int hashCode() {
        parseFile();
        return contents.hashCode();
    }

}
