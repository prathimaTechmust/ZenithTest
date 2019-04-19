package com.techmust.usermanagement.userinfo;

import java.awt.image.BufferedImage;
import java.sql.Blob;
import java.util.Date;

import com.techmust.generic.data.IGenericData;

public interface IUserInformationData extends IGenericData
{
	public  String getM_strPassword ();
	public void setM_strPassword (String strPassword);
	public String getM_strDOB ();
	public void setM_dDOB(Date oDate);
	public BufferedImage getM_buffImgUserPhoto ();
	public void setM_oUserPhoto (Blob oBlob);
	public String getM_strEmailAddress ();
	public String getM_strUserName ();
	public String getM_strLoginId ();
	public int getM_nUserId ();
	public String getM_strNewPassword ();
	public String generateXML ();
	public void setM_nUserId (int nUserId);
	public long getM_nUID();
}
