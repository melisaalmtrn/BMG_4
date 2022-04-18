package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Bu sınıf veritabanına yapılan sorguları içerir

 */

public class DataSource extends Car{

    public static final String DB_NAME = "rentacar.sqlite";
    //public static final String DB_USERNAME = "root";
    //public static final String DB_PASSWORD = "12345";

    public static final String CONNECTION_STRING = "jdbc:sqlite:C:src\\app\\db\\" + DB_NAME ;

    public static final String TABLE_CARS = "cars";
    public static final String COLUMN_CARS_ID = "id";
    public static final String COLUMN_CARS_BRAND_ID = "brand_id";
    public static final String COLUMN_CARS_COLOR_ID = "color_id";
    public static final String COLUMN_CARS_MODEL_YEAR = "model_year";
    public static final String COLUMN_CARS_MODEL_NAME = "model_name";
    public static final String COLUMN_CARS_DAILY_PRICE = "daily_price";
    public static final String COLUMN_CARS_DESCRIPTION = "description";

    public static final String TABLE_BRANDS = "brands";
    public static final String COLUMN_BRANDS_ID = "id";
    public static final String COLUMN_BRANDS_BRAND_NAME = "brand_name";

    public static final String TABLE_COLORS = "colors";
    public static final String COLUMN_COLORS_ID = "id";
    public static final String COLUMN_COLORS_COLOR_NAME = "color_name";

    public static final String TABLE_RENTALS = "rentals";
    public static final String COLUMN_RENTALS_ID = "id";
    public static final String COLUMN_RENTALS_CAR_ID = "car_id";
    public static final String COLUMN_RENTALS_USER_ID = "user_id";
    public static final String COLUMN_RENTALS_RENT_DATE = "rent_date";
    public static final String COLUMN_RENTALS_RETURN_DATE = "return_date";

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USERS_ID = "id";
    public static final String COLUMN_USERS_FULLNAME = "fullname";
    public static final String COLUMN_USERS_USERNAME = "username";
    public static final String COLUMN_USERS_EMAIL = "email";
    public static final String COLUMN_USERS_PASSWORD = "password";
    public static final String COLUMN_USERS_SALT = "salt";
    public static final String COLUMN_USERS_ADMIN = "admin";
    public static final String COLUMN_USERS_STATUS = "status";


    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    public Connection conn;

    private static final DataSource instance = new DataSource();

    /**
     * Constructor private türünde. Böylece bu sınıf new lenemez
     */

    private DataSource() { }

    public static DataSource getInstance(){
        return instance;
    }

