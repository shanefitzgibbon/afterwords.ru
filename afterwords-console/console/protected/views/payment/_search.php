<?php $form=$this->beginWidget('bootstrap.widgets.TbActiveForm',array(
	'action'=>Yii::app()->createUrl($this->route),
	'method'=>'get',
)); ?>

		<?php echo $form->textFieldRow($model,'id',array('class'=>'span5')); ?>

		<?php echo $form->textFieldRow($model,'status_id',array('class'=>'span5')); ?>

		<?php echo $form->textFieldRow($model,'order_id',array('class'=>'span5')); ?>

		<?php echo $form->textFieldRow($model,'amount',array('class'=>'span5','maxlength'=>13)); ?>

		<?php echo $form->textFieldRow($model,'create_time',array('class'=>'span5')); ?>

		<?php echo $form->textFieldRow($model,'create_user_id',array('class'=>'span5')); ?>

		<?php echo $form->textFieldRow($model,'update_time',array('class'=>'span5')); ?>

		<?php echo $form->textFieldRow($model,'update_user_id',array('class'=>'span5')); ?>

	<div class="form-actions">
		<?php $this->widget('bootstrap.widgets.TbButton', array(
			'buttonType' => 'submit',
			'type'=>'primary',
			'label'=>'Search',
		)); ?>
	</div>

<?php $this->endWidget(); ?>
