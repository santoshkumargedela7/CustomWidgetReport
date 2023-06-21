package com.widget.CustomWidgetReport.util;

public class StringSplitup {	

		

		public static String getQuestSimples(String[] arr) {
		    String Questions="";
		    int length=arr.length;
		    
		    for(int i=0;i<length;i++) {
		        Questions+="?";
		        if(i==length-1) {
		            break;
		        }
		        Questions+="','";
		        //Questions+="'"+ Questions + "'";
		    }
		    
		    return Questions;
		}



		

}
