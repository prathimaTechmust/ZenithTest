<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<div id="printStudent">
						<div class="outerPrintDiv">
							<div class="leftDiv" width="100%">
								<table>
									<tbody>
										<tr>
											<td>
												<div style="width:100%;">
													<img id="zenithlogo" src="images/zenithLogo.png" alt="zenithlogo" align="left" height="100px" />
												</div>
											</td>									
										</tr>
										<tr style="height:20px"></tr>
										<tr>
											<td>
												<div style="width:100%;">
													<p> No.35/1-2, Spencer Road, Near Coles Park, </p>
												</div>
											</td>
										</tr>
										<tr>
											<td>
												<div style="width:100%;">
													<p> Frazer Town, Bangalore-560005. </p>
												</div>
											</td>
										</tr>
										<tr>
											<td>
												<div style="width:100%;">
													<p> Office:080 41120865, 080 43708826 </p>
												</div>
											</td>
										</tr>
										<tr>
											<td>
												<div style="width:100%;">
													<p> Email: admin@thezenithfoundation.com </p>
												</div>
											</td>
										</tr>
									</tbody>
								</table>
							</div>	
							<div  class="rightDiv" width="100%">
								<table>
										<tbody>
											<tr>
												<td>
													<div style="width:100%;">
															<img id="studentimage" src="{StudentInformationData/m_strStudentImageUrl}" alt="zenithlogo" align="right" height="100px" />
													</div>
												</td>									
											</tr>
										</tbody>
								</table>							
							</div>						
						</div>
						
						<table align="center" cellspacing="10">
							<tbody>
								<tr>
									<td>
										<label> Educational Scholarship Form </label>
									</td>
								</tr>
							</tbody>
						</table>
						<div id="personaldetails">
							<table aligh="left" cellspacing="30" style="width:100%">
								<tbody>
									<tr>
										<td>
											<label> UID Number</label>
										</td>
										<td>
											<label id="uidNumber">
												<xsl:value-of select="StudentInformationData/m_nUID" />
											</label>
										</td>
									</tr>
									<tr>
										<td>
											<label> Student Name</label>
										</td>
										<td>
											<label id="studentName">
												<xsl:value-of select="StudentInformationData/m_strStudentName" />
											</label>
										</td>
									</tr>
									<tr>
										<td>
											<label> Father's Name</label>
										</td>
										<td>
											<label id="fathername">
												<xsl:value-of select="StudentInformationData/m_strFatherName" />
											</label>
										</td>
										<td style="width:200px">
										</td>
										<td>
											<label> Father's Occupation</label>
										</td>
										<td>
											<label id="FatherOccupation">
												<xsl:value-of select="StudentInformationData/m_strFatherOccupation" />
											</label>
										</td>
									</tr>
									<tr>
										<td>
											<label> Mother's Name</label>
										</td>
										<td>
											<label id="fathername">
												<xsl:value-of select="StudentInformationData/m_strMotherName" />
											</label>
										</td>
										<td style="width:200px">
										</td>
										<td>
											<label> Mother's Occupation</label>
										</td>
										<td>
											<label id="MotherOccupation">
												<xsl:value-of select="StudentInformationData/m_strMotherOccupation" />
											</label>
										</td>
									</tr>
									<tr>
										<td>
											<label>Contact Number</label>
										</td>
										<td>
											<label id="contactNumber">
												<xsl:value-of select="StudentInformationData/m_strPhoneNumber" />
											</label>
										</td>
										<td style="width:200px"></td>
										<td>
											<label> Alternative Number</label>
										</td>
										<td>
											<label id="alternativeNumber">
												<xsl:value-of select="StudentInformationData/m_strAlternateNumber" />
											</label>
										</td>
									</tr>
									<tr>
										<td>
											<label> E-Mail ID</label>
										</td>
										<td>
											<label id="emailID">
												<xsl:value-of select="StudentInformationData/m_strEmailAddress" />
											</label>
										</td>
									</tr>
									<tr>
										<td>
											<label> Current Home Address</label>
										</td>
										<td>
											<label id="homeaddress">
												<xsl:value-of select="StudentInformationData/m_strCurrentAddress" />
											</label>
										</td>
									</tr>
									<tr style="width:200px">
										<td>
											<label> City</label>
										</td>
										<td>
											<label id="city">
												<xsl:value-of select="StudentInformationData/m_strCity" />
											</label>
										</td>
										<td style="width:200px"></td>
										<td>
											<label> State</label>
										</td>
										<td>
											<label id="state">
												<xsl:value-of select="StudentInformationData/m_strState" />
											</label>
										</td>
										<td style="width:200px"></td>
										<td>
											<label>PIN Code</label>
										</td>
										<td>
											<label id="pincode">
												<xsl:value-of select="StudentInformationData/m_nPincode" />
											</label>
										</td>
									</tr>
									<tr style="width:200px">
										<td>
											<label> Name of the School/college or Institute </label>
										</td>
										<td>
											<label id="schoolname">
												<xsl:value-of
													select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_oInstitutionInformationData/InstitutionInformationData/m_strInstitutionName" />
											</label>
										</td>
									</tr>
									<tr>
										<td>
											<label> class/course </label>
										</td>
										<td>
											<label id="classorcourse">
												<xsl:value-of
													select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_oCourseInformationData/CourseInformationData/m_strShortCourseName" />
											</label>
										</td>
									</tr>
									<tr>
										<td>
											<label> Annual Fee</label>
										</td>
										<td>
											<label id="annualfee">
												<xsl:value-of
													select="format-number(StudentInformationData/m_oAcademicDetails/AcademicDetails/m_fAnnualFee, '##,##,##0')" />
											</label>
										</td>
										<td style="width:200px"></td>
										<td>
											<label>Paid</label>
										</td>
										<td>
											<label id="paid">
												<xsl:value-of
													select="format-number(StudentInformationData/m_oAcademicDetails/AcademicDetails/m_fPaidFee, '##,##,##0')" />
											</label>
										</td>								
									</tr>
								</tbody>
							</table>
						</div>
						<table id="organizationDetails" aligh="left" cellspacing="30"
							height="80%">
							<tbody>
								<tr>
									<td>
										<label> Organizations and amount received in scholoarship</label>
									</td>
								</tr>
							</tbody>
						</table>
						<table id="Siblings Details" aligh="left" cellspacing="30" style="height:50px">
							<tbody>
								<tr>
									<td>
										<label>Details of student's brother/sisters </label>
									</td>
								</tr>
							</tbody>
						</table>			
					   <div style="margin-bottom: 3em;"></div>	
						<p style="text-decoration: underline;  text-align: center;">CERTIFICATE FROM THE INSTITUTION </p>
						 <br/>
						 <p>  we, hereby certify that <label id="studentName" > </label> is a student of our institution studying in the class  <label id="classId" > </label> 
						 During the academic year <label id="academicyear" > </label></p>
						 <p> Name of the school/ college / institute / Trust on which cheque/ DD needs to be Issued.</p>
					     <div style="margin-bottom: 3em;"></div>						 					 
						  <div id="CommentSection" style="border:1px solid black;" >
							<p> Full address, Seal and Email-ID of the School/ College/ Institution/ Trust </p>
							<div style="margin-bottom: 9em;"></div>
							<p>  HEAD MASTER /  PRINCIPAL / DEAN or AUTHORISED PERSON </p>
							<table id="CetificateTable" aligh="left" cellspacing="30" >
								<tbody>
									<tr style="width:200px">
										<td><label> NAME</label></td>						
										<td style="width:200px"></td>									
										<td><label> CONTACT NUMBER</label></td>									
									</tr>
									<tr>
										<td><label> SIGNATURE</label></td>										
									</tr>
								</tbody>
							</table>
						</div>
				        <div style="margin-bottom: 3em;"></div>	
						<div id="CommentSection" style="border:1px solid black;" >
							<p>  Comments from student / parents /Guardian</p>
							<table id="CommentSection" aligh="left" cellspacing="30" style="width: 100%;" >
								<tbody>
									<tr style="width:200px"  height="20">
							
									</tr>			
									<tr style="width:200px"  height="20">
										
									</tr>
									<tr style="width:200px"  height="20">
										
									</tr>
									<tr style="width:200px"  height="20">
										
									</tr>
									<tr style="width:200px"  height="20">
										
									</tr>
								</tbody>
							</table>
						</div>						 						  			 										
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>    
        