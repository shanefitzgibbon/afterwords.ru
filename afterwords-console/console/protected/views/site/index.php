<?php
/* @var $this SiteController */

$this->pageTitle=Yii::app()->name.' | Home';
?>


<?php if(!Yii::app()->user->isGuest):?>
    <div class="row">
        <div class="span12">
            <p class="lead"> Welcome back, <?php echo Yii::app()->user->name; ?>.</p>
        </div>
    </div>

<div class="row">
    <div class="span12">You last logged in on <?php echo Yii::app()->user->lastLogin; ?>.</div>
</div>

<div class="row">
    <div class="span5">
        <?php $box = $this->beginWidget(
            'bootstrap.widgets.TbBox',
            array(
                'title' => 'Statistics',
                'headerIcon' => 'icon-th-list',
                'htmlOptions' => array('class' => 'bootstrap-widget-table')
            )
        );?>
        <table class="table">
            <tbody>
            <tr class="odd">
                <td class="lead">20</td>
                <td>Outstanding Orders</td>
            </tr>
            <tr class="odd">
                <td class="lead">75</td>
                <td>Pages within outstanding orders</td>
            </tr>
            <tr class="even">
                <td class="lead">5</td>
                <td>Orders pending payment</td>
            </tr>
            <tr class="odd">
                <td class="lead">10</td>
                <td>Orders without an assigned editor</td>
            </tr>
            </tbody>
        </table>
        <?php $this->endWidget(); ?>
    </div>
    <div class="span7">
        <?php $box = $this->beginWidget(
            'bootstrap.widgets.TbBox',
            array(
                'title' => 'Latest Orders',
                'headerIcon' => 'icon-th-list',
                'htmlOptions' => array('class' => 'bootstrap-widget-table')
            )
        );?>
        <table class="table">
            <thead>
            <tr>
                <th>#</th>
                <th>Username</th>
                <th>Order date</th>
                <th>Assigned Editor</th>
                <th>Payment Status</th>
            </tr>
            </thead>
            <tbody>
            <tr class="odd">
                <td style="width: 60px">1</td>
                <td>Mark</td>
                <td>Otto</td>
                <td>CSS</td>
                <td><span class="label label-warning">Pending</span></td>
            </tr>
            <tr class="even">
                <td style="width: 60px">2</td>
                <td>Jacob</td>
                <td>Thornton</td>
                <td>JavaScript</td>
                <td><span class="label label-success">Paid</span></td>
            </tr>
            <tr class="odd">
                <td style="width: 60px">3</td>
                <td>Stu</td>
                <td>Dent</td>
                <td>HTML</td>
                <td><span class="label label-important">Failed</span></td>
            </tr>
            </tbody>
        </table>
        <?php $this->endWidget(); ?>
    </div>
</div>
<?php endif;?>
