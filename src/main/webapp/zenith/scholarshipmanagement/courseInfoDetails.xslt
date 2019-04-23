<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:variable name="m_nCourseId" select="CourseInformationData/m_nCourseId" />
	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
					<tr>
			 			<td class="xslt_fieldHeader" colspan="2">Course Information Details</td>
					</tr>
					<tr>
						<td width="50%" valign="top">
							<table class="trademust" cellSpacing="5px">
								<tr>
									<td class="xslt_fieldHeading">ShortCourse Name :</td>
									<td class="xslt_fieldData" id="m_strShortCourseName">
										<xsl:value-of select="CourseInformationData/m_strShortCourseName" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										LongCourse Name :
									</td>
									<td class="xslt_fieldData" id="m_strLongCourseName">
										<xsl:value-of select="CourseInformationData/m_strLongCourseName" />
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
