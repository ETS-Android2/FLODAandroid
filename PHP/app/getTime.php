<?php
	$id_sondy=$_POST["id_sondy"];
	
	$conn = new mysqli("serwer1727017.home.pl", "24939152_0000008", "zserTy3$2s6", "24939152_0000008");
	$response = array();
	$wynik = mysqli_query($conn,"SELECT `Delay_time` FROM  `floda_sonda_settings` WHERE  `id_device` =  '$id_sondy'");
	
	$row = mysqli_fetch_array($wynik);
	echo $row['Delay_time'];
?>