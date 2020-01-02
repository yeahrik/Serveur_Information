package Serveur;

import Protocole.FECOP;
import Protocole.INFOP;
import Request.RequestControlID;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;


public class ProcessingClient implements Runnable {
    private Socket _socket;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private INFOP _protocoleINFOP;
    private FECOP _protocoleFECOP;

    private String _separator;
    private String _endOfLine;

    public ProcessingClient(Socket pSock, String _sep, String _eof, ObjectOutputStream oos, ObjectInputStream ois){

        _socket = pSock;
        _separator = _sep;
        _endOfLine = _eof;
        _protocoleINFOP = new INFOP();
        _protocoleFECOP = new FECOP();

    }

    //Le traitement lancé dans un thread séparé
    public void run(){
        System.out.println("Lancement du traitement de la connexion cliente");
        System.out.println("Thread numero = " + Thread.currentThread().getId());

        try {
            ois = new ObjectInputStream(_socket.getInputStream());
            oos = new ObjectOutputStream(_socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //tant que la connexion est active, on traite les demandes
        while(!_socket.isClosed()){

            try {

                //On attend la demande du client
                RequestControlID response = read();
                InetSocketAddress remote = (InetSocketAddress) _socket.getRemoteSocketAddress();

                //On affiche quelques infos, pour le débuggage
                String debug = "";
                debug = "Thread : " + Thread.currentThread().getName() + ". ";
                debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
                debug += " Sur le port : " + remote.getPort() + ".\n";
                debug += "\t -> Commande reçue : " + response + "\n";
                System.out.println("\n" + debug);

                //On traite la demande du client en fonction de la commande envoyée
                RequestControlID toSend;

                if(response.getProtocole().equals("INFOP")){
                    toSend = _protocoleINFOP.AnalyseRequete(response, _separator, _endOfLine);
                } else {
                    toSend = _protocoleFECOP.AnalyseRequete(response, _separator, _endOfLine);
                }

                //On envoie la réponse au client
                oos.writeObject(toSend);
                oos.flush();
                //Il FAUT IMPERATIVEMENT UTILISER flush()
                //Sinon les données ne seront pas transmises au client
                //et il attendra indéfiniment


            }catch(SocketException e){
                System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //La méthode que nous utilisons pour lire les réponses
    private RequestControlID read() throws IOException{

        RequestControlID response = null;

        try {
            response = (RequestControlID) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return response;


    }

}
