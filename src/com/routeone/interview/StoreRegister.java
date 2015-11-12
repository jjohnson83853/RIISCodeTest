package com.routeone.interview;

import java.util.*;
import java.io.File;
import java.text.*;
import java.util.regex.*;

public class StoreRegister {

    protected final static String INVENTORY_ITEM_NOT_FOUND_BEGIN = "Cannot find item '";
    protected final static String INVENTORY_ITEM_NOT_FOUND_END = "' in inventory file.";
    protected final static String INVALID_DOLLAR_AMOUNT_BEGIN = "Invalid dollar amount: ";
    protected static final String INVENTORY_FILE_IS_NOT_LOADED = "Inventory file is not loaded";
    protected static final String INVENTORY_FILE_MUST_BE_SPECIFIED = "Inventory file must be specified";

    public static final String INVALID_MONEY_COLUMN = "Invalid money column: ";
    private CSVFile csvFile = null;

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
        try {
            for (String myOrderItems : items) {
                myValueToFormat = null;
                Map<String, String> myValueColumn = csvFile.get(myOrderItems);
                if (myValueColumn == null) {
                    throw new RuntimeException(INVENTORY_ITEM_NOT_FOUND_BEGIN + myOrderItems + INVENTORY_ITEM_NOT_FOUND_END);
                }

                myValueToFormat = myValueColumn.get("value");
                Pattern myPattern = Pattern.compile("^\\$(([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))(\\.\\d\\d)?$");
                //Pattern myPattern = Pattern.compile("19.99");
                Matcher myMatcher = myPattern.matcher(myValueToFormat);
                if (!myMatcher.matches()) {
                    throw new RuntimeException(INVALID_MONEY_COLUMN + myValueToFormat);
                }
                myTotal += myFormat.parse(myValueToFormat).doubleValue();
            }
        } catch(ParseException pe) {
            throw new RuntimeException(INVALID_DOLLAR_AMOUNT_BEGIN + myValueToFormat);
        }
        return myFormat.format(myTotal);
    }

    private List<String> orderItems(List<String> items) {
        if (items == null) {
            return new ArrayList<String>();
        }
        final List<String> myOrderedItems = new ArrayList<String>();
        myOrderedItems.addAll(items);
        Collections.sort(myOrderedItems);
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