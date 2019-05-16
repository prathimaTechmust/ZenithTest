<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:variable name="m_nStudentId" select="StudentInformationData/m_nStudentId" />
	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
					<tr>
			 			<td class="xslt_fieldHeader" colspan="2">Student Information Details</td>
					</tr>
					<tr>
						<td width="50%" valign="top">
							<table class="trademust" cellSpacing="5px">
								<tr>
									<td class="xslt_fieldHeading">UID :</td>
									<td class="xslt_fieldData" id="m_nUID">
										<xsl:value-of select="StudentInformationData/m_nUID" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">Student Name :</td>
									<td class="xslt_fieldData" id="m_strStudentName">
										<xsl:value-of select="StudentInformationData/m_strStudentName" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Father Name :
									</td>
									<td class="xslt_fieldData" id="m_strFatherName">
										<xsl:value-of select="StudentInformationData/m_strFatherName" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Mother Name :
									</td>
									<td class="xslt_fieldData" id="m_strMotherName">
										<xsl:value-of select="StudentInformationData/m_strMotherName" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Religion :
									</td>
									<td class="xslt_fieldData" id="m_strReligion">
										<xsl:value-of select="StudentInformationData/m_strReligion" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Phone Number :
									</td>
									<td class="xslt_fieldData" id="m_strPhoneNumber">
										<xsl:value-of select="StudentInformationData/m_strPhoneNumber" />
									</td>									
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Alternative Number :
									</td>
									<td class="xslt_fieldData" id="m_strAlternateNumber">
										<xsl:value-of select="StudentInformationData/m_strAlternateNumber" />
									</td>								
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Email-Id :
									</td>
									<td class="xslt_fieldData" id="m_strEmailAddress">
										<xsl:value-of select="StudentInformationData/m_strEmailAddress" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Gender :
									</td>
									<td class="xslt_fieldData" id="m_strGender">
										<xsl:value-of select="StudentInformationData/m_strGender" />
									</td>
								</tr>
							</table>
						</td>
						<td width="50%" valign="top">
							<table class="xslt_trademust" cellSpacing="5px">
								<tr>
									<td class="xslt_fieldHeading">Income Per Annum(₹):</td>
									<td class="xslt_fieldData" id="m_nFamilyIncome">
										<xsl:value-of select= "format-number(StudentInformationData/m_nFamilyIncome, '##,##,##0')" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Father Occupation :
									</td>
									<td class="xslt_fieldData" id="m_strFatherOccupation">
										<xsl:value-of select="StudentInformationData/m_strFatherOccupation" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Mother Occupation :
									</td>
									<td class="xslt_fieldData" id="m_strMotherOccupation">
										<xsl:value-of select="StudentInformationData/m_strMotherOccupation" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Address :
									</td>
									<td class="xslt_fieldData" id="m_strCurrentAddress">
										<xsl:value-of select="StudentInformationData/m_strCurrentAddress" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										City :
									</td>
									<td class="xslt_fieldData" id="m_strCity">
										<xsl:value-of select="StudentInformationData/m_strCity" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										State :
									</td>
									<td class="xslt_fieldData" id="m_strState">
										<xsl:value-of select="StudentInformationData/m_strState" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										PinCode :
									</td>
									<td class="xslt_fieldData" id="m_nPincode">
										<xsl:value-of select="StudentInformationData/m_nPincode" />
									</td>
								</tr>
							</table>
						</td>
						<td class="trademust" align="left" style="vertical-align:top;width:15%">
							<table class="trademust">
								<tr>
									<td class="trademust" align="center">
										<a href="javascript:studentList_setPreview ('{StudentInformationData/m_strStudentImageUrl}');">
											<img src="{StudentInformationData/m_strStudentImageUrl}" id = "imagePreview_img_student" class="studentDetailImage"/>
										</a>
										<br />
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<!-- <tr>
			 						 						 			
											
						
					</tr> -->
					<tr>
						<td width="50%" valign="top">
							<table class="xslt_trademust" cellSpacing="5px">
								<tr>
									<td class = "xslt_zenithFiledHeader">Academic Details</td>								
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Academic Name:									
									</td>
									<td class="xslt_fieldData" id="m_strInstitutionName">
										<xsl:value-of select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_strInstitutionName"/>
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Course Name:									
									</td>
									<td class="xslt_fieldData" id="m_strCourseName">
										<xsl:value-of select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_strCourseName"/>
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Annual Fee(₹):									
									</td>
									<td class="xslt_fieldData" id="m_strCourseName">
										<xsl:value-of select="format-number(StudentInformationData/m_oAcademicDetails/AcademicDetails/m_fAnnualFee, '##,##,##0')"/>
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Paid Fee(₹):									
									</td>
									<td class="xslt_fieldData" id="m_strCourseName">
										<xsl:value-of select="format-number(StudentInformationData/m_oAcademicDetails/AcademicDetails/m_fPaidFee, '##,##,##0')"/>
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Balance Fee(₹):									
									</td>
									<td class="xslt_fieldData" id="m_strCourseName">
										<xsl:value-of select="format-number(StudentInformationData/m_oAcademicDetails/AcademicDetails/m_fBalanceFee, '##,##,##0')"/>
									</td>
								</tr>
							</table>
						</td>
						<td width="50%" valign="top">
							<table class="xslt_trademust" cellSpacing="5px">
								<tr>
									<td class = "xslt_zenithFiledHeader">Scolarship Details</td>	
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Organization Name
									</td>
									<td class="xslt_fieldHeading">
										Amount(₹)
									</td>
								</tr>																
								<tr>
									<xsl:for-each select="StudentInformationData/m_oScholarshipDetails/ScholarshipDetails">
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
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
