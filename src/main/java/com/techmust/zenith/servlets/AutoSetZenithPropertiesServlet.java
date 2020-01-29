package com.techmust.zenith.servlets;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.PropertyConfigurator;
import com.techmust.generic.data.AppProperties;
import com.techmust.usermanagement.initializer.UserManagementInitializer;

public class AutoSetZenithPropertiesServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	public void init (ServletConfig oServletConfig) throws ServletException
	{
		super.init(oServletConfig);
		try 
		{
			/*int interval = 30000000; // 5min
		    Date timeToRun = new Date(System.currentTimeMillis() + interval);
		    Timer timer = new Timer();
		    
		    timer.schedule(new TimerTask()
		    {
		            public void run() 
		            {
		            	System.out.println("Tick");
		                System.out.println("Tock");
		            }
		        }, 1000);*/
			String strContextPath = oServletConfig.getServletContext().getRealPath("/WEB-INF/");
			AppProperties.setProperty("PROJECT_PROPERTY_FILE", strContextPath + File.separator + "Zenith.properties");
			AppProperties.setProperty("MENU_XSLT_FILE", strContextPath+File.separator + "ZenithMenu.xslt");
			AppProperties.setProperty("ONLINE_MENU_XSLT_FILE", strContextPath+File.separator + "ZenithOnlineMenu.xslt");
			AppProperties.setProperty("OFFCANVAS_XSLT_FILE", strContextPath+File.separator + "ZenithOffcanvas.xslt");
			AppProperties.setProperty("MAIL_PROPERTY_FILE", strContextPath + File.separator + "Zenithmail.properties");
			/*System.out.println ("AutoSetZenithPropertiesServlet : strContextPath : " + strContextPath + File.separator + "log4jZenith.properties");
			PropertyConfigurator.configure (strContextPath + File.separator+"log4jZenith.properties");*/
			AppProperties.setProperty ("UserManagement.defaults", strContextPath + File.separator + "UserManagement.defaults.xml");
			initialize ();
		} 
		catch (Exception oException) 
		{
			System.out.println ("AutoSetZenithPropertiesServlet : init : " +oException.toString());
		}
	}

	private void initialize() throws Exception
    {
		System.out.println ("AutoSetZenithPropertiesServlet : initialize : ");
// TODO : commented to check connection pool issues using hibernate sessions 0n 2/6/2019
		UserManagementInitializer.initialize ();
//		EMailDataProcessor oEMailDataProcessor = new EMailDataProcessor ();
//		oEMailDataProcessor.initiateSendEMailThread();
	}
}
