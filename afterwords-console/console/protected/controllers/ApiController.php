<?php
/**
 * Created by JetBrains PhpStorm.
 * User: shane
 * Date: 14/09/13
 * Time: 8:43 PM
 * To change this template use File | Settings | File Templates.
 */
class ApiController extends Controller
{
    // Members
    /**
     * Key which has to be in HTTP USERNAME and PASSWORD headers
     */
    Const APPLICATION_ID = 'AFTERWORDSRU';

    Const WORDS_PER_PAGE = 300;
    Const COST_PER_PAGE = 250;
    Const COST_OF_ANALYSIS_PER_PAGE = 700;

    /**
     * Default response format
     * either 'json' or 'xml'
     */
    private $format = 'json';

    private $allowedOrigin = 'http://afterwords.ru';

    /**
     * @return array action filters
     */
    public function filters()
    {
        return array();
    }

    // Actions
    public function actionList()
    {
    }

    public function actionView()
    {
    }

    public function actionCreate()
    {
        $this->_checkAuth();
        $user = Yii::app()->user;
        $user=User::model()->find('LOWER(username)=?',array(strtolower($_SERVER['PHP_AUTH_USER'])));
        $json = file_get_contents('php://input');
        $put_vars = CJSON::decode($json,true);  //true means use associative array

        switch ($_GET['model']) {
            case 'order':
                $this->_createOrder($put_vars['text'], $user);
                break;
            default:
                $this->_sendResponse(501,
                    sprintf('Mode <b>create</b> is not implemented for model <b>%s</b>',
                        $_GET['model']));
                Yii::app()->end();
        }

    }

    public function actionUpdate()
    {
    }

    public function actionDelete()
    {
    }

    public function actionRegister()
    {
        $json = file_get_contents('php://input');
        $put_vars = CJSON::decode($json,true);  //true means use associative array
        $model = new User;
        $model->username = $put_vars['email'];
        $model->first_name = $put_vars['firstName'];
        $model->last_name = $put_vars['lastName'];
        $model->email = $put_vars['email'];
        $model->password = $put_vars['password'];
        $model->password_repeat = $put_vars['password'];
        $model->is_editor = false;
        // Try to save the model
        if($model->save()) {
            $resultValues = array(
                'status' => 'created',
                'id' => $model->id,
                'email' => $model->email,
                'firstName' => $model->first_name,
                'lastName' => $model->last_name
            );
            $this->_sendResponse(200, CJSON::encode($resultValues));
        }
        else {
            $msg = "<h1>Error</h1>";
            $msg .= sprintf("Couldn't register user");
            $msg .= "<ul>";
            foreach($model->errors as $attribute=>$attr_errors) {
                $msg .= "<li>Attribute: $attribute</li>";
                $msg .= "<ul>";
                foreach($attr_errors as $attr_error)
                    $msg .= "<li>$attr_error</li>";
                $msg .= "</ul>";
            }
            $msg .= "</ul>";
            $this->_sendResponse(500, $msg );
        }
    }

    public function actionLogin()
    {
        $model = new UserIdentity($_SERVER['PHP_AUTH_USER'],$_SERVER['PHP_AUTH_PW']);
        $model->authenticate();
        if ($model->errorCode===UserIdentity::ERROR_NONE){
            Yii::app()->user->login($model);
            $user=User::model()->find('LOWER(username)=?',array(strtolower($model->username)));
            $resultValues = array(
                'email' => $user->email,
                'firstName' => $user->first_name,
                'lastName' => $user->last_name
            );
            $this->_sendResponse(200, CJSON::encode($resultValues));

        }
        else {
            $msg = "<h1>Error</h1>";
            $msg .= sprintf("Couldn't login user");
            $msg .= "<ul>";
            foreach($model->errors as $attribute=>$attr_errors) {
                $msg .= "<li>Attribute: $attribute</li>";
                $msg .= "<ul>";
                foreach($attr_errors as $attr_error)
                    $msg .= "<li>$attr_error</li>";
                $msg .= "</ul>";
            }
            $msg .= "</ul>";
            $this->_sendResponse(500, $msg );
        }
    }

    public function actionPreflight() {
        $content_type = 'application/json';
        $this->_sendResponse(200, '', $content_type);
    }

    private function _checkAuth()
    {
        if (!isset($_SERVER['PHP_AUTH_USER'])) {
            header('WWW-Authenticate: Basic');
            $this->_sendResponse(401, '');
        }
    }

