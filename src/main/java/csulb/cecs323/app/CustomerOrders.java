/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 *
 *  This code is distributed to CSULB students in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, other than educational.
 *
 *  2018 Alvaro Monge <alvaro.monge@csulb.edu>
 *
 */

package csulb.cecs323.app;

// Import all of the entity classes that we have written for this application.
import csulb.cecs323.model.*;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.Scanner;

/**
 * A simple application to demonstrate how to persist an object in JPA.
 * <p>
 * This is for demonstration and educational purposes only.
 * </p>
 * <p>
 *     Originally provided by Dr. Alvaro Monge of CSULB, and subsequently modified by Dave Brown.
 * </p>
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 *
 *  This code is distributed to CSULB students in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, other than educational.
 *
 *  2021 David Brown <david.brown@csulb.edu>
 *
 */
public class CustomerOrders {
   /**
    * You will likely need the entityManager in a great many functions throughout your application.
    * Rather than make this a global variable, we will make it an instance variable within the CustomerOrders
    * class, and create an instance of CustomerOrders in the main.
    */
   private EntityManager entityManager;

   /**
    * The Logger can easily be configured to log to a file, rather than, or in addition to, the console.
    * We use it because it is easy to control how much or how little logging gets done without having to
    * go through the application and comment out/uncomment code and run the risk of introducing a bug.
    * Here also, we want to make sure that the one Logger instance is readily available throughout the
    * application, without resorting to creating a global variable.
    */
   private static final Logger LOGGER = Logger.getLogger(CustomerOrders.class.getName());

   /**
    * The constructor for the CustomerOrders class.  All that it does is stash the provided EntityManager
    * for use later in the application.
    * @param manager    The EntityManager that we will use.
    */
   public CustomerOrders(EntityManager manager) {
      this.entityManager = manager;
   }

