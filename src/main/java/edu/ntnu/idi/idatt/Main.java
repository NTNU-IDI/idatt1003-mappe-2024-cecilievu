package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.views.UserInterface;

/**
 * Static main method to run the fridge and cookbook application.
 */
public class Main {

  /**
   * Main method that runs the application.
   *
   * @param args the arguments for the main method
   */
  public static void main(String[] args) {
    UserInterface ui = new UserInterface();
    ui.init();
    ui.start();
  }
}

