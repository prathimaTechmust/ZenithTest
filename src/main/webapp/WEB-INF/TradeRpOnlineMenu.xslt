<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html>
      <body>
		<ul class="side-nav leftSideNav">
			<xsl:for-each select="root/ActionArea">
			<xsl:sort select="Sequence" data-type="number"/>
		      <ul class="sidenav">
		      	 <xsl:for-each select="ActionList/Action">
				 <xsl:sort select="Sequence" data-type="number"/>
			         <li style="list-style: none;"><a href='javascript:{Target}'><span><xsl:value-of select="ActionName"/></span></a></li>
		         </xsl:for-each>
		      </ul>
		   </xsl:for-each>
		</ul>
	</body>
	</html>
  </xsl:template>

</xsl:stylesheet>
