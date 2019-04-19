<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/">
    <html>
      <body>
		<table class="trademust">
	 		<tr>
				<td class="xslt_fieldHeader" colspan="2">Item Category Details</td>
			</tr>
			<tr>
				<table class="trademust">
					<tr>
						<td class="xslt_fieldHeading">
						Category Name :
						</td>
						<td class="xslt_fieldData" id="m_strCategoryName"><xsl:value-of select="ItemCategoryData/m_strCategoryName"/></td>
					</tr>
					<tr>
						<td class="xslt_fieldHeading">
						Created By :
						</td>
						<td class="xslt_fieldData" id="m_strCreatedBy"><xsl:value-of select="ItemCategoryData/m_strCreatedBy"/></td>
					</tr>
					<tr>
						<td class="xslt_fieldHeading">
						Created On :
						</td>
						<td class="xslt_fieldData" id="m_strDate"><xsl:value-of select="ItemCategoryData/m_strDate"/></td>
					</tr>
				</table>
			</tr>
		</table> 
	 </body>
  </html>
</xsl:template>
</xsl:stylesheet>