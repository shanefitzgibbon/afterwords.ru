<?php
/**
 * DocumentEditorForm class.
 * DocumentEditorForm is the data structure for keeping
 * the form data related to assigning an existing editor to a document. It is used by the 'assignEditor' action of 'DocumentController'.
 */
class DocumentEditorForm extends CFormModel
{
    /**
     * @var string username of the editor being assigned to the project
     */
    public $username;

    /**
     * @var object an instance of the Document AR model class
     */
    public $document;

    private $_user;

    /**
     * Declares the validation rules.
     * The rules state that username and password are required,
     * and password needs to be authenticated using the verify() method
     */
    public function rules()
    {
        return array(
            // username is required
            array('username', 'required'),
            //username needs to be checked for existence
            array('username', 'exist', 'className'=>'User'),
            array('username', 'verify'),
        );
    }


    /**
     * Authenticates the existence of the user in the system.
     * If valid, it will also assign the editor to the document.
     * This is the 'verify' validator as declared in rules().
     */
    public function verify($attribute,$params)
    {
        if(!$this->hasErrors())  // we only want to authenticate when no other input errors are present
        {
            $user = User::model()->findByAttributes(array('username'=>$this->username));
            if(!$user->is_editor)
            {
                $this->addError('username','This user is not an editor.');
            }
            else
            {
                $this->_user = $user;
            }
        }
    }

    public function assign()
    {
        if($this->_user instanceof User)
        {

            //assign the user, in the specified role, to the project
            $this->document->editor = $this->_user;
            $this->document->editor_id = $this._user.id;
            $this->document->save();
            return true;
        }
        else
        {
            $this->addError('username','Error when attempting to assign this editor to the project.');
            return false;
        }

    }

    /**
     * Generates an array of usernames to use for the autocomplete
     */
    public function createUsernameList()
    {
        $sql = "SELECT username FROM tbl_user WHERE is_editor <> 0";
        $command = Yii::app()->db->createCommand($sql);
        $rows = $command->queryAll();
        //format it for use with auto complete widget
        $usernames = array();
        foreach($rows as $row)
        {
            $usernames[]=$row['username'];
        }
        return $usernames;
    }
}
