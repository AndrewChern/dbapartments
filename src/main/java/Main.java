import java.sql.*;
import java.util.Scanner;

public class Main {

    static Connection conn;

    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);
        DbProperties props = new DbProperties();
        conn = DriverManager.getConnection(props.getUrl(), props.getUser(), props.getPassword());
        initDB();
        insertSomeApartments("Primorsky", "Deribasovskaya 1", 60.0, 2, 50000);
        insertSomeApartments("Suvorovsky", "Dobrovolskogo 1", 60.0, 2, 30000);
        insertSomeApartments("Malinovsky", "Tereshkova 1", 30.0, 1, 15000);

        while (true) {
            System.out.println("1: add apartment");
            System.out.println("2: get all apartments");
            System.out.println("3: get apartment by params");
            System.out.print("-> ");
            String s = scanner.nextLine();
            switch (s) {
                case "1":
                    addApartment(scanner);
                    break;
                case "2":
                    getAllApartments(scanner);
                    break;
                case "3":
                    getApartmentByParams(scanner);
                    break;

                default:
                    return;
            }
        }

    }

        private static void initDB ()throws SQLException {

        Statement st = conn.createStatement();

                try {
                    st.execute("DROP TABLE IF EXISTS apartments");
                    st.execute("CREATE TABLE apartments (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                                "district VARCHAR(100) NOT NULL," +
                                "address VARCHAR(100) NOT NULL," +
                                "square DOUBLE DEFAULT NULL," +
                                "rooms TINYINT DEFAULT NULL," +
                                "price DOUBLE DEFAULT NULL)");
                } finally {
                        st.close();
                }
        }

    private static void insertSomeApartments (String district, String address, double square, int rooms, double price)throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO apartments (district, address, square, rooms, price) VALUES(?, ?, ?, ?, ?)");
        try {
            ps.setString(1, district);
            ps.setString(2, address);
            ps.setDouble(3, square);
            ps.setInt(4, rooms);
            ps.setDouble(5, price);

            ps.executeUpdate();
        } finally {
            ps.close();
        }
    }

        private static void addApartment(Scanner scanner) throws SQLException {
            System.out.print("Enter apartment's district: ");
            String district = scanner.nextLine();
            System.out.print("Enter apartment's address: ");
            String address = scanner.nextLine();
            System.out.print("Enter apartment's square: ");
            String sSquare = scanner.nextLine();
            double square = Double.parseDouble(sSquare);
            System.out.print("Enter apartment's number of rooms: ");
            String sRooms = scanner.nextLine();
            int rooms = Integer.parseInt(sRooms);
            System.out.print("Enter apartment's price: ");
            String sPrice = scanner.nextLine();
            double price = Double.parseDouble(sPrice);


            PreparedStatement ps = conn.prepareStatement("INSERT INTO apartments (district, address, square, rooms, price) VALUES(?, ?, ?, ?, ?)");
            try {
                ps.setString(1, district);
                ps.setString(2, address);
                ps.setDouble(3, square);
                ps.setInt(4, rooms);
                ps.setDouble(5, price);

                ps.executeUpdate();
            } finally {
                ps.close();
            }
        }

        private static void getAllApartments(Scanner scanner) throws SQLException {

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM apartments");

            try {
                ResultSet rs = ps.executeQuery();
                try {
                    ResultSetMetaData md = rs.getMetaData();

                    for (int i = 1; i <= md.getColumnCount(); i++)
                        System.out.print(md.getColumnName(i) + "\t\t");
                    System.out.println();

                    while (rs.next()) {
                        for (int i = 1; i <= md.getColumnCount(); i++) {
                            System.out.print(rs.getString(i) + "\t\t");
                        }
                        System.out.println();
                    }
                } finally {
                    rs.close();
                }
            } finally {
                ps.close();
            }
        }

        private static void getApartmentByParams(Scanner scanner) throws SQLException {

            StringBuilder query = new StringBuilder("SELECT district, address, square, rooms, price FROM apartments WHERE ");

            System.out.println("Select search by criterion:");
            System.out.println("1: apartment's district");
            System.out.println("2: apartment's price");
            System.out.println("3: apartment's number of rooms");
            System.out.print("-> ");
            String s = scanner.nextLine();
            switch (s) {
                case "1":
                    System.out.println("input apartment's district");
                    String district = scanner.nextLine();
                    query.append("district = '").append(district).append("'");
                    break;
                case "2":
                    System.out.println("input apartment's price less then");
                    String price = scanner.nextLine();
                    query.append("price < '").append(price).append("'");
                    break;
                case "3":
                    System.out.println("input apartment's number of rooms less then");
                    String rooms = scanner.nextLine();
                    query.append("price < '").append(rooms).append("'");
                    break;
            }
            PreparedStatement ps = conn.prepareStatement(query.toString());
            try {
                ResultSet rs = ps.executeQuery();
                try {
                    ResultSetMetaData md = rs.getMetaData();

                    for (int i = 1; i <= md.getColumnCount(); i++)
                        System.out.print(md.getColumnName(i) + "\t\t");
                    System.out.println();

                    while (rs.next()) {
                        for (int i = 1; i <= md.getColumnCount(); i++) {
                            System.out.print(rs.getString(i) + "\t\t");
                        }
                        System.out.println();
                    }
                } finally {
                    rs.close();
                }
            } finally {
                ps.close();
            }
        }
}
