<?php $form=$this->beginWidget('bootstrap.widgets.TbActiveForm',array(
	'id'=>'order-form',
	'enableAjaxValidation'=>false,
)); ?>

<p class="help-block">Fields with <span class="required">*</span> are required.</p>

<?php echo $form->errorSummary($model); ?>

	<?php echo $form->textFieldRow($model,'status_id',array('class'=>'span5')); ?>

	<?php echo $form->textFieldRow($model,'customer_id',array('class'=>'span5')); ?>

	<?php echo $form->textFieldRow($model,'page_count',array('class'=>'span5')); ?>

	<?php echo $form->textFieldRow($model,'total_cost',array('class'=>'span5','maxlength'=>13)); ?>

<div class="form-actions">
	<?php $this->widget('bootstrap.widgets.TbButton', array(
			'buttonType'=>'submit',
			'type'=>'primary',
			'label'=>$model->isNewRecord ? 'Create' : 'Save',
		)); ?>
</div>

<?php $this->endWidget(); ?>
