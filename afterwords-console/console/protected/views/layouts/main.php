<?php /* @var $this Controller */ ?>
<!DOCTYPE html>
<head lang="en">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="language" content="en" />

	<link rel="stylesheet" type="text/css" href="<?php echo Yii::app()->request->baseUrl; ?>/css/main.css" />

	<title><?php echo CHtml::encode($this->pageTitle); ?></title>
</head>

<body>

<div class="container" id="page">

	<div id="mainmenu">
		<?php $this->widget('bootstrap.widgets.TbNavbar',array(
			'items'=>array(
                array(
                    'class' => 'bootstrap.widgets.TbMenu',
                    'items' => array(
                        array('label'=>'Home', 'url'=>array('/site/index')),
                        array('label'=>'Orders', 'url'=>array('/order/index')),
                        array('label'=>'Users', 'url'=>array('/user/index')),
                        array('label'=>'Payments', 'url'=>array('/payment/index')),
                        array('label'=>'Login', 'url'=>array('/site/login'), 'visible'=>Yii::app()->user->isGuest),
                        array('label'=>'Logout ('.Yii::app()->user->name.')', 'url'=>array('/site/logout'), 'visible'=>!Yii::app()->user->isGuest)
                    )
                )
			),
		)); ?>
	</div><!-- mainmenu -->

    <?php echo $content; ?>

	<div class="clear"></div>

	<footer class="footer">
        <div class="container">
		<p>Copyright &copy; <?php echo date('Y'); ?>, Afterwords.</p>
		<p>All Rights Reserved.</p>
        </div>
	</footer><!-- footer -->

</div><!-- page -->

</body>
</html>
