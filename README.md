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
  
# All available movie theater commands:
  1. mt assignAirDateTimes (Admin command) - Add air date time and auditorium to event
  2. mt bookTickets (User command) - Buy tickets
  3. mt createEvent (Admin command) - Create new event
  4. mt enterAdminPanel (User command) - Enable admin commands
  5. mt exitAdminPanel (Admin command) - Disable admin commands
  6. mt getTicketPrice (User command) - Calculate total price for ticket
  7. mt viewAuditorium (Admin command) - View auditorium
  8. mt viewAuditoriums (Admin command) - View all auditoriums
  9. mt register (User command) - Register new user
  10. mt viewAvailableSeats (Admin/User command) - View available seats for event on specific date time
  11. mt viewEvent - View event
  12. mt viewEvents (Admin/User command) - View all events
  13. mt viewPurchasedTickets (Admin command) - View all purchased tickets for specific event and air date
  14. mt viewUserTickets - View all tickets booked by user
  15. mt viewUsers (Admin command) - View all registered Users
  
# Other shell commands
  1. exit - Exits the shell
  2. help - List all commands usage
