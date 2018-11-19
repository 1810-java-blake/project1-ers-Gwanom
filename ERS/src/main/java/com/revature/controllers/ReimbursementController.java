package com.revature.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.model.Reimbursement;
import com.revature.services.ReimbursementService;
import com.revature.util.ResponseMapper;

@SuppressWarnings("serial")
public class ReimbursementController extends HttpServlet {

	private Logger log = Logger.getRootLogger();
	private ReimbursementService rs = ReimbursementService.currentImplementation;
	ObjectMapper om = new ObjectMapper();

	public void process(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String method = req.getMethod();
		switch (method) {
		case "GET":
			processGet(req, res);
			break;
		case "PUT":
			processPut(req, res);
			break;
		default:
			res.setStatus(405);
			break;
		}
	}

	private void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log.trace("ReimbursementController.processGet()");
		String uri = req.getRequestURI();
		String context = "ERS";
		uri = uri.substring(context.length() + 2, uri.length());
		String[] uriArray = uri.split("/");

		if (uriArray.length == 1) {
//			/reimbs
			log.info("retrieving all reimbursement requests");
			List<Reimbursement> reimbs = rs.findAll();
			ResponseMapper.convertAndAttach(reimbs, resp);
			return;
		} else if (uriArray.length == 2) {
//			/reimbs/1
//			check that the second param is actually a number
			try {
				int id = Integer.parseInt(uriArray[1]);
				log.info("retrieving reimbursement " + id);
				Reimbursement reimb = rs.findById(id);
				ResponseMapper.convertAndAttach(reimb, resp);
				return;
			} catch (NumberFormatException e) {
				resp.setStatus(404);
				return;
			}
		} else if (uriArray.length == 3) {
//			/reimbs/status/1
//			second must be "status", third must be a number
			if (!("status".equals(uriArray[1]))) {
				resp.setStatus(404);
				return;
			}
			try {
				int statusId = Integer.parseInt(uriArray[2]); // throws exception if NaN
				log.info("finding reimbursements with status code " + statusId);
				List<Reimbursement> reimbs = rs.findByStatusId(statusId);
				ResponseMapper.convertAndAttach(reimbs, resp);
				return;
			} catch (NumberFormatException e) {
				resp.setStatus(404);
				return;
			}
		}
	}

	private void processPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
//		/reimb/1/2
//		updates reimbursement 1 to have status code 2
		String uri = req.getRequestURI();
		String context = "ERS";
		uri = uri.substring(context.length() + 2, uri.length());
		String[] uriArray = uri.split("/");
		
		if(!(uriArray.length == 3)) {
			res.setStatus(404);
			return;
		}
		try {
			int reimId = Integer.parseInt(uriArray[1]); // throws exception if NaN
			int statusId = Integer.parseInt(uriArray[2]);
			int x = rs.updateReimbursement(reimId, statusId);
			res.getWriter().write("update: " + x);
			res.setStatus((x == 1) ? 200 : 500);
			return;
		} catch (NumberFormatException e) {
			res.setStatus(404);
			return;
		}

	}

}
