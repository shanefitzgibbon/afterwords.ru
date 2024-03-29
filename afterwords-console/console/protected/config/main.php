<?php

require_once( dirname(__FILE__) . '/../components/helpers.php');

// uncomment the following to define a path alias
// Yii::setPathOfAlias('local','path/to/local-folder');s

// This is the main Web application configuration. Any writable
// CWebApplication properties can be configured here.
return array(
	'basePath'=>dirname(__FILE__).DIRECTORY_SEPARATOR.'..',
	'name'=>'Afterwords Admin Console',

	// preloading 'log' component
	'preload'=>array(
        'log',
        'bootstrap',
    ),

	// autoloading model and component classes
	'import'=>array(
		'application.models.*',
		'application.components.*',
        'ext.AweCrud.components.*', // AweCrud components
	),

	'modules'=>array(
		// uncomment the following to enable the Gii tool

		'gii'=>array(
			'class'=>'system.gii.GiiModule',
			'password'=>'admin',
			// If removed, Gii defaults to localhost only. Edit carefully to taste.
			'ipFilters'=>array('127.0.0.1','::1'),
            'generatorPaths' => array(
                'bootstrap.gii'
            ),
		),
	),

	// application components
	'components'=>array(
		'user'=>array(
			// enable cookie-based authentication
			'allowAutoLogin'=>true,
		),
		// uncomment the following to enable URLs in path-format
		'urlManager'=>array(
            'showScriptName'=>false,
			'urlFormat'=>'path',
			'rules'=>array(
                //REST CORS pattern
                array('api/preflight', 'pattern'=>'api/*', 'verb'=>'OPTIONS'),
                //REST patterns
                array('api/login', 'pattern'=>'api/login'),
                array('api/register', 'pattern'=>'api/users/register'),
                array('api/create', 'pattern'=>'api/create/<model:\w+>', 'verb'=>'POST'),


                //Default rules
				'<controller:\w+>/<id:\d+>'=>'<controller>/view',
				'<controller:\w+>/<action:\w+>/<id:\d+>'=>'<controller>/<action>',
				'<controller:\w+>/<action:\w+>'=>'<controller>/<action>',
			),
		),
        'authManager'=>array(
            'class'=>'CDbAuthManager',
            'connectionID'=>'db',
            'itemTable' => 'tbl_auth_item',
            'itemChildTable' => 'tbl_auth_item_child',
            'assignmentTable' => 'tbl_auth_assignment',
        ),
		// uncomment the following to use a MySQL database
		'db'=>array(
			'connectionString' => 'mysql:host=localhost;mysql:port=8889;dbname=afterwords',
			'emulatePrepare' => true,
			'username' => 'root',
			'password' => 'root',
			'charset' => 'utf8',
		),
		'errorHandler'=>array(
			// use 'site/error' action to display errors
			'errorAction'=>'site/error',
		),
		'log'=>array(
			'class'=>'CLogRouter',
			'routes'=>array(
				array(
					'class'=>'CFileLogRoute',
					'levels'=>'error, warning',
				),
				// uncomment the following to show log messages on web pages
//				array(
//					'class'=>'CWebLogRoute',
//				),
			),
		),
        'messages' => array (
            'extensionPaths' => array(
                'AweCrud' => 'ext.AweCrud.messages', // AweCrud messages directory.
            ),
        ),
        'bootstrap' => array(
            'class' => 'ext.bootstrap.components.Bootstrap',
            'responsiveCss' => true,
        ),
        'format'=>array(
            'class'=>'AWFormatter',
            'dateFormat'=>'d/m/Y',
        ),
	),

	// application-level parameters that can be accessed
	// using Yii::app()->params['paramName']
	'params'=>array(
		// this is used in contact page
		'adminEmail'=>'webmaster@afterwords.ru',
	),
);