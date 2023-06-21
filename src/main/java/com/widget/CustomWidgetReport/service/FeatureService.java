package com.widget.CustomWidgetReport.service;

import java.util.List;

import com.widget.CustomWidgetReport.entity.Feature;


public interface FeatureService {
	
	List<Feature> findAllByStatus(String status);
	
	Feature findById(Integer featureId);

}
