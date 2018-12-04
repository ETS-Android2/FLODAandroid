<?php
/**
 * Created by PhpStorm.
 * User: Bartek
 * Date: 18/11/2018
 * Time: 14:56
 */

$conn = new mysqli("serwer1727017.home.pl",'24939152_0000008','zserTy3$2s6','24939152_0000008');
$ID=$_POST["ID"];
if ($mysqli->connect_errno) {
        $response["success"] = false;
        echo json_encode($response);
        exit();
    }
$result=$conn->query("select ID,Nick,Name,Surname,Email,su from floda_user_detail where blocked=0 and activated=1 and ID='$ID'");
$array =  array();
while($row=$result->fetch_assoc()){
    $array[]=array(ID=>$row["ID"],nick=>$row["Nick"],Name=>$row["Name"],Surname=>$row["Surname"],email=>$row["Email"],su=>$row["su"]);
}
if($conn->affected_rows<1){
	$array[]=array(id=>"0");
    echo json_encode($array);
}else {
    echo json_encode($array);
}
?>