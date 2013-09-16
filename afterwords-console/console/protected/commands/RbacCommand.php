<?php
class RbacCommand extends CConsoleCommand
{

    private $_authManager;


    public function getHelp()
    {

        $description = "DESCRIPTION\n";
        $description .= '    '."This command generates an initial RBAC authorization hierarchy.\n";
        return parent::getHelp() . $description;
    }


    /**
     * The default action - create the RBAC structure.
     */
    public function actionIndex()
    {

        $this->ensureAuthManagerDefined();

        //provide the oportunity for the use to abort the request
        $message = "This command will create three roles: Admin, Editor, and Customer\n";
        $message .= " and the following permissions:\n";
        $message .= "create, read, update and delete user\n";
        $message .= "create, read, update and delete order\n";
        $message .= "create, read, update and delete document\n";
        $message .= "create, read, update and delete payment\n";
        $message .= "Would you like to continue?";

        //check the input from the user and continue if
        //they indicated yes to the above question
        if($this->confirm($message))
        {
            //first we need to remove all operations,
            //roles, child relationship and assignments
            $this->_authManager->clearAll();

            //create the lowest level operations for users
            $this->_authManager->createOperation(
                "createUser",
                "create a new user");
            $this->_authManager->createOperation(
                "readUser",
                "read user profile information");
            $this->_authManager->createOperation(
                "updateUser",
                "update a users information");
            $this->_authManager->createOperation(
                "deleteUser",
                "remove a user from a project");

            //create the lowest level operations for orders
            $this->_authManager->createOperation(
                "createOrder",
                "create a new order");
            $this->_authManager->createOperation(
                "readOrder",
                "read order information");
            $this->_authManager->createOperation(
                "updateOrder",
                "update order information");
            $this->_authManager->createOperation(
                "deleteOrder",
                "delete an order");

            //create the lowest level operations for documents
            $this->_authManager->createOperation(
                "createDocument",
                "create a new document");
            $this->_authManager->createOperation(
                "readDocument",
                "read document information");
            $this->_authManager->createOperation(
                "updateDocument",
                "update document information");
            $this->_authManager->createOperation(
                "deleteDocument",
                "delete a document");

            //create the lowest level operations for payments
            $this->_authManager->createOperation(
                "createPayment",
                "create a new payment");
            $this->_authManager->createOperation(
                "readPayment",
                "read payment information");
            $this->_authManager->createOperation(
                "updatePayment",
                "update payment information");
            $this->_authManager->createOperation(
                "deletePayment",
                "delete a payment");

            //create the customer role and add the appropriate
            //permissions as children to this role
            $role=$this->_authManager->createRole("customer");
            $role->addChild("readUser");
            $role->addChild("createOrder");
            $role->addChild("readOrder");
            $role->addChild("createDocument");
            $role->addChild("readDocument");
            $role->addChild("createPayment");
            $role->addChild("readPayment");

            //create the editor role, and add the appropriate
            $role=$this->_authManager->createRole("editor");
            $role->addChild("readUser");
            $role->addChild("readOrder");
            $role->addChild("updateOrder");
            $role->addChild("readDocument");
            $role->addChild("updateDocument");


            //create the admin role, and add the appropriate permissions,
            //as well as both the customer and editor roles as children
            $role=$this->_authManager->createRole("admin");
            $role->addChild("customer");
            $role->addChild("editor");
            $role->addChild("createUser");
            $role->addChild("updateUser");
            $role->addChild("deleteUser");
            $role->addChild("deleteOrder");
            $role->addChild("updatePayment");
            $role->addChild("deletePayment");

            //provide a message indicating success
            echo "Authorization hierarchy successfully generated.\n";
        }
        else
            echo "Operation cancelled.\n";
    }

    public function actionDelete()
    {
        $this->ensureAuthManagerDefined();
        $message = "This command will clear all RBAC definitions.\n";
        $message .= "Would you like to continue?";
        //check the input from the user and continue if they indicated
        //yes to the above question
        if($this->confirm($message))
        {
            $this->_authManager->clearAll();
            echo "Authorization hierarchy removed.\n";
        }
        else
            echo "Delete operation cancelled.\n";

    }

    protected function ensureAuthManagerDefined()
    {
        //ensure that an authManager is defined as this is mandatory for creating an auth heirarchy
        if(($this->_authManager=Yii::app()->authManager)===null)
        {
            $message = "Error: an authorization manager, named 'authManager' must be con-figured to use this command.";
            $this->usageError($message);
        }
    }
}
