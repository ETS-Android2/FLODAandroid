<?php
	$server ='www.serwer1727017.home.pl';
	$dbname='24939152_0000008';
	$username='24939152_0000008';
	$password='zserTy3$2s6';
	
	$conn = new mysqli($server,$username,$password,$dbname);
	if($conn->connect_error){
        $response["success"] = false;
        die('Connection failed');  
	}
	$name=$_GET["name"];
	$author=$_GET["autor"];


	//TODO: NALEZY ZROBIC MIEJSCE GDZIE BEDZIE SIE WARTOSC ZE SPINNEROW AUTOMATYCZNIE ZMIENIAC!


	if(isset($_GET["name"])){
	if($conn->query("Insert into FLODA_main_database (Nazwa,id_autora) value ('$name',$author);")){
		$ID=$conn->insert_id;
		if(isset($_GET["podlewanie"])){
			$foo=$_GET["podlewanie"];
			$conn->query("update FLODA_main_database set c_k_p=$foo where ID=$ID");
		}
		if(isset($_GET["podlewanieZ"])){
			$foo=$_GET["podlewanieZ"];
			$result=$conn->query("select val1,val2 from FLODA_pre_settings where CODE = '$foo'");
			while($row = $result->fetch_array()){
				$conn->query("update FLODA_main_database set a_w_g=".$row["val1"].",a_w_g_x=".$row["val2"]." where ID=$ID"); //zakres kiedy altert ma wystapic
			}	
		}
		if(isset($_GET["naslonecznieniemin"]) AND isset($_GET["naslonecznieniemax"])){
			$foo=$_GET["naslonecznieniemin"];
			$foo2=$_GET["naslonecznieniemax"];
			$conn->query("update FLODA_main_database set s_d_s=$foo,s_d_s_x=$foo2 where ID=$ID");
		}
		if(isset($_GET["naslonecznienieZ"])){
			$foo=$_GET["naslonecznienieZ"];
			$result=$conn->query("select val1,val2 from FLODA_pre_settings where CODE = '$foo'");
			while($row = $result->fetch_array()){
				$conn->query("update FLODA_main_database set s_d_s=".$row["val1"].",s_d_s_x=".$row["val2"]." where ID=$ID"); //zakres kiedy altert ma wystapic
			}	
		}
		if(isset($_GET["tempmin"]) AND isset($_GET["tempmax"])){
			$foo=$_GET["tempmin"];
			$foo2=$_GET["tempmax"];
			$conn->query("update FLODA_main_database set s_d_t=$foo,s_d_t_x=$foo2  where ID=$ID");
		}
		if(isset($_GET["wilgmin"]) AND isset($_GET["wilgmax"])){
			$foo=$_GET["wilgmin"];
			$foo2=$_GET["wilgmax"];
			$conn->query("update FLODA_main_database set s_d_w=$foo,s_d_w_x=$foo2 where ID=$ID");
		}
		if(isset($_GET["www"])){
			$foo=$_GET["www"];
			$conn->query("update FLODA_main_database set www='$foo' where ID=$ID");
		}
		echo("z".$ID);
	}else{
		die('0');
	}
}else{
	die('0');
}
	/*while($row = $result->fetch_array()){
			echo $row["c"];

	}*/



?>