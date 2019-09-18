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
	table.organization {
	border-collapse: collapse;
	}

	td.organization {
	font-family: sans-serif;
	padding: 4px;
    text-align: center;
	}
	
	
	td.organizationData{
	text-align: center;
	font-weight: bold;
	}				
	</style>
</head>
<body>
	<table border="1" width="100%" >
		<tr>	
			<td style="width: 55%;" >
				<table>
					<tr>
						<td align="left">
							<a href="javascript:studentList_setPreview ('{StudentInformationData/m_strStudentImageUrl}');">
								
							</a>
							<br/>
						</td>
						<td>
							<table>
								<tr>
									<tr>
										<td class="xslt_fieldHeading">UID :</td>
										<td class="xslt_fieldData" id="m_nUID">
											<xsl:value-of select="StudentInformationData/m_nUID" />
										</td>
									</tr>									
								</tr>
								<tr>
									<td class="xslt_fieldHeading">Name :</td>
									<td class="xslt_fieldData" id="m_strStudentName">
										<xsl:value-of select="StudentInformationData/m_strStudentName" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">Category :</td>
									<td class="xslt_fieldData" id="m_strCategory">
										<xsl:value-of select="StudentInformationData/m_strCategory"/>								
									</td>						
								</tr>
								<tr>
									<td class="xslt_fieldHeading">Priority Status :</td>
									<td class="xslt_fieldData" id="m_strApplicationStatus">
										<xsl:value-of select="StudentInformationData/m_nApplicationPriority"/>								
									</td>						
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
			<td   align="center">			
				<!--BUTTONS DEFINATION -->	
				<table>
					<tr>
						<td class="zenith" align="left">
							<table>
								<tr>
									<td class="zenith" align="left" style="vertical-align:top;width:15%">
									<table class="zenith">
										<tr>																																																									
										<td class="xslt_fieldHeading" style="text-align: left;">
										<span style="font-weight:bold">Approved amount  &#x20b9;: </span> 
										</td>
										<td class="xslt_fieldData" id="m_fApprovedAmount">
										<span style="font-weight:bold"> </span> 
											<xsl:value-of select="format-number(StudentInformationData/m_oZenithScholarshipDetails/ZenithScholarshipDetails/m_fApprovedAmount, '##,##,##0')" />
										</td>																																			
										</tr>									
									</table>										
									</td>																
								</tr>		
								<tr>
									<td class="zenith" align="left" style="vertical-align:top;width:15%">
										<table class="zenith">
											<tr>
												<td>																																															
													<button type="button" width="20" align="center" class = "zenith addButton" style="width:130px;" id="verifyStudent" title="Prepare Cheque" onClick="prepareCheque()">Prepare Cheque</button>
												</td>																					
											</tr>									
										</table>										
									</td>																
								</tr>								
							</table>												
						</td>																					
					</tr>								
				</table>
			</td>
		</tr>	
		<tr>
			<td>
			<!--Personal details-->
			<table>
				<tr>
					<td width="50%" valign="top">	
						<table class="xslt_trademust" cellSpacing="5px">
							<tr>
								<td class = "xslt_zenithFieldHeader">Academic Details:</td>								
							</tr>
							<tr>
								<td class="xslt_fieldHeading">
									Academic Name:									
								</td>
								<td class="xslt_fieldData" id="m_strInstitutionName">
									<xsl:value-of select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_oInstitutionInformationData/InstitutionInformationData/m_strInstitutionName"/>
								</td>
							</tr>
							<tr>
								<td class="xslt_fieldHeading">
									Course Name:									
								</td>
								<td class="xslt_fieldData" id="m_strCourseName">
									<xsl:value-of select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_oCourseInformationData/CourseInformationData/m_strShortCourseName"/>
								</td>
							</tr>					
						</table>
					</td>					
				</tr>
			</table>
			</td>
			<td>
				<!-- Academic details-->
				<table>
					<tr>
																			
					</tr>
				</table>			
			</td>
		</tr>
	</table>
</body>
</html>	
</xsl:template>
</xsl:stylesheet>
