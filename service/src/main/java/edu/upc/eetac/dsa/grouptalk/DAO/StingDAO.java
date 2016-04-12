package edu.upc.eetac.dsa.grouptalk.DAO;

import edu.upc.eetac.dsa.grouptalk.entity.Sting;
import edu.upc.eetac.dsa.grouptalk.entity.StingCollection;

import java.sql.SQLException;

/**
 * Created by marc on 2/03/16.
 */
public interface StingDAO {
    public Sting createSting(String userid, String content, String groupid) throws SQLException;
    public Sting getStingById(String id) throws SQLException;
    public StingCollection getStings(String themeid) throws SQLException;
    public Sting updateSting(String id, String subject, String content) throws SQLException, UserNoGenuineUpdateStingException;
    public boolean genuineUser(String userid, String stingid) throws SQLException;
    public boolean deleteSting(String id, String userid) throws SQLException, UserNoGenuineUpdateStingException;
}
