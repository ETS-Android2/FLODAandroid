<?php
$mail = $_GET["mail"];

$conn = new mysqli("serwer1727017.home.pl", "24939152_0000008", "zserTy3$2s6", "24939152_0000008");
$result=$conn->query("Select activated from floda_user_detail where email like '$mail' and activated=0");
if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
		$conn->query("UPDATE floda_user_detail set activated=1 where email='$mail'");
		if($conn->affected_rows>0){
        echo  ('Twoje konto zostalo aktywowane');
		}else{
			echo ('Błąd aktywacji');
    }
	}
} else {
    echo ("Błąd operacji");
}





?>