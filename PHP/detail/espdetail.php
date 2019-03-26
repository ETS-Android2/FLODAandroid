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
      $result=$conn->query("select s.id_device as id,s.Delay_time as del,(select IP from floda_log where nr_floda=(SELECT ID_sondy from FLODA_connections where id=$id limit 1)  order by id desc limit 1) as ip from floda_sonda_settings s left join floda_log o on o.nr_floda=s.id_device where s.id_device=(SELECT ID_sondy from FLODA_connections where id=$id) limit 1");
      while($row =$result->fetch_array()){
        $resu=array(id=>$row["id"],del=>$row["del"],ip=>$row["ip"]);
				echo json_encode($resu);
      }
	}else{
		die("Brak danych");
	}
 
?>