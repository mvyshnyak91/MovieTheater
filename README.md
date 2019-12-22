# How to run app
  1. Go to the root of the project.
  2. Run the following commands:
      - mvn clean package
      - sh target/appassembler/bin/movie-theater.sh

# Tips
  1. Show all available commands for movie theater app:
      - type 'mt' in terminal and press 'Tab' 2 times.
  2. Show all mandatory parameters for command:
      - type 'mt <command>' and press Enter.
  3. Autofilling for commands and parameters:
      - press Tab during typing commands/parameters in terminal

# Pattern for running commands: 
  pattern: mt :command --:param1 :value1 --:param2 :value2 ... --:paramN :valueN
  
  example: mt createEvent --name eventName --basePrice 10 --rating HIGH
  
  - If your :value contains spaces, wrap it in single quotes ':value'.
  - All movie theater commands start with 'mt'.
  
# Available movie theater commands:
  1. mt assignAirDates - Add air date and auditorium to event
  2. mt buyTickets - Buy tickets
  3. mt createEvent - Create new event
  4. mt enterAdminPanel - Enable admin commands
  5. mt exitAdminPanel - Disable admin commands
  6. mt getTicketPrice - Calculate total price for ticket
  7. mt register - Register new user
  8. mt viewAvailableSeats - View available seats for event on specific date time
  9. mt viewEvents - View all events
  10. mt viewPurchasedTickets - View all purchased tickets for specific event and air date
  11. mt viewUsers - View all registered Users
  
# Other shell commands
  1. exit - Exits the shell
  2. help - List all commands usage
