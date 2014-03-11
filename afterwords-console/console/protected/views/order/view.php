<?php
$this->breadcrumbs=array(
	'Orders'=>array('index'),
	$model->id,
);

$this->menu=array(
array('label'=>'List Order','url'=>array('index')),
array('label'=>'Create Order','url'=>array('create')),
array('label'=>'Update Order','url'=>array('update','id'=>$model->id)),
array('label'=>'Delete Order','url'=>'#','linkOptions'=>array('submit'=>array('delete','id'=>$model->id),'confirm'=>'Are you sure you want to delete this item?')),
array('label'=>'Manage Order','url'=>array('admin')),
);
?>

<h1>View Order #<?php echo $model->id; ?></h1>

<?php
$this->widget(
    'bootstrap.widgets.TbDetailView',
    array(
        'data'=>$model,
        'attributes'=>array(
            'id',
            'status_id',
            'customer_id',
            'page_count',
            'total_cost',
            'create_time',
            'create_user_id',
            'update_time',
            'update_user_id',
        ),
    )
);

$form = $this->beginWidget('bootstrap.widgets.TbActiveForm', array(
    'id' => 'document-form',
    'enableAjaxValidation' => false,
    'enableClientValidation'=>true,
    'clientOptions'=>array(
        'validateOnSubmit'=>true,
    ),

));

$textEditor = $form->redactorRow($model->document, 'original_text', array(
    'label' => false,
    'options' => array(
        'minHeight' => 200,
        'buttons' => array(
            'formatting', '|', 'bold', 'italic', 'deleted', '|', 'alignment', '|', 'unorderedlist', 'orderedlist', 'outdent', 'indent')
    )
));

$this->widget(
    'bootstrap.widgets.TbTabs',
    array(
        'type' => 'tabs', // 'tabs' or 'pills'
        'tabs' => array(
            array(
                'label' => 'Edited Text',
                'content' => $textEditor,
                'active' => true
            ),
            array('label' => 'Original Text', 'content' => $model->document->original_text),
            array('label' => 'Analysis', 'content' => 'Analysis of the changes go here if that option was selected when ordering.'),
        ),
    )
);

$this->endWidget();
unset($form);
?>
