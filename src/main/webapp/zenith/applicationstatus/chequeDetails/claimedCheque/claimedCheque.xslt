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
		
		table.organization {
		border-collapse: collapse;
		}
	
		td.organization {
		border: 1px solid black;
		padding: 10px;
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
			<td style="width: 50%;" >
				<table>
					<tr>
						<td align="left">
							<a href="javascript:studentList_setPreview ('{StudentInformationData/m_strStudentImageUrl}');">
								<img src="{StudentInformationData/m_strStudentImageUrl}" id = "imagePreview_img_student" class="studentDetailImage" onerror="this.onerror=null; this.src='images/NoImage.png'" />
							</a>
							<br/>
						</td>
						<td >
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
									<td class="xslt_fieldHeading">Application Status :</td>
									<td class="xslt_fieldData" id="m_strApplicationStatus">
										<xsl:value-of select="StudentInformationData/m_oZenithScholarshipDetails/ZenithScholarshipDetails/m_strStatus"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
			<td align="center">			
				<!--BUTTONS DEFINATION -->	
				<table>
					<tr>
						<td class="zenith" align="left">
							<table>		
								<tr>
									<td class="zenith" align="left" style="vertical-align:top;width:15%">
										<table class="zenith">
											<tr>
												<td>																																															
											        <button type="button" width="120px" align="center" class="zenith addButton" style="width:120px;" id="documentView" title="documentView" onclick="viewStudentDocument('{StudentInformationData/m_oAcademicDetails/AcademicDetails/m_nAcademicId}')">View Documents </button>																																															
													<button type="button" width="20" align="center" class = "zenith addButton" style="width:100px;" id="printStudent" title="Print" onClick="printClaimedChequeList()">Print</button>
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
					<td>
						<table class="zenith" cellSpacing="5px">
							<tr>
								<td class = "xslt_zenithFieldHeader">Personal Details:</td>								
							</tr>						
							<tr>
								<td class ="xslt_fieldHeading">
									Facilitator Name:
								</td>
								<td class ="xslt_fieldData" id = "m_strFacilitatorName">
									<xsl:value-of select="StudentInformationData/m_oFacilitatorInformationData/FacilitatorInformationData/m_strFacilitatorName"/>
								</td>
							</tr>
							<tr>
								<td class="xslt_fieldHeading">Parental Status:</td>
								<td class="xslt_fieldData" id="m_strParentalStatus">
									<xsl:value-of select= "StudentInformationData/m_strParentalStatus" />
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
								<td class="xslt_fieldHeading">Student Aadhar Number :</td>
								<td class="xslt_fieldData" id="m_nStudentAadharNumber">
									<xsl:value-of select="StudentInformationData/m_nStudentAadharNumber" />
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
						</table>		
					</td>
					<td>
						<table class="zenith" cellSpacing="5px">
							<tr>
								<td class = "xslt_zenithFieldHeader"> <br/> </td>								
							</tr>
							<tr>
								<td class="xslt_fieldHeading">
								 Date of Birth:
								</td>
								<td class="xslt_fieldData" id="m_dDateOfBirth">
									<xsl:value-of select="StudentInformationData/m_dDateOfBirth" />
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
									Email-Id :
								</td>
								<td class="xslt_fieldData" id="m_strEmailAddress">
									<xsl:value-of select="StudentInformationData/m_strEmailAddress" />
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
				</tr>
			</table>
			</td>
			<td>
				<!-- Academic details-->
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
								<tr>
									<td class="xslt_fieldHeading">
										Specialization:									
									</td>
									<td class="xslt_fieldData" id="m_strSpecialization">
										<xsl:value-of select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_strSpecialization"/>
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Score:									
									</td>
									<td class="xslt_fieldData" id="m_strStudentScore">
										<xsl:value-of select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_strStudentScore"/>
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Annual Fee(₹):									
									</td>
									<td class="xslt_fieldData" id="m_fAnnualFee">
										<xsl:value-of select="format-number(StudentInformationData/m_oAcademicDetails/AcademicDetails/m_fAnnualFee, '##,##,##0')"/>
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Paid Fee(₹):									
									</td>
									<td class="xslt_fieldData" id="m_fPaidFee">
										<xsl:value-of select="format-number(StudentInformationData/m_oAcademicDetails/AcademicDetails/m_fPaidFee, '##,##,##0')"/>
									</td>
								</tr>								
							</table>
						</td>																		
					</tr>
				</table>			
			</td>
		</tr>
		<tr>
		<td class="topAlign">
		<!-- Parents guardian details -->
		<table>
		<tr>
			<td>
				<table>
					<tr>
						<td class="topAlign">
							<table class="xslt_trademust" cellSpacing="5px">
								<tr>
									<td class = "xslt_zenithFieldHeader">Family Details:</td>								
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
										Father Occupation :
									</td>
									<td class="xslt_fieldData" id="m_strFatherOccupation">
										<xsl:value-of select="StudentInformationData/m_strFatherOccupation" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Father Aadhar Number :
									</td>
									<td class="xslt_fieldData" id="m_nFatherAadharNumber">
										<xsl:value-of select="StudentInformationData/m_nFatherAadharNumber" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">Income Per Annum(₹):</td>
									<td class="xslt_fieldData" id="m_nFamilyIncome">
										<xsl:value-of select= "format-number(StudentInformationData/m_nFamilyIncome, '##,##,##0')" />
									</td>
								</tr>	
							</table>						
						</td>
						<td class="topAlign">
							<table class="xslt_trademust" cellSpacing="5px">
								<tr>
									<td class = "xslt_zenithFieldHeader"> <br/> </td>								
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
										Mother Occupation :
									</td>
									<td class="xslt_fieldData" id="m_strMotherOccupation">
										<xsl:value-of select="StudentInformationData/m_strMotherOccupation" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Mother Aadhar Number :
									</td>
									<td class="xslt_fieldData" id="m_nMotherAadharNumber">
										<xsl:value-of select="StudentInformationData/m_nMotherAadharNumber" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Alternative Number :
									</td>
									<td class="xslt_fieldData" id="m_strAlternateNumber">
										<xsl:value-of select="StudentInformationData/m_strAlternateNumber" />
									</td>								
							s	</tr>
							</table>		
						</td>				
					</tr>
					<tr>
						<td class="topAlign" >
							<table class="xslt_trademust" cellSpacing="5px">
								<tr>
									<td class = "xslt_zenithFieldHeader">Siblings:</td>								
								</tr>						
								<tr>
									<td>
										<table border="1" cellSpacing="5px" class="organization" >	
											<tr>
												<td class="organization">Name</td>
												<td class="organization">Studying</td>
												<td class="organization">School/College</td>
											</tr>																			
											<tr>
												<xsl:for-each select="StudentInformationData/m_oSibilingDetails/SiblingsDetails">
												<tr>
													<td class="organization organizationData" >
														<xsl:value-of select="m_strSiblingName" />
													</td>
													<td class="organization organizationData">
														<xsl:value-of select="m_strStudying"/>
													</td>
													<td class="organization organizationData">
														<xsl:value-of select="m_strStudyingInstitution"/>
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
			</td>	
			</tr>
			</table>			
			</td>
			<td class="topAlign">
			<!-- Scholarship  details -->
			<table>
			<tr>
				<td>
					<table class="xslt_trademust" cellSpacing="5px">
						<tr>
							<td class = "xslt_zenithFieldHeader">Scolarship Details</td>	
						</tr>
						<tr>
							<td class="xslt_fieldHeading">
								<label class="dataLabel">Required amount(₹) : </label>
							</td>
							<td class="xslt_fieldData">
								<xsl:variable name="annualFee" select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_fAnnualFee"></xsl:variable>
								<xsl:variable name="paidFee" select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_fPaidFee"></xsl:variable>
								<xsl:value-of select="format-number($annualFee - $paidFee, '##,##,##0')"/>
							</td>									
						</tr>
						<tr> 
							<td class="xslt_fieldHeading">
								Sanctioned amount(₹):
							</td>
							<td class="xslt_fieldData">
								<xsl:value-of select="format-number(StudentInformationData/m_oZenithScholarshipDetails/ZenithScholarshipDetails/m_fApprovedAmount, '##,##,##0')" ></xsl:value-of>
							</td>									
						</tr>							
					</table>
					<table border="1" cellSpacing="5px" class="organization" >	
						<tr>
							<td class="organization">Organization Name</td>
							<td class="organization">Amount(₹)</td>
						</tr>																			
						<tr>
						<xsl:for-each select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_oScholarshipDetails/ScholarshipDetails">
						<tr>
							<td class="organization organizationData" >
								<xsl:value-of select="m_strOrganizationName" />
							</td>
							<td class="organization organizationData">
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
		<tr>
		<!--CHEQUE DETAILS -->
		<td  class="topAlign">
			<table class="xslt_trademust" cellSpacing="5px">
				<tr>
					<td class = "xslt_zenithFieldHeader">Cheque Details</td>	
				</tr>
				<tr>
					<td class="xslt_fieldHeading">
						In favour of 
					</td>
					<td>:</td>
					<td class="xslt_fieldData">
						<xsl:value-of select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_oInstitutionInformationData/InstitutionInformationData/m_strInstitutionName">
	                      </xsl:value-of>
					</td>									
				</tr>		
				<tr>
					<td class="xslt_fieldHeading">
						Sanctioned Date 
					</td>
					<td>:</td>
					<td class="xslt_fieldData">
						<xsl:value-of select="StudentInformationData/m_oZenithScholarshipDetails/ZenithScholarshipDetails/m_dApprovedDate" >
						</xsl:value-of>
					</td>									
				</tr>
				<tr>
					<td class="xslt_fieldHeading">
						Claimed on 
					</td>
					<td>:</td>
					<td class="xslt_fieldData">
						<xsl:value-of select="StudentInformationData/m_oZenithScholarshipDetails/ZenithScholarshipDetails/m_dClaimedDate" >
						</xsl:value-of>				
					</td>									
				</tr>				
			</table>		
		</td>
		<!--Collected By -->
		<td  class="topAlign">
			<table class="xslt_trademust" cellSpacing="5px">
				<tr>
					<td class = "xslt_zenithFieldHeader">Collected By</td>	
				</tr>
				<tr>
					<td class="xslt_fieldHeading">
						Cheque / DD Number 
					</td>
					<td>:</td>
					<td class="xslt_fieldData">
						<xsl:value-of select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_oStudentScholarshipAccount/StudentScholarshipAccount/m_nChequeNumber">
						</xsl:value-of>
					
					</td>									
				</tr>	
				<tr>
					<td class="xslt_fieldHeading">
						Receiver name 
					</td>
					<td>:</td>
					<td  class="xslt_fieldData">
					<xsl:value-of select="StudentInformationData/m_oZenithScholarshipDetails/ZenithScholarshipDetails/m_strReceiverName" >
						</xsl:value-of>						
					</td>									
				</tr>		
				<tr>
					<td class="xslt_fieldHeading">
						Receiver contact number 
					</td>
					<td>:</td>
					<td class="xslt_fieldData">
						<xsl:value-of select="StudentInformationData/m_oZenithScholarshipDetails/ZenithScholarshipDetails/m_strReceiverContactNumber" >
						</xsl:value-of>
					
					</td>									
				</tr>
				<tr>
					<td class="xslt_fieldHeading">
						Date of issue 
					</td>
					<td>:</td>
					<td class="xslt_fieldData">
						<xsl:value-of select="StudentInformationData/m_oZenithScholarshipDetails/ZenithScholarshipDetails/m_dChequeIssueDate" >
						</xsl:value-of>					
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
