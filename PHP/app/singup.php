<?php
	if(isset($_POST["name"]) && isset($_POST["mail"])){
	$mail=$_POST["mail"];
	$name=$_POST["name"];
	$surname=$_POST["surname"];
	$password=$_POST["password"];
	$nick=$_POST["nick"];
	$conn = new mysqli("serwer1727017.home.pl", "24939152_0000008", "zserTy3$2s6", "24939152_0000008");
	$response = array();
	
	$sql="INSERT INTO floda_user_detail(Nick,Name,Surname,Email,passwd) values ('$nick','$name','$surname','$mail',md5('$password'))";
	$conn->query($sql);
	
	if($conn->affected_rows<1){

		echo ("Takie konto już istnieje");
		
	}else{
		$sql="INSERT INTO floda_user_stats(id_kto,lang,default_server) values ($last_id,'pl','http://serwer1727017.home.pl/2ti'";
	$conn->query($sql);
		mail($mail,"Witamy $nick!, w beta testach projektu FLODA!","Potwierdź swoje konto w projekcie dzięki temu łączu \n http://serwer1727017.home.pl/2ti/floda/app/confirmmail.php?mail=$mail"); 
		echo ('1');
	} 
	}else{
		echo "brak parametru";
	}
	
	

?>