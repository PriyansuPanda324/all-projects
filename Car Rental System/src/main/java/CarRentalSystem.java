import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
        loadCarsFromDatabase();
        loadCustomersFromDatabase();
        loadRentalsFromDatabase();
    }

    // Add Car
    public void addCar(Car car) {
        try {
            DatabaseHelper.addCar(car);
            cars.add(car);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get Car by ID
    public Car getCarById(String carId) {
        for (Car car : cars) {
            if (car.getCarId().equals(carId)) {
                return car;
            }
        }
        return null;
    }

    // Add Customer
    public void addCustomer(Customer customer) {
        try {
            DatabaseHelper.addCustomer(customer);
            customers.add(customer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get Customers Count
    public int getCustomersCount() {
        return customers.size();
    }

    // Rent Car
    public void rentCar(Car car, Customer customer, int days) {
        if (car != null && car.isAvailable()) {
            try {
                DatabaseHelper.addRental(car, customer, days);
                car.setAvailable(false);
                DatabaseHelper.updateCarAvailability(car.getCarId(), false);
                rentals.add(new Rental(car, customer, days));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Car is not available for rent.");
        }
    }

    // Return Car
    public void returnCar(Car car) {
        if (car != null && !car.isAvailable()) {
            try {
                // Remove the rental record from the database
                DatabaseHelper.deleteRental(car.getCarId());

                // Update car availability in the database
                DatabaseHelper.updateCarAvailability(car.getCarId(), true);

                // Remove the rental from the local list
                Rental rentalToRemove = null;
                for (Rental rental : rentals) {
                    if (rental.getCar().equals(car)) {
                        rentalToRemove = rental;
                        break;
                    }
                }
                if (rentalToRemove != null) {
                    rentals.remove(rentalToRemove);
                }

                // Mark car as available
                car.setAvailable(true);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Car is either not rented or already available.");
        }
    }

    // Load Cars from Database
    private void loadCarsFromDatabase() {
        // Implementation to load cars from the database and populate the 'cars' list
    }

    // Load Customers from Database
    private void loadCustomersFromDatabase() {
        // Implementation to load customers from the database and populate the 'customers' list
    }

    // Load Rentals from Database
    private void loadRentalsFromDatabase() {
        // Implementation to load rentals from the database and populate the 'rentals' list
    }
}
