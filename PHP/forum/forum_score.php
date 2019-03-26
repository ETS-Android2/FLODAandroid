<?php
	$conn=new mysqli("serwer1727017.home.pl",'24939152_0000008','zserTy3$2s6','24939152_0000008');
	if ($mysqli->connect_errno) {
    $response["success"] = false;
    echo json_encode($response);
    die();
	}	
	$array = array();
	if(isset($_GET["ID"]) && isset($_GET["op"])&& isset($_GET["operacja"])){
	$ID=$_GET["ID"];
	$op = $_GET["op"];
	$ope = $_GET["operacja"];
	$response=$conn->query("select ID from floda_forum_rating where ID_post=$ID and ID_user = $op");
	if(!mysqli_num_rows($response)>0){
		$conn->query("insert into floda_forum_rating(ID_post, ID_user) values ($ID,$op)");
		if($ope==-1){
		$conn->query("update floda_forum set Score=Score-1 where ID=$ID");
		}else{
		$conn->query("update floda_forum set Score=Score+1 where ID=$ID");
		}
		die("1");
	}else{
		die("0");
	}
	}else{
		die("0");
	}














?>