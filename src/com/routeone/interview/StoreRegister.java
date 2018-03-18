package com.routeone.interview;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoreRegister {

    protected final static String INVENTORY_ITEM_NOT_FOUND_BEGIN = "Cannot find item '";
    protected final static String INVENTORY_ITEM_NOT_FOUND_END = "' in inventory file.";
    protected static final String INVENTORY_FILE_IS_NOT_LOADED = "Inventory file is not loaded";
    protected static final String INVENTORY_FILE_MUST_BE_SPECIFIED = "Inventory file must be specified";

    public static final String INVALID_MONEY_COLUMN = "Invalid money column: ";
    private CSVFile csvFile = null;
    final static Pattern pattern = Pattern.compile("^\\$(([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))(\\.\\d\\d)?$");

    public void loadInventory(File inventoryFile) {
        if (inventoryFile == null) {
            throw new RuntimeException(INVENTORY_FILE_MUST_BE_SPECIFIED);
        }
        final List<String> inventoryFileColumns = new ArrayList<String>() {{
            add("name");
            add("value");
            add("category");
        }};
        this.csvFile = new CSVFile("name", inventoryFileColumns, inventoryFile);
    }


    private String sumOrderFormatted(List<String> items) {
        if (csvFile == null) {
            throw new RuntimeException(INVENTORY_FILE_IS_NOT_LOADED);
        }
        if (items == null) {
            return "$0.00";
        }
        double myTotal = 0.0;
        final NumberFormat myFormat = NumberFormat.getCurrencyInstance(Locale.US);

        String myValueToFormat = null;
        String myOriginalValueFromInventory = null;
        try {
            for (String myOrderItems : items) {
                myValueToFormat = null;
                Map<String, String> myValueColumn = csvFile.get(myOrderItems);
                if (myValueColumn == null) {
                    throw new RuntimeException(INVENTORY_ITEM_NOT_FOUND_BEGIN + myOrderItems + INVENTORY_ITEM_NOT_FOUND_END);
                }

                myOriginalValueFromInventory = myValueColumn.get("value");
                myValueToFormat = "$" + myOriginalValueFromInventory.replace("$", "");

                Matcher myMatcher = pattern.matcher(myValueToFormat);
                if (!myMatcher.matches()) {
                    throw new RuntimeException(INVALID_MONEY_COLUMN + myOriginalValueFromInventory);
                }
                myTotal += myFormat.parse(myValueToFormat).doubleValue();
            }
        } catch (ParseException pe) {
            throw new RuntimeException(INVALID_MONEY_COLUMN + myOriginalValueFromInventory);
        }
        return myFormat.format(myTotal);
    }

    private List<Map<String,String>> makeUnindexed(Map<String,Map<String,String>> items) {
        final List<Map<String,String>> myConvertedList = new ArrayList<Map<String,String>>(items.size());
        for(Map.Entry<String,Map<String,String>> myEntry: items.entrySet()) {
            myConvertedList.add( myEntry.getValue());
        }
        return myConvertedList;
    }

    private Map<String,Map<String,String>> findCSVRowsByItemName(List<String> items) {
        final Map<String,Map<String,String>> myCSVFile = new HashMap<String,Map<String,String>>(items.size());
        if(items !=null ){
            for(String myEntry: items) {
                myCSVFile.put(myEntry, this.csvFile.get(myEntry));
            }
        }
        return myCSVFile;
    }

    private List<String> orderItems(List<String> items) {
        if (items == null) {
            return Collections.emptyList();
        }

        final List<Map<String,String>> myConvertedList = makeUnindexed(findCSVRowsByItemName(items));

        //Order most expensive item to list, then alphabetically
        Collections.sort(myConvertedList, new Comparator<Map<String, String>>() {
            public int compare(Map<String, String> row1, Map<String, String> row2) {
                Double myValue1 = row1.get("value") != null ? Double.valueOf(row1.get("value")) : 0.00;
                Double myValue2 = row2.get("value") != null ? Double.valueOf(row2.get("value")) : 0.00;
                int myValueCompareResult = myValue1.compareTo(myValue2);
                if (myValueCompareResult != 0) {
                    return myValueCompareResult*-1;
                } else {
                    return row1.get("name") != null ? row1.get("name").compareTo(row2.get("name")) : -1;
                }
            }
        });

        //get keys back in order
        final List<String> myOrderedItems = new ArrayList<String>();
        for(Map<String,String> myRecord: myConvertedList) {
            myOrderedItems.add(myRecord.get("name"));
        }

        return myOrderedItems;
    }

    public Receipt checkoutOrder(List<String> items) {

        final String myFormattedTotal = sumOrderFormatted(items);
        final List<String> myOrderedItems = orderItems(items);
        return new Receipt() {
            @Override
            public String getFormattedTotal() {
                return myFormattedTotal;
            }

            @Override
            public List<String> getOrderedItems() {
                return myOrderedItems;
            }
        };
    }
}