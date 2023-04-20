
package ensat.ginf2.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.net.ServerSocket;
import java.net.*;
import java.util.*;

/**
 *
 * @author ahmed
 */
public class ServerMT extends Thread{
    private boolean active=true;
    int nbr_client;
    public static void main(String[] args) {
        // l'appel de la methode start() va executer la methode run()
        // et dans la methode run() on demarre le serveur
        new ServerMT().start();
    }

    @Override
    public void run() {
        ServerSocket ss;
        try {
            ss = new ServerSocket(1232); // tcp
            System.out.println("Demarrage du serveur...");
            while(active){
           Socket socket = ss.accept();
           ++nbr_client;
           new Conversation(socket,nbr_client).start();
           // Pour chaque client qui vient de se conneceter je cree un thread : la Conversation herite de la classe Thread
                
         }
        } catch (IOException ex) {
            System.err.println("Erreur: "+ex.getMessage());
        }
        
    }
    class Conversation extends Thread{
        int num;
        private boolean connected=true;
        private Socket socket;
        public Conversation(Socket socket,int num){
            this.socket = socket; this.num = num;
        }
        @Override
        public void run() {
            try {
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader bfr = new BufferedReader(isr);
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os,true);

                System.out.println("Connexion du client numero "+num);
                String ip = socket.getRemoteSocketAddress().toString();
                pw.println("Bienvenue vous etes le client numero "+num+" IP adresse : "+ip);
                String requete;
                while (connected && (requete = bfr.readLine()) != null) {
                    if(requete.equals("exit")){
                        connected = false;
                        System.out.println("Le client "+ip+" s'est deconnecte");
                    }
                    else{
                        System.out.println("Le client "+ip+" a envoye la requete "+requete);
                        pw.println("La longueur de votre message est "+requete.length());
                    }
                }
                socket.close(); // fermer le socket une fois la communication terminée
            } catch (Exception e) {
                e.printStackTrace();
            }
          }
} 
}
