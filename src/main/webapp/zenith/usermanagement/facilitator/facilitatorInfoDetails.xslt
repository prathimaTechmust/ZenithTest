<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:variable name="m_nFacilitatorId" select="FacilitatorInformationData/m_nFacilitatorId" />
	<xsl:template match="/">
		<html>
			<body>
				<table class="zenith">
					<tr>
			 			<td class="xslt_fieldHeader" colspan="2">Facilitator Information Details</td>
					</tr>
					<tr>
						<td width="50%" valign="top">
							<table class="zenith" cellSpacing="5px">
								<tr>
									<td class="xslt_fieldHeading">Facilitator Name :</td>
									<td class="xslt_fieldData" id="m_strFacilitatorName">
										<xsl:value-of select="FacilitatorInformationData/m_strFacilitatorName" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Email :
									</td>
									<td class="xslt_fieldData" id="m_strEmail">
										<xsl:value-of select="FacilitatorInformationData/m_strEmail" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Phone Number :
									</td>
									<td class="xslt_fieldData" id="m_strPhoneNumber">
										<xsl:value-of select="FacilitatorInformationData/m_strPhoneNumber" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										City :
									</td>
									<td class="xslt_fieldData" id="m_strCity">
										<xsl:value-of select="FacilitatorInformationData/m_strCity" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										State:
									</td>
									<td class="xslt_fieldData" id="m_strState">
										<xsl:value-of select="FacilitatorInformationData/m_strState"/>
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
