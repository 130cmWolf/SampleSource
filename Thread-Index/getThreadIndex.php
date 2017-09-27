<?php

function GUID()
{
    if (function_exists('com_create_guid') === true)
    {
        return trim(com_create_guid(), '{}');
    }

    return sprintf('%04X%04X-%04X-%04X-%04X-%04X%04X%04X', mt_rand(0, 65535), mt_rand(0, 65535), mt_rand(0, 65535), mt_rand(16384, 20479), mt_rand(32768, 49151), mt_rand(0, 65535), mt_rand(0, 65535), mt_rand(0, 65535));
}

function getNewThreadIndex()
{
    $offset = ((new DateTime())->getTimeStamp() - (new DateTime("1601-01-01T00:00:00.000Z"))->getTimeStamp());
    $hex = hex2bin(substr(sprintf("%016X", $offset *10 *1000 *1000), 0, 12) . str_replace ('-', '', GUID()));
    return base64_encode($hex);
}
function getNextThreadIndex($parent)
{
    // 親解析
    $parenthex = bin2hex(base64_decode($parent));
    $parentdate = new DateTime("1601-01-01T00:00:00.000Z");
    $parentdate->modify("+" . sprintf("%d", hexdec(substr($parenthex, 0, 12) ."0000") /10 /1000 /1000) . " seconds" );
    // 子孫解析
    $childrens = str_split(substr($parenthex, 44), 10);
    foreach ($childrens as $child) {
        $time_diff = str_repeat("0", 15) . substr(sprintf("%040s" ,base_convert($child, 16, 2)), 1, 31) . str_repeat("0", 18);
        $parentdate->modify("+" . sprintf("%d", bindec($time_diff) /10 /1000 /1000 ) . " seconds" );
    }
    // 親末裔差取得
    $c_time_offset  = ((new DateTime())->getTimeStamp() - $parentdate->getTimeStamp()) *10 *1000 *1000;
    $time_diff  = sprintf("%064s", decbin($c_time_offset));
    $binary = substr($time_diff, 23, 31);
    // 親子孫末裔結合
    return base64_encode(hex2bin($parenthex . sprintf("%010X", intval($binary, 2))));
}


$index = getNewThreadIndex();
print $index."\n";
sleep(3);
$nextindex = getNextThreadIndex($index);
print $nextindex."\n";
sleep(3);
$nextindex = getNextThreadIndex($nextindex);
print $nextindex."\n";

?>
