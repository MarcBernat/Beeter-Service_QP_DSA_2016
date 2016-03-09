package edu.upc.eetac.dsa.grouptalk.DAO;

import edu.upc.eetac.dsa.grouptalk.entity.Group;
import edu.upc.eetac.dsa.grouptalk.entity.GroupCollection;
import edu.upc.eetac.dsa.grouptalk.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by marc on 3/03/16.
 */
public class GroupDAOImpl implements GroupDAO {
    @Override
    public Group createGroup(String subject) throws SQLException, GroupAlreadyExistsException{
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GroupDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();

            stmt = connection.prepareStatement(GroupDAOQuery.CREATE_GROUP);
            stmt.setString(1, id);
            stmt.setString(2, subject);
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
        return getGroupById(id);
    }

    @Override
    public Group getGroupById(String id) throws SQLException{
        Group group = null;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GroupDAOQuery.GET_GROUP_BY_ID);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                group = new Group();
                group.setId(rs.getString("id"));
                group.setSubject(rs.getString("subject"));
                group.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                group.setLastModified(rs.getTimestamp("last_modified").getTime());
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return group;
    }
    @Override
    public GroupCollection getGroups() throws SQLException{
        GroupCollection GroupCollection = new GroupCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(GroupDAOQuery.GET_GROUPS_ALL);

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                Group Group = new Group();
                Group.setId(rs.getString("id"));
                Group.setSubject(rs.getString("subject"));
                Group.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                Group.setLastModified(rs.getTimestamp("last_modified").getTime());
                if (first) {
                    GroupCollection.setNewestTimestamp(Group.getLastModified());
                    first = false;
                }
                GroupCollection.setOldestTimestamp(Group.getLastModified());
                GroupCollection.getGroups().add(Group);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return GroupCollection;
    }
    @Override
    public boolean isInGroup (String userid, String groupid) throws SQLException{
        Connection connection = null;
        PreparedStatement stmt = null;
        String group = null;
        boolean in = false;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GroupDAOQuery.CHECK_USER_IN_GROUP);
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


}
