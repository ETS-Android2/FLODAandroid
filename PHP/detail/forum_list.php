<?php
	$conn=new mysqli("serwer1727017.home.pl",'24939152_0000008','zserTy3$2s6','24939152_0000008');
	if ($mysqli->connect_errno) {
    $response["success"] = false;
    echo json_encode($response);
    die();
	}	
	$array = array();
	$search=$_POST["search"];
	$response=$conn->query("select ID,Title,Score,category from floda_forum where blocked=0 and Title like '%$search%'");
	while($row=$response->fetch_assoc()){
		$data[] = array(ID=>$row["ID"],title=>$row["Title"],score=>$row["Score"],category=>$row["category"]);
	}
	echo json_encode($data);	
	















?>