package edu.upc.eetac.dsa.grouptalk.DAO;

import edu.upc.eetac.dsa.grouptalk.entity.User;

import java.sql.SQLException;

/**
 * Created by marc on 2/03/16.
 */
public interface UserDAO {
        public User createUser(String loginid, String password, String email, String fullname) throws SQLException, UserAlreadyExistsException;

        public User updateProfile(String id, String email, String fullname) throws SQLException;

        public User getUserById(String id) throws SQLException;

        public User getUserByLoginid(String loginid) throws SQLException;

        public boolean deleteUser(String id) throws SQLException;

        public boolean checkPassword(String id, String password) throws SQLException;

        public boolean signUpGroup(String id, String idgroup) throws SQLException;

        public boolean signDownGroup(String id, String idgroup) throws SQLException;
}