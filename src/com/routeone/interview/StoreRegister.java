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

                //Handle if there is no dollar sign in the inventory file.
                myOriginalValueFromInventory = myValueColumn.get("value");
                myValueToFormat = "$"+myOriginalValueFromInventory.replace("$","");

                Matcher myMatcher = pattern.matcher(myValueToFormat);
                if (!myMatcher.matches()) {
                    throw new RuntimeException(INVALID_MONEY_COLUMN + myOriginalValueFromInventory);
                }
                myTotal += myFormat.parse(myValueToFormat).doubleValue();
            }
        } catch(ParseException pe) {
            throw new RuntimeException(INVALID_MONEY_COLUMN + myOriginalValueFromInventory);
        }
        return myFormat.format(myTotal);
    }

    private List<String> orderItems(List<String> items) {
        if (items == null) {
            return new ArrayList<String>();
        }
        final List<String> myOrderedItems = new ArrayList<String>();

        //Order most expensive item to list, then alphabetically
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