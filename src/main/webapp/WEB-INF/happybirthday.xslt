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
				<title>Message details</title>
			</head>
			<body>
				<p>Hello <xsl:value-of select="ContactMail/ContactData/m_strContactName" /></p>
				<p>
					<xsl:value-of select="ContactMail/ContactData/m_strContactName" />
				</p>
				<p>
					<xsl:value-of select="ContactMail/strContent" />
				</p>
				<p>Thank you</p>
				<p>
					ZENITH FOUNDATION Team
				</p>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
