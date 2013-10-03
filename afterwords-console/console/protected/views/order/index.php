<?php
$this->breadcrumbs=array(
	'Orders',
);

$this->menu=array(
array('label'=>'Create Order','url'=>array('create')),
array('label'=>'Manage Order','url'=>array('admin')),
);
?>

<h1>Orders</h1>

<?php

$gridColumns = array(
    array('name'=>'id', 'header'=>'#', 'htmlOptions'=>array('style'=>'width: 60px') ),
    array('name'=>'status_id', 'header' => 'Status', 'type'=>'orderStatus'),
    array('name'=>'customer.username', 'header'=>'Username'),
    array('name'=>'document.original_text', 'header'=>'Original Text', 'value'=>array($this, 'renderTruncatedText')),
    array('name'=>'create_time', 'header'=>'Date Created', 'type'=>'date'),
    array('name'=>'payment.status_id', 'header' => 'Payment Status', 'type'=>'paymentStatus'),
    array(
        'htmlOptions' => array('nowrap'=>'nowrap'),
        'class'=>'bootstrap.widgets.TbButtonColumn',
        'template' => '{view}',
        'viewButtonUrl'=>'Yii::app()->createUrl("/Order/view", array("id"=>$data["id"]))',
    )
);

$this->widget(
    'bootstrap.widgets.TbGridView',
    array(
        'dataProvider' => $dataProvider,
        'template' => "{items}",
        'columns' => $gridColumns,
    )
);