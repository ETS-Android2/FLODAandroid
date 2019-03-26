<?php
	$conn=new mysqli("serwer1727017.home.pl",'24939152_0000008','zserTy3$2s6','24939152_0000008');
	if ($mysqli->connect_errno) {
    $response["success"] = false;
    echo json_encode($response);
    die();
	}	
	$array = array();
	if(isset($_POST["ID_post"])&&isset($_POST["text"])&&isset($_POST["usr_id"])){
	$ID_post=$_POST["ID_post"];
	$text=$_POST["text"];
	$usr_id=$_POST["usr_id"];
	$conn->query("insert into floda_forum_comments (text,user_id,id_post) values ('$text','$usr_id','$ID_post')");
	echo json_encode($conn->insert_id());	
	}















?>