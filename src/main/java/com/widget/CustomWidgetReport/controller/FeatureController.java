package com.widget.CustomWidgetReport.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.widget.CustomWidgetReport.entity.Feature;
import com.widget.CustomWidgetReport.service.FeatureService;
import com.widget.CustomWidgetReport.util.CustomWidgetLog;
import com.widget.CustomWidgetReport.util.EntityConstants.Status;
import com.widget.CustomWidgetReport.util.Response;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/features")
public class FeatureController {
	
	private static final Logger log = LogManager.getLogger("FeatureController"); 
	
	@Autowired
	private FeatureService featureService;

	@GetMapping("/list")
	public Response<List<Feature>> getRoles() {
		List<Feature> features = new ArrayList<>();
		try {
			features = featureService.findAllByStatus(Status.ACTIVE.toString());
		} catch (Exception e) {
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() +"  Error while getting feature :  " + e);
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() +" stack trace : " + Arrays.toString(e.getStackTrace()));
			return new Response<List<Feature>>(500, false, HttpStatus.INTERNAL_SERVER_ERROR,
					"Error while getting features").setResponse(null);
		}
		return new Response<List<Feature>>(200, true, HttpStatus.OK, features.size() + " features has been retrived")
				.setResponse(features);
	}

}
