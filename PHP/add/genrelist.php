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
	$nazwa= strtolower($_POST["szukaj"]);
	$array = array();
	$result=$conn->query("select ID,Nazwa,s_d_s,s_d_s_x,a_w_g,c_k_p,s_d_t,s_d_t_x,s_d_w,s_d_w_x from FLODA_main_database where Nazwa like '%$nazwa%' and display=1 limit 60");
	while($row = $result->fetch_array()){
		if($row["a_w_g"]!=0){
			$array[]=array(ID=>$row["ID"],Nazwa=>$row["Nazwa"],sun=>$row["s_d_s"]."-".$row["s_d_s_x"]." lux/day",soil_alert=>"min ".$row["a_w_g"]." j.",mintemp=>$row["s_d_t"]."-".$row["s_d_t_x"]."°C",humid=>$row["s_d_w"]."-".$row["s_d_w_x"]."%");
		}else{
			$array[]=array(ID=>$row["ID"],Nazwa=>$row["Nazwa"],sun=>$row["s_d_s"]."-".$row["s_d_s_x"]." lux/day",soil_alert=>"co ".$row["c_k_p"]." dni",mintemp=>$row["s_d_t"]."-".$row["s_d_t_x"]."°C",humid=>$row["s_d_w"]."-".$row["s_d_w_x"]."%");

		}
	}
	echo json_encode($array);
?>