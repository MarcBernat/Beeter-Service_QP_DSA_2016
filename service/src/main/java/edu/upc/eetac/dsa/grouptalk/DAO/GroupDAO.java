package edu.upc.eetac.dsa.grouptalk.DAO;

import edu.upc.eetac.dsa.grouptalk.entity.Group;
import edu.upc.eetac.dsa.grouptalk.entity.GroupCollection;
import edu.upc.eetac.dsa.grouptalk.entity.User;

import java.sql.SQLException;

/**
 * Created by marc on 3/03/16.
 */
public interface GroupDAO {
    public Group createGroup(String subject) throws SQLException, GroupAlreadyExistsException;
    public Group getGroupById(String id) throws SQLException;
    public GroupCollection getGroups() throws SQLException;
    public boolean isInGroup(String userid, String groupid) throws SQLException;
}
