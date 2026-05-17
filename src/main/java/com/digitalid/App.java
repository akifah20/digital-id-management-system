package com.digitalid;

import java.util.Scanner;

import com.digitalid.facade.DigitalIDFacade;
import com.digitalid.presentation.ConsoleUI;

public class App {
    public static void main(String[] args) {
        DigitalIDFacade facade = new DigitalIDFacade();
        Scanner scanner = new Scanner(System.in);
        ConsoleUI ui = new ConsoleUI(facade, scanner);
        ui.start();

    }
}
