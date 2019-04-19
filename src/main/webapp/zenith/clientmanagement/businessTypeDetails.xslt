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
							Business Type :
							</td>
							<td class="xslt_fieldData" id="m_strBusinessName"><xsl:value-of select="BusinessTypeData/m_strBusinessName"/></td>
							<td align="right">
								<table>
									<td>
										<img src="images/edit_database_24.png" width="20"  title="Edit"  align="center" id="editImageId" onClick="listBusinessType_edit ({BusinessTypeData/m_nBusinessTypeId})"/>
									</td>
									<td>
										<img src="images/delete.png" width="20" title="Delete" align="center" id="deleteImageId" onClick="listBusinessType_listDetail_delete ()"/>
									</td>
								</table>
							</td>
						</tr>
					</table>					
				</td>
			</tr>	
		</table> 
	 </body>
  </html>
</xsl:template>
</xsl:stylesheet>