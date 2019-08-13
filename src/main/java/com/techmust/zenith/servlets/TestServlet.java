package com.techmust.traderp.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	public void doGet (HttpServletRequest oReq, HttpServletResponse oResp) throws ServletException, IOException
	{
		String strUserName = oReq.getParameter ("username");
		String strPassword = oReq.getParameter("password");
		String strType = "";
		try
		{
			strType = oReq.getParameter("type");
		}
		catch (Exception oException)
		{
			strType = "";
		}
		System.out.println ("TestServlet : doGet - strUserName : "+strUserName);
		System.out.println ("TestServlet : doGet - strPassword : "+strPassword);
		if (strType != null && strType.equalsIgnoreCase("image"))
			getImageBytes (oResp);
		else
		{
			String strResponse = "Hello this is response from servlet";
			oResp.getWriter().println(strResponse);
		}
//		sendBytes (strResponse.getBytes(), oResp);
	}
	
	private void getImageBytes(HttpServletResponse oResp)
    {
		File oFile = new File ("C:\\Users\\user\\Desktop\\photo\\03photo7.jpg");
		try
        {
	        org.hibernate.engine.jdbc.StreamUtils.copy(new FileInputStream(oFile), oResp.getOutputStream());
        }
		catch (FileNotFoundException oException)
        {
			oException.printStackTrace();
        } 
		catch (IOException oIOException)
        {
			oIOException.printStackTrace();
        }
    }

	@SuppressWarnings("unused")
	private void sendBytes(byte[] bytes, HttpServletResponse oResp)
    throws IOException
	{
		try
		{
			ServletOutputStream servletoutputstream = oResp.getOutputStream();
			servletoutputstream.write(bytes);
			servletoutputstream.flush();
			servletoutputstream.close();
		} catch (Exception oException)
		{
			System.out.println ("TestServlet : doGet - oException : "+oException);
		}
	}

	@Override
    protected void doPost(HttpServletRequest oReq, HttpServletResponse oResp)
            throws ServletException, IOException
    {
	    // TODO Auto-generated method stub
	    super.doPost(oReq, oResp);
	    doGet(oReq, oResp);
    }
	
}
