<?php
	$whose=$_POST["whose"];
	$id_sondy=$_POST["id_sondy"];
	$name1=$_POST["name1"];
	$name2=$_POST["name2"];
	$password=$_POST["password"];
	$nazwa=$_POST["nazwa"];
	$conn = new mysqli("serwer1727017.home.pl", "24939152_0000008", "zserTy3$2s6", "24939152_0000008");
	$response = array();
	
	$sql="SELECT * FROM  `floda_sonda_settings` WHERE  `id_device` =  $id_sondy AND  `acess_password` like  md5('$password') ";
	
	$result=$conn->query($sql);
	
	//echo ("'$whose','$id_sondy','$nazwa','$name'");
	
	if($nazwa=="" || $id_sondy=="" || $password=="")
	{
		echo ('Uzupełnij wszyskie pola');
	}
	else{
		if($conn->affected_rows<1){

					echo ('Nieprawidłowy login lub hasło!');
		}else{
			if($name1=="0" && $name2=="0"){
				echo ('Wybierz ustawienia dla swojej rośliny');
			}
			else{
				if($name1!="0" && $name2!="0"){
					echo ('Możesz wybrać tylko jedno ustawienie dla swojej rośliny');
				}
				else{
					if($name1=="0"){
						$name=$name2;
					}
					else{
						$name=$name1;
					}
					$sql="INSERT INTO FLODA_connections(whose, ID_sondy, Name, ID_from_base) values ('$whose','$id_sondy','$nazwa','$name')";
					$result=$conn->query($sql);
					if($conn->affected_rows<1){

						echo ("Wystąpił nieoczekiwany błąd");
					
					}else{
						echo ('Dodano roślinę. Odśwież aplikację, aby zobaczyć jego statystki');
						
					}
				}
			}	
		}	
	}
	

 
?>