    private function _sendResponse($status = 200, $body = '', $content_type = 'text/html')
    {
        // set the status
        $status_header = 'HTTP/1.1 ' . $status . ' ' . $this->_getStatusCodeMessage($status);
        header($status_header);
        // and the content type
        header('Content-type: ' . $content_type);

        $origin = (isset($_SERVER['HTTP_ORIGIN']) ? $_SERVER['HTTP_ORIGIN'] : null);
        if ($origin){
            if ($origin == 'http://localhost:9000' || $origin == 'http://afterwords.local:9000' || $origin == 'http://afterwords.ru' || $origin == 'http://afterwords.smfsoftware.com.au')
                $this->allowedOrigin = $origin;
                header("Access-Control-Allow-Origin: " . $this->allowedOrigin);
        }
        header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
        header("Access-Control-Allow-Headers: Content-Type, X-Requested-With, Accept, Authorization");
        header("Access-Control-Allow-Credentials: true");
        header("Access-Control-Max-Age: " + (60 * 60 * 24));

        // pages with body are easy
        if ($body != '') {
            // send the body
            echo $body;
            foreach (Yii::app()->log->routes as $route) {
                if($route instanceof CWebLogRoute) {
                    $route->enabled = false; // disable any weblogroutes
                }
            }
            Yii::app()->end();
        } // we need to create the body if none is passed
        else {
            // create some body messages
            $message = '';

            // this is purely optional, but makes the pages a little nicer to read
            // for your users.  Since you won't likely send a lot of different status codes,
            // this also shouldn't be too ponderous to maintain
            switch ($status) {
                case 401:
                    $message = 'You must be authorized to view this page.';
                    break;
                case 404:
                    $message = 'The requested URL ' . $_SERVER['REQUEST_URI'] . ' was not found.';
                    break;
                case 500:
                    $message = 'The server encountered an error processing your request.';
                    break;
                case 501:
                    $message = 'The requested method is not implemented.';
                    break;
            }

            // servers don't always have a signature turned on
            // (this is an apache directive "ServerSignature On")
            $signature = ($_SERVER['SERVER_SIGNATURE'] == '') ? $_SERVER['SERVER_SOFTWARE'] . ' Server at ' . $_SERVER['SERVER_NAME'] . ' Port ' . $_SERVER['SERVER_PORT'] : $_SERVER['SERVER_SIGNATURE'];

            // this should be templated in a real-world solution
            $body = '
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <title>' . $status . ' ' . $this->_getStatusCodeMessage($status) . '</title>
</head>
<body>
    <h1>' . $this->_getStatusCodeMessage($status) . '</h1>
    <p>' . $message . '</p>
    <hr />
    <address>' . $signature . '</address>
</body>
</html>';

            echo $body;
        }
        Yii::app()->end();
    }

    private function _getStatusCodeMessage($status)
    {
        // these could be stored in a .ini file and loaded
        // via parse_ini_file()... however, this will suffice
        // for an example
        $codes = Array(
            200 => 'OK',
            400 => 'Bad Request',
            401 => 'Unauthorized',
            402 => 'Payment Required',
            403 => 'Forbidden',
            404 => 'Not Found',
            500 => 'Internal Server Error',
            501 => 'Not Implemented',
        );
        return (isset($codes[$status])) ? $codes[$status] : '';
    }

    private function _createOrder($originalText, $customer)
    {
        $transaction = Yii::app()->db->beginTransaction();
        $order = new Order;
        $order->customer_id = $customer->id;
        $wordCount = str_word_count($originalText);
        $order->page_count = $wordCount / ApiController::WORDS_PER_PAGE;
        $order->total_cost = $order->page_count * ApiController::COST_PER_PAGE;
        $success = $order->save(false);
        $document = new Document;
        $document->original_text = $originalText;
        $document->edited_text = $originalText;
        $document->order_id = $order->id;
        $success = $success ? $document->save(false) : $success;
        $payment = new Payment;
        $payment->order_id = $order->id;
        $payment->status_id = Payment::STATUS_PENDING;
        $success = $success ? $payment->save(false) : $success;
        if ($success){
            $transaction->commit();
            $resultValues = array(
                'status' => 'created',
                'id' => $order->id,
            );
            $this->_sendResponse(200, CJSON::encode($resultValues), 'application/json');
        }
        else {
            $transaction->rollback();
            $msg = "<h1>Error</h1>";
            $msg .= sprintf("Couldn't create order");
            $this->_sendResponse(500, $msg);
        }
    }
}