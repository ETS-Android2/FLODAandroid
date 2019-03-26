
<?php
$server ='www.serwer1727017.home.pl';
$dbname='24939152_0000008';
$username='24939152_0000008';
$password='zserTy3$2s6';

$nr = $_GET["nr"];
$haslo =md5($_GET["haslo"]);


    $conn = new mysqli($server,$username,$password,$dbname);
    if($conn->connect_error){
        $response["success"] = false;
        die('Connection failed');
          
    }
	$wynik=$conn->query("select ss.Delay_time as dt from floda_log sd left join floda_sonda_settings ss on ss.id_device=sd.nr_floda where ss.id_device=$nr  order by ss.Date limit 1");

	while($row = $wynik->fetch_assoc()){
	echo $row["dt"];
	}
	
	
	$conn->close();

?>