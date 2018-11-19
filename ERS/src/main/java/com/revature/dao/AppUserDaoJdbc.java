package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.model.AppUser;
import com.revature.model.Reimbursement;
import com.revature.util.ConnectionUtil;

public class AppUserDaoJdbc implements AppUserDao {

	private Logger log = Logger.getRootLogger();

	/**
	 * Given an SQL ResultSet object, create and return a new AppUser object,
	 * populating its fields with the data retrieved from the database
	 * 
	 * @param rs - an SQL result set containing the details of a single AppUser
	 * @return a new AppUser object
	 * @throws SQLException
	 */
	private AppUser extractFromResultSet(ResultSet rs) throws SQLException {
		log.trace("AppUserDaoJdbc.extractFromResultSet()");
		return new AppUser(rs.getInt("ers_users_id"), rs.getString("ers_username"), rs.getString("ers_password"),
				rs.getString("user_first_name"), rs.getString("user_last_name"), rs.getString("user_email"),
				rs.getString("user_role"));
	}

	/**
	 * Given an SQL ResultSet object, create and return a new Reimbursement object,
	 * populating its fields with the data retrieved from the database
	 * 
	 * @param rs - an SQL result set containing the details of a single
	 *           reimbursement
	 * @return a new Reimbursement object
	 * @throws SQLException
	 */
	private Reimbursement extractRFromResultSet(ResultSet rs) throws SQLException {
		log.trace("AppUserDaoJdbc.extractRFromResultSet()");
		return new Reimbursement(rs.getInt("reimb_id"), rs.getDouble("reimb_amt"), rs.getTimestamp("reimb_submitted"),
				rs.getTimestamp("reimb_resolved"), rs.getString("reimb_description"), rs.getInt("reimb_author"),
				rs.getString("user_first_name"), rs.getString("user_last_name"), rs.getInt("reimb_resolver"), rs.getInt("reimb_status_id"), rs.getInt("reimb_type_id"));
	}

	@Override
	public AppUser findById(int id) {
		log.trace("AppUserDaoJdbc.findById()");
		try (Connection conn = ConnectionUtil.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(
					"SELECT * FROM ers_users INNER JOIN ers_user_roles ON ers_user_role_id = user_role_id WHERE ers_users_id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return extractFromResultSet(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<AppUser> findAll() {
		log.trace("AppUserDaoJdbc.findAll()");
		try (Connection conn = ConnectionUtil.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(
					"SELECT * FROM ers_users INNER JOIN ers_user_roles ON user_role_id = ers_user_role_id");
			ResultSet rs = ps.executeQuery();
			List<AppUser> users = new ArrayList<>();
			while (rs.next()) {
				users.add(extractFromResultSet(rs));
			}
			return users;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AppUser findByUsernameAndPassword(String username, String password) {
		log.trace("AppUserDaoJdbc.findByUsernameAndPassword");
		try (Connection conn = ConnectionUtil.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(
					"SELECT * FROM ers_users INNER JOIN ers_user_roles ON user_role_id = ers_user_role_id "
							+ "WHERE ers_username = ?  AND ers_password = ?");
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return extractFromResultSet(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int postNewUser(AppUser au) {
		log.trace("AppUserDaoJdbc.postNewUser()");
		try (Connection con = ConnectionUtil.getConnection()) {
			PreparedStatement ps = con.prepareStatement(
					"INSERT INTO ers_users (ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id)"
							+ "VALUES (?,?,?,?,?,?)",
					new String[] { "ers_users_id" }); // so that rs has access to the new id
			ps.setString(1, au.getUsername());
			ps.setString(2, au.getPassword());
			ps.setString(3, au.getFirstName());
			ps.setString(4, au.getLastName());
			ps.setString(5, au.getEmail());
			ps.setInt(6, 1); // one may not add a manager through the web client
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys(); // get a set containing the id of the new user
			if (rs.next()) {
				int id = rs.getInt("ers_users_id");
				au.setUserId(id);
				return id;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<Reimbursement> findReimbsByUser(int id) {
		log.trace("AppUserDaoJdbc.findReimbsByUser()");
		try {
			String SQL = 
					"SELECT * FROM ers_reimbursement as r " +
					"INNER JOIN ers_users as u " +
					"ON r.reimb_author = u.ers_users_id " +
					"WHERE reimb_author = ?";
			Connection con = ConnectionUtil.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			List<Reimbursement> reimbursements = new ArrayList<>();
			while (rs.next()) {
				reimbursements.add(extractRFromResultSet(rs));
			}
			return reimbursements;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int postNewReimb(Reimbursement reim, int userid) {
		log.trace("AppUserDaoJdbc.postNewReimb");
		try {
			Connection con = ConnectionUtil.getConnection();
			PreparedStatement ps = con.prepareStatement(
					"insert into ers_reimbursement (reimb_amt, reimb_submitted, reimb_description, reimb_author, reimb_status_id, reimb_type_id) "
				  + "values(?, now(), ?, ?, ?, ?)",
					new String[] { "reimb_id" });

			ps.setDouble(1, reim.getReimbursementAmount());
			ps.setString(2, reim.getDescription());
			ps.setInt(3, userid);
			ps.setInt(4, 1); // reimbursement requests will always have a PENDING status when created
			ps.setInt(5, reim.getReimbursementTypeId());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				int id = rs.getInt("reimb_id");
				reim.setReimbursementId(id);
				return id;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

}