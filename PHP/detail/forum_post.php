<?php
	$conn=new mysqli("serwer1727017.home.pl",'24939152_0000008','zserTy3$2s6','24939152_0000008');
	if ($mysqli->connect_errno) {
    $response["success"] = false;
    echo json_encode($response);
    die();
	}	
	$array = array();
	if(isset($_POST["ID"])){
	$ID=$_POST["ID"];
	$response=$conn->query("select cs.ID as ID,cs.Title as Title,cs.Score as Score,cs.Description as Description,cs.date as date, cs.owner_id as owner,ds.Name as name,ds.Surname as surname	from floda_forum cs left join floda_user_detail ds on cs.owner_id=ds.ID where cs.blocked=0 and cs.ID=$ID");
	while($row=$response->fetch_assoc()){
		$data[] = array(ID=>$row["ID"],title=>$row["Title"],score=>$row["Score"],description=>$row["Description"],date => $row["date"],name => $row["name"],surname => $row["surname"],owner_id => $row["owner"]);
	}
	$response=$conn->query("select cs.text as text,cs.time as time,ds.Nick as nick from floda_forum_comments cs left join floda_user_detail ds on cs.user_id=ds.ID where cs.blocked=0 and cs.id_post=$ID order by cs.ID desc");
	while($row=$response->fetch_assoc()){
		$data[] = array(title=>$row["text"],who_date=>$row["time"]."      ".$row["nick"]);
	}
	echo json_encode($data);	
	}















?>