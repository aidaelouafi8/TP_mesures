import java.sql.*;
import java.util.ArrayList;

public class Model {
    public Connection connexion() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/capteurs";
        String user = "root";
        String password = "passroot";
        Connection c = DriverManager.getConnection(url, user, password);
        return c;
    }

    //Partie Login ;
    public String verifLogin (String username, String password) throws SQLException, ClassNotFoundException {
        Connection c = this.connexion();
        String query = "SELECT * FROM users WHERE username=? AND password=?";
        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        String role = null;
        if (rs.next()) {
            role = rs.getString("role");
        }
        return role;
    }

    //Côté client : c'est la fonction que le client peut envoyer les données de mesure :
    public void stockerMesure (int numCapteur, double Temperature, double Pression, double Humidite) throws SQLException, ClassNotFoundException {
        Connection c = this.connexion();
        String query = "INSERT INTO mesure (NumCapteur, Temperature, Pression, Humidite) VALUES (?,?,?,?)";
        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1,numCapteur);
        ps.setDouble(2,Temperature);
        ps.setDouble(3,Pression);
        ps.setDouble(4,Humidite);
        ps.executeUpdate();
    }

    //Côté Opérateur : c'est les fonctions que l'Opérateur peut faire :
    //Afficher tous les mesures :
    public ArrayList<String> afficheTousMesure () throws SQLException, ClassNotFoundException {
        ArrayList<String> mesures = new ArrayList<>();
        Connection c = this.connexion();
        String query = "SELECT * FROM mesure LIMIT 10";
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String id = rs.getString(1);
            String numCapteur = rs.getString("NumCapteur");
            String temp = rs.getString("Temperature");
            String pression = rs.getString("Pression");
            String humidite = rs.getString("Humidite");
            String heure = rs.getString("date");
            mesures.add("Capteur N°"+numCapteur+" : " + "Température = "+temp+", Pression = "+pression+", Humidité = "+humidite+", date = "+heure);
        }
        return mesures;
    }
    //Filtrer les mesures par grandeur :
    public ArrayList<String> afficheMesureParGrandeur (String Type) throws SQLException, ClassNotFoundException {
        ArrayList<String> mesure = new ArrayList<>();
        Connection c = this.connexion();
        String query = "SELECT NumCapteur,"+Type+" FROM mesure LIMIT 10";
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String numCapteur = rs.getString("NumCapteur");
            double valeur = rs.getDouble(Type);
            mesure.add("Capteur "+numCapteur+ " : " +Type+ " = " +valeur);
        }
        return mesure;
    }
    //Calculer toute les moyennes générales :
    public ArrayList<String> afficherTousLesMoyennes () throws SQLException, ClassNotFoundException {
        ArrayList<String>moyennes = new ArrayList<>();
        Connection c = this.connexion();
        String query = "SELECT AVG(Temperature) AS moyTemperature, AVG(Pression) AS moyPression, AVG(Humidite) AS moyHumidite FROM mesure";
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()){
            double moyTemp = rs.getDouble("moyTemperature");
            double moyPress = rs.getDouble("moyPression");
            double moyHumid = rs.getDouble("moyHumidite");

            moyennes.add("Moyenne Température = "+moyTemp);
            moyennes.add("Moyenne Pression = "+moyPress);
            moyennes.add("Moyenne Humidité = "+moyHumid);
        }
        return moyennes;
    }
    //Calculer la moyenne par chaque grandeur choisi :
    public double moyenneParGrandeur (String Type) throws SQLException, ClassNotFoundException{
        Connection c = this.connexion();
        String query = "SELECT AVG("+Type+") AS moyenne FROM mesure";
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            return rs.getDouble("moyenne");
        }

        return 0.0;
    }
}