    public boolean open(){
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch (SQLException e){
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close(){
        try {
            if(conn != null){
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    // Araba sorguları başlangıç

    public List<Car> getAllCars(int sortRental){

        StringBuilder queryCars = queryCars();

        if (sortRental != ORDER_BY_NONE) {
            queryCars.append(" ORDER BY ");
            queryCars.append(COLUMN_CARS_ID);
            if (sortRental == ORDER_BY_DESC) {
                queryCars.append(" DESC");
            } else {
                queryCars.append(" ASC");
            }
        }
        try(Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(queryCars.toString()))    {

            List<Car> cars = new ArrayList<>();
            while (results.next()){
                Car car = new Car();
                car.setId(results.getInt(1));
                car.setBrand_name(results.getString(2));
                car.setColor_name(results.getString(3));
                car.setModel_year(results.getInt(4));
                car.setModel_name(results.getString(5));
                car.setDaily_price(results.getDouble(6));
                car.setDescription(results.getString(7));
                car.setNr_sales(results.getInt(8));
                cars.add(car);

            }
            return  cars;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Car> getOneCar(int car_id) {

        StringBuilder queryCars = queryCars();
        queryCars.append(" WHERE " + TABLE_CARS + "." + COLUMN_CARS_ID + " = ? LIMIT 1");
        try (PreparedStatement statement = conn.prepareStatement(String.valueOf(queryCars))) {
            statement.setInt(1, car_id);
            ResultSet results = statement.executeQuery();
            List<Car> cars = new ArrayList<>();
            while (results.next()) {
                Car car = new Car();
                car.setId(results.getInt(1));
                car.setBrand_name(results.getString(2));
                car.setColor_name(results.getString(3));
                car.setModel_year(results.getInt(4));
                car.setModel_name((results.getString(5)));
                car.setDaily_price(results.getDouble(6));
                car.setDescription(results.getString(7));
                car.setNr_sales(results.getInt(8));
                car.setBrand_id(results.getInt(9));
                car.setColor_id((results.getInt(10)));
                cars.add(car);
            }
            return cars;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Car>searchCars(String searchString, int sortRental) {
        StringBuilder queryProducts = queryCars();
        queryProducts.append(" WHERE (" + TABLE_CARS + "." + COLUMN_BRANDS_BRAND_NAME + " LIKE ? OR " + TABLE_CARS + "." + COLUMN_CARS_DESCRIPTION + " LIKE ?)");

        if (sortRental != ORDER_BY_NONE) {
            queryProducts.append(" ORDER BY ");
            queryProducts.append(COLUMN_BRANDS_BRAND_NAME);
            if (sortRental == ORDER_BY_DESC) {
                queryProducts.append(" DESC");
            } else {
                queryProducts.append(" ASC");
            }
        }

        try (PreparedStatement statement = conn.prepareStatement(queryProducts.toString())) {
            statement.setString(1, "%" + searchString + "%");
            statement.setString(2, "%" + searchString + "%");
            ResultSet results = statement.executeQuery();

            List<Car> Car = new ArrayList<>();
            while (results.next()) {
                Car car = new Car();
                car.setId(results.getInt(1));
               // car.setBrand_id(results.getInt(2));
                car.setBrand_name(results.getString(2));
                //car.setColor_id(results.getInt(4));
                car.setColor_name(results.getString(3));
                car.setModel_year(results.getInt(4));
                car.setModel_name(results.getString(5));
                car.setDaily_price(results.getDouble(6));
                car.setNr_sales(results.getInt(7));
                Car.add(car);
            }
            return Car;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    private StringBuilder queryCars() {

        return new StringBuilder("SELECT " +
                TABLE_CARS + "." + COLUMN_CARS_ID + ", " +
                TABLE_BRANDS + "." + COLUMN_BRANDS_BRAND_NAME + ", " +
                TABLE_COLORS + "." + COLUMN_COLORS_COLOR_NAME + ", " +
                TABLE_CARS + "." + COLUMN_CARS_MODEL_YEAR+ ", " +
                TABLE_CARS + "." + COLUMN_CARS_MODEL_NAME+ ", " +
                TABLE_CARS + "." + COLUMN_CARS_DAILY_PRICE + ", " +
                TABLE_CARS + "." + COLUMN_CARS_DESCRIPTION + ", " +
                " (SELECT COUNT(*) FROM " + TABLE_RENTALS + " WHERE " + TABLE_RENTALS + "." + COLUMN_RENTALS_CAR_ID + " = " + TABLE_CARS + "." + COLUMN_CARS_ID + ") AS nr_sales" + ", " +
                TABLE_BRANDS + "." + COLUMN_BRANDS_ID + ", " +
                TABLE_COLORS + "." + COLUMN_COLORS_ID +
                " FROM " + TABLE_CARS +
                " LEFT JOIN " + TABLE_BRANDS +
                " ON " + TABLE_CARS + "." + COLUMN_CARS_BRAND_ID +
                " = " + TABLE_BRANDS + "." + COLUMN_BRANDS_ID +
                " LEFT JOIN " + TABLE_COLORS +
                " ON " + TABLE_CARS + "." + COLUMN_CARS_COLOR_ID +
                " = " + TABLE_COLORS + "." + COLUMN_COLORS_ID
        );
    }

    /**
     * Bu metoto aracın id sinde göre aracı siler.
     */
    public boolean deleteSingleCar(int car_id){
        String sql = "DELETE FROM " + TABLE_CARS + " WHERE " + COLUMN_CARS_ID + " = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setInt(1, car_id);
            int rows = statement.executeUpdate();
            System.out.println(rows + " record(s) deleted.");
            return  true;
        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return false;
        }
    }



    public boolean insertNewCar(int brand_id, int color_id,int model_year, String model_name, double daily_price, String description) {

        String sql = "INSERT INTO " + TABLE_CARS + " ("
                + COLUMN_CARS_BRAND_ID + ", "
                + COLUMN_CARS_COLOR_ID + ", "
                + COLUMN_CARS_MODEL_YEAR + ", "
                + COLUMN_CARS_MODEL_NAME + ", "
                + COLUMN_CARS_DAILY_PRICE + ", "
                + COLUMN_CARS_DESCRIPTION +
                ") VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, brand_id);
            statement.setInt(2, color_id);
            statement.setInt(3, model_year);
            statement.setString(4, model_name);
            statement.setDouble(5, daily_price);
            statement.setString(6, description);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return false;
        }
    }

    public boolean updateOneCar(int car_id, int brand_id, int color_id, int model_year,String model_name , double daily_price, String description){

        String sql = "UPDATE " + TABLE_CARS + " SET "
                + COLUMN_CARS_BRAND_ID + " = ?" + ", "
                + COLUMN_CARS_COLOR_ID + " = ?" + ", "
                + COLUMN_CARS_MODEL_YEAR + " = ?" + ", "
                + COLUMN_CARS_MODEL_NAME + " = ?" + ", "
                + COLUMN_CARS_DAILY_PRICE + " = ?" + ", "
                + COLUMN_CARS_DESCRIPTION + " = ?" +
                " WHERE " + COLUMN_CARS_ID + " = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, brand_id);
            statement.setInt(2, color_id);
            statement.setInt(3, model_year);
            statement.setString(4, model_name);
            statement.setDouble(5, daily_price);
            statement.setString(6, description);
            statement.setInt(7, car_id);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return false;
        }



    }

    // Araba sorguları bitiş

    // Kullanıcı sorguları başlangıç

    public User getUserByUsername(String username) throws SQLException {

        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERS_USERNAME + " = ?");
        preparedStatement.setString(1, username);
        ResultSet results = preparedStatement.executeQuery();

        User user = new User();
        if (results.next()) {

            user.setId(results.getInt("id"));
            user.setUsername(results.getString("username"));
            user.setEmail(results.getString("email"));
            user.setPassword(results.getString("password"));
            user.setSalt(results.getString("salt"));
            user.setAdmin(results.getInt("admin"));
            user.setStatus(results.getString("status"));

        }

        return user;
    }

    public User getUserByEmail(String email) throws SQLException {

        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERS_EMAIL + " = ?");
        preparedStatement.setString(1, email);
        ResultSet results = preparedStatement.executeQuery();

        User user = new User();
        if (results.next()) {

            user.setId(results.getInt("id"));
            user.setFullname(results.getString("fullname"));
            user.setUsername(results.getString("username"));
            user.setEmail(results.getString("email"));
            user.setPassword(results.getString("password"));
            user.setSalt(results.getString("salt"));
            user.setAdmin(results.getInt("admin"));
            user.setStatus(results.getString("status"));

        }

        return user;
    }

    public boolean insertNewUser(String fullName, String username, String email, String password, String salt) {

        String sql = "INSERT INTO " + TABLE_USERS + " ("
                + COLUMN_USERS_FULLNAME + ", "
                + COLUMN_USERS_USERNAME + ", "
                + COLUMN_USERS_EMAIL + ", "
                + COLUMN_USERS_PASSWORD + ", "
                + COLUMN_USERS_SALT + ", "
                + COLUMN_USERS_ADMIN + ", "
                + COLUMN_USERS_STATUS +
                ") VALUES (?, ?, ?, ?, ?, 0, 'enabled')";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, fullName);
            statement.setString(2, username);
            statement.setString(3, email);
            statement.setString(4, password);
            statement.setString(5, salt);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return false;
        }
    }

    // Kullanıcı sorguları bitiş


    /**
     * Bu metot araçların markalarını veritabanından alır
     */
    // sortOrder = sıralama düzeni
    public List<Brands> getCarBrands(int sortOrder){
        StringBuilder queryBrands = new StringBuilder("SELECT " +
                TABLE_BRANDS + "." + COLUMN_BRANDS_ID + ", " +
                TABLE_BRANDS + "." + COLUMN_BRANDS_BRAND_NAME +
                " FROM " + TABLE_BRANDS
        );

        if (sortOrder != ORDER_BY_NONE) {
            queryBrands.append(" ORDER BY ");
            queryBrands.append(COLUMN_BRANDS_ID);
            if(sortOrder == ORDER_BY_DESC){
                queryBrands.append(" DESC ");
            } else {
                queryBrands.append(" ASC ");
            }
        }

        try (Statement statement = conn.createStatement();
            ResultSet results =statement.executeQuery(queryBrands.toString())) {

            List<Brands> brands = new ArrayList<>();
            while (results.next()){
                Brands brand = new Brands();
                brand.setId(results.getInt(1));
                brand.setBrand_name(results.getString(2));
                brands.add(brand);
            }
            return brands;

        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Colors> getCarColors(int sortOrder){
        StringBuilder queryBrands = new StringBuilder("SELECT " +
                TABLE_COLORS + "." + COLUMN_COLORS_ID + ", " +
                TABLE_COLORS + "." + COLUMN_COLORS_COLOR_NAME +
                " FROM " + TABLE_COLORS
        );

        if (sortOrder != ORDER_BY_NONE) {
            queryBrands.append(" ORDER BY ");
            queryBrands.append(COLUMN_COLORS_ID);
            if(sortOrder == ORDER_BY_DESC){
                queryBrands.append(" DESC ");
            } else {
                queryBrands.append(" ASC ");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet results =statement.executeQuery(queryBrands.toString())) {

            List<Colors> colors = new ArrayList<>();
            while (results.next()){
                Colors color = new Colors();
                color.setId(results.getInt(1));
                color.setColor_name(results.getString(2));
                colors.add(color);
            }
            return colors;

        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }

    }

    public Integer countAllCars() {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT COUNT(*) FROM " + TABLE_CARS)) {
            if (results.next()) {
                return results.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return 0;
        }
    }

    public Integer countAllCustomers() {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT COUNT(*) FROM " + TABLE_USERS +
                     " WHERE " + COLUMN_USERS_ADMIN + "= 0"
             )
        ) {
            if (results.next()) {
                return results.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return 0;
        }
    }

    /**
     * This method counts all the orders on the database.
     * @param user_id       Provided user id.
     * @return int      Returns count of the orders.
     * @since                   1.0.0
     */
    public Integer countUserRentals(int user_id) {

        try (PreparedStatement statement = conn.prepareStatement(String.valueOf("SELECT COUNT(*) FROM " + TABLE_RENTALS + " WHERE " + COLUMN_RENTALS_USER_ID + "= ?"))) {
            statement.setInt(1, user_id);
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return 0;
        }
    }

    public List<Rental> getAllUserRentals(int sortRental, int user_id) {

        StringBuilder queryRentals = new StringBuilder("SELECT " +
                TABLE_RENTALS + "." + COLUMN_RENTALS_ID + ", " +
                TABLE_RENTALS + "." + COLUMN_RENTALS_CAR_ID + ", " +
                TABLE_RENTALS + "." + COLUMN_RENTALS_USER_ID + ", " +
                TABLE_USERS + "." + COLUMN_USERS_FULLNAME + ", " +
                TABLE_RENTALS + "." + COLUMN_RENTALS_RENT_DATE + ", " +
                TABLE_CARS + "." + COLUMN_CARS_MODEL_NAME + ", " +
                TABLE_CARS + "." + COLUMN_CARS_DAILY_PRICE +
                " FROM " + TABLE_RENTALS
        );

        queryRentals.append("" +
                " LEFT JOIN " + TABLE_CARS +
                " ON " + TABLE_RENTALS + "." + COLUMN_RENTALS_CAR_ID +
                " = " + TABLE_CARS + "." + COLUMN_CARS_ID);
        queryRentals.append("" +
                " LEFT JOIN " + TABLE_USERS +
                " ON " + TABLE_RENTALS + "." + COLUMN_RENTALS_USER_ID +
                " = " + TABLE_USERS + "." + COLUMN_USERS_ID);

        queryRentals.append(" WHERE " + TABLE_RENTALS + "." + COLUMN_RENTALS_USER_ID + " = ").append(user_id);

        if (sortRental != ORDER_BY_NONE) {
            queryRentals.append(" ORDER BY ");
            queryRentals.append(COLUMN_USERS_FULLNAME);
            if (sortRental == ORDER_BY_DESC) {
                queryRentals.append(" DESC");
            } else {
                queryRentals.append(" ASC");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(queryRentals.toString())) {

            List<Rental> rentals = new ArrayList<>();
            while (results.next()) {
                Rental rental = new Rental();
                rental.setId(results.getInt(1));
                rental.setCar_id(results.getInt(2));
                rental.setUser_id(results.getInt(3));
                rental.setUser_full_name(results.getString(4));
                rental.setRental_date(results.getString(5));
                rental.setModel_name(results.getString(6));
                rental.setRental_price(results.getDouble(7));
                rentals.add(rental);
            }
            return rentals;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    // Rentals sorguları başlangıç


    public List<Rental> getAllRentals(int sortOrder) {

        StringBuilder queryRentals = new StringBuilder("SELECT " +
                TABLE_RENTALS + "." + COLUMN_RENTALS_ID + ", " +
                TABLE_RENTALS + "." + COLUMN_RENTALS_CAR_ID + ", " +
                TABLE_RENTALS + "." + COLUMN_RENTALS_USER_ID + ", " +
                TABLE_USERS + "." + COLUMN_USERS_FULLNAME + ", " +
                TABLE_RENTALS + "." + COLUMN_RENTALS_RENT_DATE + ", " +
                TABLE_CARS + "." + COLUMN_CARS_MODEL_NAME + ", " +
                TABLE_BRANDS + "." + COLUMN_BRANDS_BRAND_NAME + ", " +
                TABLE_CARS + "." + COLUMN_CARS_DAILY_PRICE +
                " FROM " + TABLE_RENTALS
        );

        queryRentals.append("" +
                " LEFT JOIN " + TABLE_CARS +
                " ON " + TABLE_RENTALS + "." + COLUMN_RENTALS_CAR_ID +
                " = " + TABLE_CARS + "." + COLUMN_CARS_ID);
        queryRentals.append("" +
                " LEFT JOIN " + TABLE_USERS +
                " ON " + TABLE_RENTALS + "." + COLUMN_RENTALS_USER_ID +
                " = " + TABLE_USERS + "." + COLUMN_USERS_ID);
        queryRentals.append("" +
                " LEFT JOIN " + TABLE_BRANDS +
                " ON " + TABLE_CARS + "." + COLUMN_CARS_ID +
                " = " + TABLE_BRANDS + "." + COLUMN_BRANDS_ID);

        if (sortOrder != ORDER_BY_NONE) {
            queryRentals.append(" ORDER BY ");
            queryRentals.append(COLUMN_USERS_FULLNAME);
            if (sortOrder == ORDER_BY_DESC) {
                queryRentals.append(" DESC");
            } else {
                queryRentals.append(" ASC");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(queryRentals.toString())) {

            List<Rental> rentals = new ArrayList<>();
            while (results.next()) {
                Rental rental = new Rental();
                rental.setId(results.getInt(1));
                rental.setCar_id(results.getInt(2));
                rental.setUser_id(results.getInt(3));
                rental.setUser_full_name(results.getString(4));
                rental.setRental_date(results.getString(5));
                //rental.setReturn_date(results.getDate(6));
                rental.setModel_name(results.getString(6));
                rental.setBrand_name(results.getString(7));
                rental.setRental_price(results.getDouble(8));
                rentals.add(rental);
            }
            return rentals;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }


    public boolean insertNewRental(int car_id, int user_id, String rent_date) {

        String sql = "INSERT INTO " + TABLE_RENTALS + " ("
                + COLUMN_RENTALS_CAR_ID + ", "
                + COLUMN_RENTALS_USER_ID + ", "
                + COLUMN_RENTALS_RENT_DATE +
                ") VALUES (?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, car_id);
            statement.setInt(2, user_id);
            statement.setString(3, rent_date);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return false;
        }
    }



    public void decreaseStock(int car_id) {

        String sql = "UPDATE " + TABLE_CARS + " SET " + COLUMN_CARS_BRAND_ID + " = " + COLUMN_CARS_BRAND_ID + " - 1 WHERE " + COLUMN_CARS_ID + " = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, car_id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }

    public List<Customer> getAllCustomers(int sortOrder) {

        StringBuilder queryCustomers = queryCustomers();

        if (sortOrder != ORDER_BY_NONE) {
            queryCustomers.append(" ORDER BY ");
            queryCustomers.append(COLUMN_USERS_FULLNAME);
            if (sortOrder == ORDER_BY_DESC) {
                queryCustomers.append(" DESC");
            } else {
                queryCustomers.append(" ASC");
            }
        }
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(queryCustomers.toString())) {

            List<Customer> customers = new ArrayList<>();
            while (results.next()) {
                Customer customer = new Customer();
                customer.setId(results.getInt(1));
                customer.setFullname(results.getString(2));
                customer.setEmail(results.getString(3));
                customer.setUsername(results.getString(4));
                customer.setRentals(results.getInt(5));
                customer.setStatus(results.getString(6));
                customers.add(customer);
            }
            return customers;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    private StringBuilder queryCustomers() {
        return new StringBuilder("SELECT " +
                TABLE_USERS + "." + COLUMN_USERS_ID + ", " +
                TABLE_USERS + "." + COLUMN_USERS_FULLNAME + ", " +
                TABLE_USERS + "." + COLUMN_USERS_EMAIL + ", " +
                TABLE_USERS + "." + COLUMN_USERS_USERNAME + ", " +
                " (SELECT COUNT(*) FROM " + TABLE_RENTALS + " WHERE " + TABLE_RENTALS + "." + COLUMN_RENTALS_ID + " = " + TABLE_USERS + "." + COLUMN_USERS_ID + ") AS orders" + ", " +
                TABLE_USERS + "." + COLUMN_USERS_STATUS +
                " FROM " + TABLE_USERS +
                " WHERE " + TABLE_USERS + "." + COLUMN_USERS_ADMIN + " = 0"
        );
    }

    public List<Customer> getOneCustomer(int customer_id) {

        StringBuilder queryCustomers = queryCustomers();
        queryCustomers.append(" AND " + TABLE_USERS + "." + COLUMN_USERS_ID + " = ?");
        try (PreparedStatement statement = conn.prepareStatement(String.valueOf(queryCustomers))) {
            statement.setInt(1, customer_id);
            ResultSet results = statement.executeQuery();
            List<Customer> customers = new ArrayList<>();
            while (results.next()) {
                Customer customer = new Customer();
                customer.setId(results.getInt(1));
                customer.setFullname(results.getString(2));
                customer.setEmail(results.getString(3));
                customer.setUsername(results.getString(4));
                customer.setRentals(results.getInt(5));
                customer.setStatus(results.getString(6));
                customers.add(customer);
            }
            return customers;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Customer> searchCustomers(String searchString, int sortOrder) {

        StringBuilder queryCustomers = queryCustomers();

        queryCustomers.append(" AND (" + TABLE_USERS + "." + COLUMN_USERS_FULLNAME + " LIKE ? OR " + TABLE_USERS + "." + COLUMN_USERS_USERNAME + " LIKE ?)");

        if (sortOrder != ORDER_BY_NONE) {
            queryCustomers.append(" ORDER BY ");
            queryCustomers.append(COLUMN_USERS_FULLNAME);
            if (sortOrder == ORDER_BY_DESC) {
                queryCustomers.append(" DESC");
            } else {
                queryCustomers.append(" ASC");
            }
        }

        try (PreparedStatement statement = conn.prepareStatement(queryCustomers.toString())) {
            statement.setString(1, "%" + searchString + "%");
            statement.setString(2, "%" + searchString + "%");
            ResultSet results = statement.executeQuery();

            List<Customer> customers = new ArrayList<>();
            while (results.next()) {
                Customer customer = new Customer();
                customer.setId(results.getInt(1));
                customer.setFullname(results.getString(2));
                customer.setEmail(results.getString(3));
                customer.setUsername(results.getString(4));
                customer.setRentals(results.getInt(5));
                customer.setStatus(results.getString(6));
                customers.add(customer);
            }
            return customers;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteSingleCustomer(int customerId) {
        String sql = "DELETE FROM " + TABLE_USERS + " WHERE " + COLUMN_USERS_ID + " = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            int rows = statement.executeUpdate();
            System.out.println(rows + " " + TABLE_USERS + " record(s) deleted.");


            String sql2 = "DELETE FROM " + TABLE_RENTALS + " WHERE " + COLUMN_RENTALS_USER_ID + " = ?";

            try (PreparedStatement statement2 = conn.prepareStatement(sql2)) {
                statement2.setInt(1, customerId);
                int rows2 = statement2.executeUpdate();
                System.out.println(rows2 + " " + TABLE_RENTALS + " record(s) deleted.");
                return true;
            } catch (SQLException e) {
                System.out.println("Query failed: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return false;
        }
    }


}
