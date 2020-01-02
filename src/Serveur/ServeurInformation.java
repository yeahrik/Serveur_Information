package Serveur;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServeurInformation
{

    //On initialise des valeurs par défaut
    private int _port = 2345;
    private String _host = "127.0.0.1";
    private ServerSocket _server = null;
    private String _separator;
    private String _endOfLine;
    private boolean _isRunning = true;
    private int nbThreadMax = 10;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    protected ExecutorService threadPool = ///
            Executors.newFixedThreadPool(nbThreadMax);///

    public ServeurInformation(String pHost, int pPort, String separator, String endOfLine)
    {
        _port = pPort;
        _separator = separator;
        _endOfLine = endOfLine;
        try {
            _server = new ServerSocket(_port, 100, InetAddress.getByName(_host));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //On lance notre serveur
    public void open(){

        //Toujours dans un thread à part vu qu'il est dans une boucle infinie
        Thread t = new Thread(new Runnable(){
            public void run(){
                while(_isRunning == true){

                    try {
                        //On attend une connexion d'un client
                        Socket client = _server.accept();

                        //Une fois reçue, on la traite dans un thread séparé
                        System.out.println("Connexion cliente reçue.");

                        threadPool.execute(///
                                new ProcessingClient(client, _separator, _endOfLine, oos, ois));


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    _server.close();
                    threadPool.shutdown();

                } catch (IOException e) {
                    e.printStackTrace();
                    _server = null;
                }
            }
        });

        t.start();
    }

    public void close(){
        _isRunning = false;
    }
}
