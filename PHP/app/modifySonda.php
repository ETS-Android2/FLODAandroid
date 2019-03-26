<?php
	$time=$_POST['time'];
	$password=$_POST['password'];
	$id_sondy=$_POST['id_sondy'];
	
	
	if($password=="" && $time==""){
		echo("1");
	}
	elseif($password==""){
		$sql="UPDATE `floda_sonda_settings` SET `delay_time`='$time' WHERE `id_device`='$id_sondy'";
		$conn = new mysqli("serwer1727017.home.pl", "24939152_0000008", "zserTy3$2s6", "24939152_0000008");
		$conn->query($sql);
		$conn->affected_rows;
		echo ("2");
	}
	elseif($time==""){
		$sql="UPDATE `floda_sonda_settings` SET `acess_password` = md5('$password') WHERE `id_device`='$id_sondy'";
		$conn = new mysqli("serwer1727017.home.pl", "24939152_0000008", "zserTy3$2s6", "24939152_0000008");
		$conn->query($sql);
		if($conn->affected_rows<1){
			echo ("3");
		}
		else{
			echo ("4");
		}
	}
	else{
		$sql="UPDATE `floda_sonda_settings` SET `acess_password` = md5('$password') WHERE `id_device`='$id_sondy'";
		$conn = new mysqli("serwer1727017.home.pl", "24939152_0000008", "zserTy3$2s6", "24939152_0000008");
		$conn->query($sql);
		if($conn->affected_rows<1){
			echo ("5");
		}
		else{
			$sql="UPDATE `floda_sonda_settings` SET `delay_time`='$time' WHERE `id_device`='$id_sondy'";
			$conn = new mysqli("serwer1727017.home.pl", "24939152_0000008", "zserTy3$2s6", "24939152_0000008");
			$conn->query($sql);
			$conn->affected_rows;
			echo ("6");
		}
	}
?>