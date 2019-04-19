<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
 <xsl:output method="html" encoding="utf-8"  omit-xml-declaration="yes" 
	indent="yes"
 	doctype-public="-//W3C//DTD HTML 4.0 Transitional//EN" 
	doctype-system="http://www.w3.org/TR/1998/REC-html40-19980424/loose.dtd"/>
	<xsl:template match="/">
		<html>
			<head>
				<title>Login Details</title>
			</head>
			<body>
				<img href="http://www.trademust.com:8080/traderp/online" src="http://www.trademust.com:8080/traderp/images/TrademustLogo.jpg"  title="Logo" />
				<br/>
				<p>Dear Sir/Madam </p>
				<p>Your login details are </p>
				<p>LoginId -  "<xsl:value-of select="LoginDetails/m_strEmail"/>"</p>
				<p>Password - "<xsl:value-of select="LoginDetails/m_strPassword"/>"</p>
				<br/>
				<p>Thank you</p>
				TradeMust
				<p>
					<a href="http://www.trademust.com:8080/traderp/online">Click here</a>
					to login
				</p>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
