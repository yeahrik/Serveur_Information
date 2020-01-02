package Serveur;

import Protocole.INFOP;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;


public class ProcessingClient implements Runnable {
    private Socket sock;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    private INFOP protocole;
    private String _separator;
    private String _endOfLine;

    public ProcessingClient(Socket pSock, String _sep, String _eof){

        sock = pSock;
        _separator = _sep;
        _endOfLine = _eof;
        protocole = new INFOP();
    }

    //Le traitement lancé dans un thread séparé
    public void run(){
        System.out.println("Lancement du traitement de la connexion cliente");
        System.out.println("Thread numero = " + Thread.currentThread().getId());
        //tant que la connexion est active, on traite les demandes
        while(!sock.isClosed()){

            try {

                writer = new PrintWriter(sock.getOutputStream());
                reader = new BufferedInputStream(sock.getInputStream());

                //On attend la demande du client
                String response = read();
                InetSocketAddress remote = (InetSocketAddress)sock.getRemoteSocketAddress();

                //On affiche quelques infos, pour le débuggage
                String debug = "";
                debug = "Thread : " + Thread.currentThread().getName() + ". ";
                debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
                debug += " Sur le port : " + remote.getPort() + ".\n";
                debug += "\t -> Commande reçue : " + response + "\n";
                System.out.println("\n" + debug);

                //On traite la demande du client en fonction de la commande envoyée
                String toSend = "";
                toSend = protocole.AnalyseRequete(response, _separator, _endOfLine);


                //On envoie la réponse au client
                writer.write(toSend);
                //Il FAUT IMPERATIVEMENT UTILISER flush()
                //Sinon les données ne seront pas transmises au client
                //et il attendra indéfiniment
                writer.flush();


            }catch(SocketException e){
                System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //La méthode que nous utilisons pour lire les réponses
    private String read() throws IOException{
        String response = "";
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b, 0, stream);
        return response;
    }

}
