package Serveur;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class LaunchServer {

    public static void main(String[] args) throws IOException {

        String _separator = "";
        String _endOfLine = "";
        String _port = "";
        String _host = "";

        try
        {
            Properties _propFile = new Properties();
            InputStream _inStream = null;
            _inStream = new FileInputStream("config.properties");
            _propFile.load(_inStream);
            _host = _propFile.getProperty("HOST");
            _separator = _propFile.getProperty("SEPARATOR");
            _endOfLine = _propFile.getProperty("ENDOFLINE");
            _port = _propFile.getProperty("PORT_INFO");

            _inStream.close();
        }
        catch (IOException e)
        {
            System.err.println("Error Reading Properties Files [" + e + "]");
        }

        ServeurInformation ts = new ServeurInformation(_host, Integer.parseInt(_port), _separator, _endOfLine);
        ts.open();

        System.out.println("Serveur initialisé.");
    }
}
