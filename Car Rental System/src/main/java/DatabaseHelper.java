import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/car_rental_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Priyansu@123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Car operations
    public static void addCar(Car car) throws SQLException {
        String query = "INSERT INTO cars (car_id, brand, model, base_price_per_day, is_available) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, car.getCarId());
            pstmt.setString(2, car.getBrand());
            pstmt.setString(3, car.getModel());
            pstmt.setDouble(4, car.getBasePricePerDay());
            pstmt.setBoolean(5, car.isAvailable());
            pstmt.executeUpdate();
        }
    }

    public static Car getCarById(String carId) throws SQLException {
        String query = "SELECT * FROM cars WHERE car_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, carId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Car(
                        rs.getString("car_id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getDouble("base_price_per_day"),
                        rs.getBoolean("is_available")
                );
            }
        }
        return null;
    }

    public static void updateCarAvailability(String carId, boolean isAvailable) throws SQLException {
        String query = "UPDATE cars SET is_available = ? WHERE car_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setBoolean(1, isAvailable);
            pstmt.setString(2, carId);
            pstmt.executeUpdate();
        }
    }

    // Customer operations
    public static void addCustomer(Customer customer) throws SQLException {
        String query = "INSERT INTO customers (customer_id, name) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, customer.getCustomerId());
            pstmt.setString(2, customer.getName());
            pstmt.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Customer ID already exists: " + customer.getCustomerId());
            // Handle the duplicate entry case appropriately, e.g., show an error message to the user.
        }
    }


    public static Customer getCustomerById(String customerId) throws SQLException {
        String query = "SELECT * FROM customers WHERE customer_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getString("customer_id"),
                        rs.getString("name")
                );
            }
        }
        return null;
    }

    // Rental operations
    public static void addRental(Car car, Customer customer, int rentalDays) throws SQLException {
        String query = "INSERT INTO rentals (car_id, customer_id, rental_date, return_date) VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL ? DAY))";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, car.getCarId());
            pstmt.setString(2, customer.getCustomerId());
            pstmt.setInt(3, rentalDays);
            pstmt.executeUpdate();
        }
    }

    public static void deleteRental(String carId) throws SQLException {
        String query = "DELETE FROM rentals WHERE car_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, carId);
            pstmt.executeUpdate();
        }
    }

    public static Rental getRentalByCarId(String carId) throws SQLException {
        String query = "SELECT * FROM rentals WHERE car_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, carId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Car car = getCarById(carId);
                Customer customer = getCustomerById(rs.getString("customer_id"));
                return new Rental(car, customer, rs.getInt("rental_days"));
            }
        }
        return null;
    }
}
