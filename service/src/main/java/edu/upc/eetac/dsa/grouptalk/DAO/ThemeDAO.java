package edu.upc.eetac.dsa.grouptalk.DAO;

import edu.upc.eetac.dsa.grouptalk.entity.Group;
import edu.upc.eetac.dsa.grouptalk.entity.Theme;
import edu.upc.eetac.dsa.grouptalk.entity.ThemeCollection;

import java.sql.SQLException;

/**
 * Created by marc on 9/03/16.
 */
public interface ThemeDAO {
    public boolean isInGroup (String userid, String groupid) throws SQLException;
    public ThemeCollection getThemesByGroup(String groupid, String userid) throws SQLException, UserNotInscribedException;
    public Theme createTheme(String subject, String userid, String groupid) throws SQLException, ThemeAlreadyExistsException, UserNotInscribedException;
    public Theme getThemeByID(String id) throws SQLException;
    public boolean deleteThemeandStingsbyUser(String userid, String themeid) throws SQLException, UserNoGenuineUpdateStingException;
    public boolean deleteThemeandStingsbyAdmin(String userid, String themeid) throws SQLException;
}