   public static void main(String[] args) {
      LOGGER.fine("Creating EntityManagerFactory and EntityManager");
      EntityManagerFactory factory = Persistence.createEntityManagerFactory("CustomerOrders");
      EntityManager manager = factory.createEntityManager();
      // Create an instance of CustomerOrders and store our new EntityManager as an instance variable.
      CustomerOrders customerOrders = new CustomerOrders(manager);

      // Any changes to the database need to be done within a transaction.
      // See: https://en.wikibooks.org/wiki/Java_Persistence/Transactions

      LOGGER.fine("Begin of Transaction");
      EntityTransaction tx = manager.getTransaction();

      tx.begin();
      // List of Products that I want to persist.  I could just as easily done this with the seed-data.sql
      List <Products> products = new ArrayList<Products>();
      List <Customers> customers = new ArrayList<Customers>();
      // Load up my List with the Entities that I want to persist.  Note, this does not put them
      // into the database.
      //products.add(new Products("076174517163", "16 oz. Hickory Hammer", "Stanely Tools", "00001", 9.97, 50));
      // Create the list of owners in the database.
      //customerOrders.createEntity (products);

      // Commit the changes so that the new data persists and is visible to other users.
      tx.commit();
      LOGGER.fine("End of Transaction");


      Scanner in = new Scanner(System.in);
      boolean isValid = false;
      int inputProduct = 0;
      int productAmount = 0;
      int inputCustomerName= 0;
      List<Products> productsBought = new ArrayList<Products>();  //for use in product column
      List<Integer> productsQuantities = new ArrayList<Integer>();  //quantity column
      List<Double> productsPrices = new ArrayList<Double>();  //unit_sale_price column
      Date orderDate = null;  //order_date column
      String confirm;
      Date currentDate = new Date();
      LocalDateTime current = LocalDateTime.now();
      DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
      String formattedDate = current.format(formatTime);

      products = customerOrders.getProductList(); //set products list equal to the retrieved product list
      customerOrders.createEntity (products); //persist all products
      Products productChoice = null; //create variable to store customer's product choice

      customers = customerOrders.getCustomerNameList();
      customerOrders.createEntity(customers);
      Customers customerNameChoice = null;

      System.out.print("Commencing Order Application...\n");

      System.out.print("Available Customer Names to Place Order:\n");
      for (int i=0; i<customers.size(); i++) {  //print product menu
         System.out.println(i+1 + "\t" + customers.get(i).getLast_name() + " " + customers.get(i).getFirst_name());
      }

      System.out.print("Enter the sequence number of desired customer name: ");
      inputCustomerName = getInteger(1, customers.size());
      customerNameChoice = customers.get(inputCustomerName - 1); //-1 because sequence numbers started at 1 instead of 0
      System.out.println("You chose Customer " + inputCustomerName + ", " + " " +
              customers.get(inputCustomerName - 1).getFirst_name() + " " +
              customers.get(inputCustomerName - 1).getLast_name());

      System.out.println();
      isValid = false;

      int orderDateChoice = 0;
      System.out.print("Enter Order Date: \n1. Default to Current \n2. Enter Date\n");
      boolean valid = false;
      orderDateChoice = getInteger(1, 2);

      if(orderDateChoice == 1)
      {
         System.out.println("Order Date Set As: " + currentDate);
         orderDate = currentDate;
      }
      else if(orderDateChoice == 2)
      {
         int orderYear = 0;
         System.out.println("Enter Order Date Year (2000-" + (currentDate.getYear()+1900) + "): ");
         valid = false;
         while (!valid) {
            if (in.hasNextInt()) {
               orderYear = in.nextInt();
               if (orderYear <= currentDate.getYear()+1900 && orderYear >= 2000) { //check that year is within 2000-2022 inclusive
                  valid = true;
               } else if(orderYear-1900 > currentDate.getYear()) { //check that orderYear isn't greater than current year. orderYear subtracts 1900 from current year
                  System.out.println("Order can not be placed in the future.");
               }
               else { //otherwise, invalid range
                  System.out.println("Invalid Range.");
               }
            } else { //case of non-integer input
               in.next();
               System.out.println("Invalid Input.");
            }
         }

         int orderMonth = 0;
         System.out.println("Enter Order Month (1-12): ");
         valid = false;
         while (!valid) {
            if (in.hasNextInt()) {
               orderMonth = in.nextInt();
               if (orderYear == (currentDate.getYear()+1900)) { //if the order year is current real-world year
                  if(orderMonth <= (currentDate.getMonth() + 1) && orderMonth >= 1) //ensure it isn't in future
                  {
                     valid = true;
                  } else if(orderMonth > 12 || orderMonth < 1) { //check for months out of range
                     System.out.println("Invalid Range.");
                  }
                  else {  //otherwise, order is in future (between current real-world month and 12)
                     System.out.println("Order can not be placed in the future.");
                  }
               } else if (orderMonth <= 12 && orderMonth >= 1) { //if not 2022, validate month
                  valid = true;
               } else {
                  System.out.println("Invalid Range.");
               }
            } else {
               in.next();
               System.out.println("Invalid Input.");
            }
         }

         int orderDay = 0;
         System.out.println("Enter Order Day (1-31): ");
         valid = false;
         while (!valid) {
            if (in.hasNextInt()) {
               orderDay = in.nextInt();
               if (orderYear == (currentDate.getYear()+1900)) { //if the order year is current real-world year
                  if (orderMonth == (currentDate.getMonth() + 1)) //check if order month is current real month
                  {
                     if(orderDay <= 14 && orderDay >= 1) //ensure it isn't in future. had to hardcode day of month because couldn't find a relevant method
                     {
                        valid = true;
                     } else if(orderDay < 1 || orderDay > 31) { //not in future but not valid range
                        System.out.println("Invalid Range.");
                     } else { //not valid outside valid range, but not inside valid range, so in future
                        System.out.println("Order can not be placed in the future.");
                     }
                  } else if(orderDay <= 31 && orderDay >= 1) { //in case of current year but not current month
                     valid = true;
                  } else {
                     System.out.println("Invalid Range.");
                  }
               } else if(orderDay <= 31 && orderDay >= 1) { //in case of not current year
                  valid = true;
               } else {
                  System.out.println("Invalid Range.");
               }
            } else {
               in.next();
               System.out.println("Invalid Input.");
            }
         }
         orderDate = new Date(orderYear, (orderMonth - 1), orderDay);
      }
      else
      {
         System.out.println("Error.");
      }


      System.out.print("\n +=== Available Products ===+\n");
      for (int i=0; i<products.size(); i++) {  //print product menu
         System.out.println(i+1 + "\t" + products.get(i).getProd_name());
      }

      System.out.println("========================================");
      int userChoice = 0;
      int moreProduct = 2;
      do {
         do { //input validation loop
            System.out.print("\nEnter sequence number of desired product: ");
            if (in.hasNextInt()) { //check against non-integer input
               inputProduct = in.nextInt(); //take user input in form of int
               String productChoiceName = "";
               if (inputProduct > 0 && inputProduct <= products.size()) {
                  isValid = true;
                  productChoice = products.get(inputProduct - 1); //-1 because sequence numbers started at 1 instead of 0
                  productChoiceName = productChoice.getProd_name();

                  System.out.println("You chose product " + inputProduct + ", " + productChoiceName);
                  System.out.println("How many of this product do you wish to purchase: ");
                  productAmount = in.nextInt();
               /*System.out.println("You selected " + productAmount + " of " + productChoiceName + "\nIs this correct (y/n)?");
               confirm = in.next().toUpperCase();
               if (confirm.equals("Y")){*/
                  if (productAmount <= productChoice.getUnits_in_stock()) { // Checks if there is enough product to complete purchase
                     productsBought.add(productChoice);
                     productsQuantities.add(0, productAmount);
                     productsPrices.add(0, productChoice.getUnit_list_price());
                     System.out.println(productChoiceName + " [Quantity: " + productAmount + "] has been added to your cart.");
                  } else {
                     System.out.println("Sorry! We don't have enough stock of product " + productChoiceName +
                             "! We only have " + productChoice.getUnits_in_stock() + " left.");
                     //System.out.println("Would you like to:\n1. Remove " + productChoiceName + " from your order\n" +
                     //"2. Abort the order");

                     //userChoice = getInteger(1, 2);
                     //switch (userChoice) {
                     //case 1: System.out.println(productChoiceName + " has been removed from your order."); break;
                     //case 2: System.out.println("The current order has been aborted."); break;
                     //}
                  }
               } else {
                  System.out.println("Invalid product! Try again.");
               }
            } else {
               in.next();
               System.out.println("Invalid Input.");
            }
         } while (!isValid);
         System.out.println("Would you like to:\n 1. Order another product\n 2. Finalize order");
         moreProduct = getInteger(1, 2);
      } while(moreProduct == 1);

      //Order total
      double orderTotal = 0;
      for (int i = 0; i < productsQuantities.size(); i++) {
         orderTotal += (productsQuantities.get(i) * productsPrices.get(i));
      }
      System.out.println("Your order total is $" + orderTotal); //part 3di

      System.out.println("Would you like to:\n 1. Place your order\n 2. Abort your order");
      int finalizeOrder = getInteger(1, 2);
      if (finalizeOrder == 1) {
         //add rows in Order_Lines
         System.out.println(" == Thank you for your Purchase! == ");
         System.out.println("Quantity\tProduct");
         System.out.println(productAmount + "x\t" + productChoice.getProd_name());

         List<Order_lines> order_linesList = new ArrayList<Order_lines>();
         LocalDateTime orderDateTime = LocalDateTime.of(orderDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), current.toLocalTime());
         for (int i = 0; i < productsBought.size(); i++) { //loop for adding info to order_lines list
            order_linesList.add(0, new Order_lines(
                    new Orders(customerNameChoice, orderDateTime, productsBought.get(i).getMfgr()),
                    productsBought.get(i), productsQuantities.get(i), productsPrices.get(i)));
         }
         customerOrders.createEntity(order_linesList);  //create order_lines objects in database
         //change product quantities
         productChoice.setUnits_in_stock(productChoice.getUnits_in_stock() - productAmount); //implementation quesitonable
      } else { //if they don't want to place order, they want to abort it
         //abort transaction
         System.out.println(" === Sorry to see you go! Thank You for shopping with us === \n\n\n");
      }

