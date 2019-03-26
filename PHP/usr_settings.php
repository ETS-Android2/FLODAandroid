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
	
	$id=$_GET["ID"];
	if(isset($_GET["ID"])&&$_GET["ID"]!=0){
			if(isset($_GET["lang"])){
				$foo=$_GET["lang"];
				$conn->query("update floda_user_stats set lang='$foo' where id_kto=$id");
				echo ("Ble");
			}
			if(isset($_GET["password"])){
				$foo=$_GET["password"];
				$conn->query("update floda_user_stats set passwd=md5($foo) where id_kto=$id");
				echo ("Ble");
			}
	}else{
		die("Brak danych");
	}

?>