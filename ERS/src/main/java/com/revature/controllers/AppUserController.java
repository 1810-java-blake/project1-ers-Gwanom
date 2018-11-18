package com.revature.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dto.Credential;
import com.revature.model.AppUser;
import com.revature.model.Reimbursement;
import com.revature.services.AppUserService;
import com.revature.util.ResponseMapper;

@SuppressWarnings("serial")
public class AppUserController extends HttpServlet {

	private AppUserService aus = AppUserService.currentImplementation;
	private Logger log = Logger.getRootLogger();
	ObjectMapper om = new ObjectMapper();

	void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String method = req.getMethod();
		switch (method) {
		case "GET":
			processGet(req, resp);
			break;
		case "POST":
			processPost(req, resp);
			break;
		default:
			resp.setStatus(404);
			break;
		}
	}

	private void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		log.trace("AppUserController.processGet()");

		String uri = req.getRequestURI();
		log.debug(uri);
		String context = "ERS";
		uri = uri.substring(context.length() + 2, uri.length());
		log.debug(uri);
		String[] uriArray = uri.split("/");
		

		if (uriArray.length == 1) {
//			 /users
			log.info("retreiving all users");
			List<AppUser> users = aus.findAll();
			ResponseMapper.convertAndAttach(users, resp);
			return;
		} else if (uriArray.length == 2) {
//			/users/1
//			get user with id 1
			try {
				int id = Integer.parseInt(uriArray[1]);
				log.info("retrieving user with id " + id);
				AppUser targetUser = aus.findById(id);
				ResponseMapper.convertAndAttach(targetUser, resp);
				return;
			} catch (NumberFormatException e) {
				resp.setStatus(404);
				return;
			}
		} else if (uriArray.length == 3) {
//			/users/1/reimb
//			get all reimbursement requests from user with id 1
			if ("reimb".equals(uriArray[2])) {
				try {
					int id = Integer.parseInt(uriArray[1]);
					log.info("retrieving requests from user with id " + id);
					List<Reimbursement> reimbs = aus.findReimbsByUser(id);
					ResponseMapper.convertAndAttach(reimbs, resp);
					return;
				} catch (NumberFormatException e) {
					log.trace("NumberFormatException catch");
					resp.setStatus(404);
					return;
				}
			} else {
				resp.setStatus(404);
				return;
			}
		} else {
//			requesting an endpoint which does not exist
			resp.setStatus(404);
			return;
		}
	}

	private void processPost(HttpServletRequest req, HttpServletResponse resp)
			throws JsonParseException, JsonMappingException, IOException {
		log.trace("AppUserController.processPost");
		String uri = req.getRequestURI();
		String context = "ERS";
		uri = uri.substring(context.length() + 2, uri.length());
		String[] uriArray = uri.split("/");
		if (uriArray.length == 1) {
//			/users
			log.info("Creating new user");
			AppUser au = om.readValue(req.getReader(), AppUser.class);
			int status = aus.postNewUser(au);
			log.debug("added user: " + au.toString());
			log.trace("with id: " + status);
			return;
		} else if (uriArray.length == 2) {
//			/users/login
			if ("users/login".equals(uri)) {
				log.info("attempting to log in");
				Credential cred = om.readValue(req.getReader(), Credential.class);
				if (!aus.login(cred, req.getSession())) {
					resp.setStatus(403);
				} else {
					log.trace("setting status 200");
					log.debug("a(n) " + req.getSession().getAttribute("role") + " logged in" );
					resp.setStatus(200);
					String respBody = "{\"role\": \"" + req.getSession().getAttribute("role") + "\"," + 
										    "\"id\": \"" + req.getSession().getAttribute("u_id") + "\"," + 
										    "\"name\": \"" + req.getSession().getAttribute("name") + "\"}";
					log.debug(respBody);
					resp.getWriter().write(respBody);
				}
			} else if("users/logout".equals(uri)) {
				log.trace("invalidating session");
				req.getSession().invalidate();
				return;
			} else {
				resp.setStatus(404);
				return;
			}
		} else if (uriArray.length == 3) {
//			/users/1/reimb
			log.info("Creating new reimbursement for user " + uriArray[1]);
			Reimbursement reim = om.readValue(req.getReader(), Reimbursement.class);
			int status = aus.postNewReimb(reim, Integer.parseInt(uriArray[1]));
			log.debug(status);
		} else {
//			requesting an endpoint which does not exist
			resp.setStatus(404);
		}
	}

}