package com.techmust.scholarshipmanagement.student;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;
import com.techmust.scholarshipmanagement.studentdocuments.StudentDocuments;

public class StudentDataResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<StudentInformationData> m_arrStudentInformationData ;
	public Object m_nRowCount;
	public StudentDocuments m_oStudentDocuments;
	public String m_strStudentXMLData;
	public String m_strStudentDownloadReportURL;
    public StudentDataResponse ()
	{
    	m_arrStudentInformationData = new ArrayList<StudentInformationData> ();
    	m_nRowCount = 0;
    	m_oStudentDocuments = new StudentDocuments();
    	m_strStudentXMLData = "";
    	m_strStudentDownloadReportURL = "";
	}
}
