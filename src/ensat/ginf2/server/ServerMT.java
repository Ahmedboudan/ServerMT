
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
        private Socket socket;
        public Conversation(Socket socket,int num){
            this.socket = socket; this.num = num;
        }
        @Override
        public void run() {
            try {
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);// lire une chaine de caractere
                // Input stream lit ocktet par ocktet et l'envoie vers BufferdReader
                BufferedReader bfr = new BufferedReader(isr);
                // buffredReader va lire une chaine de caractere
                OutputStream os = socket.getOutputStream(); // Pur envoyer la reponse
                /*
                    le parametre boolean true designe que je vais lire ligne par ligne
                    si il est mis a false c a d je ne peux lire que lorsque j'appele la methode flush
                */
                PrintWriter pw = new PrintWriter(os,true);
                
                System.out.println("Connexion du client numero "+num); // affichage sur le serveur
                String ip = socket.getRemoteSocketAddress().toString();
                pw.println("Bienvenue vous etes le client numero "+num+" IP adresse : "+ip);// j'envoie au client
                while (true) {
                    String requete = bfr.readLine();
                    System.out.println("Le client "+ip+" a envoye la requete "+requete);
                    pw.println(requete.length());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    
}
