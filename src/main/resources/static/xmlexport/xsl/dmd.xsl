<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
  <html>
    <body>
      <h1>Demande de troc recue :</h1>
      <ul>
	<li>Demande recue par : <xsl:value-of select="Fichier/Header/nmIE"/></li>
	<li>Mail de l'emmeteur : <xsl:value-of select="Fichier/Header/MailExp"/></li>
	<li>Description de la demande : <xsl:value-of select="Fichier/Body/CollMess/Message/Dmd/DescDmd"/></li>
      </ul>
    </body>
  </html>
  </xsl:template>

</xsl:stylesheet>
