<?php
/**
 * Created by PhpStorm.
 * User: Bartek
 * Date: 18/11/2018
 * Time: 14:56
 */

$conn = new mysqli("serwer1727017.home.pl",'24939152_0000008','zserTy3$2s6','24939152_0000008');
$login=$_POST["login"];
$passwd = $_POST["password"];
if ($mysqli->connect_errno) {
        $response["success"] = false;
        echo json_encode($response);
        exit();
    }
$ID=null;
$result=$conn->query("select s.ID as ID,a.lang as lang from floda_user_detail s left join floda_user_stats a on a.id_kto=s.ID where blocked=0 and activated=1 and Nick='$login' and passwd=md5('$passwd')");
$array =  array();
while($row=$result->fetch_assoc()){
    $array[]=array(id=>$row["ID"],lang=>$row["lang"]);
}
if($conn->affected_rows<1){
	$array[]=array(id=>"0");
    echo json_encode($array);
}else {
    echo json_encode($array);
}
?>