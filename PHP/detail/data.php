<?php
	$conn=new mysqli("serwer1727017.home.pl",'24939152_0000008','zserTy3$2s6','24939152_0000008');
	if ($mysqli->connect_errno) {
    $response["success"] = false;
    echo json_encode($response);
    die();
	}	
	
	$id=$_POST["id"];
	$nr=$_POST["nr"];
    //$id=2;
    //$nr=0;
    $array =  array();
	if($nr=="0"){
	$main=$conn->query("select Nazwa,Name,s_d_s,s_d_s_x,a_w_g,a_w_g_x,c_k_p,s_d_t,s_d_t_x,s_d_w,s_d_w_x,www from dataget where id=$id limit 1");
	while($row=$main->fetch_assoc()){
	$array[]=array(Nazwa => $row["Nazwa"],s_d_s_x => $row["s_d_s_x"],a_w_g_x => $row["a_w_g_x"],s_d_t_x => $row["s_d_t_x"],s_d_w_x => $row["s_d_w_x"],s_d_s => $row["s_d_s"],name=> $row["Name"],a_w_g => $row["a_w_g"],c_k_p => $row["c_k_p"],s_d_t => $row["s_d_t"],s_d_t_x => $row["s_d_t_x"],s_d_w => $row["s_d_w"],www => $row["www"], watered => $row["watered"]);
	}
	$mainn=$conn->query("select time,round(temperature) as temperature,round(soil) as soil,round(humidity) as humidity,sun from dataget where ID=$id order by date limit 100");
	while($row=$mainn->fetch_assoc()){
	$array[]=array(time => $row["time"],temperature => $row["temperature"],soil => $row["soil"],humidity => $row["humidity"],sun => $row["sun"]);
	}
	
	}else{
	$main=$conn->query("select Nazwa,Name,s_d_s,s_d_s_x,a_w_g,a_w_g_x,c_k_p,s_d_t,s_d_t_x,s_d_w,s_d_w_x,www from dataget where id=$id limit 1");
	while($row=$main->fetch_assoc()){
		$array[]=array(Nazwa => $row["Nazwa"],s_d_s_x => $row["s_d_s_x"],a_w_g_x => $row["a_w_g_x"],s_d_t_x => $row["s_d_t_x"],s_d_w_x => $row["s_d_w_x"],s_d_s => $row["s_d_s"],name=> $row["Name"],a_w_g => $row["a_w_g"],c_k_p => $row["c_k_p"],s_d_t => $row["s_d_t"],s_d_t_x => $row["s_d_t_x"],s_d_w => $row["s_d_w"],www => $row["www"], watered => $row["watered"]);
	}
	$mainn=$conn->query("select concat(month,' ', day) as time,round(temp) as temperature,round(soil) as soil,round(humidity)  as humidity,round(sun) as sun from srednia_dniowa where nr_floda=(select ID_sondy from FLODA_connections where ID=$id) order by date limit 100");
	while($row=$mainn->fetch_assoc()){
	$array[]=array(time => $row["time"],temperature => $row["temperature"],soil => $row["soil"],humidity => $row["humidity"],sun => $row["sun"]);
	}
	
	}

echo json_encode($array);















?>