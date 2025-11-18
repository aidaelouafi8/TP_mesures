import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class Communication extends Thread {
    private Socket clientSocket;
    public Communication (Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {

            InputStream is = clientSocket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            OutputStream os = clientSocket.getOutputStream();
            PrintWriter pw = new PrintWriter(os,true);

            Model model = new Model();

            String username = br.readLine();
            String password = br.readLine();
            String clientRole = br.readLine();
            String role = model.verifLogin(username, password);

            if (role == null || !clientRole.equalsIgnoreCase(role)) {
                pw.println("ERROR");
                System.out.println("Authentification échouée pour : " + username);
                clientSocket.close();
                return;
            }
            else {
                pw.println("OK:" + role);
                System.out.println("Authentification réussie pour : " + username + " (rôle=" + role + ")");
            }

            if (role.equalsIgnoreCase("capteur")){
                while (true) {
                    int numCapteur = Integer.parseInt(br.readLine());
                    double Temperature = Double.parseDouble(br.readLine());
                    double Pression = Double.parseDouble(br.readLine());
                    double Humidite = Double.parseDouble(br.readLine());

                    model.stockerMesure(numCapteur, Temperature, Pression, Humidite);
                }
            } else {
                System.out.println("Opérateur connecté (authentifié) : " + username + " - rôle : " + role);
            }

        } catch (IOException e) {
            System.out.println("Erreur dans la communication avec le client : " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
