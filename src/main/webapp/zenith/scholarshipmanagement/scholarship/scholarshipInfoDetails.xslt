<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:variable name="m_nStudentId" select="StudentInformationData/m_nStudentId" />
	<xsl:template match="/">
		<html>
			<body>
				<table class="zenith">
					<tr>
			 			<td class="xslt_fieldHeader" colspan="2">Scholarship Information Details</td>
					</tr>
					<tr>
						<td width="50%" valign="top">
							<table class="zenith" cellSpacing="5px">
								<tr>
									<td class="xslt_fieldHeading">
										UID :
									</td>
									<td class="xslt_fieldData" id="m_nUID">
										<xsl:value-of select="StudentInformationData/m_nUID" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Student Name :
									</td>
									<td class="xslt_fieldData" id="m_strStudentName">
										<xsl:value-of select="StudentInformationData/m_strStudentName" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Course Name :
									</td>
									<td class="xslt_fieldData" id="m_strCourseName">
										<xsl:value-of select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_strCourseName" />
									</td>
								</tr>								
							</table>
						</td>
						<td width="50%" valign="top">
							<table class="xslt_trademust" cellSpacing="5px">
								<tr>
									<td class="xslt_fieldHeading">
										Total Fee :
									</td>
									<td class="xslt_fieldData" id="m_fAnnualFee">
										<xsl:value-of select="format-number(StudentInformationData/m_oAcademicDetails/AcademicDetails/m_fAnnualFee, '##,##,##0')" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Paid Fee :
									</td>
									<td class="xslt_fieldData" id="m_fPaidFee">
										<xsl:value-of select="format-number(StudentInformationData/m_oAcademicDetails/AcademicDetails/m_fPaidFee, '##,##,##0')" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Scholarship Required :
									</td>
									<td class="xslt_fieldData" id="m_strScholarshipRequired">
										<xsl:value-of select="StudentInformationData/m_oScholarshipDetails/ScholarshipDetails/m_strScholarshipRequired" />
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
