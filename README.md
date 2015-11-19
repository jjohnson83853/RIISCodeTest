Testing
-------
To run the test task (after you have installed gradle):

gradle test


Building
--------
To build (and test at the same time):

gradle build

This creates: ./build/libs/RIISCodeTest.jar

This does not have a main. It is driven by unit tests.

Assumptions
-----------
 - If there are more or less then three columns the inventory files is invalid.
 - A dollar amount of 1.2 is invalid (and not considered 1.20)
 - The test coverage is not as high as it could be. I assumed that's not necesary because to get all the branch/line coverage would take take way to much work for a simple coding test. Currently line coverage is at 78%. StoreRegister is covered pretty good while CSVFile is not. In the CSVFile most of where I am lacking is in proxy functions of HashMap.

