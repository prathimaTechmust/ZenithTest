package com.techmust.generic.email;

public enum EMailStatus
{
	kToSend, kSent, kFailed; 
	private static final String [] m_arrMailStatusNames = {"ToSend", "Sent", "Failed"};

	public String toString ()
	{
		return m_arrMailStatusNames[this.ordinal()];
	}
}
