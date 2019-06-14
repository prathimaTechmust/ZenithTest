package com.techmust.applicationstatus.verified;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.techmust.generic.data.MasterData;

@Entity
@Table(name = "verified")
public class VerifiedStudents extends MasterData 
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int m_nverifiedId;
	
	@Column(name = "verifiedby")
	private String m_strVerifiedBy;
	
	@Column(name = "date")
	private Calendar m_dDate;

	public VerifiedStudents()
	{
		m_nverifiedId = -1;
		m_strVerifiedBy = "";
		m_dDate = Calendar.getInstance();	
	}

	public int getM_nverifiedId()
	{
		return m_nverifiedId;
	}

	public void setM_nverifiedId(int m_nverifiedId) 
	{
		this.m_nverifiedId = m_nverifiedId;
	}

	public String getM_strVerifiedBy()
	{
		return m_strVerifiedBy;
	}

	public void setM_strVerifiedBy(String m_strVerifiedBy)
	{
		this.m_strVerifiedBy = m_strVerifiedBy;
	}

	public Calendar getM_dDate() 
	{
		return m_dDate;
	}

	public void setM_dDate(Calendar m_dDate)
	{
		this.m_dDate = m_dDate;
	}
}
