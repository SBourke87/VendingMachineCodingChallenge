package vendingmachineproject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Double.parseDouble;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Scanner;
import org.json.simple.*;
import org.json.simple.parser.*;



public class VendingMachineProject  {
    static VendingItem[][] VendingArray;
    static int[] currentSelection = new int[2];
    final static String defaultPath = ".\\src\\resources\\input.json";
    
    VendingMachineProject(String path) throws IOException, FileNotFoundException, ParseException {
        VendingArray = init(path);
    }
    VendingMachineProject() throws IOException, FileNotFoundException, ParseException {
        VendingArray = init(".\\src\\resources\\input.json");
    }
    
    public static void main(String[] args) throws IOException, FileNotFoundException, ParseException, ParseException, ParseException, ParseException {
            //Initializes vending machine with default path
            //Go to selection input
            getSelectionInput();
    }
    static void getSelectionInput() throws IOException, FileNotFoundException, FileNotFoundException, ParseException, ParseException {
        while(true) {
         Scanner scanInput = new Scanner(System.in);
         System.out.println("Enter Selection, or path to a json file to configure machine");
         System.out.println("Enter a letter a - " + numberToLetter(VendingArray.length) + " followed by a number 1 - " + VendingArray[0].length);
         String input = scanInput.nextLine();
         //Here we will validate input, if input is valid go to payment
         //If input is invalid, explain why and restart the loop!
            if(validateInput(input)) {
                selectItem(VendingArray[currentSelection[0]][currentSelection[1]]);
            }
            //If input ends in .json
            if(input.length() > 5){
            if (input.contains("json")) {
                try {
                //Try to initialize it as the new machine
                init(input);
                }
                catch(FileNotFoundException e) {
                    System.out.println("JSON File Not Found!");
                }
            }
            }
         
        }
    }
    
    static void selectItem(VendingItem item) throws IOException, FileNotFoundException, ParseException, ParseException {
        Scanner cashScan = new Scanner(System.in);
        double cashIn = 0;
        NumberFormat toDollars = NumberFormat.getCurrencyInstance();
        String moneyPrice = toDollars.format(item.price);
        System.out.println("You have selected: " + item.name + "\n");
        System.out.println("Please insert" + moneyPrice);
        while(true) {
            try {
            cashIn += cashScan.nextDouble();
            }
            catch (Exception e) {
                System.out.println("Try again, input was not a number");
                cashScan.next();
            }
            System.out.println("Current Funds = " + toDollars.format(cashIn));
            if (cashIn >= item.price) {
                System.out.println("Item dispensed, enjoy!");
                item.amount--;
                System.out.println("Your change is: " + getChangeString(cashIn - item.price));
                getSelectionInput();
                
            }
            
        }
    }
    static Boolean validateFile(String s) {
        File temp = new File(s);
        return temp.exists();
    }
    
    static String getChangeString(double d) {
        //returns a string with change given
        int quarters, dimes, nickels, pennies;
        String s = "";
        d *= 100;
        quarters = (int) (d/25);
        d %= 25;
        dimes = (int)(d/10);
        d %= 10;
        nickels = (int) (d/5);
        d %= 5;
        pennies = (int) d;
        s += quarters + " Quarters " + dimes + " Dimes " + nickels + " Nickels and " + pennies + " Pennies.";
        return s;
    }
    

    
    static boolean validateInput(String s) {
        //The main purpose of this is to return whether the user input actully 
        //corresponds to a product in the machine.
        //The main goal here is to avoid out of bounds errors and make things easier
        //on other methods
        if(s.length() != 2){
            return false;
        }
        char[] nums = "123456789".toCharArray();
        boolean isValid = false;
        char[] input = s.toCharArray();
        int coord1 = letterToNumber(input[0]);
        //this checks if the input is acceptable
        boolean isCoord2Valid = false;
        for (char c : nums) {
            if (c == input[1]) {
            isCoord2Valid = true;
            break;
            }
        }
        if(coord1 == -1 || !isCoord2Valid) {
            return false;
            //This happens when coord1 is nonalphabetical or coord2 is not a number from 1-9
        }
        int coord2 = Integer.parseInt(String.valueOf(input[1])) - 1;        
        if(input.length > 2) {
            //This is for when the selection is too long
            System.out.println("Selection too long, please try again");
            return false;
        }
        if ((coord1 >= 0) && (coord1 < VendingArray.length) && (coord2 >= 0) && (coord2 < VendingArray[0].length)) {
        if (VendingArray[coord1][coord2] != null && VendingArray[coord1][coord2].amount > 0) {
                //This is activated when the input is valid
            currentSelection[0] = coord1;
            currentSelection[1] = coord2;
            return true;
        }
            else {
        //This will happen if the input is a valid slot in the machine, but the slot is empty or out of stock
                System.out.println("We're sorry, the selection you entered is currently out of stock");
                return false;
            }
        }
        return false;
    }
    
