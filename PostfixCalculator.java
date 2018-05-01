// Program Title: Postfixulator 
// Programmer: Alan Bennett
// Description: Calculates basic math problems that are in postfix notation
// Creation date: 11/9/2015
// Last modification date: 11/16/2015

import java.util.Stack;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PostfixCalculator extends Application 
{
    static private Text calcMsg; // Declare calcMsg
    static private Stack<String> stack; // Declare string stack
    
    public static void main(String[] args) 
    {
        launch(args); 
    }
    
    @Override
    public void start(Stage primaryStage) 
    {
        stack = new Stack(); // Initialize stack
        
        /* GridPane for organizing buttons in */
        GridPane calcPane = new GridPane(); // Make a new GridPane
        calcPane.setHgap(10); // Set the horizontal gap between cells as 10
        calcPane.setVgap(10); // Set the vertical gap between cells as 10
        calcPane.setStyle("-fx-background-color: lightgreen"); // Set the background color as light green
        
        /* Creating buttons 1 through 9 */
        for (int j = 1, count = 9; j < 5; j++) // j = row, count = current number
        {
            for (int i = 0, num = 3; i < 3; i++, num -= 2, count--) // i = column
            {   
                if (count >= 1) // If count is greater than or equal to 1
                    createButton(calcPane,Integer.toString(count), i + num, j); // Create the button
            }
        }
        
        /* Create the rest of the buttons */
        createButton(calcPane, "/", 4, 1); // Creates division button
        createButton(calcPane, "*", 4, 2); // Creates multiplication button
        createButton(calcPane, "-", 4, 3); // Creates subtraction button
        createButton(calcPane, "+", 4, 4); // Creates addition button 
        createButton(calcPane, "0", 2, 4); // Creates zero digit button
        createButton(calcPane, "C", 3, 4); // Creates clear button
        createButton(calcPane, "_", 4, 5); // Creates space button
        
        /* Create calculate button */
        Button button = new Button("Calculate"); 
        button.setPrefWidth(110);
        GridPane.setColumnSpan(button,3); // Span the button over 3 columns
        calcPane.add(button, 1, 5); // Adds it to the 1st column and the fifth row
        button.setOnMouseClicked (e -> { calculate(); } ); // If clicked go to calculate method
        
        /* Create calculator message */
        calcMsg = new Text(); 
        calcMsg.setFont(new Font(13.5)); // Set the font size to 13.5
        calcMsg.setFill(Color.WHITE); // Make the font color white
        
        /* Create a HBox to store calcMsg in */
        HBox header = new HBox(); 
        header.setStyle("-fx-background-color: gray"); // Make header's background color gray
        header.setPadding(new Insets(5)); // Set the padding to 5
        header.getChildren().add(calcMsg); // Add calcMsg to header
        
        /* Create a BorderPane to store calcPane and header in */
        BorderPane calcBorder = new BorderPane(); 
        calcBorder.setTop(header); // Add the header to top section of calcBorder
        calcBorder.setCenter(calcPane); // Add calcPane to the center section of calcBorder
       
        /* Create the scene and set the primaryStage */
        Scene calcScene = new Scene(calcBorder); // Place calcBorder in the scene
        
        primaryStage.setMinWidth(170); // Set minimum width to 170 pixels
        primaryStage.setMaxWidth(300); // Set the maximum width to 1000 pixels
        primaryStage.setMinHeight(245); // Set the minimum height to 245 pixels
        primaryStage.setMaxHeight(245); // Set the maximum height to 245 pixels
        primaryStage.setScene(calcScene); // Place the scene in the stage
        primaryStage.setTitle("Postfixulator"); // Title of program
        primaryStage.show(); // Display primaryStage
    }
    
    public static void createButton(GridPane calcPane, String symbol, int c, int r)
    {
        Button button = new Button(symbol); // Creates a button with its label as symbol
        calcPane.add(button, c, r); // Add button to the cth column and the rth row of calcPane
        button.setPrefWidth(30); // Set preferred width to 30
        button.setOnMouseClicked(e -> { buttonClick(symbol); }); // If clicked, go to buttonClick method
    }
    
    public static void buttonClick(String symbol) 
    {
        if (calcMsg.getText() != null && !calcMsg.getText().isEmpty()) // if there is an error message displayed
        { 
           if (calcMsg.getText().charAt(0) == 'E') { calcMsg.setText(null); } // clear it
        }
        
        if ("_".equals(symbol)) { symbol = " "; } // If symbol = "_", make it a blank space
        
        if (!"C".equals(symbol)) // If the symbol is not equal to "C"
        {
            String newMsg = calcMsg.getText() + symbol; // Add the symbol to the newMsg string
            calcMsg.setText(newMsg); // Set calcMsg as the newMsg string
        }
        
        else // Otherwise, the button pressed is C
        {   
            calcMsg.setText(null); // Clear out calcMsg by setting it to null
            
            while(stack.empty() == false) // Loop until stack is empty
                stack.pop(); // Pop the top element off of stack
        }
    }
    
    public static void calculate()
    {
        boolean operators = false, numbers = false; // Make operators and numbers false by default
        String stringArray[] = calcMsg.getText().split(" "); // Split the calcMsg whenever a blank space 
                                                             // is encountered and put it in an array
        
        for (String stringArray1 : stringArray) // Traverses whole array
        {     
            if("ERR:".equals(stringArray1)) { return; } // if error message, exit method 
            
            else if (!"+".equals(stringArray1) && !"-".equals(stringArray1) 
                && !"*".equals(stringArray1) && !"/".equals(stringArray1)) // if an operands 
            {
                numbers = true; // Make numbers true
                
                try // Try for exceptions
                {
                    int check = Integer.parseInt(stringArray1); // Convert string to integer
                }
                catch (NumberFormatException e) // Catch number format exceptions
                {
                    numbers = false; // Make numbers false
                }
                
                stack.push(stringArray1); // push current element onto the stack
            }
            
            else // Otherwise, it is an operator
            {
                operators = true; // Set operators to true
               
                if (numbers == false) // If there are no numbers
                { 
                    calcMsg.setText("ERR: No numbers"); // Display error
                    return; // Exit out of method
                } 
                
                try // Try for exceptions
                {
                    Stack<String> stackCopy = (Stack<String>) stack.clone(); // Make stackCopy a copy of stack
                    int num1 = Integer.valueOf(stackCopy.pop()); // Make num1 equal to value of element popped off
                    int num2 = Integer.valueOf(stackCopy.pop()); // Make num2 equal to value of element popped off
                }
                catch (java.util.EmptyStackException e) // catch empty stack exceptions
                {
                    calcMsg.setText("ERR: Invalid Calculation"); // Display error message
                    return; // Exit out of method
                }
                    
                int num1 = Integer.valueOf(stack.pop()); // Make num1 equal to value of element popped off 
                int num2 = Integer.valueOf(stack.pop()); // Make num2 equal to value of element popped off
                    
                switch (stringArray1) 
                {
                    case "+": // If current element is addition operator
                        stack.push(Integer.toString(num1 + num2)); // Perform addition of num1 and num2
                        break; // Break out of switch statement
                    case "-": // If current element is subtraction operator
                        stack.push(Integer.toString(num2 - num1)); // Perform subtraction of num2 and num1
                        break; // Break out of switch statement
                    case "*": // If current element is multiplication operator
                        stack.push(Integer.toString(num1 * num2)); // Perform multplication of num1 and num2
                        break; // Break out of switch statement
                    case "/": // If current element is division operator
                        if (num1 == 0) // If denominator is zero
                            calcMsg.setText("ERR: Undefined"); // Display error message
                        else // Otherwise
                            stack.push(Integer.toString(num2 / num1)); // Perform division of num2 and num1
                        break; // Break out of switch statement
                }
            }
        }
        
        if (stack.empty() == false) // if stack is not empty
        {
            if (operators == false && numbers == false) { calcMsg.setText("ERR: Invalid Calculation");} // if no numbers or operators, display error
            else if(operators == true) { calcMsg.setText(stack.pop()); } // If there are operators, pop the top element of stack and display it
            else { calcMsg.setText("ERR: No operator(s)"); } // Otherwise, display error
        }
    }
}
