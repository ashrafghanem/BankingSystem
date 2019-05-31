<?php
    $server="localhost";
    $dbuser="ashraf";
    $password="ashraf1998";
    $dbname="bankingsystem";
    $db = new mysqli($server,$dbuser,$password,$dbname);
    if(mysqli_connect_errno()){
        echo "Error Connecting to the database!";
    }


    if(isset($_REQUEST['user']) && isset($_REQUEST['pass'])) {
//        $cookie_name="LastLogIn";
//        $cookie_value=time();
//        setcookie($cookie_name,$cookie_value,86400+time());

        $user = $_REQUEST['user'];
        $pass = $_REQUEST['pass'];
        $query = "SELECT * FROM `clients` WHERE 1";
        $res = $db->query($query);
        for ($i = 0; $i < $res->num_rows; $i++) {
            $row = $res->fetch_row();
            if ($row[0] === $user && $row[1] === $pass) {
                echo "Found;$row[2]";
                return;
            }
        }
        echo "Not Found";
    }
    else if(isset($_REQUEST['addBalance']) && isset($_REQUEST['accnum']) ){
        $balance = $_REQUEST['addBalance'];
        $accNum =  $_REQUEST['accnum'];
        $query = "UPDATE CLIENTS SET BALANCE=BALANCE+".intval($balance)." WHERE ACCOUNT_NUM=".$accNum.";";
        $res = $db->query($query);
        $db->commit();

        $date = date('Y-m-d H:i:s');
        $query = "INSERT INTO `transactions`(`account_num`, `operation`, `amount`, `date`) VALUES (".$accNum.",'deposit',".$balance.",'".$date."');";
        $res = $db->query($query);
        $db->commit();
        try {
            $query = "SELECT BALANCE FROM `clients` WHERE ACCOUNT_NUM=".$accNum.";";
            $res = $db->query($query);
            $row = $res->fetch_row();
            echo "success;$row[0]";
        }catch (Exception $e){
            echo "fail";
        }
    }
    else if(isset($_REQUEST['subtractBalance']) && isset($_REQUEST['accnum']) ){
        $balance = $_REQUEST['subtractBalance'];
        $accNum =  $_REQUEST['accnum'];
        $query = "UPDATE CLIENTS SET BALANCE=BALANCE-".intval($balance)." WHERE ACCOUNT_NUM=".$accNum.";";
        $res = $db->query($query);
        $db->commit();

        $date = date('Y-m-d H:i:s');
        $query = "INSERT INTO `transactions`(`account_num`, `operation`, `amount`, `date`) VALUES (".$accNum.",'withdraw',".$balance.",'".$date."');";
        $res = $db->query($query);
        $db->commit();
        try {
            $query = "SELECT BALANCE FROM `clients` WHERE ACCOUNT_NUM=".$accNum.";";
            $res = $db->query($query);
            $row = $res->fetch_row();
            echo "success;$row[0]";
        }catch (Exception $e){
            echo "fail";
        }
    }
    else if(isset($_REQUEST['opNum']) && isset($_REQUEST['accnum'])) {
        $accNum =  $_REQUEST['accnum'];
        try {
            $query = "SELECT * FROM `transactions` WHERE ACCOUNT_NUM=" . $accNum . ";";
            $res = $db->query($query);
            $records = "";
            for ($i = 0; $i < $res->num_rows; $i++) {
                $row = $res->fetch_row();
                $records .= ($row[0] . ";" . $row[1] . ";" . $row[2] . ";" . $row[3] . "/");
            }
            echo $records;
        }catch (Exception $ex){
            echo "fail";
        }
    }
    else if(isset($_REQUEST['history']) && isset($_REQUEST['accnum'])) {
        $accNum =  $_REQUEST['accnum'];
        try {
            $query = "SELECT AMOUNT FROM `transactions` WHERE ACCOUNT_NUM=".$accNum.";";
            $res = $db->query($query);
            $records = "";
            for ($i = 0; $i < $res->num_rows; $i++) {
                $row = $res->fetch_row();
                    $records .= ($row[0] ."/");
            }
            echo $records;
        }catch (Exception $ex){
            echo "fail";
        }
    }
