<?php
	$whose=$_POST["whose"];
	$name=$_POST["name"];
	$password=$_POST["password"];
	$conn = new mysqli("serwer1727017.home.pl", "24939152_0000008", "zserTy3$2s6", "24939152_0000008");
	$response = array();
	
	//echo ("'$whose','$password','$name'");
	
	$sql="SELECT * FROM  `floda_user_detail` WHERE  `ID` =  $whose AND  `passwd` like  md5('$password') ";
	
	$result=$conn->query($sql);
	
	
	
	if($name=="Wybierz roślinę" || $password=="")
	{
		echo ('Uzupełnij wszyskie pola');
	}
	else{
		if($conn->affected_rows<1){
					echo ('Niepoprawne hasło');
		}else{
					$sql="DELETE FROM `FLODA_connections` WHERE `Name`='$name'";
					$result=$conn->query($sql);
					if($conn->affected_rows<1){

						echo ("Wystapił nieoczekiwany błąd");
					
					}else{
						echo ("Poprawnie usunięto roślinę");
						
					}
		}	
	}
	

 
?>