    static char numberToLetter(int x) {
        //returns letter corresponding to index
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        return alphabet[x - 1];
    }
    static int letterToNumber(char x) {
        //returns index of character in alphabet, returns -1 if input is non-alphabetical
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for(int y = 0; y < alphabet.length; y++) {
            if (x == alphabet[y]){
            return y;
        }   
    }        return -1;
    }  
    
    static Boolean isValidFormat(JSONObject object) {
        int numItems = 0;
        //This checks if the json file is in the proper format
        JSONArray items = (JSONArray) object.get("items");
        JSONObject config = (JSONObject) object.get("config");
        Iterator<JSONObject> listIterator = items.iterator();
        while(listIterator.hasNext()) {
            numItems++;
            listIterator.next();
        }
        //If rows and columns are not null
        if(config.get("rows") != null && config.get("columns") != null) {
            int numRows = ((Number) config.get("rows")).intValue();
            int numColumns = ( Integer.parseInt((String) config.get("columns")));
            //Check if rows can fit within alphabet and columns can fit in 1-9
            if(numRows > 0 && numRows < 26 && numColumns > 0 && numColumns < 10) {
                if(numItems <= numRows * numColumns) {
                    return true;
                }
            }
        }
        return false;
    }
   
    static VendingItem[][] init(String inputPath) throws FileNotFoundException, IOException, ParseException {
        /* This method is meant to take all relevant info from our json file 
           and turn it into a more manageable 2d array of VendingItems
           the reason behind this is that simpleJSON arrays difficult to iterate through
            This will also help keep our main method easily readable
        */
        int numRows;
        int numColumns;
        //Parser and filereader for our input file
        JSONParser parser = new JSONParser();
        /*TODO Allow custom json file input*/
        FileReader readJson = new FileReader(inputPath);
        Object data = parser.parse(readJson);
        //We can pull  config and  items from this base json file
        JSONObject vendData;

        vendData = (JSONObject)data;
        //If the json is in invalid format
        if(!isValidFormat(vendData)) {
            System.out.println("JSON file has invalid formatting, make sure rows do not exceed 25 and columns do not exceed 9");
            System.out.println("The default json file has been initialized");
            init(defaultPath);
            getSelectionInput();
        }
        //this initializes our  configuration data, setting the number of rows and columns
        JSONObject config = (JSONObject)vendData.get("config");
        numRows = ((Number) config.get("rows")).intValue();
        numColumns = ( Integer.parseInt((String) config.get("columns")));
        //Create new 2d array of vending machine items
        VendingItem[][] construct = new VendingItem[numRows][numColumns];
        //Now we will create our 2d array with numRows rows and numColumns columns
        JSONArray items = (JSONArray)vendData.get("items");
        //Iterate through items JSONArray, mapping all items to 2d array
        
        Iterator<JSONObject> listIterator = items.iterator();
        

        for(int y = 0; y < numRows; y++) {
            for(int z = 0; z < numColumns; z++) {
                VendingItem tempItem = new VendingItem();
                JSONObject temp = listIterator.next();
                //Sanitize price to deposit as a double, we will handle price formatting elsewhere
                String str = (String) temp.get("price");
                str = str.replaceAll("[^\\d.]", "");
      
                //Assign values in item array
                tempItem.name = (String)temp.get("name");
                tempItem.amount = ((Number) temp.get("amount")).intValue();
                tempItem.price = parseDouble(str);
                
                construct[y][z] = tempItem;
                if(!listIterator.hasNext()){
                        break;
                    }
                    }
                    if(!listIterator.hasNext()){
                        break;
                    }
            }
        return construct;
    }
}
