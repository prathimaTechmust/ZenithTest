<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html>
      <body>
		<table class="trademust">
	 		<tr>
				<td class="trademust">
					<table class="trademust">
						<tr>
							<td class="xslt_fieldHeading" width="150px">
							Business Type Name :
							</td>
							<td class="xslt_fieldData" id="m_strBusinessName"><xsl:value-of select="BusinessTypeData/m_strBusinessName"/></td>
							<td align="right">
								<table>
									<td>
										<img src="images/edit_database_24.png" width="20"  title="Edit"  align="center" id="editImageId" onClick="listBusinessType_listDetail_edit ()"/>
									</td>
									<td>
										<img src="images/delete.png" width="20" title="Delete" align="center" id="deleteImageId" onClick="listBusinessType_listDetail_delete ()"/>
									</td>
								</table>
							</td>
						</tr>
					</table>
					<h2><b>List Of Business Type</b></h2> 
					<table border="1">
						<tr>
							<td class="xslt_fieldHeading">
								Id 
							</td>
							<td class="xslt_fieldHeading">
								Business Type Name 
							</td>							 
						</tr>
						<xsl:for-each select="BusinessTypeData">
						<tr>
							<td class="xslt_fieldData">
								<xsl:value-of select="m_nBusinessTypeId"/>
							</td>
							<td class="xslt_fieldData">
								<xsl:value-of select="m_strBusinessName"/>
							</td>							
						</tr>
						</xsl:for-each>
					</table> 
				</td>
			</tr>	
		</table> 
	 </body>
  </html>
</xsl:template>
</xsl:stylesheet>