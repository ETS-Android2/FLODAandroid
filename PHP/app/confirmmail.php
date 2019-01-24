<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Potwierdzanie maila</title>
    <link type="text/css" href="style.css" rel="stylesheet">
</head>
<body>
<div class="c">
<div class="cen">
<div class="d">
    <a href="mailto:bartoszadamczyk3@gmail.com"><?php
$mail = $_GET["mail"];
if(isset($_GET["mail"])){
$conn = new mysqli("serwer1727017.home.pl", "24939152_0000008", "zserTy3$2s6", "24939152_0000008");
$result=$conn->query("Select activated from floda_user_detail where email like '$mail' and activated=0");
if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
		$conn->query("UPDATE floda_user_detail set activated=1 where email='$mail'");
		if($conn->affected_rows>0){
        echo  "Twoje konto zostalo aktywowane";
		}else{
			echo "Błąd aktywacji (konto zostalo juz aktywowane)</div><div class='b'>Jeżeli nie jesteś w stanie rozwiazac problemu skontaktuj sie z administracją</div>";
    }
	}
} else {
    echo "Błąd operacji</div><div class='b'>Jeżeli nie jesteś w stanie rozwiazac problemu skontaktuj sie z administracją</div>";
}
}else{
	
	echo "<font color='maroon'>Błąd (brak danych)</font></div><div class='b'>Jeżeli nie jesteś w stanie rozwiazac problemu skontaktuj sie z administracją</div>";
	
}




?></a>
</div></div></div>
</body>
</html>


