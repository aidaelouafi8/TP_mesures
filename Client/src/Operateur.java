import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Operateur {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Model model = new Model();
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
            pw.println("operateur");

            String reponse = br.readLine();
            if (reponse.startsWith("ERROR")){
                System.out.println("Échec d'authentification.");
                socket.close();
                return;
            }
            if (reponse.startsWith("OK")){
                String role = reponse.substring(3).trim();
                if (!role.equalsIgnoreCase("operateur")) {
                    System.out.println("Connexion refusé !!Impossible de connecter autant qu'un operateur dans un compte capteur.");
                    socket.close();
                    return;
                }
                System.out.println("Authentifié avec succès en tant qu'opérateur");
            }

            int choix;
            do {
                System.out.println("----------MENU CONSULTATION----------");
                System.out.println("1. Afficher toutes les mesures");
                System.out.println("2. Afficher les mesures par grandeur");
                System.out.println("3. Afficher toutes les moyennes");
                System.out.println("4. Afficher la moyenne par grandeur");
                System.out.println("5. Quitter");
                System.out.print("Veuillez saisir votre choix : ");
                choix = scanner.nextInt();
                switch (choix) {
                    case 1: {
                        ArrayList<String> mesures = model.afficheTousMesure();
                        for (int i = 0; i < mesures.size(); i++) {
                            System.out.println(mesures.get(i));
                        }
                        break;
                    }
                    case 2: {
                        do {
                            System.out.println("1. Température");
                            System.out.println("2. Pression");
                            System.out.println("3. Humidité");
                            System.out.println("4. Retour");
                            System.out.println("Donner votre choix :");
                            choix = scanner.nextInt();
                            ArrayList<String> mesure;
                            switch (choix) {
                                case 1:
                                    mesure = model.afficheMesureParGrandeur("Temperature");
                                    for (String mes : mesure) {
                                        System.out.println(mes);
                                    }
                                    break;
                                case 2:
                                    mesure = model.afficheMesureParGrandeur("Pression");
                                    for (String mes : mesure) {
                                        System.out.println(mes);
                                    }
                                    break;
                                case 3:
                                    mesure = model.afficheMesureParGrandeur("Humidite");
                                    for (String mes : mesure) {
                                        System.out.println(mes);
                                    }
                                    break;
                                case 4:
                                    break;
                                default:
                                    System.out.println("Choix invalide !");
                            }
                        } while (choix != 4);
                        break;
                    }
                    case 3: {
                        ArrayList<String> moyennes = model.afficherTousLesMoyennes();
                        for (int i = 0; i < moyennes.size(); i++) {
                            System.out.println(moyennes.get(i));
                        }
                        break;
                    }
                    case 4:
                        do {
                            System.out.println("1. Température");
                            System.out.println("2. Pression");
                            System.out.println("3. Humidité");
                            System.out.println("4. Retour");
                            System.out.println("Donner votre choix :");
                            choix = scanner.nextInt();
                            double moyenne;
                            switch (choix) {
                                case 1:
                                    moyenne = model.moyenneParGrandeur("Temperature");
                                    System.out.println(moyenne);
                                    break;
                                case 2:
                                    moyenne = model.moyenneParGrandeur("Pression");
                                    System.out.println(moyenne);
                                    break;
                                case 3:
                                    moyenne = model.moyenneParGrandeur("Humidite");
                                    System.out.println(moyenne);
                                    break;
                                case 4:
                                    break;
                                default:
                                    System.out.println("Choix invalide !");
                            }
                        } while (choix != 4);
                        break;
                    case 5:
                        System.out.println("Vous avez quitter le programme");
                        break;
                    default:
                        System.out.println("Choix invalide !");

                }
            } while (choix != 5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
