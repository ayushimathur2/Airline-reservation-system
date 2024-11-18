import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

class Passenger {
    private String name;
    private String email;
    private String phoneNumber;

    public Passenger(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}

class Flight {
    String flightNumber, destination, arrivalTime, departureTime;
    int availableSeats, seatsBooked;
    double ticketPrice;

    Flight(String flightNumber, String destination, int availableSeats,
           double ticketPrice, String arrivalTime, String departureTime) {
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.availableSeats = availableSeats;
        this.ticketPrice = ticketPrice;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.seatsBooked = 0;
    }

    String getFlightNumber() {
        return flightNumber;
    }

    String getDestination() {
        return destination;
    }

    int getAvailableSeats() {
        return availableSeats;
    }

    double getTicketPrice() {
        return ticketPrice;
    }

    int getSeatsBooked() {
        return seatsBooked;
    }

    String getArrivalTime() {
        return arrivalTime;
    }

    String getDepartureTime() {
        return departureTime;
    }

    void bookSeats(int numSeats) {
        if (numSeats > 0 && numSeats <= availableSeats) {
            availableSeats -= numSeats;
            seatsBooked += numSeats;
            System.out.println("Booking successful. Enjoy your flight!");
        } else {
            System.out.println("Booking failed. Not enough available seats.");
        }
    }

    @Override
    public String toString() {
        return "---Flight " + flightNumber + " to " + destination + "---" +
               "\n- Available Seats: " + availableSeats +
               "\n- Booked Seats: " + seatsBooked +
               "\n- Ticket Price: $" + ticketPrice +
               "\n- Arrival Time: " + arrivalTime +
               "\n- Departure Time: " + departureTime;
    }
}

class Airline {
    static ArrayList<Flight> flights = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    private static final String JDBC_URL = "";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        Passenger passenger = getPassengerDetails();
        initializeFlights();
        displayMenu(passenger);
    }

    private static Passenger getPassengerDetails() {
        System.out.println("Passenger Details:");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your phone number: ");
        String phoneNumber = scanner.nextLine();

        return new Passenger(name, email, phoneNumber);
    }
    

    static void initializeFlights() {
        flights.add(new Flight("101", "New York", 50, 200.0, "08:00", "12:00"));
        flights.add(new Flight("102", "Los Angeles", 30, 150.0, "10:00", "14:00"));
        flights.add(new Flight("103", "Chicago", 40, 180.0, "12:00", "16:00"));
    }

    static void displayMenu(Passenger passenger) {
        int choice;
        do {
            System.out.println("\nAirline Reservation System");
            System.out.println("1. Display Available Flights");
            System.out.println("2. Book a Flight");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    displayAvailableFlights();
                    break;
                case 2:
                    bookFlight(passenger);
                    break;
                case 3:
                    System.out.println("Thank you for using the Airline Reservation System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);
    }

    static void displayAvailableFlights() {
        System.out.println("\nAvailable Flights:");
        for (Flight flight : flights) {
            System.out.println(flight);
        }
    }

    static void bookFlight(Passenger passenger) {
        System.out.print("Enter the flight number: ");
        String flightNumber = scanner.next();

        Flight selectedFlight = findFlight(flightNumber);

        if (selectedFlight != null) {
            System.out.println("Flight details: " + selectedFlight);
            System.out.print("Enter the number of seats to book: ");
            int numSeats = scanner.nextInt();

            double totalCost = numSeats * selectedFlight.getTicketPrice();
            System.out.println("Total cost: $" + totalCost);
            System.out.print("Enter 1 to proceed with payment, 0 to cancel: ");
            int paymentChoice = scanner.nextInt();

            if (paymentChoice == 1) {
                processPayment(totalCost);
                selectedFlight.bookSeats(numSeats);

                // Insert booking information into the database
   
                insertBooking(passenger, selectedFlight, numSeats, totalCost);
            } else {
                System.out.println("Booking canceled. No payment made.");
            }
        } else {
            System.out.println("Flight not found. Please enter a valid flight number.");
        }
    }

    static void processPayment(double totalCost) {
        System.out.println("Processing payment... Payment of $" + totalCost + " successful.");
    }

    static Flight findFlight(String flightNumber) {
        for (int i = 0; i < flights.size(); i++) {
            Flight flight = flights.get(i);
            if (flight.getFlightNumber().equals(flightNumber)) {
                return flight;
            }
        }
        return null;
    }

    static void insertBooking(Passenger passenger, Flight flight, int numSeats, double totalCost) {
        String insertQuery = "INSERT INTO bookings (passenger_name, passenger_email, passenger_phone, flight_number, num_seats, total_cost) VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)
        ) {
            preparedStatement.setString(1, passenger.getName());
            preparedStatement.setString(2, passenger.getEmail());
            preparedStatement.setString(3, passenger.getPhoneNumber());
            preparedStatement.setString(4, flight.getFlightNumber());
            preparedStatement.setInt(5, numSeats);
            preparedStatement.setDouble(6, totalCost);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Booking information inserted successfully.");
            } else {
                System.out.println("Failed to insert booking information.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


