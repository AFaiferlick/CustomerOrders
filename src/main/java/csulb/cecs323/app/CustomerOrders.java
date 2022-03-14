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

      System.out.println(currentDate);

      System.out.println();
      isValid = false;

      int orderDateChoice = 0;
      System.out.println("Enter Order Date: \n1. New Order \n2. Present");
      orderDateChoice = getInteger(1, 2);
      boolean valid = false;

      if(orderDateChoice == 1)
      {
         System.out.println("Order Date Set As:\t");
         System.out.println(currentDate);
         System.out.println("========================================\n");
      }
      else if(orderDateChoice == 2)
      {
         int userDate = 0;
         System.out.println("Enter Order Date Year (2000-2022): \n");
         valid = false;
         while (!valid) {
            if (in.hasNextInt()) {
               userDate = in.nextInt();
               if (userDate <= 2022 && userDate >= 2000) {
                  valid = true;
               } else {
                  System.out.println("Invalid Range.");
               }
            } else {
               in.next();
               System.out.println("Invalid Input.");
            }
         }


      }
      else
      {
         System.out.println("Error.");
      }

      System.out.print(" +=== Available Products ===+\n");
      for (int i=0; i<products.size(); i++) {  //print product menu
         System.out.println(i+1 + "\t" + products.get(i).getProd_name());
      }

      System.out.println("========================================");
      int userChoice = 0;
      do { //input validation loop
         System.out.print("\nEnter sequence number of desired product: ");
         inputProduct = in.nextInt(); //take user input in form of int
         String productChoiceName = "";
         if (inputProduct > 0 && inputProduct <= products.size()){
            isValid = true;
            productChoice = products.get(inputProduct-1); //-1 because sequence numbers started at 1 instead of 0
            productChoiceName = productChoice.getProd_name();

            System.out.println("You chose product " + inputProduct + ", " + productChoiceName);
            System.out.println("How many of this product do you wish to purchase: ");
            productAmount = in.nextInt();
            /*System.out.println("You selected " + productAmount + " of " + productChoiceName + "\nIs this correct (y/n)?");
            confirm = in.next().toUpperCase();
            if (confirm.equals("Y")){*/
               if (productAmount <= productChoice.getUnits_in_stock() ){
                  // add into cart.
                  System.out.println(productChoiceName + " [Quantity: " + productAmount + "] has been added to your cart.");
               }else{
                  System.out.println("Sorry! We don't have enough stock of product " + productChoiceName + "!");
                  System.out.println("Would you like to:\n1. Remove " + productChoiceName + " from your order\n" +
                          "2. Abort the order");

                  userChoice = getInteger(1, 2);
                  switch (userChoice) {
                     case 1: System.out.println(productChoiceName + " has been removed from your order."); break;
                     case 2: System.out.println("The current order has been aborted."); break;
                  }
               }
            /*} else {
               isValid = false;
            }*/
         } else {
            System.out.println("Invalid product! Try again.");
         }
      } while (!isValid);

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

   public List<Products> getProductList () {
      List<Products> products = this.entityManager.createNamedQuery("ReturnProductList",
              Products.class).getResultList();
      return products;
   }

   public Customers getCustomerName (String UPC) {
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

   public List<Customers> getCustomerNameList ()
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
