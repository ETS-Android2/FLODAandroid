<?php
	$id=$_POST["id"];
	$name=$_POST["name"];
	$surname=$_POST["surname"];
	$nick=$_POST["nick"];
	$conn = new mysqli("serwer1727017.home.pl", "24939152_0000008", "zserTy3$2s6", "24939152_0000008");
	$response = array();
	
	$sql="UPDATE `floda_user_detail` SET `Nick`='$nick',`Name`='$name',`Surname`='$surname' WHERE `ID`='$id'";
	$conn->query($sql);
	
	if($nick=="" || $name=="" || $surname==""){
		echo("Uzupełnij wszystkie pola");
	}
	else{
		if($conn->affected_rows<1){

			echo ("Nie zmieniono żadnych danych");
			
		}else{
			echo ("Zmieniono dane użytkownika");
		} 
	}
?>