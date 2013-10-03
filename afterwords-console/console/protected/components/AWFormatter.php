<?php
/**
 * Created by JetBrains PhpStorm.
 * User: shane
 * Date: 1/10/13
 * Time: 10:25 PM
 * To change this template use File | Settings | File Templates.
 */
class AWFormatter extends CFormatter
{
    public $paymentStatusFormat=array(
        Payment::STATUS_PENDING=>'<span class="label label-warning">Pending</span>',
        Payment::STATUS_PAID=>'<span class="label label-success">Paid</span>',
        Payment::STATUS_REJECTED=>'<span class="label label-important">Rejected</span>',
    );

    public $orderStatusFormat=array(
        Order::STATUS_UNASSIGNED=>'<span class="label label-warning">Unassigned</span>',
        Order::STATUS_ASSIGNED=>'<span class="label">Assigned</span>',
        Order::STATUS_COMPLETED=>'<span class="label label-success">Complete</span>',
        Order::STATUS_DELETED=>'<span class="label label-important">Deleted</span>',
    );

    public function formatPaymentStatus($value)
    {
        if(!empty($this->paymentStatusFormat[$value])){
            return $this->paymentStatusFormat[$value];
        }
        else{
            return CHtml::encode('Unknown');
        }
    }

    public function formatOrderStatus($value)
    {
        if(!empty($this->orderStatusFormat[$value])){
            return $this->orderStatusFormat[$value];
        }
        else{
            return CHtml::encode('Unknown');
        }
//        return 'empty';
    }
}