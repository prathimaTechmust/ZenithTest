package com.techmust.scholarshipmanagement.transaction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.techmust.generic.data.MasterData;

@Entity
@Table(name = "transactionDetails")
public class Transaction  extends MasterData
{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transactionid")
	private int m_nTransactionId;
	
	@Column(name = "username")
	private String m_strUserName;
	
	@Column(name = "taskPerformed")
	private String m_strTaskPerformed;
	
	@Column(name = "time")
	private int m_nTime;

	public int getM_nTransactionId() 
	{
		return m_nTransactionId;
	}

	public void setM_nTransactionId(int m_nTransactionId) 
	{
		this.m_nTransactionId = m_nTransactionId;
	}

	public String getM_strUserName() 
	{
		return m_strUserName;
	}

	public void setM_strUserName(String m_strUserName) 
	{
		this.m_strUserName = m_strUserName;
	}

	public String getM_strTaskPerformed()
	{
		return m_strTaskPerformed;
	}

	public void setM_strTaskPerformed(String m_strTaskPerformed) 
	{
		this.m_strTaskPerformed = m_strTaskPerformed;
	}

	public int getM_nTime() 
	{
		return m_nTime;
	}

	public void setM_nTime(int m_nTime)
	{
		this.m_nTime = m_nTime;
	}
	
}
