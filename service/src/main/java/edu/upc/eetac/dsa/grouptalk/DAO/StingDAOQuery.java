package edu.upc.eetac.dsa.grouptalk.DAO;

/**
 * Created by marc on 2/03/16.
 */
public interface StingDAOQuery {
    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_STING = "insert into stings (id, userid, content) values (UNHEX(?), UNHEX(?), ?)";
    public final static String GET_STING_BY_ID = "select hex(s.id) as id, hex(s.userid) as userid, s.content, s.subject, s.creation_timestamp, s.last_modified, u.fullname from stings s, users u where s.id=unhex(?) and u.id=s.userid";
    public final static String GET_STINGS_BY_THEME = "select hex(id), hex(userid), content, last_modified, creation_timestamp from sting inner join theme_sting_rel on sting.id = theme_sting_rel.stingid where theme_sting_rel.themeid = unhex(?)";
    public final static String UPDATE_STING = "update stings set content=? last_modified=? where id=unhex(?)";
    public final static String DELETE_STING = "delete from stings where id=unhex(?)";
    public final static String CREATE_REL_STING_THEME = "insert into theme_sting_rel (themeid, stingid) values (UNHEX(?), UNHEX(?));";
    public final static String CHECK_USER_IN_STING = "select hex(userid) from sting where id=unhex(?)";
}
