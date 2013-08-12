<?php $form=$this->beginWidget('bootstrap.widgets.TbActiveForm',array(
	'id'=>'document-form',
	'enableAjaxValidation'=>false,
)); ?>

<p class="help-block">Fields with <span class="required">*</span> are required.</p>

<?php echo $form->errorSummary($model); ?>

	<?php echo $form->textFieldRow($model,'order_id',array('class'=>'span5')); ?>

	<?php echo $form->textFieldRow($model,'editor_id',array('class'=>'span5')); ?>

	<?php echo $form->textAreaRow($model,'original_text',array('rows'=>6, 'cols'=>50, 'class'=>'span8')); ?>

	<?php echo $form->textAreaRow($model,'edited_text',array('rows'=>6, 'cols'=>50, 'class'=>'span8')); ?>

<div class="form-actions">
	<?php $this->widget('bootstrap.widgets.TbButton', array(
			'buttonType'=>'submit',
			'type'=>'primary',
			'label'=>$model->isNewRecord ? 'Create' : 'Save',
		)); ?>
</div>

<?php $this->endWidget(); ?>
