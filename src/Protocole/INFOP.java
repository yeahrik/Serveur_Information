package Protocole;

import DataBase.Utilities.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class INFOP
{
    private MySqlBean _bdAccess = new MySqlBean();
    private String _passwordBD;
    private String _userBD;
    private int _portBD;
    private String _nameBD;
    private String _ipBD;

    // constructeur
    public INFOP() {

        try
        {
            Properties _propFile = new Properties();
            InputStream _inStream;
            _inStream = new FileInputStream("config.properties");
            _propFile.load(_inStream);
            _passwordBD = _propFile.getProperty("BD_PASSWORD");
            _userBD = _propFile.getProperty("BD_USER");
            _portBD = Integer.parseInt(_propFile.getProperty("BD_PORT"));
            _nameBD = _propFile.getProperty("BD_NAME");
            _ipBD = _propFile.getProperty("BD_IP");
            _inStream.close();
        }
        catch (IOException e)
        {
            System.err.println("Error Reading Properties Files [" + e + "]");
        }

        //Récup infos de connexion
        _bdAccess.set_ip(_ipBD);
        _bdAccess.set_user(_userBD);
        _bdAccess.set_password(_passwordBD);
        _bdAccess.set_port(_portBD);
        _bdAccess.set_database(_nameBD);

        //Connection
        _bdAccess.ConnectDB();
        System.out.println("Client connecte sur BD_KITCHEN");
    }

    public String AnalyseRequete(String response, String _separator, String _endOfLine) {

        String tokfull = response.replaceAll(_endOfLine, "");
        String[] tok = tokfull.split(_separator);
        String requete = tok[0];

        String reponse_commande = "FAIL";

        switch(requete.toUpperCase()){
            case "LOGIN":////////////////
                String username = tok[1];
                String password = tok[2];
                String password_bd;

                try
                {
                    String reqSql = "select username, password from user where username = '" + username + "'";
                    System.out.println("Requete : " + reqSql);
                    ResultSet rs = _bdAccess.SelectQuery(reqSql);

                    rs.next();

                    if(rs.getString("username").equals(""))
                    {
                        // user n'existe pas
                        System.out.println("Cet utilisateur n'existe pas\n");
                    } else {
                        password_bd = rs.getString("password");

                        if(!password.equals(password_bd))
                        {
                            // password incorrect
                            System.out.println("Mot de passe incorrect\n");
                        } else {
                            // OK
                            reponse_commande = "ACK";
                        }
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                break;

            default :

                reponse_commande = "AnalyseCommande : Commande inconnue";
                break;
        }

        return reponse_commande;

    }

}
