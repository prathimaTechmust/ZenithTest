<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:variable name="m_nStudentId" select="StudentInformationData/m_nStudentId" />
<xsl:template match="/">
<html>
<head>
	<style>
		.topAlign{
		vertical-align: top;				
		}		
		td.container > div 
		{
		    width: 100%;
		    height: 100%;
		    overflow:hidden;
		}
		td.container 
		{
		    height: 20px;
		}
	</style>
</head>
<body>
	<table border="1" width="100%" >
		<tr>	
			<td style="width: 50%;" >
				<table>
					<tr>
						<td class = "xslt_zenithFieldHeader">Log  : </td>								
					</tr>
					<tr>
						<td>
							<table>
								<tr>
									<td class="xslt_fieldHeading">User name :</td>
									<td class="xslt_fieldData" id="m_strLoginUserName">
										<xsl:value-of select="ActivityLog/m_strLoginUserName"/>
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">Function name :</td>
									<td class="xslt_fieldData" id="m_strTaskPerformed">
										<xsl:value-of select="ActivityLog/m_strTaskPerformed"/>
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">Date :</td>
									<td class="xslt_fieldData" id="m_dCreatedOn">
										<xsl:value-of select="ActivityLog/m_dCreatedOn"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>	
			<td style="width: 50%;" >
			<table>
				<tr>
					<td class = "xslt_zenithFieldHeader">Parameters : </td>								
				</tr>	
				<tr>
					<td>
						<table>
							<tr>									
								<xsl:for-each select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_oScholarshipDetails/ScholarshipDetails">
									<tr>
										<td class="xslt_organizationDetails">
											<xsl:value-of select="m_strOrganizationName" />
										</td>
										<td class="xslt_organizationDetails">
											<xsl:value-of select="format-number(m_fAmount, '##,##,##0')"/>
										</td>											
									</tr>									
								</xsl:for-each>	
							</tr>
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
