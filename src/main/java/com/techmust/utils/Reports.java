package com.techmust.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.techmust.constants.Constants;

public class Reports
{
	public static final Logger m_logger = Logger.getLogger(Reports.class);
	
	
	@SuppressWarnings("unused")
	public static String createWorkBook (Map<Integer, Object[]> excelData)
	{
		m_logger.info("createWorkBook");
		String strDownloadPath="";
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
					else if(oObject instanceof Float)
						oCell.setCellValue((Float) oObject);
					else
						oCell.setCellValue((Long)oObject);
				}				
			}
			strDownloadPath = downloadStudentReports(oXssfWorkbook);						
		}
		catch (Exception oException)
		{
			m_logger.error("createWorkBook - oException"+oException);
		}
		return strDownloadPath;	
	}	

	private static String downloadStudentReports(XSSFWorkbook oXssfWorkbook)
	{
		String strDownloadPath="";
		try 
		{	String strExcelTempPath = System.getProperty("java.io.tmpdir")+"/"+Constants.REPORTFILENAME+".xlsx";
			FileOutputStream oCreateExcelFile = new FileOutputStream(strExcelTempPath);
			oXssfWorkbook.write(oCreateExcelFile);			
			oCreateExcelFile.close();
			File oFile = new File(strExcelTempPath);
			String strS3ReportsFolderPath = Constants.STUDENTREPORTSFOLDER+Constants.REPORTFILENAME+".xlsx";
			strDownloadPath = AWSUtils.UploadExcelReport(strS3ReportsFolderPath,oFile);
		}
		catch (Exception oException)
		{
			m_logger.error("downloadStudentReports - oException"+oException);
		}	
		return strDownloadPath;
	}

	@SuppressWarnings({ "rawtypes", "unchecked"})
	public static Object[] getColumnHeader ()
	{
		Object[] arrColumnHeaders = null;
		
		List columnList = new ArrayList ();
		columnList.add("UID");
		columnList.add("NAME");
		columnList.add("STUDENTADHAARNUMBER");
		columnList.add("CATEGORY");
		columnList.add("REFERENCE");
		columnList.add("GENDER");
		columnList.add("RELIGION");
		columnList.add("CLASS");
		columnList.add("INSTITUTION");
		columnList.add("SCORES");
		columnList.add("PARENTAL STATUS");
		columnList.add("FATHER'S NAME");
		columnList.add("FATHERADHAARNUMBER");
		columnList.add("FATHER OCCUPATION");
		columnList.add("CONTACTNUMBER");
		columnList.add("MOTHER'SNAME");
		columnList.add("MOTHERADHAARNUMBER");
		columnList.add("MOTHER OCCUPATION");
		columnList.add("CITY");
		columnList.add("STATE");
		columnList.add("PINCODE");
		columnList.add("AMOUNTSANCTIONED");
		columnList.add("CHEQUENUMBER");
		columnList.add("REMARKS");
		arrColumnHeaders = columnList.toArray();
		return arrColumnHeaders;		
	}

}
