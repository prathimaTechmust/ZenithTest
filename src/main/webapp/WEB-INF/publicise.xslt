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
				<title>Publicise</title>
			</head>
			<body>
				<p>Hello <xsl:value-of select="root/ContactData/m_strContactName"/> </p>
				<p>We would like to inform you on our new arrivals that have come into our inventory.</p>
				<p><b>Item Details</b>
					<table class="asiancorp">
						<tr>
							<td class="xslt_fieldHeading">Item Name :</td>
							<td class="xslt_fieldData" id="m_strItemName">
								<xsl:value-of select="root/ItemData/m_strItemName" />
							</td>
						</tr>
						<tr>
							<td class="xslt_fieldHeading">Brand :</td>
							<td class="xslt_fieldData" id="m_strBrand">
								<xsl:value-of select="root/ItemData/m_strBrand" />
							</td>
						</tr>
						<tr>
							<td class="xslt_fieldHeading">Detail :</td>
							<td class="xslt_fieldData" id="m_strDetail">
								<xsl:value-of select="root/ItemData/m_strDetail" />
							</td>
						</tr>
					</table>
				</p>
				<p>Please visit our store for more details.</p>
				<p>Thank you</p>
				Team TradeMust<br/>
				<img style="width:120px;height:30px;" src="images/defaultLogo.PNG" title="Logo"/>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>