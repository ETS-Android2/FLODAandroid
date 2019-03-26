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
	$id = $_GET["ID"];
	$tit= $_GET["title"];
	$desc= $_GET["desc"];
	$cat= $_GET["cat"];
	if(isset($_GET["ID"])&&isset($_GET["desc"])&&isset($_GET["title"])){
		$result=$conn->query("INSERT into floda_forum (Title,Description,owner_id,category) values ('$tit','$desc','$id','$cat');");
		echo $conn->insert_id;
	}else{
		echo "0";
	}

?>