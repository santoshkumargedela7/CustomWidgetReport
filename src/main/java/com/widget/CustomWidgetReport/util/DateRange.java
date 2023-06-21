package com.widget.CustomWidgetReport.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class DateRange {
	
	
	public String getDateRanges(String daterRangeName,String sele_startdate,String sele_enddate) {
		
		

		
		String dateStr="";
		try {
			String startDate = null;
			String endDate = null;
			String startTime = " 00:00:00";
			String endTime = " 23:59:59";
			Date currDate = new Date();
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
			@SuppressWarnings("unused")
			DateTimeFormatter endTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String modifiedDt = format1.format(currDate);			
			
			
			if (daterRangeName.equalsIgnoreCase("custom")) {
				if (sele_startdate == null || sele_startdate.trim().equalsIgnoreCase("")) {
					startDate = modifiedDt.concat(startTime);
				} else {
					startDate = sele_startdate;
					Date startDateParse = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
					modifiedDt = format1.format(startDateParse);
					startDate= modifiedDt.concat(startTime);
				}

				if (sele_enddate == null || sele_enddate.trim().equalsIgnoreCase("")) {
					sele_enddate = modifiedDt.concat(endTime);
				} else {
					endDate = sele_enddate;
					Date endDateParse = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
					modifiedDt = format1.format(endDateParse);
					endDate = modifiedDt.concat(endTime);
				}
				log.info("Custom starttime:" + startDate);
				log.info("Custom endtime:" + endDate);
			} else if (daterRangeName.equalsIgnoreCase("today")) {
				@SuppressWarnings("unused")
				LocalDateTime now = LocalDateTime.now();
				startDate = modifiedDt.concat(startTime);
				endDate = modifiedDt.concat(endTime);
			
				log.info("Today starttime:" + startDate);
				log.info("Tdday endtime:" + endDate);
		

			} else if (daterRangeName.equalsIgnoreCase("thisWeek")) {
				LocalDateTime now = LocalDateTime.now();
				TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
				LocalDateTime weekStartDate = now.with(fieldISO, 1);
				String modifiedWeekStartDate = weekStartDate.format(format);
				startDate = modifiedWeekStartDate.concat(startTime);
				endDate = modifiedDt.concat(endTime);
				log.info("thisweek starttime:" + startDate);
				log.info("thisweek endtime:" + endDate);
				
			} else if (daterRangeName.equalsIgnoreCase("thisMonth")) {
				LocalDateTime todayDateTime = LocalDateTime.now();
				LocalDateTime firstDateOfThisMonth = todayDateTime.withDayOfMonth(1);
		        String modifiedFirstDateOfThisMonth = firstDateOfThisMonth.format(format);  
			//	String modifiedThisMonthEndDate = todayDateTime.format(format);
				startDate = modifiedFirstDateOfThisMonth.concat(startTime);
				endDate = modifiedDt.concat(endTime);
				log.info("thisMonth starttime:" + startDate);
				log.info("thisMonth endtime:" + endDate);
			}
			
			dateStr=startDate+"###"+endDate;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		return dateStr;
	}

}
