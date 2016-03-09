package edu.upc.eetac.dsa.grouptalk.DAO;

import edu.upc.eetac.dsa.grouptalk.entity.Theme;
import edu.upc.eetac.dsa.grouptalk.entity.ThemeCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by marc on 9/03/16.
 */
public class ThemeDAOImpl implements ThemeDAO {

    @Override
    public boolean isInGroup (String userid, String groupid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String group = null;
        boolean in = false;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(ThemeDAOQuery.CHECK_USER_IN_GROUP);
            stmt.setString(1, userid);
            ResultSet rs = stmt.executeQuery();
            /**
             * Sacamos los grupos inscritos por el usuario y comprobamos si alguno
             * de ellos coincide con el grupo pasado como valor
             */

            while (rs.next())
            {
                group = rs.getString(1);
                if(groupid == group)
                {
                    in = true;
                }
            }

            return in;

        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

    @Override
    public ThemeCollection getThemesByGroup(String groupid, String userid) throws SQLException, UserNotInscribedException {
        ThemeCollection themeCollection = new ThemeCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            if (isInGroup(userid, groupid)) {
                throw new UserNotInscribedException();
            }

            connection = Database.getConnection();
            stmt = connection.prepareStatement(ThemeDAOQuery.GET_THEMES_BY_GROUP);
            stmt.setString(1, groupid);

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                Theme theme = new Theme();
                theme.setId(rs.getString("id"));
                theme.setUserid(rs.getString("userid"));
                theme.setSubject(rs.getString("subject"));
                theme.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                theme.setLastModified(rs.getTimestamp("last_modified").getTime());
                if (first) {
                    themeCollection.setNewestTimestamp(theme.getLastModified());
                    first = false;
                }
                themeCollection.setOldestTimestamp(theme.getLastModified());
                themeCollection.getThemes().add(theme);


            }
            return themeCollection;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public Theme createTheme(String subject, String userid, String groupid) throws SQLException, ThemeAlreadyExistsException, UserNotInscribedException
    {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try {
            connection = Database.getConnection();
            //Comprobación
            if (isInGroup(userid, groupid)) {
                throw new UserNotInscribedException();
            }
            stmt = connection.prepareStatement(ThemeDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();

            stmt = connection.prepareStatement(ThemeDAOQuery.CREATE_THEME);
            stmt.setString(1, id);
            stmt.setString(2, userid);
            stmt.setString(3, subject);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return getThemeByID(id);

    }

    @Override
    public Theme getThemeByID(String id) throws SQLException
    {
        Theme theme = null;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GroupDAOQuery.GET_GROUP_BY_ID);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                theme = new Theme();
                theme.setId(rs.getString("id"));
                theme.setUserid(rs.getString("userid"));
                theme.setSubject(rs.getString("subject"));
                theme.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                theme.setLastModified(rs.getTimestamp("last_modified").getTime());
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return theme;
    }
}
