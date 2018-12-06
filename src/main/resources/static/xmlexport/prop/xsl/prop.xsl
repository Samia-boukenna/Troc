<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


    <xsl:template match="/">
        <html xmlns:th="http://www.thymeleaf.org" lang="en">
            <head>
                <meta charset="utf-8"/>
                <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
                <meta name="viewport" content="width=device-width, initial-scale=1"/>
                <meta name="description" content=""/>
                <meta name="author" content=""/>
                <link rel="icon" type="image/png" sizes="16x16" href="../plugins/images/favicon.png"/>
                <title>Ample Admin Template - The Ultimate Multipurpose admin template</title>

                <link href="../../css/bootstrap.min.css" rel="stylesheet"/>

                <link href="../../css/sidebar-nav.min.css" rel="stylesheet"/>

                <link href="../../css/animate.css" rel="stylesheet"/>

                <link href="../../css/style.css" rel="stylesheet"/>

                <link href="../../css/colors/default.css" id="theme" rel="stylesheet"/>
                <link href="../../css/troc.css"  rel="stylesheet"/>

                <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
                <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
                <!--[if lt IE 9]>
                <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
                <script src="https://oss.maxcdn.com/libs/respond.../js/1.4.2/respond.min.js"></script>
                <![endif]-->
            </head>
            <body class="fix-header">
                <!-- ============================================================== -->
                <!-- Preloader -->
                <!-- ============================================================== -->
                <div class="preloader">
                    <svg class="circular" viewBox="25 25 50 50">
                        <circle class="path" cx="50" cy="50" r="20" fill="none" stroke-width="2" stroke-miterlimit="10" />
                    </svg>
                </div>
                <h1>Demande de troc recue :</h1>
                <div id="wrapper">
                    <!-- ============================================================== -->
                    <!-- Topbar header - style you can find in pages.scss -->
                    <!-- ============================================================== -->
                    <nav class="navbar navbar-default navbar-static-top m-b-0">
                        <div class="navbar-header">
                            <div class="top-left-part">
                                <!-- Logo -->
                                <a class="logo" href="/">
                                    <img src="../../images/xml-troc.png" width="200" alt="home" class="light-logo img-responsive" style="display: inline-block" />
                                </a>
                            </div>
                            <!-- /Logo -->
                        </div>
                        <!-- /.navbar-header -->
                        <!-- /.navbar-top-links -->
                        <!-- /.navbar-static-side -->
                    </nav>
                    <!-- End Top Navigation -->
                    <!-- ============================================================== -->
                    <!-- Left Sidebar - style you can find in sidebar.scss  -->
                    <!-- ============================================================== -->
                    <div class="navbar-default sidebar" role="navigation">
                        <div class="sidebar-nav slimscrollsidebar">
                            <div class="sidebar-head">
                                <h3>
                                    <span class="fa-fw open-close">
                                        <i class="ti-close ti-menu"></i>
                                    </span> 
                                    <span class="hide-menu">Navigation</span>
                                </h3>
                            </div>
                            <ul class="nav" id="side-menu">
                                <li style="padding: 70px 0 0;">
                                    <a href="/nouvelle_dmd" class="waves-effect">
                                        <i class="fa fa-clock-o fa-fw" aria-hidden="true"></i>Envoyer une nouvelle demande</a>
                                    <a href="/nouvelle_prop" class="waves-effect">
                                        <i class="fa fa-clock-o fa-fw" aria-hidden="true"></i>Envoyer une nouvelle proposition</a>
                                    <a href="/mes_dmd" class="waves-effect">
                                        <i class="fa fa-clock-o fa-fw" aria-hidden="true"></i>Mes demandes envoyées</a>
                                    <a href="/dmd-recues" class="waves-effect">
                                        <i class="fa fa-clock-o fa-fw" aria-hidden="true"></i>Mes demandes recues</a>
                                    <a href="/prop-recues" class="waves-effect">
                                        <i class="fa fa-clock-o fa-fw" aria-hidden="true"></i>Mes propositions reçues</a>
                                    <a href="/mes_prop" class="waves-effect">
                                        <i class="fa fa-clock-o fa-fw" aria-hidden="true"></i>Mes propositions envoyées</a>
                                    <a href="/mes_objets" class="waves-effect">
                                        <i class="fa fa-clock-o fa-fw" aria-hidden="true"></i>Gerer mes objets</a>
                                    <a href="/mes_propositions" class="waves-effect">
                                        <i class="fa fa-clock-o fa-fw" aria-hidden="true"></i>Gerer mes propositions</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <!-- ============================================================== -->
                    <!-- End Left Sidebar -->
                    <!-- ============================================================== -->
                    <!-- ============================================================== -->
                    <!-- Page Content -->
                    <!-- ============================================================== -->
                    <div id="page-wrapper">
                        <div class="container-fluid">
                            <div class="row bg-title">
                                <div class="col-lg-3 col-md-4 col-sm-4 col-xs-12">
                                    <h4 class="page-title">Details demande</h4> 
                                </div>
                                <div class="col-lg-9 col-sm-8 col-md-8 col-xs-12">
                                    <ol class="breadcrumb">
                                        <li>
                                            <a href="/">Troc</a>
                                        </li>
                                        <li class="active">details demande</li>
                                    </ol>
                                </div>
                            </div>
                            <!-- /.row -->

                            <!-- .row -->
                            <div class="row">
                                <div class="col-md-8 col-md-offset-2 col-xs-12">
                                    <div class="row">
                                        <form class="form-horizontal">
                                            <div class="form-group">
                                                <label class="col-sm-5 control-label">Demande recue par</label>
                                                <div class="col-sm-7">
                                                    <p class="form-control-static"> 
                                                        <xsl:value-of select="Fichier/Header/NmIE"/>
                                                    </p>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-5 control-label">Mail de l'emmeteur</label>
                                                <div class="col-sm-7">
                                                    <p class="form-control-static">
                                                        <xsl:value-of select="Fichier/Header/MailExp"/>
                                                    </p>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-5 control-label">Titre de la proposition</label>
                                                <div class="col-sm-7">
                                                    <p class="form-control-static">
                                                        <xsl:value-of select="Fichier/Body/CollMess/Message/Prop/TitreP"/>
                                                    </p>
                                                </div>
                                            </div>
                                            <h3> Description de l'offre</h3>
                                            
                                            <xsl:for-each select="Fichier/Body/CollMess/Message/Prop/Offre/Objet">
                                                <ul>
                                                    <li>
                                                        
                                                        
                                                        <p class="form-control-static">
                                                            <b>Type d'objet proposé : </b>
                                                            <xsl:value-of select="Type"/>
                                                        </p>
                                                        <ul>
                                                            <xsl:for-each select="Description/Parametre">
                                                                <li>
                                                                    
                                                                    <p class="form-control-static">
                                                                        <b>Nom de la caracteristique :</b>
                                                                        <xsl:value-of select="Nom"/>
                                                                    </p>
                                                                </li>
                                                                <li>
                                                                    
                                                                    <p class="form-control-static">
                                                                        <b>Valeur de la caracteristique : </b>
                                                                        <xsl:value-of select="Valeur"/>
                                                                    </p>
                                                                </li>
                                                            </xsl:for-each>
                                                        </ul>
                                                    </li>
                                                </ul>
                                            </xsl:for-each>
                                            
                                            
                                            <h3> Description de la demande</h3>
                                            <xsl:for-each select="Fichier/Body/CollMess/Message/Prop/Demande/Objet">
                                                <ul>
                                                    <li>
                                                       
                                                        <p class="form-control-static">
                                                            <b>Type d'objet demandé </b>
                                                            <xsl:value-of select="Type"/>
                                                        </p>
                                                        <ul>
                                                            <xsl:for-each select="Description/Parametre">
                                                                <li>
                                                                    
                                                                    <p class="form-control-static">
                                                                        <b>Nom de la caracteristique : </b>
                                                                        <xsl:value-of select="Nom"/>
                                                                    </p>
                                                                </li>
                                                                <li>
                                                                    
                                                                    <p class="form-control-static">
                                                                        <b>Valeur de la caracteristique : </b>
                                                                        <xsl:value-of select="Valeur"/>
                                                                    </p>
                                                                </li>
                                                            </xsl:for-each>
                                                        </ul>
                                                    </li>
                                                </ul>
                                            </xsl:for-each>
                                            
                                            <p class="sr-only" id="nm-ie">
                                                <xsl:value-of select="/Fichier/Header/nmIE"/>
                                            </p>
                                            <p class="sr-only" id="nm-ir">
                                                <xsl:value-of select="/Fichier/Header/nmIR"/>
                                            </p>
                                            <p class="sr-only" id="mail-dest">
                                                <xsl:value-of select="/Fichier/Header/MailDest"/>
                                            </p>
                                            <p class="sr-only" id="mail-exp">
                                                <xsl:value-of select="/Fichier/Header/MailExp"/>
                                            </p>
                                            <p class="sr-only" id="msg-id">
                                                <xsl:value-of select="Fichier/Body/CollMess/Message/@MsgId"/>
                                            </p>
                                           
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <footer class="footer text-center"> 2017 @Copyright Troc </footer>
                        <!-- jQuery -->
                        <script src="../../js/jquery.min.js"></script>
                        <!-- Bootstrap Core JavaScript -->
                        <script src="../../js/bootstrap.min.js"></script>
                        <!-- Menu Plugin JavaScript -->
                        <script src="../../js/sidebar-nav.min.js"></script>
                        <!--slimscroll JavaScript -->
                        <script src="../../js/jquery.slimscroll.js"></script>
                        <!--Wave Effects -->
                        <script src="../../js/waves.js"></script>
                        <!-- Custom Theme JavaScript -->
                        <script src="../../js/custom.min.js"></script>
                        <script src="../../js/troc.js"></script>
                    </div>
                </div>


            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
