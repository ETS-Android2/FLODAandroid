<?php
/**
 * Created by PhpStorm.
 * User: Bartek
 * Date: 01/09/2018
 * Time: 23:58
 */

$server ='www.serwer1727017.home.pl';
$dbname='24939152_0000008';
$username='24939152_0000008';
$password='zserTy3$2s6';
$temperature = $_GET["temperature"];
$soil = $_GET["soil"];
$ph = $_GET["ph"];
$humidity = $_GET["humidity"];
$sun = $_GET["sun"];
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

    if($temperature!=NULL){
    $sql="INSERT INTO floda_log(`temperature`,`soil`,`ph`,`humidity`,`sun`,`IP`,`nr_floda`,`haslo`) values ('$temperature','$soil','$ph','$humidity','$sun','$ip','$nr','$haslo')";
    $conn->query($sql);
    
    }else{
      die('Brak danych');
    }
    $conn->close();
?>
		