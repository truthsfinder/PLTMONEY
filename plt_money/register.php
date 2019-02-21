<?php
 	
 	require "db_config.php";

 	//inserting data to user
	$username = $_POST["username"];
	$password = $_POST["password"];

	$query = "INSERT INTO user (`username`, `password`) 
			  VALUES('$username', md5('$password'));";

	if(mysqli_query($con, $query)){
		echo "success";
	}else{
		echo "failed";
	}

?>