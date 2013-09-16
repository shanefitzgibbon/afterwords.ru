<?php

class m130812_063253_add_is_editor_column extends CDbMigration
{
	public function up()
	{
        $this->addColumn('tbl_user', 'is_editor', 'TINYINT(1) NOT NULL DEFAULT 0');
	}

	public function down()
	{
		$this->dropColumn('tbl_user', 'is_editor');
	}
}