<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
        <head>
        <style>		
				td{
					vertical-align: top;				
				}
				
				.fullWidthTable {
					width:100%;
				}
				
				.mainTable{
					   border: 1px solid black;
					   padding: 5px;
				}				
				
				.tdBorder {
						border: 1px black;
						border-width: 0.4;
						border-style: solid;
				}
				.title {
					padding: 10px;
					border-bottom: 1px black;
					border-bottom-width: 0.4;
					border-bottom-style: solid;
					margin: 0px;
				}
				.rightimage {
					float: right;
					height: 150px;
				}
				
				.leftimage {
					float: left;
					height: 100px;
				}
				.centerParagraph{
					text-align:center;
				}
				
				.rightParagraph {
					text-align:right;
					float: right;
					font-size: 20px;
				}
						
				.fontSize15px{
					font-size: 20px;
				}
				
				.thick{
					font-weight:600;
				 }
				 
				 .headRow{
					font-weight:600;
					font-size: 15px;
				 }
				 
				 .title{
				 font-weight:600;
				 font-size: 15px;
				 }
				 
				 .dataValue{
				    font-size: 11px;
					font-weight:600;				
				 }
				 
				  .dataLabel{
				    font-size: 11px;
					font-weight:500;
					
				 }
				 			 
				.studentNameFontSize{
					font-size: 14px;
				 }	
				 
				.borderRight{
				    width: 50%;
				 border-right:1px solid black;
				}
				
				.borderRightThreeColumn{
				    width: 33%;
					border-right:1px solid black;
				}
				
				.contentpadding{
				    padding-left: 3px;
					padding-right: 3px;
				}
		 			
        </style>
        </head>
	<body>
			<table width="100%" class="mainTable">
				<tbody>
				<tr><!-- Logo Row --></tr>
				<tr>
					<td>
						<table width="100%">
							<tbody>
							<tr>
									<td>
										<table>
											<tr>
												<td class="dataLabel">
													<img class="leftimage" src="images/zenithLogo.png"/>
												</td>
											</tr>
											<tr>
												<td class="dataLabel">
													<p>
														35/1-2 Spencer Road,<br/>
														Behind Santhosh Hospital,<br/>
														Frazer Town<br/>
														Bangalore-05<br/>
														Cell : 8553743750<br/>
														Ph: 080-41120865 | 080-43708826<br/>
													</p>
												</td>
											</tr>
										</table>
									</td>
									<td><img class="rightimage" src="{StudentInformationData/m_strStudentImageUrl}"/></td>
							</tr>
							</tbody>
						</table>
					</td>
				</tr>
				<!-- Form Head Row -->
				<tr>
					<td>
						<p class="centerParagraph headRow">Educational Scholarship Form <span class="rightParagraph">UID : <xsl:value-of select="StudentInformationData/m_nUID" /></span></p>
						<table><tbody><tr></tr></tbody></table>
					</td>
				</tr>
					

				<!-- Student Details Row -->
					<tr>
						<td class="tdBorder">
							<p class="title">Student Details</p><table><tbody><tr></tr><tr></tr></tbody></table>
							<table class="fullWidthTable">
							<tbody>
							<tr>
							<td class="borderRight">
								<!-- Student Personal Details -->
								<table>									
								<tbody>
									<tr>
									  <td class="dataLabel">
										 <label>Student Name </label> 								  
									  </td>
									  <td>:</td>
									  <td class="dataValue studentNameFontSize">
										<xsl:value-of select="StudentInformationData/m_strStudentName"></xsl:value-of></td>
									</tr>									
									<tr>
										<td class="dataLabel">
											<label>Contact Number </label>
										</td>
										<td>:</td>
										<td class="dataValue">
											<xsl:value-of select="StudentInformationData/m_strPhoneNumber">
										</xsl:value-of></td>
									</tr>								
									<tr>
										<td class="dataLabel">
											<label>Alternative Number </label>
										</td>
										<td>:</td>
										<td class="dataValue">
											<xsl:value-of select="StudentInformationData/m_strAlternateNumber"></xsl:value-of></td>
									</tr>
									<tr>
										<td class="dataLabel">
											<label> E-Mail ID </label>
										</td>
										<td>:</td>
									    <td class="dataValue">
											<xsl:value-of select="StudentInformationData/m_strEmailAddress"></xsl:value-of>
										</td>
									</tr>								 
								</tbody>
								</table>
							</td>
							<td>
							<!-- Student Contact Details -->
										<table>
											<tbody><tr>
												  <td class="dataLabel">
													 <label>Home Address </label> 								  
												  </td>
												  <td>:</td>
												  <td class="dataValue">
													<xsl:value-of select="StudentInformationData/m_strCurrentAddress">									  
												  </xsl:value-of></td>
											</tr>									
												<tr>
													<td class="dataLabel">
														<label>City </label>
													</td>
													<td>:</td>
													<td class="dataValue">
													 <xsl:value-of select="StudentInformationData/m_strCity"> 
													</xsl:value-of></td>
												</tr>								
												<tr>
													<td class="dataLabel">
														<label>State </label>
													</td>
													<td>:</td>
													<td class="dataValue">
													 <xsl:value-of select="StudentInformationData/m_strState">										  
													</xsl:value-of></td>
												</tr>
												<tr>
													<td class="dataLabel">
														<label>PIN Code</label>
													</td>
													<td>:</td>
													<td class="dataValue">
														<xsl:value-of select="StudentInformationData/m_nPincode">										  
													 </xsl:value-of></td>
												</tr>			
										</tbody></table>
									</td>
								</tr>							
							</tbody>
							</table>
						</td>
					</tr>

				<!-- Family Details Row -->
					<tr>
						<td class="tdBorder">
							<p class="title">Family Details</p><table>
								<tbody><tr></tr><tr></tr></tbody></table>
								<table class="fullWidthTable">
								<tbody>
								<tr>
								<td class="borderRight">
									<!-- Parent Details -->
									<table>
										<tbody>
											<tr>
												<td>
													<label class="dataLabel">Father Name</label>
												</td>
												<td>:</td>
												<td class="dataValue">
												<xsl:value-of select="StudentInformationData/m_strFatherName">
												</xsl:value-of></td>									
											</tr>
											<tr>
												<td>
													<label class="dataLabel">Mother Name</label>
												</td>
												<td>:</td>
												<td class="dataValue">
												<xsl:value-of select="StudentInformationData/m_strMotherName">
												</xsl:value-of></td>									
											</tr>
											<tr>
												<td>
													<label class="dataLabel">Family Income per Annum(₹) </label>
												</td>
												<td>:</td>
												<td class="dataValue"> 
												 <xsl:value-of select="format-number(StudentInformationData/m_nFamilyIncome, '##,##,##0')">
												</xsl:value-of></td>									
											</tr>
											<tr>
												<td>
												  <label class="dataLabel">Parental Status</label>
												</td>
												<td>:</td>
												<td class="dataValue">
												 <xsl:value-of select="StudentInformationData/m_strParentalStatus">
												</xsl:value-of></td>									
											</tr>
										</tbody>
									</table>
								</td>
								<td class="borderRightThreeColumn">
								<!-- Occupation Details -->
								<table>
									<tbody>
										<tr>
											<td>
												<label class="dataLabel"> Occupation</label>
											</td>
											<td>:</td>
											<td class="dataValue">
											 <xsl:value-of select="StudentInformationData/m_strFatherOccupation">
											</xsl:value-of></td>									
										</tr>
										<tr>
											<td>
												<label class="dataLabel"> Occupation</label>
											</td>
											<td>:</td>
											<td class="dataValue">
											 <xsl:value-of select="StudentInformationData/m_strMotherOccupation">
											</xsl:value-of></td>									
										</tr>
									</tbody>
								</table>
								</td>
								<td>
				<!-- ID Details -->
								<table>
									<tbody>
										<tr>
											<td>
												<label class="dataLabel">Aadhaar Number</label>
											</td>
											<td>:</td>
											<td class="dataValue">
											<xsl:value-of select="StudentInformationData/m_nFatherAadharNumber"></xsl:value-of></td>									
										</tr>
										<tr>
											<td>
											 <label class="dataLabel">Aadhaar Number</label>
											</td>
											<td>:</td>
											<td class="dataValue">
											<xsl:value-of select="StudentInformationData/m_nMotherAadharNumber">
											</xsl:value-of></td>									
										</tr>
									</tbody>
								</table>
								</td>
								</tr>
								</tbody>
								</table>
							</td>
						</tr>
										
				<!--Academic details-->
					<tr>
						<td class="tdBorder">
							<p class="title">Academic  Details</p><table><tbody><tr></tr><tr></tr></tbody></table>
							<table class="fullWidthTable">
								<tbody>
									<tr>
										<td class="borderRight">
				<!-- Institite Information Details -->
											<table>		
												<tbody>
												<tr>
													<td>
														<label class="dataLabel">Name of the School/College or Institute </label>
													</td>
													<td>:</td>
													<td class="dataValue">
													 <xsl:value-of select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_oInstitutionInformationData/InstitutionInformationData/m_strInstitutionName">
													</xsl:value-of></td>									
												</tr>
												<tr>
													<td>
														<label class="dataLabel">Class/Course</label>
													</td>
													<td>:</td>
													<td class="dataValue">
													<xsl:value-of select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_oCourseInformationData/CourseInformationData/m_strShortCourseName">
													</xsl:value-of>
													</td>									
												</tr>															 
												</tbody>
											</table>
										</td>
										<td>
				<!-- Fee Information Details -->
											<table>
												<tbody>
													<tr>
														<td>
															<label class="dataLabel">Annual Fees(₹)</label>
														</td>
														<td>:</td>
														<td class="dataValue">
														<xsl:value-of select="format-number(StudentInformationData/m_oAcademicDetails/AcademicDetails/m_fAnnualFee, '##,##,##0')"></xsl:value-of></td>									
													</tr>
												</tbody>
												</table>
											</td>
										</tr>
									</tbody>
								</table>
								</td>
							</tr>
						
					
				<!--Certificate section -->
					<tr>
						<td class="tdBorder">
							<p class="centerParagraph thick" style="text-decoration: underline;">CERTIFICATE FROM THE INSTITUTION </p><table>
								<tbody><tr></tr><tr></tr></tbody></table>
								<table class="fullWidthTable ContentPadding">
									<tbody>
										<tr>
											<td>
											 <p>
												 we,hereby certify that 
												 <label id="studentname" class="thick"><xsl:value-of select="StudentInformationData/m_strStudentName"> </xsl:value-of></label> 
												 is a student of our institution studying in the class 
												 <label id="instituteName" class="thick">  <xsl:value-of select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_oCourseInformationData/CourseInformationData/m_strShortCourseName"> </xsl:value-of></label> 
												 During the academic year 
												 <label id="academicyear" class="thick"><xsl:value-of select="StudentInformationData/m_oAcademicDetails/AcademicDetails/m_oAcademicYear/AcademicYear/m_strAcademicYear"></xsl:value-of></label>
											 </p>
											</td>
										</tr>
										<tr>
											<td>
												<br/>
											</td>
										</tr>									
										<tr>
											<td>
												<p> Name of the school/ college / institute / Trust on which <span class="headRow"> cheque/ DD </span> needs to be Issued.</p>
											</td>										
										</tr>
										<tr>
											<td>
												<br/><hr/>												
											</td>
										</tr>
										<tr>
											<td>
												<p> Full address, Seal and Email-ID of the School/ College/ Institution/ Trust </p>
												<br/><br/><br/><br/>	
											</td>
										</tr>
										<tr>
											<td>
												<p> HEAD MASTER /  PRINCIPAL / DEAN or AUTHORISED PERSON </p><br/>
											</td>
										</tr>
										<tr> </tr>
									</tbody>
								</table>
								<table class="fullWidthTable"> 
									<tbody><tr><td></td></tr>
										<tr width="50%">
											<td>
												<p> Name</p>
											</td>
											<td>
												<p> Contact Number</p>
											</td>													
										</tr>
										<tr>
											<td>
												<br/>
											</td>
										</tr>
										<tr width="50%">
											<td>
												<p> Signature</p>
											</td>
										</tr>														
									</tbody>
							</table>
						</td>
					</tr>
				</tbody>
				</table>								
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>    
        