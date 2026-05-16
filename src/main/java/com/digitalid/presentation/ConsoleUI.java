package com.digitalid.presentation;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.digitalid.domain.Organisation;
import com.digitalid.domain.OrganisationType;
import com.digitalid.facade.DigitalIDFacade;

//coords user interaction, menu display, + inputs.
//operations performed thru the facade
//presentation layer
public class ConsoleUI {

    private final DigitalIDFacade facade;
    private final Scanner scanner;
    private Organisation currentOrganisation;

    private static final List<Organisation> AVAILABLE_ORGANISATIONS = Arrays.asList(
            new Organisation("Home Office (Central Authority)", OrganisationType.CENTRAL_AUTHORITY),
            new Organisation("HMR (Tac Authority)", OrganisationType.TAX_AUTHORITY),
            new Organisation("DVLA (Driving License Authority)", OrganisationType.DRIVING_LICENCE_AUTHORITY),
            new Organisation("Halifax (Bank)", OrganisationType.BANK),
            new Organisation("AstraZeneca(Employer)", OrganisationType.EMPLOYER),
            new Organisation("Welfare Service", OrganisationType.WELFARE_SERVICE));

    public ConsoleUI(DigitalIDFacade facade, Scanner scanner) {
        if (facade == null) {
            throw new IllegalArgumentException("Facade neeeded. ");
        }
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner needed. ");
        }
        this.facade = facade;
        this.scanner = scanner;
    }

    public void start() {
        System.out.println();
        System.out.println("  WELCOME TO THE DIGITAL ID MANAGEMENT SYSTEM :) ");

        while (true) {
            if (currentOrganisation == null) {
                if (!showOrganisationMenu()) {
                    break; // user chose to exit
                }
            } else {
                if (!showActionMenu()) {
                    break; // user decides to exit menu
                }
            }
        }

        System.out.println("\n see ya ;) \n");
    }

    // SELECTION menu.
    private boolean showOrganisationMenu() {
        System.out.println("\nSelect your organisation:");
        for (int i = 0; i < AVAILABLE_ORGANISATIONS.size(); i++) {
            Organisation org = AVAILABLE_ORGANISATIONS.get(i);
            System.out.printf("%d. %s (%s)%n",
                    i + 1, org.getName(), org.getType().getDisplayName());
        }
        System.out.println("0. Exit");

        int option = readInt("option: ", 0, AVAILABLE_ORGANISATIONS.size());
        if (option == 0) {
            return false;
        }

        currentOrganisation = AVAILABLE_ORGANISATIONS.get(option - 1);
        System.out.printf("%nYou are logged in as: %s (%s)%n",
                currentOrganisation.getName(),
                currentOrganisation.getType().getDisplayName());
        return true;
    }

    // action menu for the current org.
    // Returns false if the user wants to exit.
    private boolean showActionMenu() {
        System.out.println("\nWhat would you like to do?");

        boolean canManage = currentOrganisation.canManageIdentities();
        if (canManage) {
            System.out.println("1. Create a new Digital ID");
            System.out.println("2. Update a Digital ID");
            System.out.println("3. Change Digital ID status");
        }
        System.out.println("4. Verify a Digital ID");
        System.out.println("5. List all Digital IDs");
        System.out.println("6. Switch organisation");
        System.out.println("0. Exit");

        int option = readInt("option: ", 0, 6);

        // to do: action handlers
        switch (option) {
            case 0:
                return false;
            case 6:
                currentOrganisation = null;
                return true;
            default:
                System.out.println("(.....)");
                return true;
        }
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                throw new IllegalStateException("Input stream closed");
            }
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value < min || value > max) {
                    System.out.printf("Enter a number between %d and %d.%n", min, max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }
}
