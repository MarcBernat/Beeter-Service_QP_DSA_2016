package edu.upc.eetac.dsa.grouptalk.DAO;

/**
 * Created by marc on 9/03/16.
 */
public interface ThemeDAOQuery {
    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String GET_THEMES_BY_GROUP = "select hex(id), hex(userid), subject, last_modified, creation_timestamp from theme inner join group_theme_rel on theme.id = group_theme_rel.themeid where group_theme_rel.groupid = unhex(?)";
    public final static String CHECK_USER_IN_GROUP = "select hex(groupid) from group_user_rel where userid=unhex(?)";
    public final static String CREATE_THEME = "insert into theme (id, userid, subject) values (UNHEX(?), UNHEX(?), ?);";
    public final static String GET_THEMES_BY_ID = "select hex(id), hex(userid), subject, last_modified, creation_timestamp from theme where id=unhex(?)";
    public final static String CREATE_REL_THEME_GROUP = "insert into group_theme_rel (groupid, userid) values (UNHEX(?), UNHEX(?));";
    public final static String CHECK_USER_OF_THEME = "select hex(userid) from theme where id=unhex(?)";
    public final static String GET_STINGS_BY_THEME = "select hex(id), hex(userid), content, last_modified, creation_timestamp from sting inner join theme_sting_rel on sting.id = theme_sting_rel.stingid where theme_sting_rel.themeid = unhex(?)";
    public final static String DELETE_STINGS = "delete from stings where id=unhex(?)";
    public final static String DELETE_STINGS_REL_THEME = "delete from theme_sting_rel where themeid=unhex(?) and stingid=unhex(?)";
    public final static String DELETE_THEME = "delete from theme where id=unhex(?)";
}

