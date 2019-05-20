<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:variable name="m_nUserId" select="UserInformationData/m_nUserId" />
	<xsl:template match="/">
		<html>
			<body>
				<table class="zenith">
					<tr>
			 			<td class="xslt_fieldHeader" colspan="2">User Information Details</td>
					</tr>
					<tr>
						<td width="50%" valign="top">
							<table class="zenith" cellSpacing="5px">
								<tr>
									<td class="xslt_fieldHeading">User Name :</td>
									<td class="xslt_fieldData" id="m_strUserName">
										<xsl:value-of select="UserInformationData/m_strUserName" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										LogIn Name :
									</td>
									<td class="xslt_fieldData" id="m_strLoginId">
										<xsl:value-of select="UserInformationData/m_strLoginId" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Role :
									</td>
									<td class="xslt_fieldData" id="m_strRoleName">
										<xsl:value-of
											select="UserInformationData/m_oRole/RoleData/m_strRoleName" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Employee Id :
									</td>
									<td class="xslt_fieldData" id="m_strEmployeeId">
										<xsl:value-of select="UserInformationData/m_strEmployeeId" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Email-Id :
									</td>
									<td class="xslt_fieldData" id="m_strEmailAddress">
										<xsl:value-of select="UserInformationData/m_strEmailAddress" />
									</td>
								</tr>
								<tr>
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
										<xsl:value-of select="UserInformationData/m_dDOB"/>
									</td>
								</tr>
							</table>
						</td>
						<td width="50%" valign="top">
							<table class="xslt_trademust" cellSpacing="5px">
								<tr>
									<td class="xslt_fieldHeading">
										Address :
									</td>
									<td class="xslt_fieldData" id="m_strAddress">
										<xsl:value-of select="UserInformationData/m_strAddress" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Phone Number :
									</td>
									<td class="xslt_fieldData" id="m_strPhoneNumber">
										<xsl:value-of select="UserInformationData/m_strPhoneNumber" />
									</td>
								</tr>
							</table>
						</td>
						<td class="zenith" align="left" style="vertical-align:top;width:15%">
							<table class="zenith">
								<tr>
									<td class="zenith" align="center">
										<a href="javascript:userImage_setPreview ('{UserInformationData/m_strUserImageUrl}');">
											<img src="{UserInformationData/m_strUserImageUrl}" id = "imagePreview_img_user" class="userDetailImage"/>
										</a>
										<br />
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
