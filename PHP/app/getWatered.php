<?php
	$id=$_POST["id"];
	
	$conn = new mysqli("serwer1727017.home.pl", "24939152_0000008", "zserTy3$2s6", "24939152_0000008");
	$response = array();
	$wynik = mysqli_query($conn,"SELECT `date`, `watered` FROM `floda_log` WHERE `nr_floda`= 2 ORDER BY date DESC LIMIT 1");
	
	$row = mysqli_fetch_array($wynik);
	echo $row['date'];
	echo ("\n");
	echo $row['watered'];
?>