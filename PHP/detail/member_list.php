<?php
	$conn=new mysqli("serwer1727017.home.pl",'24939152_0000008','zserTy3$2s6','24939152_0000008');
	if ($mysqli->connect_errno) {
    $response["success"] = false;
    echo json_encode($response);
    die();
	}	
	$array = array();
	
	$response=$conn->query("select ID, Nick,Name,Surname,Email,su from floda_user_detail where blocked=0 and activated=1");
	while($row=$response->fetch_assoc()){
		$data[] = array(name=>$row["Name"]." (".$row["Nick"].") ".$row["Surname"],ID=>$row["ID"],email=>$row["Email"],su=>$row["su"]);
	}
	echo json_encode($data);	
	















?>