<?php
/**
 * Created by PhpStorm.
 * User: Bartek
 * Date: 01/09/2018
 * Time: 23:58
 */

$server ='www.serwer1727017.home.pl';
$dbname='24939152_0000008';
$username='24939152_0000008';
$password='zserTy3$2s6';
$id = (int)$_POST["login"];

    $conn = new mysqli($server,$username,$password,$dbname);
    if($conn->connect_error){
        $response["success"] = false;
        die('Connection failed');
          
    }
    if (!empty($_SERVER['HTTP_CLIENT_IP'])) {
            $ip = $_SERVER['HTTP_CLIENT_IP'];
        } elseif (!empty($_SERVER['HTTP_X_FORWARDED_FOR'])) {
            $ip = $_SERVER['HTTP_X_FORWARDED_FOR'];
        } else {
            $ip = $_SERVER['REMOTE_ADDR'];
	}
	$wynik=$conn->query("select count(*) as c from floda_user_detail where ID=$id and blocked!=1 and activated!=0");
	while($row = $wynik->fetch_assoc()){
	echo $row['c'];
	}
    $conn->close();
?>
		