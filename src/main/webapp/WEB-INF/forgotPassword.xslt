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
				<title>Reset Password</title>
			</head>
			<body>
				<p>Dear Sir/Madam </p>
				<p>Your Zenith online password has been reset.</p>
				<p> Your new password is <b>"<xsl:value-of select="ForgotPasswordData/m_strPassword"/>"</b></p>
				<p><b>We strongly recommend you to change your password after you login.</b></p>
				<p>Thank you</p>
					Zenith Foundation
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
