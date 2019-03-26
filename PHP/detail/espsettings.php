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
  $resu= array();
	
	$id=$_GET["ID"];
	if(isset($_GET["ID"])&&$_GET["ID"]!=0){
    if(isset($_GET["time"])){
        $conn->query("update floda_sonda_settings set Delay_time=".$_GET["time"]." where id_device=(SELECT ID_sondy from FLODA_connections where id=$id)");
    }
    if(isset($_GET["pass"])){
        $conn->query("update floda_sonda_settings set acess_password=md5(\"".$_GET["pass"]."\") where id_device=(SELECT ID_sondy from FLODA_connections where id=$id) ");
    }
    		
	}else{
		die("Brak danych");
	}
 
?>