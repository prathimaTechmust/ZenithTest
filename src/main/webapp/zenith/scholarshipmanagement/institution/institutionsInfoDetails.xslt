<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:variable name="m_nInstitutionId" select="InstitutionInformationData/m_nInstitutionId" />
	<xsl:template match="/">
		<html>
			<body>
				<table class="zenith">
					<tr>
			 			<td class="xslt_fieldHeader" colspan="2">Institution Information Details</td>
					</tr>
					<tr>
						<td width="50%" valign="top">
							<table class="zenith" cellSpacing="5px">
								<tr>
									<td class="xslt_fieldHeading">Institution Name :</td>
									<td class="xslt_fieldData" id="m_strInstitutionName">
										<xsl:value-of select="InstitutionInformationData/m_strInstitutionName" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Institution Email :
									</td>
									<td class="xslt_fieldData" id="m_strInstitutionEmailAddress">
										<xsl:value-of select="InstitutionInformationData/m_strInstitutionEmailAddress" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Contact Person Name :
									</td>
									<td class="xslt_fieldData" id="m_strContactPersonName">
										<xsl:value-of
											select="InstitutionInformationData/m_strContactPersonName" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Contact Person Number :
									</td>
									<td class="xslt_fieldData" id="m_strPhoneNumber">
										<xsl:value-of
											select="InstitutionInformationData/m_strPhoneNumber" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Contact Person Email :
									</td>
									<td class="xslt_fieldData" id="m_strContactPersonEmail">
										<xsl:value-of select="InstitutionInformationData/m_strContactPersonEmail" />
									</td>									
								</tr>
								<tr>
                                   <td class="xslt_fieldHeading">
                                        Institution Type :
                                    </td>
                                    <td class="xslt_fieldData" id ="m_strInstitutionType">
                                       <xsl:value-of select="InstitutionInformationData/m_strInstitutionType"/>
                                   </td>
                                </tr>
							</table>
						</td>
						<td width="50%" valign="top">
							<table class="xslt_trademust" cellSpacing="5px">
								<tr>
									<td class="xslt_fieldHeading">
										Institution Address :
									</td>
									<td class="xslt_fieldData" id="m_strInstitutionAddress">
										<xsl:value-of select="InstitutionInformationData/m_strInstitutionAddress" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										City :
									</td>
									<td class="xslt_fieldData" id="m_strCity">
										<xsl:value-of select="InstitutionInformationData/m_strCity" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										State :
									</td>
									<td class="xslt_fieldData" id="m_strState">
										<xsl:value-of select="InstitutionInformationData/m_strState" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										PinCode :
									</td>
									<td class="xslt_fieldData" id="m_nPincode">
										<xsl:value-of select="InstitutionInformationData/m_nPincode" />
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
