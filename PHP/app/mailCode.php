<?php
	$mail=$_POST["mail"];
	
	$conn = new mysqli("serwer1727017.home.pl", "24939152_0000008", "zserTy3$2s6", "24939152_0000008");
	$response = array();
	$id=rand(1000000,9999999);
	$sql="SELECT * FROM  `floda_user_detail` WHERE  `Email` =  '$mail'";
	$conn->query($sql);
	
	if($mail=="")
	{
		echo ("0");
	}
	else{
		if($conn->affected_rows<1){
			echo("2");
		}else{
			$sql="UPDATE `floda_codes` SET `active`='0' WHERE `Email`='$mail'";
			$conn->query($sql);
		
			$conn->affected_rows;
			
		
			$sql="INSERT INTO floda_codes(ID,Type,Email,active) values ('$id','password','$mail','1')";
			$conn->query($sql);
		
			$conn->affected_rows;
			mail($mail,"Odzyskiwanie hasla","Kod identyfikacyjny: $id");
			echo ("1");
		}
	}
?>