      System.out.println();
   } // End of the main method

   /**
    * Create and persist a list of objects to the database.
    * @param entities   The list of entities to persist.  These can be any object that has been
    *                   properly annotated in JPA and marked as "persistable."  I specifically
    *                   used a Java generic so that I did not have to write this over and over.
    */
   public <E> void createEntity(List <E> entities) {
      for (E next : entities) {
         LOGGER.info("Persisting: " + next);
         // Use the CustomerOrders entityManager instance variable to get our EntityManager.
         this.entityManager.persist(next);
      }

      // The auto generated ID (if present) is not passed in to the constructor since JPA will
      // generate a value.  So the previous for loop will not show a value for the ID.  But
      // now that the Entity has been persisted, JPA has generated the ID and filled that in.
      for (E next : entities) {
         LOGGER.info("Persisted object after flush (non-null id): " + next);
      }
   } // End of createEntity member method

   /**
    * Think of this as a simple map from a String to an instance of Products that has the
    * same name, as the string that you pass in.  To create a new Cars instance, you need to pass
    * in an instance of Products to satisfy the foreign key constraint, not just a string
    * representing the name of the style.
    * @param UPC        The name of the product that you are looking for.
    * @return           The Products instance corresponding to that UPC.
    */
   public Products getProduct (String UPC) {
      // Run the native query that we defined in the Products entity to find the right style.
      List<Products> products = this.entityManager.createNamedQuery("ReturnProduct",
              Products.class).setParameter(1, UPC).getResultList();
      if (products.size() == 0) {
         // Invalid style name passed in.
         return null;
      } else {
         // Return the style object that they asked for.
         return products.get(0);
      }
   }// End of the getProduct method

   public List<Products> getProductList () {  //for use in PART 2
      List<Products> products = this.entityManager.createNamedQuery("ReturnProductList",
              Products.class).getResultList();
      return products;
   }

   public Customers getCustomerName (String UPC) { //for use in PART 1
      // Run the native query that we defined in the Customers entity to find the right style.
      List<Customers> customers = this.entityManager.createNamedQuery("ReturnCustomerName",
              Customers.class).setParameter(1, UPC).getResultList();
      if (customers.size() == 0) {
         // Invalid style name passed in.
         return null;
      } else {
         // Return the style object that they asked for.
         return customers.get(0);
      }
   }// End of the getStyle method

   public List<Customers> getCustomerNameList () //for use in PART 1
   {
      List<Customers> customers = this.entityManager.createNamedQuery("ReturnCustomerNameList",
              Customers.class).getResultList();
      return customers;
   } // End of getCustomerNameList method

   /** Returns an integer between the specified minimum and maximum range (inclusive).
    *  Validates user input
    *  @param minRange     The minimum integer that the user can input.
    *  @param maxRange     The maximum integer that the user can input.
    *  @return An integer between the given range.
    */
   public static int getInteger(int minRange, int maxRange) {
      boolean isValid = false;
      int userInput = 0;
      Scanner in = new Scanner(System.in);

      while (!isValid) {
         if (in.hasNextInt()) {
            userInput = in.nextInt();
            if (userInput >= minRange && userInput <= maxRange) {
               isValid = true;

            } else {
               System.out.println("Invalid range. Try again!");
            }
         } else {
            in.next(); // clear invalid input
            System.out.println("Invalid input! Try again.");
         }
      }
      return userInput;

   } // End of getInteger method

} // End of CustomerOrders class
