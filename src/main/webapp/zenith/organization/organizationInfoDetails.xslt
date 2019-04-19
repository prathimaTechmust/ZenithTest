<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:variable name="m_nOrganizationId" select="OrganizationInformationData/m_nOrganizationId" />
	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
					<tr>
			 			<td class="xslt_fieldHeader" colspan="2">Organization Information Details</td>
					</tr>
					<tr>
						<td width="50%" valign="top">
							<table class="trademust" cellSpacing="5px">
								<tr>
									<td class="xslt_fieldHeading">Organization Name :</td>
									<td class="xslt_fieldData" id="m_strOrganizationName">
										<xsl:value-of select="OrganizationInformationData/m_strOrganizationName" />
									</td>
								</tr>
								<!-- <tr>
									<td class="xslt_fieldHeading">
										Address :
									</td>
									<td class="xslt_fieldData" id="m_strAddress">
										<xsl:value-of select="OrganizationInformationData/m_strAddress" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										PhoneNumber :
									</td>
									<td class="xslt_fieldData" id="m_strPhoneNumber">
										<xsl:value-of
											select="OrganizationInformationData/m_strPhoneNumber" />
									</td>
								</tr> -->
								<!-- <tr>
									<td class="xslt_fieldHeading">
										Employee Id :
									</td>
									<td class="xslt_fieldData" id="m_strEmployeeId">
										<xsl:value-of select="UserInformationData/m_strEmployeeId" />
									</td>
								</tr> -->
								<tr>
									<td class="xslt_fieldHeading">
										Email-Id :
									</td>
									<td class="xslt_fieldData" id="m_strEmailAddress">
										<xsl:value-of select="OrganizationInformationData/m_strEmailAddress" />
									</td>
								</tr>
								<!-- <tr>
									<td class="xslt_fieldHeading">
										Gender :
									</td>
									<td class="xslt_fieldData" id="m_strGender">
										<xsl:value-of select="UserInformationData/m_strGender" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Date Of Birth :
									</td>
									<td class="xslt_fieldData" id="m_dDOB">
										<xsl:value-of select="UserInformationData/m_dDOB" />
									</td>
								</tr> -->
							</table>
						</td>
						<td width="50%" valign="top">
							<table class="xslt_trademust" cellSpacing="5px">
								<tr>
									<td class="xslt_fieldHeading">
										Address :
									</td>
									<td class="xslt_fieldData" id="m_strAddress">
										<xsl:value-of select="OrganizationInformationData/m_strAddress" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Phone Number :
									</td>
									<td class="xslt_fieldData" id="m_strPhoneNumber">
										<xsl:value-of select="OrganizationInformationData/m_strPhoneNumber" />
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