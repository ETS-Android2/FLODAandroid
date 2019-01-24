<?php
	$server ='www.serwer1727017.home.pl';
	$dbname='24939152_0000008';
	$username='24939152_0000008';
	$password='zserTy3$2s6';
	$id=$_POST["log"];
	$pass=$_POST["has"];
	$conn = new mysqli($server,$username,$password,$dbname);
	if($conn->connect_error){
        $response["success"] = false;
        die('Connection failed');  
    }
	$result=$conn->query("select count(*) as c from floda_sonda_settings where id_device=$id and acess_password= md5('$pass');");
	while($row = $result->fetch_array()){
			echo $row["c"];

	}



?>