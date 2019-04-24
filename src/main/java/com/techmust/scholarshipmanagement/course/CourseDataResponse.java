package com.techmust.scholarshipmanagement.course;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class CourseDataResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<CourseInformationData> m_arrCourseInformationData ;
	public Object m_nRowCount;
    public CourseDataResponse ()
	{
    	m_arrCourseInformationData = new ArrayList<CourseInformationData> ();
    	m_nRowCount = 0;
	}
}
