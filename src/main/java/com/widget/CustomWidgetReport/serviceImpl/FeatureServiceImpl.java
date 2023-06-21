package com.widget.CustomWidgetReport.serviceImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.widget.CustomWidgetReport.entity.Feature;

@Service
@Transactional
public class FeatureServiceImpl implements com.widget.CustomWidgetReport.service.FeatureService {

	@Autowired
	private com.widget.CustomWidgetReport.repository.FeatureRepository featureRepository;
	
	@Override
	public List<com.widget.CustomWidgetReport.entity.Feature> findAllByStatus(String status) {
		return featureRepository.findAllByStatusIgnoreCase(status);
	}

	@Override
	public Feature findById(Integer featureId) {
		return featureRepository.findById(featureId).get();
	}

	
}
