package com.widget.CustomWidgetReport.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.widget.CustomWidgetReport.entity.Feature;

public interface FeatureRepository extends JpaRepository<Feature, Integer>{

	List<Feature> findAllByStatusIgnoreCase(String status);

}
