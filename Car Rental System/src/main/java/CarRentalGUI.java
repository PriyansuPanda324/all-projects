import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CarRentalGUI {
    private JFrame frame;
    private CarRentalSystem rentalSystem;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "password";

    public CarRentalGUI() {
        rentalSystem = new CarRentalSystem();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Car Rental System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        showLoginDialog();
    }

    private void showLoginDialog() {
        JDialog dialog = new JDialog(frame, "Admin Login", true);
        dialog.setLayout(new GridLayout(3, 2));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        dialog.add(new JLabel("Username:"));
        dialog.add(usernameField);
        dialog.add(new JLabel("Password:"));
        dialog.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
                    dialog.dispose();
                    createMainWindow();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dialog.add(loginButton);
        dialog.setSize(300, 150);
        dialog.setVisible(true);
    }

    private void createMainWindow() {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new GridLayout(4, 1));

        JButton addCarButton = new JButton("Add Car");
        addCarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddCarDialog();
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        adminPanel.add(addCarButton);
        adminPanel.add(exitButton);

        tabbedPane.addTab("Admin Panel", adminPanel);
        tabbedPane.addTab("Main Panel", createMainPanel());

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));

        JButton rentCarButton = new JButton("Rent a Car");
        rentCarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showRentCarDialog();
            }
        });

        JButton returnCarButton = new JButton("Return a Car");
        returnCarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showReturnCarDialog();
            }
        });

        mainPanel.add(rentCarButton);
        mainPanel.add(returnCarButton);

        return mainPanel;
    }

    private void showAddCarDialog() {
        JDialog dialog = new JDialog(frame, "Add Car", true);
        dialog.setLayout(new GridLayout(5, 2));

        JTextField carIdField = new JTextField();
        JTextField brandField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField priceField = new JTextField();

        dialog.add(new JLabel("Car ID:"));
        dialog.add(carIdField);
        dialog.add(new JLabel("Brand:"));
        dialog.add(brandField);
        dialog.add(new JLabel("Model:"));
        dialog.add(modelField);
        dialog.add(new JLabel("Price per Day:"));
        dialog.add(priceField);

        JButton addButton = new JButton("Add Car");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String carId = carIdField.getText();
                String brand = brandField.getText();
                String model = modelField.getText();
                double pricePerDay = Double.parseDouble(priceField.getText());

                Car car = new Car(carId, brand, model, pricePerDay, true);
                rentalSystem.addCar(car);
                dialog.dispose();
                JOptionPane.showMessageDialog(frame, "Car added successfully.");
            }
        });

        dialog.add(addButton);
        dialog.setSize(300, 200);
        dialog.setVisible(true);
    }

    private void showRentCarDialog() {
        JDialog dialog = new JDialog(frame, "Rent Car", true);
        dialog.setLayout(new GridLayout(4, 2));

        JTextField nameField = new JTextField();
        JTextField carIdField = new JTextField();
        JTextField daysField = new JTextField();

        dialog.add(new JLabel("Customer Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Car ID:"));
        dialog.add(carIdField);
        dialog.add(new JLabel("Rental Days:"));
        dialog.add(daysField);

        JButton rentButton = new JButton("Rent Car");
        rentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String customerName = nameField.getText();
                String carId = carIdField.getText();
                int rentalDays = Integer.parseInt(daysField.getText());

                Car car = rentalSystem.getCarById(carId);
                if (car != null && car.isAvailable()) {
                    Customer customer = new Customer("CUS" + (rentalSystem.getCustomersCount() + 1), customerName);
                    rentalSystem.addCustomer(customer);
                    rentalSystem.rentCar(car, customer, rentalDays);
                    JOptionPane.showMessageDialog(frame, "Car rented successfully.");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Car not available or invalid car ID.");
                }
            }
        });

        dialog.add(rentButton);
        dialog.setSize(300, 200);
        dialog.setVisible(true);
    }

    private void showReturnCarDialog() {
        JDialog dialog = new JDialog(frame, "Return Car", true);
        dialog.setLayout(new GridLayout(2, 2));

        JTextField carIdField = new JTextField();

        dialog.add(new JLabel("Car ID:"));
        dialog.add(carIdField);

        JButton returnButton = new JButton("Return Car");
        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String carId = carIdField.getText();
                Car car = rentalSystem.getCarById(carId);
                if (car != null && !car.isAvailable()) {
                    rentalSystem.returnCar(car);
                    JOptionPane.showMessageDialog(frame, "Car returned successfully.");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid car ID or car not rented.");
                }
            }
        });

        dialog.add(returnButton);
        dialog.setSize(300, 150);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CarRentalGUI();
            }
        });
    }
}
