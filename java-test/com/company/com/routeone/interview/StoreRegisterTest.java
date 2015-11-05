package com.company.com.routeone.interview;

import org.junit.Assert;
import org.junit.Test;
import java.util.*;
import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by bullprog3 on 11/3/15.
 */
public class StoreRegisterTest {

    @Test
    public void testLoadInventory() throws Exception {

        final StoreRegister myStoreRegister = new StoreRegister();
        myStoreRegister.loadInventory(new File("happy-path.csv"));

        final List<String> myTestItems = new ArrayList<String>() {
            {
                add("PC1033");
                add("GenericMotherboard");
                add("Mouse");
                add("LCD");
            }
        };
        final Receipt myReceipt = myStoreRegister.checkoutOrder(myTestItems);

        final List<String> myExpectedArray = new ArrayList<String>() {{
            add("GenericMotherboard");
            add("LCD");
            add("Mouse");
            add("PC1033");
        }};

        Assert.assertArrayEquals(myExpectedArray.toArray() ,myReceipt.getOrderedItems().toArray());
        Assert.assertEquals("$738.98", myReceipt.getFormattedTotal());
    }

    @Test
    public void testNullItemList(){

        final StoreRegister myStoreRegister = new StoreRegister();
        myStoreRegister.loadInventory(new File("happy-path.csv"));

        final Receipt myReceipt = myStoreRegister.checkoutOrder((List<String>)null);

        Assert.assertNotNull(myReceipt.getOrderedItems());
        Assert.assertEquals(0, myReceipt.getOrderedItems().size());
        Assert.assertEquals("$0.00", myReceipt.getFormattedTotal());
    }

    @Test
    public void testEmptyItemList(){

        final StoreRegister myStoreRegister = new StoreRegister();
        myStoreRegister.loadInventory(new File("happy-path.csv"));

        final Receipt myReceipt = myStoreRegister.checkoutOrder(new ArrayList<String>());

        Assert.assertNotNull(myReceipt.getOrderedItems());
        Assert.assertEquals(0, myReceipt.getOrderedItems().size());
        Assert.assertEquals("$0.00", myReceipt.getFormattedTotal());
    }
    @Test
    public void testOneItemList(){

        final StoreRegister myStoreRegister = new StoreRegister();
        myStoreRegister.loadInventory(new File("happy-path.csv"));

        final List<String> myTestItems = new ArrayList<String>() {
            {
                add("PC1033");
            }
        };
        final Receipt myReceipt = myStoreRegister.checkoutOrder(myTestItems);

        final List<String> myExpectedArray = new ArrayList<String>() {{

            add("PC1033");
        }};

        Assert.assertArrayEquals(myExpectedArray.toArray() ,myReceipt.getOrderedItems().toArray());
        Assert.assertEquals("$19.99", myReceipt.getFormattedTotal());
    }

    @Test
    public void testNullFile(){


        final StoreRegister myStoreRegister = new StoreRegister();
        myStoreRegister.loadInventory(new File("happy-path.csv"));

        final List<String> myTestItems = new ArrayList<String>() {
            {
                add("PC1033");
            }
        };
        final Receipt myReceipt = myStoreRegister.checkoutOrder(myTestItems);

        final List<String> myExpectedArray = new ArrayList<String>() {{

            add("PC1033");
        }};

        Assert.assertArrayEquals(myExpectedArray.toArray() ,myReceipt.getOrderedItems().toArray());
        Assert.assertEquals("$19.99", myReceipt.getFormattedTotal());
    }

    @Test
    public void testOneRecordFile(){

        final StoreRegister myStoreRegister = new StoreRegister();
        myStoreRegister.loadInventory(new File("onerecord.csv"));

        final List<String> myTestItems = new ArrayList<String>() {
            {
                add("PC1033");
            }
        };
        final Receipt myReceipt = myStoreRegister.checkoutOrder(myTestItems);

        final List<String> myExpectedArray = new ArrayList<String>() {{
            add("PC1033");
        }};

        Assert.assertArrayEquals(myExpectedArray.toArray() ,myReceipt.getOrderedItems().toArray());
        Assert.assertEquals("$19.99", myReceipt.getFormattedTotal());

    }

    @Test
    public void testInvalidFile(){
        try {
            final StoreRegister myStoreRegister = new StoreRegister();
            myStoreRegister.loadInventory(new File("invalid.csv"));

            final List<String> myTestItems = new ArrayList<String>() {
                {
                    add("PC1033");
                }
            };
            final Receipt myReceipt = myStoreRegister.checkoutOrder(myTestItems);

            Assert.fail();
        } catch(RuntimeException re) {
            Assert.assertEquals(CSVFile.WRONG_NUMBER_COLUMN, re.getMessage());
        } catch(Exception e){
            Assert.fail();
        }
    }

    @Test
    public void testFormattedTotalWithCommas(){

        final StoreRegister myStoreRegister = new StoreRegister();
        myStoreRegister.loadInventory(new File("largvalue.csv"));

        final List<String> myTestItems = new ArrayList<String>() {
            {
                add("PC1033");
                add("GenericMotherboard");
                add("Mouse");
                add("LCD");
            }
        };
        final Receipt myReceipt = myStoreRegister.checkoutOrder(myTestItems);

        final List<String> myExpectedArray = new ArrayList<String>() {{
            add("GenericMotherboard");
            add("LCD");
            add("Mouse");
            add("PC1033");
        }};

        Assert.assertArrayEquals(myExpectedArray.toArray() ,myReceipt.getOrderedItems().toArray());
        Assert.assertEquals("$738.98", myReceipt.getFormattedTotal());

    }

    @Test
    public void testMissingInventoryItems(){
        final StoreRegister myStoreRegister = new StoreRegister();
        myStoreRegister.loadInventory(new File("empty.csv"));

        final List<String> myTestItems = new ArrayList<String>() {
            {
                add("PC1033");
            }
        };

        try {
            myStoreRegister.checkoutOrder(myTestItems);
            Assert.fail();
        } catch(RuntimeException re) {
            Assert.assertEquals(StoreRegister.INVENTORY_ITEM_NOT_FOUND_BEGIN+ "PC1033"
                    + StoreRegister.INVENTORY_ITEM_NOT_FOUND_END, re.getMessage());
        } catch(Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testFileWithSpecialCharacters(){
        Assert.fail();
    }
}