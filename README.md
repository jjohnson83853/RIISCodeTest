Testing
-------
To run the test task (after you have installed gradle):

gradle test

I am not sure if my test a thorough enough. I could write a ton more tests but it seems like on unreasonable amount of time to do that for a coding test. Normally I would use a code coverage tool to find branches that need testing.

Building
--------
To build:

gradle build

This creates: ./build/libs/CodeTest.jar

This does not have a main. It is driven by unit tests.

Assumptions
-----------
# Dollar amounts are not stored in currency format in the inventory file.
# If there are more or less then three columns the inventory files is invalid.
# A dollar amount of 1.2 is invalid (and not considered 1.20

