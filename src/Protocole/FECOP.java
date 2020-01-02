package Protocole;

import DataBase.Utilities.MySqlBean;
import Request.RequestControlID;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class FECOP
{
    private MySqlBean _bdAccess = new MySqlBean();
    private String _passwordBD;
    private String _userBD;
    private int _portBD;
    private String _nameBD;
    private String _ipBD;

    // constructeur
    public FECOP() {

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
        System.out.println("Client connecte sur BD_FERRIES");
    }

    public RequestControlID AnalyseRequete(RequestControlID request, String _separator, String _endOfLine) {

        RequestControlID response_commande = new RequestControlID();

        switch(request.getType()){
            case RequestControlID.REQUEST_INFO_COURS:

//                int nbMonnDiff = 0;
//                ArrayList<String> list_noms_mon = new ArrayList<>();
//                ArrayList<String> list_cours_mon = new ArrayList<>();
//
//                try
//                {
//                    // 1. Recup infos sur nombre et chaque monnaie
//                    String reqSqlInfosMonaies = "select * from cours_monetaires";
//                    ResultSet rs = _bdAccess.SelectQuery(reqSqlInfosMonaies);
//
//                    while(rs.next())
//                    {
//                        nbMonnDiff++;
//
//                        String nom_monnaie = rs.getString("nom_monnaie");
//                        String cours_monnaie = rs.getString("cours_monnaie");
//
//                        System.out.println(rs.getString("nom_monnaie")
//                                + " - " + rs.getString("cours_monnaie"));
//
//                        list_noms_mon.add(nom_monnaie);
//                        list_cours_mon.add(cours_monnaie);
//                    }
//
//
//                    // construire reponse au client
//                    response_commande = "ACK" + _separator + nbMonnDiff;
//
//                    for(int i = 0; i < list_noms_mon.size(); i++)
//                    {
//                        response_commande += _separator;
//                        response_commande += list_noms_mon.get(i) + "|" + list_cours_mon.get(i);
//
//                    }
//
//
//
//                } catch (SQLException e1) {
//                    e1.printStackTrace();
//                }
                break;

            default :

                response_commande.setType(RequestControlID.REQUEST_INCONNU);
                response_commande.setData("FAIL");
                break;


        }

        return response_commande;

    }

}
