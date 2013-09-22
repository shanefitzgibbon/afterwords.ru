<?php

class m130917_114223_add_user_name_columns extends CDbMigration
{

	public function up()
	{
        $this->addColumn('tbl_user', 'first_name', 'string NOT NULL AFTER username');
        $this->addColumn('tbl_user', 'last_name', 'string NOT NULL AFTER first_name');
	}

	public function down()
	{
        $this->dropColumn('tbl_user', 'first_name');
        $this->dropColumn('tbl_user', 'last_name');
	}
}