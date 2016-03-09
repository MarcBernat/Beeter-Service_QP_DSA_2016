package edu.upc.eetac.dsa.grouptalk.DAO;

/**
 * Created by marc on 3/03/16.
 */
public interface GroupDAOQuery {
    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_GROUP = "insert into groups (id, subject) values (UNHEX(?), ?);";
    public final static String GET_GROUP_BY_ID = "select hex(u.id) as id, u.subject, u.last_modified, u.creation_timestamp from groups u where id=unhex(?)";
    public final static String GET_GROUPS_ALL = "select hex(u.id) as id, u.subject, u.last_modified, u.creation_timestamp from groups";
    public final static String CHECK_USER_IN_GROUP = "select hex(groupid) from group_user_rel where userid=unhex(?)";
}
