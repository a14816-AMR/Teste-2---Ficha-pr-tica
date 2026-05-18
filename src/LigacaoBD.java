import java.sql.Connection;
import java.sql.DriverManager;

public class LigacaoBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/appdb";

    private static final String USER = "appuser";
    private static final String PASSWORD = "apppass";

    public static Connection ligar() {

        try {
            Connection con = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );
            return con;
        } catch (Exception e) {
            System.out.println("Erro ao ligar à base de dados!");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("A testar ligação à base de dados...");
        Connection con = ligar();
        if (con != null) {
            System.out.println("Ligação realizada com sucesso!");
            try {
                con.close();
                System.out.println("Ligação fechada.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Falha na ligação!");
        }
    }
}