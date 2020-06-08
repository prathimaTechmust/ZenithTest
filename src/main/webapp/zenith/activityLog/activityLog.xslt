<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/ActivityLog">
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
												<xsl:value-of select="m_strLoginUserName"/>
											</td>
										</tr>
										<tr>
											<td class="xslt_fieldHeading">Function name :</td>
											<td class="xslt_fieldData" id="m_strTaskPerformed">
												<xsl:value-of select="m_strTaskPerformed"/>
											</td>
										</tr>
										<tr>
											<td class="xslt_fieldHeading">Date :</td>
											<td class="xslt_fieldData" id="m_dCreatedOn">
												<xsl:value-of select="m_dCreatedOn"/>
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
				<xsl:if test="StudentInformationData">
					<tr>
						<td class="xslt_fieldHeading">UID :</td>
						<td class="xslt_fieldData"><xsl:value-of select="StudentInformationData/m_nUID"/></td>
					</tr>
					<tr>
						<td class="xslt_fieldHeading">Student Name : </td>
						<td class="xslt_fieldData"><xsl:value-of select="StudentInformationData/m_strStudentName"/></td>
					</tr>
					<tr>
				  		<td class="xslt_fieldHeading"> Academic Year :</td>
				  		<td class="xslt_fieldData"><xsl:value-of select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_oAcademicYear/AcademicYear/m_nAcademicYearId"/> </td>
				  	</tr>
				</xsl:if>
				
				<xsl:if test="FacilitatorInformationData">
					<tr>
						<td class="xslt_fieldHeading">Facilitator Name :</td>
						<td class="xslt_fieldData"><xsl:value-of select="FacilitatorInformationData/m_strFacilitatorName"/></td>
					</tr>
					<tr>
						<td class="xslt_fieldHeading">Phone Number : </td>
						<td class="xslt_fieldData"><xsl:value-of select="FacilitatorInformationData/m_strPhoneNumber"/></td>
					</tr>
					<tr>
						<td class="xslt_fieldHeading">City : </td>
						<td class="xslt_fieldData"><xsl:value-of select="FacilitatorInformationData/m_strCity"/></td>
					</tr>
					<tr>
						<td class="xslt_fieldHeading">State : </td>
						<td class="xslt_fieldData"><xsl:value-of select="FacilitatorInformationData/m_strState"/></td>
					</tr>
				</xsl:if>
				
				<xsl:if test="CourseInformationData">
					<tr>
						<td class="xslt_fieldHeading">Short Course Name :</td>
						<td class="xslt_fieldData"><xsl:value-of select="CourseInformationData/m_strShortCourseName"/></td>
					</tr>
					<tr>
						<td class="xslt_fieldHeading">Long Course Name : </td>
						<td class="xslt_fieldData"><xsl:value-of select="CourseInformationData/m_strLongCourseName"/></td>
					</tr>
				</xsl:if>
				
				<xsl:if test="InstitutionInformationData">
					<tr>
						<td class="xslt_fieldHeading">Institution Name :</td>
						<td class="xslt_fieldData"><xsl:value-of select="InstitutionInformationData/m_strInstitutionName"/></td>
					</tr>
					<tr>
						<td class="xslt_fieldHeading">Phone Number : </td>
						<td class="xslt_fieldData"><xsl:value-of select="InstitutionInformationData/m_strPhoneNumber"/></td>
					</tr>
					<tr>
						<td class="xslt_fieldHeading">City : </td>
						<td class="xslt_fieldData"><xsl:value-of select="InstitutionInformationData/m_strCity"/></td>
					</tr>
					<tr>
						<td class="xslt_fieldHeading">State : </td>
						<td class="xslt_fieldData"><xsl:value-of select="InstitutionInformationData/m_strState"/></td>
					</tr>
				</xsl:if>
				<xsl:if test="ZenithScholarshipDetails">
					<tr>
						<td class="xslt_fieldHeading">UID :</td>
						<td class="xslt_fieldData"><xsl:value-of select="ZenithScholarshipDetails/m_nStudentUID"/></td>
					</tr>
					<tr>
						<td class="xslt_fieldHeading">Student Name :</td>
						<td class="xslt_fieldData"><xsl:value-of select="ZenithScholarshipDetails/m_strStudentName"/></td>
					</tr>
					<tr>
				  		<td class="xslt_fieldHeading"> Academic Year :</td>
				  		<td class="xslt_fieldData"> <xsl:value-of select="ZenithScholarshipDetails/m_nAcademicYearId"/> </td>
				  	</tr>
				
				</xsl:if>
					
				</table>
			</td>
		</tr>
		  </table>
		</body>
	</html>
</xsl:template>
 </xsl:stylesheet>
