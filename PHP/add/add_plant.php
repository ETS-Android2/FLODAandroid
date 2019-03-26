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
	$id_genre=$_POST["id_gatunku"];
	$id=$_POST["log"];
	$pass=$_POST["has"];
	$name=$_POST["name"];
	$id_uzyt=$_POST["id_autora"];
	$foo="0";
	$result=$conn->query("select count(*) as c from floda_sonda_settings where id_device=$id and acess_password= md5('$pass');");
	while($row = $result->fetch_array()){
			$foo = $row["c"];
	}
	if($foo=="1"){
	if($conn->query("Insert into FLODA_connections (whose,ID_sondy,Name,ID_from_base) values ($id_uzyt,$id,'$name',$id_genre)")==TRUE){
		echo ("1");
	}else{
		echo ("0");
	}
	}else{
		echo ("0");
	}
	
	/*while($row = $result->fetch_array()){
			echo $row["c"];

	}*/



?>