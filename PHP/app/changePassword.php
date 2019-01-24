<?php
	$mail=$_POST['mail'];
	$newPass=$_POST['newPass'];
	$verPass=$_POST['verPass'];
	
	$conn = new mysqli("serwer1727017.home.pl", "24939152_0000008", "zserTy3$2s6", "24939152_0000008");
	$sql="UPDATE `floda_user_detail` SET `passwd` = md5('$newPass') WHERE `Email`='$mail'";
	$conn->query($sql);
	
	if($newPass=="" || $verPass==""){
		echo("Wpisz oba hasła");
	}
	else{
		if($newPass!=$verPass){

			echo ("Błędne potwierdzenie hasła");
			
		}else{
			
			if($conn->affected_rows<1){

				echo ("Nowe hasło musi się różnić od starego");
			
			}else{
				echo ("Zmieniono hasło");
			} 

		} 
	}
?>