
<?php
$server ='www.serwer1727017.home.pl';
$dbname='24939152_0000008';
$username='24939152_0000008';
$password='zserTy3$2s6';

$nr = $_GET["nr"];
$haslo = md5($_GET["haslo"]);


    $conn = new mysqli($server,$username,$password,$dbname);
    if($conn->connect_error){
        $response["success"] = false;
        die('Connection failed');
          
    }
    if (!empty($_SERVER['HTTP_CLIENT_IP'])) {
            $ip = $_SERVER['HTTP_CLIENT_IP'];
        } elseif (!empty($_SERVER['HTTP_X_FORWARDED_FOR'])) {
            $ip = $_SERVER['HTTP_X_FORWARDED_FOR'];
        } else {
            $ip = $_SERVER['REMOTE_ADDR'];
}
	if($nr!=NULL && $haslo!=NULL){
		$sql="select arg11, arg12,arg21  from floda_log sd left join floda_sonda_settings ss on ss.id_device=sd.nr_floda where sd.nr_floda=$nr and sd.haslo='$haslo' order by ss.Date limit 1";
		$conn->query($sql);
		$wynik=$conn->query($sql);
	while($row = $wynik->fetch_assoc()){
	$array[]=array(arg11=>$row["arg11"],arg12=>$row["arg12"],arg21=>$row["arg21"]);
	}
	}else{
	die ('brak danych');
	}
	$conn->close();
	echo json_encode($array);

?>