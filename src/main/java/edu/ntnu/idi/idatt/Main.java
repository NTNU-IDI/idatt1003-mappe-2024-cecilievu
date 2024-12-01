package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.views.UserInterface;

public class Main {
    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        ui.init();
        ui.start();
    }
}

