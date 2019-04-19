<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html>
      <body>
		<table class="trademust">
			<tr>
	 			<td class="xslt_fieldHeader" colspan="2">Location Details</td>
				<td class="trademust" align="right" colspan="2">
					<table>
						<tr>
							<td>
								<img src="images/edit_database_24.png" width="20"  title="Edit"  align="center" id="editImageId" onClick="locationList_listDetail_edit ()"/>
							</td>
							<td>
								<img src="images/delete.png" width="20" title="Delete" align="center" id="deleteImageId" onClick="locationList_listDetail_delete ()"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
			</tr>	
			<tr>
				<td class="xslt_fieldHeading">Name :</td>
				<td class="xslt_fieldData" id="m_strName"><xsl:value-of select="LocationData/m_strName"/></td>
			</tr>
			<tr>
				<td class="xslt_fieldHeading">Address:</td>
				<td class="xslt_fieldData" id="m_strName"><xsl:value-of select="LocationData/m_strAddress"/></td>
			</tr>
		</table>
	</body>
	 </html>
  </xsl:template>
</xsl:stylesheet>