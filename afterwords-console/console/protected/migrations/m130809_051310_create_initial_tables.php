<?php

class m130809_051310_create_initial_tables extends CDbMigration
{

	public function safeUp()
	{
        $this->createTable('tbl_user', array(
            'id' => 'pk',
            'username' => 'string NOT NULL',
            'email' => 'string NOT NULL',
            'password' => 'string NOT NULL',
            'last_login_time' => 'datetime DEFAULT NULL',
            'create_time' => 'datetime DEFAULT NULL',
            'create_user_id' => 'int(11) DEFAULT NULL',
            'update_time' => 'datetime DEFAULT NULL',
            'update_user_id' => 'int(11) DEFAULT NULL',
        ), 'ENGINE=InnoDB');

        $this->createTable('tbl_order', array(
            'id' => 'pk',
            'status_id' => 'int(11) DEFAULT NULL',
            'customer_id' => 'int(11) DEFAULT NULL',
            'page_count' => 'int(11) DEFAULT NULL',
            'total_cost' => 'DECIMAL(13, 2) DEFAULT NULL',
            'create_time' => 'datetime DEFAULT NULL',
            'create_user_id' => 'int(11) DEFAULT NULL',
            'update_time' => 'datetime DEFAULT NULL',
            'update_user_id' => 'int(11) DEFAULT NULL',
        ), 'ENGINE=InnoDB');

        $this->createTable('tbl_document', array(
            'id' => 'pk',
            'order_id' => 'int(11) DEFAULT NULL',
            'editor_id' => 'int(11) DEFAULT NULL',
            'original_text' => 'text NOT NULL',
            'edited_text' => 'text NOT NULL',
            'create_time' => 'datetime DEFAULT NULL',
            'create_user_id' => 'int(11) DEFAULT NULL',
            'update_time' => 'datetime DEFAULT NULL',
            'update_user_id' => 'int(11) DEFAULT NULL',
        ), 'ENGINE=InnoDB');

        $this->createTable('tbl_payment', array(
            'id' => 'pk',
            'status_id' => 'int(11) DEFAULT NULL',
            'order_id' => 'int(11) DEFAULT NULL',
            'amount' => 'DECIMAL(13, 2) DEFAULT NULL',
            'create_time' => 'datetime DEFAULT NULL',
            'create_user_id' => 'int(11) DEFAULT NULL',
            'update_time' => 'datetime DEFAULT NULL',
            'update_user_id' => 'int(11) DEFAULT NULL',
        ), 'ENGINE=InnoDB');

        //foreign key relationships

        //the tbl_order.customer_id is a reference to tbl_user.id
        $this->addForeignKey("fk_order_customer", "tbl_order", "customer_id", "tbl_user", "id", "CASCADE", "RESTRICT");

        //the tbl_document.editor_id is a reference to tbl_user.id
        $this->addForeignKey("fk_document_editor", "tbl_document", "editor_id", "tbl_user", "id", "CASCADE", "RESTRICT");

        //the tbl_issue.owner_id is a reference to tbl_user.id
        $this->addForeignKey("fk_payment_order", "tbl_payment", "order_id", "tbl_order", "id", "CASCADE", "RESTRICT");

        //the tbl_issue.requester_id is a reference to tbl_user.id
        $this->addForeignKey("fk_document_order", "tbl_document", "order_id", "tbl_order", "id", "CASCADE", "RESTRICT");
	}

	public function safeDown()
	{
        $this->truncateTable('tbl_payment');
        $this->truncateTable('tbl_document');
        $this->truncateTable('tbl_order');
        $this->truncateTable('tbl_user');

        $this->dropTable('tbl_payment');
        $this->dropTable('tbl_document');
        $this->dropTable('tbl_order');
        $this->dropTable('tbl_user');
	}
}