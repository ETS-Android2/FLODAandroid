<?php
	$server ='www.serwer1727017.home.pl';
	$dbname='24939152_0000008';
	$username='24939152_0000008';
	$password='zserTy3$2s6';
	
	$conn = new mysqli($server,$username,$password,$dbname);
	if($conn->connect_error){
        $response["success"] = false;
        die('Connection failed');  
	}
	if(isset($_POST["ID"])){
	$id=$_POST["ID"];
	if($conn->query("delete from FLODA_connections where ID=$id")==TRUE){
		echo("1");
	}else{
		echo("0");
	}}else{
		die("0");
	}

?>