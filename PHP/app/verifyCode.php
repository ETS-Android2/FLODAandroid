<?php
	$mail=$_POST["mail"];
	$id=$_POST["id"];

	$conn = new mysqli("serwer1727017.home.pl", "24939152_0000008", "zserTy3$2s6", "24939152_0000008");
	$response = array();
	
	$sql="SELECT * FROM  `floda_codes` WHERE  `Email` =  '$mail' AND  `ID` = '$id' AND `active`='1'";
	
	$conn->query($sql);
	
	if($id==""){
		echo("0");
	}
	else{
		if($conn->affected_rows<1){

			echo ("1");
			
		}else{
			echo ("2");
			$sql="UPDATE `floda_codes` SET `active`='0' WHERE `Email`='$mail'";
			$conn->query($sql);
			$conn->affected_rows;
		} 
	}
	
?>