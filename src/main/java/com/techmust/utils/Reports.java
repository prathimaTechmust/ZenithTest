package com.techmust.utils;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Reports
{
	public static final Logger m_logger = Logger.getLogger(Reports.class);
	
	
	public static XSSFWorkbook createWorkBook (Map<Integer, Object[]> excelData)
	{
		m_logger.info("createWorkBook");
		XSSFWorkbook oXssfWorkbook = new XSSFWorkbook();
		XSSFSheet oXssfSheet = oXssfWorkbook.createSheet("StudentInformation Data");
		try
		{	
			int rowcount = 0;
			for(Map.Entry<Integer,Object[]> oMapEntry:excelData.entrySet())
			{
				Integer oKeySet = oMapEntry.getKey();
				Object[] arrObject = oMapEntry.getValue();
				Row oRow = oXssfSheet.createRow(rowcount++);
				int cellCount = 0;
				for(Object oObject :arrObject)
				{
					Cell oCell = oRow.createCell(cellCount++);					
					if(oObject instanceof String)
						oCell.setCellValue((String) oObject);
					else if(oObject instanceof Integer)
						oCell.setCellValue((Integer)oObject);
					else
						oCell.setCellValue((Long)oObject);
				}
				
			}			
			FileOutputStream oCreateExcelFile = new FileOutputStream("C:/Program Files/Apache Software Foundation/Tomcat 9.0/webapps/logs/report.xlsx");
			oXssfWorkbook.write(oCreateExcelFile);
			oCreateExcelFile.close();			
		}
		catch (Exception oException)
		{
			m_logger.error("createWorkBook - oException"+oException);
		}
		return oXssfWorkbook;	
	}	
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public static Object[] getColumnHeader ()
	{
		Object[] arrColumnHeaders = null;
		
		List columnList = new ArrayList ();
		columnList.add("UID");
		columnList.add("NAME");
		columnList.add("CATEGORY");
		columnList.add("REFERENCE");
		columnList.add("GENDER");
		columnList.add("RELIGION");
		columnList.add("CLASS");
		columnList.add("INSTITUTION");
		columnList.add("SCORES");
		columnList.add("PARENTAL STATUS");
		columnList.add("FATHER'S NAME");		
		columnList.add("FATHER OCCUPATION");
		columnList.add("CONTACTNUMBER");
		columnList.add("MOTHER'SNAME");
		columnList.add("MOTHER OCCUPATION");
		columnList.add("CITY");
		columnList.add("STATE");
		columnList.add("CHEQUENUMBER");
		columnList.add("REMARKS");
		arrColumnHeaders = columnList.toArray();
		return arrColumnHeaders;		
	}

}
