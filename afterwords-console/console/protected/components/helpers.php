<?php
/**
 * Created by JetBrains PhpStorm.
 * User: shane
 * Date: 1/10/13
 * Time: 9:30 PM
 * To change this template use File | Settings | File Templates.
 */
function truncate($string, $length, $stopanywhere=false) {
    //truncates a string to a certain char length, stopping on a word if not specified otherwise.
    if (strlen($string) > $length) {
        //limit hit!
        $string = substr($string,0,($length -3));
        if ($stopanywhere) {
            //stop anywhere
            $string .= '...';
        } else{
            //stop on a word.
            $string = substr($string,0,strrpos($string,' ')).'...';
        }
    }
    return $string;
}
