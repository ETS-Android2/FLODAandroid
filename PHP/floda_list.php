<?php
/**
 * Created by PhpStorm.
 * User: Bartek
 * Date: 18/11/2018
 * Time: 14:56
 */
 header('Content-type: text/html; charset=utf8_polish_ci');
$array = array();
$conn = new mysqli("serwer1727017.home.pl", '24939152_0000008', 'zserTy3$2s6', '24939152_0000008');
if ($mysqli->connect_errno) {
    $response["success"] = false;
    echo json_encode($response);
    die();
}

if (!isset($_GET["ID"])&&!isset($_POST["ID"])) {
   
        $array[] = array(ID => "0");
        echo json_encode($array);
        die();
    
}
if(isset($_GET["ID"])){
	$ID = $_GET["ID"];
	$result=$conn->query("
select h.ID                                                                   as ID,
if((fss.Delay_time - TIMESTAMPDIFF(minute, fl.date,CURRENT_TIME)<0)||fl.date is null,'0','1')  as ison,
if(((s.humidity >= d.s_d_w and s.humidity <= d.s_d_w_x) or d.s_d_w = 0), 1,
   0)                                                                  as normwilg,
if((s.temp >= d.s_d_t and s.temp <= d.s_d_t_x) or d.s_d_t = 0, 1, 0)   as normtemp,
if((s.sun >= d.s_d_s and s.sun <= d.s_d_s_x) or d.s_d_s = 0, 1, 0)     as normsun,
if(((((s.soil >= d.a_w_g and d.a_w_g != 0) and (s.soil <= d.a_w_g_x and d.a_w_g_x != 0)) or
     (d.c_k_p - 1 >= DATEDIFF(current_timestamp, watered) and (fl.watered is not null or d.c_k_p != 0))) or
    (d.a_w_g = 0 and d.c_k_p = 0)), 1, 0)                              as normpod
from FLODA_connections h
left join FLODA_main_database d on
d.ID = h.ID_from_base
left join floda_sonda_settings fss on h.ID_sondy = fss.id_device
left join srednia_dniowa s on s.nr_floda = h.ID_sondy and s.date = (select date
                                                                    from srednia_dniowa
                                                                    where srednia_dniowa.nr_floda = h.ID_sondy
                                                                    order by date desc
                                                                    limit 1)
left join floda_log fl on fss.id_device = fl.nr_floda and fl.date = (select date
                                                                     from floda_log
                                                                     where s.nr_floda = nr_floda
                                                                     order by date desc
                                                                     limit 1)
where h.whose =$ID  and (((fss.Delay_time - TIMESTAMPDIFF(minute, fl.date,CURRENT_TIME)<0)||fl.date is null)=0 or ((s.humidity >= d.s_d_w and s.humidity <= d.s_d_w_x) or d.s_d_w = 0)=0 or (s.temp >= d.s_d_t and s.temp <= d.s_d_t_x) or d.s_d_t =0 or (s.sun >= d.s_d_s and s.sun <= d.s_d_s_x) or d.s_d_s = 0 or ((((s.soil >= d.a_w_g and d.a_w_g != 0) and (s.soil <= d.a_w_g_x and d.a_w_g_x != 0)) or
     (d.c_k_p - 1 >= DATEDIFF(current_timestamp, watered) and (fl.watered is not null or d.c_k_p != 0))) or
    (d.a_w_g = 0 and d.c_k_p = 0))=0)
group by h.ID;");
	$num=$result->num_rows;
	$conn->query("drop table d");
	    if ($num>0) {
    die("1");
} else {
    die("0");
}

}
$ID = $_POST["ID"];

$result = $conn->query("select h.ID                                                                   as ID,
h.Name                                                                 as Name,
h.ID_sondy                                                             as ID_sondy,
d.ID                                                                   as idf,
d.Nazwa                                                                as latin,
if((fss.Delay_time - TIMESTAMPDIFF(minute, fl.date,CURRENT_TIME)<0)||fl.date is null,'0','1')  as ison,
if(((s.humidity >= d.s_d_w and s.humidity <= d.s_d_w_x) or d.s_d_w = 0), 1,
   0)                                                                  as normwilg,
if((s.temp >= d.s_d_t and s.temp <= d.s_d_t_x) or d.s_d_t = 0, 1, 0)   as normtemp,
if((s.sun >= d.s_d_s and s.sun <= d.s_d_s_x) or d.s_d_s = 0, 1, 0)     as normsun,
if(((((s.soil >= d.a_w_g and d.a_w_g != 0) and (s.soil <= d.a_w_g_x and d.a_w_g_x != 0)) or
     (d.c_k_p - 1 >= DATEDIFF(current_timestamp, watered) and (fl.watered is not null or d.c_k_p != 0))) or
    (d.a_w_g = 0 and d.c_k_p = 0)), 1, 0)                              as normpod
from FLODA_connections h
left join FLODA_main_database d on
d.ID = h.ID_from_base
left join floda_sonda_settings fss on h.ID_sondy = fss.id_device
left join srednia_dniowa s on s.nr_floda = h.ID_sondy and s.date = (select date
                                                                    from srednia_dniowa
                                                                    where srednia_dniowa.nr_floda = h.ID_sondy
                                                                    order by date desc
                                                                    limit 1)
left join floda_log fl on fss.id_device = fl.nr_floda and fl.date = (select date
                                                                     from floda_log
                                                                     where s.nr_floda = nr_floda
                                                                     order by date desc
                                                                     limit 1)
where h.whose = $ID
group by h.ID;");

while ($row = $result->fetch_assoc()) {
    $array[] = array(ID => $row["ID"], Name => $row["Name"], sonda => $row["ID_sondy"], infoid => $row["idf"], latin => $row["latin"],ison=>$row["ison"], normwilg=>$row["normwilg"],normtemp=>$row["normtemp"],normpod=>$row["normpod"],normsun=>$row["normsun"],);
}
if ($conn->affected_rows < 1) {
    $array[] = array(ID => "0");
    echo json_encode($array);
} else {
    echo json_encode($array);
}

?>