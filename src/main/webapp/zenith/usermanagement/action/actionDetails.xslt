<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html>
      <body>
		<table class="zenith">
			<tr>
				<td class="zenith">
					<div>
						<table class="zenith">
							<tr>
								<td class="xslt_fieldHeader" colspan="2">Action Details
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
			<tr>
				<td class="zenith">
					<table>
						<tr>
							<td class="xslt_fieldHeading">
							Action Name :
							</td>
							<td class="xslt_fieldData" id="m_strActionName"><xsl:value-of select="ActionData/m_strActionName"/></td>
						</tr>
						<tr>
							<td class="xslt_fieldHeading">
							Action Area  Name :
							</td>
							<td class="xslt_fieldData" id="m_strActionAreaName"><xsl:value-of select="ActionData/m_oActionArea/ActionAreaData/m_strActionAreaName"/></td>
						</tr>
						<tr>
							<td class="xslt_fieldHeading">
							Target :
							</td>
							<td class="xslt_fieldData" id="m_strActionTarget"><xsl:value-of select="ActionData/m_strActionTarget"/></td>
						</tr>
						<tr>
							<td class="xslt_fieldHeading">
							Sequence Number :
							</td>
							<td class="xslt_fieldData" id="m_strActionTarget"><xsl:value-of select="ActionData/m_nSequenceNumber"/></td>
						</tr>
					</table>
				</td>
			</tr>	 		
		</table> 
	 </body>
  </html>
</xsl:template>
</xsl:stylesheet>