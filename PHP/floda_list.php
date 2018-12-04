<?php
/**
 * Created by PhpStorm.
 * User: Bartek
 * Date: 18/11/2018
 * Time: 14:56
 */
$array = array();
$conn = new mysqli("serwer1727017.home.pl", '24939152_0000008', 'zserTy3$2s6', '24939152_0000008');
if ($mysqli->connect_errno) {
    $response["success"] = false;
    echo json_encode($response);
    die();
}
if (!isset($_POST["ID"])) {
    if (!isset($_POST["ID"])) {
        $array[] = array(ID => "0");
        echo json_encode($array);
        die();
    }
}
$ID = $_POST["ID"];

$result = $conn->query("select h.ID as ID,h.Name as Name,h.ID_sondy as ID_sondy ,d.ID as idf,d.Name as latin from FLODA_connections h left join FLODA_main_database d on d.ID=h.ID_from_base where h.whose=$ID;");

while ($row = $result->fetch_assoc()) {
    $array[] = array(ID => $row["ID"], Name => $row["Name"], sonda => $row["ID_sondy"], infoid => $row["idf"], latin => $row["latin"]);
}
if ($conn->affected_rows < 1) {
    $array[] = array(ID => "0");
    echo json_encode($array);
} else {
    echo json_encode($array);
}
?>