# Project Title

Simple overview of use/purpose.

## Vending Machine Coding Challenge

An implementation of a vending machine written in java.

### Dependencies

*org.json.simple

### Implementation

This program works by taking in a json file as input and converting it into a 2d array of vending machine items.
By default, the file input.json is initialized from 'resources'. When selecting an item, a json file can be entered instead to reinitialize the vending machine. If the file isnt found, or if the json file is improperly formatted, the initialization will fail.
By default this program can handle up to 25 rows and up to 9 columns, for a maximum of 255 total items. This limit was put in place to keep input to one letter and one number. The average vending machine holds around 64 items, so this implementation will work for most vending machines.
This implementation is based on interacting with a vending machine display, so if a product is out of stock it will notify the user and prompt them for another selection. When a valid item is selected, the user will input a number for payment, and when this amount is sufficient a message will print with their change.

### Limitations 

json.simple is a fairly old library and has some interesting quirks, I relegated its function to a single initialization class. This means while the machine is not initializing it does not edit the json files. As a consequence, if the machine is turned off and back on again it will be reset to the default json file. If I had more time I would implement json saving and loading, as well as an administrator menu where items could be edited directly.


## Authors

*Samuel Bourke
