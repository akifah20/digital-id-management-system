package com.digitalid.presentation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.Organisation;
import com.digitalid.domain.OrganisationType;
import com.digitalid.domain.Status;
import com.digitalid.domain.VerificationResponse;
import com.digitalid.exception.AuthorizationException;
import com.digitalid.exception.IdentityNotFoundException;
import com.digitalid.exception.ValidationException;
import com.digitalid.facade.DigitalIDFacade;

//coords user interaction, menu display, + inputs.
//operations performed thru the facade
//presentation layer
public class ConsoleUI {

    private final DigitalIDFacade facade;
    private final Scanner scanner;
    private Organisation currentOrganisation;

    private static final List<Organisation> AVAILABLE_ORGANISATIONS = Arrays.asList(
            new Organisation("Home Office", OrganisationType.CENTRAL_AUTHORITY),
            new Organisation("HMRC", OrganisationType.TAX_AUTHORITY),
            new Organisation("DVLA", OrganisationType.DRIVING_LICENCE_AUTHORITY),
            new Organisation("Halifax", OrganisationType.BANK),
            new Organisation("AstraZeneca", OrganisationType.EMPLOYER),
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

        // No org selected tHEN show the org picker
        // Org selected THEN show the action menu
        // if user enters false then it means they want to exit.
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

        System.out.println("\nsee ya :)\n");
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
            System.out.println("""

            1. Create a new Digital ID
            2. Update a Digital ID
            3. Change Digital ID
            4. Verify a Digital ID
            5. List all Digital IDs
            6. Switch organisation 
            0. Exit
                    
            """);
             

        int option = readInt("option: ", 0, 6);

        switch (option) {
            case 0:
                return false;
            case 1:
                if (canManage)
                    handleCreate();
                else
                    System.out.println("Not authorised.");
                return true;
            case 2:
                if (canManage)
                    handleUpdate();
                else
                    System.out.println("Not authorised.");
                return true;
            case 3:
                if (canManage)
                    handleChangeStatus();
                else
                    System.out.println("Not authorised.");
                return true;
            case 4:
                handleVerify();
                return true;
            case 5:
                handleList();
                return true;
            case 6:
                currentOrganisation = null;
                return true;
            default:
                System.out.println("Invalid option.");
                return true;
        }
    }

    private void handleCreate() {
        System.out.println("\n <3 Create a new Digital ID <3 ");
        try {
            String firstName = readLine("First name: ");
            String lastName = readLine("Last name: ");
            LocalDate dateOfBirth = readDate("Date of birth (YYYY-MM-DD): ");

            DigitalID id = facade.createIdentity(firstName, lastName, dateOfBirth, currentOrganisation);
            System.out.println("\n yay - Digital ID created successfully!");
            printIdentity(id);
        } catch (ValidationException | AuthorizationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    private void handleUpdate() {
        System.out.println("\n OoO - Update a Digital ID - OoO ");
        try {
            String idNumber = readLine("Digital ID number: ");
            System.out.println("(Leave this blank to keep the current value)");
            String firstNameInput = readLine("New first name: ");
            String lastNameInput = readLine("New last name: ");

            String firstName = firstNameInput.isEmpty() ? null : firstNameInput;
            String lastName = lastNameInput.isEmpty() ? null : lastNameInput;

            DigitalID updated = facade.updateName(idNumber, firstName, lastName, currentOrganisation);
            System.out.println("\nDigital ID updated successfully!");
            printIdentity(updated);
        } catch (ValidationException | AuthorizationException | IdentityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleChangeStatus() {
        System.out.println("\n ~ Change Digital ID Status ~ ");
        try {
            String idNumber = readLine("Digital ID number: ");

            System.out.println("Select new status:");
            Status[] statuses = Status.values();
            for (int i = 0; i < statuses.length; i++) {
                System.out.printf("%d. %s%n", i + 1, statuses[i]);
            }
            int choice = readInt("Choice: ", 1, statuses.length);
            Status newStatus = statuses[choice - 1];

            DigitalID updated = facade.changeStatus(idNumber, newStatus, currentOrganisation);
            System.out.println("\nStatus changed successfully!");
            printIdentity(updated);
        } catch (ValidationException | AuthorizationException | IdentityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleVerify() {
        System.out.println("\n < Verify a Digital ID >");
        try {
            String idNumber = readLine("Digital ID number: ");
            VerificationResponse response = facade.verifyIdentity(idNumber, currentOrganisation);
            printVerificationResponse(response);
        } catch (AuthorizationException | IdentityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleList() {
        System.out.println("\n !! All Digital IDs !!");
        List<DigitalID> identities = facade.listAllIdentities();
        if (identities.isEmpty()) {
            System.out.println("(none yet)");
            return;
        }
        for (DigitalID id : identities) {
            printIdentity(id);
            System.out.println();
        }
        System.out.println("Total: " + identities.size());
    }

    private void printIdentity(DigitalID id) {
        System.out.println("  ID Number : " + id.getDigitalIdNumber());
        System.out.println("  Name      : " + id.getFullName());
        System.out.println("  DOB       : " + id.getDateOfBirth());
        System.out.println("  Status    : " + id.getStatus());
        System.out.println("  Created   : " + id.getCreatedDate());
        System.out.println("  Modified  : " + id.getLastModifiedDate());
    }

    private void printVerificationResponse(VerificationResponse response) {
        System.out.println();
        System.out.println("Verification result for " + response.getDigitalIdNumber());
        System.out.println("  Valid     : " + response.isValid());
        System.out.println("  Status    : " + response.getStatus());
        System.out.println("  Message   : " + response.getMessage());
        System.out.println("  Time      : " + response.getVerificationTime());
        if (!response.getAdditionalInfo().isEmpty()) {
            System.out.println("  Details   :");
            response.getAdditionalInfo().forEach((k, v) -> System.out.println("    - " + k + ": " + v));
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            throw new IllegalStateException("Input stream closed");
        }
        return scanner.nextLine().trim();
    }

    private LocalDate readDate(String prompt) {
        return LocalDate.parse(readLine(prompt));
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
