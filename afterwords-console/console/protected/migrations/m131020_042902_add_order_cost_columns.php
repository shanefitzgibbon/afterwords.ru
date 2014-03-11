<?php

class m131020_042902_add_order_cost_columns extends CDbMigration
{
	public function up()
	{
        $this->addColumn('tbl_user', 'first_name', 'string NOT NULL AFTER username');
	}

	public function down()
	{
		echo "m131020_042902_add_order_cost_columns does not support migration down.\n";
		return false;
	}

	/*
	// Use safeUp/safeDown to do migration with transaction
	public function safeUp()
	{
	}

	public function safeDown()
	{
	}
	*/
}