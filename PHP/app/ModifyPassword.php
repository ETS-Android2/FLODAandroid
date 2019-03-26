<?php
	$id=$_POST["id"];
	$oldPass=$_POST["oldPass"];
	$newPass=$_POST["newPass"];
	$conPass=$_POST["conPass"];
	$conn = new mysqli("serwer1727017.home.pl", "24939152_0000008", "zserTy3$2s6", "24939152_0000008");
	$response = array();
	
	$sql="SELECT * FROM  `floda_user_detail` WHERE  `ID` =  $id AND  `passwd` like  md5('$oldPass') ";
	
	$result=$conn->query($sql);
	
	if($oldPass=="" || $newPass=="" || $conPass=="")
	{
		echo ('Uzupełnij wszyskie pola');
	}
	else{
		if($newPass!=$conPass){
			echo ("Nieprawidłowe potwierdzenie hasła");
		}
		else{
			if($conn->affected_rows<1){

				echo ('Niepoprawne hasło!');
			}else{
				
				$sql="UPDATE `floda_user_detail` SET `passwd` = md5('$newPass') WHERE `ID`='$id'";
				$result=$conn->query($sql);
				if($conn->affected_rows<1){

					echo ("Nowe hasło musi się różnić od starego");
			
				}else{
					echo ("Zmieniono hasło");
		
				}
			}
		}	
	}
	

 
?>