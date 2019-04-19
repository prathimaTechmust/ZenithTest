package com.techmust.usermanagement.userinfo;

public enum UserStatus 
{
	kActive, kInactive, kSuspended, kInvalidStatus;
    private static final String [] m_arrUserStatusNames = {"Active", "Inactive", "Suspended", "Invalid Status"};
	
	public String toString ()
	{
		return m_arrUserStatusNames [this.ordinal ()];
	}
}
