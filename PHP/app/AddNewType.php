<?php
	$whose=$_POST["whose"];
	$nazwa=$_POST["nazwa"];
	$s_d_s=$_POST["s_d_s"];
	$a_w_g=$_POST["a_w_g"];
	$c_k_p=$_POST["c_k_p"];
	$s_d_t=$_POST["s_d_t"];
	$s_d_w=$_POST["s_d_w"];
	$www=$_POST["www"];
	$conn = new mysqli("serwer1727017.home.pl", "24939152_0000008", "zserTy3$2s6", "24939152_0000008");
	$response = array();
	
	$sql="INSERT INTO FLODA_main_database(Nazwa,s_d_s,a_w_g,c_k_p,s_d_t,s_d_w,id_autora,www) values ('$nazwa','$s_d_s','$a_w_g','$c_k_p','$s_d_t','$s_d_w','$whose','$www')";
	$conn->query($sql);
	
	if($nazwa=="" || $s_d_s=="" || $a_w_g=="" || $c_k_p=="" || $s_d_t=="" || $s_d_w=="" || $www==""){
		echo("Uzupełnij wszystkie pola");
	}
	else{
		if($conn->affected_rows<1){

			echo ("Taka nazwa już istnieje");
			
		}else{
			echo ("Dodano nowy gatunek");
	}
	}	
	

?>