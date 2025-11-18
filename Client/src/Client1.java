import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class Client1 {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        try {
            Socket socket = new Socket("127.0.0.1", 1234);

            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);


            System.out.print("Nom d'utilisateur : ");
            String username = scanner.nextLine();
            System.out.print("Mot de passe : ");
            String password = scanner.nextLine();
            pw.println(username);
            pw.println(password);
            pw.println("capteur");

            String reponse = br.readLine();
            if (reponse.startsWith("ERROR")) {
                System.out.println("Echec d'authentification");
                socket.close();
                return;
            }
            if (reponse.startsWith("OK")) {
                String role = reponse.substring(3).trim();
                if (!role.equalsIgnoreCase("capteur")) {
                    System.out.println("Connexion refusé !!Impossible de connecter autant qu'un operateur dans un compte capteur.");
                }
                System.out.println("Authentifié avec succès en tant que capteur");
            }

            Random rand = new Random();
            int numCapteur = 1;

            while (true) {
                double temperature = rand.nextDouble(41); //0-40
                double Pression = rand.nextDouble(60)+950; //870-1013
                double Humidite = rand.nextDouble(101); // 0-100

                pw.println(numCapteur);
                pw.println(temperature);
                pw.println(Pression);
                pw.println(Humidite);

                System.out.println("Envoyé -> Temp: " + temperature +", Pression: " + Pression +", Humidité: " + Humidite);

                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}