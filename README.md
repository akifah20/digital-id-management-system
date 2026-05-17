# digital id management system


## GitHub repository link: 
https://github.com/akifah20/digital-id-management-system

## Instructions for running the system:
In the terminal, run this command: 
**java -jar target/digital-id-management-system-0.0.1-SNAPSHOT.jar**

## Overview of system structure and main components: 

### System Structure:

#### Layered architecture:

starting from bottom to top:
Database layer - the HashMap inside InMemoryIdentityRepository stores ID objects in memory. When we close the console application, all of this data is lost.
Persistence layer - stores and retrieves the ID objects.
Buisness logic layer - domain, service, strategy and exception as these all contain rules and decisions.
Presentation layer - facade and console UI 

design patterns used:
facade - structraual design pattern
strategy pattern - behavioural design pattern

dependency injection


### Main Components:

CRUD operation | Application via methods. double check in the identityRepository + IdentityManagementService files
--------------- -------------------------------------------------------------------------------
Create         | 
Read           | 
Update         | 
Delete         |  
